/*
 * PrintFMReportDates.java
 *
 * Created on April 2 2008, 20:40
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
 
    Print periodic daily totals fiscal report
 
    Command:	66H. Length: 12 bytes.
    ·	Tax Officer password (4 bytes)
    ·	Report type (1 byte) «0» – short, «1» – full
    ·	Date of first daily totals record in FM (3 bytes) DD-MM-YY
    ·	Date of last daily totals record in FM (3 bytes) DD-MM-YY
 
    Answer:		66H. Length: 12 bytes.
    ·	Result Code (1 byte)
    ·	Date of first daily totals record in FM (3 bytes) DD-MM-YY
    ·	Date of last daily totals record in FM (3 bytes) DD-MM-YY
    ·	Number of first daily totals record in FM (2 bytes) 0000…2100
    ·	Number of last daily totals record in FM (2 bytes) 0000…2100
 
****************************************************************************/

public final class PrintFMReportDates extends PrinterCommand
{
    // in
    private final int password;
    private final int reportType;
    private final PrinterDate date1;
    private final PrinterDate date2;
    // out
    public int sessionNumber1 = 0;
    public int sessionNumber2 = 0;
    public PrinterDate sessionDate1 = new PrinterDate();
    public PrinterDate sessionDate2 = new PrinterDate();
    
    /**
     * Creates a new instance of PrintFMReportDates
     */
    public PrintFMReportDates(int password, int reportType, 
        PrinterDate date1, PrinterDate date2) 
    {
        this.password = password;
        this.reportType = reportType;
        this.date1 = date1;
        this.date2 = date2;
    }
    
    public final int getCode()
    {
        return 0x66;
    }
    
    public final String getText()
    {
        return "Print periodic daily totals fiscal report";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeDate(date1);
        out.writeDate(date2);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        sessionDate1 = in.readDate();
        sessionDate2 = in.readDate();
        sessionNumber1 = in.readShort();
        sessionNumber2 = in.readShort();
    }
}
