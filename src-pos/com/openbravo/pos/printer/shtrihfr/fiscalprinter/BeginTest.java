/*
 * BeginTest.java
 *
 * Created on April 2 2008, 20:12
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

/*****************************************************************************
 
    Start Test
 
    Command:	19H. Length: 6 bytes.
    ·	Operator password (4 bytes)
    ·	Test time out (1 byte) 1…99
    Answer:		19H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30

*****************************************************************************/

public class BeginTest extends PrinterCommand
{
    // in params
    private final int password;
    private final int runningPeriod;
    // out
    private int operator;
    
    
    /**
     * Creates a new instance of BeginTest
     */
    public BeginTest(int password, int runningPeriod) 
    {
        MethodParameter.checkRange(runningPeriod, 0, 0xFF, "test period");
        
        this.password = password;
        this.runningPeriod = runningPeriod;
    }
    
    public final int getCode()
    {
        return 0x19;
    }
    
    public final String getText()
    {
        return "Start test";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(runningPeriod);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
    }
}
