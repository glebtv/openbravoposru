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
package com.openbravo.pos.printer.aurafr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class AuraFR {

//    private ByteArrayOutputStream lineout;

    private static final byte INIT = 0x3f; //Запрос состояния ККМ
    private static final byte PRN = 0x4C; //Печать строки текста
    private static final byte BEEP = 0x47; //Гудок
    private static final byte TITLE = 0x6C; //Печать клише чека
    private static final byte CUT = 0x75; //Отрезать чек
    private static final byte OPEN_DRAWER = (byte) 0x80; //Открыть денежный ящик

    private static final byte OPEN_TICKET = (byte) 0x92; //Открыть чек
    private static final byte CLOSETICKET = (byte) 0x4A;
    private static final byte REGISTRATION = (byte) 0x52;
    private static final byte SELECTMODE = (byte) 0x56;
    private static final byte CANCELMODE = (byte) 0x48;
    private static final byte REFUND = (byte) 0x57;    

    private static final byte XREPORT = (byte) 0x67;
    private static final byte ZREPORT = (byte) 0x5A;

    private static final byte ETX = 0x03; //Запрос состояния ККМ
    private static final byte STX = 0x02; //Печать строки текста

    private static final byte DLE = 0x10; //Печать строки текста

    private static final byte NULL = 0x00; // Нулевое значение

    private static final byte[] PASS = {0x00, 0x00};
    private static final byte[] ADMINPASS = {0x00, 0x00, 0x00, 0x30};

    public AuraFR() {
//        lineout = null;
    }

    public byte[] InitMessage() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(INIT);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();  
    }

    public byte[] BeepMessage() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(BEEP);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] CutTicketMessage(int iFlag) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(CUT);
        lineout.write(iFlag);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] TextMessage(String sText) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(PRN);
        if (sText.length()>40){
            sText = sText.substring(0, 40);
        }
        if (sText != null) {
            byte[] MSG = new byte[sText.length()];
            MSG = UnicodeConverterCP866(sText);
            for (int i = 0; i < MSG.length; i++) {
                lineout.write(MSG[i]);
            }
        }
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

   public byte[] StampTitleReportMessage() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(TITLE);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] OpenDrawerMessage() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(OPEN_DRAWER);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] OpenTicket(int iFlag, int iType) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(OPEN_TICKET);
        lineout.write(iFlag);
        lineout.write(iType);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }
    
    public byte[] RegistrationLine(int iFlag, double dProductPrice, double dProductUnit, int iSection) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(REGISTRATION);
        lineout.write(iFlag);
        byte[] MSG = new byte[5];
        MSG = convertDouble(dProductPrice,2);
        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
        MSG = null;
        MSG = convertDouble(dProductUnit,3);
        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
        lineout.write(iSection);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }
    
    public byte[] RefundLine(int iFlag, double dProductPrice, double dProductUnit) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(REFUND);
        lineout.write(iFlag);
        byte[] MSG = new byte[5];
        MSG = convertDouble(dProductPrice,2);
        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
        MSG = null;
        MSG = convertDouble(dProductUnit,3);
        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] SelectMode(int iMode) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(SELECTMODE);
        lineout.write(iMode);
        for (int i = 0; i < ADMINPASS.length; i++) lineout.write(ADMINPASS[i]);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] CancelMode() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(CANCELMODE);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] CloseTicket(int iFlag,int iPaidType, double dTotalPaid) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(CLOSETICKET);
        lineout.write(iFlag);
        lineout.write(iPaidType);
        byte[] MSG = new byte[5];
        MSG = convertDouble(dTotalPaid,2);
        for (int i = 0; i < MSG.length; i++) lineout.write(MSG[i]);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] XReport(int iTypeReport) {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(XREPORT);
        lineout.write(iTypeReport);
        byte[] bData = new byte[lineout.size()];
        bData = lineout.toByteArray();
        lineout.reset();
        for (int i = 0; i < 4; i++) lineout.write(bData[i]);;
        for (int i = 4; i < bData.length; i++) {
            if (bData[i] == ETX || bData[i] == DLE) {
                lineout.write(DLE);
            }
            lineout.write(bData[i]);
        }
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    public byte[] ZReport() {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(STX);
        for (int i = 0; i < PASS.length; i++) lineout.write(PASS[i]);
        lineout.write(ZREPORT);
        lineout.write(ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        return lineout.toByteArray();
    }

    //Формирование контрольной суммы
    private byte calcCheckSumCRC(byte[] adata) {
        int isum = 0;
        for (int i = 1; i < adata.length; i++) {
            isum = isum ^ adata[i];
        }
        byte result = (byte) isum;
        return result;
    }

    //Конвертация текста из UTF8 в CP866
    private byte[] UnicodeConverterCP866(String sText) {
        if (sText == null) {
            return new byte[0];
        } else {
            byte[] result = new byte[sText.length()];
            for (int i = 0; i < sText.length(); i++) {
                result[i] = transChar(sText.charAt(i));
            }
            return result;
        }
    }

    private byte[] convertDouble(double dValue, int iRound) {

        double newDouble = new BigDecimal(dValue * Math.pow(10, iRound)).setScale(0, RoundingMode.UP).doubleValue();
        String sString = Double.toString(newDouble);
        sString = sString.replace(".0", "");
        int iStringLength = sString.length();
        if (iStringLength > 10) {
            sString = "0";
        }
        for (int i = 0; i < 10 - iStringLength; i++) {
            sString = "0".concat(sString);
        }
        return hexStringToByteArray(sString);
    }

     private byte[] hexStringToByteArray(String s) {
	int len = s.length();
	byte[] data = new byte[len / 2];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
	}
	return data;
    }


    //Конвертация таблицы символов из UTF8
    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080) && (sChar != 0x0024)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
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
            case '\u044A': return (byte) 0xEA;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEC;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

            // kk - Kazakh - Ә ә Ғ ғ Қ қ Ң ң Ө ө Ұ ұ Ү ү Һ һ І i
            
            case '\u04D8': return (byte) 0xB0;// Ә
            case '\u04D9': return (byte) 0xB1;// ә
            case '\u0492': return (byte) 0xDB;// Ғ
            case '\u0493': return (byte) 0xDC;// ғ
            case '\u049A': return (byte) 0xDE;// Қ
            case '\u049B': return (byte) 0xDF;// қ
            case '\u04A2': return (byte) 0xF0;// Ң
            case '\u04A3': return (byte) 0xF1;// ң
            case '\u04E8': return (byte) 0xF3;// Ө
            case '\u04E9': return (byte) 0xF4;// ө
            case '\u04B0': return (byte) 0xF5;// Ұ
            case '\u04B1': return (byte) 0xF6;// ұ
            case '\u04AE': return (byte) 0xF7;// Ү
            case '\u04AF': return (byte) 0xF8;// ү
            case '\u04BA': return (byte) 0xFD;// Һ
            case '\u04BB': return (byte) 0xFE;// һ
            case '\u0406': return (byte) 0x49;// І
            case '\u0456': return (byte) 0x69;// i

            case '\u00A0': return (byte) 0x7F;// &nbsp
            case '\u2116': return (byte) 0x24;// №
            case '\u20AC': return (byte) 0xF2;// €
            case '\u0024': return (byte) 0xFC;// $
            case '\u2014': return (byte) 0xDD;// —

                  default: return 0x3F; // ? Not valid character.
            }
        }
    }

        public String ReadErrorMessage(byte bMessage){
             switch (bMessage) {
             case 0x00 : return "Ошибок нет";
             case 0x01 : return "Контрольная лента обработана без ошибок";
             case 0x08 : return "Неверная цена (сумма)";
             case 0x0A : return "Неверное количество";
             case 0x0B : return "Переполнение счетчика наличности";
             case 0x0C : return "Невозможно сторно последней операции";
             case 0x0D : return "Сторно по коду невозможно (в чеке зарегистрировано меньшее количество товаров с указанным кодом)";
             case 0x0E : return "Невозможен повтор последней операции";
             case 0x0F : return "Повторная скидка на операцию невозможна";
             case 0x10 : return "Скидка/надбавка на предыдущую операцию невозможна";
             case 0x11 : return "Неверный код товара";
             case 0x12 : return "Неверный штрих-код товара";
             case 0x13 : return "Неверный формат";
             case 0x14 : return "Неверная длина";
             case 0x15 : return "ККМ заблокирована в режиме ввода даты";
             case 0x16 : return "Требуется подтверждение ввода даты";
             case 0x18 : return "Нет больше данных для передачи ПО ККМ";
             case 0x19 : return "Нет подтверждения или отмены продажи";
             case 0x1A : return "Отчет с гашением прерван. Вход в режим невозможен.";
             case 0x1B : return "Отключение контроля наличности невозможно (не настроены необходимые типы оплаты).";
             case 0x1E : return "Вход в режим заблокирован";
             case 0x1F : return "Проверьте дату и время";
             case 0x20 : return "Зарезервировано";
             case 0x21 : return "Невозможно закрыть архив";
             case 0x3D : return "Товар не найден";
             case 0x3E : return "Весовой штрих-код с количеством <>1.000";
             case 0x3F : return "Переполнение буфера чека";
             case 0x40 : return "Недостаточное количество товара";
             case 0x41 : return "Сторнируемое количество больше проданного";
             case 0x42 : return "Заблокированный товар не найден в буфере чека";
             case 0x43 : return "Данный товар не продавался в чеке, сторно невозможно";
             case 0x44 : return "Memo PlusTM 3TM заблокировано с ПК";
             case 0x45 : return "Ошибка контрольной суммы таблицы настроек Memo PlusTM 3TM";
             case 0x46 : return "Неверная команда от ККМ";
             case 0x66 : return "Команда не реализуется в данном режиме ККМ";
             case 0x67 : return "Нет бумаги";
             case 0x68 : return "Нет связи с принтером чеков";
             case 0x69 : return "Механическая ошибка печатающего устройства";
             case 0x6A : return "Неверный тип чека";
             case 0x6B : return "Нет больше строк картинки";
             case 0x6C : return "Неверный номер регистра";
             case 0x6D : return "Недопустимое целевое устройство";
             case 0x6E : return "Нет места в массиве картинок";
             case 0x6F : return "Неверный номер картинки / картинка отсутствует";
             case 0x70 : return "Сумма сторно больше, чем было получено данным типом оплаты";
             case 0x71 : return "Сумма не наличных платежей превышает сумму чека";
             case 0x72 : return "Сумма платежей меньше суммы чека";
             case 0x73 : return "Накопление меньше суммы возврата или аннулирования";
             case 0x75 : return "Переполнение суммы платежей";
             case 0x76 : return "(зарезервировано)";
             case 0x7A : return "Данная модель ККМ не может выполнить команду";
             case 0x7B : return "Неверная величина скидки / надбавки";
             case 0x7C : return "Операция после скидки / надбавки невозможна";
             case 0x7D : return "Неверная секция";
             case 0x7E : return "Неверный вид оплаты";
             case 0x7F : return "Переполнение при умножении";
             case (byte) 0x80 : return "Операция запрещена в таблице настроек";
             case (byte) 0x81 : return "Переполнение итога чека";
             case (byte) 0x82 : return "Открыт чек аннулирования – операция невозможна";
             case (byte) 0x84 : return "Переполнение буфера контрольной ленты";
             case (byte) 0x86 : return "Вносимая клиентом сумма меньше суммы чека";
             case (byte) 0x87 : return "Открыт чек возврата – операция невозможна";
             case (byte) 0x88 : return "Смена превысила 24 часа";
             case (byte) 0x89 : return "Открыт чек продажи – операция невозможна";
             case (byte) 0x8A : return "Переполнение ФП";
             case (byte) 0x8C : return "Неверный пароль";
             case (byte) 0x8D : return "Буфер контрольной ленты не переполнен";
             case (byte) 0x8E : return "Идет обработка контрольной ленты";
             case (byte) 0x8F : return "Обнуленная касса (повторное гашение невозможно)";
             case (byte) 0x91 : return "Неверный номер таблицы";
             case (byte) 0x92 : return "Неверный номер ряда";
             case (byte) 0x93 : return "Неверный номер поля";
             case (byte) 0x94 : return "Неверная дата";
             case (byte) 0x95 : return "Неверное время";
             case (byte) 0x96 : return "Сумма чека по секции меньше суммы сторно";
             case (byte) 0x97 : return "Подсчет суммы сдачи невозможен";
             case (byte) 0x98 : return "В ККМ нет денег для выплаты";
             case (byte) 0x9A : return "Чек закрыт – операция невозможна";
             case (byte) 0x9B : return "Чек открыт – операция невозможна";
             case (byte) 0x9C : return "Смена открыта, операция невозможна";
             case (byte) 0x9D : return "ККМ заблокирована, ждет ввода пароля доступа к ФП";
             case (byte) 0x9E : return "Заводской номер уже задан";
             case (byte) 0x9F : return "Количество перерегистраций не может быть более 4";
             case (byte) 0xA0 : return "Ошибка ФП";
             case (byte) 0xA2 : return "Неверная смена";
             case (byte) 0xA3 : return "Неверный тип отчета";
             case (byte) 0xA4 : return "Недопустимый пароль";
             case (byte) 0xA5 : return "Недопустимый заводской номер ККМ";
             case (byte) 0xA6 : return "Недопустимый РНМ";
             case (byte) 0xA7 : return "Недопустимый ИНН";
             case (byte) 0xA8 : return "ККМ не фискализирована";
             case (byte) 0xA9 : return "Не задан заводской номер";
             case (byte) 0xAA : return "Нет отчетов";
             case (byte) 0xAB : return "Режим не активизирован";
             case (byte) 0xAC : return "Нет указанного чека в КЛ";
             case (byte) 0xAD : return "Нет больше записей КЛ";
             case (byte) 0xAE : return "Некорректный код или номер кода защиты ККМ";
             case (byte) 0xB0 : return "Требуется выполнение общего гашения";
             case (byte) 0xB1 : return "Команда не разрешена введенными кодами защиты ККМ";
             case (byte) 0xB2 : return "Невозможна отмена скидки/надбавки";
             case (byte) 0xB3 : return "Невозможно закрыть чек данным типом оплаты (в чеке присутствуют операции без контроля наличных)";
             case (byte) 0xBA : return "Ошибка обмена с фискальным модулем";
             case (byte) 0xBE : return "Необходимо провести профилактические работы";
             case (byte) 0xC8 : return "Нет устройства, обрабатывающего данную команду";
             case (byte) 0xC9 : return "Нет связи с внешним устройством";
             case (byte) 0xCA : return "Ошибочное состояние ТРК";
             case (byte) 0xCB : return "Больше одной регистрации в чеке";
             case (byte) 0xCC : return "Ошибочный номер ТРК";
             case (byte) 0xCD : return "Неверный делитель";
             case (byte) 0xCF : return "В ККМ произведено 20 активизаций";
             case (byte) 0xD0 : return "Зарезервировано";
             case (byte) 0xD1 : return "Перегрев головки принтера";
             case (byte) 0xD2 : return "Зарезервировано";
             case (byte) 0xD3 : return "Зарезервировано";
             case (byte) 0xD4 : return "Зарезервировано";
             case (byte) 0xD5 : return "Зарезервировано";
             case (byte) 0xD6 : return "Зарезервировано";
             case (byte) 0xD7 : return "Зарезервировано";
             case (byte) 0xD8 : return "Зарезервировано";
             case (byte) 0xD9 : return "Зарезервировано";
             case (byte) 0xDA : return "Зарезервировано";
             case (byte) 0xDB : return "Зарезервировано";
             case (byte) 0xDC : return "Буфер переполнен";
             case (byte) 0xDD : return "Невозможно напечатать вторую фискальную копию";
             case (byte) 0xDE : return "Требуется гашение ЭЖ";
             
                      default : return "The error message is not identified.";
         }
    }
}
