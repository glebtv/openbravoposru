//    Исходный код для Openbravo POS, автоматизированной системы продаж для работы
//    с сенсорным экраном, предоставлен ТОО "Норд-Трейдинг ЛТД", Республика Казахстан,
//    в период 2008-2010 годов на условиях лицензионного соглашения GNU LGPL.
//
//    Исходный код данного файл разработан в рамках проекта Openbravo POS ru
//    для использования системы Openbravo POS на территории бывшего СССР
//    <http://code.google.com/p/openbravoposru/>.
//
//    Openbravo POS является свободным программным обеспечением. Вы имеете право
//    любым доступным образом его распространять и/или модифицировать соблюдая
//    условия изложенные в GNU Lesser General Public License версии 3 и выше.
//
//    Данный исходный распространяется как есть, без каких либо гарантий на его
//    использование в каких либо целях, включая коммерческое применение. Данный
//    исход код может быть использован для связи с сторонними библиотеками
//    распространяемыми под другими лицензионными соглашениями. Подробности
//    смотрите в описании лицензионного соглашение GNU Lesser General Public License.
//
//    Ознакомится с условиями изложенными в GNU Lesser General Public License
//    вы можете в файле lgpl-3.0.txt каталога licensing проекта Openbravo POS ru.
//    А также на сайте <http://www.gnu.org/licenses/>.

package com.openbravo.pos.printer.shtrihfr;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import com.openbravo.pos.printer.*;
//import java.util.Arrays;

public class DeviceShtrihFRComm implements ShtrihFRReaderWritter, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private String m_sPort;
    private OutputStream m_out;
    private InputStream m_in;

   private ShtrihFR m_FR;

    private int iAnswerLength;

    //Состояние принтера
    private static final int PRINTER_READY = 0;     //Принтер готов к получению сообщения
    private static final int PRINTER_READING = 1;   //Принтер отправляет сообщение
    private int m_iStatusPrinter;

    //Интервалы ожидания сообщений от принтера
    private static final int T1 = 200;              //Задержка 0,05 сек
    private static final int T5 = 200;             //Задержка 0,05 сек
    private static final int T6 = 100;              //Задержка 0,1 сек
    private static final int T7 = 200;              //Задержка 0,1 сек

    //Управляющие символы протокола
    private static final byte[] ENQ = {0x05};       //Запрос
    private static final byte[] ACK = {0x06};       //Подтверждение получения сообщения
    private static final byte[] STX = {0x02};       //Начало блока данных
    private static final byte[] NAK = {0x15};       //Отрицание получения сообщения

    private static final byte NULL = 0x00;

    private static final byte[] PASS = {0x1E, 0x00, 0x00, 0x00};       //Отрицание получения сообщения

    public DeviceShtrihFRComm(String sPortPrinter) {
        m_sPort = sPortPrinter;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
        m_FR = new ShtrihFR();
    }

    //Команды обмена с принтером
    //Проверка состояния принтера
    public void sendInitMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 5);
        lineout.write(ShtrihFR.INIT);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),16);
    }

    //Получить наименование принтера
    public void getTypePrinter() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 1);
        lineout.write(ShtrihFR.TYPE);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),48);
    }

    //Печать текстового сообщения
    public void sendTextMessage(String sText) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 46);
        lineout.write(ShtrihFR.PRN);
        for (int i = 0; i < 4; i++) {
            lineout.write(PASS[i]);
        }
        lineout.write((byte) 2);
        byte[] MSG = new byte[40];
        byte[] MSGBuf = m_FR.UnicodeConverterCP1251(sText);
        if (MSGBuf.length <= 40) {
            for (int i = 0; i < MSGBuf.length; i++) {
                MSG[i] = MSGBuf[i];
            }
        } else {
            for (int i = 0; i < 40; i++) {
                MSG[i] = MSGBuf[i];
            }
        }
        for (int i = 0; i < 40; i++) lineout.write(MSG[i]);        
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(), 3);
    }

    //Продвинуть чековую ленту
    public void sendTicketUpMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 7);
        lineout.write(ShtrihFR.NEW_LINE);
        for (int i = 0; i < 4; i++) {
            lineout.write(PASS[i]);
        }
        lineout.write((byte) 2);
        lineout.write((byte) 5);        
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(), 3);
    }

    //Вывод звукового сигнала
    public void sendBeepMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 5);
        lineout.write(ShtrihFR.BEEP);
        for (int i = 0; i < 4; i++) lineout.write(PASS[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(), 3);
    }

    //Печать клише чека
//    public void sendStampTitleReportMessage() throws TicketPrinterException {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write((byte) 37);
//        lineout.write(ShtrihFR.TITLE);
//        for (int i=0; i<4; i++) lineout.write(PASS[i]);
//        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
//        sendMessage(lineout.toByteArray(),5);
//    }

    //Открыть денежный ящик
    public void sendOpenDrawerMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 6);
        lineout.write(ShtrihFR.OPEN_DRAWER);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write((byte) 0);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
    }

    //Обрезать чек
    public void sendTicketCutMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 6);
        lineout.write(ShtrihFR.PARTIAL_CUT);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write((byte) 0);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
    }

        //Обрезать чек
    public void sendPrintImageMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 7);
        lineout.write(ShtrihFR.PRINT_IMAGE);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write((byte) 1);
        lineout.write((byte) 200);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
    }

    public void sendSaleLine(String sProductName, double dProductPrice, double dSaleUnits, int iProductTax) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 60);
        lineout.write(ShtrihFR.SALE);

        for (int i = 0; i < 4; i++) {
            lineout.write(PASS[i]);
        }

        byte[] UNITBuf = new byte[24];
        UNITBuf = m_FR.convertHEX((long) (dSaleUnits * 1000));
        for (int i = 0; i < 5; i++) lineout.write(UNITBuf[i]);

        byte[] PRICEBuf = new byte[24];        
        if (ShtrihFR.FLAG_MDE == 1) {
            PRICEBuf = m_FR.convertHEX((long) (dProductPrice * 100));
        } else {
            PRICEBuf = m_FR.convertHEX((long) dProductPrice);
        }
        for (int i = 0; i < 5; i++) lineout.write(PRICEBuf[i]);

        lineout.write(1);
        lineout.write(iProductTax);
        lineout.write(0);
        lineout.write(0);
        lineout.write(0);

        byte[] MSG = new byte[40];
        byte[] MSGBuf = m_FR.UnicodeConverterCP1251(sProductName);
        if (MSGBuf.length <= 40) {
            for (int i = 0; i < MSGBuf.length; i++) {
                MSG[i] = MSGBuf[i];
            }
        } else {
            for (int i = 0; i < 40; i++) {
                MSG[i] = MSGBuf[i];
            }
        }
        for (int i = 0; i < 40; i++) lineout.write(MSG[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(), 3);

    }

    public void sendOpenTicket(int iTicketType) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 6);
        lineout.write(ShtrihFR.OPEN_TICKET);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write((byte) iTicketType);        
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
    }

    public void sendCloseTicket(String sTypePayment, double dTotalPayment, int iTax) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 71);
        lineout.write(ShtrihFR.CLOSE_TICKET);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);

        byte[] PRICEBuf = new byte[24];
        if (ShtrihFR.FLAG_MDE == 1) {
            PRICEBuf = m_FR.convertHEX((long) (dTotalPayment * 100));
        } else {
            PRICEBuf = m_FR.convertHEX((long) dTotalPayment);
        }
        for (int i = 0; i < 5; i++) lineout.write(PRICEBuf[i]);

        for (int i=0; i<5; i++) lineout.write(NULL);
        for (int i=0; i<5; i++) lineout.write(NULL);
        for (int i=0; i<5; i++) lineout.write(NULL);

        for (int i=0; i<2; i++) lineout.write(NULL);

        lineout.write(iTax);

        lineout.write(0);
        lineout.write(0);
        lineout.write(0);

        byte[] MSG = new byte[40];
        byte[] MSGBuf = m_FR.UnicodeConverterCP1251(sTypePayment);
        if (MSGBuf.length <= 40) {
            for (int i = 0; i < MSGBuf.length; i++) {
                MSG[i] = MSGBuf[i];
            }
        } else {
            for (int i = 0; i < 40; i++) {
                MSG[i] = MSGBuf[i];
            }
        }
        for (int i = 0; i < 40; i++) lineout.write(MSG[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));

        sendMessage(lineout.toByteArray(), 8);

    }

    public void printZReport() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 5);
        lineout.write(ShtrihFR.Z_REPORT);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
    }

    public void printXReport() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write((byte) 5);
        lineout.write(ShtrihFR.X_REPORT);
        for (int i=0; i<4; i++) lineout.write(PASS[i]);
        lineout.write(m_FR.calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray(),3);
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
    private void sendMessage(byte[] data, int iLength) {
        synchronized (this) {
            iAnswerLength = iLength;

            write(ENQ);
            try {
                wait(T1);
            } catch (InterruptedException e) {
            }

//            if (m_iStatusPrinter == PRINTER_READING) {
            write(STX);
            write(data);
            flush();
            try {
                wait(T5);
            } catch (InterruptedException e) {
            }
//            }

            write(ACK);
            flush();
            try {
                wait(T7);
            } catch (InterruptedException e) {
            }
            iAnswerLength = 0;
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

                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
            }
//            System.out.println ("OUT:"+ Arrays.toString(data));
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
                        byte[] b = new byte[iAnswerLength+4];
                        m_in.read(b);
                        if (b[0] == NAK[0] && m_iStatusPrinter == PRINTER_READY) {
                            synchronized (this) {
                                m_iStatusPrinter = PRINTER_READING;
                                notify();
                            }
                        } else if (b[0] == ACK[0] && b[1] == STX[0] && b[2] == iAnswerLength && m_iStatusPrinter == PRINTER_READING) {
                            synchronized (this) {
                                System.out.println(" SEND COMMAND: " + m_FR.ReadSendMessage(b[3]));
                                System.out.println("ERROR MESSAGE: " + m_FR.ReadErrorMessage(b[4]));
                                m_iStatusPrinter = PRINTER_READY;
                                notifyAll();
                            }
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }
    }
}
