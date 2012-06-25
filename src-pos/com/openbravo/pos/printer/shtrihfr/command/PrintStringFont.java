/*
 * PrintStringFont.java
 *
 * Created on April 2 2008, 20:02
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
import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/****************************************************************************
 
    Print String With Specific Font
 
    Command:	2FH. Length: 47 bytes.
    ·	Operator password (4 bytes)
    ·	Flags (1 byte) 
        Bit 0 – print on journal station, 
        Bit 1 – print on receipt station
    ·	Font number (1 byte) 0…255
    ·	String of characters to print (40 bytes)
 
    Answer:		2FH. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
    NOTE: 
        Only WIN1251 code page characters can be printed. 
        Characters with codes 0 to 31 are ignored.

****************************************************************************/
public class PrintStringFont extends PrinterCommand {
    // in params
    private final int password;
    private final int station;
    private final int fontNumber;
    private final String line;
    // out
    private int operator;

    /**
     * Creates a new instance of PrintStringFont
     */
    public PrintStringFont(
            int password, 
            int station,
            int fontNumber, 
            String line) {
        MethodParameter.checkRange(fontNumber, 0, 0xFF, "font number");

        this.password = password;
        this.station = station;
        this.fontNumber = fontNumber;
        this.line = line;
    }

    public final int getCode() {
        return 0x2F;
    }

    public final String getText() {
        return "Print string with specific font";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(station);
        out.writeByte(fontNumber);
        out.writeString(line, 40);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
    }

    public int getOperator() {
        return operator;
    }
}
