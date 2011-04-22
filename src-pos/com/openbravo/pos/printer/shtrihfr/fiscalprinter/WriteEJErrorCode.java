/*
 * WriteEJErrorCode.java
 *
 * Created on 16 January 2009, 14:39
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
 
    Return EKLZ Error Code
 
    Command:	BCH. Length: 6 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Result Code (1 byte)
 
    Answer:		BCH. Length: 2 bytes.
    ·	Result Code (1 byte)
 
    NOTE: Command can be executed only with EKLZ development kit.

****************************************************************************/

public final class WriteEJErrorCode extends PrinterCommand
{
    // in 
    private final int password;
    private final byte errorCode;
    
    /**
     * Creates a new instance of WriteEJErrorCode
     */
    public WriteEJErrorCode(
        int password,
        byte errorCode) 
    {
        this.password = password;
        this.errorCode = errorCode;
    }
    
    public final int getCode()
    {
        return 0xBC;
    }
    
    public final String getText()
    {
        return "Set EJ error code";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(errorCode);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
    }
}
