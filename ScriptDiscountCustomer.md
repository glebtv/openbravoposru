## Разрешить предоставление скидки по товару ##

В справочнике Администрация -> Управление -> Товары указать параметр разрешающий или запрещающий предоставление скидки по конкретной номенклатурной позиции. Выбрать интересующий товар и задать на вкладке Параметры следующие значение:

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>  
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">  
<properties>  
<entry key="discount">Yes</entry> 
</properties>
```

## Установить данные клиента ##

Перейти в справочник Администрация -> Клиенты -> Клиенты. Выбрать необходимую запись клиента для редактирования.

### Задать номер дисконтной карты ###

Сгенерировать номер дисконтной карты. Признаком, что это дисконтная карта клиента, является литера "c" в начале сгенерированного кода.

### Установить размер скидки ###

В поле на вкладке Примечание ввести размер скидки в процентах предоставляемой данному клиенту по данной дисконтной карте.

## Изменить на экране строку формы чека ##

### Изменить форму строки ###
В ресурсе Ticket.Line добавим строку для отображения размера скидки:

```
    <column name="label.discount" width="50" align="center" value="&lt;html&gt;&lt;font color=green&gt;${ticketline.getProperty('discount','0.0')}%&lt;/font&gt;&lt;/html&gt;"/>
```

### Добавить локализацию ###
Для английского языка добавить в pos\_messages.properties сроку:
```
label.discount=Discount
```

Для русского языка добавить в pos\_messages\_ru.properties сроку:
```
label.discount=Скидка
```

## Создать скрипт Script.DiscountCustomerAdd ##

Для применения скидки создать новый текстовый ресурс Script.DiscountCustomerAdd содержащий:

```
import com.openbravo.pos.util.RoundUtils;

customer = ticket.getCustomer();

if (customer != null) {

double discount = 0.0;

discount = Double.parseDouble(ticket.getCustomer().getNotes());

if (line.getProperty("discount") == null) { 
   line.setProperty("discount", Double.toString(0.0)); 
} else if (line.getProperty("discount").equals("Yes") || line.getProperty("discount").equals("Y")) {
   line.setPrice(RoundUtils.getValue(line.getPrice() - line.getPrice() * (discount/100)));
   line.setProperty("discount", Double.toString(discount)); 
} else {
   line.setProperty("discount", Double.toString(0.0)); 
}
}
```

## Добавить реакцию на событие ##

Для автоматического применения скрипта Script.DiscountCustomerAdd добавить в Ticket.Buttons строчку:

```
    <event key="ticket.addline" code="Script.DiscountCustomerAdd"/>
```

## Итог ##

|![![](http://farm5.static.flickr.com/4109/5070880993_f769f86413_m.jpg)](http://farm5.static.flickr.com/4109/5070880993_f769f86413_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

Теперь если для товара разрешена скидка (параметр discount равен значению "Yes" или "Y") и клиент имеет дисконтную карту с номером сгенерированном в справочнике, ему будет предоставлена скидка в размере процентов указанных в справочнике (например для 10% значение должно быть равно 7). Для этого перед началом регистрации покупок оператор POS-системы должен провести сканером по штрих-коду на дисконтной карте и только затем начать работу с товаром.