/*
 * PrinterDevice.java
 *
 * Created on July 31 2007, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */ 

package com.shtrih.fiscalprinter;

import java.util.logging.*;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import com.shtrih.fiscalprinter.PrinterError;
   
class PrinterFrame
{
    private final static byte STX = 0x02;
    
    public static byte getCrc(byte[] data)
    {
        byte crc = (byte)data.length;
        for (int i=0;i<data.length;i++)
        {
            crc ^= data[i];
        }
        return crc;
    }

    public static byte[] encode(byte[] data)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        baos.write(STX);
        baos.write(data.length);
        baos.write(data, 0, data.length);
        baos.write(getCrc(data));
        return baos.toByteArray();
    }
}
    
public class PrinterDevice 
{
    // serial port interface
    private PrinterPort port; 
    // byte receive timeotu
    private int byteTimeout = 100;
    // constants
    private final static byte STX                   = 0x02;
    private final static byte ENQ                   = 0x05;
    private final static byte ACK                   = 0x06;
    private final static byte NAK                   = 0x15;
    // maximum counters
    private final static byte maxEnqNumber          = 10;
    private final static byte maxNakCommandNumber   = 3;
    private final static byte maxNakAnswerNumber    = 3;
    private final static byte maxAckNumber          = 3;
    
    private static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");
    
    public PrinterDevice(PrinterPort port)
    {
        this.port = port;
    }
      
    public PrinterPort getPort()
    {
        return port;
    }
    
    public int getByteTimeout()
    {
        return byteTimeout;
    }
        
    public void setByteTimeout(int value)
    {
        byteTimeout = value;
    }
    
    private static byte[] copyOf(byte[] original, int newLength) 
    {
        byte[] copy = new byte[newLength];
        System.arraycopy(original, 0, copy, 0,
                         Math.min(original.length, newLength));
        return copy;
    
    }
    
    private byte[] readAnswer(int timeout)
        throws Exception
    {
        int enqNumber = 0;
        for (;;)
        {
            port.setTimeout(timeout);
            // STX
            do{} while (port.readByte() != STX);
            // data length
            int dataLength = port.readByte() + 1;
            // command data
            byte[] commandData = port.readBytes(dataLength);
            // check CRC
            byte crc = commandData[commandData.length-1];
            commandData = copyOf(commandData, commandData.length-1);
            if (PrinterFrame.getCrc(commandData) == crc)
            {
                port.write(ACK);
                return commandData;
            } else
            {
                port.write(NAK);
                port.write(ENQ);
                int B = port.readByte();
                switch (B)
                {
                    case ACK: break;
                    case NAK: return commandData;
                default:
                    enqNumber++;
                    if (enqNumber >= maxEnqNumber)
                        throw new IOException("No connection, read: enqNumber >= maxEnqNumber");
                }
            }
        }
    }
    
    private void writeCommand(byte[] data)
        throws Exception
    {
        byte nakCommandNumber = 0;
        
       {
            port.write(data);
            switch (port.readByte())
            {
                case ACK: return;
                case NAK: 
                {
                    nakCommandNumber++;
                    break;
                }
                default: 
                    return;
            }
            if (nakCommandNumber >= maxNakCommandNumber) 
                throw new IOException("Timeout sending command");
        }
    }
       
    public byte[] send(byte[] data, int timeout)
        throws Exception
    {
        int ackNumber = 0;
        int enqNumber = 0;
        
        for (;;)
        {
            port.setTimeout(byteTimeout);
            port.write(ENQ);
            
            int B = 0;
            try
            {
                B = port.readByte();
            }
            catch (IOException e)
            {
                
            }
                
            switch (B)
            {
                case ACK: 
                {
                    readAnswer(timeout);
                    ackNumber++;
                    break;
                }
                case NAK: 
                {
                    writeCommand(PrinterFrame.encode(data));
                    return readAnswer(timeout);
                }
                default:
                {
                    Thread.sleep(100);
                    enqNumber++;
                }
            }
            
            if (ackNumber >= maxAckNumber)
                throw new IOException("Timeout reading answer");
            
            if (enqNumber >= maxEnqNumber)
                throw new IOException("No connection, send: enqNumber >= maxEnqNumber");
        }
    }
    
     public byte[] sendCommand(byte[] data, int timeout)
        throws Exception
    {
       synchronized(port.getPort().getSerialPort())
       {
           return send(data, timeout);
       }
    }
     
    public synchronized void execute(PrinterCommand command)
        throws Exception
    {
        logger.log(Level.INFO, command.getText());
        byte[] rx = sendCommand(command.encodeData(), command.getTimeout());
        command.decodeData(rx);
        if (command.getResultCode() != 0) 
        {
            String text = PrinterError.getFullText(command.getResultCode());
            logger.log(Level.SEVERE, text);
        }
   }
}
