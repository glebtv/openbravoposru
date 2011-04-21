/*
 * LoadGraphics.java
 *
 * Created on April 15 2008, 12:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/****************************************************************************
 
    Load Graphics In FP
 
    Command: 	C0H. Length: 46 bytes.
    ·	Operator password (4 bytes)
    ·	Graphics line number (1 byte) 0…199
    ·	Graphical data (40 bytes)
 
    Answer:		C0H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30

****************************************************************************/

public class LoadGraphics extends PrinterCommand
{
    // in
    private final int password;     // Operator password (4 bytes)
    private final int lineNumber;   // Graphics line number (1 byte) 0…199
    private final byte[] data;      // Graphical data (40 bytes)
    // out
    private int operator;
    
    /**
     * Creates a new instance of LoadGraphics
     */
    public LoadGraphics(int password, int lineNumber, 
        byte[] data) 
    {
        MethodParameter.checkRange(lineNumber, 1, 
            PrinterConst.MAX_LINES_GRAPHICS, "line number");
        
        this.password = password;
        this.lineNumber = lineNumber;
        this.data = data;
    }
    
    public final int getCode()
    {
        return 0xC0;
    }
    
    public final String getText()
    {
        return "Load graphics";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(lineNumber);
        out.writeBytes(data, 40);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
    }
    
    public int getOperator()
    {
        return operator;
    }
}

