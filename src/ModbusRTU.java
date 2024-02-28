//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

//package ModbusRTU;

import de.re.easymodbus.modbusclient.ModbusClient;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ModbusRTU extends Thread{
    public LinkedHashMap<String,Integer> PLCin;
    public LinkedHashMap<String,Integer> PLCout;
    public int regsToRead;
    int [] responseint;
    public
    boolean connectPLC = false;

    public ModbusRTU(int numb) {
        regsToRead = numb;
    }

    public  void run() {
        boolean success = false;
        ModbusClient modbusClient = new ModbusClient();

        PLCout = new LinkedHashMap <String, Integer>(0);
        PLCin = new LinkedHashMap <String, Integer>(regsToRead);

        PLCin.put("plcDOuts",0);  // выход ПЛК транслируются в это слово D0
        PLCin.put("propAutoSet",0);  // d1
        PLCin.put("bits1",0);  //d2
        PLCin.put("DD1",4); //d3
        PLCin.put("stage",5); //d4


        for (int i = 0; i < PLCin.size(); i++) {
            System.out.println(PLCin.values().toArray()[i]);
        }


        PLCout.put("currWeight",-999); //100
        PLCout.put("controlWord",0); //101
        PLCout.put("autoSet",0);     //102
        PLCout.put("lifeWord",0);   //103
        PLCout.put("handRegSet",0);//104
        PLCout.put("maxPres",0);  //105
        PLCout.put("autoGis",0);   //106
        PLCout.put("val2",0); //107
        PLCout.put("propSet",0);     //108
        PLCout.put("FrqHandSet",0); //109 //


        while (true) {
            try {
                modbusClient.Connect("192.168.1.10", 502);
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка подключения к ПЛК");
            }


                try {
                    while (!success & modbusClient.isConnected()) {
                        responseint = modbusClient.ReadHoldingRegisters(0, regsToRead);
                        for (int i = 0; i < regsToRead; i++) {
                            PLCin.put((String) PLCin.keySet().toArray()[i],responseint[i]);
                        }
                        parsePLCIN();
                        morfPLCOUT();
                        int[] writeint = new int[PLCout.size()];
                        for (int i = 0; i < PLCout.size(); i++) {
                            writeint[i] = PLCout.get(PLCout.keySet().toArray()[i]);
                        }
                        modbusClient.WriteMultipleRegisters(100, writeint);
                        connectPLC = true;

                }
                } catch (Exception var9) {
                    var9.printStackTrace();
                    connectPLC = false;

                }

            try {
                modbusClient.Disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void morfPLCOUT() {
        PLCout.put("currWeight", Math.abs ((int) (Main.din2.weight2 )));
        int b = PLCout.get("lifeWord")+1;
        if (b>= 65500) b=0;
        PLCout.put("lifeWord",b);
        PLCout.put("propSet",(int)(Alg.propHandSet * 10.0d));
        PLCout.put("controlWord",Alg.control);
        PLCout.put("handRegSet",Math.abs((int)(Alg.RegHandSet))); //d104
        if ( Alg.maxPres > 10000.0d ) {
            PLCout.put("maxPres", 2);
        }
        else {
            PLCout.put("maxPres", 1);
        }
        PLCout.put("FrqHandSet",(int)(Alg.FrqHandSet*100d));
        PLCout.put("autoSet",(int)PROG.autoPresSet);
        PLCout.put("autoGis",(int)PROG.autoPresGis);
    }

    private void parsePLCIN() {
        Alg.do100_00 = (PLCin.get("plcDOuts")&1) == 1;
        Alg.do100_01 = (PLCin.get("plcDOuts")>>1&1) == 1;
        Alg.do100_02 = (PLCin.get("plcDOuts")>>2&1) == 1;
        Alg.do100_03 = (PLCin.get("plcDOuts")>>3&1) == 1;
        Alg.do100_04 = (PLCin.get("plcDOuts")>>4&1) == 1;
        Alg.propAutoSet = ((double)PLCin.get("propAutoSet"))/40.0d;
        Alg.DD1 = (double)PLCin.get("DD1")/10d;

        Alg.DD1Error  = (PLCin.get("bits1")&1) == 1;
        Alg.PresIsSet = (PLCin.get("bits1")>>1&1) == 1;
        Alg.filterNC  = (PLCin.get("bits1")>>2&1) == 1;
        Alg.oilTempCont = (PLCin.get("bits1")>>3&1) == 1;
        Alg.stationIsOn = (PLCin.get("bits1")>>4&1) == 1;
        Alg.PresHHErr = (PLCin.get("bits1")>>5&1) == 1;
        Alg.FilterErr = (PLCin.get("bits1")>>6&1) == 1;
        Alg.TempOilErr = (PLCin.get("bits1")>>7&1) == 1;
        Alg.stage = PLCin.get("stage");
    }
}
