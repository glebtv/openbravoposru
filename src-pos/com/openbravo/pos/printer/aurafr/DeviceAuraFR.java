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

//    private ByteArrayOutputStream lineout;
//    public static final byte[] ADMIN_PASSWORD = {0x00, 0x00, 0x00, 0x30};

//    private static final byte INIT = 0x3f; //Запрос состояния ККМ
//    private static final byte PRN = 0x4C; //Печать строки текста
//    private static final byte BEEP = 0x47; //Гудок
//    private static final byte TITLE = 0x6C; //Печать клише чека
//    private static final byte CUT = 0x75; //Отрезать чек
//    private static final byte OPEN_DRAWER = (byte) 0x80; //Открыть денежный ящик
//
//    private static final byte OPEN_TICKET = (byte) 0x92; //Открыть чек
//    private static final byte CLOSETICKET = (byte) 0x4A;
//    private static final byte REGISTRATION = (byte) 0x52;
//    private static final byte SELECTMODE = (byte) 0x56;
//    private static final byte CANCELMODE = (byte) 0x48;
//    private static final byte REFUND = (byte) 0x57;    
//
//    private static final byte XREPORT = (byte) 0x67;
//    private static final byte ZREPORT = (byte) 0x5A;
//    
//    private static final byte READ_REGISTER = (byte) 0x91;
//    private static final byte OPEN = (byte) 0x9A;
//
//    private static final byte ETX = 0x03;
//    private static final byte STX = 0x02;
//
//    private static final byte DLE = 0x10;
//
//    private static final byte NULL = 0x00; // Нулевое значение
//
//    private static final byte[] PASS = {0x00, 0x00};

//    public DeviceAuraFR() {
////        lineout = null;
//    }
    
    public void GenerateCommand(AuraFRReaderWritter mFiscalPrinter, PrinterCommand mCommand) throws TicketFiscalPrinterException {
        logger.log(Level.INFO, "Message: {0} {1}", new Object[]{mCommand.getCode(), mCommand.getText()});
        mFiscalPrinter.sendMessage(CASHIER_PASSWORD, mCommand);
    }

    public void CheckErrorAnswer(AuraFRReaderWritter mFiscalPrinter, PrinterError mError) throws TicketFiscalPrinterException {
        logger.log(Level.INFO, "Answer: {0} {1}", new Object[]{mError.getCodeError(), mError.getTextError()});
        if (mError.getCodeError() != 0) {
            mFiscalPrinter.disconnectDevice();
            throw new TicketFiscalPrinterException(mError.getFullTextError());
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
//
//public byte[] RefundLine(int iFlag, double dProductPrice, double dProductUnit) {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(REFUND);
//        lineout.write(iFlag);
//        byte[] MSG = new byte[5];
//        MSG = convertDouble(Math.abs(dProductPrice),2);
//        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
//        MSG = null;
//        MSG = convertDouble(Math.abs(dProductUnit),3);
//        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
//        byte[] bData = new byte[lineout.size()];
//        bData = lineout.toByteArray();
//        lineout.reset();
//        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
//        for (int i = 4; i < bData.length; i++) {
//            if (bData[i] == ETX || bData[i] == DLE) {
//                lineout.write(DLE);
//            }
//            lineout.write(bData[i]);
//        }
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] CloseTicket(int iFlag,int iPaidType, double dTotalPaid) {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(CLOSETICKET);
//        lineout.write(iFlag);
//        lineout.write(iPaidType);
//        byte[] MSG = new byte[5];
//        MSG = convertDouble(Math.abs(dTotalPaid),2);
//        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
//        byte[] bData = new byte[lineout.size()];
//        bData = lineout.toByteArray();
//        lineout.reset();
//        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
//        for (int i = 4; i < bData.length; i++) {
//            if (bData[i] == ETX || bData[i] == DLE) {
//                lineout.write(DLE);
//            }
//            lineout.write(bData[i]);
//        }
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] XReport(int iTypeReport) {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(XREPORT);
//        lineout.write(iTypeReport);
//        byte[] bData = new byte[lineout.size()];
//        bData = lineout.toByteArray();
//        lineout.reset();
//        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
//        for (int i = 4; i < bData.length; i++) {
//            if (bData[i] == ETX || bData[i] == DLE) {
//                lineout.write(DLE);
//            }
//            lineout.write(bData[i]);
//        }
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] ZReport() {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(ZREPORT);
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] ReadRegister(int iRegister, int iParameter1, int iParameter2) {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(READ_REGISTER);
//        byte[] MSG = new byte[1];
//        String hex = Integer.toHexString(iRegister);
//        MSG = hexStringToByteArray(hex);
//        lineout.write(MSG[0]);
//        lineout.write(iParameter1);
//        lineout.write(iParameter2);
//        byte[] bData = new byte[lineout.size()];
//        bData = lineout.toByteArray();
//        lineout.reset();
//        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
//        for (int i = 4; i < bData.length; i++) {
//            if (bData[i] == ETX || bData[i] == DLE) {
//                lineout.write(DLE);
//            }
//            lineout.write(bData[i]);
//        }
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
//
//    public byte[] OpenMessage(int iFlag, String iText) {
//        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
//        lineout.write(STX);
//        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
//        lineout.write(OPEN);
//        lineout.write(iFlag);
//        lineout.write(ETX);
//        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
//        return lineout.toByteArray();
//    }
}
