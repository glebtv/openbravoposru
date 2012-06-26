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
import com.openbravo.pos.printer.DeviceFiscalPrinter;
import com.openbravo.pos.printer.TicketFiscalPrinterException;
import com.openbravo.pos.printer.aurafr.command.*;
import com.openbravo.pos.printer.aurafr.fiscalprinter.PrinterMode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 * @author: Andrey Svininykh <svininykh@gmail.com>
 */

public class DeviceFiscalPrinterAuraFR extends DeviceAuraFR implements DeviceFiscalPrinter {

    private AuraFRReaderWritter m_CommOutputFiscal;
    
    private int iFPCounter = 0;
    
    private String m_sName;
    private String m_sTicketType;
    
    private ReadMode cmd_ReadMode;
    private CancelCurrentMode cmd_CancelCurrentMode;
    private SelectMode cmd_SelectMode;
    private PrintString cmd_PrintString;
    private VoidFiscalReceipt cmd_VoidFiscalReceipt;
    private BeginFiscalReceipt cmd_BeginFiscalReceipt;
    private ReadFullStatus cmd_ReadFullStatus;
    private PrintSale cmd_PrintSale;
    private PrintSaleRefund cmd_PrintSaleRefund;
    private PrintCashIn cmd_PrintCashIn;
    private PrintCashOut cmd_PrintCashOut;
    private EndFiscalReceipt cmd_EndFiscalReceipt;
    private CutPaper cmd_CutPaper;
    private PrintXReport cmd_PrintXReport;
    private PrintZReport cmd_PrintZReport;
    private ReadCashRegister cmd_ReadCashRegister;
    private BeginFiscalDay cmd_BeginFiscalDay;
    
    int iReceiptLineCounter = 0;

    // Creates new TicketPrinter
    public DeviceFiscalPrinterAuraFR(String sDevicePrinterPort, Integer iPortSpeed, Integer iPortBits, Integer iPortStopBits, Integer iPortParity) {
        try {
            m_sName = AppLocal.getIntString("Printer.Serial");
            m_CommOutputFiscal = new DeviceAuraFRComm(sDevicePrinterPort, iPortSpeed, iPortBits, iPortStopBits, iPortParity);
    //        m_CommOutputFiscal.getTypePrinter();
            m_CommOutputFiscal.connectDevice();

            cmd_ReadFullStatus = new ReadFullStatus();
            GenerateCommand(iFPCounter++, m_CommOutputFiscal, cmd_ReadFullStatus);

            SetPrinterMode(MODE_SELECT);

            GenerateCommand(iFPCounter++, m_CommOutputFiscal, new Beep());

            m_CommOutputFiscal.disconnectDevice();
        } catch (TicketFiscalPrinterException ex) {
            Logger.getLogger(DeviceFiscalPrinterAuraFR.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFiscalName() {
        return m_sName;
    }

    public JComponent getFiscalComponent() {
        return null;
    }

    public void reset() {
    }

    public void printCashIn(double dSumm) throws TicketFiscalPrinterException {
        m_CommOutputFiscal.connectDevice();
        SetPrinterMode(MODE_SELECT);
        SetPrinterMode(MODE_REGISTRATION);
        
        CheckCurrentOpenDay();

        cmd_ReadCashRegister = new ReadCashRegister((byte) 0x04, (byte) 0x00, (byte) 0x00);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_ReadCashRegister);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_ReadCashRegister.getErrorAnswer());  
        iFPCounter++;
        
        cmd_PrintCashIn = new PrintCashIn(RECEIPT_FLAG_POST, dSumm);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintCashIn);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintCashIn.getErrorAnswer());  
        iFPCounter++;
        
        GenerateCommand(iFPCounter++, m_CommOutputFiscal, new Beep());
        m_CommOutputFiscal.disconnectDevice();
    }

    public void printCashOut(double dSumm) throws TicketFiscalPrinterException {
        m_CommOutputFiscal.connectDevice();
        SetPrinterMode(MODE_SELECT);
        SetPrinterMode(MODE_REGISTRATION);

        CheckCurrentOpenDay();
      
        cmd_ReadCashRegister = new ReadCashRegister((byte) 0x05, (byte) 0x00, (byte) 0x00);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_ReadCashRegister);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_ReadCashRegister.getErrorAnswer());  
        iFPCounter++;
        
        cmd_PrintCashOut = new PrintCashOut(RECEIPT_FLAG_POST, dSumm);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintCashOut);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintCashOut.getErrorAnswer());
        iFPCounter++;

        GenerateCommand(iFPCounter++, m_CommOutputFiscal, new Beep());
        m_CommOutputFiscal.disconnectDevice();
    }

    //Начало печати чека
    public void beginReceipt(String sType, String sCashier) throws TicketFiscalPrinterException {
        m_sTicketType = sType;
        m_CommOutputFiscal.connectDevice();
        SetPrinterMode(MODE_SELECT);
        SetPrinterMode(MODE_REGISTRATION);

        CheckCurrentOpenDay();

        if (m_sTicketType.equals("sale")) {
            cmd_BeginFiscalReceipt = new BeginFiscalReceipt(RECEIPT_FLAG_POST, RECEIPT_TYPE_SALE);
        } else if (m_sTicketType.equals("refund")) {
            cmd_BeginFiscalReceipt = new BeginFiscalReceipt(RECEIPT_FLAG_POST, RECEIPT_TYPE_REFUND);
        }
//        } else if (m_sTicketType.equals("invoice")){
//            
//        } else if (m_sTicketType.equals("return")){
//            
//        }        

        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_BeginFiscalReceipt);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_BeginFiscalReceipt.getErrorAnswer());
        iFPCounter++;

        GenerateCommand(iFPCounter++, m_CommOutputFiscal, new Beep());
    }

    // Печать строки продажи или возврата по товару
    public void printLine(String sproduct, double dprice, double dunits, int taxinfo) throws TicketFiscalPrinterException {

        cmd_PrintString = new PrintString(sproduct);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintString);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
        iFPCounter++;

        if (dprice >= 0 && dunits >= 0 && m_sTicketType.equals("sale")) {
            cmd_PrintSale = new PrintSale(LINE_FLAG_POST,  dprice / dunits, dunits, Integer.toString(taxinfo));
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintSale);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintSale.getErrorAnswer());
            iReceiptLineCounter++;
        } else if (dprice < 0 && dunits < 0 && m_sTicketType.equals("refund")) {
            cmd_PrintSaleRefund = new PrintSaleRefund(LINE_FLAG_POST, dprice / dunits, -1 * dunits);
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintSaleRefund);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintSaleRefund.getErrorAnswer());
            iReceiptLineCounter++;
        } else {
            iReceiptLineCounter = 0;
            String sLineError = "Error in receipt line parameters.";
            CancelCurrentOpenReceipt(sLineError);
            throw new TicketFiscalPrinterException(sLineError);
        }
    }

    //Печать текста
    public void printMessage(String sText) throws TicketFiscalPrinterException {
        cmd_PrintString = new PrintString(sText);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintString);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
        iFPCounter++;
    }

    public void endLine() {
    }

    //Окончание печати чека
    public void endReceipt() throws TicketFiscalPrinterException {
//        SetPrinterMode(0);
        if (iReceiptLineCounter == 0) {
            cmd_VoidFiscalReceipt = new VoidFiscalReceipt();
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_VoidFiscalReceipt);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_VoidFiscalReceipt.getErrorAnswer());
            iFPCounter++;
        } else {
            iReceiptLineCounter = 0;
        }
        GenerateCommand(iFPCounter++, m_CommOutputFiscal, new Beep());
        m_CommOutputFiscal.disconnectDevice();
    }

    //Печать итоговой оплаты по чеку
    public void printTotal(String sPayment, double dpaid, String sPaymentType) throws TicketFiscalPrinterException {
        cmd_PrintString = new PrintString(sPayment);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintString);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
        iFPCounter++;

        int iType = 0;
        
        if (sPaymentType.equals("cash") || sPaymentType.equals("cashrefund")) {
            iType = PAYMENT_TYPE_CASH;
        } else if (sPaymentType.equals("debt")) {
            iType = PAYMENT_TYPE_DEBIT;
        } else if (sPaymentType.equals("magcard") || sPaymentType.equals("magcardrefund")) {
            iType = PAYMENT_TYPE_MAGCARD;
        } else {
            String sPaymentError = "Not support " + sPaymentType + " payment type.";
            CancelCurrentOpenReceipt(sPaymentError);
            throw new TicketFiscalPrinterException(sPaymentError);
        }
                    
        if (dpaid >= 0 && m_sTicketType.equals("sale") && iType != 0) {
            cmd_EndFiscalReceipt = new EndFiscalReceipt(RECEIPT_FLAG_POST, iType, dpaid);
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_EndFiscalReceipt);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
            iFPCounter++;
        } else if (dpaid < 0 && m_sTicketType.equals("refund") && iType != 0) {
            cmd_EndFiscalReceipt = new EndFiscalReceipt(RECEIPT_FLAG_POST, iType, 0.0);
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_EndFiscalReceipt);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
            iFPCounter++;
        } else {
            String sCloseTicketError = "Error close ticket.";
            CancelCurrentOpenReceipt(sCloseTicketError);
            throw new TicketFiscalPrinterException(sCloseTicketError);
        }
    }

    //Отрезание бумаги
    public void cutPaper(boolean complete) throws TicketFiscalPrinterException {
        if (complete) {
            cmd_CutPaper = new CutPaper(1);
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_CutPaper);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_CutPaper.getErrorAnswer());
            iFPCounter++;
        }
    }

    //Печать X-отчёта
    public void printXReport() throws TicketFiscalPrinterException {
        m_CommOutputFiscal.connectDevice();
        SetPrinterMode(MODE_X_REPORT);
        
        cmd_PrintXReport = new PrintXReport(REPORT_TYPE_X);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintXReport);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintXReport.getErrorAnswer());
        iFPCounter++;
        
        m_CommOutputFiscal.disconnectDevice();
    }

    //Печать Z-отчёта
    public void printZReport() throws TicketFiscalPrinterException {
        m_CommOutputFiscal.connectDevice();
        SetPrinterMode(MODE_Z_REPORT);
        
        cmd_PrintZReport = new PrintZReport();
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintZReport);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintZReport.getErrorAnswer());
        iFPCounter++;
       
        m_CommOutputFiscal.disconnectDevice();
    }

    private PrinterMode GetModeAnswer() throws TicketFiscalPrinterException {
        cmd_ReadMode = new ReadMode();
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_ReadMode);
        PrinterMode mModeCurrent = cmd_ReadMode.getMode();
        logger.log(Level.WARNING, "Get mode {0}: {1} {2}", new Object[]{iFPCounter, mModeCurrent.getModeStatus(), mModeCurrent.getModeText()});
        iFPCounter++;
        return mModeCurrent;
    }

    private void SetPrinterMode(int iMode) throws TicketFiscalPrinterException {
        int iCurrentMode = GetModeAnswer().getModeStatus();
        byte bCurrentError = 0x00;
        if (iCurrentMode != iMode) {

            if (iCurrentMode != MODE_SELECT) {
                cmd_CancelCurrentMode = new CancelCurrentMode();
                GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_CancelCurrentMode);
                bCurrentError = cmd_CancelCurrentMode.getErrorAnswer().getCodeError();
                if (bCurrentError == (byte) 0x89 || bCurrentError == (byte) 0x87 || bCurrentError == (byte) 0x82) {
                    CancelCurrentOpenReceipt(cmd_CancelCurrentMode.getErrorAnswer().getFullTextError());
                } else {
                    CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_CancelCurrentMode.getErrorAnswer());
                    iFPCounter++;
                }
            }

            if (iMode != MODE_SELECT) {
                cmd_SelectMode = new SelectMode(iMode, ADMIN_PASSWORD);
                GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_SelectMode);
                CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_SelectMode.getErrorAnswer());
                iFPCounter++;
            }

            PrinterMode mMode = GetModeAnswer();
            if (mMode.getModeStatus() != iMode) {
                m_CommOutputFiscal.disconnectDevice();
                throw new TicketFiscalPrinterException("Not set FP mode " + mMode.getFullModeText());
            }
        }
    }
    
    private void CancelCurrentOpenReceipt(String sPrintErrorText) throws TicketFiscalPrinterException {
        cmd_PrintString = new PrintString(sPrintErrorText);
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_PrintString);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_PrintString.getErrorAnswer());
        iFPCounter++;

        cmd_VoidFiscalReceipt = new VoidFiscalReceipt();
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_VoidFiscalReceipt);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_VoidFiscalReceipt.getErrorAnswer());
        iFPCounter++;
        
        cmd_CancelCurrentMode = new CancelCurrentMode();
        GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_CancelCurrentMode);
        CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_CancelCurrentMode.getErrorAnswer());
        iFPCounter++;
        
//        throw new TicketFiscalPrinterException(sPrintErrorText);
    }
    
    private void CheckCurrentOpenDay() throws TicketFiscalPrinterException {
        cmd_ReadCashRegister = new ReadCashRegister((byte) 0x12, (byte) 0x00, (byte) 0x00);
        GenerateCommand(iFPCounter++, m_CommOutputFiscal, cmd_ReadCashRegister);
        logger.log(Level.WARNING, "Fiscal day open: {0} Last data close: {1}", new Object[]{cmd_ReadCashRegister.isOpenFiscalDay(), cmd_ReadCashRegister.getDateCloseFiscalDay().getTime().toString()});

        if (!cmd_ReadCashRegister.isOpenFiscalDay()) {
            cmd_BeginFiscalDay = new BeginFiscalDay(RECEIPT_FLAG_POST, "");
            GenerateCommand(iFPCounter, m_CommOutputFiscal, cmd_BeginFiscalDay);
            CheckErrorAnswer(iFPCounter, m_CommOutputFiscal, cmd_BeginFiscalDay.getErrorAnswer());
            iFPCounter++;
        }
    }
}
