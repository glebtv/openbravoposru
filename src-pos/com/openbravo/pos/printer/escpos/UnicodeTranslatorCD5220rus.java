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

public class UnicodeTranslatorCD5220rus extends UnicodeTranslator {
    
    /** Creates a new instance of UnicodeTranslatorInt */
    public UnicodeTranslatorCD5220rus() {
    }
    
    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_RUS;
    }
    
    public byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
        
                default: return 0x3F; // ? Not valid character.

            // ru - Russian
            case '\u0410': return (byte) 0x80;// A
            case '\u0411': return (byte) 0x81;// Б
            case '\u0412': return (byte) 0x82;// В
            case '\u0413': return (byte) 0x83;// Г
            case '\u0414': return (byte) 0x84;// Д
            case '\u0415': return (byte) 0x85;// Е
            case '\u0401': return (byte) 0x85;// Ё
            case '\u0416': return (byte) 0x86;// Ж
            case '\u0417': return (byte) 0x87;// З
            case '\u0418': return (byte) 0x88;// И
            case '\u0419': return (byte) 0x89;// Й
            case '\u041A': return (byte) 0x8A;// К
            case '\u041B': return (byte) 0x8B;// Л
            case '\u041C': return (byte) 0x8C;// М
            case '\u041D': return (byte) 0x8D;// Н
            case '\u041E': return (byte) 0x8E;// О
            case '\u041F': return (byte) 0x8F;// П
            case '\u0420': return (byte) 0x90;// Р
            case '\u0421': return (byte) 0x91;// С
            case '\u0422': return (byte) 0x92;// Т
            case '\u0423': return (byte) 0x93;// У
            case '\u0424': return (byte) 0x94;// Ф
            case '\u0425': return (byte) 0x95;// Х
            case '\u0426': return (byte) 0x96;// Ц
            case '\u0427': return (byte) 0x97;// Ч
            case '\u0428': return (byte) 0xE8;// Ш
            case '\u0429': return (byte) 0xE9;// Щ
            case '\u042A': return (byte) 0x9A;// Ъ
            case '\u042B': return (byte) 0x9B;// Ы
            case '\u042C': return (byte) 0x9C;// Ь
            case '\u042D': return (byte) 0x9D;// Э
            case '\u042E': return (byte) 0x9E;// Ю
            case '\u042F': return (byte) 0x9F;// Я
            case '\u0430': return (byte) 0xA0;// a
            case '\u0431': return (byte) 0xA1;// б
            case '\u0432': return (byte) 0xA2;// в
            case '\u0433': return (byte) 0xA3;// г
            case '\u0434': return (byte) 0xA4;// д
            case '\u0435': return (byte) 0xA5;// е
            case '\u0451': return (byte) 0xA5;// ё
            case '\u0436': return (byte) 0xA6;// ж
            case '\u0437': return (byte) 0xA7;// з
            case '\u0438': return (byte) 0xA8;// и
            case '\u0439': return (byte) 0xA9;// й
            case '\u043A': return (byte) 0xAA;// к
            case '\u043B': return (byte) 0xAB;// л
            case '\u043C': return (byte) 0xAC;// м
            case '\u043D': return (byte) 0xAD;// н
            case '\u043E': return (byte) 0xAE;// о
            case '\u043F': return (byte) 0xAF;// п
            case '\u0440': return (byte) 0xE0;// р
            case '\u0441': return (byte) 0xE1;// с
            case '\u0442': return (byte) 0xE2;// т
            case '\u0443': return (byte) 0xE3;// у
            case '\u0444': return (byte) 0xE4;// ф
            case '\u0445': return (byte) 0xE5;// х
            case '\u0446': return (byte) 0xE6;// ц
            case '\u0447': return (byte) 0xE7;// ч
            case '\u0448': return (byte) 0xE8;// ш
            case '\u0449': return (byte) 0xE9;// щ
            case '\u044A': return (byte) 0xEC;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEA;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

            // kk - Kazakh
            case '\u04D8': return (byte) 0xF0;// Ә
            case '\u04D9': return (byte) 0xF8;// ә
            case '\u0492': return (byte) 0xF1;// Ғ
            case '\u0493': return (byte) 0xF9;// ғ
            case '\u049A': return (byte) 0xF2;// Қ
            case '\u049B': return (byte) 0xFA;// қ
            case '\u04A2': return (byte) 0xF3;// Ң
            case '\u04A3': return (byte) 0xFB;// ң
            case '\u04E8': return (byte) 0xF4;// Ө
            case '\u04E9': return (byte) 0xFC;// ө
            case '\u04B0': return (byte) 0xF5;// Ұ
            case '\u04B1': return (byte) 0xFD;// ұ
            case '\u04AE': return (byte) 0xF6;// Ү
            case '\u04AF': return (byte) 0xFE;// ү
            case '\u04BA': return (byte) 0xF7;// Һ
            case '\u04BB': return (byte) 0xF7;// һ
            case '\u0406': return (byte) 0x49;// І
            case '\u0456': return (byte) 0x69;// i
 
            }          
        }
    }   
    
    public byte transNumberChar(char sChar) {
        switch (sChar) {
        case '0' : return 0x30;
        case '1' : return 0x31;
        case '2' : return 0x32;
        case '3' : return 0x33;
        case '4' : return 0x34;
        case '5' : return 0x35;
        case '6' : return 0x36;
        case '7' : return 0x37;
        case '8' : return 0x38;
        case '9' : return 0x39;
        default: return 0x30;
        }          
    }     
}
