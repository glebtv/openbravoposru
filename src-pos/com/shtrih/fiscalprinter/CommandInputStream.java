/*
 * CommandInputStream.java
 *
 * Created on 2 April 2008, 17:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.io.ByteArrayInputStream;

public class CommandInputStream 
{
    private final String charsetName;
    private ByteArrayInputStream stream;
       
    public CommandInputStream(String charsetName)
    {
        this.charsetName = charsetName;
    }
    
    public String getCharsetName()
    {
        return charsetName;
    }
    
    public void setData(byte[] data)
    {
        stream = new ByteArrayInputStream(data);
    }
    
    public int getSize()
    {
        return stream.available();
    }
    
    public int readByte()
    {
        int B = (byte) stream.read();
        if (B < 0) {B = (int)(256 + B); }
        return  B;
    }

    public int readShort()
        throws Exception
    {
        int ch2 = readByte();
        int ch1 = readByte();
        return ((ch1 << 8) + (ch2 << 0));
    }
    
    public PrinterTime readTime()
    {
        int hour = readByte();
        int min = readByte();
        int sec = readByte();
        return new PrinterTime(hour, min, sec);
    }
    
    public PrinterDate readDate()
    {
        int day = readByte();
        int month = readByte();
        int year = readByte();
        return new PrinterDate(day, month, year);
    }
            
    public int readInt()
      throws Exception
    {
        int ch4 = readByte();
        int ch3 = readByte();
        int ch2 = readByte();
        int ch1 = readByte();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    
    public long readLong(int len)
      throws Exception
    {
        if ((len < 1)||(len > 8))
        {
            throw new IllegalArgumentException(
                "Invalid data length value");
        }
        
        long B;
        long result = 0;
        for (int i =0;i<len;i++)
        {
            B = readByte();
            result += (B << (8*i));
        }
        return result;
    }
    
    public byte[] readBytes(int len) 
    {
        byte[] b = new byte[len];
        stream.read(b, 0, len);
        return b;
    }
    
    public String readString(int len)
        throws Exception
    {
        return new String(readBytes(len), charsetName);
    }
    
    public String readString()
        throws Exception
    {
        return new String(readBytes(stream.available()), charsetName);
    }
}
