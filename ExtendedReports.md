# Расширенный отчёт по продажам #

![![](http://farm3.static.flickr.com/2747/4489123770_ea6c19c651_m.jpg)](http://farm3.static.flickr.com/2747/4489123770_78c8c49b96_o.png)

## Описание ##

Отражает какой клиента, сколько товаров приобрёл.

Группировка:
  * по клиенту;
  * по категории товара;
  * по наименованию товара.

Столбцы для клиентов:
  * код налогоплательщика,
  * название клиента.

Столбцы для категорий:
  * наименование категории.

Столбцы для товара:
  * код товара,
  * название товара,
  * цена продажи,
  * реализованное количество,
  * реализованная сумма.

Фильтры для выборки:
  * по дате `com.openbravo.pos.reports.JParamsDatesInterval()`,
  * по штрих-коду и по шаблону `com.openbravo.pos.ticket.ProductFilter()`,
  * по клиенту `com.openbravo.pos.reports.JParamsCustomer()`.

Файлы:
  * `extproducts.bs`
  * `extproducts.jrxml`
  * `extproducts_messages.properties`
  * `extproducts_messages_ru.properties`

Обсуждения:
  * [Issue 37](https://code.google.com/p/openbravoposru/issues/detail?id=37);
  * [r338](https://code.google.com/p/openbravoposru/source/detail?r=338).

## SQL запрос ##

```
SELECT
 CUSTOMERS.TAXID,
 CUSTOMERS.NAME AS CUSTOMER,
 CATEGORIES.NAME AS CATEGORY,
 PRODUCTS.REFERENCE,
 PRODUCTS.NAME AS PRODUCT,
 SUM(TICKETLINES.UNITS) AS UNIT,
 SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) AS TOTAL,
 SUM(TICKETLINES.UNITS * TICKETLINES.PRICE) / SUM(TICKETLINES.UNITS) AS MEANPRICE
FROM
 TICKETS LEFT OUTER JOIN CUSTOMERS ON TICKETS.CUSTOMER = CUSTOMERS.ID,
 TICKETLINES LEFT OUTER JOIN PRODUCTS ON TICKETLINES.PRODUCT = PRODUCTS.ID LEFT OUTER JOIN CATEGORIES ON PRODUCTS.CATEGORY = CATEGORIES.ID,
 RECEIPTS
WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET
GROUP BY CUSTOMERS.ID, CATEGORIES.ID, PRODUCTS.ID
ORDER BY CUSTOMERS.NAME, CATEGORIES.NAME, PRODUCTS.NAME
```

# Диаграмма продаж по категориям товаров #

![![](http://farm3.static.flickr.com/2786/4488475633_07c4c43132_m.jpg)](http://farm3.static.flickr.com/2786/4488475633_8c02a2a1e4_o.png)

## Описание ##

Отражает в натуральном и стоимостном выражении сколько товаров из каждой категории было реализовано.

Группировка:
  * по категории товара.

Тип диаграммы:
  * круговая.

Фильтры для выборки:
  * по дате `com.openbravo.pos.reports.JParamsDatesInterval()`.

Файлы:
  * `piesalescat.bs`;
  * `piesalescat.jrxml`;
  * `piesalescat_messages.properties`;
  * `piesalescat_messages_ru.properties`.

Обсуждения:
  * [Issue 43](https://code.google.com/p/openbravoposru/issues/detail?id=43);
  * [r335](https://code.google.com/p/openbravoposru/source/detail?r=335).

## SQL запрос ##

```
SELECT
 CATEGORIES.NAME AS CAT,
 SUM(TICKETLINES.UNITS) AS UNITS,
 SUM(TICKETLINES.PRICE * TICKETLINES.UNITS) AS TOTAL
FROM TICKETLINES
 LEFT OUTER JOIN PRODUCTS ON TICKETLINES.PRODUCT = PRODUCTS.ID
 LEFT OUTER JOIN CATEGORIES ON PRODUCTS.CATEGORY = CATEGORIES.ID
 LEFT OUTER JOIN TICKETS ON TICKETLINES.TICKET = TICKETS.ID
 LEFT OUTER JOIN RECEIPTS ON TICKETS.ID = RECEIPTS.ID
GROUP BY CATEGORIES.NAME
ORDER BY CATEGORIES.NAME ASC
```

# Каталог распродажи #

![![](http://farm3.static.flickr.com/2092/4507485891_349802b91b_m.jpg)](http://farm3.static.flickr.com/2092/4507485891_59e5a42dbe_o.png)

## Описание ##

Выводит изображения предлагаемых товаров с указанием цены.

Группировка:
  * по категории товара;
  * по названию.

Фильтры для выборки:
  * по штрих-коду и по шаблону `com.openbravo.pos.ticket.ProductFilter()`.

Файлы:
  * `salecatalog.bs`;
  * `salecatalog.jrxml`;
  * `salecatalog_messages.properties`;
  * `salecatalog_messages_ru.properties`.

Обсуждения:
  * [Issue 47](https://code.google.com/p/openbravoposru/issues/detail?id=47);
  * [r339](https://code.google.com/p/openbravoposru/source/detail?r=339).

## SQL запрос ##

```
SELECT
  PRODUCTS.ID,
  PRODUCTS.REFERENCE,
  PRODUCTS.CODE,
  PRODUCTS.NAME,
  PRODUCTS.PRICESELL,
  PRODUCTS.IMAGE,
  TC.ID AS TAXCAT,
  CATEGORIES.NAME AS CATEGORYNAME
FROM
  PRODUCTS LEFT OUTER JOIN CATEGORIES ON
  PRODUCTS.CATEGORY = CATEGORIES.ID LEFT OUTER JOIN TAXCATEGORIES TC ON
  PRODUCTS.TAXCAT = TC.ID,
  PRODUCTS_CAT
WHERE PRODUCTS.ID = PRODUCTS_CAT.PRODUCT
ORDER BY CATEGORIES.NAME, PRODUCTS.NAME
```

# Графическая информация о реализации товаров #

![![](http://farm4.static.flickr.com/3259/4558540106_94af34048e_m.jpg)](http://farm4.static.flickr.com/3259/4558540106_098492e881_o.png)

## Описание ##

Вывести максимально полную информацию о товаре с графическим представлением ежедневной реализации.

Группировка:
  * по названию товара;
  * по ежедневному объёму реализации товара;
  * по имени продавца.

Фильтры для выборки:
  * по штрих-коду и по шаблону `com.openbravo.pos.ticket.ProductFilter()`.

Файлы:
  * `timeseriesproduct.bs`;
  * `timeseriesproduct.jrxml`;
  * `timeseriesproduct_messages.properties`;
  * `timeseriesproduct_messages_ru.properties`.

Обсуждения:
  * [Issue 56](https://code.google.com/p/openbravoposru/issues/detail?id=56);
  * [r345](https://code.google.com/p/openbravoposru/source/detail?r=345).

## SQL запрос ##

```
SELECT
 CATEGORIES.NAME AS CATNAME,
 PRODUCTS.NAME AS PRODUCT,
 PRODUCTS.IMAGE AS PRODIMG,
 PRODUCTS.REFERENCE AS REF,
 PRODUCTS.CODE AS BARCODE,
 PRODUCTS.PRICEBUY,
 PRODUCTS.PRICESELL,
 PRODUCTS.STOCKCOST,
 PRODUCTS.STOCKVOLUME,
 TC.ID AS TAXCAT,
 TC.NAME AS TAXCATNAME,
 PEOPLE.NAME AS SALER,
 RECEIPTS.DATENEW AS RECEIPTDATE,
 SUM(TICKETLINES.UNITS) AS TOTALUNIT
FROM
 RECEIPTS LEFT OUTER JOIN TICKETS ON
 RECEIPTS.ID = TICKETS.ID LEFT OUTER JOIN TICKETLINES ON
 TICKETS.ID = TICKETLINES.TICKET,
 PRODUCTS LEFT OUTER JOIN CATEGORIES ON
 PRODUCTS.CATEGORY = CATEGORIES.ID LEFT OUTER JOIN TAXCATEGORIES TC ON
 PRODUCTS.TAXCAT = TC.ID,
 PRODUCTS_CAT,
 PEOPLE
WHERE
 PRODUCTS.ID = TICKETLINES.PRODUCT AND PEOPLE.ID = TICKETS.PERSON AND PRODUCTS.ID = PRODUCTS_CAT.PRODUCT
GROUP BY
 PRODUCTS.ID,
 DAY(RECEIPTS.DATENEW),
 PEOPLE.ID
ORDER BY
 CATEGORIES.NAME,
 PRODUCTS.NAME,
 RECEIPTS.DATENEW,
 PEOPLE.NAME
```