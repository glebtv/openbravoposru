/*
 * ReadFullStatus.java
 *
 * Created on 2 April 2008, 19:14
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
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.FullPrinterStatus;

/****************************************************************************
 
    Get FP Status
 
    Command:	11H. Length: 5 bytes.
    ·	Operator password (4 bytes)
    Answer:		11H. Length: 48 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
    ·	FP firmware version (2 bytes)
    ·	FP firmware build (2 bytes)
    ·	FP firmware date (3 bytes) DD-MM-YY
    ·	Number of FP in checkout line (1 byte)
    ·	Current receipt number (2 bytes)
    ·	FP flags (2 bytes)
    ·	FP mode (1 byte)
    ·	FP submode (1 byte)
    ·	FP port (1 byte)
    ·	FM firmware version (2 bytes)
    ·	FM firmware build (2 bytes)
    ·	FM firmware date (3 bytes) DD-MM-YY
    ·	Current date (3 bytes) DD-MM-YY
    ·	Current time (3 bytes) HH-MM-SS
    ·	FM flags (1 byte)
    ·	Serial number (4 bytes)
    ·	Number of last daily totals record in FM (2 bytes) 0000…2100
    ·	Quantity of free daily totals records left in FM (2 bytes)
    ·	Last fiscalization/refiscalization record number in FM (1 byte) 1…16
    ·	Quantity of free fiscalization/refiscalization records left in FM (1 byte) 0…15
    ·	Taxpayer ID (6 bytes)

****************************************************************************/
 public class ReadFullStatus extends PrinterCommand {
    // in

    private final int password;
    // out params
    private FullPrinterStatus status = new FullPrinterStatus();

    /**
     * Creates a new instance of ReadFullStatus
     */
    public ReadFullStatus(int password) {
        this.password = password;
    }

    public final int getCode() {
        return 0x11;
    }

    public final String getText() {
        return "Get status";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
    }

    public final void decode(CommandInputStream in) throws Exception {
        byte[] version = new byte[2];

        status.operatorNumber = in.readByte();
        version[0] = (byte) in.readByte();
        version[1] = (byte) in.readByte();
        status.firmwareVersion = new String(version, in.getCharsetName());
        status.firmwareBuild = in.readShort();
        status.firmwareDate = in.readDate();
        status.logicalNumber = in.readByte();
        status.documentNumber = in.readShort();
        status.flags = in.readShort();
        status.setMode(in.readByte());
        status.advancedMode = in.readByte();
        status.portNumber = in.readByte();

        version[0] = (byte) in.readByte();
        version[1] = (byte) in.readByte();
        status.fmFirmwareVersion = new String(version, in.getCharsetName());

        status.fmFirmwareBuild = in.readShort();
        status.fmFirmwareDate = in.readDate();
        status.date = in.readDate();
        status.time = in.readTime();
        status.fmFlags.setValue(in.readByte());
        status.serial = in.readInt();
        if (status.serial == 0xFFFFFFFF) {
            status.serialText = "not entered";
        } else {
            status.serialText = Long.toString(status.serial);
        }

        status.sessionNumber = in.readShort();
        status.freeRecordInFM = in.readShort();
        status.registrationNumber = in.readByte();
        status.freeRegistration = in.readByte();
        status.inn = in.readLong(6);

        if (status.inn == 0xFFFFFFFFFFFFL) {
            status.innText = "not entered";
        } else {
            status.innText = Long.toString(status.inn);
        }
    }

    public FullPrinterStatus getStatus() {
        return status;
    }
}
