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

import com.openbravo.pos.printer.TicketFiscalPrinterException;
import com.openbravo.pos.printer.aurafr.command.PrinterCommand;
import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterError;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeviceAuraFR {
    
    // Logger
    protected static Logger logger = Logger.getLogger("com.openbravo.pos.printer.aurafr");
    
    public static final int MAX_STRING_LENGTH = 40; 
    
    public static final int SPACE_STRINGE_BEFORE_CUT = 3; 
    
    public static final String ADMIN_PASSWORD = "30";
    public static final String CASHIER_PASSWORD = "0";
    
    public static final byte MODE_SELECT = 0x00;
    public static final byte MODE_REGISTRATION = 0x01;
    public static final byte MODE_X_REPORT = 0x02;
    public static final byte MODE_Z_REPORT = 0x03;
    public static final byte MODE_PROGRAM = 0x04;
    public static final byte MODE_FM = 0x05;
    public static final byte MODE_EJ = 0x06;
    
    public static final byte RECEIPT_FLAG_POST = 0x00;
//    public static final byte RECEIPT_FLAG_CHECK = 0x01;
//    public static final byte RECEIPT_FLAG_BUFFER = 0x04;

    public static final byte LINE_FLAG_POST_CTRL = 0x00;
    public static final byte LINE_FLAG_CHECK_CTRL = 0x01;
    public static final byte LINE_FLAG_POST = 0x02;
    public static final byte LINE_FLAG_CHECK = 0x03;
    
    public static final byte RECEIPT_TYPE_SALE = 0x01;
    public static final byte RECEIPT_TYPE_REFUND = 0x02;
//    public static final byte RECEIPT_TYPE_SALE_CANCEL = 0x03;
//    public static final byte RECEIPT_TYPE_INVOICE = 0x04;
//    public static final byte RECEIPT_TYPE_RETURN = 0x05;
//    public static final byte RECEIPT_TYPE_INVOICE_CANCEL = 0x06;
    
    public static final byte PAYMENT_TYPE_CASH = 0x01;
    public static final byte PAYMENT_TYPE_DEBIT = 0x02;
    public static final byte PAYMENT_TYPE_MAGCARD = 0x04;

    public static final byte REPORT_TYPE_X = 0x01;
    public static final byte REPORT_TYPE_BY_SECTION = 0x02;
    public static final byte REPORT_TYPE_BY_CASHIER = 0x03;
    public static final byte REPORT_TYPE_BY_PRODUCT = 0x04;
    public static final byte REPORT_TYPE_BY_HOUR = 0x05;
    public static final byte REPORT_TYPE_BY_UNIT = 0x07;
    
    public void GenerateCommand(int iMessageCounter, AuraFRReaderWritter mFiscalPrinter, PrinterCommand mCommand) throws TicketFiscalPrinterException {
        logger.log(Level.INFO, "Send message {0}: {1} {2}", new Object[]{iMessageCounter, mCommand.getCode(), mCommand.getText()});
        mFiscalPrinter.sendMessage(CASHIER_PASSWORD, mCommand);
    }

    public void CheckErrorAnswer(int iMessageCounter, AuraFRReaderWritter mFiscalPrinter, PrinterError mError) throws TicketFiscalPrinterException {
        if (mError.getCodeError() != 0) {
            logger.log(Level.SEVERE, "Get error {0}: {1} {2}", new Object[]{iMessageCounter, mError.getCodeError(), mError.getTextError()});
            mFiscalPrinter.disconnectDevice();
            throw new TicketFiscalPrinterException(mError.getFullTextError());
        } else {
            logger.log(Level.INFO, "Get answer {0}: {1} {2}", new Object[]{iMessageCounter, mError.getCodeError(), mError.getTextError()});
        }
    }

//   public byte[] StampTitleReportMessage() {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(TITLE);
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] OpenDrawerMessage() {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(OPEN_DRAWER);
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
}
