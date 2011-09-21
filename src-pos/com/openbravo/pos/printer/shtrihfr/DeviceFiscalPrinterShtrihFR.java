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

// TODO: Did we need check printer connection during application startup?
// TODO: Did we need put printer constants like baud rate in config?
// TODO: Use real fiscal passwords
// TODO: Use I18n for error messages

package com.openbravo.pos.printer.shtrihfr;

import gnu.io.NoSuchPortException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

import com.openbravo.pos.printer.DeviceFiscalPrinter;
import com.openbravo.pos.printer.TicketFiscalPrinterException;

import com.openbravo.pos.printer.shtrihfr.DeviceShtrihFR;

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.*;


/**
 * @author: Gennady Kovalev <gik@bigur.ru>
 */

public class DeviceFiscalPrinterShtrihFR extends DeviceShtrihFR implements DeviceFiscalPrinter {

    // Constants

    // Creates new TicketPrinter
    public DeviceFiscalPrinterShtrihFR(String sDevicePrinterPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) {
        super(sDevicePrinterPort, iPortSpeed, iPortBits, iPortStopBits, iPortParity);
    }

    public String getFiscalName() {
        return m_sSerialDevice;
    }

    public JComponent getFiscalComponent() {
        return null;
    }


    //
    // OpenbravoPOS' printer interface methods
    //

    // Начало печати чека
    public void beginReceipt(String sType, String sCashier) throws TicketFiscalPrinterException {
        logger.finer("Begin printing receipt started");

        try {
            setCashierName(sCashier);

            int iFiscalPassword = getFiscalPassword();
            PrinterCommand command = new BeginFiscalReceipt(iFiscalPassword, 0x00);

            boolean bMustExit = false;

            Infinity:
            for (;;) {
                executeCommand(command);

                switch (command.getResultCode()) {
                    case 0:
                        // Command successfully
                        break Infinity;

                    case 0x50:
                        // Previous command printing
                        waitCommandComplete();
                        break;

                    default:
                        // Ok, we have some troubles, try to fix it
                        if (!bMustExit) {
                            bMustExit = true;
                            ShortPrinterStatus status = getPrinterStatusShort();
                            PrinterMode mode = status.getMode();

                            // Receipt already opened, try to cancel
                            if (mode.isReceiptOpened()) {
                                logger.severe("Can't open new receipt becouse it already opened, try to close old one.");
                                cancelReceipt();
                            }

                            // Other error, go throw exception
                            else {
                                String message = PrinterError.getFullText(command.getResultCode());
                                closePort();
                                throw new TicketFiscalPrinterException(message);
                            }
                        } else {
                            String message = PrinterError.getFullText(command.getResultCode());
                            closePort();
                            throw new TicketFiscalPrinterException(message);
                        }
                        break;
                }

            }

        } catch (java.io.IOException e) {
            logger.severe("I/O error with device using port " + m_sSerialDevice + ": " + e.getMessage());
            closePort();
            throw new TicketFiscalPrinterException(ERROR_IO);

        } catch (gnu.io.NoSuchPortException e) {
            logger.severe("Can't connect device using port " + m_sSerialDevice);
            throw new TicketFiscalPrinterException(ERROR_NO_SUCH_PORT);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while begin receipt", e);
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Begin printing receipt ended");
    }

    // Печать строки продажи по товару
    public void printLine(String sproduct, double dprice, double dunits, int taxinfo) throws TicketFiscalPrinterException {
        logger.finer("Printing line started");

        try {
            int iFiscalPassword = getFiscalPassword();

            long lPrice = (long) (dprice / dunits * 100);
            long lQuantity = (long) (dunits * 1000);
            int iDepartment = 0;
            int iTax1 = 0;
            int iTax2 = 0;
            int iTax3 = 0;
            int iTax4 = 0;
            String sText = sproduct;
            if (sproduct.length() > MAX_TEXT_LENGHT) {
                sText = sproduct.substring(0, MAX_TEXT_LENGHT);
            }
            PriceItem m_PriceItem = new PriceItem(lPrice, lQuantity, iDepartment, iTax1, iTax2, iTax3, iTax4, sText);

            PrinterCommand command = new PrintSale(iFiscalPassword, m_PriceItem);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing line", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Printing line ended");
    }

    // Печать текста
    public void printMessage(String sText) throws TicketFiscalPrinterException {
        logger.finer("Printing message started: " + sText);

        try {
            int iFiscalPassword = getFiscalPassword();
            int iStation = SMFP_STATION_RECJRN;

            PrinterCommand command = new PrintString(iFiscalPassword, iStation, sText);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing message", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Printing message ended");
    }

    // Печать итоговой оплаты по чеку
    public void printTotal(String sPayment, double dpaid, String sPaymentType) throws TicketFiscalPrinterException {
        logger.finer("Printing total started");

        try {
            int iFiscalPassword = getFiscalPassword();

            long lSum1 = (long) (dpaid * 100);
            long lSum2 = 0;
            long lSum3 = 0;
            long lSum4 = 0;
            int iTax1 = 0;
            int iTax2 = 0;
            int iTax3 = 0;
            int iTax4 = 0;
            int iDiscount = 0;
            CloseRecParams m_Params = new CloseRecParams(lSum1, lSum2, lSum3, lSum4, iTax1, iTax2, iTax3, iTax4, iDiscount, sPayment);

            PrinterCommand command = new EndFiscalReceipt(iFiscalPassword, m_Params);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing total", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Printing total ended");
    }

    // Окончание печати чека
    public void endReceipt() throws TicketFiscalPrinterException {
        logger.finer("End of printing receipt started");
        closePort();
        logger.finer("End of printing receipt ended");
    }

    // Отрезание бумаги
    public void cutPaper(boolean complete) throws TicketFiscalPrinterException {
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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while cutting paper", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Cutting paper ended");
    }

    // Печать Z-отчёта
    public void printZReport() throws TicketFiscalPrinterException {
        logger.finer("Printing Z-Report started");

        try {
            int iFiscalPassword = getFiscalPassword();

            PrinterCommand command = new PrintZReport(iFiscalPassword);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing Z-Report", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Printing Z-Report ended");
    }

    // Печать X-отчёта
    public void printXReport() throws TicketFiscalPrinterException {
        logger.finer("Printing X-Report started");

        try {
            int iFiscalPassword = getFiscalPassword();

            PrinterCommand command = new PrintXReport(iFiscalPassword);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while printing X-Report", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Printing X-Report ended");
    }


    //
    // Additional hi-level methods
    //

    // Cancel receipt
    public void cancelReceipt() throws TicketFiscalPrinterException {
        logger.finer("Cancel receipt started");

        try {
            int iFiscalPassword = getFiscalPassword();

            PrinterCommand command = new VoidFiscalReceipt(iFiscalPassword);

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
                        closePort();
                        throw new TicketFiscalPrinterException(message);
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurs while closing receipt", e);
            closePort();
            throw new TicketFiscalPrinterException(e);
        }
        logger.finer("Cancel receipt ended");
    }
}
