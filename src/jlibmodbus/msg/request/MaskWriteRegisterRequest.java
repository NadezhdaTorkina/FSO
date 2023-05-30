package jlibmodbus.msg.request;

import jlibmodbus.data.DataHolder;
import jlibmodbus.exception.ModbusNumberException;
import jlibmodbus.exception.ModbusProtocolException;
import jlibmodbus.msg.base.AbstractDataRequest;
import jlibmodbus.msg.base.ModbusResponse;
import jlibmodbus.msg.response.MaskWriteRegisterResponse;
import jlibmodbus.net.stream.base.ModbusInputStream;
import jlibmodbus.net.stream.base.ModbusOutputStream;
import jlibmodbus.utils.ModbusFunctionCode;

import java.io.IOException;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
final public class MaskWriteRegisterRequest extends AbstractDataRequest {

    private int maskAnd;
    private int maskOr;

    public MaskWriteRegisterRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return MaskWriteRegisterResponse.class;
    }

    /*result = ((reg & and) | (or & !and))*/
    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        MaskWriteRegisterResponse response = (MaskWriteRegisterResponse) getResponse();
        response.setStartAddress(getStartAddress());
        response.setMaskAnd(getMaskAnd());
        response.setMaskOr(getMaskOr());

        try {
            int reg = dataHolder.readHoldingRegister(getStartAddress());
            dataHolder.writeHoldingRegister(getStartAddress(), (reg & getMaskAnd()) | (getMaskOr() & (~getMaskAnd())));
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof MaskWriteRegisterResponse)) {
            return false;
        }
        MaskWriteRegisterResponse r = (MaskWriteRegisterResponse) response;
        return (r.getStartAddress() == getStartAddress()) &&
                (r.getMaskAnd() == getMaskAnd()) &&
                (r.getMaskOr() == getMaskOr());
    }

    @Override
    protected void readData(ModbusInputStream fifo) throws IOException {
        setMaskAnd(fifo.readShortBE());
        setMaskOr(fifo.readShortBE());
    }

    @Override
    public void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getMaskAnd());
        fifo.writeShortBE(getMaskOr());
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.MASK_WRITE_REGISTER.toInt();
    }

    @Override
    protected int dataSize() {
        return 4;
    }

    public int getMaskAnd() {
        return maskAnd;
    }

    public void setMaskAnd(int maskAnd) {
        this.maskAnd = maskAnd;
    }

    public int getMaskOr() {
        return maskOr;
    }

    public void setMaskOr(int maskOr) {
        this.maskOr = maskOr;
    }
}
