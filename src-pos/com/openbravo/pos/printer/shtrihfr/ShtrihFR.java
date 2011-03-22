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

package com.openbravo.pos.printer.shtrihfr;

public class  ShtrihFR {

    public static final byte TYPE   = (byte) 0xFC; // Получить тип устройства
    public static final byte INIT   = 0x10; // Короткий запрос состояния ККМ

    public static final byte PRN    = 0x17; //Печать строки текста
    public static final byte BEEP   = 0x13; //Гудок

    public static final byte SALE   = (byte) 0x80; //Продажа

    public static final byte OPEN_TICKET   = (byte) 0x8D; //Открыть чек
    public static final byte CLOSE_TICKET   = (byte) 0x85; //Закрыть чек

    public static final byte TITLE  = 0x18; //Печать заголовка

    public static final byte PRINT_IMAGE  = (byte) 0xC1; //Печать графики
    public static final byte OPEN_DRAWER  = 0x28; //Открыть денежный ящик
    public static final byte PARTIAL_CUT  = 0x25; //Отрезка чека
    public static final byte NEW_LINE     = 0x29; //Протяжка

    public static final byte X_REPORT  = 0x40; //Суточный отчёт без гашения
    public static final byte Z_REPORT  = 0x41; //Суточный отчёт c гашением

    public static final int FLAG_MDE  = 0; //Флаг устанавливающий работу с копейками

    public ShtrihFR() {

    }

        //Формирование контрольной суммы
    public byte calcCheckSumCRC(byte[] adata) {
        int isum = 0;
        for (int i = 0; i < adata.length; i++) {
            isum = isum ^ adata[i];
        }
        byte result = (byte) isum;
        return result;
    }

    public static byte[] convertHEX(long sdata) {

        byte[] result = new byte[24];

        if (sdata <= 255.00 && sdata >= 0) {
            result[0] = (byte) sdata;
        } else {
            int j = 0;
            String buff = Long.toHexString(Long.reverseBytes(sdata));
            for (int i = 0; i < buff.length() / 2; i++) {
                result[i] = (byte) Integer.parseInt(buff.substring(j, j + 2), 16);
                j = j + 2;
            }
        }
        return result;
    }

    //Конвертация текста из UTF8 в CP1251
    public byte[] UnicodeConverterCP1251(String sText) {
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

    //Конвертация таблицы символов из UTF8 в CP1251
    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
            // ru - Russian
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

                  default: return 0x3F; // ? Not valid character.
            }
        }
    }

        public String ReadSendMessage(byte bMessage){
                 switch (bMessage){
             case 0x01 : return "Запрос дампа";
             case 0x02 : return "Запрос данных";
             case 0x03 : return "Прерывание выдачи данных";
             case 0x0D : return "Фискализация (перерегистрация) с длинным РНМ";
             case 0x0E : return "Ввод длинного заводского номера";
             case 0x0F : return "Запрос длинного заводского номера и длинного РНМ";
             case 0x10 : return "Короткий запрос состояния ФР";
             case 0x11 : return "Запрос состояния ФР";
             case 0x12 : return "Печать жирной строки";
             case 0x13 : return "Гудок";
             case 0x14 : return "Установка параметров обмена";
             case 0x15 : return "Чтение параметров обмена";
             case 0x16 : return "Технологическое обнуление";
             case 0x17 : return "Печать строки";
             case 0x18 : return "Печать заголовка документа";
             case 0x19 : return "Тестовый прогон";
             case 0x1A : return "Запрос денежного регистра";
             case 0x1B : return "Запрос операционного регистра";
             case 0x1C : return "Запись лицензии";
             case 0x1D : return "Чтение лицензии";
             case 0x1E : return "Запись таблицы";
             case 0x1F : return "Чтение таблицы";
             case 0x20 : return "Запись положения десятичной точки";
             case 0x21 : return "Программирование времени";
             case 0x22 : return "Программирование даты";
             case 0x23 : return "Подтверждение программирования даты";
             case 0x24 : return "Инициализация таблиц начальными значениями";
             case 0x25 : return "Отрезка чека";
             case 0x26 : return "Прочитать параметры шрифта";
             case 0x27 : return "Общее гашение";
             case 0x28 : return "Открыть денежный ящик";
             case 0x29 : return "Протяжка";
             case 0x2A : return "Выброс подкладного документа";
             case 0x2B : return "Прерывание тестового прогона";
             case 0x2C : return "Снятие показаний операционных регистров";
             case 0x2D : return "Запрос структуры таблицы";
             case 0x2E : return "Запрос структуры поля";
             case 0x2F : return "Печать строки данным шрифтом";
             case 0x40 : return "Суточный отчет без гашения";
             case 0x41 : return "Суточный отчет с гашением";
             case 0x42 : return "Отчёт по секциям";
             case 0x43 : return "Отчёт по налогам";
             case 0x50 : return "Внесение";
             case 0x51 : return "Выплата";
             case 0x60 : return "Ввод заводского номера";
             case 0x61 : return "Инициализация ФП";
             case 0x62 : return "Запрос суммы записей в ФП";
             case 0x63 : return "Запрос даты последней записи в ФП";
             case 0x64 : return "Запрос диапазона дат и смен";
             case 0x65 : return "Фискализация (перерегистрация)";
             case 0x66 : return "Фискальный отчет по диапазону дат";
             case 0x67 : return "Фискальный отчет по диапазону смен";
             case 0x68 : return "Прерывание полного отчета";
             case 0x69 : return "Чтение параметров фискализации (перерегистрации)";
             case 0x70 : return "Открыть фискальный подкладной документ";
             case 0x71 : return "Открыть стандартный фискальный подкладной документ";
             case 0x72 : return "Формирование операции на подкладном документе";
             case 0x73 : return "Формирование стандартной операции на подкладном документе";
             case 0x74 : return "Формирование скидки/надбавки на подкладном документе";
             case 0x75 : return "Формирование стандартной скидки/надбавки на подкладном документе";
             case 0x76 : return "Формирование закрытия чека на подкладном документе";
             case 0x77 : return "Формирование стандартного закрытия чека на подкладном документе";
             case 0x78 : return "Конфигурация подкладного документа";
             case 0x79 : return "Установка стандартной конфигурации подкладного документа";
             case 0x7A : return "Заполнение буфера подкладного документа нефискальной информацией";
             case 0x7B : return "Очистка строки буфера подкладного документа от нефискальной информации";
             case 0x7C : return "Очистка всего буфера подкладного документа от нефискальной информации";
             case 0x7D : return "Печать подкладного документа";
             case 0x7E : return "Общая конфигурация подкладного документа";
             case (byte) 0x80 : return "Продажа";
             case (byte) 0x81 : return "Покупка";
             case (byte) 0x82 : return "Возврат продажи";
             case (byte) 0x83 : return "Возврат покупки";
             case (byte) 0x84 : return "Сторно";
             case (byte) 0x85 : return "Закрытие чека";
             case (byte) 0x86 : return "Скидка";
             case (byte) 0x87 : return "Надбавка";
             case (byte) 0x88 : return "Аннулирование чека";
             case (byte) 0x89 : return "Подытог чека";
             case (byte) 0x8A : return "Сторно скидки";
             case (byte) 0x8B : return "Сторно надбавки";
             case (byte) 0x8C : return "Повтор документа";
             case (byte) 0x8D : return "Открыть чек";
             case (byte) 0x90 : return "Формирование чека отпуска нефтепродуктов в режиме предоплаты заданной дозы";
             case (byte) 0x91 : return "Формирование чека отпуска нефтепродуктов в режиме предоплаты на заданную сумму";
             case (byte) 0x92 : return "Формирование чека коррекции при неполном отпуске нефтепродуктов";
             case (byte) 0x93 : return "Задание дозы РК в миллилитрах";
             case (byte) 0x94 : return "Задание дозы РК в денежных единицах";
             case (byte) 0x95 : return "Продажа нефтепродуктов";
             case (byte) 0x96 : return "Останов РК";
             case (byte) 0x97 : return "Пуск РК";
             case (byte) 0x98 : return "Сброс РК";
             case (byte) 0x99 : return "Сброс всех ТРК";
             case (byte) 0x9A : return "Задание параметров РК";
             case (byte) 0x9B : return "Считать литровый суммарный счетчик";
             case (byte) 0x9E : return "Запрос текущей дозы РК";
             case (byte) 0x9F : return "Запрос состояния РК";
             case (byte) 0xA0 : return "Отчет ЭКЛЗ по отделам в заданном диапазоне дат";
             case (byte) 0xA1 : return "Отчет ЭКЛЗ по отделам в заданном диапазоне номеров смен";
             case (byte) 0xA2 : return "Отчет ЭКЛЗ по закрытиям смен в заданном диапазоне дат";
             case (byte) 0xA3 : return "Отчет ЭКЛЗ по закрытиям смен в заданном диапазоне номеров смен";
             case (byte) 0xA4 : return "Итоги смены по номеру смены ЭКЛЗ";
             case (byte) 0xA5 : return "Платежный документ из ЭКЛЗ по номеру КПК";
             case (byte) 0xA6 : return "Контрольная лента из ЭКЛЗ по номеру смены";
             case (byte) 0xA7 : return "Прерывание полного отчета ЭКЛЗ или контрольной ленты ЭКЛЗ или печати платежного документа ЭКЛЗ";
             case (byte) 0xA8 : return "Итог активизации ЭКЛЗ";
             case (byte) 0xA9 : return "Активизация ЭКЛЗ";
             case (byte) 0xAA : return "Закрытие архива ЭКЛЗ";
             case (byte) 0xAB : return "Запрос регистрационного номера ЭКЛЗ";
             case (byte) 0xAC : return "Прекращение ЭКЛЗ";
             case (byte) 0xAD : return "Запрос состояния по коду 1 ЭКЛЗ";
             case (byte) 0xAE : return "Запрос состояния по коду 2 ЭКЛЗ";
             case (byte) 0xAF : return "Тест целостности архива ЭКЛЗ";
             case (byte) 0xB0 : return "Продолжение печати";
             case (byte) 0xB1 : return "Запрос версии ЭКЛЗ";
             case (byte) 0xB2 : return "Инициализация архива ЭКЛЗ";
             case (byte) 0xB3 : return "Запрос данных отчёта ЭКЛЗ";
             case (byte) 0xB4 : return "Запрос контрольной ленты ЭКЛЗ";
             case (byte) 0xB5 : return "Запрос документа ЭКЛЗ";
             case (byte) 0xB6 : return "Запрос отчёта ЭКЛЗ по отделам в заданном диапазоне дат";
             case (byte) 0xB7 : return "Запрос отчёта ЭКЛЗ по отделам в заданном диапазоне номеров смен";
             case (byte) 0xB8 : return "Запрос отчёта ЭКЛЗ по закрытиям смен в заданном диапазоне дат";
             case (byte) 0xB9 : return "Запрос отчёта ЭКЛЗ по закрытиям смен в заданном диапазоне номеров смен";
             case (byte) 0xBA : return "Запрос в ЭКЛЗ итогов смены по номеру смены";
             case (byte) 0xBB : return "Запрос итога активизации ЭКЛЗ";
             case (byte) 0xBC : return "Вернуть ошибку ЭКЛЗ";
             case (byte) 0xC0 : return "Загрузка графики";
             case (byte) 0xC1 : return "Печать графики";
             case (byte) 0xC2 : return "Печать штрих-кода";
             case (byte) 0xC3 : return "Загрузка расширенной графики";
             case (byte) 0xC4 : return "Печать расширенной графики";
             case (byte) 0xF0 : return "Управление заслонкой";
             case (byte) 0xF1 : return "Выдать чек";
             case (byte) 0xF3 : return "Установить пароль ЦТО";
             case (byte) 0xFC : return "Получить тип устройства";
             case (byte) 0xFD : return "Управление портом дополнительного внешнего устройства";

                      default : return "The send command is not identified.";
         }

    }

    public String ReadErrorMessage(byte bMessage){
                 switch (bMessage){
             case 0x00 : return "0	ФП	Ошибок нет";
             case 0x01 : return "1	ФП 	Неисправен накопитель ФП 1, ФП 2 или часы";
             case 0x02 : return "2	ФП	Отсутствует ФП 1";
             case 0x03 : return "3	ФП	Отсутствует ФП 2";
             case 0x04 : return "4	ФП 	Некорректные параметры в команде  обращения к ФП";
             case 0x05 : return "5	ФП	Нет запрошенных данных";
             case 0x06 : return "6	ФП	ФП в режиме вывода данных";
             case 0x07 : return "7 	ФП	Некорректные параметры в команде для данной реализации ФП";
             case 0x08 : return "8	ФП	Команда не поддерживается в данной реализации ФП";
             case 0x09 : return "9	ФП	Некорректная длина команды";
             case 0x0A : return "10	ФП	Формат данных не BCD";
             case 0x0B : return "11	ФП	Неисправна ячейка памяти ФП при записи итога";
             case 0x11 : return "17	ФП	Не введена лицензия";
             case 0x12 : return "18	ФП	Заводской номер уже введен";
             case 0x13 : return "19	ФП	Текущая дата меньше даты последней записи в ФП";
             case 0x14 : return "20	ФП	Область сменных итогов ФП переполнена";
             case 0x15 : return "21	ФП	Смена уже открыта";
             case 0x16 : return "22	ФП	Смена не открыта";
             case 0x17 : return "23	ФП	Номер первой смены больше номера последней смены";
             case 0x18 : return "24	ФП	Дата первой смены больше даты последней смены";
             case 0x19 : return "25	ФП	Нет данных в ФП";
             case 0x1A : return "26	ФП	Область перерегистраций в ФП переполнена";
             case 0x1B : return "27	ФП	Заводской номер не введен";
             case 0x1C : return "28	ФП	В заданном диапазоне есть поврежденная запись";
             case 0x1D : return "29	ФП	Повреждена последняя запись сменных итогов";
             case 0x1E : return "30	ФП	Область перерегистраций ФП переполнена";
             case 0x1F : return "31	ФП	Отсутствует память регистров";
             case 0x20 : return "32	ФП	Переполнение денежного регистра при добавлении";
             case 0x21 : return "33	ФП	Вычитаемая сумма больше содержимого денежного регистра";
             case 0x22 : return "34	ФП	Неверная дата";
             case 0x23 : return "35	ФП	Нет записи активизации";
             case 0x24 : return "36	ФП	Область активизаций переполнена";
             case 0x25 : return "37	ФП	Нет активизации с запрашиваемым номером";
             case 0x26 : return "38	ФР	Вносимая клиентом сумма меньше суммы чека";
             case 0x2B : return "43	ФР	Невозможно отменить предыдущую команду";
             case 0x2C : return "44	ФР	Обнулённая касса (повторное гашение невозможно)";
             case 0x2D : return "45	ФР	Сумма чека по секции меньше суммы сторно";
             case 0x2E : return "46	ФР	В ФР нет денег для выплаты";
             case 0x30 : return "48	ФР	ФР заблокирован, ждет ввода пароля налогового инспектора";
             case 0x32 : return "50	ФР	Требуется выполнение общего гашения";
             case 0x33 : return "51	ФР	Некорректные параметры в команде";
             case 0x34 : return "52	ФР	Нет данных";
             case 0x35 : return "53	ФР	Некорректный параметр при данных настройках";
             case 0x36 : return "54	ФР	Некорректные параметры в команде для данной реализации ФР";
             case 0x37 : return "55	ФР	Команда не поддерживается в данной реализации ФР";
             case 0x38 : return "56	ФР	Ошибка в ПЗУ";
             case 0x39 : return "57	ФР	Внутренняя ошибка ПО ФР";
             case 0x3A : return "58	ФР	Переполнение накопления по надбавкам в смене";
             case 0x3B : return "59	ФР	Переполнение накопления в смене";
             case 0x3C : return "60	ФР	ЭКЛЗ: неверный регистрационный номер";
             case 0x3D : return "61	ФР	Смена не открыта – операция невозможна";
             case 0x3E : return "62	ФР	Переполнение накопления по секциям в смене";
             case 0x3F : return "63	ФР	Переполнение накопления по скидкам в  смене";
             case 0x40 : return "64	ФР	Переполнение диапазона скидок";
             case 0x41 : return "65	ФР	Переполнение диапазона оплаты наличными";
             case 0x42 : return "66	ФР	Переполнение диапазона оплаты типом 2";
             case 0x43 : return "67	ФР	Переполнение диапазона оплаты типом 3";
             case 0x44 : return "68	ФР	Переполнение диапазона оплаты типом 4";
             case 0x45 : return "69	ФР	Cумма всех типов оплаты меньше итога чека";
             case 0x46 : return "70	ФР	Не хватает наличности в кассе";
             case 0x47 : return "71	ФР	Переполнение накопления по налогам в смене";
             case 0x48 : return "72	ФР	Переполнение итога чека";
             case 0x49 : return "73	ФР	Операция невозможна в открытом чеке данного типа";
             case 0x4A : return "74	ФР	Открыт чек – операция невозможна";
             case 0x4B : return "75	ФР	Буфер чека переполнен";
             case 0x4C : return "76	ФР	Переполнение накопления по обороту налогов в смене";
             case 0x4D : return "77	ФР	Вносимая безналичной оплатой сумма больше суммы чека";
             case 0x4E : return "78	ФР	Смена превысила 24 часа";
             case 0x4F : return "79	ФР	Неверный пароль";
             case 0x50 : return "80	ФР	Идет печать предыдущей команды";
             case 0x51 : return "81	ФР	Переполнение накоплений наличными в смене";
             case 0x52 : return "82	ФР	Переполнение накоплений по типу оплаты 2 в смене";
             case 0x53 : return "83	ФР	Переполнение накоплений по типу оплаты 3 в смене";
             case 0x54 : return "84	ФР	Переполнение накоплений по типу оплаты 4 в смене";
             case 0x55 : return "85	ФР	Чек закрыт – операция невозможна";
             case 0x56 : return "86	ФР	Нет документа для повтора";
             case 0x57 : return "87	ФР	ЭКЛЗ: количество закрытых смен не совпадает с ФП";
             case 0x58 : return "88	ФР	Ожидание команды продолжения печати";
             case 0x59 : return "89	ФР	Документ открыт другим оператором";
             case 0x5A : return "90	ФР	Скидка превышает накопления в чеке";
             case 0x5B : return "91	ФР	Переполнение диапазона надбавок";
             case 0x5C : return "92	ФР	Понижено напряжение 24В";
             case 0x5D : return "93	ФР	Таблица не определена";
             case 0x5E : return "94	ФР	Некорректная операция";
             case 0x5F : return "95	ФР	Отрицательный итог чека";
             case 0x60 : return "96	ФР	Переполнение при умножении";
             case 0x61 : return "97	ФР	Переполнение диапазона цены";
             case 0x62 : return "98	ФР	Переполнение диапазона количества";
             case 0x63 : return "99	ФР	Переполнение диапазона отдела";
             case 0x64 : return "100	ФР	ФП отсутствует";
             case 0x65 : return "101	ФР	Не хватает денег в секции";
             case 0x66 : return "102	ФР	Переполнение денег в секции";
             case 0x67 : return "103	ФР	Ошибка связи с ФП";
             case 0x68 : return "104	ФР	Не хватает денег по обороту налогов";
             case 0x69 : return "105	ФР	Переполнение денег по обороту налогов";
             case 0x6A : return "106	ФР	Ошибка питания в момент ответа по I2C";
             case 0x6B : return "107	ФР	Нет чековой ленты";
             case 0x6C : return "108	ФР	Нет контрольной ленты";
             case 0x6D : return "109	ФР	Не хватает денег по налогу";
             case 0x6E : return "110	ФР	Переполнение денег по налогу";
             case 0x6F : return "111	ФР	Переполнение по выплате в смене";
             case 0x70 : return "112	ФР	Переполнение ФП";
             case 0x71 : return "113	ФР	Ошибка отрезчика";
             case 0x72 : return "114	ФР	Команда не поддерживается в данном подрежиме";
             case 0x73 : return "115	ФР	Команда не поддерживается в данном режиме";
             case 0x74 : return "116	ФР	Ошибка ОЗУ";
             case 0x75 : return "117	ФР	Ошибка питания";
             case 0x76 : return "118	ФР	Ошибка принтера: нет импульсов с тахогенератора";
             case 0x77 : return "119	ФР	Ошибка принтера: нет сигнала с датчиков";
             case 0x78 : return "120	ФР	Замена ПО";
             case 0x79 : return "121	ФР	Замена ФП";
             case 0x7A : return "122	ФР	Поле не редактируется";
             case 0x7B : return "123	ФР	Ошибка оборудования";
             case 0x7C : return "124	ФР	Не совпадает дата";
             case 0x7D : return "125	ФР	Неверный формат даты";
             case 0x7E : return "126	ФР	Неверное значение в поле длины";
             case 0x7F : return "127	ФР	Переполнение диапазона итога чека";
             case (byte) 0x80 : return "128	ФР	Ошибка связи с ФП";
             case (byte) 0x81 : return "129	ФР	Ошибка связи с ФП";
             case (byte) 0x82 : return "130	ФР	Ошибка связи с ФП";
             case (byte) 0x83 : return "131	ФР	Ошибка связи с ФП";
             case (byte) 0x84 : return "132	ФР	Переполнение наличности";
             case (byte) 0x85 : return "133	ФР	Переполнение по продажам в смене";
             case (byte) 0x86 : return "134	ФР	Переполнение по покупкам в смене";
             case (byte) 0x87 : return "135	ФР	Переполнение по возвратам продаж в смене";
             case (byte) 0x88 : return "136	ФР	Переполнение по возвратам покупок в смене";
             case (byte) 0x89 : return "137	ФР	Переполнение по внесению в смене";
             case (byte) 0x8A : return "138	ФР	Переполнение по надбавкам в чеке";
             case (byte) 0x8B : return "139	ФР	Переполнение по скидкам в чеке";
             case (byte) 0x8C : return "140	ФР	Отрицательный итог надбавки в чеке";
             case (byte) 0x8D : return "141	ФР	Отрицательный итог скидки в чеке";
             case (byte) 0x8E : return "142	ФР	Нулевой итог чека";
             case (byte) 0x8F : return "143	ФР	Касса не фискализирована";
             case (byte) 0x90 : return "144	ФР	Поле превышает размер, установленный в настройках";
             case (byte) 0x91 : return "145	ФР	Выход за границу поля печати при данных настройках шрифта";
             case (byte) 0x92 : return "146	ФР	Наложение полей";
             case (byte) 0x93 : return "147	ФР	Восстановление ОЗУ прошло успешно";
             case (byte) 0x94 : return "148	ФР	Исчерпан лимит операций в чеке";
             case (byte) 0xA0 : return "160	ФР	Ошибка связи с ЭКЛЗ";
             case (byte) 0xA1 : return "161	ФР	ЭКЛЗ отсутствует";
             case (byte) 0xA2 : return "162	ЭКЛЗ	ЭКЛЗ: Некорректный формат или параметр команды";
             case (byte) 0xA3 : return "163	ЭКЛЗ	Некорректное состояние ЭКЛЗ";
             case (byte) 0xA4 : return "164	ЭКЛЗ	Авария ЭКЛЗ";
             case (byte) 0xA5 : return "165	ЭКЛЗ	Авария КС в составе ЭКЛЗ";
             case (byte) 0xA6 : return "166	ЭКЛЗ	Исчерпан временной ресурс ЭКЛЗ";
             case (byte) 0xA7 : return "167	ЭКЛЗ	ЭКЛЗ переполнена";
             case (byte) 0xA8 : return "168	ЭКЛЗ	ЭКЛЗ: Неверные дата и время";
             case (byte) 0xA9 : return "169	ЭКЛЗ	ЭКЛЗ: Нет запрошенных данных";
             case (byte) 0xAA : return "170	ЭКЛЗ	Переполнение ЭКЛЗ (отрицательный итог документа)";
             case (byte) 0xB0 : return "176	ФР	ЭКЛЗ: Переполнение в параметре количество";
             case (byte) 0xB1 : return "177	ФР	ЭКЛЗ: Переполнение в параметре сумма";
             case (byte) 0xB2 : return "178	ФР	ЭКЛЗ: Уже активизирована";
             case (byte) 0xC0 : return "192	ФР	Контроль даты и времени (подтвердите дату и время)";
             case (byte) 0xC1 : return "193	ФР	ЭКЛЗ: суточный отчёт с гашением прервать нельзя";
             case (byte) 0xC2 : return "194	ФР	Превышение напряжения в блоке питания";
             case (byte) 0xC3 : return "195	ФР	Несовпадение итогов чека и ЭКЛЗ";
             case (byte) 0xC4 : return "196	ФР	Несовпадение номеров смен";
             case (byte) 0xC5 : return "197	ФР	Буфер подкладного документа пуст";
             case (byte) 0xC6 : return "198	ФР	Подкладной документ отсутствует";
             case (byte) 0xC7 : return "199	ФР	Поле не редактируется в данном режиме";

                      default : return "The error message is not identified.";
         }

    }
    }
