/*
 * ReadEJReportDepartmentDates.java
 *
 * Created on 16 January 2009, 14:23
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
 
    Get Data Of EKLZ Department Report In Dates Range
 
    Command:	B6H. Length: 13 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Report type (1 byte) «0» – short, «1» – full
    ·	Department number (1 byte) 1…16
    ·	Date of first daily totals in range (3 bytes) DD-MM-YY
    ·	Date of last daily totals in range (3 bytes) DD-MM-YY
 
    Answer:		B6H. Length: 18 bytes.
    ·	Result Code (1 byte)
    ·	ECR model (16 bytes) string of WIN1251 code page characters
 
    NOTE: Command execution may take up to 150 seconds.
 
****************************************************************************/
public final class ReadEJReportDepartmentDates extends PrinterCommand {
    // in 
    private final int password;
    private final byte reportType;
    private final byte department;
    private final PrinterDate date1;
    private final PrinterDate date2;
    // out
    private String ecrModel = new String("");

    /**
     * Creates a new instance of ReadEJReportDepartmentDates
     */
    public ReadEJReportDepartmentDates(
            int password,
            byte reportType,
            byte department,
            PrinterDate date1,
            PrinterDate date2) {
        this.password = password;
        this.reportType = reportType;
        this.department = department;
        this.date1 = date1;
        this.date2 = date2;
    }

    public final int getCode() {
        return 0xB6;
    }

    public final String getText() {
        return "Read electronic journal department report in date range";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(reportType);
        out.writeByte(department);
        out.writeDate(date1);
        out.writeDate(date2);
    }

    public final void decode(CommandInputStream in) throws Exception {
        ecrModel = in.readString(in.getSize());
    }

    public String getEcrModel() {
        return ecrModel;
    }
}
