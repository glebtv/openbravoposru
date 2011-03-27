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

public class UnicodeTranslator1251Cyr extends UnicodeTranslator {

    /** Creates a new instance of UnicodeTranslatorInt */
    public UnicodeTranslator1251Cyr() {
    }

    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_13;
    }
    
    public final byte[] convertString(String sConvert) {
        byte bAux[] = new byte[sConvert.length()];
        for (int i = 0; i < sConvert.length(); i++) {
            bAux[i] = transChar(sConvert.charAt(i));
        }
        return bAux;
    }

    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {

                default: return 0x3F; // ? Not valid character.

            // ru - Russian for 1251 (Cyrillic) code page
            case '\u0410': return (byte) 0xC0;// A
            case '\u0411': return (byte) 0xC1;// Б
            case '\u0412': return (byte) 0xC2;// В
            case '\u0413': return (byte) 0xC3;// Г
            case '\u0414': return (byte) 0xC4;// Д
            case '\u0415': return (byte) 0xC5;// Е
            case '\u0401': return (byte) 0xA8;// Ё
            case '\u0416': return (byte) 0xC6;// Ж
            case '\u0417': return (byte) 0xC7;// З
            case '\u0418': return (byte) 0xC8;// И
            case '\u0419': return (byte) 0xC9;// Й
            case '\u041A': return (byte) 0xCA;// К
            case '\u041B': return (byte) 0xCB;// Л
            case '\u041C': return (byte) 0xCC;// М
            case '\u041D': return (byte) 0xCD;// Н
            case '\u041E': return (byte) 0xCE;// О
            case '\u041F': return (byte) 0xCF;// П
            case '\u0420': return (byte) 0xD0;// Р
            case '\u0421': return (byte) 0xD1;// С
            case '\u0422': return (byte) 0xD2;// Т
            case '\u0423': return (byte) 0xD3;// У
            case '\u0424': return (byte) 0xD4;// Ф
            case '\u0425': return (byte) 0xD5;// Х
            case '\u0426': return (byte) 0xD6;// Ц
            case '\u0427': return (byte) 0xD7;// Ч
            case '\u0428': return (byte) 0xD8;// Ш
            case '\u0429': return (byte) 0xD9;// Щ
            case '\u042A': return (byte) 0xDA;// Ъ
            case '\u042B': return (byte) 0xDB;// Ы
            case '\u042C': return (byte) 0xDC;// Ь
            case '\u042D': return (byte) 0xDD;// Э
            case '\u042E': return (byte) 0xDE;// Ю
            case '\u042F': return (byte) 0xDF;// Я
            case '\u0430': return (byte) 0xE0;// a
            case '\u0431': return (byte) 0xE1;// б
            case '\u0432': return (byte) 0xE2;// в
            case '\u0433': return (byte) 0xE3;// г
            case '\u0434': return (byte) 0xE4;// д
            case '\u0435': return (byte) 0xE5;// е
            case '\u0451': return (byte) 0xB8;// ё
            case '\u0436': return (byte) 0xE6;// ж
            case '\u0437': return (byte) 0xE7;// з
            case '\u0438': return (byte) 0xE8;// и
            case '\u0439': return (byte) 0xE9;// й
            case '\u043A': return (byte) 0xEA;// к
            case '\u043B': return (byte) 0xEB;// л
            case '\u043C': return (byte) 0xEC;// м
            case '\u043D': return (byte) 0xED;// н
            case '\u043E': return (byte) 0xEE;// о
            case '\u043F': return (byte) 0xEF;// п
            case '\u0440': return (byte) 0xF0;// р
            case '\u0441': return (byte) 0xF1;// с
            case '\u0442': return (byte) 0xF2;// т
            case '\u0443': return (byte) 0xF3;// у
            case '\u0444': return (byte) 0xF4;// ф
            case '\u0445': return (byte) 0xF5;// х
            case '\u0446': return (byte) 0xF6;// ц
            case '\u0447': return (byte) 0xF7;// ч
            case '\u0448': return (byte) 0xF8;// ш
            case '\u0449': return (byte) 0xF9;// щ
            case '\u044A': return (byte) 0xFA;// ъ
            case '\u044B': return (byte) 0xFB;// ы
            case '\u044C': return (byte) 0xFC;// ь
            case '\u044D': return (byte) 0xFD;// ы
            case '\u044E': return (byte) 0xFE;// ю
            case '\u044F': return (byte) 0xFF;// я
            }
        }
    }
}
