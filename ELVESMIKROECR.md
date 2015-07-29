Реализация функций печати текста чеков [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS) на [ККМ ЭЛВЕС Микро-Ф](http://www.shtrih-m.ru/production/produce_54.html) по протоколу работы с [контрольно-кассовыми машинами](http://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%BE%D0%BB%D1%8C%D0%BD%D0%BE-%D0%BA%D0%B0%D1%81%D1%81%D0%BE%D0%B2%D0%B0%D1%8F_%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D0%B0)(далее ККМ).

## Преамбула ##
В Openbravo POS реализована возможность работы с принтерами чеков работающими по протоколу  ESC/POS, через JavaPOS или через драйверы принтера операционной системы. Механизм работы при печати чеков в Openbravo POS разделён на два уровня:

  * обработка информации отправляемой POS-системой на печать с помощью специального XML-шаблона;
  * передача информации на принтер в соответствие с спецификацией протокола обмена принтера.

В рамках данного решения предлагается оставив без изменений уровень подготовки для печати информации, реализовать обмен между POS-системой и ККМ  ЭЛВЕС Микро-Ф согласно спецификации на [Протокол работы ККМ версии 2.2](http://support.shtrih-m.ru/data/elves-mikro-f-01/protocol_2_2.zip).

## Постановка задачи ##
ККМ  ЭЛВЕС Микро-Ф работает с POS-системой по Протоколу работы ККМ версии 2.2. Обмен данного устройства с компьютером производится по [последовательному порту](http://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D1%80%D1%82). Отличием работы данного устройства и протокола обмена от протокола ESC/POS является пакетная отправка (в ESC/POS посимвольная)  сообщений и ожидание POS-системой подтверждения ККМ. Ниже приведена схема алгоритма обмена POS системы с ККМ.
```
Общий алгоритм обмена POS с ККМ

Отправка сообщения
POS: <ENQ><-T1-><STX|DATAOUT|ETX|CRC><-T3-><EOT>
ККМ:      <ACK>                      <ACK>

Приём сообщения
POS: <-T5-><ACK><--------T7--------><ACK><-T8->
ККМ: <ENQ>      <STX|DATAIN|ETX|CRC>     <EOT>

где T1, T3, T5, T7, T8 — время ожидания POS ответа от ККМ;
    ENQ, STX, EOT, ACK, ETX — управляющие символы;
    DATAOUT — пакет данных посылаемый POS;
    DATAIN — пакет данных принимаемый POS;
    CRC — контрольная сумма пакета данных.
```
Согласно представленной схемы предлагается построчно отправлять текстовую информацию формируемую Openbravo POS для печати на ККМ.

## Решение задачи ##

### Необходимые ресурсы ###
Для разработки вам понадобится:
  * установленная среда разработки [Netbeans IDE 6.5](http://www.netbeans.org/);
  * [исходный код Openbravo POS 2.20](http://downloads.sourceforge.net/openbravopos/openbravopos_2.20_src.zip?modtime=1219951842&big_mirror=0);
  * [спецификация на протокол обмена](http://support.shtrih-m.ru/data/elves-mikro-f-01/protocol_2_2.zip);
  * ККМ ЭЛВЕС Микро-Ф подключённый к компьютеру.

### Изменения исходного кода ###

Добавляем в меню настройки конфигурации новый вид принтеров чеков. Вносим наименование нового устройства `elveskkm` в `JPanelConfigGeneral.java`. Можно задать как один принтер, так и все сразу все три, это необходимо если предполагается использовать для печати на нескольких ККМ.
```
...
package com.openbravo.pos.config;
...
        jcboMachinePrinter.addItem("javapos");
        jcboMachinePrinter.addItem("elveskkm");
        jcboMachinePrinter.addItem("Not defined");
...
        StringParser p = new StringParser(config.getProperty("machine.printer"));
        String sparam = unifySerialInterface(p.nextToken(':'));
        if ("elveskkm".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem(sparam);
            jcboConnPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter.setSelectedItem(p.nextToken(','));
        } else if ("serial".equals(sparam) || "file".equals(sparam)) {
...
        String sMachinePrinter = comboValue(jcboMachinePrinter.getSelectedItem());
        if ("epson".equals(sMachinePrinter) || "tmu220".equals(sMachinePrinter) || "star".equals(sMachinePrinter) || "ithaca".equals(sMachinePrinter) || "surepos".equals(sMachinePrinter) || "elveskkm".equals(sMachinePrinter)) {
            config.setProperty("machine.printer", sMachinePrinter + ":" + comboValue(jcboConnPrinter.getSelectedItem()) + "," + comboValue(jcboSerialPrinter.getSelectedItem()));
...
    private void jcboMachinePrinterActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        CardLayout cl = (CardLayout)(m_jPrinterParams1.getLayout());
        
        if ("epson".equals(jcboMachinePrinter.getSelectedItem()) || "tmu220".equals(jcboMachinePrinter.getSelectedItem()) || "star".equals(jcboMachinePrinter.getSelectedItem()) || "ithaca".equals(jcboMachinePrinter.getSelectedItem()) || "surepos".equals(jcboMachinePrinter.getSelectedItem()) || "shtrihm".equals(jcboMachinePrinter.getSelectedItem()) || "elveskkm".equals(jcboMachinePrinter.getSelectedItem())) {
            cl.show(m_jPrinterParams1, "comm");
        } else if ("javapos".equals(jcboMachinePrinter.getSelectedItem())) {
            cl.show(m_jPrinterParams1, "javapos");
        } else {
            cl.show(m_jPrinterParams1, "empty");
        }
    }
...
```

Добавляем проверку на тип подключённого принтера и запуск метода `DevicePrinterElvesKKM()`. Вносим наименование нового устройства `elveskkm` в `DeviceTicket.java` и добавляем вызов метода `DevicePrinterElvesKKM()`:

```
...
package com.openbravo.pos.printer;
...
import com.openbravo.pos.printer.escpos.*;
import com.openbravo.pos.printer.elveskkm.*;
import com.openbravo.pos.printer.javapos.DeviceDisplayJavaPOS;
...
            if ("serial".equals(sPrinterType) || "rxtx".equals(sPrinterType) || "file".equals(sPrinterType)) {
                sPrinterParam2 = sPrinterParam1;
                sPrinterParam1 = sPrinterType;
                sPrinterType = "epson";
            }          
           
            try {
                if ("screen".equals(sPrinterType)) {
...
                } else if ("elveskkm".equals(sPrinterType) && "serial".equals(sPrinterParam1)) {
                    addPrinter(sPrinterIndex, new DevicePrinterElvesKKM(sPrinterParam2));
...
```

Создаём пакет `com.openbravo.pos.printer.elveskkm` в котором располагаем следующие классы:

`PrinterReaderWritter.class`, перечисляем методы необходимые для работы принтера чеков в режиме отправки и приёма сообщений.
```
package com.openbravo.pos.printer.elveskkm;

import com.openbravo.pos.printer.*;

public interface PrinterReaderWritter {
     public void sendInitMessage() throws TicketPrinterException;
     public void sendTextMessage(String sText) throws TicketPrinterException;
     public void sendBeepMessage() throws TicketPrinterException;
     public void sendStampTitleReportMessage() throws TicketPrinterException;
     public void sendOpenDrawerMessage() throws TicketPrinterException;
     public void disconnectDevice();
}
```
`DeviceElvesKKMComm.java`, устанавливаем процедуры необходимые для выполнения методов класса `PrinterReaderWritter.class`.
```
package com.openbravo.pos.printer.elveskkm;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;
import com.openbravo.pos.printer.*;

public class DeviceElvesKKMComm implements PrinterReaderWritter, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private String m_sPort;
    private OutputStream m_out;
    private InputStream m_in;

    //Состояние принтера
    private static final int PRINTER_READY = 0;     //Принтер готов к получению сообщения
    private static final int PRINTER_READING = 1;   //Принтер отправляет сообщение
    private int m_iStatusPrinter;

    //Интервалы ожидания сообщений от принтера
    private static final int T1 = 500;              //Задержка 0,5 сек
    private static final int T3 = 500;              //Задержка 0,5 сек
    private static final int T5 = 2000;             //Задержка 2,0 сек
    private static final int T6 = 500;              //Задержка 0,5 сек
    private static final int T7 = 500;              //Задержка 0,5 сек
    private static final int T8 = 1000;             //Задержка 1,0 сек

    //Управляющие символы протокола
    private static final byte[] ENQ = {0x05};       //Запрос
    private static final byte[] EOT = {0x04};       //Конец передачи
    private static final byte[] ACK = {0x06};       //Подтверждение получения сообщения
    private static final byte[] STX = {0x02};       //Начало блока данных
    private static final byte[] NAK = {0x15};       //Отрицание получения сообщения

    public DeviceElvesKKMComm(String sPortPrinter) {
        m_sPort = sPortPrinter;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    //Команды обмена с принтером
    //Проверка состояния принтера
    public void sendInitMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.INIT);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Печать текстового сообщения
    public void sendTextMessage(String sText) throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.PRN);
        byte[] MSG = new byte[sText.length()];
        MSG = UnicodeConverterCP866(sText);
        for (int i = 0; i < MSG.length && i < 24; i++) {
            lineout.write(MSG[i]);
        }
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Вывод звукового сигнала
    public void sendBeepMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.BEEP);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Печать клише чека
    public void sendStampTitleReportMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.TITLE);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Открыть денежный ящик
    public void sendOpenDrawerMessage() throws TicketPrinterException {
        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        lineout.write(ElvesKKM.OPEN_DRAWER);
        lineout.write(ElvesKKM.ETX);
        lineout.write(calcCheckSumCRC(lineout.toByteArray()));
        sendMessage(lineout.toByteArray());
    }

    //Закрыть соединение
    public void disconnectDevice() {
        try {
            m_out.close();
            m_in.close();
            m_CommPortPrinter.close();
        } catch (IOException e) {
        }

        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    //Функция отправки блока данных
    private void sendMessage(byte[] data) {
        synchronized (this) {
            write(ENQ);
            try {
                wait(T1);
            } catch (InterruptedException e) {
            }
            write(STX);
            write(data);
            flush();
            try {
                wait(T3);
            } catch (InterruptedException e) {
            }
            write(EOT);
            flush();
            try {
                wait(T5);
            } catch (InterruptedException e) {
            }
            write(ACK);
            flush();
            try {
                wait(T7);
            } catch (InterruptedException e) {
            }
            write(ACK);
            flush();
            try {
                wait(T8);
            } catch (InterruptedException e) {
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
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort); // Tomamos el puerto
                m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", T6); // Abrimos el puerto

                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura
                m_in = m_CommPortPrinter.getInputStream();

                m_CommPortPrinter.addEventListener(this);
                m_CommPortPrinter.notifyOnDataAvailable(true);

                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_ODD); // Configuramos el puerto
            }
            m_out.write(data); //Отправка байта сообщения на принтер
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

        // Determine type of event.
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
                        int b = m_in.read(); //Приём байта сообщения с принтера
                        if (b == EOT[0] || b == NAK[0] && m_iStatusPrinter == PRINTER_READING) { // RS ASCII
                            synchronized (this) {
                                m_iStatusPrinter = PRINTER_READY;
                                notifyAll();
                            }
                        } else if (b == ACK[0] || b == ENQ[0]) {
                            synchronized (this) {
                                m_iStatusPrinter = PRINTER_READY;
                                notify();
                            }
                        } else if (b == STX[0]) {
                            if (m_iStatusPrinter == PRINTER_READY) {
                                m_iStatusPrinter = PRINTER_READING;
                            }
                        } else {
                            m_iStatusPrinter = PRINTER_READING;
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }

    }

    //Формирование контрольной суммы
    private byte calcCheckSumCRC(byte[] adata) {
        int isum = 0;
        for (int i = 0; i < adata.length; i++) {
            isum = isum ^ adata[i];
        }
        byte result = (byte) isum;
        return result;
    }

    //Конвертация текста из UTF8 в CP866
    private byte[] UnicodeConverterCP866(String sText) {
        if (sText == null) {
            return new byte[0];
        } else {
            byte[] result = new byte[sText.length()];
            for (int i = 0; i < sText.length(); i++) {
                result[i] = transChar(sText.charAt(i));
            }
            return result;
        }
    }

    //Конвертация таблицы символов из UTF8 в CP866
    private byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
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
            case '\u044A': return (byte) 0xEA;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEC;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

                  default: return 0x3F; // ? Not valid character.
            }
        }
    }
}
```
`DevicePrinterElvesKKM.class`, перечисляем методы действий необходимых для печати чеков согласно установленного шаблона.
```
package com.openbravo.pos.printer.elveskkm;

import java.awt.image.BufferedImage;
import javax.swing.JComponent;

import com.openbravo.pos.printer.*;
import com.openbravo.pos.forms.AppLocal;

public class DevicePrinterElvesKKM implements DevicePrinter {

    private PrinterReaderWritter m_CommOutputPrinter;
    private String m_sName;

    // Creates new TicketPrinter
    public DevicePrinterElvesKKM(String sDevicePrinterPort) throws TicketPrinterException {

        m_sName = AppLocal.getIntString("Printer.Serial");
        m_CommOutputPrinter = new DeviceElvesKKMComm(sDevicePrinterPort);
        m_CommOutputPrinter.sendInitMessage();
        m_CommOutputPrinter.disconnectDevice();
    }

    public String getPrinterName() {
        return m_sName;
    }

    public String getPrinterDescription() {
        return null;
    }

    public JComponent getPrinterComponent() {
        return null;
    }

    public void reset() {
    }

    //Начало печати чека
    public void beginReceipt() {
        try {
            m_CommOutputPrinter.sendInitMessage();
        } catch (TicketPrinterException e) {
        }

    }

    public void printImage(BufferedImage image) {
    }

    public void printBarCode(String type, String position, String code) {
    }

    public void beginLine(int iTextSize) {
    }

    //Печать текста
    public void printText(int iStyle, String sText) {
        try {
            m_CommOutputPrinter.sendTextMessage(sText);
        } catch (TicketPrinterException e) {
        }
    }

    public void endLine() {
    }

    //Окончание печати чека
    public void endReceipt() {
        try {
            m_CommOutputPrinter.sendStampTitleReportMessage();
            m_CommOutputPrinter.sendBeepMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }

    //Открытие денежного ящика
    public void openDrawer() {
        try {
            m_CommOutputPrinter.sendInitMessage();
            m_CommOutputPrinter.sendOpenDrawerMessage();
        } catch (TicketPrinterException e) {
        }
        m_CommOutputPrinter.disconnectDevice();
    }
}
```
`ElvesKKM.java`,  перечисляем константы команд протокола принтера.
```
package com.openbravo.pos.printer.elveskkm;

public class ElvesKKM {

    public static final byte ETX    = 0x03; //Символ конца блока данных
    public static final byte INIT   = 0x3f; //Запрос состояния ККМ
    public static final byte PRN    = 0x4C; //Печать строки текста
    public static final byte BEEP   = 0x47; //Гудок

    public static final byte TITLE  = 0x6C; //Печать клише чека
    public static final byte OPEN_DRAWER  = (byte) 0x80; //Открыть денежный ящик
}
```
### Оптимизация шаблонов ###
Так как в реализованной для ККМ ЭЛВЕС Микро-Ф функции печати вывод производится только текста расположенного между тегов `<text></text>` без использования тегов `<line></line>` на уровне обмена POS-системы с ККМ, необходимо скорректировать шаблоны чеков по приведённому образцу для чека `Printer.TicketPreview.xml`. Также следует установить ширину строки печати в 24 символа, что определённо техническими характеристиками печатающего устройства данной ККМ.
```
<?xml version="1.0" encoding="UTF-8"?>
    <output>
        <ticket>
            <line>
                <text align="center" length="24">************************</text>
            </line>
            <line>
                <text align="center" length="24">Openbravo POS</text>
            </line>
            <line>
                <text align="center" length="24">Добро пожаловать!</text>
            </line>
            <line>
                <text align="center" length="24">************************</text>
            </line>
            <line>
                <text align="left" length="24">Чек: ${ticket.printId()}</text>
            </line>
            <line>
                <text align="left" length="24">Дата: ${ticket.printDate()}</text>
            </line>
            <line>
                <text align ="right" length="24">Кол-во   Цена</text>
            </line>
            <line>
                <text>------------------------</text>
            </line>

            #foreach ($ticketline in $ticket.getLines())
             <line>

             #if ($ticketline.isProductCom())
                <text align ="left" length="24">*${ticketline.printName()}</text>
             #else
                <text align ="left" length="24">${ticketline.printName()}</text>
             #end
             
             </line>
             <line>
                <text align ="right" length="24">${ticketline.printMultiply()} X ${ticketline.printPrice()}</text>
             </line>
             <line>
                <text align ="right" length="24">${ticketline.printSubValue()}</text>
             </line>
            #end

            <line>
                <text>========================</text>
            </line>
            <line>
                <text> </text>
            </line>
            <line>
                <text align ="right" length="24">Кол-во товаров: ${ticket.printArticlesCount()}</text>
            </line>
            <line>
                <text align ="right" length="24">Всего. ${ticket.printSubTotal()}</text>
            </line>
            <line>
                <text align ="right" length="24">Итого. ${ticket.printTotal()}</text>
            </line>
            <line>
                <text align="right" length="24">Кассир: ${ticket.printUser()}</text>
            </line>
        </ticket>
    </output>
```
## Результат ##

### Характеристика тестовой системы ###
  * POS-компьютер CiTAQ EPOS-2000I;
  * ОС Windows 2000;
  * исходный код Openbravo POS версия 2.20;
  * ККМ Штрих-М «ЭЛВЕС Микро-Ф v2.2», подключён к порту COM1;
  * параметры подключения 4800,N,8,1.

### Проверка работы ###
  * Подключить ККМ к компьютеру.
  * Компилировать исходный код программы.
  * Исправить ошибки, повторить компиляцию.
  * Запустить исходный код.
  * Войти в _Управление -> Настройки -> Ресурсы_ заменить шаблона `Printer.TicketPreview.xml` на оптимизированный для ККМ ЭЛВЕС Микро-Ф.
  * Сохранить шаблон примера чека.
  * Перейти в _Система -> Оборудование -> Настройки_, для _Принтер 1_ из списка выбрать `elveskkm`, и указать тип подключения `serial` и порт подключения.
  * Сохранить параметры POS терминала.
  * Перезапустить программу.
  * Перейти в _Действия -> Продажи_.
  * Ввести товар для продажи.
  * Нажать _Печать_.
  * Начнётся печать примера чека.
  * Дождаться звукового сигнала от ККМ и проверить полученный пример чека.
  * Нажать _Открыть денежный ящик_.
  * Дождаться открытия денежного ящика.

### Примеры работы ###
Подключение ККМ ЭЛВЕС Микро-Ф к Openbravo POS в качестве чекового принтера.
| ![http://farm4.static.flickr.com/3581/3329584887_639f8e0b41_m.jpg](http://farm4.static.flickr.com/3581/3329584887_639f8e0b41_m.jpg) | ![http://farm4.static.flickr.com/3339/3329584889_159092e72f_m.jpg](http://farm4.static.flickr.com/3339/3329584889_159092e72f_m.jpg) |
|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|

Пример чека полученного в результате подключения ККМ ЭЛВЕС Микро-Ф к Openbravo POS.
| ![![](http://farm4.static.flickr.com/3645/3341787186_f6bdd16794_m.jpg)](http://farm4.static.flickr.com/3645/3341787186_9d928cf933_o.png) |
|:-----------------------------------------------------------------------------------------------------------------------------------------|

### Нерешённые задачи ###
Так как представленное решения призвано только проиллюстрировать возможности по подключению к Openbravo POS нестандартного оборудования, требуется доработка представленного кода в следующих направлениях:

  * добавить в `serialEvent()` метод приёма и проверки байта `CRC` получаемого в конце сообщения от ККМ;
  * реализовать функцию приёма и реакции POS-системы на сообщения от ККМ;
  * добавить метод маскировки байт `ETX` и `DLE` в входящих и исходящих сообщениях согласно спецификации.

Также представленное выше решение может быть использовано для подключения других марок ККМ работающих под данному протоколу (например ШТРИХ-Микро-Ф и ТВЕС-Мини-Ф), а изложенные в нём принципы также позволяют разработать решения для подключения к Openbravo POS фискальных регистраторов в качестве принтеров чеков (например Штрих-ФР-Ф и Феликс-Р-Ф).

## Исходный код ##
[Изменённые файлы исходного кода для Openbravo POS 2.20](http://openbravoposru.googlecode.com/files/OpenbravoPOS-ELVESMIKROECR.zip). Для установки файлы следует скопировать в каталоги с исходным кодом Openbravo POS 2.20:

  * `JPanelConfigGeneral.java` в `../com/openbravo/pos/config/`
  * `DeviceTicket.java` в `../com/openbravo/pos/printer/`
  * папку `elveskkm` в `../com/openbravo/pos/printer/`

Также исходный код включён в [репозитарий данного проекта с ревизии 95](http://code.google.com/p/openbravoposru/source/list).

