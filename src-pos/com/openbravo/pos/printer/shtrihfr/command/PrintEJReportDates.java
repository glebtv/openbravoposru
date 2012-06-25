/*
 * PrintEJReportDates.java
 *
 * Created on 16 January 2009, 12:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.command;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandInputStream;
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandOutputStream;
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.PrinterDate;

/****************************************************************************
 
    Print Daily Totals Report In Dates Range From EKLZ
 
    Command:	A2H. Length: 12 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Report type (1 byte) «0» – short, «1» – full
    ·	Date of first daily totals in range (3 bytes) DD-MM-YY
    ·	Date of last daily totals in range (3 bytes) DD-MM-YY
 
    Answer:		A2H. Length: 2 bytes.
    ·	Result Code (1 byte)
 
    NOTE: Command execution may take up to 100 seconds.
 
****************************************************************************/
public final class PrintEJReportDates extends PrinterCommand {
    // in 

    private final int password;
    private final int reportType;
    private final PrinterDate date1;
    private final PrinterDate date2;

    /**
     * Creates a new instance of PrintEJReportDates
     */
    public PrintEJReportDates(
            int password,
            int reportType,
            PrinterDate date1,
            PrinterDate date2) {
        this.password = password;
        this.reportType = reportType;
        this.date1 = date1;
        this.date2 = date2;
    }

    public final int getCode() {
        return 0xA2;
    }

    public final String getText() {
        return "Print electronic journal report in date range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeDate(date1);
        out.writeDate(date2);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
