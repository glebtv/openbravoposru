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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.openbravo.pos.util.ByteArrayUtils;
import java.io.UnsupportedEncodingException;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

public class MassaKVPM {
    
    private static final byte[] HEADER = new byte[]{(byte) 0xF8, 0x55, (byte) 0xCE}; // Код заголовка
    private static final byte TYPE_FILE = (byte) 0x01; // Тип файла: PLU (товары) = BIT_0

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
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode.substring(3))), 4, false)); // Номер PLU
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(38+sName.length()+3+STRING_CONSISTATION.length+STRING_INFORMATION.length), 2, true)); // Длина записи данных
        productmessage.write(STATUS_PLU_VALUE); // Статус PLU - Наличие параметров в записи PLU
        productmessage.write(STATUS_PLU_COUNTRY); // Статус PLU - Код страны
        productmessage.write(FORMAT_LABEL); // Номер формата этикетки
        productmessage.write(FORMAT_BARCODE); // Номер формата штрихкода
        productmessage.write(Integer.parseInt(sBarcode.substring(0,2)));// Префикс штрихкода        
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes((int)(dPrice * 100)), 4, false)); // Цена за единицу веса
        productmessage.write(WEIGHT_CONTAINER); // Вес тары
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode.substring(2))), 4, false)); // Код товара
        productmessage.write(DATA_REALIZATION); // Дата реализации
        productmessage.write(DATA_VALIDITY); // Срок годности
        productmessage.write(CODE_SERTIFICATION); // Код органа сертификации
        productmessage.write(NUMBER_GROUP_GENERAL); // Номер основной группы
        productmessage.write(RESERVATION); // Резерв
        productmessage.write(CODE_FONT); // Наименование товара - Размер шрифта строки
//        System.out.println("Lenth NAME = " + sName.length());
        productmessage.write(sName.length()); // Наименование товара - Длина строки
        productmessage.write(convertCP1251(sName)); // Наименование товара - Текст строки
        productmessage.write(TERM_D); // Наименование товара - Терминатор строки
        productmessage.write(STRING_CONSISTATION); // Состав товара
        productmessage.write(STRING_INFORMATION); // Информационное сообщение
//        System.out.println("Lenth MES = " + productmessage.size());
        productmessage.write(calcCheckSum(productmessage.toByteArray())); // Контрольный разряд данных
        return productmessage.toByteArray();
    }

    public static byte[] CreateDATAMessage(byte bCommand, byte[] bData, int iCurrentRow, int iRows) throws IOException {
//        System.out.println(iCurrentRow+" of " + iRows);
        ByteArrayOutputStream datamessage = new ByteArrayOutputStream();
        datamessage.write(HEADER); // Заголовочная последовательность
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(8 + bData.length), 2, true)); // Длина тела сообщения
        datamessage.write(bCommand); // Управляющая команда
        datamessage.write(TYPE_FILE); // Тип файла
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(iRows), 2, true)); // Число записей в файле
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(iCurrentRow), 2, true)); // Номер текущей записи
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(bData.length), 2, true)); // Длина записи
        datamessage.write(bData); // Данные
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(CalcCRC16MassaK((short) 0, datamessage.toByteArray(), 5)), 2, true)); //CRC
        return datamessage.toByteArray();
    }

    public static byte[] CreateUDPMessage(byte bCommand) throws IOException {
        ByteArrayOutputStream datamessage = new ByteArrayOutputStream();
        datamessage.write(HEADER); // Заголовочная последовательность
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(1), 2, true)); // Длина тела сообщения
        datamessage.write(bCommand); // Управляющая команда
        datamessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(CalcCRC16MassaK((short) 0, datamessage.toByteArray(), 5)), 2, true)); //CRC
        return datamessage.toByteArray();
    }

    private static byte[] convertCP1251(String sdata) {
        byte[] result = new byte[sdata.length()];
//        result = new String(sdata.getBytes("UTF8"), "CP1251").getBytes();
//        System.out.println("convert = " + getHexString(result));

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
            // А Б В Г Д Е Ё Ж З И Й К Л М Н О П Р С Т У Ф Х Ц Ч Ш Щ Ъ Ы Ь Э Ю Я
            // а б в г д е ё ж з и й к л м н о п р с т у ф х ц ч ш щ ъ ы ь э ю я
            
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

            // kk - Kazakh - Ә ә Ғ ғ Қ қ Ң ң Ө ө Ұ ұ Ү ү Һ һ І i

            case '\u04D8': return (byte) 0xA3;// Ә
            case '\u04D9': return (byte) 0xBC;// ә
            case '\u0492': return (byte) 0xAA;// Ғ
            case '\u0493': return (byte) 0xBA;// ғ
            case '\u049A': return (byte) 0x8D;// Қ
            case '\u049B': return (byte) 0x9D;// қ
            case '\u04A2': return (byte) 0xBD;// Ң
            case '\u04A3': return (byte) 0xBE;// ң
            case '\u04E8': return (byte) 0xA5;// Ө
            case '\u04E9': return (byte) 0xB4;// ө
            case '\u04B0': return (byte) 0xA1;// Ұ
            case '\u04B1': return (byte) 0xA2;// ұ
            case '\u04AE': return (byte) 0xAF;// Ү
            case '\u04AF': return (byte) 0xBF;// ү
            case '\u04BA': return (byte) 0x8E;// Һ
            case '\u04BB': return (byte) 0x9E;// һ
            case '\u0406': return (byte) 0xB2;// І
            case '\u0456': return (byte) 0xB3;// i

            // ua - Ukrainian - Є є Ї ї Ґ ґ
//            case '\u0404': return (byte) 0xAA;// Є
//            case '\u0454': return (byte) 0xBA;// є
//            case '\u0407': return (byte) 0xAF;// Ї
//            case '\u0457': return (byte) 0xBF;// ї
//            case '\u0490': return (byte) 0xA5;// Ґ
//            case '\u0491': return (byte) 0xB4;// ґ

            default: return (byte) 0x3F; // ? Not valid character.
        }
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

    private static int CalcCRC16MassaK(short crc, byte[] buf, int tab) {
        short bits;
        short accumulator, temp;

        for (int k = tab; k < buf.length; k++) {
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

//        System.out.println("CRC16 MassaK = " + Integer.toHexString((int) crc));

        return crc;
    }
}
