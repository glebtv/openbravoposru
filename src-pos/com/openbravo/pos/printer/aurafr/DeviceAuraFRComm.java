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

package com.openbravo.pos.printer.aurafr;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import com.openbravo.pos.printer.*;
import java.util.LinkedList;
import java.util.Queue;

public class DeviceAuraFRComm implements AuraFRReaderWritter, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    
    private String m_sPort;
    private Integer m_iPortSpeed;
    private Integer m_iPortBits;
    private Integer m_iPortStopBits;
    private Integer m_iPortParity;    

    private OutputStream m_out;
    private InputStream m_in;
    private AuraFR m_FR;

    //Состояние принтера
    private static final int FR_READY = 0;     //ФР готов к получению сообщения
    private static final int FR_READING = 1;   //ФР принемает сообщение
    private static final int FR_FINISH = 2;    //ФР закончил принемать сообщение
    private static final int FR_MASKING = 3;   //ФР производит маскировку байт сообщения
    private static final int FR_CRC = 4;       //ФР шлёт последний байт сообщения с контрольной суммой
    private int m_iStatusPrinter;

    private int iLengthCounter = 0;

    //Интервалы ожидания сообщений от принтера
    private static final int T5 = 2000;             //Задержка 2,0 сек
    private static final int T6 = 1000;              //Задержка 0,5 сек

    //Управляющие символы протокола
    private static final byte[] ENQ = {0x05};       //Запрос
    private static final byte[] EOT = {0x04};       //Конец передачи
    private static final byte[] ACK = {0x06};       //Подтверждение получения сообщения
    private static final byte[] STX = {0x02};       //Начало блока данных
    private static final byte[] NAK = {0x15};       //Отрицание получения сообщения
    private static final byte ETX = 0x03;           //Символ конца блока данных
    private static final byte DLE = 0x10;           //Экранирование управляющих символов

    private Queue<byte[]> m_aLines;
    private ByteArrayOutputStream m_abuffer;

    public DeviceAuraFRComm(String sPortPrinter, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) {
        m_sPort = sPortPrinter;
        m_iPortSpeed = iPortSpeed;
        m_iPortBits = iPortBits;
        m_iPortStopBits = iPortStopBits;
        m_iPortParity = iPortParity;
        
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
        m_iStatusPrinter = 0;
        m_FR = new AuraFR();
    }

    public void connectDevice() {
        try {
            m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort);
            m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", T6);
            m_out = m_CommPortPrinter.getOutputStream();
            m_in = m_CommPortPrinter.getInputStream();
            m_CommPortPrinter.addEventListener(this);
            m_CommPortPrinter.notifyOnDataAvailable(true);
            m_CommPortPrinter.setSerialPortParams(m_iPortSpeed, m_iPortBits, m_iPortStopBits, m_iPortParity);
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
        } catch (Exception e) {
            m_PortIdPrinter = null;
            m_CommPortPrinter = null;
            m_out = null;
            m_in = null;
//            throw new TicketPrinterException(e);
        }

        synchronized (this) {
//            m_iStatusPrinter = 1;
            m_aLines = new LinkedList<byte[]>();
            m_abuffer = new ByteArrayOutputStream();
        }
    }

    public void disconnectDevice() {

        try {
            m_out.close();
            m_in.close();
            m_CommPortPrinter.close();
        } catch (IOException e) {
        }

        synchronized (this) {
//            m_iStatusPrinter = 1;
            // m_iStatus = STATUS_WAITING;
            m_aLines = null;
            m_abuffer = null;
        }

        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    //Команды обмена с принтером
    //Проверка состояния принтера
    public void sendInitMessage() throws TicketPrinterException {
        sendMessage(m_FR.InitMessage(), 1);
    }

    //Вывод звукового сигнала
    public void sendBeepMessage() throws TicketPrinterException {
        sendMessage(m_FR.BeepMessage(), 0);
    }

    public void sendCutTicketMessage(int iFlag) throws TicketPrinterException {
        sendMessage(m_FR.CutTicketMessage(iFlag), 1);
    }

    public void sendTextMessage(String sText) throws TicketPrinterException {
        sendMessage(m_FR.TextMessage(sText), 1);
    }

    public void sendStampTitleReportMessage() throws TicketPrinterException {
        sendMessage(m_FR.StampTitleReportMessage(), 1);
    }

    public void sendOpenDrawerMessage() throws TicketPrinterException {
        sendMessage(m_FR.OpenDrawerMessage(), 1);
    }

    public void sendOpenTicket(int iFlag, int iTypeTicket) throws TicketPrinterException {
        sendMessage(m_FR.OpenTicket(iFlag, iTypeTicket), 1);
    }

    public void sendSelectModeMessage(int iMode) throws TicketPrinterException {
        sendMessage(m_FR.SelectMode(iMode), 1);
    }

    public void sendCancelModeMessage() throws TicketPrinterException {
        sendMessage(m_FR.CancelMode(), 1);
    }

    public void sendCloseTicketMessage(int iFlag, int iType, double dPaid) throws TicketPrinterException {
        sendMessage(m_FR.CloseTicket(iFlag, iType, dPaid), 1);
    }

    public void sendRegistrationLine(int iFlag, double dProductPrice, double dSaleUnits, int iProductSection) throws TicketPrinterException {
        sendMessage(m_FR.RegistrationLine(iFlag, dProductPrice, dSaleUnits, iProductSection), 1);
    }

    public void printXReport(int iType) throws TicketPrinterException {
        sendMessage(m_FR.XReport(iType), 1);
    }

    public void printZReport() throws TicketPrinterException {
        sendMessage(m_FR.ZReport(), 1);
    }

//    private void readCommand(byte[] cmd) throws TicketPrinterException {
//        byte[] b = readLine();
//        if (!checkCommand(cmd, b)) {
//            throw new TicketPrinterException("Command not expected");
//        }
//    }

    private void sendMessage(byte[] bMessage, int iGetAnswer) throws TicketPrinterException {
        writeLine(ENQ);
        System.out.println("ReadLine: " + readLine().length);
//        readCommand(ACK);
        writeLine(bMessage);
        System.out.println("ReadLine: " + readLine().length);
//        readCommand(ACK);
        writeLine(EOT);
        if (iGetAnswer == 1) {
            System.out.println("ReadLine: " + readLine().length);
//        readCommand(ENQ);
            writeLine(ACK);
            System.out.println("ReadLine: " + readLine().length);
//        readCommand(STX);
            writeLine(ACK);
            System.out.println("ReadLine: " + readLine().length);
//        readCommand(EOT);
        }
    }

    private void writeLine(byte[] aline) throws TicketPrinterException {
        if (m_CommPortPrinter == null) {
            throw new TicketPrinterException("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new TicketPrinterException(e);
                }
            }
        }
    }

    private byte[] readLine() throws TicketPrinterException {
        synchronized (this) {

            if (!m_aLines.isEmpty()) {
                return m_aLines.poll();
            }

//            if (m_iStatusPrinter != 0) {
                try {
                    wait(T5);
                } catch (InterruptedException e) {
                }
//                if (m_iStatusPrinter != 0) {
//                    m_iStatusPrinter = 0;

            if (m_aLines.isEmpty()) {
                throw new TicketPrinterException("Timeout");
            } else {
                return m_aLines.poll();
            }
        }
            
    }

//    private boolean checkCommand(byte[] bcommand, byte[] brecieved) {
//        if (bcommand.length == brecieved.length) {
//            for (int i = 0; i < bcommand.length; i++) {
//                if (bcommand[i] != brecieved[i]) {
//                    return false;
//                }
//            }
//            return true;
//        } else {
//            return false;
//        }
//    }

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
                        int b = m_in.read();

                        synchronized(this) {
                            if ((b == ACK[0]  || b == ENQ[0] || b == EOT[0]) && m_iStatusPrinter == FR_READY || (b != ACK[0]  || b != ENQ[0] || b != EOT[0]) && m_iStatusPrinter == FR_FINISH) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_iStatusPrinter = FR_READY;
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                                m_abuffer.reset();
                                notifyAll();
                            } else if (b == STX[0] && m_iStatusPrinter == FR_READY) {
                                m_abuffer.write(b);
                                m_iStatusPrinter = FR_READING;
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                            } else if (b == DLE && m_iStatusPrinter == FR_READING) {
                                m_iStatusPrinter = FR_MASKING;
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                            } else if (m_iStatusPrinter == FR_READING && b != ETX && b != DLE) {
                                m_abuffer.write(b);
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                            } else if (m_iStatusPrinter == FR_MASKING && b == ETX || b == DLE) {
                                m_abuffer.write(b);
                                m_iStatusPrinter = 1;
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                            } else if (b == ETX && m_iStatusPrinter != FR_MASKING) {
                                m_abuffer.write(b);
                                m_iStatusPrinter = FR_FINISH;
                            }
                        }
                    }
                } catch (IOException eIO) {}
                break;
        }
    }
}
