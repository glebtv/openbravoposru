/*
 * TestEJArchive.java
 *
 * Created on 16 January 2009, 13:41
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
 
    Test EKLZ Archive Structure
 
    Command:	AFH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		AFH. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/

public final class TestEJArchive extends PrinterCommand
{
    // in 
    private final int password;
    
    /**
     * Creates a new instance of TestEJArchive
     */
    public TestEJArchive(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAF;
    }
    
    public final String getText()
    {
        return "Test electronic journal archive";
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
