# Описание #

![![](http://www.godexintl.com/upload/1430f7f1acf18378.jpg)](http://www.godexintl.com/upload/1430f7f1acf18378.jpg)

Для принтеров этикеток Godex появился системный драйвер под Linux:

http://www.godexintl.com/downloads.aspx?cid=14

Он осуществляет перевод растровой картинки в внутренний язык принтера EZPL. Ниже инструкция, как его подключить к Ubuntu 12.04.

# Сборка исходников #

Устанавливаем библиотеки для сборки драйвера из исходников.

```
 sudo apt-get install libcupsimage2-dev
```

Проверяем наличие CUPS PPD.

```
 sudo apt-get install cups-ppdc 
```

Скачиваем исходники драйвера и распаковываем их http://www.godexintl.com/download/rastertoezpl-0.8.tar.gz. Собираем и устанавливаем драйвер:

```
 ./configure
 make
 sudo make install
```

Заходим в панель управления принтерами и там должен появится новый принтер.

![http://farm9.staticflickr.com/8003/7566746486_9a7203405f_n_d.jpg](http://farm9.staticflickr.com/8003/7566746486_9a7203405f_n_d.jpg)

# Настройка AppArmor #

Чтобы начать работу с принтером вам также необходимо разрешить отправку на печать в AppArmor. Для этого устанавливаем утилиты управления профилями AppArmor.

```
 sudo apt-get install apparmor-profiles apparmor-utils
```

Редактируем профиль для сервера печати CUPS.

```
 cd /etc/apparmor.d/
 sudo nano usr.sbin.cupsd
```

Добавив в блок `/usr/sbin/cupsd` строку:

```
 /usr/local/libexec/rastertoezpl/rastertoezpl Uxr,
```

Сохраняем и активируем AppArmor с новыми настройками.

```
 sudo aa-enforce /usr/sbin/cupsd .
```

# Настройка печати #

Теперь можем попробовать распечатать пробную страницу Ubuntu.

![![](http://farm8.staticflickr.com/7117/7566753648_3e601aa474_d.jpg)](http://farm8.staticflickr.com/7117/7566753648_3e601aa474_b_d.jpg)

Вот результат. В принципе теперь можете на этом принтере распечатывать всё что хотите из Ubuntu.

![![](http://farm8.staticflickr.com/7134/7566818716_f9d93bacc7_n_d.jpg)](http://farm8.staticflickr.com/7134/7566818716_f9d93bacc7_b_d.jpg)

Если у вас картинка не вошла попробуйте настроить размер этикетки через вкладку **Параметры принтера**. Скорей всего вам надо выставить значение **Media Size** под размер заправленной ленты и размер просвета **Label Gap Length** между этикетками.

![![](http://farm8.staticflickr.com/7259/7566773106_f3db0e6a49_d.jpg)](http://farm8.staticflickr.com/7259/7566773106_f3db0e6a49_b_d.jpg)

# Печать этикеток из Openbravo POS #

Меняем шаблон отчётов на листе на шаблон для этикеток на ленте. В нашем проекте он называется `productlabels58x30`. Для этого размера надо выставить в параметрах принтера **Media Size** 50x30mm. Меняем или добавляем новую строку в ресурс `Menu.Root`.

```
        submenu.addPanel("/com/openbravo/images/appointment.png", "Menu.ProductLabels", "/com/openbravo/reports/productlabels58x30.bs");
```

Открываем доступ к отчёту в профиле пользователя.

```
    <class name="/com/openbravo/reports/productlabels58x30.bs"/> 
```

Всё сохраняем и перезагружаемся. Пробуем распечатать этикетку.

![![](http://farm9.staticflickr.com/8431/7566799680_8a9b43e1fc_d.jpg)](http://farm9.staticflickr.com/8431/7566799680_8a9b43e1fc_b_d.jpg)

# Решение проблемы с расположением #

![![](http://farm9.staticflickr.com/8433/7566816850_d2e85952b3_n_d.jpg)](http://farm9.staticflickr.com/8433/7566816850_d2e85952b3_b_d.jpg)

Как видите результат не совсем тот, что ожидался. Для решения попробовал поэкспериментировать с **Параметрами задания** в драйвере принтера. У меня получился хоть и перевёрнутый, но рабочий результат после выставления значения **Ориентация** в **Перевёрнутая книжная**.

![![](http://farm9.staticflickr.com/8159/7566772952_17a63f317f_d.jpg)](http://farm9.staticflickr.com/8159/7566772952_17a63f317f_b_d.jpg)

Применяем новые параметры и пробуем ещё раз.

![![](http://farm8.staticflickr.com/7134/7566817706_0abd4a1aa6_n_d.jpg)](http://farm8.staticflickr.com/7134/7566817706_0abd4a1aa6_b_d.jpg)

Всё работает и теперь у нас есть принтер работающий с Openbravo POS из под Linux.