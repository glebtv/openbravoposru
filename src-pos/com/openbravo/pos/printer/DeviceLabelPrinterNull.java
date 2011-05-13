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

package com.openbravo.pos.printer;
/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
import com.openbravo.pos.forms.AppLocal;

public class DeviceLabelPrinterNull implements DeviceLabelPrinter {
    
    private String m_sName;
    private String m_sDescription;
    
    /** Creates a new instance of DeviceDisplayNull */
    public DeviceLabelPrinterNull() {
        this(null);
    }
    
    /** Creates a new instance of DeviceDisplayNull */
    public DeviceLabelPrinterNull(String desc) {
        m_sName = AppLocal.getIntString("LabelPrinter.Null");
        m_sDescription = desc;
    }

    public String getLabelPrinterName() {
        return m_sName;
    }    
    public String getLabelPrinterDescription() {
        return m_sDescription;
    }   
    
    public void reset() {
    }

    public void beginLabel(String sCodePage, String sWidth, String sHeight, String sGap) {
    }    
    
    public void printTextBox(String sCharset, String sFontPoint, String sLabelX, String sLabelY, String sRotation, String sText) {
    }
    
    public void printBarcodeBox(String sTypeBarcode, String sLabelX, String sLabelY, String sHeight, String sRotation, String sCode) {
    }
    
    public void drawLineBox(String sLabelX, String sLabelY, String sWidth, String sHeight){        
    }

    public void drawRectangleBox(String sLabelX, String sLabelY, String sWidth, String sHeight){        
    }    
        
    public void endLabel() {
    }       
}
