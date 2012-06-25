/*
 * PrintXReport.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class PrintXReport extends PrinterCommand {

    /*
     * Начало снятия отчета без гашения
     *
     * Команда: 0x67 <Тип отчета(1)>
     * 
     * Ответ: 0x55 <Код ошибки(1)> <(0)>
     *
     *Тип Отчета – Формат BCD: 
     * 1 - Суточный отчет (X-отчет)
     * 2 - Отчет по секциям 
     * 3 - Отчет по кассирам 
     * 4 - Отчет по товарам 
     * 5 - Почасовой отчет 
     * 7 - Отчет количеств
     *
     * Примечание 1: При печати отчета по секциям (Тип Отчета = 2) поля «СКИДКИ»
     * и «НАДБАВКИ» отражают сумму скидок и надбавок, начисленных на ВЕСЬ ЧЕК
     * (Область = 0), по всем чекам. Скидки и надбавки, начисленные на последнюю
     * операцию (Область = 1), учитываются в сумме по секции, к которой
     * относилась операция.
     * 
     * Примечание 2: Значение ТипОтчета = 4 поддерживается только ККМ «Меркурий-
     * 140Ф» АТОЛ.
     *
     * Последовательности выполнения: Начало снятия отчета без гашения. 
     * Цикл команд Запрос кода состояния ККМ, 
     * пока Состояние = 2.2 (рекомендуемая частота опроса – 2 раза / сек.).
     * 
     * Если Состояние = 2.0,
     *   то если бит 0 поля Флаги = 1, 
     *     то ошибка «Нет бумаги» (на остатке ленты ККМ автоматически
     *     печатается «Чек аннулирован» и отчет прерывается),
     *   иначе если бит 1 поля Флаги = 1,
     *     то ошибка «Нет связи с принтером чека», 
     *     иначе – удачное завершение, 
     *   иначе если бит 2 поля Флаги = 1,
     *     то ошибка «Механическая ошибка печатающего устройства»,
     *   иначе (биты 0, 1 и 2 поля Флаги = 0) ошибка «Снятие отчета прервалось». 
     *
     * Если Состояние ≠ 2.0 – ошибка «Снятие отчета прервалось» и печать «Чек
     * аннулирован» (например, выключили и включили питание ККМ (наиболее
     * вероятно при большом периоде опроса)).
     *
     */
  
    private byte bReportType;
    
    private static int CMD_LENGTH = 1;
    
    private PrinterError mError = new PrinterError();

    public PrintXReport(int iReportType) {
        bReportType = (byte) iReportType;
    }

    public final int getCode() {
        return 0x67;
    }

    public final String getText() {
        return "Print X report";
    }

    public byte[] getMessageData() {
        byte[] bMessage = new byte[CMD_LENGTH];
        bMessage[0] = bReportType;

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
