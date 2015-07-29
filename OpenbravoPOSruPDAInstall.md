# Openbravo POS PDA Module + Apache Tomcat (Openbravo POS on MySQL, windows 7) #

1. Проверяем установлен ли JDK, если нет устанавливаем по-умолчанию в `C:\Program Files\Java\jdk1.7.0_02`.

2. Переходим в http://tomcat.apache.org/ и скачиваем [Apache Tomcat](http://www.gtlib.gatech.edu/pub/apache/tomcat/tomcat-6/v6.0.35/bin/apache-tomcat-6.0.35-windows-x86.zip).

3. Распаковываем в корень диска `С:`. Т.о. получаем: `C:\apache-tomcat-6.0.35\`.

4. Добавляем пару системных переменных:
```
JAVA_HOME = C:\Program Files\Java\jdk1.7.0_02
CATALINA_HOME = C:\apache-tomcat-6.0.35
```

5. Проверяем доступность переменных, введя в командной стоке:
```
echo %java_home%
```
и
```
echo %catalina_home%
```
в ответ должны получить значение переменной, если не получили перезагружаем для их обновления.

6. Идём в `C:\apache-tomcat-6.0.35\bin\` и запускаем сервер запуском `startup.bat` (остановка - `shutdown.bat`)

7. Переходим по http://localhost:8080/ и проверяем работоспособность.

8. Качаем переведённую на русский язык сборку модуля [Openbravo POS PDA версии 2.30.2](http://code.google.com/p/openbravoposru/downloads/detail?name=obposru_2.30.2_pda.war). Распаковываем в: `C:\apache-tomcat-6.0.35\webapps\obposru_2.30.2_pda\`. Директория `C:\apache-tomcat-6.0.35\webapps\` является директорией для приложений.

9. Перезапускаем сервер, аналогично п.6.

Теперь модуль доступен по адресу: http://localhost:8080/obposru_2.30.2_pda/

Если не логинеться делаем следующий шаг:

10. Идем в установленный OpenBravo POS, по-умолчанию `C:\Program Files\openbravoposru-2.30.2\lib\` копируем от туда `mysql-connector-java-5.1.10-bin` в `C:\apache-tomcat-6.0.35\lib`