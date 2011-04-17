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
import com.openbravo.pos.util.ByteArrayUtils;
import java.io.UnsupportedEncodingException;

/**
 * @author Andrey Svininykh svininykh@gmail.com
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
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode.substring(3))))); // Номер PLU
        productmessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(38+sName.length()+3+STRING_CONSISTATION.length+STRING_INFORMATION.length))); // Длина записи данных
        productmessage.write(STATUS_PLU_VALUE); // Статус PLU - Наличие параметров в записи PLU
        productmessage.write(STATUS_PLU_COUNTRY); // Статус PLU - Код страны
        productmessage.write(FORMAT_LABEL); // Номер формата этикетки
        productmessage.write(FORMAT_BARCODE); // Номер формата штрихкода
        productmessage.write(Integer.parseInt(sBarcode.substring(0,3)));// Префикс штрихкода 00
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes((int)(dPrice * 100)))); // Цена за единицу веса
        productmessage.write(WEIGHT_CONTAINER); // Вес тары
        productmessage.write(ByteArrayUtils.convertIntegerToByteArray(Integer.reverseBytes(Integer.parseInt(sBarcode)))); // Код товара
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
        ByteArrayOutputStream datamessage = new ByteArrayOutputStream();
        datamessage.write(HEADER); // Заголовочная последовательность
        datamessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(8 + bData.length))); // Длина тела сообщения
        datamessage.write(bCommand); // Управляющая команда
        datamessage.write(TYPE_FILE); // Тип файла
        datamessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(iRows))); // Число записей в файле
        datamessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(iCurrentRow))); // Номер текущей записи
        datamessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(bData.length))); // Длина записи
        datamessage.write(bData); // Данные
        datamessage.write(ByteArrayUtils.convertShortIntegerToByteArray(Integer.reverseBytes(CalcCRC16MassaK((short) 0, datamessage.toByteArray(), 5)))); //CRC
        return datamessage.toByteArray();
    }

    private static byte[] convertCP1251(String sdata) throws UnsupportedEncodingException {
        sdata = new String(sdata.getBytes("UTF8"), "CP1251");
        byte[] result = new byte[sdata.length()];
        for (int i = 0; i < sdata.length(); i++) {
            char c = sdata.charAt(i);
            if ((c >= 0x0020) && (c < 0x0080) && (c >= 0x00C0) && (c <= 0x00FF)) {
                result[i] = (byte) c;
            } else {
                result[i] = 0x3F; // ? Not valid character.
            }
        }
        return result;
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
