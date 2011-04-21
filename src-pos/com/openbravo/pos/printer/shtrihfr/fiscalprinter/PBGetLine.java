/*
 * PBGetLine.java
 *
 * Created on January 16 2009, 15:00
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
 
    Read print buffer line
 
    Command: 	C9H. Message length: 7 bytes.
    ·	Operator password (4 bytes)
    ·	Line number (2 bytes)
 
    Answer:		C9H. Message length: 2 + n bytes
    ·	Result code (1 byte)
    ·	Line data (n bytes)
 
****************************************************************************/

public final class PBGetLine extends PrinterCommand
{
    // in 
    private final int password;
    private final int lineNumber;
    // out
    private String lineText = new String("");
    
    /** Creates a new instance of PBGetLine */
    public PBGetLine(
        int password,
        int lineNumber) 
    {
        this.password = password;
        this.lineNumber = lineNumber;
    }
    public final int getCode()
    {
        return 0xC9;
    }
    
    public final String getText()
    {
        return "Read print buffer line";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeShort(lineNumber);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        lineText = in.readString(in.getSize());
    }
}
