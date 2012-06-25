/*
 * ContinuePrint.java
 *
 * Created on 2 April 2008, 20:17
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

/*****************************************************************************
 
    Continue printing
 
    Command:	B0H. Length: 5 bytes.
    ·	Operator, Administrator or System Administrator password (4 bytes)
    Answer:		B0H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
*****************************************************************************/

public final class ContinuePrint extends PrinterCommand {
    // in
    private final int password;
    // out
    private int operator = 0;

    /**
     * Creates a new instance of ContinuePrint
     */
    public ContinuePrint(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0xB0;
    }

    public final String getText() {
        return "Continue printing";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
