/*
 * InitEJArchive.java
 *
 * Created on 16 January 2009, 14:03
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
 
    Initialize EKLZ archive
 
    Command:	B2H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		B2H. Length: 2 bytes.
    ·	Result Code (1 byte)
 
    NOTE: Command can be executed only with EKLZ development kit. 
    Command execution may take up to 20 seconds.

****************************************************************************/
public final class InitEJArchive extends PrinterCommand {
    // in 

    private final int password;

    /**
     * Creates a new instance of InitEJArchive
     */
    public InitEJArchive(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0xB2;
    }

    public final String getText() {
        return "Initialize electronic journal archive";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
    }
}
