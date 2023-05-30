package jlibmodbus.msg.request;

import jlibmodbus.data.DataHolder;
import jlibmodbus.exception.ModbusNumberException;
import jlibmodbus.msg.base.ModbusRequest;
import jlibmodbus.msg.base.ModbusResponse;
import jlibmodbus.msg.response.IllegalFunctionResponse;
import jlibmodbus.net.stream.base.ModbusInputStream;
import jlibmodbus.net.stream.base.ModbusOutputStream;

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
public class IllegalFunctionRequest extends ModbusRequest {

    final private int functionCode;

    public IllegalFunctionRequest(int functionCode) {
        super();

        this.functionCode = functionCode;
    }

    @Override
    protected Class getResponseClass() {
        return IllegalFunctionResponse.class;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        throw new IOException("Can't send Illegal request");
    }

    @Override
    public int requestSize() {
        return 0;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        IllegalFunctionResponse response = (IllegalFunctionResponse) getResponse();
        response.setFunctionCode(getFunction());
        return response;
    }

    @Override
    protected boolean validateResponseImpl(ModbusResponse response) {
        return response.getFunction() == getFunction();
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        //no op
    }

    @Override
    public int getFunction() {
        return functionCode;
    }
}
