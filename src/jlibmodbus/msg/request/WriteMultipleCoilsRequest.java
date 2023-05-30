package jlibmodbus.msg.request;

import jlibmodbus.Modbus;
import jlibmodbus.data.DataHolder;
import jlibmodbus.exception.ModbusNumberException;
import jlibmodbus.exception.ModbusProtocolException;
import jlibmodbus.msg.base.AbstractWriteMultipleRequest;
import jlibmodbus.msg.base.ModbusResponse;
import jlibmodbus.msg.response.WriteMultipleCoilsResponse;
import jlibmodbus.net.stream.base.ModbusInputStream;
import jlibmodbus.utils.DataUtils;
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

final public class WriteMultipleCoilsRequest extends AbstractWriteMultipleRequest {

    public WriteMultipleCoilsRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return WriteMultipleCoilsResponse.class;
    }

    @Override
    protected boolean checkAddressRange(int startAddress, int quantity) {
        return Modbus.checkWriteCoilCount(quantity) &&
                Modbus.checkStartAddress(startAddress) &&
                Modbus.checkEndAddress(startAddress + quantity);
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteMultipleCoilsResponse response = (WriteMultipleCoilsResponse) getResponse();
        response.setStartAddress(getStartAddress());
        response.setQuantity(getQuantity());
        try {
            dataHolder.writeCoilRange(getStartAddress(), getCoils());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteMultipleCoilsResponse)) {
            return false;
        }
        WriteMultipleCoilsResponse r = (WriteMultipleCoilsResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getQuantity();
    }

    @Override
    public void readData(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        super.readData(fifo);
        if ((int) Math.ceil((double) getQuantity() / 8) != getByteCount()) {
            throw new ModbusNumberException("Byte count not matches quantity/8", getByteCount());
        }
        if (!checkAddressRange(getStartAddress(), getQuantity()))
            throw new ModbusNumberException("Coil count greater than max coil count", getQuantity());
        setCoils(DataUtils.toBitsArray(getBytes(), getQuantity()));
    }

    public boolean[] getCoils() {
        return DataUtils.toBitsArray(getBytes(), getQuantity());
    }

    public void setCoils(boolean[] coils) throws ModbusNumberException {
        setBytes(DataUtils.toByteArray(coils));
        setQuantity(coils.length);
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_MULTIPLE_COILS.toInt();
    }
}
