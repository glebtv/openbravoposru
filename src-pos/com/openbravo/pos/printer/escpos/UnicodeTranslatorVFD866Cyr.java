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

package com.openbravo.pos.printer.escpos;

import java.io.UnsupportedEncodingException;

public class UnicodeTranslatorVFD866Cyr extends UnicodeTranslator {

    public UnicodeTranslatorVFD866Cyr() {
    }

    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_7;
    }

    public final byte[] convertString(String sConvert) {
        byte bAux[] = new byte[sConvert.length()];
        for (int i = 0; i < sConvert.length(); i++) {
                    char c = sConvert.charAt(i);
            if ((c >= 0x0020) && (c < 0x0080)) {
                bAux[i] = (byte) c;
            } else if (c >= '\u0410' && c <= '\u044F' || c =='\u0401' || c == '\u0451') {
                try {
                    bAux[i] = Character.toString(c).getBytes("CP866")[0]; // CP866 Cyrillic
                } catch (UnsupportedEncodingException ex) {
                }
            } else if (c == '\u00A0') {
                bAux[i] = (byte) 0x7F; // &nbsp
            } else if (c == '\u2116') {
                bAux[i] = (byte) 0xFC; // №
            } else {
                bAux[i] = (byte) 0x3F; // ? Not valid character.
            }
        }
        return bAux;
    }
}