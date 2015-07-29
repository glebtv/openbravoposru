Данный статья является практическим примером работы с механизмом скриптов интегрированного в [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS). Исходные коды основан [POS - Сheck stock of the product](http://wiki.openbravo.com/wiki/POS_-_%D0%A1heck_stock_of_the_product) на публикации из [Openbravo Wiki](http://wiki.openbravo.com). Для общего понимания работы скриптов в [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS) желательно предварительно ознакомится с [Openbravo POS Scripting Tutorial](http://wiki.openbravo.com/wiki/Openbravo_POS_Scripting_Tutorial).

# Вступление #

Одной из основных задач учёта при работе с товаром является контроль остатков в местах хранения (складе, торговом зале, на стеллажах). Для целей контроля в системе POS необходим гибкий механизм отслеживания наличия товаров, при этом в системе может быть запрещена или разрешена продажа количества более чем имеется на регистрах учёта базы данных. В Openbravo POS данный механизм не реализован и в данном примере будет представлено решения по внедрению его в систему.

# Общие принципы #

Наиболее удобным способом внедрения системы отслеживания остатков в системе Openbravo POS является создание специального скрипта, выполняемого стандартными средствами программы. Выполнятся данный скрипт будет выполнятся в окне **Продажи** и позволит кассиру отслеживать какой товар и в каком количестве имеется на складе. Информация о том какой товар и в каком количестве необходимо продать доступна через предустановленные переменные механизма скриптов, а вот информация об остатков необходимо получить из рабочей базы данных. По этому процесс добавления требуемой функциональности будет состоять из этапов:
  1. Получение данных об остатках из базы данных.
  1. Расчёт текущего и необходимого количества товара.
  1. Внедрение механизма доступа пользователя к полученным данным.

# Механизм скриптов #
Скрипт в Openbravo POS это написанная на Java подпрограмма, исходный код которой хранится в базе данных Openbravo POS. Исполнен скрипт может быть только из окна **Продажи**. Преимуществами использования скриптов являются:
  * простота написания кода;
  * нет необходимости компиляции всего исходного кода программы;
  * доступ ко всем объектам, методам и свойствам Openbravo POS, а также к любым средствам Java API;
  * скрипт автоматически доступен на всех терминалах подключённых рабочей базе данных;
  * назначения прав исполнения через систему профилей пользователей.
Для получения более полного представления о возможностях использования скриптов, в данной статье предлагается рассмотреть два варианта применения:
  * просмотр остатков на складе по выбранной товарной позиции;
  * запрет на продажу товара при недостатке остатков на складе.

## Изменение исходного кода ##

Для доступа к базе данных из скрипта используется код следующего вида:

```
Session session = new Session(dbURL, dbUser, dbPassword);
DataLogicSales logicsale = new DataLogicSales();
logicsale.init(session);
DataLogicSystem logicsystem = new DataLogicSystem();
logicsystem.init(session);
Properties p = logicsystem.getResourceAsProperties(hostname + "/properties");
String loc = p.getProperty("location");
```

Где `dbURL`, `dbUser`, `dbPassword`, это параметры доступа к текущей базе данных, а `hostname`, это имя данного POS-терминала. Эти параметры доступны в **Система -> Оборудование** раздел **База данных**, и могут быть перенесены в скрипт в виде `"параметр"`. Для автоматического получения параметров базы данных предлагается дополнить исходный код программы в части предопределённых переменных скрипта. Для этого требуется изменить метод `evalScript` класса `ScriptObject` входящего в `JPanelTicket`.

```
        public Object evalScript(String code, ScriptArg... args) throws ScriptException {

            ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);

            // Получить имя и пароль к базе данных
            String sDBUser = m_App.getProperties().getProperty("db.user");
            String sDBPassword = m_App.getProperties().getProperty("db.password");
            if (sDBUser != null && sDBPassword != null && sDBPassword.startsWith("crypt:")) {
                AltEncrypter cypher = new AltEncrypter("cypherkey" + sDBUser);
                sDBPassword = cypher.decrypt(sDBPassword.substring(6));
            }

            // Создать переменные для подключения к базе данных и получения настроек
            script.put("hostname", m_App.getProperties().getProperty("machine.hostname"));
            script.put("dbURL", m_App.getProperties().getProperty("db.URL"));
            script.put("dbUser", sDBUser);
            script.put("dbPassword", sDBPassword);

            script.put("ticket", ticket);
            script.put("place", ticketext);
            script.put("taxes", taxcollection);
            script.put("taxeslogic", taxeslogic);
            script.put("user", m_App.getAppUserView().getUser());
            script.put("sales", this);

            // more arguments
            for (ScriptArg arg : args) {
                script.put(arg.getKey(), arg.getValue());
            }

            return script.eval(code);
        }
    }
```

Данный код включён в данный проект с [ревизии 173](http://code.google.com/p/openbravoposru/source/detail?r=173) и [предложен к включению](https://issues.openbravo.com/view.php?id=6743) в основной код Openbravo POS, так как внедрение данных переменных позволит расширить применения скриптов.

## Проверка остатков ##

Для получения информации об остатках используется диалоговое окно сообщения класса `JOptionPane` метод `showMessageDialog`. Информация выводимая в данном окне содержит:
  1. Наименование товара из позиции выбранной в табличной части окна **Продажи**.
  1. Номер склада, где этот товар находится.
  1. Количество товара находящееся на данном складе.

### Создание скрипта ###

Для создания скрипта следует перейти в **Администрация -> Настройки -> Ресурсы** и создать новый текстовый ресурс с названием **Script.StockCheck** содержащий следующий код:

```
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.data.loader.Session;
import java.util.Properties;

    //Подключение к базе данных
    Session session = new Session(dbURL, dbUser, dbPassword);
    DataLogicSales logicsale = new DataLogicSales();
    logicsale.init(session);
    DataLogicSystem logicsystem = new DataLogicSystem();
    logicsystem.init(session);

    //Подключение к ресурсу с параметрами POS терминала
    Properties p = logicsystem.getResourceAsProperties(hostname + "/properties");

    //Получение номера склада установленного текущим для данного POS терминала
    String loc = p.getProperty("location");

    //Получение порядкового номера позиции выбранной в чеки
    index = sales.getSelectedIndex();

    //Получение данных по строке чека согласнно порядкого номера позиции
    row = ticket.getLine(index);

    //Получение кода товара в выбранной позиции
    product = row.getProductID();

    //Получение значения характеристики для товара в текущей строке
    att = row.getProductAttSetInstId();

    //Получение количества выбранного товара с заданными характеристиками на текущем складе в базе данных Openbravo POS
    units = logicsale.findProductStock(loc,product,att);

    //Вывод диалогового окна с сообщениием об остатке по выбраному товару на текущем складе
    JOptionPane.showMessageDialog(null, "По " + row.getProductName() + "(" + row.getProductAttSetInstDesc() + ")" + " остаток = " + units +" на складе № " + loc, "Остатки", JOptionPane.PLAIN_MESSAGE);
```

### Добавление кнопки ###

Для организации диалога пользователя и запуска скрипта необходимо создать кнопку в верхней части окна **Продажи**. Для этого дополнить ресурс `Ticket.Buttons` строкой:

```
    <button key="button.infostock" name="button.infostock" image="icon.stock" code="Script.StockCheck"/>
```
  * `key`, это уникальный идентификатор кнопки, используемый в профилях для задания прав доступа.
  * `name`, это ключ для локализации надписи на кнопке.
  * `image`, это наименование ресурса с иконкой отображаемой на кнопке.
  * `code`, это атрибут определяющий наименование скрипта запускаемого при нажатии на кнопку.

### Назначение профиля ###

Разрешение пользователям использовать кнопку задаётся в правах доступа  **Администрация -> Настройки -> Профили**. Для этого в конце необходимого профиля добавить строку:

```
    <class name="button.infostock"/>
```

Если право доступа для пользователя не задано, кнопка на экране будет заблокирована.

### Добавление иконки ###

Для загрузки иконки необходимо создать ресурс типа изображение с именем _icon.stock_ и загрузить графический файл. Размер иконки должен составлять 16x16 пикселей. Если иконка задана не будет, то её заменит иконка по-умолчанию в виде зелёного шара.

### Локализация ###

Для локализации подписи на кнопке необходимо внести изменения в файлы локаллизаций `/locales/pos_messages*.properties`, для английской:
```
button.infostock=Stock
```
И для русской:
```
button.infostock=Остаток
```

## Отслеживание остатков ##

Для отслеживания остатков и запрещения продаж в случае если товаров на текущем складе недостаёт, будет использован механизм запуска скриптов по событию.

### Создание скрипта для события ticket.addline ###

Первый вариант запуска скрипта будет связан с действием пользователя по добавлению новой позиции товара в список чека окна **Продажи**. Когда пользователь добавляет новую позиции по товару в чек, скрипт проверяет есть такое количество на текущем складе пользователя. При этом в алгоритм скрипта внедрён механизм учёта количества товара уже занесённого в чек. Этот скрипт будет срабатывать если для данного товара не была установлена группа характеристик.

```
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.data.loader.Session;
import java.util.Properties;

    //Проверка есть у товара характеристики
    if (line.getProductAttSetId() == null) {

    //Подключение к базе данных
    Session session = new Session(dbURL, dbUser, dbPassword);
    DataLogicSales logicsale = new DataLogicSales();
    logicsale.init(session);
    DataLogicSystem logicsystem = new DataLogicSystem();
    logicsystem.init(session);

    //Подключение к ресурсу с параметрами POS терминала
    Properties p = logicsystem.getResourceAsProperties(hostname + "/properties");

    //Получение номера склада установленного текущим для данного POS терминала
    String loc = p.getProperty("location");

    //Получение кода товара из выбранной строки чека
    product = line.getProductID();

    //Получение количества выбранного товара на текущем складе в базе данных Openbravo POS
    units = logicsale.findProductStock(loc,product,null);

    //Подсчёт количества товара данного наименования уже помещённого в чек
    multiply = 0;

    for (int i= 0; i < ticket.getLinesCount(); i++) {
         row = ticket.getLine(i);
         if (row.getProductID() == product) {
             multiply = multiply + row.getMultiply();
         }
    }

    //Расчёт разницы между товаром имеющемся на складе и товаром запрашиваемым в данном чеки
    diff = units - line.getMultiply() - multiply;

    //Если полученное значение разницы отрицательно, значит товара на складе недостаточно и добавление выбранного товара в чек невозможно.
    //Если значение разницы осталось положительным, то товара на складе достаточно и он может быть добавлен в чек.
    if (diff < 0.0) {
        javax.swing.JOptionPane.showMessageDialog(null, "Недостаточное количество " + line.getProductName() + " на складе № " + loc + ".", "Остаток", JOptionPane.WARNING_MESSAGE);
        return "Cancel";
    } else {
        return null;
    }
  }
```

Изменить склад для текущего POS терминала можно в ресурсе `hostname/properties`, задав номер склада внутри тега `<entry key="location"> </entry>`.

### Создание скрипта для события ticket.setline ###

Второй вариант запуска скрипта будет связан с действием пользователя по изменению уже введённой позиции внутри чека. Данный вариант отличается от первоначального механизмом исключения из учёта количества товара в изменяемой позиции. Он также будет срабатывать при задании характеристик товара.

```
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.data.loader.Session;
import java.util.Properties;

    //Подключение к базе данных
    Session session = new Session(dbURL, dbUser, dbPassword);
    DataLogicSales logicsale = new DataLogicSales();
    logicsale.init(session);
    DataLogicSystem logicsystem = new DataLogicSystem();
    logicsystem.init(session);

    //Подключение к ресурсу с параметрами POS терминала
    Properties p = logicsystem.getResourceAsProperties(hostname + "/properties");

    //Получение номера склада установленного текущим для данного POS терминала
    String loc = p.getProperty("location");

    //Получение кода товара из выбранной строки чека
    product = line.getProductID();

    //Получить значение характеристики для товара в текущей строке
    att = line.getProductAttSetInstId();

    //Получение количества выбранного товара на текущем складе в базе данных Openbravo POS
    units = logicsale.findProductStock(loc,product,att);

    //Получение порядкового номера позиции выбранной в чеки
    index = sales.getSelectedIndex();

    multiply = 0;

    //Получение отрицательного количества в изменяемой позиции
    if (index != -1) {
       multiply = multiply - line.getMultiply();
    }

    //Подсчёт количества товара данного наименования и характеристик уже помещённого в чек
    for (int i= 0; i < ticket.getLinesCount(); i++) {
         row = ticket.getLine(i);
         if (row.getProductAttSetInstId() != null) {
            if (row.getProductID().equals(product) && row.getProductAttSetInstId().equals(att)) {
               multiply = multiply + row.getMultiply();
            }
         } else {
            if (row.getProductID().equals(product) && row.getProductAttSetInstId() == null) {
               multiply = multiply + row.getMultiply();
            }
         }
    }

    //Расчёт разницы между товарам имеющемся на складе и товаром запрашиваемым в данном чеки
    diff = units -  line.getMultiply() - multiply;

    //Если полученное значение разницы отрицательно, значит товаром на складе недостаточно и добавление выбранного товара в чек невозможно.
    //Если значение разницы осталось положительным, то товара на складе достаточно и он может быть добавлен в чек.
    if (diff < 0.0) {
        javax.swing.JOptionPane.showMessageDialog(null, "Недостаточное количество " + line.getProductName() + "(" + line.getProductAttSetInstDesc() + ")" + " на складе № " + loc + ".", "Остаток", JOptionPane.WARNING_MESSAGE);
        return "Cancel";
    } else {
        return null;
    }
```

### Добавление события ###

Для того, чтобы события были доступны к исполнению в окне "Платежи", необходимо дополнить ресурс `Ticket.Buttons` двумя строками:
```
    <event key="ticket.addline" code="Script.StockCurrentAdd"/>
    <event key="ticket.setline" code="Script.StockCurrentSet"/>
```
  * `key`, ключ события после которого должен быть исполнен скрипт.
  * `code`, имя ресурса с кодом скрипта для исполнения.

# Интеграция в систему #

Для того, чтобы созданные скрипты были доступны в системе сразу после установки, необходимо следующие файлы разместить в `/src-pos/com/openbravo/pos/templates` исходного кода:
  * `Script.StockCheck.txt`, содержит скрипт для проверки остатков;
  * `Script.StockCurrentAdd.txt`, содержит скрипт для контроля остатков;
  * `Script.StockCurrentSet.txt`, содержит скрипт для контроля остатков;
  * `applications-other.png`, изображение иконки и пакета [Gnome 2.18 icon theme](http://art.gnome.org/themes/icon).

Добавить в файл `/src-pos/com/openbravo/pos/templates/Ticket.Buttons.xml` строки:

```
...
<configuration>
...
    <button key="button.infostock" image="icon.stock" name="button.infostock" code="Script.StockCheck"/>
...
    <event key="ticket.addline" code="Script.StockCurrentAdd"/>
    <event key="ticket.setline" code="Script.StockCurrentSet"/>
...
</configuration>
```

Добавить в файлы шаблонов профилей привилегий `/src-pos/com/openbravo/pos/templates/Role.*.xml` строку:

```
...
<permissions>
...
    <class name="button.infostock"/>
</permissions>
```

Внести в SQL-скрипты создания первоначальной базы данных `/src-pos/com/openbravo/pos/scripts/*-create` (для каждого вида сервера) следующие запросы:

```
...
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('90001', 'Script.StockCheck', 0, $FILE{/com/openbravo/pos/templates/Script.StockCheck.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('90002', 'Script.StockCurrentAdd', 0, $FILE{/com/openbravo/pos/templates/Script.StockCurrentAdd.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('90003', 'Script.StockCurrentSet', 0, $FILE{/com/openbravo/pos/templates/Script.StockCurrentSet.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('90004', 'icon.stock', 1, $FILE{/com/openbravo/pos/templates/applications-other.png});
...
```

Эти запросы создадут записи с ресурсами из файлов шаблонов в первоначальной базе данных.

# Проверка работы #

Все приведённые выше изменения исходного кода размещены в данном проекте с ревизии [r215](https://code.google.com/p/openbravoposru/source/detail?r=215). Для проверки работы описанного кода необходимы загрузить исходный код из репозитария и произвести сборку средствами компилятора.

| ![![](http://farm4.static.flickr.com/3459/3974227349_cb536afaf4_m.jpg)](http://farm4.static.flickr.com/3459/3974227349_09e4a2c744_o.png)| ![![](http://farm4.static.flickr.com/3526/3974227751_2a9d3eb6a9_m.jpg)](http://farm4.static.flickr.com/3526/3974227751_1caefa123b_o.png) | ![![](http://farm3.static.flickr.com/2463/3974992874_58d8e01cc2_m.jpg)](http://farm3.static.flickr.com/2463/3974992874_34eee61fd9_o.png) |
|:----------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------|
| Информация об остатках                                                                                                                  | Контроль остатков                                                                                                                        | Изменение позиции                                                                                                                        |

  1. Перед запуском в свойствах Openbravo POS следует изменить наименование базы данных для создания необходимых ресурсов сразу после установки.
  1. После запуска заполнить справочник **Товары** и ввести **Остатки на складе**.
  1. Перейти в окно **Платежи** и выбрать товар из каталога.
  1. Выделив позицию товара в табличной части, нажать кнопку **Остатки**, сравнить выведенное значение с фактическим.
  1. Вводить различные позиции товаров из каталога до появления сообщения, проверить результат.
  1. Перейти на любую позицию в табличной части и нажать кнопку с лупой.
  1. Ввести любое количество в соответствующее поле и нажать **Принять**, проверить результат.

# Заключение #

Приведённые примеры является лишь частью того потенциала использования скриптов, который позволяет настроить Openbravo POS под различные задачи учёта. Подробнее познакомиться с разработкой скриптов можно на следующих страницах:
  * [Openbravo POS Scripting Tutorial](http://wiki.openbravo.com/wiki/Openbravo_POS_Scripting_Tutorial) - подробная инструкция от разработчиков Openbravo POS.
  * [Code Snippets Openbravo POS](http://wiki.openbravo.com/wiki/Category:Code_Snippets_POS) - примеры использования скриптов от сообщества Openbravo POS.