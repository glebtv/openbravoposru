package com.openbravo.pos.printer.shtrihfr.command;

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandInputStream;
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandOutputStream;

public abstract class PrinterCommand {

    private int resultCode = 0;
    private int timeout = 10000;            // command execution timeout
    private final CommandInputStream in;    // command input parameters
    private final CommandOutputStream out;  // command output parameters

    /**
     * Creates a new instance of PrinterCommand
     */
    public PrinterCommand() {
        String charsetName = "Cp1251";
        this.in = new CommandInputStream(charsetName);
        this.out = new CommandOutputStream(charsetName);
    }

    public int getTimeout() {
        return timeout;
    }

    public int getResultCode() {
        return resultCode;
    }

    private void checkMinLength(int length, int minLength) throws Exception {
        if (length < minLength) {
            throw new Exception("Invalid answer data length");
        }
    }

    public void decodeData(byte[] data) throws Exception {
        in.setData(data);
        checkMinLength(in.getSize(), 2);
        int commandCode = in.readByte();
        resultCode = in.readByte();
        if (resultCode == 0) {
            decode(in);
        }
    }

    public byte[] encodeData() throws Exception {
        out.reset();
        out.writeByte(getCode());
        encode(out);
        return out.getData();
    }

    public abstract int getCode();

    public abstract String getText();

    public abstract void encode(CommandOutputStream out) throws Exception;
    
    public abstract void decode(CommandInputStream in) throws Exception;
}
