/*
 * CutPaper.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class CutPaper extends PrinterCommand {

    /*
     * Отрезать чек
     *
     * Команда: 0x75 <Флаг(1)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаг(1)> – битовое поле: 0 – отрезать полностью, ≠0 – отрезать частично,
     * проверяется только 0-й бит.
     *
     */
    
    private byte bCutType;
    
    private static int CMD_LENGTH = 1;
    
    private PrinterError mError = new PrinterError();

    public CutPaper(int iCutType) {
        if (iCutType != 0) {
            bCutType = 0x01;
        } else {
            bCutType = 0x00;
        }
    }

    public final int getCode() {
        return 0x75;
    }

    public final String getText() {
        return "Cut receipt";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bCutType;
        return bMessage;
    }

    public int getMessageDataSize() {
        return CMD_LENGTH;
    }

    public final boolean isAnswer() {
        return true;
    }

    public final void readAnswerData(byte[] data) {
        if (data[0] == 0x55) {
            mError.bCode = data[1];
        }
    }

    public PrinterError getErrorAnswer() {
        return mError;
    }
}
