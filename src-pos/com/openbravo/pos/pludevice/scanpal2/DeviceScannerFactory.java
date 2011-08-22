//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.pludevice.scanpal2;

import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.util.SerialPortParameters;
import com.openbravo.pos.util.StringParser;
import gnu.io.SerialPort;

public class DeviceScannerFactory {
    
    /** Creates a new instance of DeviceScannerFactory */
    private DeviceScannerFactory() {
    }
    
    public static DeviceScanner createInstance(AppProperties props) {
        
        Integer iScannerSerialPortSpeed = 115200;
        Integer iScannerSerialPortDataBits = SerialPort.DATABITS_8;
        Integer iScannerSerialPortStopBits = SerialPort.STOPBITS_1;
        Integer iScannerSerialPortParity = SerialPort.PARITY_NONE;
        
        StringParser sd = null;
        sd = new StringParser(props.getProperty("machine.pludevice"));            
        
        if (sd == null) {
            sd = new StringParser(props.getProperty("machine.scanner"));
        }
        
        String sScannerType = sd.nextToken(':');
        String sScannerParam1 = sd.nextToken(',');
        // String sScannerParam2 = sd.nextToken(',');
        iScannerSerialPortSpeed = SerialPortParameters.getSpeed(sd.nextToken(','));
        iScannerSerialPortDataBits =  SerialPortParameters.getDataBits(sd.nextToken(','));
        iScannerSerialPortStopBits = SerialPortParameters.getStopBits(sd.nextToken(','));
        iScannerSerialPortParity = SerialPortParameters.getParity(sd.nextToken(','));          
        
        if ("scanpal2".equals(sScannerType)) {
            return new DeviceScannerComm(sScannerParam1, iScannerSerialPortSpeed, iScannerSerialPortDataBits, iScannerSerialPortStopBits, iScannerSerialPortParity);
        } else {
            return null;
        }
    }  
}
