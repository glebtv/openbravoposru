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
import java.io.UnsupportedEncodingException;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;

public class DeviceLabelPrinterEZPL implements DeviceLabelPrinter {

    private LabelPrinterWritter m_CommOutputPrinter;
//    private LabelCodes m_codes;
//    private LabelUnicodeTranslator m_trans;
//    private boolean m_bInline;
    private String m_sName;

    // Creates new TicketPrinter
    public DeviceLabelPrinterEZPL(LabelPrinterWritter CommOutputPrinter) throws TicketPrinterException {

        m_sName = AppLocal.getIntString("PrinterLabel.Serial");
        m_CommOutputPrinter = CommOutputPrinter;
//        m_codes = codes;
//        m_trans = trans;

        // Inicializamos la impresora
//        m_CommOutputPrinter.init(new byte[] {0x00});
//
//        m_CommOutputPrinter.write(new byte[] {0x00});
//        m_CommOutputPrinter.init(m_codes.getInitSequence());
//        m_CommOutputPrinter.write(m_trans.getCodeTable());

        m_CommOutputPrinter.flush();
    }

    public String getLabelPrinterName() {
        return m_sName;
    }

    public String getLabelPrinterDescription() {
        return null;
    }

    public void reset() {
    }

    public void beginLabel(String sCodePage, String sWidth, String sHeight, String sGap) {
        
        String sUnicodeTranslator = "0";
        
        if (sCodePage.equals("cp866")) {
            sUnicodeTranslator = "10";
        }
        
        try {
//            m_CommOutputPrinter.write("^Q150,0,0");
            String sBuffer = "^Q" + sHeight + "," + sGap + ",0";
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
            sBuffer = "^W" + sWidth;
//            m_CommOutputPrinter.write("^W50");
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
            if (!sUnicodeTranslator.equals("0")) {
//            m_CommOutputPrinter.write("^XSET,CODEPAGE,10");
                sBuffer = "^XSET,CODEPAGE," + sCodePage;
                m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
                m_CommOutputPrinter.write(EZPL.CR_LF);
            }
            m_CommOutputPrinter.write(EZPL.START_LABEL.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    public void printTextBox(String sCharset, String sFontPoint, String sLabelX, String sLabelY, String sRotation, String sText) {
        
        String sFont = "A";
        
        if (sFontPoint.equals("10")){
            sFont = "C";
        }
        
        String sBuffer = "A" + sFont +"," + sLabelX + "," + sLabelY + ",0,0,0," + sRotation + ",";

        try {
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(sText.getBytes(sCharset));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    public void drawLineBox(String sLabelX, String sLabelY, String sWidth, String sHeight) {
        try {
            String sBuffer = "Le," + sLabelX + "," + sLabelY + "," + sWidth + "," + sHeight;
//            m_CommOutputPrinter.write("Le,212,45,311,53".getBytes("ASCII"));
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    public void drawRectangleBox(String sLabelX, String sLabelY, String sWidth, String sHeight) {

        String sBuffer = "R" + sLabelX + "," + sLabelY + "," + sWidth + "," + sHeight + ",8,8";
        
        try {
//            m_CommOutputPrinter.write("R20,20,120,120,8,8".getBytes("ASCII"));
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    public void printBarcodeBox(String sTypeBarcode, String sLabelX, String sLabelY, String sHeight, String sRotation, String sCode) {
        String sBarcode = "E";
        if (sTypeBarcode.equals("CODE128")){
            sBarcode = "Q";
        }
        String sBuffer = "B" + sBarcode + "," + sLabelX + "," + sLabelY + ",3,3," + sHeight + "," + sRotation + ",1,";

        try {
//            m_CommOutputPrinter.write("BE,20,100,3,3,100,0,1,".getBytes("ASCII"));
            m_CommOutputPrinter.write(sBuffer.getBytes("ASCII"));
            m_CommOutputPrinter.write(sCode.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    public void endLabel() {
        try {
            m_CommOutputPrinter.write(EZPL.END_LABEL.getBytes("ASCII"));
            m_CommOutputPrinter.write(EZPL.CR_LF);
        } catch (UnsupportedEncodingException ex) {
        }
    }
}
