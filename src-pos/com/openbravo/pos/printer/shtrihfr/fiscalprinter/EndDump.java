/*
 * EndDump.java
 *
 * Created on 2 April 2008, 18:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/*****************************************************************************

    Stop Getting Data From Dump
 
    Command:	03H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		03H. Length: 2 bytes.
    ·	Result Code (1 byte)

*****************************************************************************/
 
public final class EndDump extends PrinterCommand
{
    // in 
    private final int password;
    
    /**
     * Creates a new instance of EndDump
     */
    public EndDump(int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0x03;
    }
    
    public final String getText()
    {
        return "End dump";
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
