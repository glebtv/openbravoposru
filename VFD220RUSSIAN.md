Реализация [поддержки кириллицы](http://ru.wikipedia.org/wiki/%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0) в [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS) для [дисплея](http://ru.wikipedia.org/wiki/%D0%94%D0%B8%D1%81%D0%BF%D0%BB%D0%B5%D0%B9) покупателя [VFD-220](http://www.seetron.com/vfd220_1.htm) по [протоколу CD5220](http://www.totalbarcode.com/download/CD5220_Manual.pdf).

## Преамбула ##

В [Теме 4](http://code.google.com/p/openbravoposru/issues/detail?id=4)  уже обсуждались вопросы реализации протоколов обмена Openbravo POS для различных видов оборудования. Система Openbravo POS использует [протокол ESC/POS](http://www.posprint.ru/pdf/escpos.pdf) для печати на чековых принтерах. Основной проблемой поддержки данного протокола Openbravo POS является отсутствие в программе таблицы символов для кириллицы.

### ESC/POS для чековых принтеров ###

[Протокол ESC/POS](http://www.posprint.ru/pdf/escpos.pdf) работает с чековыми принтерами и дисплеями покупателя по схожим принципам, отправляя коды символа через [последовательный интерфейс](http://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%B8%D0%BD%D1%82%D0%B5%D1%80%D1%84%D0%B5%D0%B9%D1%81). Коды символов заложены в [ПЗУ](http://ru.wikipedia.org/wiki/%D0%9F%D0%97%D0%A3) устройства, то есть для работы с ними требуется произвести конвертацию из кодировки [UTF8](http://ru.wikipedia.org/wiki/UTF8), с которой работает Openbravo POS, в кодировку заложенную в [EEPROM](http://ru.wikipedia.org/wiki/%D0%9F%D0%97%D0%A3) памяти устройства. В [Теме 4](http://code.google.com/p/openbravoposru/issues/detail?id=4) обсуждалась поддержка символов кириллицы чековым принтером [Samsung STP-103](http://www.samsungminiprinters.com/_eng/products/product_form.asp?code=0101&uid=6). Коды таблицы символов кириллицы для данного принтера соответствуют кодам таблицы символов [Windows-1251](http://ru.wikipedia.org/wiki/Windows_1251). Решение данной задачи было реализовано в виде добавления списка условий соответствия символов
[UTF8](http://ru.wikipedia.org/wiki/UTF8) символам [Windows-1251](http://ru.wikipedia.org/wiki/Windows_1251). Реализацию данной функции предложено сделать в виде добавления к исходному коду Openbravo POS проверки данного списка на соответствие в методе `UnicodeTranslatorRus()`. По похожей схеме производится реализация поддержки дисплея покупателя работающего по близкому к ESC/POS [протоколу CD5220](http://www.totalbarcode.com/download/CD5220_Manual.pdf).

## Постановка задачи ##
[Протокол CD5220](http://www.totalbarcode.com/download/CD5220_Manual.pdf) является вариацией протокола ESC/POS и используется для работы дисплеев покупателя реализованных на элементной базе [Serially Interfaced 2x20 Vacuum Fluorescent Display (VFD)](http://www.seetron.com/vfdmnl/mnl.htm). Данные дисплеи выпускаются, как отдельными устройствами, так и монтируются в единые [POS-системы](http://ru.wikipedia.org/wiki/POS-%D1%81%D0%B8%D1%81%D1%82%D0%B5%D0%BC%D0%B0). Обмен данного устройства с компьютером производится по [последовательному порту](http://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D1%80%D1%82). В данной статье рассматривается работа с дисплеем покупателя интегрированным в систему [CiTAQ EPOS-2000I](http://www.citaqpos.com/epos_show.asp?id=8). Без доработки дисплей с системой Openbravo POS работает, но вместо символов кириллицы выводит знаки `???`. Так как дисплей уже частично поддерживает ESC/POS, в программу требуется внести только изменения для реализации функций поддержки кириллицы для русского языка. В дисплеях данной марки в таблице символов также запрограммированы буквы [казахского алфавита](http://ru.wikipedia.org/wiki/%D0%9A%D0%B0%D0%B7%D0%B0%D1%85%D1%81%D0%BA%D0%B8%D0%B9_%D0%B0%D0%BB%D1%84%D0%B0%D0%B2%D0%B8%D1%82), по этому одновременно может быть сделана поддержка [казахского языка](http://ru.wikipedia.org/wiki/%D0%9A%D0%B0%D0%B7%D0%B0%D1%85%D1%81%D0%BA%D0%B8%D0%B9_%D1%8F%D0%B7%D1%8B%D0%BA).

## Решение задачи ##

### Необходимые ресурсы ###
Для разработки вам понадобится:
  * установленная среда разработки [Netbeans IDE 6.5](http://www.netbeans.org/);
  * [исходный код Openbravo POS 2.20](http://downloads.sourceforge.net/openbravopos/openbravopos_2.20_src.zip?modtime=1219951842&big_mirror=0);
  * [спецификация на протокол обмена](http://www.totalbarcode.com/download/CD5220_Manual.pdf);
  * дисплей VFD-220 подключённый к компьютеру.

### Изменения исходного кода ###

Добавляем в меню настройки конфигурации новый вид дисплеев покупателя. Вносим наименование нового устройства `cd5220rus` в `JPanelConfigGeneral.java`:
```
...
package com.openbravo.pos.config;
...
        jcboMachineDisplay.addItem("ld200");

        jcboMachineDisplay.addItem("cd5220rus"); // элемент списка поддерживаемых дисплеев покупателя

        jcboMachineDisplay.addItem("surepos");

...
         String sMachineDisplay = comboValue(jcboMachineDisplay.getSelectedItem());

        if ("epson".equals(sMachineDisplay) || "ld200".equals(sMachineDisplay) || "cd5220rus".equals(sMachineDisplay) || "surepos".equals(sMachineDisplay)) {

            config.setProperty("machine.display", sMachineDisplay + ":" + comboValue(jcboConnDisplay.getSelectedItem()) + "," + comboValue(jcboSerialDisplay.getSelectedItem()));

 ...
        CardLayout cl = (CardLayout)(m_jDisplayParams.getLayout());

        if ("epson".equals(jcboMachineDisplay.getSelectedItem()) || "ld200".equals(jcboMachineDisplay.getSelectedItem()) || "cd5220rus".equals(jcboMachineDisplay.getSelectedItem()) || "surepos".equals(jcboMachineDisplay.getSelectedItem())) {

            cl.show(m_jDisplayParams, "comm");

...
```
Добавляем проверку на тип подключённого дисплея и запуск метода `UnicodeTranslatorRus()`. Вносим наименование нового устройства `cd5220rus` в `DeviceTicket.java` и добавляем вызов метода `UnicodeTranslatorRus()`:
```
...
package com.openbravo.pos.printer; 
...
            } else if ("ld200".equals(sDisplayType)) {
                m_devicedisplay = new DeviceDisplayESCPOS(pws.getPrinterWritter(sDisplayParam1, sDisplayParam2), new UnicodeTranslatorEur());
            } else if ("cd5220rus".equals(sDisplayType)) {
                m_devicedisplay = new DeviceDisplayESCPOS(pws.getPrinterWritter(sDisplayParam1, sDisplayParam2), new UnicodeTranslatorRus());
            } else if ("javapos".equals(sDisplayType)) {
                m_devicedisplay = new DeviceDisplayJavaPOS(sDisplayParam1);
...
```
Создаём метод `UnicodeTranslatorRus()`, где производим инициализацию таблицы символов и подстановку кодов UTF8. Коды таблицы символов запрограммированные в дисплеи:
  * с `0x20` по `0x7F`, соответствуют кодам UTF8 для знаков припинания, цифр, прописных и строчных букв английского алфавита;
  * с `0x80` по `0x9F`, соответствуют заглавным буквам русского алфавита;
  * с `0xA0` по `0xAF` и с `0xE0` по `0xEF`, соответствуют прописным буквам русского алфавита;
  * с `0xF0` по `0xFE`, соответствуют буквам казахского алфавита, начертание которых не встречается в английском и русском алфавите.
```
package com.openbravo.pos.printer.escpos;



public class UnicodeTranslatorRus extends UnicodeTranslator {


    public UnicodeTranslatorRus() {

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
```
Добавляем константу инициализации таблицы символов `CODE_TABLE_RUS` в метод `ESCPOS.java`.
```
...
package com.openbravo.pos.printer.escpos;
...
    public static final byte[] CODE_TABLE_00 = {0x1B, 0x74, 0x00};

    public static final byte[] CODE_TABLE_13 = {0x1B, 0x74, 0x13};

    public static final byte[] CODE_TABLE_RUS = {0x1B, 0x63, 0x52};
...
```

## Результат ##

### Характеристика тестовой системы ###
  * POS-компьютер CiTAQ EPOS-2000I;
  * ОС Windows 2000;
  * исходный код Openbravo POS версия 2.20;
  * дисплей VFD-220, подключён к порту COM2;
  * параметры подключения `BaudRate: 9600,N,8,1`;
  * установленный режим дисплея `CommandType: CD5220/2`.

### Проверка работы ###
  * Подключить дисплей покупателя к компьютеру.
  * Компилировать исходный код программы.
  * Исправить ошибки, повторить компиляцию.
  * Запустить исходный код.
  * Зайти в _Управление -> Настройки -> Ресурсы_ в шаблоне изменить вторую строку на «Добро пожаловать!» для русского язык, на «Қош келдiңiздер!» для казахского.
  * Сохранить шаблон приветствия.
  * Перейти в _Система -> Оборудование -> Настройки_, для _Дисплей клиента_ из списка выбрать `cd5220rus` и указать порт подключения.
  * Сохранить параметры POS терминала.
  * Перезапустить программу, на экране должно появиться приветствие на русском или казахском языке.

### Примеры работы ###
Поддержка дисплеем покупателя русского и казахского алфавитов.

| ![http://farm4.static.flickr.com/3379/3271493254_1aa3feef5a_m.jpg](http://farm4.static.flickr.com/3379/3271493254_1aa3feef5a_m.jpg) | ![http://farm4.static.flickr.com/3425/3271493262_056de3d6e1_m.jpg](http://farm4.static.flickr.com/3425/3271493262_056de3d6e1_m.jpg) |
|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|
| русский                                                                                                                             | казахский                                                                                                                           |

Работа Openbravo POS с шаблонами вывода на дисплей покупателя.
| ![http://farm4.static.flickr.com/3416/3271493264_65b37c0657_m.jpg](http://farm4.static.flickr.com/3416/3271493264_65b37c0657_m.jpg) | ![http://farm4.static.flickr.com/3434/3271493268_f628163d37_m.jpg](http://farm4.static.flickr.com/3434/3271493268_f628163d37_m.jpg) | ![http://farm4.static.flickr.com/3369/3271493272_39ab5a392d_m.jpg](http://farm4.static.flickr.com/3369/3271493272_39ab5a392d_m.jpg) |
|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|
| Welcome!                                                                                                                            | Добро пожаловать!                                                                                                                   | Қош келдiңiздер!                                                                                                                    |


## Исходный код ##

[Изменённые файлы исходного кода для Openbravo POS 2.20.](http://openbravoposru.googlecode.com/files/OpenbravoPOS-VFD220RUSSIAN.zip) Для установки файлы следует скопировать в каталоги с исходным кодом Openbravo POS 2.20:

  * `JPanelConfigGeneral.java` в `../com/openbravo/pos/config/`
  * `DeviceTicket.java` в `../com/openbravo/pos/printer/`
  * `UnicodeTranslatorRus.java` в `../com/openbravo/pos/printer/escpos/`
  * `ESCPOS.java` в `../com/openbravo/pos/printer/escpos/`

Также исходный код включён в [репозитарий данного проекта с ревизии 106](http://code.google.com/p/openbravoposru/source/list).
