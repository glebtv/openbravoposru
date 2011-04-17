//    Исходный код для Openbravo POS, автоматизированной системы продаж для работы
//    с сенсорным экраном, предоставлен ТОО "Норд-Трейдинг ЛТД", Республика Казахстан,
//    в период 2008-2011 годов на условиях лицензионного соглашения GNU LGPL.
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

package com.openbravo.pos.massakvpm;

/**
 * @author Andrey Svininykh svininykh@gmail.com
 */

import gnu.io.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceScaleVPMComm implements DeviceScaleVPM, SerialPortEventListener {

    private static final byte CMD_TCP_DFILE = (byte) 0x82; // Загрузить в весы запись
    private static final byte CMD_TCP_ACK_DFILE = 0x42; // Запись файла загружена в весы
    private static final byte CMD_TCP_BAD_DFILE = 0x43; // Запись с неожидаемым номером или типом файла загружена в весы

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;

    private String m_sPort;
    private OutputStream m_out;
    private InputStream m_in;

    private Queue<byte[]> m_aLines;
    private ByteArrayOutputStream m_abuffer;

    private int m_iProductOrder;

    private MassaKVPM m_ScaleVPM;

    public DeviceScaleVPMComm(String sPort) {
        m_sPort = sPort;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

        public void connectDevice() throws DeviceScaleVPMException {
        try {
            m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort);
            m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000);
            m_out = m_CommPortPrinter.getOutputStream();
            m_in = m_CommPortPrinter.getInputStream();
            m_CommPortPrinter.addEventListener(this);
            m_CommPortPrinter.notifyOnDataAvailable(true);
            m_CommPortPrinter.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN);
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
            throw new DeviceScaleVPMException(e);
        }

        synchronized (this) {
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
            m_aLines = null;
            m_abuffer = null;
        }

        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    public void startUploadProduct() throws DeviceScaleVPMException {
//        writeLine();           // CMD_UDP_POLL
//        readCommand();         // CMD_UDP_RES_ID
        // CMD_TCP_RESET_FILES
        // CMD_TCP_ACK_RESET_FILES
        m_iProductOrder = 0;
    }

    public void stopUploadProduct() throws DeviceScaleVPMException {
//        writeLine(COMMAND_OFF);
    }

    public void sendProduct(String sName, String sCode, Double dPrice, int iPLUs) throws DeviceScaleVPMException {
        
        m_iProductOrder++; //Номер товара в диапозоне.

        if (m_iProductOrder > 0 || m_iProductOrder <= 20000) {
            try {
                writeLine(m_ScaleVPM.CreateDATAMessage(CMD_TCP_DFILE, m_ScaleVPM.CreatePLUMessage(sCode, dPrice, sName), m_iProductOrder, iPLUs));
            } catch (IOException ex) {
            }
        } else {
             throw new DeviceScaleVPMException("Количество PLU выходит из диапазона поддерживаемого устройством");
        }
    readCommand(CMD_TCP_ACK_DFILE);
    }

    private void readCommand(byte cmd) throws DeviceScaleVPMException {
        byte[] b = readLine();
        if (!checkCommand(cmd, b[5])) {
            throw new DeviceScaleVPMException("Command not expected");
        }
    }

    private void writeLine(byte[] aline) throws DeviceScaleVPMException {
        if (m_CommPortPrinter == null) {
            throw new DeviceScaleVPMException("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new DeviceScaleVPMException(e);
                }
            }
        }
    }

    private byte[] readLine() throws DeviceScaleVPMException {
        synchronized (this) {

            if (!m_aLines.isEmpty()) {
                return m_aLines.poll();
            }

            try {
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_aLines.isEmpty()) {
                throw new DeviceScaleVPMException("Timeout");
            } else {
                return m_aLines.poll();
            }
        }
    }


    private boolean checkCommand(byte bcommand, byte brecieved) {
//        if (bcommand.length == brecieved.length) {
//            for (int i = 0; i < bcommand.length; i++) {
                if (bcommand != brecieved) {
                    return false;
                } else return true;
//            }
//            return true;
//        } else {
//            return false;
//        }
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
                        byte[] b = new byte[6];
                        m_in.read(b);
                        synchronized(this) {
                            if (b[5] == CMD_TCP_ACK_DFILE || b[5] == CMD_TCP_BAD_DFILE) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_abuffer.reset();
                                notifyAll();
                            }
//                            } else {
//                                m_abuffer.write(b);
//                            }
                        }
                    }
                } catch (IOException eIO) {}
                break;
        }
    }

}
