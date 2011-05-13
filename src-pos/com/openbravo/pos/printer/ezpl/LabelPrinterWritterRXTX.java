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

package com.openbravo.pos.printer.ezpl;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

// import javax.comm.*; // Java comm library
import gnu.io.*; // RXTX comm library
import java.io.*;
import com.openbravo.pos.printer.*;

public class LabelPrinterWritterRXTX extends LabelPrinterWritter /* implements SerialPortEventListener */ {
    
    private CommPortIdentifier m_PortIdPrinter;
    private CommPort m_CommPortPrinter;  
    
    private String m_sPortPrinter;
    private OutputStream m_out;
    
    /** Creates a new instance of PrinterWritterComm */
    public LabelPrinterWritterRXTX(String sPortPrinter) throws TicketPrinterException {
        m_sPortPrinter = sPortPrinter;
        m_out = null; 
    }
    
    protected void internalWrite(byte[] data) {
        try {  
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortPrinter); // Tomamos el puerto                   
                m_CommPortPrinter = m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto       

                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura   

                if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    ((SerialPort)m_CommPortPrinter).setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
                    // this line prevents the printer tmu220 to stop printing after +-18 lines printed. Bug 8324
                    // But if added a regression error appears. Bug 9417, Better to keep it commented.
                    // ((SerialPort)m_CommPortPrinter).setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN);
// Not needed to set parallel properties
//                } else if (m_PortIdPrinter.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
//                    ((ParallelPort)m_CommPortPrinter).setMode(1);
                }
            }
            m_out.write(data);
        } catch (NoSuchPortException e) {
            System.err.println(e);
        } catch (PortInUseException e) {
            System.err.println(e);
        } catch (UnsupportedCommOperationException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }      
    }
    
    protected void internalFlush() {
        try {  
            if (m_out != null) {
                m_out.flush();
            }
        } catch (IOException e) {
            System.err.println(e);
        }    
    }
    
    protected void internalClose() {
        try {  
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
                m_CommPortPrinter = null;
                m_PortIdPrinter = null;
            }
        } catch (IOException e) {
            System.err.println(e);
        }    
    }
}
