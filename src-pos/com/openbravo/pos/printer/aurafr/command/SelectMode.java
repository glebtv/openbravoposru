/*
 * SelectMode.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.DeviceAuraFR;
import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import com.openbravo.pos.util.ByteArrayUtils;
import java.math.BigInteger;
import org.bcd4j.BCD;
import org.bcd4j.PackedBCD;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class SelectMode extends PrinterCommand {

    /*
     * Вход в режим
     *
     * Команда: 0x56 <Режим(1)> <Пароль(4)>
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * <Режим(1)> – устанавливаемый режим (двоично-десятичное число):
     * 1 - Режим регистрации
     * 2 - Режим отчетов без гашения
     * 3 - Режим отчетов с гашением
     * 4 - Режим программирования
     * 5 - Режим доступа к ФП
     * 6 - Режим доступа к ЭКЛЗ (в Аура-01ФР-KZ отсутствует)
     *
     * <Пароль(4)> – 8 двоично-десятичных символов, пароль для входа в указанный
     * режим (все пароли, кроме пароля доступа к ФП, программируются в таблице
     * паролей ККМ, пароль доступа к ФП изменяется при проведении
     * фискализации/перерегистрации).
     *
     */
    private byte bMode;
    
    private byte[] bModePassword = new byte[4];
    
    private static int CMD_LENGTH = 5;
    
    private PrinterError mError = new PrinterError();

    public SelectMode(int iMode, String sModePassword) {
        bMode = (byte) iMode;
        PackedBCD PackBCD = new PackedBCD(new BigInteger(sModePassword), 8);
//        System.out.println(ByteArrayUtils.getHexString(PackBCD.toByteArray()));
        bModePassword = PackBCD.toByteArray();
    }

    public final int getCode() {
        return 0x56;
    }

    public final String getText() {
        return "Select FP mode";
    }

    public byte[] getMessageData() {

        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bMode;

        for (int i = 0; i < bModePassword.length; i++) {
            bMessage[i + 1] = bModePassword[i];
        }

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
    }

    public PrinterError getErrorAnswer() {
        return mError;
    }
}
