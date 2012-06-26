/*
 * PrintSaleRefund.java
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
public class PrintSaleRefund extends PrinterCommand {

    /*
     * Возврат
     *
     * Команда: 0x57 <Флаги(1)> <Цена(5)> <Количество(5)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаги(1)> – битовое поле: 
     * 0-й (младший) бит: 0 – выполнить операцию, 1 – режим проверки операции; 
     * 1-й бит: 0 – проверять денежную наличность, 1 – не проверять.
     * Остальные биты не используются и должны содержать ноль.
     * 
     * <Цена(5)> – двоично-десятичная 0000000000..9999999999 мде, цена
     * регистрируемого товара (2 знака после запятой: 0,01 ... 99999999,99).
     *
     * <Количество(5)> – двоично-десятичное 0000000001.. 9999999999 (3 знака 
     * после запятой: 0,001 ... 9999999,999), возвращаемое количество товара.
     *
     * Команда выполняется только при выполнении всех условий: 
     * Чек закрыт или открыт чек возврата;
     * Сумма чека + Цена * Количество ≤ 9999999999 мде.
     * Сумма наличных в ККМ не меньше возвращаемой суммы.
     * 
     * Примечание 1: Если (Цена * Количество) < 0,5 мде, то вернется 0 мде.
     * 
     */
    
    private byte bSaleRefundFlag;
    private byte[] bSaleRefundPrice = new byte[5];
    private byte[] bSaleRefundUnit = new byte[5];
    
    private static int CMD_LENGTH = 11;
    
    private PrinterError mError = new PrinterError();

    public PrintSaleRefund(
            int iSaleRefundFlag,
            double dSaleRefundPrice,
            double dSaleRefundUnit) {
        
        bSaleRefundFlag = (byte) iSaleRefundFlag;
        bSaleRefundPrice = new PackedBCD(
                new BigDecimal(dSaleRefundPrice * Math.pow(10, 2)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();

        bSaleRefundUnit = new PackedBCD(
                new BigDecimal(dSaleRefundUnit * Math.pow(10, 3)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();
    }

    public final int getCode() {
        return 0x57;
    }

    public final String getText() {
        return "Sale refund";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bSaleRefundFlag;

        for (int i = 0; i < bSaleRefundPrice.length; i++) {
            bMessage[i + 1] = bSaleRefundPrice[i];
        }

        for (int i = 0; i < bSaleRefundUnit.length; i++) {
            bMessage[i + 6] = bSaleRefundUnit[i];
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
