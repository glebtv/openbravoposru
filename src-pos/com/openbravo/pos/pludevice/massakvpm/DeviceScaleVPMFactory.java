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

package com.openbravo.pos.pludevice.massakvpm;

import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.util.SerialPortParameters;
import com.openbravo.pos.util.StringParser;
import gnu.io.SerialPort;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

public class DeviceScaleVPMFactory {
    private DeviceScaleVPMFactory() {
    }

    public static DeviceScaleVPM createInstance(AppProperties props) {
        
        Integer iPLUDeviceSerialPortSpeed = 57600;
        Integer iPLUDeviceSerialPortDataBits = SerialPort.DATABITS_8;
        Integer iPLUDeviceSerialPortStopBits = SerialPort.STOPBITS_1;
        Integer iPLUDeviceSerialPortParity = SerialPort.PARITY_NONE;
        
        StringParser sd = null;
        sd = new StringParser(props.getProperty("machine.pludevice"));            
        
        if (sd == null) {
            sd = new StringParser(props.getProperty("machine.scanner"));
        }
        
        String sPLUDeviceType = sd.nextToken(':');
        String sPLUDeviceType1 = sd.nextToken(',');
        iPLUDeviceSerialPortSpeed = SerialPortParameters.getSpeed(sd.nextToken(','));
        iPLUDeviceSerialPortDataBits =  SerialPortParameters.getDataBits(sd.nextToken(','));
        iPLUDeviceSerialPortStopBits = SerialPortParameters.getStopBits(sd.nextToken(','));
        iPLUDeviceSerialPortParity = SerialPortParameters.getParity(sd.nextToken(','));          

        if ("massakvpm".equals(sPLUDeviceType)) {
            return new DeviceScaleVPMComm(sPLUDeviceType, sPLUDeviceType1, iPLUDeviceSerialPortSpeed, iPLUDeviceSerialPortDataBits, iPLUDeviceSerialPortStopBits, iPLUDeviceSerialPortParity);
        } else {
            return null;
        }
    }
}
