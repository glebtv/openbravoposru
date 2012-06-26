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

import com.openbravo.pos.printer.TicketFiscalPrinterException;
import com.openbravo.pos.printer.aurafr.command.PrinterCommand;
import gnu.io.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;

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
    private DeviceAuraFR m_FR;

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
    private static final int T6 = 500;              //Задержка 0,5 сек

    //Управляющие символы протокола
    private static final byte[] ENQ = {0x05};       //Запрос
    private static final byte[] EOT = {0x04};       //Конец передачи
    private static final byte[] ACK = {0x06};       //Подтверждение получения сообщения
    private static final byte[] STX = {0x02};       //Начало блока данных
    private static final byte[] NAK = {0x15};       //Отрицание получения сообщения
    private static final byte ETX = 0x03;           //Символ конца блока данных
    private static final byte DLE = 0x10;           //Экранирование управляющих символов
    private static final byte[] PASS = {0x00, 0x00};
    
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
        m_FR = new DeviceAuraFR();
    }

    public void connectDevice() {
        try {
            m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort);
            m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", T6);
            m_CommPortPrinter.setOutputBufferSize(1024);
            m_CommPortPrinter.setInputBufferSize(1024);
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
    
//    private void readCommand() throws TicketPrinterException {
//        byte[] b = readLine();
////        System.out.println("ReadLine: " + b.length);        
//        System.out.println("Packet(" + b.length + "):" + ByteArrayUtils.getHexString(b));
////        if (!checkCommand(cmd, b)) {
////            throw new TicketPrinterException("Command not expected");
////        }
//    }

    private boolean checkCommand(byte[] bcommand, byte[] brecieved) {
        if (bcommand[0] == brecieved[0]) {
            return true;
        } else {
            return false;
        }
    }
    
    public void sendMessage(String sCsrPwd, PrinterCommand oPrnCmd) throws TicketFiscalPrinterException {
        writeLine(ENQ);
        for (int i = 5; i > 0; i--) {
            if (checkCommand(readLine(), ACK)) {
                i = 0;
                writeLine(oPrnCmd.createMessage(sCsrPwd));
                for (int j = 10; j > 0; j--) {
                    j = 0;
                    if (checkCommand(readLine(), ACK)) {
                        writeLine(EOT);
                        if (oPrnCmd.isAnswer()) {
                            checkCommand(readLine(), ENQ);
                            writeLine(ACK);
                            oPrnCmd.readAnswer(readLine());
                            writeLine(ACK);
                            checkCommand(readLine(), EOT);
                        }
                    } else {
                        System.out.println("Not send MESSAGE");
                    }
                }
            } else {
                System.out.println("Not get ACK");
            }
        }
    }
    
    private void writeLine(byte[] aline) throws TicketFiscalPrinterException {
        if (m_CommPortPrinter == null) {
            throw new TicketFiscalPrinterException("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new TicketFiscalPrinterException(e);
                }
            }
        }
    }

    private byte[] readLine() throws TicketFiscalPrinterException {
        synchronized (this) {

            if (!m_aLines.isEmpty()) {
                return m_aLines.poll();
            }
            
            try {
                wait(T5);
            } catch (InterruptedException e) {
            }
            
            if (m_aLines.isEmpty()) {
                throw new TicketFiscalPrinterException("Timeout");
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

                        if ((b == ACK[0] || b == ENQ[0] || b == EOT[0]) && m_iStatusPrinter == FR_READY || (b != ACK[0] || b != ENQ[0] || b != EOT[0]) && m_iStatusPrinter == FR_FINISH) {
                            synchronized (this) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_iStatusPrinter = FR_READY;
//                                System.out.println("b: " + Integer.toHexString(b) + " SP: " + m_iStatusPrinter);
                                m_abuffer.reset();
                                notifyAll();
                            }
                        } else if (b == STX[0] && m_iStatusPrinter == FR_READY) {
                            m_abuffer.write(b);
                            m_iStatusPrinter = FR_READING;
                        } else if (b == DLE && m_iStatusPrinter == FR_READING) {
                            m_iStatusPrinter = FR_MASKING;
                        } else if (m_iStatusPrinter == FR_READING && b != ETX && b != DLE) {
                            m_abuffer.write(b);
                        } else if (m_iStatusPrinter == FR_MASKING && b == ETX || b == DLE) {
                            m_abuffer.write(b);
                            m_iStatusPrinter = FR_READING;
                        } else if (b == ETX && m_iStatusPrinter != FR_MASKING) {
                            m_abuffer.write(b);
                            m_iStatusPrinter = FR_FINISH;
                        }
                    }

                } catch (IOException eIO) {
                }
                break;
        }
    }
}
