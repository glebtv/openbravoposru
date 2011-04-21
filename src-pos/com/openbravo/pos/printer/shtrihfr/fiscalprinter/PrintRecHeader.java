/*
 * PrintRecHeader.java
 *
 * Created on January 15 2009, 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/****************************************************************************
 
    Print Receipt Header
 
    Command:	18H. Length: 37 bytes.
    ·	Operator password (4 bytes)
    ·	Receipt title (30 bytes)
    ·	Receipt number (2 bytes)
 
    Answer:		18H. Length: 5 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
    ·	Current receipt number (2 bytes)
 
****************************************************************************/

public final class PrintRecHeader extends PrinterCommand
{
    // in
    private final int password;
    private final String title;
    private final int number;
    // out
    private int operator;
    private int recNumber;
    
    /**
     * Creates a new instance of PrintRecHeader
     */
    public PrintRecHeader(int password, String title, int number) 
    {
        this.password = password;
        this.title = title;
        this.number = number;
    }
    
    public final int getCode()
    {
        return 0x18;
    }
    
    public final String getText()
    {
        return "Print receipt header";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeInt(password);
        out.writeString(title, 30);
        out.writeShort(number);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
        recNumber = in.readShort();
    }
    
    public int getOperator()
    {
        return operator;
    }
    
    public int getRecNumber()
    {
        return recNumber;
    }
    
}

