/*
 * BeginFiscalReceipt.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class BeginFiscalReceipt extends PrinterCommand {

    /*
     * Открыть чек
     *
     * Команда: 0x92 <Флаги(1)> <Тип чека(1)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаги(1)> – битовое поле: 0 – выполнить операцию, 1 – режим проверки
     * операции; 3 – буферизировать документ (0 – печатать сразу, не формировать
     * копию, 1 – буферизировать). Если 3-й бит = 1, то после успешного
     * выполнения команды ККМ переходит в режим 1.5.
     *
     * <Тип чека(1)>: 1 – чек продажи, 2 – чек возврата продажи, 3 – чек
     * аннулирования продажи, 4 – чек покупки, 5 – чек возврата покупки, 6 – чек
     * аннулирования
     * 
     */
    
    private byte bReceiptFlag;
    private byte bReceiptType;
    
    private static int CMD_LENGTH = 2;
    
    private PrinterError mError = new PrinterError();

    public BeginFiscalReceipt(int iReceiptFlag, int iReceiptType) {
        bReceiptFlag = (byte) iReceiptFlag;
        bReceiptType = (byte) iReceiptType;
    }

    public final int getCode() {
        return 0x92;
    }

    public final String getText() {
        return "Open receipt";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bReceiptFlag;
        bMessage[1] = bReceiptType;
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
