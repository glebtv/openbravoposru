/*
 * BeginFiscalDay.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.DeviceAuraFR;
import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.printer.aurafr.util.UnicodeConverterCP866KZ;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class BeginFiscalDay extends PrinterCommand {

    /*
     * Открыть смену
     *
     * Команда: 0x9A <Флаги(1)> <Текст(Х)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Флаги(1)> – битовое поле. ККМ проверяет только младший бит: 0 –
     * выполнить операцию, 1 – режим проверки операции. Остальные биты не
     * используются и должны содержать ноль.
     * 
     * <Текст(Х)> – строка произвольного текста в кодировке MS-DOS 866.
     *
     * Если поле имеет длину 0 (байты не переданы), то строка не печатается.
     *
     * Если в таблице 2, ряд 1, поле 43 записан 0, то смена открывается, но
     * документ не печатается (и строка текста также).
     * 
     * Команда работает в режиме 1.0 только если чек и смена закрыты. Состояние
     * ККМ и чека не меняется, а смена открывается.
     *
     */

    private byte bBeginFiscalDayFlag;
    private String sText = "";
    
    private static int CMD_LENGTH = 1;
    
    private PrinterError mError = new PrinterError();

    public BeginFiscalDay(
            int iBeginFiscalDayFlag,
            String sText) {
        bBeginFiscalDayFlag = (byte) iBeginFiscalDayFlag;
        
        if (sText.length() > DeviceAuraFR.MAX_STRING_LENGTH) {
            this.sText = sText.substring(0, DeviceAuraFR.MAX_STRING_LENGTH);
        } else {
            this.sText = sText;
        }
    }

    public final int getCode() {
        return 0x9A;
    }

    public final String getText() {
        return "Open fiscal day";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bBeginFiscalDayFlag;
//        if (sText != null) {
//            return UnicodeConverterCP866KZ.convertString(sText);
//        } else {
//            return null;
//        }
        return bMessage;
    }

    public int getMessageDataSize() {
        return CMD_LENGTH + sText.length();
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
