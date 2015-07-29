Пример подключение электронных весов [ТВЕС ВР 4149-11 "Капля"](http://tves.com.ru/index.php?p=production&id=5) для получения показаний о массе товара при продаже в системе [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS).

## Преамбула ##

Приведённый ниже пример является первым моим опытом в программировании для Openbravo POS, он показывает простоту внедрения поддержки нового оборудования в Openbravo POS. Код данного примера был в октябре 2008 года был направлен разработчикам Openbravo POS для размещения в репозитарии исходных кодов, но пока этого не произошло.

Выражаю благодарность [ОАО "ТВЕС"](http://tves.com.ru), и лично Грибановскому Л.Г., за предоставленную спецификацию на данный тип весов.

## Постановка задачи ##
Обмен электронных весов с компьютером производится по [последовательному порту](http://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D1%80%D1%82). Согласно протокола обмен между электронными весами и POS системой производится в следующей последовательности:

  * на весы помещается груз;
  * POS система отправляет запрос на весы для получения;
  * весы отвечают на запрос в виде сообщения содержащего показания датчика массы.

Также POS системой может быть передана на весы цена товара для отображения на электронном табло весов.

```
Общий алгоритм обмена POS с электронными весами ВР4149-11(10, 02, 06) версия ПО 3.9

Если масса на весах равна 13,571 кг.

Исходящий запрос 8 байт, посылается POS: 
0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00

Входящий ответ 17 байт, посылается весами:
0x01, 0x05, 0x07, 0x03, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
```

Если исходящие сообщения не содержит информации о цене единицы товара, то не требуется делать дополнительную обработку средствами Openbravo POS. Для входящего сообщения требуется разработать функцию обработки для получения значения массы для последующей регистрации в системе Openbravo POS.

## Решение задачи ##

### Необходимые ресурсы ###
Для разработки вам понадобится:
  * установленная среда разработки [Netbeans IDE 6.5](http://www.netbeans.org/);
  * [исходный код Openbravo POS 2.20](http://downloads.sourceforge.net/openbravopos/openbravopos_2.20_src.zip?modtime=1219951842&big_mirror=0);
  * электронные весы ТВЕС ВР 4149-11 "Капля" подключённые к компьютеру.

### Изменения исходного кода ###
Добавляем в меню настройки конфигурации новый вид весов. Вносим наименование нового устройства `tves4149` в `JPanelConfigGeneral.java`.
```
...
package com.openbravo.pos.config;
...
        jcboMachineScale.addItem("samsungesp");
        jcboMachineScale.addItem("tves4149");
        jcboMachineScale.addItem("Not defined");
...
        p = new StringParser(config.getProperty("machine.scale"));
        sparam = p.nextToken(':');
        jcboMachineScale.setSelectedItem(sparam);
        if ("dialog1".equals(sparam) || "samsungesp".equals(sparam) || "tves4149".equals(sparam)) {
            jcboSerialScale.setSelectedItem(p.nextToken(','));
        }
...
    private void jcboMachineScaleActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        CardLayout cl = (CardLayout)(m_jScaleParams.getLayout());
        
        if ("dialog1".equals(jcboMachineScale.getSelectedItem()) || "samsungesp".equals(jcboMachineScale.getSelectedItem())|| "tves4149".equals(jcboMachineScale.getSelectedItem())) {
            cl.show(m_jScaleParams, "comm");
        } else {
            cl.show(m_jScaleParams, "empty");
        }
    }  
...
```
Добавляем проверку на тип подключённых весов и запуск метода `ScaleTves()`. Вносим наименование нового устройства `tves4149` в `DeviceScale.java`:
```
...
package com.openbravo.pos.scale;
...
    public DeviceScale(AppProperties props) {
        StringParser sd = new StringParser(props.getProperty("machine.scale"));
        String sScaleType = sd.nextToken(':');
        String sScaleParam1 = sd.nextToken(',');
        // String sScaleParam2 = sd.nextToken(',');
        
        if ("dialog1".equals(sScaleType)) {
            m_scale = new ScaleComm(sScaleParam1);
        } else if ("samsungesp".equals(sScaleType)) {
            m_scale = new ScaleSamsungEsp(sScaleParam1);            
        } else if ("fake".equals(sScaleType)) { // a fake scale for debugging purposes
            m_scale = new ScaleFake();            
        } else if ("screen".equals(sScaleType)) { // on screen scale
            m_scale = new ScaleDialog();
        } else if ("tves4149".equals(sScaleType)) { // scale ВР4149-10 & ВР4149-11
            m_scale = new ScaleTves(sScaleParam1);
        } else {
            m_scale = null;
        }
    }
...
```
Создаём метод `ScaleTves()`, где производим процедурой `readWeight()` передачу исходящего сообщения с запросом массы от POS и приём сообщения о массе с весов. При побайтном получении сообщения с весов производится вычисления массы из первых 5 байт и сохранение его в `m_dWeightBuffer`.
```
package com.openbravo.pos.scale;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;

public class ScaleTves implements Scale, SerialPortEventListener {

    private String m_sPortScale;
    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private OutputStream m_out;
    private InputStream m_in;
    private static final int SCALE_READY = 0;
    private static final int SCALE_READING = 1;
    private double m_dWeightBuffer;
    private int m_iStatusScale;

    public ScaleTves(String sPortPrinter) {
        m_sPortScale = sPortPrinter;
        m_out = null;
        m_in = null;
        m_iStatusScale = SCALE_READY;
        m_dWeightBuffer = 0.0;
    }

    public Double readWeight() {

        synchronized (this) {
            if (m_iStatusScale != SCALE_READY) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                }
                if (m_iStatusScale != SCALE_READY) {
                    m_iStatusScale = SCALE_READY;
                }
            }
            
            m_dWeightBuffer = 0.0;
            
            write(new byte[]{0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00});

            flush();

            try {                
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_iStatusScale == SCALE_READY) {
                // a value as been readed.
                double dWeight = m_dWeightBuffer / 1000.0;
                m_dWeightBuffer = 0.0;
                return new Double(dWeight);
            } else {
                m_iStatusScale = SCALE_READY;
                m_dWeightBuffer = 0.0;
                return new Double(0.0);
            }
        }
    }

    private void flush() {
        try {
            m_out.flush();
        } catch (IOException e) {
        }
    }

    private void write(byte[] data) {
        try {
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortScale); // Tomamos el puerto
                m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto
                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura
                m_in = m_CommPortPrinter.getInputStream();
                m_CommPortPrinter.addEventListener(this);
                m_CommPortPrinter.notifyOnDataAvailable(true);
                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
            }
            m_out.write(data);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent e) {
        switch (e.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (m_in.available() > 0) {
                        byte[] b = new byte[17];
                        m_in.read(b);
                        if (m_iStatusScale == SCALE_READY) {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READING;
                        }
                        for (int i = 5; i >= 0; i--) {
                            m_dWeightBuffer = m_dWeightBuffer * 10.0 + (int) b[i];
                        }
                        if (m_dWeightBuffer >= 0.0 && m_dWeightBuffer <= 15050.0) {
                            synchronized (this) {
                                m_iStatusScale = SCALE_READY;
                                notifyAll();
                            }

                        } else {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READY;
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }
    }
}
```

## Результат ##

### Характеристика тестовой системы ###
  * компьютер на базе системной платы MSI 975X Platinum Powerup Edition;
  * ОС Ubuntu 8.04 LTS;
  * исходный код Openbravo POS версия 2.20;
  * электронные весы ТВЕС ВР 4149-11 "Капля", подключён к порту COM1;
  * параметры подключения 4800,N,8,1.

### Проверка работы ###
  * Подключить весы к компьютеру.
  * Компилировать исходный код программы.
  * Исправить ошибки, повторить компиляцию.
  * Запустить исходный код.
  * Перейти в _Система -> Оборудование -> Настройки_, для _Весы_ из списка выбрать `tves4149` и указать порт подключения.
  * Сохранить параметры POS терминала.
  * Перезапустить программу.
  * Перейти в _Управление -> Остатки -> Товары_ и ввести данные о товаре.
  * Отметить в закладке _Остатки_ значение _Весовой товар_.
  * Перейти в _Действия -> Продажи_.
  * Поместить товар на весы, на табло электронных весов должно появится значение массы.
  * Выбрать товар для продажи в POS системе.
  * В поле чека должна появится позиция данного товара, значение столбца _Кол-во_ должно соответствовать значению на табло.

### Примеры работы ###
Подключение электронных весов ТВЕС ВР 4149-11 "Капля" к Openbravo POS в поверочной лаборатории.
|![![](http://farm4.static.flickr.com/3237/2948019223_c1ee6a1415_m.jpg)](http://farm4.static.flickr.com/3237/2948019223_c1ee6a1415_b.jpg)|
|:---------------------------------------------------------------------------------------------------------------------------------------|

### Нерешённые задачи ###
В данном коде не приводится пример использования функции электронных весов ТВЕС ВР 4149-11 "Капля" принимать значение цены из внешних источников, это вызвано некорректной работой кода в ПО весов, которые были использованы при тестировании. О данной ошибке было сообщено разработчикам ПО весов, как только будет известна версия ПО с которой данная функция будет корректно работать, возможность передачи цены будет описана и включена в приведённый исходный код.

## Исходный код ##
[Изменённые файлы исходного кода для Openbravo POS 2.20](http://openbravoposru.googlecode.com/files/OpenbravoPOS-TVESVR4149.zip). Для установки файлы следует скопировать в каталоги с исходным кодом Openbravo POS 2.20:

  * `JPanelConfigGeneral.java` в `../com/openbravo/pos/config/`
  * `DeviceScale.java` в `../com/openbravo/pos/scale/`
  * `ScaleTves.java` в `../com/openbravo/pos/scale/`

Также исходный код включён в [репозитарий данного проекта с ревизии 112](http://code.google.com/p/openbravoposru/source/list).
