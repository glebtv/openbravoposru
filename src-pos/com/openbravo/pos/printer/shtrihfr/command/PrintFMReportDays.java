/*
 * PrintFMReportDays.java
 *
 * Created on April 2 2008, 20:41
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
 
    Print fiscal report for daily totals numbers range
 
    Command:	67H. Length: 10 bytes.
    ·	Tax Officer password (4 bytes)
    ·	Report type (1 byte) «0» – short, «1» – full
    ·	Shift number of first daily totals record in FM (2 bytes) 0000…2100
    ·	Shift number of last daily totals record in FM (2 bytes) 0000…2100
 
    Answer:		67H. Length: 12 bytes.
    ·	Result Code (1 byte)
    ·	Date of first daily totals record in FM (3 bytes) DD-MM-YY
    ·	Date of last daily totals record in FM (3 bytes) DD-MM-YY
    ·	Number of first daily totals record in FM (2 bytes) 0000…2100
    ·	Number of last daily totals record in FM (2 bytes) 0000…2100
 
****************************************************************************/
public final class PrintFMReportDays extends PrinterCommand {
    // in
    private final int password;
    private final int session1;
    private final int session2;
    private final int reportType;
    // out
    public int sessionNumber1 = 0;
    public int sessionNumber2 = 0;
    public PrinterDate sessionDate1 = new PrinterDate();
    public PrinterDate sessionDate2 = new PrinterDate();

    /**
     * Creates a new instance of PrintFMReportDays
     */
    public PrintFMReportDays(
            int password, 
            int reportType,
            int session1, 
            int session2) {
        this.password = password;
        this.reportType = reportType;
        this.session1 = session1;
        this.session2 = session2;
    }

    public final int getCode() {
        return 0x67;
    }

    public final String getText() {
        return "Print fiscal report for daily totals numbers range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeShort(session1);
        out.writeShort(session2);
    }

    public final void decode(CommandInputStream in) throws Exception {
        sessionDate1 = in.readDate();
        sessionDate2 = in.readDate();
        sessionNumber1 = in.readShort();
        sessionNumber2 = in.readShort();
    }
}
