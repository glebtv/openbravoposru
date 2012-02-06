/*
 * ReadEJReport.java
 *
 * Created on 16 January 2009, 14:09
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
 
    Get Data Of EKLZ Report
 
    Command:	B3H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		B3H. Length: (2+X) bytes.
    ·	Result Code (1 byte)
    ·	Report part or line (X bytes)

****************************************************************************/

public final class ReadEJReport extends PrinterCommand
{
    // in 
    private final int password;
    // out
    private String data;
    
    
    /**
     * Creates a new instance of ReadEJReport
     */
    public ReadEJReport(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xB3;
    }
    
    public final String getText()
    {
        return "Read electronic journal report line";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        data = in.readString(in.getSize());
    }
    
    public String getData()
    {
        return data;
    }
}
