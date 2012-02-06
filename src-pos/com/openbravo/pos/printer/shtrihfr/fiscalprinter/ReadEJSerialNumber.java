/*
 * ReadEJSerialNumber.java
 *
 * Created on 16 January 2009, 13:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 
    Get EKLZ Serial Number
 
    Command:	ABH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		ABH. Length: 7 bytes.
    ·	Result Code (1 byte)
    ·	EKLZ Serial Number (5 bytes) 0000000000…9999999999

****************************************************************************/

public final class ReadEJSerialNumber extends PrinterCommand
{
    // in 
    private final int password;
    // out 
    private long serial;
    
    /**
     * Creates a new instance of ReadEJSerialNumber
     */
    public ReadEJSerialNumber(int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAB;
    }
    
    public final String getText()
    {
        return "Read electronic journal serial number";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        serial = in.readLong(5);
    }
}
