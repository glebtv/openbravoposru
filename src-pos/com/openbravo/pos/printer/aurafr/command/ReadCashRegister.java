/*
 * ReadMode.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.util.ByteArrayUtils;
import java.util.Calendar;
import org.bcd4j.PackedBCD;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ReadCashRegister extends PrinterCommand {

    /*
     * Считать регистр
     *
     * Команда: 0x91 <Регистр(1)> <Параметр1(1)> <Параметр2(1)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <Значение(Х)>
     *
     * Команда предназначена для возможности получения в любой момент значений
     * основных операционных и денежных регистров, а также для получения
     * основных параметров ККМ (дата, время, версия, номер документа, сквозной
     * номер документа и т.д.).
     *
     * <Регистр(1)> – двоичное число. Допустимые значения 1..30. Значения 31..255 –
     * недопустимые (зарезервированы для будущих версий).
     * 
     * <Параметр1(1)> – двоичное число (0 .. 255). Если поле Регистр не требует
     * параметров, то поле Параметр1 должно передаваться и содержать 0.
     *
     * <Параметр2(1)> – двоичное число (0 .. 255). Если поле Регистр не требует
     * параметров, то поле Параметр2 должно передаваться и содержать 0.
     *
     * <Значение(Х)> – в зависимости от регистра ККМ возвращает различное число байт
     * в качестве значения регистра. Также интерпретация поля Значение (формат и
     * тип поля) возлагается на хост в соответствии с Таблицей регистров.
     *
     * При описании регистров использовались следующие обозначения:
     * ТЧ – Тип чека (1 – продажи, 2 – возврат, 3 – аннулирования, 4 – покупка,
     * 5 – возврат покупки, 6 – аннулирование покупки);
     * ТО – Тип оплаты (1 – наличные, 2 – тип оплаты 2, 3 – тип оплаты 3, 4 –
     * тип оплаты 4);
     * Цх – целое число из диапазона от 0 до 10х – 1;
     * Дх.у – дробное число из диапазона 0 до 10х – 10-у)
     * ТИ – Тип итога (0 – итог по продажам, 1 – итог по покупкам).
     *
     * <0x04> <0x00> <0x00> 6 BCD (Д10.2) Сумма Внесений 
     * 
     * <0x05> <0x00> <0x00> 6 BCD (Д10.2) Cумма Выплат
     * 
     * <0x12> <0x00> <0x00> 1 Bin (Ц1) Смена открыта 0-закрыта, 1-открыта
     *                      3 BCD (ДДММГГ) Дата закрытия смены 
     *                      3 BCD (ЧЧММСС) Время закрытия смены Последняя минута
     *                                     смены, СС = 59.
     * 
     */
    
    private byte bTotalizer;
    private byte bTotParam1;
    private byte bTotParam2;
///    private int iTotLength;
    
    private static int CMD_LENGTH = 3; 
    
    private PrinterError mError = new PrinterError();
    
    private PackedBCD mBCD;
    
    private int iFlagOpenDay = 0;
    
    private int iDateClose = 0;
    private int iMonthClose = 0;
    private int iYearClose = 0;
    private int iHourClose = 0;
    private int iMinuteClose = 0;
    private int iSecondClose = 0;

    
    public ReadCashRegister(byte bTotalizer, byte bTotParam1, byte bTotParam2) {
        this.bTotalizer = bTotalizer;
        this.bTotParam1 = bTotParam1;
        this.bTotParam2 = bTotParam2;        
//        this.iTotLength = iTotLength;
    }
    
    public final int getCode() {
        return 0x91;
    }

    public final String getText() {
        return "Get cash totalizer value";
    }    

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bTotalizer;
        bMessage[1] = bTotParam1;
        bMessage[2] = bTotParam2;

//        System.out.println("Packet(" + bMessage.length + "):" + ByteArrayUtils.getHexString(bMessage));
        
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

        if (bTotalizer == 0x12) {
            iFlagOpenDay = data[2];
            iDateClose = new PackedBCD(new byte[]{data[3]}).toBigInteger().intValue();
            iMonthClose = new PackedBCD(new byte[]{data[4]}).toBigInteger().intValue();
            iYearClose = new PackedBCD(new byte[]{data[5]}).toBigInteger().intValue() + 2000;
            iHourClose = new PackedBCD(new byte[]{data[6]}).toBigInteger().intValue();
            iMinuteClose = new PackedBCD(new byte[]{data[7]}).toBigInteger().intValue();
            iSecondClose = new PackedBCD(new byte[]{data[8]}).toBigInteger().intValue();

//            System.out.println("Packet(" + data.length + "):" + ByteArrayUtils.getHexString(data));
        }
    }

    public PrinterError getErrorAnswer() {
        return mError;
    }

    public Boolean isOpenFiscalDay() {
        if (iFlagOpenDay == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Calendar getDateCloseFiscalDay() {
        Calendar cDateCloseFiscalDay = Calendar.getInstance();
        cDateCloseFiscalDay.set(iYearClose, iMonthClose, iDateClose, iHourClose, iMinuteClose, iSecondClose);

        return cDateCloseFiscalDay;

    }
}
