import jlibmodbus.Modbus;
import jlibmodbus.exception.ModbusIOException;
import jlibmodbus.exception.ModbusNumberException;
import jlibmodbus.exception.ModbusProtocolException;
import jlibmodbus.master.ModbusMaster;
import jlibmodbus.master.ModbusMasterFactory;
import jlibmodbus.serial.*;
import jlibmodbus.utils.DataUtils;
import jlibmodbus.utils.FrameEvent;
import jlibmodbus.utils.FrameEventListener;
import jssc.SerialPortList;

import java.io.ObjectInputFilter;

/*
 * Copyright (C) 2018 "Invertor" Factory", JSC
 * All rights reserved
 *
 * This file is part of JLibModbus.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
 * or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Authors: Vladislav Kochedykov.
 * email: vladislav.kochedykov@gmail.com
 */
public class RTU extends Thread {

    public RTU(int i, String s) {
        Din_adr = i;
        com = s;
    }

    public static int swap(int value)
    {
        int b1 = (value >>  0) & 0xff;
        int b2 = (value >>  8) & 0xff;
        int b3 = (value >> 16) & 0xff;
        int b4 = (value >> 24) & 0xff;

        return b2 << 24 | b1 << 16 | b4 << 8 | b3 << 0;
    }


    private int Din_adr;
    private String com;

    public boolean stop = false;
    public double koef;
    public double weight;
    public double weight2;
    public boolean zeroCmd;
    public boolean zeroOk;
    public boolean commOk;
    public boolean commErr;
    public int diaposone = 0;


    ModbusMaster master;


    @Override
    public void run() {
        while (!isInterrupted()) {

            try {
                Modbus.setLogLevel(Modbus.LogLevel.LEVEL_RELEASE);
                SerialParameters serialParameters = new SerialParameters();
                String[] dev_list = SerialPortList.getPortNames();

                try {
                    jssc.SerialPort sp = new jssc.SerialPort(dev_list[0]);
                    sp.purgePort(0);
                    sp.purgePort(1);
                    sp.closePort();
                }
                catch (Exception e) { }

                serialParameters.setDevice(dev_list[0]);
                serialParameters.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600);
                serialParameters.setDataBits(8);
                serialParameters.setParity(SerialPort.Parity.NONE);
                serialParameters.setStopBits(1);
                SerialUtils.trySelectConnector();
                master = ModbusMasterFactory.createModbusMasterRTU(serialParameters);
                master.setResponseTimeout(1000);

            }
            catch (Exception e) {
            e.printStackTrace();
            }

            try {
                master.connect();
                System.out.println("Connected master");
            } catch (ModbusIOException e) {
                e.printStackTrace();
            }
            if (master.isConnected()) {
                // по умолчанию проверяем на 6 адрес - большой диапозон
                Din_adr = 6;
                int countTries = 0;
                for (int i = 0; i < 5; i++) {
                    try {

                        int[] buf = master.readHoldingRegisters(Din_adr, 0, 6);
                        countTries++;
                    } catch (ModbusProtocolException e) {
                        e.printStackTrace();
                    } catch (ModbusNumberException e) {
                        e.printStackTrace();
                    } catch (ModbusIOException e) {
                        e.printStackTrace();
                    }
                }

                if (countTries > 0) {
                    diaposone = 2;
                    Alg.maxPres = 125000.0d/9.80665d;
                } else {
                    try {
                        master.connect();
                    } catch (ModbusIOException e) {
                        e.printStackTrace();
                    }
                    Din_adr = 2;
                    countTries = 0;
                    for (int i = 0; i < 5; i++) {
                        try {
                            int[] buf = master.readHoldingRegisters(Din_adr, 0, 6);
                            countTries++;
                        } catch (ModbusProtocolException e) {
                            e.printStackTrace();
                        } catch (ModbusNumberException e) {
                            e.printStackTrace();
                        } catch (ModbusIOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (countTries > 0) {
                        diaposone = 1;
                        Alg.maxPres = 10000.0d/9.80665d;
                    }
                }
            }
            //int counttries = 0;

            double div;
                    try {
                        while (!stop & master.isConnected()) {
                            if (master.isConnected()) {


                                int[] buf = master.readHoldingRegisters(Din_adr, 0, 6);
                                //koef = Math.pow(10d, -1 * (double) (buf[2] >> 16&65535));
                                koef = Math.pow(10d, -1 * (double)(char) (buf[2] >> 16));
                                //weight = koef * (double) ( buf[0]>> 16&65535);
                                div = (double)(short) (buf[2]);

                                weight =  ((double)(short) ( buf[1]))*koef*div; //проверить показания
                                if (diaposone == 1) weight2 = weight/9.80665d;
                                if (diaposone == 2) weight2 = weight * 1000d / 9.80665d;
                                //System.out.println("Din"+Din_adr+" weight = "+weight);

                                if (zeroCmd) {
                                    zeroOk = false;
                                    master.writeSingleRegister(Din_adr, 26, 1);
                                    zeroOk = true;
                                    zeroCmd = false;
                                }
                                commOk = true;
                                commErr = false;
                            }
                        }
                    } catch (ModbusProtocolException e) {
                       // e.printStackTrace();
                        commOk = false;commErr = true;
                    } catch (ModbusNumberException e) {
                       // e.printStackTrace();
                        commOk = false;commErr = true;
                    } catch (ModbusIOException e) {
                       // e.printStackTrace();
                        commOk = false;commErr = true;
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        //e.printStackTrace();
                        commOk = false;commErr = true;
                    }


            try {
                master.disconnect();
                diaposone = 0;
                Alg.maxPres = 10000.0d/9.80665d;
            } catch (ModbusIOException e) {
                e.printStackTrace();
            }

            try {
                    sleep(500);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }


        }


    }



    }




