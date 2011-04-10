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

package com.openbravo.pos.scale;

import gnu.io.*;
import java.io.*;
import java.util.BitSet;
import java.util.TooManyListenersException;

public class ScaleMassaK implements Scale, SerialPortEventListener {

    private String m_sPortScale;
    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private OutputStream m_out;
    private InputStream m_in;
    private static final int SCALE_READY = 0;
    private static final int SCALE_READING = 1;
    private double m_dWeightBuffer;
    private int m_iStatusScale;

    public ScaleMassaK(String sPortPrinter) {
        m_sPortScale = sPortPrinter;
        m_out = null;
        m_in = null;
        m_iStatusScale = SCALE_READY;
        m_dWeightBuffer = 0.0;
    }

    public Double readWeight() {

        synchronized (this) {
            if (m_iStatusScale != SCALE_READY) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                }
                if (m_iStatusScale != SCALE_READY) {
                    m_iStatusScale = SCALE_READY;
                }
            }
            
            m_dWeightBuffer = 0.0;
            
            write(new byte[]{0x48});

            flush();

            try {                
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_iStatusScale == SCALE_READY) {
                // a value as been readed.
                double dWeight = m_dWeightBuffer / 1000.0;
                m_dWeightBuffer = 0.0;
                return new Double(dWeight);
            } else {
                m_iStatusScale = SCALE_READY;
                m_dWeightBuffer = 0.0;
                return new Double(0.0);
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
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortScale); // Tomamos el puerto
                m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto
                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura
                m_in = m_CommPortPrinter.getInputStream();
                m_CommPortPrinter.addEventListener(this);
                m_CommPortPrinter.notifyOnDataAvailable(true);
                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_EVEN); // Configuramos el puerto
            }
            m_out.write(data);
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
                        byte[] b = new byte[2];
                        m_in.read(b);
                        int iSign = 1;
                        BitSet bits = new BitSet();

                        if (m_iStatusScale == SCALE_READY) {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READING;
                        }

                        for (int j = 0; j < 1 * 8; j++) {
                            if ((b[1] & (1 << (j % 8))) > 0) {
                                bits.set(j);
                            }
                        }

                        if (bits.get(7) == true) {
                            byte bBuffer = 0;
                            iSign = -1;
                            bits.set(7, false);
                            for (int j = 0; j < bits.length(); j++) {
                                if (bits.get(j)) {
                                    bBuffer |= 1 << (j % 8);
                                }
                            }
                            b[1] = bBuffer;
                        }

                        for (int i = 1; i >= 0; i--) {
                            int iBuffer = 0;
                            if (b[i] < 0) {
                                iBuffer = b[i] + 256;
                            } else {
                                iBuffer = b[i];
                            }
                            System.out.println("iBuffer: " + iBuffer);
                            m_dWeightBuffer = m_dWeightBuffer * 256 + (int) iBuffer;
                        }

                        m_dWeightBuffer = m_dWeightBuffer * iSign;

                        System.out.println("Weigth: " + m_dWeightBuffer);

                        if (m_dWeightBuffer >= 0.0 && m_dWeightBuffer <= 15050.0) {
                            synchronized (this) {
                                m_iStatusScale = SCALE_READY;
                                notifyAll();
                            }
                        } else {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READY;
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }
    }
}
