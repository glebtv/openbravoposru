/*
 * CloseEJArhive.java
 *
 * Created on 16 January 2009, 13:09
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
 
    Close EKLZ Archive
 
    Command:	AAH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		AAH. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/

public final class CloseEJArhive extends PrinterCommand
{
    // in 
    private final int password;
    
    /**
     * Creates a new instance of CloseEJArhive
     */
    public CloseEJArhive(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAA;
    }
    
    public final String getText()
    {
        return "Close electronic journal archive";
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
