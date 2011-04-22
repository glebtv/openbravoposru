/*
 * PrinterPort.java
 *
 * Created on August 30 2007, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

import gnu.io.*;
import java.util.Vector;
import java.util.logging.*;
import java.io.IOException;
import com.openbravo.pos.printer.shtrihfr.util.Hex;
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.SharedSerialPorts;
    
public class PrinterPort 
{
    static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");
    
    private SharedSerialPort port = null;
    
    /**
     * Creates a new instance of PrinterPort
     */
    public PrinterPort() 
    {
    }
    
    public SharedSerialPort getPort()
    {
        return port;
    }
    
    public void open(String portName, int baudRate, int timeout, Object owner)
        throws Exception
    {
        logger.log(Level.INFO, "open: " + 
            String.valueOf(portName) + ", " + 
            String.valueOf(timeout));
        
        close();
        port = SharedSerialPorts.getSharedSerialPorts().openPort(
            portName, timeout, owner);
        
        SerialPort serialPort = port.getSerialPort();
        serialPort.setInputBufferSize(1024);
        serialPort.setOutputBufferSize(1024);
        serialPort.setFlowControlMode(serialPort.FLOWCONTROL_NONE);
        
        serialPort.setSerialPortParams(baudRate,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
    }
    
    public void setBaudRate(int baudRate)
    throws UnsupportedCommOperationException
    {
        SerialPort serialPort = port.getSerialPort();
        serialPort.setSerialPortParams(baudRate,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
    }
    
    public void close()
      throws Exception
    {
        SharedSerialPorts.getSharedSerialPorts().closePort(port);
        port = null;
    }
      
    public boolean isOpened()
    {
        return port != null;
    }
            
    public void setTimeout(int timeout)
        throws Exception
    {
        SerialPort serialPort = port.getSerialPort();
        serialPort.enableReceiveTimeout(timeout);
        if (!(serialPort.isReceiveTimeoutEnabled()))
        {
            throw new Exception(
                "Receive timeout is not supported by port driver!");
        }
    }
    
    public byte[] readBytes(int len)
        throws Exception
    {
        int offset = 0;
        int readLen = 0;
        byte[] data = new byte[len];
        
        SerialPort serialPort = port.getSerialPort();
        do
        {
            readLen = serialPort.getInputStream().read(data, offset, len);
            offset += readLen;
            len -= readLen;
            if (readLen <= 0) 
            {
                throw new IOException("No connection, readLen <= 0");
            }
        } while (len > 0);
        logData("<- ", data);
        return data;
    }

    private int byte2Int(byte value)
    {
        int B = value;
        if (B < 0) {B = (int)(256 + B); }
        return  B;    
    }
        
    public int readByte()
        throws Exception
    {
        byte[] data = readBytes(1);
        return byte2Int(data[0]);
    }
    
   
    private int min(int i1, int i2)
    {
        if (i1 < i2) return i1; else return i2;
    }
    
    public void logData(String prefix, byte[] data)
    {
        int linelen = 20;
        int count = (data.length + linelen -1)/linelen;
        for (int i=0;i<count;i++)
        {
            int len = min(linelen, data.length - linelen*i);
            byte b[] = new byte[len];
            System.arraycopy(data, i*linelen, b, 0, len);
            logger.log(Level.INFO, prefix + (Hex.toHex(b, b.length)).toUpperCase());
        }
    }
        
    public void write(byte[] b)
           throws IOException
    {
        logData("-> ", b);
        SerialPort serialPort = port.getSerialPort();
        serialPort.getOutputStream().write(b);
    }
    
    public void write(int b)
        throws IOException
    {
        logger.log(Level.INFO, "-> " + Hex.toHex((byte)b).toUpperCase());
        SerialPort serialPort = port.getSerialPort();
        serialPort.getOutputStream().write(b);
    }
}
