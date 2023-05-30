package jlibmodbus.msg.response;

import jlibmodbus.exception.ModbusNumberException;
import jlibmodbus.msg.base.ModbusResponse;
import jlibmodbus.net.stream.base.ModbusInputStream;
import jlibmodbus.net.stream.base.ModbusOutputStream;
import jlibmodbus.utils.ModbusFunctionCode;

import java.io.IOException;

/*
 * Copyright (C) 2017 Vladislav Y. Kochedykov
 * [https://github.com/kochedykov/jlibmodbus]
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
public class BroadcastResponse extends ModbusResponse {

    private int functionCode = ModbusFunctionCode.UNKNOWN.toInt();

    @Override
    public int getFunction() {
        return functionCode;
    }

    public void setFunction(int functionCode) {
        this.functionCode = functionCode;
    }

    @Override
    protected void readResponse(ModbusInputStream fifo) throws IOException, ModbusNumberException {
        //do nothing
    }

    @Override
    protected void writeResponse(ModbusOutputStream fifo) throws IOException {
        //do nothing
    }

    @Override
    protected int responseSize() {
        return 0;
    }
}
