/*
 * CancelEJOperation.java
 *
 * Created on 16 January 2009, 13:16
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
 
    Cancel Active EKLZ Operation
 
    Command:	ACH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		ACH. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/

public final class CancelEJOperation extends PrinterCommand
{
    // in 
    private final int password;
    
    /**
     * Creates a new instance of CancelEJOperation
     */
    public CancelEJOperation(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAC;
    }
    
    public final String getText()
    {
        return "Cancel active operation";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
    }
}
