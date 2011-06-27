package com.openbravo.pos.printer.escpos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UnicodeTranslatorGB2312Chinese extends UnicodeTranslator {

    /** Creates a new instance of UnicodeTranslatorInt */
    public UnicodeTranslatorGB2312Chinese() {
    }

    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_13;
    }

    public final byte[] convertString(String sConvert) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();

        try {
            lineout.write(sConvert.getBytes("GB2312"));
        } catch (IOException ex) {
        }

        return lineout.toByteArray();
    }
}