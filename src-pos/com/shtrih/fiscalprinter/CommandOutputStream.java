/*
 * CommandOutputStream.java
 *
 * Created on 2 April 2008, 17:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.Arrays;
import java.io.ByteArrayOutputStream;


class CommandOutputStream
{
    private final String charsetName;
    private ByteArrayOutputStream stream;
    
    public CommandOutputStream(String charsetName) 
    {
        this.charsetName = charsetName;
        stream = new ByteArrayOutputStream();
    }
      
    public byte[] getData()
    {
        return stream.toByteArray();
    }
           
    public void reset()
    {
        stream.reset();
    }
  
    public void writeDate(PrinterDate date)
      throws Exception
    {
        writeByte(date.getDay());
        writeByte(date.getMonth());
        writeByte(date.getYear());
    }
    
    public void writeTime(PrinterTime time)
      throws Exception
    {
        writeByte(time.getHour());
        writeByte(time.getMin());
        writeByte(time.getSec());
    }
    
    public void writeShort(int v)
      throws Exception
    {
        stream.write((v >>>  0) & 0xFF); 	
        stream.write((v >>>  8) & 0xFF);
    }
    
    public void writeInt(int v)
      throws Exception
    {
        stream.write((v >>>  0) & 0xFF); 	
        stream.write((v >>>  8) & 0xFF);
        stream.write((v >>> 16) & 0xFF);
        stream.write((v >>> 24) & 0xFF);
    }
    
    
    public void writeLong(long v, int len)
      throws Exception
    {
        byte writeBuffer[] = new byte[len];
        for (int i=0;i<len;i++)
        {
            writeBuffer[i] = (byte)(v >>> 8*i);
        }
        stream.write(writeBuffer, 0, len);
    }

    public int size(){
        return stream.size();
    }
    
    public void writeBytes(byte[] data)
        throws Exception
    {
        stream.write(data);
    }
    
    public void writeBytes(byte[] data, int length)
        throws Exception
    {
        byte[] b = new byte[length];
        Arrays.fill(b, (byte)0);
        System.arraycopy(data, 0, b, 0, Math.min(data.length, length));
        stream.write(b);
    }
    
    public void writeBoolean(boolean v)
        throws Exception
    {
        if (v) writeByte(1); else writeByte(0);
    }
    
    public void writeString(String line, int minLen)
        throws Exception
    {
        if (line == "") line = " ";
        byte[] data = line.getBytes(charsetName);
        int len = Math.max(minLen, line.length());
        byte[] result = new byte[len];
        
        Arrays.fill(result, (byte)0);
        for(int i=0;i<data.length;i++)
        {
            result[i] = data[i];
        }
        writeBytes(result);
    }
    
    public void writeByte(int v)
      throws Exception
    {
        stream.write(v);
    }
}
