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

import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.JPrincipalApp;

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.*;


/**
 * @author: Gennady Kovalev <gik@bigur.ru>
 */

public class DeviceShtrihFR implements PrinterConst {

    // Logger
    protected static Logger logger = Logger.getLogger("com.openbravo.pos.printer.shtrihfr");

    // Error strings
    protected static String ERROR_IO = "Ошибка обмена данными с фискальным регистратором.";
    protected static String ERROR_NO_SUCH_PORT = "Не могу соединиться с фискальным регистратором.";
    protected static String ERROR_PREVIOUS_COMMAND_TIMEOUT = "Истекло время ожидания исполнения предыдущей команды.";

    // Constants
    protected static final int MAX_TEXT_LENGHT = 40;
    private static final int SLEEP_THRESHOLD = 500;
    private static final int SLEEP_MAX_STEPS = 60;
    private static final String sCharsetName = "CP1251";
    private static final int iBaudRate = 115200;
    private static final int iFiscalTimeout = 100;
    private static final int iAdminPassword = 30;

    // Fiscal printer's attributes
    //protected String m_sName;
    protected String m_sSerialDevice;
    private PrinterPort m_Port;
    private PrinterDevice m_PrinterDevice;

    // Constructor
    public DeviceShtrihFR(String sDevicePrinterPort) {
        logger.finer("Create Shtrih-FR printer instance with port: " + sDevicePrinterPort);
        //m_sName = AppLocal.getIntString("Printer.Serial");
        m_sSerialDevice = sDevicePrinterPort;
        m_Port = new PrinterPort();
        m_PrinterDevice = new PrinterDevice(m_Port);
    }

    //
    // Helper methods
    //

    // Get admin's fiscal password of current user
    protected int getAdminFiscalPassword() {
        return iAdminPassword;
    }

    // Get fiscal password of current user
    protected int getFiscalPassword() {
        return iAdminPassword;
    }

    // Open I/O port
    protected void openPort() throws Exception {
        if (!m_Port.isOpened()) {
            logger.finer("Try to open serial port...");
            m_Port.open(m_sSerialDevice, iBaudRate, iFiscalTimeout, this);
        }
    }

    // Execute command
    protected void executeCommand(PrinterCommand m_PrinterCommand) throws Exception {
        openPort();
        m_PrinterDevice.execute(m_PrinterCommand);
        logger.finer("Command executed, result code is " + m_PrinterCommand.getResultCode());
    }

    // Close I/O port
    protected void closePort() {
        if (!m_Port.isOpened()) {
            try {
                m_Port.close();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Can't close port", e);
            }
        }
    }

    // Get short status
    protected ShortPrinterStatus getPrinterStatusShort() throws Exception {
        logger.finer("Get printer status started");
        ReadShortStatus command = new ReadShortStatus(getFiscalPassword());
        executeCommand(command);
        ShortPrinterStatus status = command.getStatus();
        logger.finer("Get printer status ended");
        return status;
    }

    // Wait until the end of the command to complete
    protected void waitCommandComplete() throws Exception {
        logger.finer("Wait for complete command started");
        for (int i=0; i< SLEEP_MAX_STEPS; i++) {
            ShortPrinterStatus status = getPrinterStatusShort();
            logger.finer("Short status flags: " + status.flags);
            logger.finer("Short status advanced mode: " + status.advancedMode);
            logger.finer("Short status fmResultCode: " + status.fmResultCode);
            logger.finer("Short status eklzResultCode: " + status.eklzResultCode);
            logger.finer("Short status quantityOfOperations: " + status.quantityOfOperations);
            logger.finer("Short status batteryVoltage: " + status.batteryVoltage);
            logger.finer("Short status powerSourceVoltage: " + status.powerSourceVoltage);
            logger.finer("Short status operatorNumber: " + status.operatorNumber);
            switch (status.advancedMode) {
                case ECR_ADVANCEDMODE_IDLE:
                    logger.finer("Wait for complete command ended");
                    return;
                case ECR_ADVANCEDMODE_AFTER:
                    continuePrinting();
                    break;
                case ECR_ADVANCEDMODE_REPORT:
                case ECR_ADVANCEDMODE_PRINT:
                default:
                    Thread.sleep(SLEEP_THRESHOLD);
            }
        }
        throw new Exception(ERROR_PREVIOUS_COMMAND_TIMEOUT);
    };

    // Continue printing command
    protected void continuePrinting() throws Exception {
        logger.finer("Continue printing started");

        int iFiscalPassword = getFiscalPassword();

        PrinterCommand command = new ContinuePrint(iFiscalPassword);

        executeCommand(command);

        if (command.getResultCode() != 0) {
            String message = PrinterError.getFullText(command.getResultCode());
            closePort();
            throw new Exception(message);
        }
        logger.finer("Continue printing ended");
    }

    // Set cashier name
    protected void setCashierName(String sName) throws Exception {
        logger.finer("Set cashier name started");
        int iOperatorPassword = getFiscalPassword();

        // Try to find operator's number
        ShortPrinterStatus status = getPrinterStatusShort();
        int iOperatorNumber = status.operatorNumber;

        // Write table
        writeTable(2, iOperatorNumber, 2, sName);

        logger.finer("Set cashier name ended");
    }

    protected void writeTable(int iTableNumber, int iRowNumber, int iFieldNumber, String sFieldValue) throws Exception {
        logger.finer("Write table started: " +
                "table " + iTableNumber + ", " +
                "row " + iRowNumber + ", " +
                "field " + iFieldNumber + ", " +
                "value '" + sFieldValue + "'");

        int iAdminPassword = getAdminFiscalPassword();
        ReadFieldInfo fieldInfo = new ReadFieldInfo(iAdminPassword, iTableNumber, iFieldNumber);
        executeCommand(fieldInfo);

        byte[] bFieldValue = fieldInfo.fieldToBytes(sFieldValue, sCharsetName);

        PrinterCommand command = new WriteTable(iAdminPassword, iTableNumber, iRowNumber, iFieldNumber, bFieldValue);
        executeCommand(command);

        logger.finer("Write table ended");
    }

}
