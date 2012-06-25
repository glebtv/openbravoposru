/*
 * LoadExtendedGraphics.java
 *
 * Created on April 2 2008, 21:24
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
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.PrinterConst;
import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/****************************************************************************

    Load extended graphics

    Command: 	C4H. Length: 47 bytes.
    ·	Operator password (4 bytes)
    ·	Graphics line number (2 bytes) 0…1199
    ·	Graphical data (40 bytes)

    Answer:		C4H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
****************************************************************************/
public final class LoadExtendedGraphics extends PrinterCommand {
    // in
    private final int password;     // Operator password (4 bytes)
    private final int lineNumber;   // Graphics line number (2 bytes) 0…1199
    private final byte[] data;      // Graphical data (40 bytes)
    // out
    private int operator;

    /**
     * Creates a new instance of LoadExtendedGraphics
     */
    public LoadExtendedGraphics(
            int password, 
            int lineNumber,
            byte[] data) {
        MethodParameter.checkRange(lineNumber, 1, PrinterConst.MAX_LINES_GRAPHICS_EX, "line number");

        this.password = password;
        this.lineNumber = lineNumber;
        this.data = data;
    }

    public final int getCode() {
        return 0xC4;
    }

    public final String getText() {
        return "Load extended graphics";
    }

    public final void encode(CommandOutputStream out)
            throws Exception {
        out.writeInt(password);
        out.writeShort(lineNumber);
        out.writeBytes(data, 40);
    }

    public final void decode(CommandInputStream in)
            throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
