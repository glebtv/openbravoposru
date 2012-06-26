/*
 * PrintCashOut.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.util.ByteArrayUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.bcd4j.PackedBCD;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class PrintCashOut extends PrinterCommand {

    /*
     * Выплата денег
     *
     * Команда: 0x4F <Флаги(1)> <Сумма(5)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаги(1)> – проверяется только младший бит: 0 – выполнить операцию, 1 –
     * режим проверки операции. Остальные биты не используются и должны
     * содержать ноль.
     *
     * <Сумма(5)> – выплачиваемая из ККМ сумма, двоично-десятичная
     * 0000000001..9999999999 мде.
     *
     */
    
    private byte bCashOutFlag;
    private byte[] bCashOutAmount = new byte[5];
    
    private static int CMD_LENGTH = 6;
    
    private PrinterError mError = new PrinterError();

    public PrintCashOut(
            int iCashOutFlag,
            double dCashOutAmount) {
        
        bCashOutFlag = (byte) iCashOutFlag;
        bCashOutAmount = new PackedBCD(
                new BigDecimal(dCashOutAmount * Math.pow(10, 2)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();
    }

    public final int getCode() {
        return 0x4F;
    }

    public final String getText() {
        return "Cash-out";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bCashOutFlag;

        for (int i = 0; i < bCashOutAmount.length; i++) {
            bMessage[i + 1] = bCashOutAmount[i];
        }

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
