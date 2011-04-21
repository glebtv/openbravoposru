/*
 * BeginDump.java
 *
 * Created on 2 April 2008, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 
    Get Dump
 
    Command:	01H. Length: 6 bytes.
    ·	Service Support Center specialist password, 
        or System Administrator password in case 
        Service Support Center specialist password is not defined (4 bytes)

    ·	Fiscal Printer unit code (1 byte)
        ·	01 – Fiscal Memory 1
        ·	02 – Fiscal Memory 1
        ·	03 – Clock
        ·	04 – Nonvolatile memory
        ·	05 – Fiscal Memory processor
        ·	06 – Fiscal Printer ROM
        ·	07 – Fiscal Printer RAM
 
    Answer:		01H. Length: 4 bytes.
    ·	Result Code (1 byte)
    ·	Quantity of data blocks (2 bytes)
 
****************************************************************************/

public final class BeginDump extends PrinterCommand
{
    // in
    private int password = 0;
    private int deviceCode = 0;
    // out
    private int dataBlockNumber = 0;
    
    public BeginDump(int password, int deviceCode)
    {
        this.password = password;
        this.deviceCode = deviceCode;
    }
        
    public final int getCode()
    {
        return 0x01;
    }

    public final String getText()
    {
        return "Begin dump";
    }
    
    public void encode(CommandOutputStream out)
        throws Exception
    {
        MethodParameter.checkRange(deviceCode, 0, 0xFF, "device code");
        
        out.writeInt(password);
        out.writeByte(deviceCode);
    }
    
    public void decode(CommandInputStream in)
        throws Exception
    {
        dataBlockNumber = in.readShort();
    }
    
    public int getDataBlockNumber()
    {
        return dataBlockNumber;
    }
}
