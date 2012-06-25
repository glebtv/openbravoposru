/*
 * VoidFiscalReceipt.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class VoidFiscalReceipt extends PrinterCommand {

    /*
     * Аннулирование всего чека
     *
     * Команда: 0x59
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * Команда аннулирует(отменяет) текущий открытый чек.
     */
    
    private PrinterError mError = new PrinterError();

    public VoidFiscalReceipt() {
    }

    public final int getCode() {
        return 0x59;
    }

    public final String getText() {
        return "Cancel receipt";
    }

    public byte[] getMessageData() {
        return null;
    }

    public int getMessageDataSize() {
        return 0;
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
