/*
 * WritePortParams.java
 *
 * Created on April 2 2008, 21:20
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
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.PrinterTimeout;

/****************************************************************************
 
    Set communication parameters
 
    Command:	14H. Length: 8 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Port number (1 byte) 0…255
    ·	Baud rate (1 byte) 0…6
    ·	Inter-character time out (1 byte) 0…255
 
    Answer:		14H. Length: 2 bytes.
    ·	Result Code (1 byte)
 
****************************************************************************/
public final class WritePortParams extends PrinterCommand {
    // in

    private final int password;
    private final byte portNumber;
    private final byte baudRate;
    private final int timeout;

    /**
     * Creates a new instance of WritePortParams
     */
    public WritePortParams(
            int password, 
            byte portNumber,
            byte baudRate, 
            int timeout) {
        this.password = password;
        this.portNumber = portNumber;
        this.baudRate = baudRate;
        this.timeout = timeout;
    }

    public final int getCode() {
        return 0x14;
    }

    public final String getText() {
        return "Set communication parameters";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(portNumber);
        out.writeByte(baudRate);
        out.writeByte(PrinterTimeout.getTimeoutCode(timeout));
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
