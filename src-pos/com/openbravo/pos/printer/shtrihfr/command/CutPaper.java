/*
 * CutPaper.java
 *
 * Created on 2 April 2008, 21:18
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
 
    Cut receipt
 
    Command:	25H. Length: 6 bytes.
    ·	Operator password (4 bytes)
    ·	Cut type (1 byte) «0» – complete, «1» – incomplete
 
    Answer:		25H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
****************************************************************************/

public final class CutPaper extends PrinterCommand {
    // in
    private final int password;     // Operator password (4 bytes)
    private final int cutType;     // Cut type (1 byte)
    // out
    private int operator = 0;       // Operator index number (1 byte) 1…30

    /**
     * Creates a new instance of CutPaper
     */
    public CutPaper(
            int password, 
            int cutType) {
        this.password = password;
        this.cutType = cutType;
    }

    public final int getCode() {
        return 0x25;
    }

    public final String getText() {
        return "Cut receipt";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(cutType);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
