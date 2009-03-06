//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package com.openbravo.pos.printer.elveskkm;

// import javax.comm.*; // Java comm library
import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import com.openbravo.pos.printer.*;

public class DeviceElvesKKMComm implements PrinterReaderWritter, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private String m_sPort;
    private OutputStream m_out;
    private InputStream m_in;

    //Состояние принтера
    private static final int PRINTER_READY = 0;     //Принтер готов к получению сообщения
    private static final int PRINTER_READING = 1;   //Принтер отправляет сообщение
    private int m_iStatusPrinter;

    //Интервалы ожидания сообщений от принтера
    private static final int T1 = 500;              //Задержка 0,5 сек
    private static final int T3 = 500;              //Задержка 0,5 сек
    private static final int T5 = 2000;             //Задержка 2,0 сек
    private static final int T6 = 500;              //Задержка 0,5 сек
    private static final int T7 = 500;              //Задержка 0,5 сек
    private static final int T8 = 1000;             //Задержка 1,0 сек

    //Управляющие символы протокола
    private static final byte[] ENQ = {0x05};       //Запрос
    private static final byte[] EOT = {0x04};       //Конец передачи
    private static final byte[] ACK = {0x06};       //Подтверждение получения сообщения
    private static final byte[] STX = {0x02};       //Начало блока данных
    private static final byte[] NAK = {0x15};       //Отрицание получения сообщения

    public DeviceElvesKKMComm(String sPortPrinter) {
        m_sPort = sPortPrinter;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    //Команды обмена с принтером
    //Проверка состояния принтера
    public void sendInitMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.INIT);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Печать текстового сообщения
    public void sendTextMessage(String sText) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.PRN);
        byte[] MSG = new byte[sText.length()];
        MSG = UnicodeConverterCP1251(sText);
        for (int i = 0; i < MSG.length && i < 24; i++) {
            lineout.write(MSG[i]);
        }
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Вывод звукового сигнала
    public void sendBeepMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.BEEP);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Печать клише чека
    public void sendStampTitleReportMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.TITLE);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Открыть денежный ящик
    public void sendOpenDrawerMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.OPEN_DRAWER);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Закрыть соединение
    public void disconnectDevice() {
        try {
            m_out.close();
            m_in.close();
            m_CommPortPrinter.close();
        } catch (IOException e) {
        }

        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    //Функция отправки блока данных
    private void sendMessage(byte[] data) {
        synchronized (this) {
            write(ENQ);
            try {
                wait(T1);
            } catch (InterruptedException e) {
            }
            write(STX);
            write(data);
            flush();
            try {
                wait(T3);
            } catch (InterruptedException e) {
            }
            write(EOT);
            flush();
            try {
                wait(T5);
            } catch (InterruptedException e) {
            }
            write(ACK);
            flush();
            try {
                wait(T7);
            } catch (InterruptedException e) {
            }
            write(ACK);
            flush();
            try {
                wait(T8);
            } catch (InterruptedException e) {
            }
        }
    }

    private void flush() {
        try {
            m_out.flush();
        } catch (IOException e) {
        }
    }

    private void write(byte[] data) {
        try {
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort); // Tomamos el puerto
                m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", T6); // Abrimos el puerto

                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura
                m_in = m_CommPortPrinter.getInputStream();

                m_CommPortPrinter.addEventListener(this);
                m_CommPortPrinter.notifyOnDataAvailable(true);

                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD); // Configuramos el puerto
            }
            m_out.write(data); //Отправка байта сообщения на принтер
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent e) {

        // Determine type of event.
        switch (e.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (m_in.available() > 0) {
                        int b = m_in.read(); //Приём байта сообщения с принтера
                        if (b == EOT[0] || b == NAK[0] && m_iStatusPrinter == PRINTER_READING) { // RS ASCII
                            synchronized (this) {
                                m_iStatusPrinter = PRINTER_READY;
                                notifyAll();
                            }
                        } else if (b == ACK[0] || b == ENQ[0]) {
                            synchronized (this) {
                                m_iStatusPrinter = PRINTER_READY;
                                notify();
                            }
                        } else if (b == STX[0]) {
                            if (m_iStatusPrinter == PRINTER_READY) {
                                m_iStatusPrinter = PRINTER_READING;
                            }
                        } else {
                            m_iStatusPrinter = PRINTER_READING;
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }

    }

    //Формирование контрольной суммы
    private byte calcCheckSumCRC(byte[] adata) {
        int isum = 0;
        for (int i = 0; i < adata.length; i++) {
            isum = isum ^ adata[i];
        }
        byte result = (byte) isum;
        return result;
    }

    //Конвертация текста из UTF8 в CP1251
    private byte[] UnicodeConverterCP1251(String sText) {
        if (sText == null) {
            return new byte[0];
        } else {
            byte[] result = new byte[sText.length()];
            for (int i = 0; i < sText.length(); i++) {
                result[i] = transChar(sText.charAt(i));
            }
            return result;
        }
    }

    //Конвертация таблицы символов из UTF8 в CP1251
    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
            // ru - Russian
            case '\u0410': return (byte) 0x80;// A
            case '\u0411': return (byte) 0x81;// Б
            case '\u0412': return (byte) 0x82;// В
            case '\u0413': return (byte) 0x83;// Г
            case '\u0414': return (byte) 0x84;// Д
            case '\u0415': return (byte) 0x85;// Е
            case '\u0401': return (byte) 0x85;// Ё
            case '\u0416': return (byte) 0x86;// Ж
            case '\u0417': return (byte) 0x87;// З
            case '\u0418': return (byte) 0x88;// И
            case '\u0419': return (byte) 0x89;// Й
            case '\u041A': return (byte) 0x8A;// К
            case '\u041B': return (byte) 0x8B;// Л
            case '\u041C': return (byte) 0x8C;// М
            case '\u041D': return (byte) 0x8D;// Н
            case '\u041E': return (byte) 0x8E;// О
            case '\u041F': return (byte) 0x8F;// П
            case '\u0420': return (byte) 0x90;// Р
            case '\u0421': return (byte) 0x91;// С
            case '\u0422': return (byte) 0x92;// Т
            case '\u0423': return (byte) 0x93;// У
            case '\u0424': return (byte) 0x94;// Ф
            case '\u0425': return (byte) 0x95;// Х
            case '\u0426': return (byte) 0x96;// Ц
            case '\u0427': return (byte) 0x97;// Ч
            case '\u0428': return (byte) 0xE8;// Ш
            case '\u0429': return (byte) 0xE9;// Щ
            case '\u042A': return (byte) 0x9A;// Ъ
            case '\u042B': return (byte) 0x9B;// Ы
            case '\u042C': return (byte) 0x9C;// Ь
            case '\u042D': return (byte) 0x9D;// Э
            case '\u042E': return (byte) 0x9E;// Ю
            case '\u042F': return (byte) 0x9F;// Я
            case '\u0430': return (byte) 0xA0;// a
            case '\u0431': return (byte) 0xA1;// б
            case '\u0432': return (byte) 0xA2;// в
            case '\u0433': return (byte) 0xA3;// г
            case '\u0434': return (byte) 0xA4;// д
            case '\u0435': return (byte) 0xA5;// е
            case '\u0451': return (byte) 0xA5;// ё
            case '\u0436': return (byte) 0xA6;// ж
            case '\u0437': return (byte) 0xA7;// з
            case '\u0438': return (byte) 0xA8;// и
            case '\u0439': return (byte) 0xA9;// й
            case '\u043A': return (byte) 0xAA;// к
            case '\u043B': return (byte) 0xAB;// л
            case '\u043C': return (byte) 0xAC;// м
            case '\u043D': return (byte) 0xAD;// н
            case '\u043E': return (byte) 0xAE;// о
            case '\u043F': return (byte) 0xAF;// п
            case '\u0440': return (byte) 0xE0;// р
            case '\u0441': return (byte) 0xE1;// с
            case '\u0442': return (byte) 0xE2;// т
            case '\u0443': return (byte) 0xE3;// у
            case '\u0444': return (byte) 0xE4;// ф
            case '\u0445': return (byte) 0xE5;// х
            case '\u0446': return (byte) 0xE6;// ц
            case '\u0447': return (byte) 0xE7;// ч
            case '\u0448': return (byte) 0xE8;// ш
            case '\u0449': return (byte) 0xE9;// щ
            case '\u044A': return (byte) 0xEA;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEC;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

                  default: return 0x3F; // ? Not valid character.
            }
        }
    }

}
