/*
 * ReadMode.java
 */
package com.openbravo.pos.printer.aurafr.command;

import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterMode;
import com.openbravo.pos.util.ByteArrayUtils;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ReadMode extends PrinterCommand {

    /*
     * Запрос кода состояния ККМ
     *
     * Команда: 0x45
     *
     * Ответ: 0x55 <Режим(1)><Флаги(1)>
     *
     * <Режим(1)> – двоичное число (00h .. FFh). Младшая тетрада – режим,
     * старшая – подрежим (формат «Подрежим.Режим»).
     * 
     * <Флаги(1)> –      
     * 0-й (младший) бит: 0 – бумага есть, 1 – нет бумаги (в
     * принтере чеков). 
     * 1-й бит: 0 – связь с принтером чеков есть, 1 – связи
     * нет. 
     * 2-й бит: 0 – нет ошибок, 1 – механическая ошибка печатающего
     * устройства. 
     * 3-й бит: 0 – нет ошибок отрезчика, 1 – ошибка отрезчика. 
     * 4-й бит: 0 – нет ошибок принтера, 1 – восстановимая ошибка принтера
     * (перегрев). 
     * 5-й бит: 0 – буфер принтера ПД используется, 1 – буфер
     * принтера ПД пустой. 
     * 6-й бит: 0 – буфер принтера ПД не переполнен, 1 –
     * буфер принтера ПД переполнен.
     *
     */
  
   private PrinterMode mMode = new PrinterMode();

    public ReadMode() {
    }

    public final int getCode() {
        return 0x45;
    }

    public final String getText() {
        return "Get FP mode";
    }

    public byte[] getMessageData() {
        return null;
    }

    public int getMessageDataSize() {
        return 0;
    }

    public final void readAnswerData(byte[] data) {
        if (data[0] == 0x55) {
            mMode.bMode = data[1];
        }
       
//        System.out.println("Packet(" + data.length + "):" + ByteArrayUtils.getHexString(data));
    }
    
    public PrinterMode getMode() {
        return mMode;
    }    

    public final boolean isAnswer() {
        return true;
    }
}
