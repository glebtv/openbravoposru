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

package com.openbravo.pos.pludevice.massakvpm;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

//import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.pludevice.DevicePLUs;
import com.openbravo.pos.pludevice.DevicePLUsException;
import com.openbravo.pos.pludevice.ProductDownloaded;
import com.openbravo.pos.util.ByteArrayUtils;
import gnu.io.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;
//import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceScaleVPMComm implements DevicePLUs, SerialPortEventListener {

    protected static Logger logger = Logger.getLogger("com.openbravo.pos.pludevice.massakvpm");
    
    private static final byte CMD_TCP_DFILE = (byte) 0x82; // Загрузить в весы запись
    private static final byte CMD_UDP_POLL =  (byte) 0x00; // Запрос о наличии подключенных весов
    private static final byte CMD_UDP_RES_ID = (byte) 0x01; // Ответ с информацией о весах
    private static final byte CMD_TCP_ACK_DFILE = 0x42; // Запись файла загружена в весы
    private static final byte CMD_TCP_BAD_DFILE = 0x43; // Запись с неожидаемым номером или типом файла загружена в весы



    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;

    private String m_sDevice, m_sPort;
    private Integer m_iPortSpeed;
    private Integer m_iPortBits;
    private Integer m_iPortStopBits;
    private Integer m_iPortParity;     
    private OutputStream m_out;
    private InputStream m_in;

    private Queue<byte[]> m_aLines;
    private ByteArrayOutputStream m_abuffer;

//    private int m_iProductOrder;

    private int m_iCounter;
    private int m_iInMesSize;

    private MassaKVPM m_ScaleVPM;

    public DeviceScaleVPMComm(String sDevice, String sPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) {
        m_sDevice = sDevice;        
        m_iPortSpeed = iPortSpeed;
        m_iPortBits = iPortBits;
        m_iPortStopBits = iPortStopBits;
        m_iPortParity = iPortParity;        
        m_sPort = sPort;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

        public void connectDevice() throws DevicePLUsException {
        try {
            m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort);
            m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000);
            m_out = m_CommPortPrinter.getOutputStream();
            m_in = m_CommPortPrinter.getInputStream();
            m_CommPortPrinter.addEventListener(this);
            m_CommPortPrinter.notifyOnDataAvailable(true);
//            m_CommPortPrinter.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
//            throw new DeviceScaleVPMException(e);
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

    public void startUploadProduct() throws DevicePLUsException {
        m_iCounter = 0;
        m_iInMesSize = 0;
        try {
            writeLine(m_ScaleVPM.CreateUDPMessage(CMD_UDP_POLL));
        } catch (IOException ex) {
        }
        readCommand(CMD_UDP_RES_ID);
    }

    public void stopUploadProduct() throws DevicePLUsException {

    }

    public void sendProduct(String sName, String sCode, Double dPriceBuy, Double dPriceSell, int iCurrentPLU, int iTotalPLUs, String sBarcode) throws DevicePLUsException {
        m_iCounter = 0;
        m_iInMesSize = 0;
        if (iTotalPLUs > 20000) {
            throw new DevicePLUsException("PLUs exceed an accessible range");            
        }
        
        if (!sCode.substring(0,3).equals(sBarcode) || sCode.length() != 7) { // Сделано исходя из сушествующей логике работы с весовыми этикетками.
            sName = "";
            sCode = "0000000";
            dPriceSell = 0.0;
        }

        try {
            writeLine(m_ScaleVPM.CreateDATAMessage(CMD_TCP_DFILE, m_ScaleVPM.CreatePLUMessage(sCode, dPriceSell, sName), iCurrentPLU, iTotalPLUs));
        } catch (IOException ex) {
        }
    readCommand(CMD_TCP_ACK_DFILE);
    }

    private void writeLine(byte[] aline) throws DevicePLUsException {
        if (m_CommPortPrinter == null) {
            throw new DevicePLUsException("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    logger.info("Device: " + m_sDevice + ", Message size: " + aline.length + ", Send line:" + ByteArrayUtils.getHexString(aline));
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new DevicePLUsException(e);
                }
            }
        }
    }

    public void startDownloadProduct() throws DevicePLUsException {

    }
    
    public ProductDownloaded recieveProduct() throws DevicePLUsException {
        return null;
    }
    
    private void readCommand(byte cmd) throws DevicePLUsException {
        byte[] b = readLine();
        logger.info("Device: " + m_sDevice + " Message size: " + b.length + " Read line:" + ByteArrayUtils.getHexString(b));
        if (!checkCommand(cmd, b[5])) {
            throw new DevicePLUsException("Command not expected");
        }
    }

    private byte[] readLine() throws DevicePLUsException {
        synchronized (this) {

            if (!m_aLines.isEmpty()) {
                return m_aLines.poll();
            }

            try {
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_aLines.isEmpty()) {
                throw new DevicePLUsException("Timeout");
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
                        int b = m_in.read();
                        m_iCounter++;
//                        System.out.println("iCounter = " + m_iCounter);
                        synchronized (this) {
                            if (m_iCounter >= 8 && m_iCounter == m_iInMesSize) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_abuffer.reset();
                                notifyAll();
                            } else {
                                m_abuffer.write(b);
//                                System.out.println("m_iMessageInSize = " + m_iMessageInSize);
                                if (m_iCounter == 4) {
                                    m_iInMesSize = m_iCounter + b + 3;
                                } else if (m_iCounter == 5) {
                                    m_iInMesSize = m_iInMesSize + b * 256;
                            }
                        }
                    }
                    }
                } catch (IOException eIO) {
                }
                break;
        }
    }

}
