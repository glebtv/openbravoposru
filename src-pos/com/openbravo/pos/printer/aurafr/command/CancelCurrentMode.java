/*
 * CancelCurrentMode.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class CancelCurrentMode extends PrinterCommand {

    /*
     * Выход из текущего режима
     *
     * Команда: 0x48
     *
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     * Команда выхода из текущего режима в «надрежим». Эта команда отменяет
     * любое начатое на ККМ действие (кроме открытого чека). 
     * 
     * Структура режимов выглядит так: Режим «Выбор» – Режим i – Подрежим j, по
     * этой причине выход из подрежима сразу в режим «Выбор» недопустим.
     *
     * Данной командой нельзя выйти из состояний 1.4 (режим приема платежей по
     * чеку), 5.1 (если введен неверный пароль доступа к ФП) и 7.11 (см. также
     * примечание к команде Вход в режим).
     *
     * Данной командой следует выходить из режима 7.13 (режим оповещения
     * перевода часов на летнее/зимнее время).
     *
     */
    
    private PrinterError mError = new PrinterError();

    public CancelCurrentMode() {
    }

    public final int getCode() {
        return 0x48;
    }

    public final String getText() {
        return "Cancel current FP mode";
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
