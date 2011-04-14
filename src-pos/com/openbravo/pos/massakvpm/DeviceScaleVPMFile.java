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

package com.openbravo.pos.massakvpm;

/**
 * @author Andrey Svininykh svininykh@gmail.com
 */

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeviceScaleVPMFile {
    private String m_sFileScale;
    private OutputStream m_out;

    private boolean initialized = false;
    private ExecutorService exec;


    DeviceScaleVPMFile(String sFileScale) {
        m_sFileScale = sFileScale;
        m_out = null;
    }
    
    protected void internalWrite(byte[] data) {
        try {
            if (m_out == null) {
                m_out = new FileOutputStream(m_sFileScale);  // No poner append = true.
            }
            m_out.write(data);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    protected void internalFlush() {
        try {
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    protected void internalClose() {
        try {
            if (m_out != null) {
                m_out.flush();
                m_out.close();
                m_out = null;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

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
