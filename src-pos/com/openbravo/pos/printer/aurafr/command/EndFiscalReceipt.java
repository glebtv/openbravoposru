/*
 * EndFiscalReceipt.java
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
public class EndFiscalReceipt extends PrinterCommand {

    /*
     * Закрыть чек(со сдачей)
     *
     * Команда: 0x4A <Флаги(1)> <Тип оплаты(1)> <Внесенная сумма(5)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаги(1)> – проверяется только младший бит: 
     * 0 – выполнить операцию, 
     * 1 – режим проверки операции. Остальные биты не используются и
     * должны содержать ноль.
     *
     * <Тип оплаты(1)> – формат BCD. 01 – Наличными, 02..04 – Типом оплаты i,
     * остальные значения – зарезервированы и не используются.
     *
     * <Внесенная сумма(5)> – формат BCD, 0000000000..9999999999 мде. Для чеков
     * возврата и аннулирования всегда должна быть равна 0.
     *
     * Логика работы команды зависит от режима ККМ: 
     * 
     * Режим ККМ = 1.0 (в данном чеке не использовалась команда Расчет по чеку):
     * Если сумма = 0, то сдача не начисляется (получена сумма, равная сумме
     * чека). Вносимая сумма не может быть меньше суммы чека (если Сумма ≠ 0).
     * Для начисления сдачи необходимо передать ненулевую Сумму (не менее суммы
     * чека) и указать Тип оплаты = 1 (при оплате «неналичными» сдача
     * начисляться не может).
     *
     * Режим ККМ = 1.4 (в данном чеке использовалась команда Расчет по чеку):
     * Сумма платежей, проведенных по данному чеку командами Расчет по чеку (с
     * учетом Сторно расчета по чеку), должна быть не меньше суммы чека (Остаток
     * = 0). Вносимая сумма (команда Закрыть чек (со сдачей)) должна быть равна
     * 0. Поле Тип оплаты команды Закрыть чек (со сдачей) игнорируется.
     *
     * Внимание! Можно начислить сдачу на чек продажи, имеющий «Сумма чека» = 0
     * мде.
     *
     */

    private byte bPaymentFlag;
    private byte bPaymentType;
    private byte[] bPaymentAmount = new byte[5];
    
    private static int CMD_LENGTH = 7;
    
    private PrinterError mError = new PrinterError();

    public EndFiscalReceipt(
            int iPaymentFlag,
            int iPaymentType,
            double dPaymentAmount) {
        
        bPaymentFlag = (byte) iPaymentFlag;
        bPaymentType = (byte) iPaymentType;
        bPaymentAmount = new PackedBCD(
                new BigDecimal(dPaymentAmount * Math.pow(10, 2)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();

        System.out.println("Packet(" + bPaymentAmount.length + "):" + ByteArrayUtils.getHexString(bPaymentAmount));
    }

    public final int getCode() {
        return 0x4A;
    }

    public final String getText() {
        return "Close receipt";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bPaymentFlag;
        bMessage[1] = bPaymentType;

        for (int i = 0; i < bPaymentAmount.length; i++) {
            bMessage[i + 2] = bPaymentAmount[i];
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
