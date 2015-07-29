Для настройки распределенной печати делаем следующее.

## Задать параметры товара ##

В поле Параметры товара добавляем следующие

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>  
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">  
<properties>  
<entry key="printkb">Bar</entry> 
<entry key="sendstatus">No</entry> 
</properties>
```

Если товар относится к категории кухня Bar заменяем на Kitchen

## Добавить кнопку на панель продаж ##

Добавляем кнопку "Принять заказ" на панель Продажм через меню Ресурсы

```
<button key="button.sendorder" name="Принять заказ" code="Script.SendOrder"/> 
 
<event key="ticket.addline" code="event.addline"/> 
<event key="ticket.removeline" code="event.removeline"/> 
<event key="ticket.setline" code="event.setline"/>
<event key="ticket.total" code="event.total"/>
```

## Изменить форму строк чека ##

В ресурсе Ticket.Line добавляем строки

```
<column name="label.sendorder" width="5" align="left" value="${ticketline.getProperty('printkb','??')}"/> 
<column name="label.sendstatus" width="5" align="left" value="${ticketline.getProperty('sendstatus','??')}"/>
```


## Добавить локализацию ##

Затем в папке locales правим файл в блокноте pos\_messages.properties и добавляем в него вот это

```
button.sendorder=Send 
label.sendorder=Dest. 
label.sendstatus=Sent
```

## Установить профиль для пользователя ##

Даем разрешения на использование кнопки в Профили, добавляем вот что

```
<class name="button.sendorder"/>
```

## Создать скрипты ##

### Скрипт для отправки чека ###

Затем в окне Ресурсы создаем текстовый ресурс с названием Script.SendOrder

|![![](http://farm5.static.flickr.com/4089/5062240478_c6666c3c7e_m.jpg)](http://farm5.static.flickr.com/4089/5062240478_c6666c3c7e_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

```
// Печать бланка заказа БАР и Кухня  
 
boolean kitchen = false; 
boolean bar = false; 
boolean change_kitchen = false; 
boolean change_bar = false; 
 
for(int i= 0; i < ticket.getLinesCount(); i++){  
 line = ticket.getLine(i); 
 
// Set Discount(printkb=NULL) to N/A so it does not error on next section. 
 if (line.getProperty("printkb") == null){ 
 line.setProperty("printkb", "N/A"); 
 } 
 if (line.getProperty("sendstatus") == null){ 
 line.setProperty("sendstatus", "No"); 
 } 

 if((line.getProperty("printkb").equals("Kitchen")) && (line.getProperty("sendstatus").equals("No"))){
 kitchen = true; //There is something to print to kitchen 
 }else if ((line.getProperty("printkb").equals("Bar")) && (line.getProperty("sendstatus").equals("No"))){ 
 bar = true; //There is something to print to bar 
 }else if ((line.getProperty("printkb").equals("Kitchen")) && (line.getProperty("sendstatus").equals("Cancel"))){ 
 change_kitchen = true; //There is something to change for kitchen 
 }else if ((line.getProperty("printkb").equals("Bar")) && (line.getProperty("sendstatus").equals("Cancel"))){ 
 change_bar = true; //There is something to change for bar 
 } 
}  

if ((change_kitchen && kitchen) || (change_kitchen && !kitchen)) { 
sales.printTicket("Printer.TicketChange_Kitchen"); //Print changed kitchen items to kitchen printer 
} 
if ((change_bar && bar) || (change_bar && !bar)) { 
sales.printTicket("Printer.TicketChange_Bar"); //Print changed bar items to bar printer 
} 
if (kitchen && !change_kitchen) { 
sales.printTicket("Printer.TicketKitchen"); //Print kitchen items to kitchen printer 
} 
if (bar && !change_bar) { 
sales.printTicket("Printer.TicketBar"); //Print bar items to bar printer 
} 

//Show a nice message for confirmation 
if (kitchen && bar){ 
javax.swing.JOptionPane.showMessageDialog(null, "Your order has been sent to the Kitchen & Bar.");  
} else if (kitchen && !bar){ 
javax.swing.JOptionPane.showMessageDialog(null, "Ваш заказ послан только на кухню.");  
} else if (!kitchen && bar){ 
javax.swing.JOptionPane.showMessageDialog(null, "Ваш заказ послан только на Бар.");  
} else if (change_kitchen || change_bar){ 
javax.swing.JOptionPane.showMessageDialog(null, "Отмененные пункты посланы.");  
} else { 
javax.swing.JOptionPane.showMessageDialog(null, "Нет новых заказов.", "Print Warning", JOptionPane.WARNING_MESSAGE);
} 

//Set printkb property of item to Yes so it is not printed again 
for(int i = ticket.getLinesCount()-1; i>= 0 ; i--){  

 line = ticket.getLine(i); 
 String a = line.getProperty("sendstatus"); 
 String b = "Cancel"; 

 if(((line.getProperty("printkb").equals("Kitchen")) || (line.getProperty("printkb").equals("Bar"))) && (line.getProperty("sendstatus").equals("No"))){ 
 line.setProperty("sendstatus", "Yes"); 
 }

 //Remove cancelled lines
 if (a.equals(b)) {   
 ticket.removeLine(i); 
 } 
}

```

### Скрипты реакции на события ###

Затем создадим еще несколько текстовых ресурсов

#### event.addline ####
```
//Remove nulls for unknown products 

if (line.getProperty("printkb") == null){ 
line.setProperty("printkb", "N/A"); 
} 

//No need to send unknown products anywhere

if (line.getProperty("sendstatus") == null){ 
line.setProperty("sendstatus", "Yes"); 
} 
```

#### event.removeline ####
```
// Set SendStatus and print removals 
 
line = ticket.getLine(index); 
String a = line.getProperty("sendstatus"); 
String b = "Yes"; 
String c = "No";  
Integer myCount = 0; 
//get count of auxiliar after the main product 
for (i = index+1; i < ticket.getLinesCount(); i++) {  
 if (ticket.getLine(i).isProductCom()){ 
 myCount = myCount + 1; 
 }else{ 
 break; 
 } 
} 

//Set SendStatus of sent items to Cancel 
if (a.equals(b) && !line.isProductCom()) { 
 for (i = index + myCount; i>= index ; i--) {  
  if (ticket.getLine(i).isProductCom() && ticket.getLine(i).getProperty("sendstatus").equals("Yes")){ 
  ticket.getLine(i).setProperty("sendstatus", "Cancel"); 
  }else if (ticket.getLine(i).isProductCom() && ticket.getLine(i).getProperty("sendstatus").equals("No")){ 
  ticket.removeLine(i); 
  }else{ 
  break; 
  } 
 } 
line.setProperty("sendstatus", "Cancel"); 
} 

//Removelines of NOT sent items 
if (a.equals(c) && !line.isProductCom()) { 
 for (i = index + myCount; i>= index ; i--) {  
  if (ticket.getLine(i).isProductCom()){ 
  ticket.removeLine(i); 
  }else{ 
  break; 
  } 
 } 
ticket.removeLine(index); 
}else if (a.equals(c) && line.isProductCom()) { 
ticket.removeLine(index); 
} 
 
//Cancel event  
removeLine.cancel=true;
```

#### event.setline ####
```
// Set Discount(printkb=NULL) to N/A so it does not error on next section. 
if (line.getProperty("printkb") == null){ 
line.setProperty("printkb", "N/A"); 
} 
if (line.getProperty("sendstatus") == null){ 
line.setProperty("sendstatus", "No"); 
}

//Stop the line modification of sent products 
if (!line.getProperty("sendstatus").equals("No")){
JOptionPane.showMessageDialog(null,
   "You cannot modify a sent item.",
   "Error",
   JOptionPane.ERROR_MESSAGE);
}else{
ticket.setLine(index, line);
}
cancel=true;
```

#### event.total ####
```
//Stop payment when all items not sent 

for(int i= 0; i < ticket.getLinesCount(); i++){  
 line = ticket.getLine(i); 


// Set Discount(printkb=NULL) to N/A so it does not error on next section. 
 if (line.getProperty("printkb") == null){ 
 line.setProperty("printkb", "N/A"); 
 } 
 if (line.getProperty("sendstatus") == null){ 
 line.setProperty("sendstatus", "No"); 
 } 
 
 if(line.getProperty("sendstatus").equals("No") || line.getProperty("sendstatus").equals("Cancel")){
 mySent = "No";
 } 
}  

if (mySent == "No"){
javax.swing.JOptionPane.showMessageDialog(null, "You must Send all items to Kitchen and/or bar before payment", "Send Warning", JOptionPane.WARNING_MESSAGE);
return "Cancel";
}else{
return null;
}
```

## Добавить шаблоны чеков ##

### Для печати заказов ###

Далее создаем следующие ресурсы

#### Printer.TicketKitchen ####

|![![](http://farm5.static.flickr.com/4124/5061641373_0c48795c4a_m.jpg)](http://farm5.static.flickr.com/4124/5061641373_0c48795c4a_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

```
<?xml version="1.0" encoding="UTF-8"?> 
<!--  
 Openbravo POS is a point of sales application designed for touch screens. 
 Copyright (C) 2008 Openbravo, S.L.U. 
 http://sourceforge.net/projects/openbravopos 
  
 This program is free software; you can redistribute it and/or modify 
 it under the terms of the GNU General Public License as published by 
 the Free Software Foundation; either version 2 of the License, or 
 (at your option) any later version. 
  
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 GNU General Public License for more details. 
 
 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 -->
<output> 

<display> 
<line> 
<text align="left" length="10">Заказ посланный на Кухню</text> 
<text align="right" length="10">${ticket.printPreview()}</text> 
</line> 
<line> 
<text align="center" length="20">Спасибо.</text> 
</line> 
</display> 

<ticket printer = "2"> 
<line></line> 
<line></line> 
<line> 
<text align="center" length="42">Заказ Кухня</text> 
</line> 
<line></line> 
<line> 
<text align="left" length="15">Заказ:</text> 
<text>${ticket.printId()}</text> 
</line>  
<line> 
<text align="left" length="15">Дата:</text> 
<text>${ticket.printDate()}</text> 
</line>  
#if ($ticket.getCustomer()) 
<line> 
<text align="left" length="15">Клиент:</text> 
<text>${ticket.getCustomer().getName()}</text> 
</line>  
<line> 
<text align="left" length="15"></text> 
<text>${ticket.getCustomer().getTaxid()}</text> 
</line>  
#end 

#if ($place != "") 
<line> 
<text align="left" length="15">Стол:</text> 
<text>${place}</text> 
</line>  
#end 
<line></line>  
<line> 
<text align ="left" length="17">Наименование</text> 
<text align ="right" length="5"></text> 
</line>  
<line> 
<text>------------------------------------------</text> 
</line>  
#foreach ($ticketline in $ticket.getLines())  
#if (($ticketline.getProperty("printkb").equals("Kitchen")) && ($ticketline.getProperty("sendstatus").equals("No"))) 
<line> 
<text align ="left" length="5" bold="true">${ticketline.printMultiply()}x</text> 
#if ($ticketline.isProductCom())  
<text align ="left" length="37">--${ticketline.printName()}</text> 
#else 
<text align ="left" length="37" bold="true">${ticketline.printName()}</text> 
#end 
</line> 
#end
<!-- Add the following lines only for 2.30 Attributes -->
#if ($ticketline.productAttSetInstId)
<line>
<text align ="left" length="42">    ${ticketline.productAttSetInstDesc}</text>
</line>
#end
<!-- Add the previous lines only for 2.30 Attributes --> 
#end  
<line> 
<text>------------------------------------------</text> 
</line>  
<line> 
<text align="left" length="15">Кассир:</text> 
<text>${ticket.printUser()}</text> 
</line>  
</ticket> 
</output>
```

#### Printer.TicketBar ####

|![![](http://farm5.static.flickr.com/4132/5062240408_5ac4306042_m.jpg)](http://farm5.static.flickr.com/4132/5062240408_5ac4306042_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

```
<?xml version="1.0" encoding="UTF-8"?> 
<!--  
 Openbravo POS is a point of sales application designed for touch screens. 
 Copyright (C) 2008 Openbravo, S.L.U. 
 http://sourceforge.net/projects/openbravopos 
  
 This program is free software; you can redistribute it and/or modify 
 it under the terms of the GNU General Public License as published by 
 the Free Software Foundation; either version 2 of the License, or 
 (at your option) any later version. 
  
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 GNU General Public License for more details. 
 
 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 -->
<output> 

<display> 
<line> 
<text align="left" length="10">Заказ посланный на Бар</text> 
<text align="right" length="10">${ticket.printPreview()}</text> 
</line> 
<line> 
<text align="center" length="20">Спасибо.</text> 
</line> 
</display> 

<ticket printer = "2"> 
<line></line> 
<line></line> 
<line> 
<text align="center" length="42">Заказ Бар</text> 
</line> 
<line></line> 
<line> 
<text align="left" length="15">Заказ:</text> 
<text>${ticket.printId()}</text> 
</line>  
<line> 
<text align="left" length="15">Дата:</text> 
<text>${ticket.printDate()}</text> 
</line>  
#if ($ticket.getCustomer()) 
<line> 
<text align="left" length="15">Клиент:</text> 
<text>${ticket.getCustomer().getName()}</text> 
</line>  
<line> 
<text align="left" length="15"></text> 
<text>${ticket.getCustomer().getTaxid()}</text> 
</line>  
#end 

#if ($place != "") 
<line> 
<text align="left" length="15">Стол:</text> 
<text>${place}</text> 
</line>  
#end 
<line></line>  
<line> 
<text align ="left" length="17">Наименование</text> 
<text align ="right" length="5"></text> 
</line>  
<line> 
<text>------------------------------------------</text> 
</line>  
#foreach ($ticketline in $ticket.getLines())  
#if (($ticketline.getProperty("printkb").equals("Bar")) && ($ticketline.getProperty("sendstatus").equals("No"))) 
<line> 
<text align ="left" length="5" bold="true">${ticketline.printMultiply()}x</text> 
#if ($ticketline.isProductCom())  
<text align ="left" length="37">--${ticketline.printName()}</text> 
#else 
<text align ="left" length="37" bold="true">${ticketline.printName()}</text> 
#end 
</line> 
#end
<!-- Add the following lines only for 2.30 Attributes -->
#if ($ticketline.productAttSetInstId)
<line>
<text align ="left" length="42">    ${ticketline.productAttSetInstDesc}</text>
</line>
#end
<!-- Add the previous lines only for 2.30 Attributes --> 
#end  
<line> 
<text>------------------------------------------</text> 
</line>  
<line> 
<text align="left" length="15">Кассир:</text> 
<text>${ticket.printUser()}</text> 
</line>  
</ticket> 
</output>
```

### Для печати изменений в заказе ###

И еще парочку

#### Printer.TicketChange\_Kitchen ####

|![![](http://farm5.static.flickr.com/4128/5061630405_aec7748456_m.jpg)](http://farm5.static.flickr.com/4128/5061630405_aec7748456_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

```
<?xml version="1.0" encoding="UTF-8"?> 
<output> 
 
<display> 
<line> 
<text align="left" length="10">Change Sent</text> 
<text align="right" length="10">${ticket.printTotal()}</text> 
</line> 
<line> 
<text align="center" length="20">Спасибо.</text> 
</line> 
</display> 

<ticket printer = "2"> 
<line></line> 
<line></line> 
<line size="1"> 
<text align="center" length="42" bold="true">Отмененные пункты Кухня</text> 
</line> 
<line></line> 
<line> 
<text align="left" length="15">Заказ:</text> 
<text>${ticket.printId()}</text> 
</line>  
<line> 
<text align="left" length="15">Дата:</text> 
<text>${ticket.printDate()}</text> 
</line>  
#if ($ticket.getCustomer()) 
<line> 
<text align="left" length="15">Клиент:</text> 
<text>${ticket.getCustomer().getName()}</text> 
</line>  
<line> 
<text align="left" length="15"></text> 
<text>${ticket.getCustomer().getTaxid()}</text> 
</line>  
#end 
 
#if ($place != "") 
<line> 
<text align="left" length="15">Стол:</text> 
<text>${place}</text> 
</line>  
#end 
<line></line>  
<line> 
<text align ="left" length="17">Наименование</text> 
<text align ="right" length="5"></text> 
</line>  
<line> 
<text>------------------------------------------</text> 
</line>  
#foreach ($ticketline in $ticket.getLines())  
#if (($ticketline.getProperty("printkb").equals("Kitchen") && $ticketline.getProperty("sendstatus").equals("Cancel"))||($ticketline.getProperty("printkb").equals("Kitchen") && $ticketline.getProperty("sendstatus").equals("No"))) 
<line> 
#if ($ticketline.getProperty("sendstatus").equals("No")) 
<text align ="left" length="7" bold="true">Add</text> 
#else 
<text align ="left" length="7" bold="true">${ticketline.getProperty("sendstatus")}</text> 
#end 
<text align ="left" length="5" bold="true">${ticketline.printMultiply()}x</text> 
#if ($ticketline.isProductCom())  
<text align ="left" length="30">--${ticketline.printName()}</text> 
#else 
<text align ="left" length="30" bold="true">${ticketline.printName()}</text> 
#end 
</line>
<!-- Add the following lines only for 2.30 Attributes -->
#if ($ticketline.productAttSetInstId)
<line>
<text align ="left" length="42">    ${ticketline.productAttSetInstDesc}</text>
</line>
#end
<!-- Add the previous lines only for 2.30 Attributes -->
#end 
#end  
<line> 
<text>------------------------------------------</text> 
</line>  
</ticket> 
</output>
```

#### Printer.TicketChange\_Bar ####

|![![](http://farm5.static.flickr.com/4150/5061630317_eb2ef78091_m.jpg)](http://farm5.static.flickr.com/4150/5061630317_eb2ef78091_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

```
<?xml version="1.0" encoding="UTF-8"?> 
<output> 
 
<display> 
<line> 
<text align="left" length="10">Change Sent</text> 
<text align="right" length="10">${ticket.printTotal()}</text> 
</line> 
<line> 
<text align="center" length="20">Спасибо.</text> 
</line> 
</display> 

<ticket printer = "2"> 
<line></line> 
<line></line> 
<line size="1"> 
<text align="center" length="42" bold="true">Отмененные пункты Бар</text> 
</line> 
<line></line> 
<line> 
<text align="left" length="15">Заказ:</text> 
<text>${ticket.printId()}</text> 
</line>  
<line> 
<text align="left" length="15">Дата:</text> 
<text>${ticket.printDate()}</text> 
</line>  
#if ($ticket.getCustomer()) 
<line> 
<text align="left" length="15">Клиент:</text> 
<text>${ticket.getCustomer().getName()}</text> 
</line>  
<line> 
<text align="left" length="15"></text> 
<text>${ticket.getCustomer().getTaxid()}</text> 
</line>  
#end 
 
#if ($place != "") 
<line> 
<text align="left" length="15">Стол:</text> 
<text>${place}</text> 
</line>  
#end 
<line></line>  
<line> 
<text align ="left" length="17">Наименование</text> 
<text align ="right" length="5"></text> 
</line>  
<line> 
<text>------------------------------------------</text> 
</line>  
#foreach ($ticketline in $ticket.getLines())  
#if (($ticketline.getProperty("printkb").equals("Bar") && $ticketline.getProperty("sendstatus").equals("Cancel"))||($ticketline.getProperty("printkb").equals("Bar") && $ticketline.getProperty("sendstatus").equals("No"))) 
<line> 
#if ($ticketline.getProperty("sendstatus").equals("No")) 
<text align ="left" length="7" bold="true">Add</text> 
#else 
<text align ="left" length="7" bold="true">${ticketline.getProperty("sendstatus")}</text> 
#end 
<text align ="left" length="5" bold="true">${ticketline.printMultiply()}x</text> 
#if ($ticketline.isProductCom())  
<text align ="left" length="30">--${ticketline.printName()}</text> 
#else 
<text align ="left" length="30" bold="true">${ticketline.printName()}</text> 
#end 
</line>
<!-- Add the following lines only for 2.30 Attributes -->
#if ($ticketline.productAttSetInstId)
<line>
<text align ="left" length="42">    ${ticketline.productAttSetInstDesc}</text>
</line>
#end
<!-- Add the previous lines only for 2.30 Attributes -->
#end 
#end  
<line> 
<text>------------------------------------------</text> 
</line>  
</ticket> 
</output>
```
С ресурсами все.

## Итог ##
Далее делаем распределение печати по номерам принтеров добавляя в ресурсы `Printer.TicketBar` и `Printer.TicketKitchen` тег
`<ticket printer = "2">` или если он есть там то изменяем просто номер принтера
просто изменяя цифры в данном теге.
Выполнив все вышеуказанные операции вы добьетесь распределенной печати по категориям товаров.
Важно помнить
Сначало нажимаете кнопку "Принять заказ", а потом "Печать" рядом с ней.