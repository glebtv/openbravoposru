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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import com.openbravo.pos.printer.DevicePrinter;
import com.openbravo.pos.printer.TicketPrinterException;

import com.openbravo.pos.printer.shtrihfr.DeviceShtrihFR;

import com.shtrih.fiscalprinter.*;


/**
 * @author: Gennady Kovalev <gik@bigur.ru>
 */

public class DevicePrinterShtrihFR extends DeviceShtrihFR implements DevicePrinter {

    private String sLine;

    // Creates new TicketPrinter
    public DevicePrinterShtrihFR(String sDevicePrinterPort) {
        super(sDevicePrinterPort);
    }

    public String getPrinterName() {
        return m_sSerialDevice;
    }

    public String getPrinterDescription() {
        return null;
    }

    public JComponent getPrinterComponent() {
        return null;
    }

    // Сброс принтера
    public void reset() {
    }

    // Начало печати чека
    public void beginReceipt() {
        logger.finer("Begin printing receipt started");
        logger.finer("Begin printing receipt ended");
    }

    public void printImage(BufferedImage image) {
        logger.finer("Printing image started");
        logger.finer("Printing image ended");
    }

    public void printBarCode(String type, String position, String code) {
        logger.finer("Printing bar code started");
        logger.finer("Printing bar code ended");
    }

    public void beginLine(int iTextSize) {
        logger.finer("Printing line started, text size is: " + iTextSize);
        sLine = "";
        logger.finer("Printing line ended");
    }

    // Печать текста
    public void printText(int iStyle, String sText) {
        logger.finer("Printing text started, style is " + iStyle + ", text is: " + sText);
        sLine += sText;
        logger.finer("Printing text ended");
    }

    public void endLine() {
        logger.finer("End of line started");

        try {
            int iFiscalPassword = getFiscalPassword();
            int iStation = SMFP_STATION_RECJRN;

            if (sLine.length() > MAX_TEXT_LENGHT) {
                sLine = sLine.substring(0, MAX_TEXT_LENGHT);
            }
            logger.finer("Send line to device: " + sLine);

            PrinterCommand command = new PrintString(iFiscalPassword, iStation, sLine);

            Infinity:
            for (;;) {
                executeCommand(command);

                switch (command.getResultCode()) {

                    // Command complete successfully
                    case 0:
                        break Infinity;

                    // Printing previous command, waiting
                    case 0x50:
                        waitCommandComplete();
                        break;

                    // Other errors, generate exception
                    default:
                        String message = PrinterError.getFullText(command.getResultCode());
                        throw new Exception(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing message", e);
            closePort();
        }

        logger.finer("End of line ended");
    }

    // Окончание печати чека
    public void endReceipt() {
        logger.finer("End of receipt started");
        logger.finer("End of receipt ended");
    }

    // Открытие денежного ящика
    public void openDrawer() {
        logger.finer("Open drawer started");
        logger.finer("Open drawer ended");
    }

    // Отрезание бумаги
    public void cutPaper(boolean complete) {
        logger.finer("Cutting paper started");

        try {
            int iFiscalPassword = getFiscalPassword();

            int iCutType = 1;
            if (complete) {
                iCutType = 0;
            }

            PrinterCommand command = new CutPaper(iFiscalPassword, iCutType);

            Infinity:
            for (;;) {
                executeCommand(command);

                switch (command.getResultCode()) {

                    // Command complete successfully
                    case 0:
                        break Infinity;

                    // Printing previous command, waiting
                    case 0x50:
                        waitCommandComplete();
                        break;

                    // Other errors, generate exception
                    default:
                        String message = PrinterError.getFullText(command.getResultCode());
                        throw new Exception(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while cutting paper", e);
            closePort();
        }
        logger.finer("Cutting paper ended");
    }
}
