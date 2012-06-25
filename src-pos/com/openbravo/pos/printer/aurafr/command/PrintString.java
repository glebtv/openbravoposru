/*
 * PrintString.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.DeviceAuraFR;
import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.printer.aurafr.util.UnicodeConverterCP866KZ;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class PrintString extends PrinterCommand {

    /*
     * Печать строки
     *
     * Команда: 0x4C <Печатаемые символы(X)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Печатаемые символы(X)> – символы в кодовой странице 866 MS DOS.
     *
     * Длина строки X может быть любой из диапазона: Аура-01ФР-KZ 0..56.
     *
     * При печати пустой строки просто проматывается лента на одну строку.
     * 
     */
    
    private final String sText;
    
    private PrinterError mError = new PrinterError();

    public PrintString(String sText) {
        if (sText.length() > DeviceAuraFR.MAX_STRING_LENGTH) {
            this.sText = sText.substring(0, DeviceAuraFR.MAX_STRING_LENGTH);
        } else {
            this.sText = sText;
        }
    }

    public final int getCode() {
        return 0x4C;
    }

    public final String getText() {
        return "Print string";
    }

    public byte[] getMessageData() {
        if (sText != null) {
            System.out.println(sText);
            return UnicodeConverterCP866KZ.convertString(sText);
        } else {
            return null;
        }
    }

    public int getMessageDataSize() {
        return sText.length();
    }
    
    public final void readAnswerData(byte[] data) {
        if (data[0] == 0x55) {
            mError.bCode = data[1];
        }
    }
    
    public PrinterError getErrorAnswer() {
        return mError;
    }    

    public final boolean isAnswer() {
        return true;
    }
}
