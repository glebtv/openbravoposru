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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class LabelPrinterWritter {
    
    private boolean initialized = false;

    private ExecutorService exec;
    
    public LabelPrinterWritter() {
        exec = Executors.newSingleThreadExecutor();
    }
    
    protected abstract void internalWrite(byte[] data);
    protected abstract void internalFlush();
    protected abstract void internalClose();
    
    public void init(final byte[] data) {
        if (!initialized) {
            write(data);
            initialized = true;
        }
    }
       
    public void write(String sValue) {
        write(sValue.getBytes());
    }

    public void write(final byte[] data) {
        exec.execute(new Runnable() {
            public void run() {
                internalWrite(data);
            }
        });
    }
    
    public void flush() {
        exec.execute(new Runnable() {
            public void run() {
                internalFlush();
            }
        });
    }
    
    public void close() {
        exec.execute(new Runnable() {
            public void run() {
                internalClose();
            }
        });
        exec.shutdown();
    }
}
