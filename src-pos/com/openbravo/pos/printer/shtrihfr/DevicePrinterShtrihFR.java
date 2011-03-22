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

import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;

public class DevicePrinterShtrihFR implements DevicePrinter {

    private ShtrihFRReaderWritter m_CommOutputPrinter;
    private String m_sName;

    // Creates new TicketPrinter
    public DevicePrinterShtrihFR(String sDevicePrinterPort) throws TicketPrinterException {

        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputPrinter = new DeviceShtrihFRComm(sDevicePrinterPort);
//        m_CommOutputPrinter.getTypePrinter();
//        m_CommOutputPrinter.sendInitMessage();
        m_CommOutputPrinter.sendBeepMessage();
        m_CommOutputPrinter.disconnectDevice();
    }

    public String getPrinterName() {
        return m_sName;
    }

    public String getPrinterDescription() {
        return null;
    }

    public JComponent getPrinterComponent() {
        return null;
    }

    public void reset() {
    }

    //Начало печати чека
    public void beginReceipt() {
        try {
//            m_CommOutputPrinter.sendInitMessage();
            m_CommOutputPrinter.sendBeepMessage();
            m_CommOutputPrinter.sendPrintImageMessage();
        } catch (TicketPrinterException e) {
        }
    }

    public void printImage(BufferedImage image) {
    }

    public void printBarCode(String type, String position, String code) {
    }

    public void beginLine(int iTextSize) {
    }

    //Печать текста
    public void printText(int iStyle, String sText) {
        try {
            m_CommOutputPrinter.sendTextMessage(sText);
        } catch (TicketPrinterException e) {
        }
    }

    public void endLine() {
    }

    //Окончание печати чека
    public void endReceipt() {
        try {
//            m_CommOutputPrinter.sendStampTitleReportMessage();
            m_CommOutputPrinter.sendTicketUpMessage();
            m_CommOutputPrinter.sendTicketCutMessage();
            m_CommOutputPrinter.sendBeepMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }

    //Открытие денежного ящика
    public void openDrawer() {
        try {
            m_CommOutputPrinter.sendInitMessage();
            m_CommOutputPrinter.sendOpenDrawerMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }
}

