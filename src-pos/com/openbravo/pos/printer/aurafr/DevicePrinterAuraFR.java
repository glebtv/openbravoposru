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

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.TicketFiscalPrinterException;
import com.openbravo.pos.printer.aurafr.command.Beep;
import com.openbravo.pos.printer.aurafr.command.CutPaper;
import com.openbravo.pos.printer.aurafr.command.PrintString;
import com.openbravo.pos.printer.aurafr.command.ReadFullStatus;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class DevicePrinterAuraFR extends DeviceAuraFR implements DevicePrinter {

    private AuraFRReaderWritter m_CommOutputPrinter;
    private String m_sName;
    private String sLine = "";
    private PrintString cmd_PrintString;
    private CutPaper cmd_CutPaper;
    private ReadFullStatus cmd_ReadFullStatus;

    // Creates new TicketPrinter
    public DevicePrinterAuraFR(String sDevicePrinterPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) throws TicketFiscalPrinterException {

        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputPrinter = new DeviceAuraFRComm(sDevicePrinterPort, iPortSpeed, iPortBits, iPortStopBits, iPortParity);
        m_CommOutputPrinter.connectDevice();

        cmd_ReadFullStatus = new ReadFullStatus();
        GenerateCommand(m_CommOutputPrinter, cmd_ReadFullStatus);

        GenerateCommand(m_CommOutputPrinter, new Beep());

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
            m_CommOutputPrinter.connectDevice();
            GenerateCommand(m_CommOutputPrinter, new Beep());
        } catch (TicketFiscalPrinterException e) {
        }
    }

    public void printImage(BufferedImage image) {
    }

    public void printBarCode(String type, String position, String code) {
    }

    public void beginLine(int iTextSize) {
    }

    //Печать текста
    @Override
    public void printText(int iStyle, String sText){
        sLine = sLine.concat(sText);
    }

    public void endLine() {
        try {
            cmd_PrintString = new PrintString(sLine);
            GenerateCommand(m_CommOutputPrinter, cmd_PrintString);
            CheckErrorAnswer(m_CommOutputPrinter, cmd_PrintString.getErrorAnswer());
            sLine = "";
        } catch (TicketFiscalPrinterException ex) {
        }
    }

    //Окончание печати чека
    public void endReceipt() {
        try {
//            m_CommOutputPrinter.sendStampTitleReportMessage();
         
            for (int iSpcStr = 0; iSpcStr < SPACE_STRINGE_BEFORE_CUT; iSpcStr++) {
                cmd_PrintString = new PrintString("");
                GenerateCommand(m_CommOutputPrinter, cmd_PrintString);
                CheckErrorAnswer(m_CommOutputPrinter, cmd_PrintString.getErrorAnswer());
            }
            
            cmd_CutPaper = new CutPaper(0);
            GenerateCommand(m_CommOutputPrinter, cmd_CutPaper);
            CheckErrorAnswer(m_CommOutputPrinter, cmd_CutPaper.getErrorAnswer());

            GenerateCommand(m_CommOutputPrinter, new Beep());
        } catch (TicketFiscalPrinterException ex) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }

    //Открытие денежного ящика
    public void openDrawer() {
        try {
            m_CommOutputPrinter.connectDevice();
//            m_CommOutputPrinter.sendInitMessage();
//            m_CommOutputPrinter.sendOpenDrawerMessage();
        } catch (TicketFiscalPrinterException e) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }

    // Отрезание бумаги
    public void cutPaper(boolean complete) {
    }
}
