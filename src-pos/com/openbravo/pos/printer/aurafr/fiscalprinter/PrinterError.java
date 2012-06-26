/*
 * PrinterError.java
 *
 * Created on August 28 2007, 10:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


package com.openbravo.pos.printer.aurafr.fiscalprinter;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */

public class PrinterError {
    
    public byte bCode;
    
    public PrinterError() {
    }
    
    public String getTextMessage(byte bCode) {
        switch (bCode) {
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
             case (byte) 0x82 : return "Открыт чек аннулирования - операция невозможна";
             case (byte) 0x84 : return "Переполнение буфера контрольной ленты";
             case (byte) 0x86 : return "Вносимая клиентом сумма меньше суммы чека";
             case (byte) 0x87 : return "Открыт чек возврата - операция невозможна";
             case (byte) 0x88 : return "Смена превысила 24 часа";
             case (byte) 0x89 : return "Открыт чек продажи - операция невозможна";
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
             case (byte) 0x9A : return "Чек закрыт - операция невозможна";
             case (byte) 0x9B : return "Чек открыт - операция невозможна";
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

    public byte getCodeError() {
        return bCode;
    }

    public String getTextError() {
        return getTextMessage(bCode);
    }

    public String getFullTextError() {
        if (bCode >= 0x00) {
            return String.valueOf(bCode) + " " + getTextMessage(bCode);
        } else {
            return String.valueOf(256 +bCode) + " " + getTextMessage(bCode);
        }
    }
}
