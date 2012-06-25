/*
 * PrinterMode.java
 *
 */


package com.openbravo.pos.printer.aurafr.fiscalprinter;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

public class PrinterMode {
    
    public byte bMode;
    
    public PrinterMode() {
    }
    
    public String getTextMessage(int iMode) {
        switch (iMode) {
             case 0x00 : return "Выбор. Режим выбора состояния";
             case 0x01 : return "Регистрация. Ожидание команды";
             case 0x02 : return "Х-отчеты. Ожидание команды";
             case 0x03 : return "Z-отчеты. Ожидание команды";
             case 0x04 : return "Программирование. Ожидание команды";
             case 0x05 : return "Доступ к ФП. Ожидание команды";
             case 0x06 : return "Доступ к ЭКЛ. Ожидание команды";
//             case 0x70 : return ". ";
             
               default : return "The mode description is not identified.";
        }
    }

    public byte getModeStatus() {
        return bMode;
    }

    public String getModeText() {
        return getTextMessage(bMode);
    }

    public String getFullModeText() {
        return String.valueOf(bMode) + " " + getTextMessage(bMode);
    }
}
