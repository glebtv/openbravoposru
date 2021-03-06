/*
 * PrintEJActivationReport.java
 *
 * Created on 16 January 2009, 13:04
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

/****************************************************************************
 
    Print EKLZ Activation Receipt
 
    Command:	A8H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		A8H. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/
public final class PrintEJActivationReport extends PrinterCommand {
    // in 
    private final int password;

    /**
     * Creates a new instance of PrintEJActivationReport
     */
    public PrintEJActivationReport(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0xA8;
    }

    public final String getText() {
        return "Print electronic journal activation report";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
