/*
 * ReadEJVersion.java
 *
 * Created on 16 January 2009, 13:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 
    Get EKLZ Version
 
    Command:	B1H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		B1H. Length: 20 bytes.
    ·	Result Code (1 byte)
    ·	EKLZ version (18 bytes) string of WIN1251 code page characters

****************************************************************************/

public final class ReadEJVersion extends PrinterCommand
{
    // in 
    private final int password;
    // out
    private String version;
    
    /**
     * Creates a new instance of ReadEJVersion
     */
    public ReadEJVersion(int password) {
        this.password = password;
    }
    public final int getCode()
    {
        return 0xB1;
    }
    
    public final String getText()
    {
        return "Read electronic journal version";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        version = in.readString(18);
    }
}
