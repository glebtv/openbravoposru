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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Andrey Svininykh svininykh@gmail.com
 */

public class MassaKVPM {
    private static final byte TERM_C = 0x0C; // Конец строки
    private static final byte TERM_D = 0x0D; // Конец последней строки
    private static final byte STATUS_PLU_VALUE = 0x00; // Центровка наименования: 0x01 = true; 0x00 = false.
    private static final byte STATUS_PLU_COUNTRY = 0x00; // Код страны: 0x00 = Россия.
    private static final byte FORMAT_LABEL = 0x01; // Формата этикетки: 0x00 ... 0x0A = файл формата этикеток
    private static final byte FORMAT_BARCODE = 0x01; // Формат штрихкода: 0x00 ... 0x0A = файл штрихкодов
    private static final byte CODE_FONT = 0x00; // Код размера шрифта
    private static final byte[] WEIGHT_CONTAINER = new byte[]{0x00, 0x00, 0x00, 0x00};
    private static final byte[] DATA_REALIZATION = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] DATA_VALIDITY = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    private static final byte[] CODE_SERTIFICATION = new byte[]{0x20, 0x20, 0x20, 0x20};
    private static final byte[] NUMBER_GROUP_GENERAL = new byte[]{0x00, 0x00};
    private static final byte[] RESERVATION = new byte[]{0x00, 0x00};
    private static final byte[] STRING_CONSISTATION = new byte[]{0x00, 0x00, TERM_D}; // Строка состав товара: 0x00 = размер шрифта строки; 0x00 = длина строки.
    private static final byte[] STRING_INFORMATION = new byte[]{0x00, 0x00, TERM_D}; // Строка информационного сообщения: 0x00 = размер шрифта строки; 0x00 = длина строки.

    public MassaKVPM() {

    }

    public static byte[] CreatePLUMessage(String sBarcode, Double dPrice, String sName) throws IOException {
        ByteArrayOutputStream productmessage = new ByteArrayOutputStream();
        productmessage.write(convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode.substring(3))))); // Номер PLU
        productmessage.write(convertShortIntegerToByteArray(Integer.reverseBytes(38+sName.length()+3+STRING_CONSISTATION.length+STRING_INFORMATION.length))); // Длина записи данных
        productmessage.write(STATUS_PLU_VALUE); // Статус PLU - Наличие параметров в записи PLU
        productmessage.write(STATUS_PLU_COUNTRY); // Статус PLU - Код страны
        productmessage.write(FORMAT_LABEL); // Номер формата этикетки
        productmessage.write(FORMAT_BARCODE); // Номер формата штрихкода
        productmessage.write(Integer.parseInt(sBarcode.substring(0,3)));// Префикс штрихкода 00
        productmessage.write(convertIntegerToByteArray(Integer.reverseBytes((int)(dPrice * 100)))); // Цена за единицу веса
        productmessage.write(WEIGHT_CONTAINER); // Вес тары
        productmessage.write(convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode)))); // Код товара
        productmessage.write(DATA_REALIZATION); // Дата реализации
        productmessage.write(DATA_VALIDITY); // Срок годности
        productmessage.write(CODE_SERTIFICATION); // Код органа сертификации
        productmessage.write(NUMBER_GROUP_GENERAL); // Номер основной группы
        productmessage.write(RESERVATION); // Резерв
        productmessage.write(CODE_FONT); // Наименование товара - Размер шрифта строки
//        System.out.println("Lenth NAME = " + sName.length());
        productmessage.write(sName.length()); // Наименование товара - Длина строки
        productmessage.write(convertASCII(sName)); // Наименование товара - Текст строки
        productmessage.write(TERM_D); // Наименование товара - Терминатор строки
        productmessage.write(STRING_CONSISTATION); // Состав товара
        productmessage.write(STRING_INFORMATION); // Информационное сообщение
//        System.out.println("Lenth MES = " + productmessage.size());
        productmessage.write(calcCheckSum(productmessage.toByteArray())); // Контрольный разряд данных
        return productmessage.toByteArray();
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += " " + Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public static final byte[] convertIntegerToByteArray(int value) {
        return new byte[]{
                    (byte) (value >>> 24),
                    (byte) (value >>> 16),
                    (byte) (value >>> 8),
                    (byte) value};
    }

    public static final byte[] convertShortIntegerToByteArray(int value) {
        return new byte[]{
                    (byte) (value >>> 24),
                    (byte) (value >>> 16)};
    }

    private static byte[] convertASCII(String sdata) {
        byte[] result = new byte[sdata.length()];
        for (int i = 0; i < sdata.length(); i++) {
            char c = sdata.charAt(i);
            if ((c >= 0x0020) && (c < 0x0080)) {
                result[i] = (byte) c;
            } else {
                result[i] = convertCyrillic(c);
            }
        }
        return result;
    }

    private static byte convertCyrillic(char sdata) {
        switch (sdata) {

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

            default: return (byte) 0x3F; // ? Not valid character.
        }
    }

    private static int CalcCRC16MassaK(short crc, byte[] buf, int len, int tab) {
        short bits;
        short accumulator, temp;

        for (int k = tab; k < len; k++) {
            accumulator = 0;
            temp = (short) ((crc >> 8) << 8);
            for (bits = 0; bits < 8; bits++) {
                if (((temp ^ accumulator) & 0x8000) == 0x8000) {
                    accumulator = (short) ((accumulator << 1) ^ 0x1021);
                } else {
                    accumulator <<= 1;
                }
                temp <<= 1;
            }
            crc = (short) (accumulator ^ (crc << 8) ^ (buf[k] & 0xff));
        }
        return crc;
    }

    private static byte calcCheckSum(byte[] adata) {
        int isum = 0;

        for (int i = 0; i < adata.length; i++) {
            isum += adata[i];
        }

        byte high = (byte) ((isum & 0xFF00) >> 8);
        byte low = (byte) (isum & 0x00FF);

//        System.out.println("CRC = " + getHexString(result));
//        System.out.println("CRC low = " + Integer.toHexString(low));

        return low;
    }

}
