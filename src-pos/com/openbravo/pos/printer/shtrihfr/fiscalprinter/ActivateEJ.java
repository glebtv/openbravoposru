/*
 * ActivateEJ.java
 *
 * Created on 16 January 2009, 13:07
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
 
    Activate EKLZ
 
    Command:	A9H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		A9H. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/

public final class ActivateEJ extends PrinterCommand
{
    // in 
    private final int password;
    
    /**
     * Creates a new instance of ActivateEJ
     */
    public ActivateEJ(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xA9;
    }
    
    public final String getText()
    {
        return "Electronic journal activation";
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
