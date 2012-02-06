/*
 * BeginFiscalDay.java
 *
 * Created on January 16 2009, 16:29
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
 
    Open Fiscal Day
 
    Command:	E0H. Length: 5 bytes.
    ·	Operator password (4 bytes)
 
    Answer:		E0H. Length: 2 bytes.
    ·	Operator index number (1 byte) 1…30
 
    NOTE: Command opens new fiscal day in FM and changes FP mode 
    to «2 Open Fiscal Day».

****************************************************************************/

public final class BeginFiscalDay extends PrinterCommand
{
    // in 
    private final int password;
    // out
    private int operator;
    
    /**
     * Creates a new instance of BeginFiscalDay
     */
    public BeginFiscalDay(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xE0;
    }
    
    public final String getText()
    {
        return "Open fiscal day";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
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
