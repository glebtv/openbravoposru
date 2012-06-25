/*
 * Beep.java
 */
package com.openbravo.pos.printer.aurafr.command;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class Beep extends PrinterCommand {

    /*
     * Гудок
     *
     * Команда: 0x47 
     * 
     * Ответ: Отсутствует
     *
     * Ответа на данную команду не предусмотрено.
     */
    
    public Beep() {
    }

    public final int getCode() {
        return 0x47;
    }

    public final String getText() {
        return "Beep";
    }

    public byte[] getMessageData() {
        return null;
    }

    public int getMessageDataSize() {
        return 0;
    }

    public final boolean isAnswer() {
        return false;
    }

    @Override
    public void readAnswerData(byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
