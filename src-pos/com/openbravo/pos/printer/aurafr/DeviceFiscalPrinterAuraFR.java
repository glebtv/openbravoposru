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

import com.openbravo.pos.printer.shtrihfr.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DeviceFiscalPrinterAuraFR implements DeviceFiscalPrinter {

    private AuraFRReaderWritter m_CommOutputFiscal;
    private String m_sName;
    private String m_sTicketType;

    // Creates new TicketPrinter
    public DeviceFiscalPrinterAuraFR(String sDevicePrinterPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) throws TicketPrinterException {
        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputFiscal = new DeviceAuraFRComm(sDevicePrinterPort, iPortSpeed, iPortBits, iPortStopBits, iPortParity);
//        m_CommOutputFiscal.getTypePrinter();
        m_CommOutputFiscal.connectDevice();
        m_CommOutputFiscal.sendInitMessage();
        m_CommOutputFiscal.sendCancelModeMessage();
        m_CommOutputFiscal.sendBeepMessage();
        m_CommOutputFiscal.disconnectDevice();
    }

    public String getFiscalName() {
        return m_sName;
    }

    public JComponent getFiscalComponent() {
        return null;
    }

    public void reset() {
    }

    //Начало печати чека
    public void beginReceipt(String sType, String sCashier) {
        m_sTicketType = sType;
        try {            
            m_CommOutputFiscal.connectDevice();
//            m_CommOutputFiscal.sendInitMessage();
            m_CommOutputFiscal.sendSelectModeMessage(1);
            m_CommOutputFiscal.sendBeepMessage();
            m_CommOutputFiscal.sendOpenTicket(0, m_sTicketType);
        } catch (TicketPrinterException e) {
        }
    }

    // Печать строки продажи или возврата по товару
    public void printLine(String sproduct, double dprice, double dunits, int taxinfo) {
        try {
            m_CommOutputFiscal.sendTextMessage(sproduct);
            if (dprice >= 0 && dunits >= 0 && m_sTicketType.equals("sale")) {
                m_CommOutputFiscal.sendRegistrationLine(0, dprice / dunits, dunits, taxinfo);
            } else if (dprice < 0 && dunits < 0 && m_sTicketType.equals("refund")) {
                m_CommOutputFiscal.sendRefundLine(0, -1.0 * (dprice / dunits), -1.0 * dunits);
            } else {
                m_CommOutputFiscal.sendTextMessage("Error in ticket line.");
            }
        } catch (TicketPrinterException e) {
        }
    }

    //Печать текста
    public void printMessage(String sText) {
        try {
            m_CommOutputFiscal.sendTextMessage(sText);
        } catch (TicketPrinterException e) {
        }
    }


    public void endLine() {
    }

    //Окончание печати чека
    public void endReceipt() {
        try {
            m_CommOutputFiscal.sendCancelModeMessage();
            m_CommOutputFiscal.sendBeepMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputFiscal.disconnectDevice();
    }

    //Печать итоговой оплаты по чеку
    public void printTotal(String sPayment, double dpaid) {
        try {
            m_CommOutputFiscal.sendTextMessage(sPayment);
            if (dpaid >= 0 && m_sTicketType.equals("sale")) {
                m_CommOutputFiscal.sendCloseTicketMessage(0, 1, dpaid);
            } else if (dpaid >= 0 && m_sTicketType.equals("refund")) {
                m_CommOutputFiscal.sendCloseTicketMessage(0, 1, 0);
            } else {
                m_CommOutputFiscal.sendTextMessage("Error in ticket total.");
            }
        } catch (TicketPrinterException e) {
        }
    }

    //Отрезание бумаги
    public void cutPaper(boolean complete) {
    }

    //Печать X-отчёта
    public void printXReport() {
        try {
            m_CommOutputFiscal.connectDevice();
            m_CommOutputFiscal.sendCancelModeMessage();
            m_CommOutputFiscal.sendSelectModeMessage(2);
            m_CommOutputFiscal.printXReport(1);
            m_CommOutputFiscal.sendCancelModeMessage();
//            m_CommOutputFiscal.sendBeepMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputFiscal.disconnectDevice();
    }

    //Печать Z-отчёта
    public void printZReport() {
        try {
            m_CommOutputFiscal.connectDevice();
            m_CommOutputFiscal.sendCancelModeMessage();
            m_CommOutputFiscal.sendSelectModeMessage(3);
            m_CommOutputFiscal.printZReport();
            m_CommOutputFiscal.sendCancelModeMessage();
//            m_CommOutputFiscal.sendBeepMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputFiscal.disconnectDevice();
    }
}

