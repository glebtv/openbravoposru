/*
 * PrintSale.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.util.ByteArrayUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.bcd4j.PackedBCD;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class PrintSale extends PrinterCommand {

    /*
     * Регистрация продажи
     *
     * Команда: 0x52 <Флаги(1)> <Цена(5)> <Количество(5)> <Секция(1)>
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
     * после запятой: 0,001 ... 9999999,999), регистрируемое количество товара.
     * 
     * <Секция(1)> – двоично-десятичное число 00 .. 30 – секция, в которую
     * осуществляется регистрация.
     * Примечание 1: Если Секция = 0, то регистрация произведется в 1-ю секцию,
     * но на чеке и контрольной ленте не будут напечатаны номер и название
     * секции.
     * Примечание 2: Секции 17 .. 30 в ККМ Аура-01ФР-KZ не используются.
     *
     * Команда выполняется только при выполнении всех условий: 
     * Чек закрыт или открыт чек продажи / покупки;
     * Сумма чека + Цена * Количество ≤ 9999999999 мде.
     * Примечание 1: Если (Цена * Количество) < 0,5 мде, то зарегистрируется 
     * 0 мде.
     * 
     */
    
    private byte bSaleFlag;
    private byte[] bSalePrice = new byte[5];
    private byte[] bSaleUnit = new byte[5];
    private byte[] bSaleSection = new byte[1];
    
    private static int CMD_LENGTH = 12;
    
    private PrinterError mError = new PrinterError();

    public PrintSale(
            int iSaleFlag,
            double dSalePrice,
            double dSaleUnit,
            String sSaleSection) {
        
        bSaleFlag = (byte) iSaleFlag;
        bSalePrice = new PackedBCD(
                new BigDecimal(dSalePrice * Math.pow(10, 2)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();

        bSaleUnit = new PackedBCD(
                new BigDecimal(dSaleUnit * Math.pow(10, 3)).setScale(0, RoundingMode.HALF_UP).toBigInteger(), 10).toByteArray();

        bSaleSection = new PackedBCD(new BigInteger(sSaleSection), 2).toByteArray();

    }

    public final int getCode() {
        return 0x52;
    }

    public final String getText() {
        return "Sale";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bSaleFlag;

        for (int i = 0; i < bSalePrice.length; i++) {
            bMessage[i + 1] = bSalePrice[i];
        }

        for (int i = 0; i < bSaleUnit.length; i++) {
            bMessage[i + 6] = bSaleUnit[i];
        }

        bMessage[11] = bSaleSection[0];

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
