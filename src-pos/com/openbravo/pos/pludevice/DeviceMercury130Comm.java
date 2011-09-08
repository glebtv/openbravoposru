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

package com.openbravo.pos.pludevice;

import com.openbravo.pos.pludevice.DevicePLUs;
import com.openbravo.pos.pludevice.DevicePLUsException;
import com.openbravo.pos.pludevice.ProductDownloaded;
import gnu.io.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;

public class DeviceMercury130Comm implements DevicePLUs, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;

    private String m_sPort;
    private Integer m_iPortSpeed;
    private Integer m_iPortBits;
    private Integer m_iPortStopBits;
    private Integer m_iPortParity;    
    private OutputStream m_out;
    private InputStream m_in;

    private static final byte[] COMMAND_ACK = new byte[] {(byte) 0xf0, (byte) 0x0f};
    private static final byte[] COMMAND_OFF = new byte[] {0x00, 0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x34};
    private static final byte[] COMMAND_BUFF = new byte[] {0x03, 0x00, (byte) 0xc0, 0x01, (byte) 0x42, (byte) 0x6f, (byte) 0x78, 0x20, (byte) 0x80, (byte) 0x2d, (byte) 0xbf, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xdc, (byte) 0x41, (byte) 0x0f, 0x00, 0x08, 0x0f, (byte) 0x84, 0x39, 0x50, 0x35, 0x00, (byte) 0x90, (byte) 0xf8, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0x79};
    private static final byte[] COMMAND_W_IN = new byte[] {(byte) 0x80, (byte) 0x80};

    private Queue<byte[]> m_aLines;
    private ByteArrayOutputStream m_abuffer;

    private int m_iProductOrder;

    DeviceMercury130Comm(String sPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) {
        m_sPort = sPort;
        m_iPortSpeed = iPortSpeed;
        m_iPortBits = iPortBits;
        m_iPortStopBits = iPortStopBits;
        m_iPortParity = iPortParity;        
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
//            m_CommPortPrinter.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
            throw new DevicePLUsException(e);
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
        writeLine(COMMAND_BUFF);
        readCommand(COMMAND_ACK);
        m_iProductOrder = 0;
    }

    public void stopUploadProduct() throws DevicePLUsException {
        writeLine(COMMAND_OFF);
    }

    public void sendProduct(String sName, String sCode, Double dPrice, int iCurrentPLU, int iTotalPLUs, String sBarcode) throws DevicePLUsException {

      m_iProductOrder++;

        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        try{
            //Номер товара в диапозоне. Смещение 0. Длина 2 байта.
            if (m_iProductOrder > 0 || m_iProductOrder <= 9999) {
                lineout.write(convertHEX(m_iProductOrder), 0, 2);
            } else {
                lineout.write(0x00);
                lineout.write(0x00);
            }

            // Признак записи товара. Смещение 2. Длина 2 байта.
            lineout.write(COMMAND_W_IN, 0, 2);

            // Наименование товара. Смещение 4. Длина 24.
            for (int i = sName.length(); i < 24; i++) {
                sName = sName + " ";
            }
            lineout.write(convertASCII(sName), 0, 24);

            // Цена товара. Смещение 28. Длина 4.
            lineout.write(convertHEX((long) (dPrice * 100)), 0, 4);

            // Налоговая группа товара. Смещение 32. Длина 1.
            lineout.write(0x01);

            // Секция товара. Смещение 33. Длина 1.
            lineout.write(0x01);

            // Штрих-кода товара. Смещение 34. Длина 7.
            lineout.write(convertBCD(sCode), 0, 7);

            lineout.write(0x00);

            // Атрибут разрешения продажи товара. Смещение 43. Длина 1.
            lineout.write(0x01);

            // Атрибут быстрой продажи товара. Смещение 44. Длина 1.
            lineout.write(0x01);

            lineout.write(0x00);
            lineout.write(0x00);
            lineout.write(0x00);

            // Контрольная сумма. Смещение 47. Длина 1.
            lineout.write(calcCheckSum(lineout.toByteArray()));
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        writeLine(lineout.toByteArray());
        lineout = null;
        readCommand(COMMAND_ACK);
    }

    public void startDownloadProduct() throws DevicePLUsException {

    }
    
    public ProductDownloaded recieveProduct() throws DevicePLUsException {
        return null;
    }
    
    private void readCommand(byte[] cmd) throws DevicePLUsException {
        byte[] b = readLine();
        if (!checkCommand(cmd, b)) {
            throw new DevicePLUsException("Command not expected");
        }
    }

    private void writeLine(byte[] aline) throws DevicePLUsException {
        if (m_CommPortPrinter == null) {
            throw new DevicePLUsException("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new DevicePLUsException(e);
                }
            }
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

    private static byte[] convertHEX(long sdata) {

        byte[] result = new byte[24];

        if (sdata <= 255.00 && sdata >= 0) {
            result[0] = (byte) sdata;
        } else {
            int j = 0;
            String buff = Long.toHexString(Long.reverseBytes(sdata));
            for (int i = 0; i < buff.length() / 2; i++) {
                result[i] = (byte) Integer.parseInt(buff.substring(j, j + 2), 16);
                j = j + 2;
            }
        }
        return result;
    }

    private static byte[] convertASCII(String sdata) {
        byte[] result = new byte[sdata.length()];
        for (int i = 0; i < sdata.length(); i++) {
            char c = sdata.charAt(i);
            if ((c >= 0x0020) && (c < 0x0080)) {
                result[i] = (byte) c;
            } else {
                result[i] = convertCyrillic(c);
            }
        }
        return result;
    }

    private static byte convertCyrillic(char sdata) {
        switch (sdata) {

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
            case '\u0428': return (byte) 0x98;// Ш
            case '\u0429': return (byte) 0x99;// Щ
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
            case '\u0440': return (byte) 0xB0;// р
            case '\u0441': return (byte) 0xB1;// с
            case '\u0442': return (byte) 0xB2;// т
            case '\u0443': return (byte) 0xB3;// у
            case '\u0444': return (byte) 0xB4;// ф
            case '\u0445': return (byte) 0xB5;// х
            case '\u0446': return (byte) 0xB6;// ц
            case '\u0447': return (byte) 0xB7;// ч
            case '\u0448': return (byte) 0xB8;// ш
            case '\u0449': return (byte) 0xB9;// щ
            case '\u044A': return (byte) 0xBA;// ъ
            case '\u044B': return (byte) 0xBB;// ы
            case '\u044C': return (byte) 0xBC;// ь
            case '\u044D': return (byte) 0xBD;// ы
            case '\u044E': return (byte) 0xBE;// ю
            case '\u044F': return (byte) 0xBF;// я

            default: return (byte) 0x3F; // ? Not valid character.
        }
    }

    private static byte[] convertBCD(String sdata) {
        int j = 0;
        int x = sdata.length();
        if (x != 13) {
            sdata = "FFFFFFFFFFFFF";
        } else if (x == 8) {
            sdata = sdata + "FFFFF";
        }
        sdata = sdata + "F";
        byte[] result = new byte[sdata.length() / 2];
        for (int i = 0; i < sdata.length(); i++) {
            result[j] = (byte) Integer.parseInt(sdata.substring(i + 1, i + 2) + sdata.substring(i, i + 1), 16);
            j = j + 1;
            i++;
        }
        return result;
    }

    private byte[] calcCheckSum(byte[] adata) {
        int isum = 0;
        for (int i = 0; i < adata.length; i++) {
            byte b = (byte) (adata[i] ^ i);
            isum = isum + b;
        }
        byte low = (byte) (isum & 0x00FF);
        byte[] result = new byte[2];
        result[0] = low;
        return result;
    }

    private boolean checkCommand(byte[] bcommand, byte[] brecieved) {
        if (bcommand.length == brecieved.length) {
            for (int i = 0; i < bcommand.length; i++) {
                if (bcommand[i] != brecieved[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
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
                        int b = m_in.read();
                        synchronized(this) {
                            if (b == 0x0F) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_abuffer.reset();
                                notifyAll();
                            } else {
                                m_abuffer.write(b);
                            }
                        }
                    }
                } catch (IOException eIO) {}
                break;
        }
    }
}
