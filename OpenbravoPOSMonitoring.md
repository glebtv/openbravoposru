# Вводная #

Данная инструкция содержит руководство по установке и настройке:
  * системы мониторинга [Icinga](http://www.icinga.org/) для отслеживания параметров базы данных Openbravo POS;
  * типового набора [плагинов Nagios](http://nagiosplugins.org/);
  * плагина [check\_mysql\_health](http://labs.consol.de/lang/de/nagios/check_mysql_health/) для отслеживания состояния СУБД MySQL и получения информации из базы данных Openbravo POS;
  * плагин [check\_mem.pl](http://www.sysadminsjourney.com/content/2009/06/05/new-and-improved-checkmempl-nagios-plugin) для получения информации об использовании оперативной памяти Ubuntu Server.
  * утилиты [RRDtool](http://oss.oetiker.ch/rrdtool/) для хранения и обработки динамических последовательностей параметров Openbravo POS;
  * веб-оболочки [pnp4nagios](http://docs.pnp4nagios.org/pnp-0.6/start) для вывода информации в виде графиков построенных на основе параметров базы данных Openbravo POS.

Также в данном варианте инструкции рассматривается пример установки с использование СУБД [MySQL](http://www.mysql.com/) для хранения данных получаемых системой мониторинга. За получение, обработку и отправку данных из Icinga в базу данных отвечает IDOUtils.

В рамках данной инструкции установка и настройка производится в ОС **Ubuntu Server 10.04.1 LTS** и если не оговаривается иное с правами **root**. Перед началом работы необходимо ввести:
```
 $ sudo su
```

# Icinga #

![![](http://farm3.static.flickr.com/2707/4208158282_f97ecc9259_m.jpg)](http://farm3.static.flickr.com/2707/4208158282_1fe4c63105_o.png)

## Установка необходимых компонентов ##

До начала установки основного пакета Icinga требуется установить следующие программы библиотеки:
  * HTTP-сервер [Apache](http://ru.wikipedia.org/wiki/Apache);
  * компилятор [GCC](http://ru.wikipedia.org/wiki/Gcc);
  * библиотеки разработчиков [C/C++](http://ru.wikipedia.org/wiki/%D0%AF%D0%B7%D1%8B%D0%BA_%D0%BF%D1%80%D0%BE%D0%B3%D1%80%D0%B0%D0%BC%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F_C%2B%2B);
  * графическая библиотека [GD](http://ru.wikipedia.org/wiki/GD);
  * драйверы [libdbi](http://libdbi.sourceforge.net/) для [СУБД MySQL](http://ru.wikipedia.org/wiki/MySQL).

Для Ubuntu следует установить следующие пакеты:
```
 # apt-get install apache2 build-essential libgd2-xpm-dev
 # apt-get install libjpeg62 libjpeg62-dev libpng12 libpng12-dev
 # apt-get install mysql-server mysql-client libdbi0 libdbi0-dev libdbd-mysql
```

## Создание пользователя и установка прав ##

Создать нового пользователя **icinga** и задать пароль для него.

```
 # useradd -m icinga 
 # passwd icinga
```

Для отправки команд из классического веб-интерфейса необходимо создать группу **icinga-cmd** для включения в неё пользователей веб-интерфейса и объединить пользователей Icinga и веб-пользователей в одной группе **www-data**.

```
 # groupadd icinga-cmd
 # usermod -a -G icinga-cmd icinga
 # usermod -a -G icinga-cmd www-data
```

## Компиляция и установка из исходного кода ##

[Скачать](http://www.icinga.org/download/) наиболее свежий релиз Icinga в каталог и распаковать архив с исходным кодом.

```
 # cd /usr/src/
 # wget http://sourceforge.net/projects/icinga/files/icinga/1.2.1/icinga-1.2.1.tar.gz/download
 # tar xvzf icinga-1.2.1.tar.gz 
 # cd icinga-1.2.1
```

Запустить скрипт конфигуратора Icinga с включёнными параметрами IDOUtils и поддержкой СУБД MySQL (для более подробной информации по доступным параметрам используйте --help).

```
 # ./configure --with-command-group=icinga-cmd --enable-idoutils --enable-mysql
```

Скомпилировать исходный код Icinga.

```
 # make all
```

Установить все компоненты программы.

```
 # make fullinstall
```

## Создание базы данных icinga ##

Для СУБД MySQL создать базу данных, пользователя icinga и назначение ему прав доступа:

```
 # mysql -u root -p
```

```
 mysql> CREATE DATABASE icinga;
 GRANT USAGE ON *.* TO 'icinga'@'localhost' IDENTIFIED BY 'icinga' WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0;
 GRANT SELECT , INSERT , UPDATE , DELETE ON icinga.* TO 'icinga'@'localhost';
 FLUSH PRIVILEGES ;
 mysql> quit
```

Импортировать структуру базы данных для MySQL.

```
 # cd /usr/src/icinga-1.2.1/module/idoutils/db
 # mysql -u root -p icinga < mysql.sql
```

## Настройка IDOUtils ##

Перейти в каталог с настройками Icinga и переименовать шаблоны `idomod.cfg-sample` и `ido2db.cfg-sample`.

```
 # cd /usr/local/icinga/etc/
 # cp idomod.cfg-sample idomod.cfg
 # cp ido2db.cfg-sample ido2db.cfg
```

Если необходимо отредактировать настройки для базы данных MySQL.

```
 # nano /usr/local/icinga/etc/ido2db.cfg
```

Необходимо установить тип СУБД, порт, имя пользователя и пароль.

```
 db_servertype=mysql
 db_port=3306
 db_user=icinga
 db_pass=icinga
```

Редактировать основной файл конфигурации.

```
 # nano /usr/local/icinga/etc/icinga.cfg
```

Сняв комментарий включить для `idomod.o` действие `broker_module`:

```
broker_module=/usr/local/icinga/bin/idomod.o config_file=/usr/local/icinga/etc/idomod.cfg
```

## Запуск IDOUtils ##

IDOUtils должен быть запущен и работать до того как будет запущен Icinga.

Запустить IDOUtils.

```
 # service ido2db start
```

Так же для запуска его по-умолчанию можно установить ido2db в системные сервисы:

```
 # update-rc.d ido2db defaults
```

## Настройка классического веб-интерфейса Icinga ##

Для установки так называемого классического веб-интерфейса необходимо установить CGI-сценарии:

```
 # make cgis
 # make install-cgis
 # make install-html
```

Установить конфигурационный файл Icinga в каталог `conf.d` сервера Apache.

```
 # make install-webconf
```

Создать учётную запись **icingaadmin** для доступа к веб-интерфейсу Icinga.

```
 # htpasswd -c /usr/local/icinga/etc/htpasswd.users icingaadmin
```

Перезагрузить сервер Apache для применения новых установок.

```
 # service apache2 restart
```

## Установка плагинов Nagios ##

[Скачать](http://sourceforge.net/projects/nagiosplug/files/) последнюю версию наборов плагинов предназначенных для Nagios.

```
 # cd /usr/src/
 # wget http://sourceforge.net/projects/nagiosplug/files/nagiosplug/1.4.15/nagios-plugins-1.4.15.tar.gz/download
 # tar xvzf nagios-plugins-1.4.15.tar.gz
 # cd nagios-plugins-1.4.15
```

Запустить конфигуратор с параметрами определёнными при установке Icinga.

```
 # ./configure --prefix=/usr/local/icinga --with-cgiurl=/icinga/cgi-bin --with-htmurl=/icinga --with-nagios-user=icinga --with-nagios-group=icinga
```

Произвести очистку каталогов от прошлых установок.

```
 # make clean
```

Скомпилировать и установить плагины в каталог `/usr/local/icinga/libexec`.

```
 # make 
 # make install
```

## Запуск Icinga ##

Проверить конфигурационные файлы Icinga.

```
 # /usr/local/icinga/bin/icinga -v /usr/local/icinga/etc/icinga.cfg
```

Если ошибок нет, запустить Icinga.

```
 # service icinga start
```

Чтобы в дальнейшем Icinga запускался автоматически добавить скрипт запуска в список системных сервисов.
```
 # update-rc.d icinga defaults
```

Для доступа к веб-интерфейсу перейдите по ссылке [http://localhost/icinga/](http://localhost/icinga/).

# check\_mysql\_health #

[Скачать](http://labs.consol.de/lang/en/nagios/check_mysql_health/) наиболее свежую и стабильную версию плагина для проверки состояния СУБД MySQL от немецкой компании [ConSol\*](http://www.consol.de/nagios-monitoring/)

```
 # cd /usr/src/
 # wget http://labs.consol.de/wp-content/uploads/2010/12/check_mysql_health-2.1.5.tar.gz
 # tar xvzf check_mysql_health-2.1.5.tar.gz
 # cd check_mysql_health-2.1.5
```

Запустить конфигуратор с параметрами определёнными при установке Icinga.

```
 # ./configure --prefix=/usr/local/icinga --with-nagios-user=icinga --with-nagios-group=icinga --with-mymodules-dir=/usr/local/icinga/libexec --with-mymodules-dyn-dir=/usr/local/icinga/libexec
```

Скомпилировать исходный код плагина check\_mysql\_health.

```
 # make
```

Установить плагин в каталог `/usr/local/icinga/libexec`.

```
 # make install
```

# check\_mem.pl #

По материалам [статьи](http://www.sysadminsjourney.com/content/2009/06/05/new-and-improved-checkmempl-nagios-plugin) cайте [SysAdmin's Journey](http://www.sysadminsjourney.com/) скачать и установить Perl-плагин `check_mem.pl`.

```
 # cd /usr/local/icinga/libexec
 # wget http://www.sysadminsjourney.com/sites/sysadminsjourney.com/files/code/check_mem.pl.txt
 # mv check_mem.pl.txt check_mem.pl
```

Задать право исполнения для файла `check_mem.pl`.

```
 # chmod +x check_mem.pl
```

В основе работе данного плагина используются данные из `/proc/meminfo`.

# RRDtool #
[Скачать](http://oss.oetiker.ch/rrdtool/download.en.html) набор утилит [RRDTool](http://ru.wikipedia.org/wiki/RRDtool) для хранения и обработки динамических последовательностей данных такие как данные получаемые от плагинов работающих в рамках Icinga.

```
 # cd /usr/src/
 # wget http://oss.oetiker.ch/rrdtool/pub/rrdtool-1.4.2.tar.gz
 # tar xvzf rrdtool-1.4.2.tar.gz
 # cd rrdtool-1.4.2
```

Для Ubuntu необходимо установить библиотеки.

```
 # apt-get install libpango1.0-dev libxml2-dev
```

Запустить конфигуратор с указанием каталога установки.

```
 # ./configure --prefix=/usr/local/rrdtool
```

Скомпилировать исходный код утилиты RRDtool.

```
 # make
```

Установить утилиту в каталог `/usr/local/rrdtool`.

```
 # make install
```

# pnp4nagios #
## Компиляция и установка из исходного кода ##
[Скачать](http://sourceforge.net/projects/pnp4nagios/files/) дополнение к Nagios(в данном примере к Icinga) для автоматического сохранения данных получаемых в результате работы плагинов в RRD(Round Robin Databases) и вывода информации через веб-интерфейс в виде графиков.

```
 # cd /usr/src/
 # wget http://sourceforge.net/projects/pnp4nagios/files/PNP-0.6/pnp4nagios-0.6.1.tar.gz/download
 # tar xvzf pnp4nagios-0.6.1.tar.gz
 # cd pnp4nagios-0.6.1
```

Запустить конфигуратор с указанием пользователя и группы Icinga, также указать путь к бинарному файлу RRDTool.

```
 # ./configure --with-nagios-user=icinga --with-nagios-group=icinga --with-rrdtool=/usr/local/rrdtool/bin/rrdtool
```

Скомпилировать исходный код всех пакетов входящих в pnp4nagios.

```
 # make all
```

Установить дополнение в каталог `/usr/local/pnp4nagios/`.

```
 # make install
```

Установить примеры конфигурационных файлов pnp4nagios в `/usr/local/pnp4nagios/etc`.

```
 # make install-config
```

Установить конфигурационные файлы веб-сервера Apache.

```
 # make install-webconf
```

## Настройка веб-интерфейса для совместной работы с Icinga ##

Для Ubuntu необходимо установить дополнительные библиотеки.

```
 # apt-get install php5-gd
```

Настроить конфигурацию Apache.

```
 # nano /etc/php5/apache2/php.ini
```

Найти и задать значение **Off** для `magic_quotes_gpc`.

```
magic_quotes_gpc = Off
```

Подключаем модуль `mod_rewrite`.

```
 # sudo a2enmod rewrite
```

Изменить конфигурацию для корневого каталога хоста.

```
 # nano /etc/apache2/sites-available/default
```

Задать значение для `AllowOverride`.

```
        <Directory />
                Options FollowSymLinks
                AllowOverride All
        </Directory>
```

Открыть файл `pnp4nagios.conf`.

```
 # nano /etc/apache2/conf.d/pnp4nagios.conf
```

Исправить поля определяющие идентификацию учётных записей для веб-интерфейса на соответствующие настройкам Icinga.

```
        AuthName "Icinga Access"
        AuthType Basic
        AuthUserFile /usr/local/icinga/etc/htpasswd.users
```

Перезагрузить сервер Apache для применения новых установок.

```
 # /etc/init.d/apache2 restart
```

Зайти по адресу [http://localhost/pnp4nagios/](http://localhost/pnp4nagios/) и проверить прохождение теста. В случае успешного его прохождения удалить или переименовать файл `install.php`.

```
 # mv /usr/local/pnp4nagios/share/install.php /usr/local/pnp4nagios/share/_install.php
```

Отредактировать файл `config.php` с настройками веб-интерфейса.

```
 # nano /usr/local/pnp4nagios/etc/config.php
```

Установить значение параметра `nagios_base`.

```
$conf['nagios_base'] = "/icinga/cgi-bin";
```

## Настройки конфигурации Icinga для отправки данных в php4nagios ##

Открыть файл конфигурации Icinga.

```
 # nano /usr/local/icinga/etc/icinga.cfg
```

Разрешить передачу результатов выполнения плагинов Icinga для обработки и хранения в php4nagios.

```
process_performance_data=1

service_perfdata_file=/usr/local/pnp4nagios/var/service-perfdata
service_perfdata_file_template=DATATYPE::SERVICEPERFDATA\tTIMET::$TIMET$\tHOSTNAME::$HOSTNAME$\tSERVICEDESC::$SERVICEDESC$\t$
service_perfdata_file_mode=a
service_perfdata_file_processing_interval=15
service_perfdata_file_processing_command=process-service-perfdata-file

host_perfdata_file=/usr/local/pnp4nagios/var/host-perfdata
host_perfdata_file_template=DATATYPE::HOSTPERFDATA\tTIMET::$TIMET$\tHOSTNAME::$HOSTNAME$\tHOSTPERFDATA::$HOSTPERFDATA$\tHOST$
host_perfdata_file_mode=a
host_perfdata_file_processing_interval=15
host_perfdata_file_processing_command=process-host-perfdata-file
```

Открыть файл `commands.cfg`.

```
 # nano /usr/local/icinga/etc/objects/commands.cfg
```

Определить команды вызываемые для передачи данных в pnp4nagios.

```
define command{
       command_name    process-service-perfdata-file
       command_line    /usr/local/pnp4nagios/libexec/process_perfdata.pl --bulk=/usr/local/pnp4nagios/var/service-perfdata
}

define command{
       command_name    process-host-perfdata-file
       command_line    /usr/local/pnp4nagios/libexec/process_perfdata.pl --bulk=/usr/local/pnp4nagios/var/host-perfdata
}
```

Для доступа из веб-интерфейса Icinga к соответствующим страницам с графиками в pnp4nagios необходимо создать дополнительные шаблоны в файле `templates.cfg`.

```
 # nano /usr/local/icinga/etc/objects/templates.cfg
```

Добавить шаблон для узлов.

```
define host{
        name                            host-pnp		; Название шаблона узлов.
        action_url                      /pnp4nagios/graph?host=$HOSTNAME$&srv=_HOST_
								; Внешняя ссылка для доступа к статистике по узлу 
								; в системе pnp4nagios.
        register                        0			; Признак того, что это шаблон.
	}
```

Добавить шаблон для служб.

```
define service{
        name                            srv-pnp			; Название шаблона служб.
        action_url                      /pnp4nagios/graph?host=$HOSTNAME$&srv=$SERVICEDESC$ 
								; Внешняя ссылка для доступа к статистике по службе 
								; в системе pnp4nagios.
        register                        0			; Признак того, что это шаблон.
	}
```

Проверить файлы настроек pnp4nagios работающих совместно с Icinga(одновременно будут проверены конфигурационные файлы Icinga).

```
 # /usr/local/pnp4nagios/libexec/verify_pnp_config.pl --mode=bulk --english --monitor=icinga
```

Если ошибок нет, перезагрузить сервер Icinga для применения новых установок.

```
 # /etc/init.d/icinga restart
```

# Мониторинг базы данных Openbravo POS #
## Изменение файлов конфигурации Icinga ##

|![![](http://farm5.static.flickr.com/4028/4208135610_2de12e197c_m.jpg)](http://farm5.static.flickr.com/4028/4208135610_0511ed7e7f_o.png)|![![](http://farm3.static.flickr.com/2580/4208136054_3300fdb055_m.jpg)](http://farm3.static.flickr.com/2580/4208136054_ba9c441723_o.png)|
|:---------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------|

### Константы в resource.cfg ###

Открыть файл ресурсов Icinga.

```
 # nano /usr/local/icinga/etc/resource.cfg
```

В данном файле определяются значения констант `$USERx$`, для их дальнейшего использования при описании команд в конфигурационных файлах. Константы `$USERx$` удобны для хранения информации о пользователях, паролей и т.п. Также они определяют путь к плагинам и обработчикам, при этом если в будущем надо будет изменить этот путь изменить потребуется только значения `$USERx$` оставив прежними описание команд.

Icinga поддерживает до 32 вариантов констант `$USERx$` с `$USER1$` по `$USER32$`. Константы могут быть использованы для хранения разнообразных данных, так например для хранения параметров подключения к серверу MySQL.

В файле `resource.cfg` установить значение констант для подключения к базе данных Openbravo POS. Константе `$USER3$` присвоить значение имя для доступа к базе данных Openbravo POS.

```
$USER3$=icinga
```

Константе `$USER4$` присвоить значение пароля.

```
$USER4$=icinga
```

Константе `$USER5$` присвоить значение названия базы данных Openbravo POS, которую необходимо проверять.

```
$USER5$=openbravopos
```

Скриптам CGI не должны быть доступны файлы с ресурсами, для этого следует установить для них права 600 или 660.

```
 # chmod -R 660 /usr/local/icinga/etc/resource.cfg
```

### Описание команд проверки в commands.cfg ###

Внести описание команды `check_db_openbravopos` в `commands.cfg`, для выполнения SQL-запросов и получения информации из базы данных Openbravo POS.

```
 # nano /usr/local/icinga/etc/objects/commands.cfg
```

```
define command{
	command_name	check_db_openbravopos
	command_line	$USER1$/check_mysql_health --hostname $HOSTADDRESS$  --mode sql --username $USER3$ --password $USER4$ --database $USER5$ --name=$ARG2$ --name2=$ARG1$ --warning $ARG3$ --critical $ARG4$
	}
```

Установить порядок подстановки переменных и констант для следующих параметров плагина `check_mysql_health`:
  * `--hostname` адрес узла опрашиваемого плагином, присваивается значение переменной `$HOSTADDRESS$`;
  * `--mode` тип выполнения `check_mysql_health`, присваивается значение `sql` для вывода результата выполнения SQL-запроса;
  * `--username` имя пользователя базы данных MySQL, присваивается значение константы `$USER3$`;
  * `--password` пароль пользователя базы данных MySQL, присваивается значение константы `$USER4$`;
  * `--database` название базы данных MySQL, присваивается значение константы `$USER5$`;
  * `--name` SQL-запрос для выполнения, присваивается значение переменной `$ARG2$`;
  * `--name2` наименование наблюдаемого службой значения, присваивается значение `$ARG1$`;
  * `--warning` диапазон значений при котором статус службы становится **warning**, присваивается значение $ARG3$;
  * `--critical` диапазон значений при котором статус службы становится **critical**, присваивается значение $ARG4$.

### Шаблон для служб в templates.cfg ###

Для удобства описания служб, предназначенных для проверки состояния базы данных Openbravo POS, добавить шаблон `db-check-service` в `templates.cfg`.

```
 # nano /usr/local/icinga/etc/objects/templates.cfg
```

```
define service{
	name				db-check-service	; Название шаблона служб.
	use				generic-service		; Родительский шаблон для данной службы.
        max_check_attempts              2			; Количества повторных проверок службы для подтверждения
								; статуса оповещения.
        normal_check_interval           1			; Интервал времени в минутах для проверки статуса службы.
        retry_check_interval            1			; Интервал времени в минутах для повторной проверки статуса
								; службы в случае сбоя выполнения команды.
	notification_options		w,c			; Отправлять оповещение для статуса warning и critical.
        register                        0       		; Признак того, что это шаблон.
	}
```

### Настройки служб в localhost.cfg ###

Внести службы для использования при проведения мониторинга узла `localhost`.

```
 # nano /usr/local/icinga/etc/objects/localhost.cfg
```

Для объединения служб создать группу `openbravopos-services`.

```
define servicegroup{
	servicegroup_name               openbravopos-services
	alias                           Openbravo POS Services
	}
```

Проверка какое количество номенклатурных позиций зарегистрировано в базе данных Openbravo POS. Критерий определяющий скорость работы с товаром и влияющий на возможность работы с панелью **Продажи** (подробнее [Issue 6](https://code.google.com/p/openbravoposru/issues/detail?id=6)).

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Counter Products
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!count_products!'SELECT COUNT(*) FROM PRODUCTS'!5000!10000
        }
```

Проверка какое количество клиентов зарегистрировано в базе данных Openbravo POS. Критерий может определятся количеством выпущенных дисконтных карт.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Counter Customers
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!count_customers!'SELECT COUNT(*) FROM CUSTOMERS'!500!1000
        }
```

Проверка какое количество пользователей зарегистрировано в базе данных Openbravo POS. Допустимые рамки критерия определяется исходя из технического задания.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Counter Peoples
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!count_peoples!'SELECT COUNT(*) FROM PEOPLE'!10!20
        }
```

Проверка какое количество закрытых на момент проверки смен по кассе в базе данных Openbravo POS. Критерий определяется исходя из установленного режима сменной работы POS-терминалов.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Counter Closed Cash
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!count_closed_cash!'SELECT COUNT(*) FROM CLOSEDCASH WHERE DATEEND IS NOT NULL'!1000!2000
        }
```

Проверка сколько позиций по складским накладным и чекам проводится в минуту в базе данных Openbravo POS (данные берутся с 5 минутным запаздыванием для минимизации влияния рассинхронизированности времени между сервером и терминалами). Допустимые рамки критерия могут определяться исходя из скорости печати строк чековым принтерам за 1 минуту.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Day Counter of Stock Diary
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!day_count_stock!'SELECT COUNT(DATENEW) FROM STOCKDIARY WHERE YEAR(DATENEW) = YEAR(NOW()) AND MONTH(DATENEW) = MONTH(NOW()) AND DAY(DATENEW) = DAY(NOW()) AND HOUR(DATENEW) = HOUR(NOW()) AND MINUTE(DATENEW) = (MINUTE(NOW())-5)'!338!541
        }
```

Проверка сколько чеков проводится в минуту в базе данных Openbravo POS (данные берутся с 5 минутным запаздыванием для минимизации влияния рассинхронизированности времени между сервером и терминалами). Допустимые рамки критерия могут определяться исходя из скорости печати чеков чековым принтерам за 1 минуту.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Day Counter of Receipts
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!day_count_receipts!'SELECT COUNT(DATENEW) FROM RECEIPTS WHERE YEAR(DATENEW) = YEAR(NOW()) AND MONTH(DATENEW) = MONTH(NOW()) AND DAY(DATENEW) = DAY(NOW()) AND HOUR(DATENEW) = HOUR(NOW()) AND MINUTE(DATENEW) = (MINUTE(NOW())-5)'!11!17
        }
```

Проверка сколько чеков всех видов было проведено в базе данных Openbravo POS. Допустимые рамки критерия могут определяться исходя из гарантированного производителем количества чеков печатаемых чековым принтером.

```
define service{
        use                             db-check-service,srv-pnp
        host_name                       localhost
        service_description             DB Openbravo POS Total Number of Ticket
        servicegroups                   openbravopos-services
	check_command			check_db_openbravopos!total_number_ticket!'SELECT (TICKETSNUM.ID+TICKETSNUM_PAYMENT.ID+TICKETSNUM_REFUND.ID) AS TOTAL_TICKET FROM TICKETSNUM, TICKETSNUM_PAYMENT, TICKETSNUM_REFUND'!750000!937500
        }
```

## Изменение шаблонов pnp4nagios ##

|![![](http://farm5.static.flickr.com/4050/4207727477_650f8672a2_m.jpg)](http://farm5.static.flickr.com/4050/4207727477_e6f68d3ac3_o.png)|![![](http://farm5.static.flickr.com/4057/4208136478_dd562df4c8_m.jpg)](http://farm5.static.flickr.com/4057/4208136478_ab4d660b42_o.png)|
|:---------------------------------------------------------------------------------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------------------|

## Добавление шаблона для check\_db\_openbravopos ##

По-умолчанию при построении графиков RRDtool в pnp4nagios используется шаблон `/usr/local/pnp4nagios/share/templates.dist/default.php`. Специально для служб работающих с командой `check_db_openbravopos` можно создать специальный шаблон:

```
 # nano /usr/local/pnp4nagios/share/templates/check_db_openbravopos.php
```

Он будет содержать обработчики для каждой вызываемой службы.

```
<?php

$defcnt  = 1;

$black   = "494B4C";
$blue    = "235FC4";
$green   = "00AF00";
$yellow  = "FFFF00";
$orange  = "F57D1B";
$red     = "F83838";

$watermark = "\"Google Code Project Openbravo POS ru\"";

foreach ($DS as $i) {

    $warning = ($WARN[$i] != "") ? $WARN[$i] : "";
    $critical = ($CRIT[$i] != "") ? $CRIT[$i] : "";

    if(preg_match('/^count_products$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Изменение позиций в PRODUCTS БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Регистрация новых номенклатурных позиций\" --vertical-label \"Кол-во, ед.\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:productcounter=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:productcounter#$black ";
        $def[$defcnt] .= "GPRINT:productcounter:LAST:\"Кр. = %5.0lf ед.\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^count_peoples$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Изменение позиций в PEOPLE БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Регистрация новых пользователей\" --vertical-label \"Кол-во, ед.\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:peoplecounter=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:peoplecounter#$orange ";
        $def[$defcnt] .= "GPRINT:peoplecounter:LAST:\"Кр. = %5.0lf ед.\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^count_customers$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Изменение позиций в CUSTOMERS БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Регистрация новых клиентов\" --vertical-label \"Кол-во, ед.\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:customercounter=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:customercounter#$blue ";
        $def[$defcnt] .= "GPRINT:customercounter:LAST:\"Кр. = %5.0lf ед.\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^count_closed_cash$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Изменение позиций в CLOSEDCASH БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Регистрация закрытых смен\" --vertical-label \"Кол-во, ед.\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:closedcashcounter=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:closedcashcounter#$green ";
        $def[$defcnt] .= "GPRINT:closedcashcounter:LAST:\"Кр. = %5.0lf ед.\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^day_count_stock$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Количество зарегистрированных строк в STOCKDIARY БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Движение по складам\" --vertical-label \"Строк, стр/мин\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:stockday=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:stockday#$blue ";
        $def[$defcnt] .= "GPRINT:stockday:MIN:\"Мин. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:stockday:AVERAGE:\"Ср. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:stockday:MAX:\"Макс. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:stockday:LAST:\"Кр. = %5.0lf стр/мин\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^day_count_receipts$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Количество зарегистрированных чеков в RECEIPTS БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Движение по кассам\" --vertical-label \"Строк, стр/мин\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:receiptday=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "AREA:receiptday#$green ";
        $def[$defcnt] .= "GPRINT:receiptday:MIN:\"Мин. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:receiptday:AVERAGE:\"Ср. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:receiptday:MAX:\"Макс. = %5.0lf стр/мин\" ";
        $def[$defcnt] .= "GPRINT:receiptday:LAST:\"Кр. = %5.0lf стр/мин\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }

    if(preg_match('/^total_number_ticket$/', $NAME[$i])) {
        $ds_name[$defcnt] = "Отслеживание TICKETSNUM, TICKETSNUM_PAYMENT и  БД Openbravo POS.";
        $opt[$defcnt] = "--title \"Всего выписано чеков\" --vertical-label \"Количество, шт.\" -X 0 -l 0 --legend-position=south --legend-direction=topdown --watermark $watermark ";
        $def[$defcnt] = "";
        $def[$defcnt] .= "DEF:ticketnumber=$RRDFILE[$i]:$DS[$i]:AVERAGE:reduce=LAST " ;
        $def[$defcnt] .= "LINE2:ticketnumber#$black ";
        $def[$defcnt] .= "GPRINT:ticketnumber:LAST:\"Кр. = %5.0lf шт.\" ";
        if ($warning != "") {
		$def[$defcnt] .= "HRULE:$warning#$yellow ";
	}
	if ($critical != "") {
		$def[$defcnt] .= "HRULE:$critical#$red ";
	}
        $defcnt++;
    }
}
?>
```