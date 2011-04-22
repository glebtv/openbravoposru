/*
 * PrintEJReportDays.java
 *
 * Created on 16 January 2009, 12:34
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
 
    Print Daily Totals Report In Daily Totals Numbers Range From EKLZ
 
    Command:	A3H. Length: 10 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Report type (1 byte) «0» – short, «1» – full
    ·	First day number (2 bytes) 0000…2100
    ·	Last day number (2 bytes) 0000…2100
 
    Answer:		A3H. Length: 2 bytes.
    ·	Result Code (1 byte)
 
    NOTE: Command execution may take up to 100 seconds.

****************************************************************************/

public final class PrintEJReportDays extends PrinterCommand
{
    // in 
    private final int password;
    private final int reportType;
    private final int dayNumber1;
    private final int dayNumber2;
    
    /**
     * Creates a new instance of PrintEJReportDays
     */
    public PrintEJReportDays(
        int password, 
        int reportType,
        int dayNumber1, 
        int dayNumber2) 
    {        
        this.password = password;
        this.reportType = reportType;
        this.dayNumber1 = dayNumber1;
        this.dayNumber2 = dayNumber2;
    }
    public final int getCode()
    {
        return 0xA3;
    }
    
    public final String getText()
    {
        return "Print electronic journal dayNumber report in dayNumber range";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeShort(dayNumber1);
        out.writeShort(dayNumber2);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
    }
}
