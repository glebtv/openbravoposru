Реализация функций загрузки наименование товара, штрих-кода, цены и других данных из базы данных [Openbravo POS](http://ru.wikipedia.org/wiki/Openbravo_POS) в память ([ПЗУ](http://ru.wikipedia.org/wiki/%D0%9F%D0%97%D0%A3)) [ККМ Меркурий 130Ф](http://www.incotex.ru/incotex/kkm_130.html).

## Преамбула ##
Существует большой класс торгового оборудования хранящего информацию о товаре в ПЗУ устройства. Чаще всего это весы с печатью штрих-этикетов, терминалы сбора данных, контрольно-кассовые машины. В большинстве случаев информация о товаре содержит наименование товара, его цену и штрих-код.

Для идентификации и удобства обращения каждому позиции товара в памяти устройства присваивается идентификационный код, чаще всего называемый [PLU](http://en.wikipedia.org/wiki/Price_Look-Up). Сокращение PLU расшифровывается, как Price-Look Up, и применяется для маркировки свежих фруктов, овощей и зелени. Структура данного кода регламентировано соответствующим [стандартом IFPC](http://www.plucodes.com), согласно ней код PLU должен содержать в определённо последовательности 4-5 знака. Но в данный момент в среде разработчиков и интеграторов торгового оборудования так сложилось, что PLU обозначает код товара во внутренней базе устройства, присваивается любому типу товара и не соответствует установленным стандартам.

Предлагаемое ниже решение является примером использования Openbravo POS в качестве системы хранения и загрузки информации о товарах в памяти внешних устройств. Openbravo POS версии 2.20 поддерживается только один вид оборудования с загрузкой информации о товаров, это терминал сбора данных [Metrologic ScanPal®2](http://www.metrologic.com/corporate/products/mobilecomputers/scanpal2/). По предложенной схеме средствами Openbravo POS могут быть внедрены решения и для других видов устройств.

Выражаю благодарность [компании "Инкотекс"](http://www.incotex.ru), и лично Гончарову Виталию и Бушину Сергею, за предоставленную спецификацию на данный тип ККМ.

## Постановка задачи ##
Обмен компьютера с ККМ производится по [последовательному порту](http://ru.wikipedia.org/wiki/%D0%9F%D0%BE%D1%81%D0%BB%D0%B5%D0%B4%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C%D0%BD%D1%8B%D0%B9_%D0%BF%D0%BE%D1%80%D1%82).

Цикл обмена POS системы и ККМ Меркурий 130Ф состоит из следующих этапов:
  * перевод ККМ в режим приёма данных;
  * передача информации о товарах из POS системы в ККМ по позициям;
  * завершение сеанса обмена передачей с POS системы комманды на прерывания сеанса связи.

Ниже приведена схема алгоритма обмена информацией POS системы с ККМ.

```
Структура протокола записи товара из POS во внутреннюю базу ККМ

Посылка с POS:
для записи товара во внутренюю базу ККМ посылается 48 байт сообщения.
<PLU|0x80|0x80|NAME|PRICE|GROUP|SEC|BCD|0x00|ATR1|ATR2|0x00|0x00|0x00|CRC>

для записи товара в буфер внутреней базы ККМ посылается 48 байт сообщения.
<PLU|0xС0|PRM|NAME|PRICE|GROUP|SEC|BCD|0x00|ATR1|ATR2|0x00|0x00|0x00|CRC>

где PLU — код товара в базе ККМ, 2 байта;
    PRM — признак записи, 1 байт;
    NAME — наименование товара, 24 байта;
    PRICE — цена реализации товара включая налоги, 4 байта;
    GROUP — группа товара, 1 байт;
    SEC — секция товара, 1 байт;
    BCD — штрих-код товара, 7 байт;
    ATR1 — атрибут разрешения продаж товара, 1 байт;
    ATR2 — атрибут разрешения быстрой продажи товара, 1 байт;
    CRC — контрольная сумма пакета данных, 1 байт.

Ответ ККМ:
если запись выполнена - ответ 2 байта сообщения <0xF0|0x0F>.
если возникла ошибка  - ответ 2 байта сообщения <0x55|0xAA>.
```

Согласно представленной схемы предлагается из списка товаров формируемого Openbravo POS в панели _Товары_ передавать информацию для записи в ПЗУ ККМ. Перед передачей сообщения требуется конвертировать данные из формата Openbravo POS в соответствующий формат хранения данных в ККМ Меркурий 130Ф.

## Решение задачи ##

### Необходимые ресурсы ###
Для разработки вам понадобится:
  * установленная среда разработки [Netbeans IDE 6.5](http://www.netbeans.org/);
  * [исходный код Openbravo POS 2.20](http://downloads.sourceforge.net/openbravopos/openbravopos_2.20_src.zip?modtime=1219951842&big_mirror=0);
  * ККМ Меркурий 130Ф подключённый к компьютеру.

### Изменения исходного кода ###
За неимением специального раздела (если необходимо, то можно его создать по аналогии с другими) добавляем в меню настройки конфигурации новый вид сканера. Вносим наименование нового устройства `mercury130kz039` в `JPanelConfigGeneral.java`, также вносим изменения в исходный для сохранения информации о ККМ в файле настроек `openbravopos.properties`.
```
...
package com.openbravo.pos.config;
...
        // Device with PLUs
        jcboMachineScanner.addItem("scanpal2");
        jcboMachineScanner.addItem("mercury130kz039");
        jcboMachineScanner.addItem("Not defined");
...
        p = new StringParser(config.getProperty("machine.scanner"));
        sparam = p.nextToken(':');
        jcboMachineScanner.setSelectedItem(sparam);
        if ("scanpal2".equals(sparam) || "mercury130kz039".equals(sparam)) {
            jcboSerialScanner.setSelectedItem(p.nextToken(','));
        }    
...
        // Device with PLUs
        String sMachineScanner = comboValue(jcboMachineScanner.getSelectedItem());
        if ("scanpal2".equals(sMachineScanner) || "mercury130kz039".equals(sMachineScanner)) {
            config.setProperty("machine.scanner", sMachineScanner + ":" + comboValue(jcboSerialScanner.getSelectedItem()));
        } else {
            config.setProperty("machine.scanner", sMachineScanner);
        }
...
    private void jcboMachineScannerActionPerformed(java.awt.event.ActionEvent evt) {                                                   
        CardLayout cl = (CardLayout)(m_jScannerParams.getLayout());
        if ("scanpal2".equals(jcboMachineScanner.getSelectedItem()) || "mercury130kz039".equals(jcboMachineScanner.getSelectedItem())) {
            cl.show(m_jScannerParams, "comm");
        } else {
            cl.show(m_jScannerParams, "empty");
        }
    }  
...
```

Добавляем в `JPanelTable.java` инициализацию дополнительной панели `Mercury130` окна _Товары_.
```
...
package com.openbravo.pos.panels;
...
            // add toolbar extras
            c = getToolbarExtrasScanPal();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                toolbar.add(c);
            }

            c = getToolbarExtrasMercury130();
            if (c != null) {
                c.applyComponentOrientation(getComponentOrientation());
                toolbar.add(c);
            }
...
    public Component getToolbarExtrasScanPal() {
        return null;
    }

    public Component getToolbarExtrasMercury130() {
        return null;
    }

...
```

Вносим изменения в пакет `com.openbravo.pos.inventory`. Изменяем класс `ProductsPanel.java`.
```
...
package com.openbravo.pos.inventory;
...
    public Component getToolbarExtrasScanPal() {
...
    @Override
    public Component getToolbarExtrasMercury130() {

        JButton btnMercury130 = new JButton();
        btnMercury130.setText("Mercury130");
        btnMercury130.setVisible(app.getDeviceMercury130() != null);
        btnMercury130.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMercury130ActionPerformed(evt);
            }
        });

        return btnMercury130;
    }
...
    private void btnMercury130ActionPerformed(java.awt.event.ActionEvent evt) {
        JDlgUploadProductsMercury130.showMessage(this, app.getDeviceMercury130(), bd);
    }
...
```

Добавляем класс `JDlgUploadProductsMercury130.java`, с окном диалога передачи информации по товарам.
```
package com.openbravo.pos.inventory;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.ListModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.user.BrowsableEditableData;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.mercury130.DeviceMercury130;
import com.openbravo.pos.mercury130.DeviceMercury130Exception;

public class JDlgUploadProductsMercury130 extends javax.swing.JDialog {
    private DeviceMercury130 m_passivecr;
    private BrowsableEditableData m_bd;
    
    private JDlgUploadProductsMercury130(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    private JDlgUploadProductsMercury130(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }       

    private void init(DeviceMercury130 passivecr, BrowsableEditableData bd) {
        initComponents();
        getRootPane().setDefaultButton(jcmdOK);   
        m_passivecr = passivecr;
        m_bd = bd;
        setVisible(true);
    }
    
    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window)parent;
        } else {
            return getWindow(parent.getParent());
        }
    }    
    
    public static void showMessage(Component parent, DeviceMercury130 passivecr, BrowsableEditableData bd) {
        Window window = getWindow(parent);      
        JDlgUploadProductsMercury130 myMsg;
        if (window instanceof Frame) { 
            myMsg = new JDlgUploadProductsMercury130((Frame) window, true);
        } else {
            myMsg = new JDlgUploadProductsMercury130((Dialog) window, true);
        }
        myMsg.init(passivecr, bd);
    }         
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabelMercury130 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("caption.upload")); // NOI18N
        setResizable(false);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdOK);

        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel2.add(jcmdCancel);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(null);

        jLabelMercury130.setText(AppLocal.getIntString("message.preparemercury130")); // NOI18N
        jLabelMercury130.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabelMercury130.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabelMercury130);
        jLabelMercury130.setBounds(10, 10, 440, 70);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pos_messages"); // NOI18N

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-474)/2, (screenSize.height-161)/2, 474, 161);
    }// </editor-fold>                        

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {                                           
        dispose();
    }                                          

    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {                                       

        String stext = jLabelMercury130.getText();
        jLabelMercury130.setText(AppLocal.getIntString("label.uploadingproducts"));
        jLabelMercury130.doLayout();
        jLabelMercury130.setEnabled(true);
        jLabelMercury130.setVisible(true);
        jcmdOK.setEnabled(false);
        jcmdCancel.setEnabled(false);

        try {
            m_passivecr.connectDevice();
            m_passivecr.startUploadProduct();
            ListModel l = m_bd.getListModel();
            int size = l.getSize();
            for (int i = 0; i < size; i++) {
                Object[] myprod = (Object[]) l.getElementAt(i);
                m_passivecr.sendProduct(
                        (String) myprod[3], // name
                        (String) myprod[2], // barcode
                        (Double) myprod[7]); // sell price
            }
            m_passivecr.stopUploadProduct();
            MessageInf msg = new MessageInf(MessageInf.SGN_SUCCESS, AppLocal.getIntString("message.scannerok"));
            m_passivecr.disconnectDevice();
            msg.show(this);
        } catch (DeviceMercury130Exception e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.scannerfail"), e);
            msg.show(this);
        }

        jLabelMercury130.setText(stext);
        jcmdOK.setEnabled(true);
        jcmdCancel.setEnabled(true);
        dispose();
    }                                      

    
    // Variables declaration - do not modify                     
    private javax.swing.JLabel jLabelMercury130;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    // End of variables declaration                   
    
}
```
И шаблон с описанием интерфейса к нему.
```
<?xml version="1.0" encoding="UTF-8" ?>

<Form version="1.2" maxVersion="1.2" type="org.netbeans.modules.form.forminfo.JDialogFormInfo">
  <Properties>
    <Property name="defaultCloseOperation" type="int" value="2"/>
    <Property name="title" type="java.lang.String" editor="org.netbeans.modules.i18n.form.FormI18nStringEditor">
      <ResourceString bundle="pos_messages.properties" key="caption.upload" replaceFormat="AppLocal.getIntString(&quot;{key}&quot;)"/>
    </Property>
    <Property name="resizable" type="boolean" value="false"/>
  </Properties>
  <SyntheticProperties>
    <SyntheticProperty name="formSize" type="java.awt.Dimension" value="-84,-19,0,5,115,114,0,18,106,97,118,97,46,97,119,116,46,68,105,109,101,110,115,105,111,110,65,-114,-39,-41,-84,95,68,20,2,0,2,73,0,6,104,101,105,103,104,116,73,0,5,119,105,100,116,104,120,112,0,0,0,-95,0,0,1,-38"/>
    <SyntheticProperty name="formSizePolicy" type="int" value="0"/>
    <SyntheticProperty name="generateSize" type="boolean" value="true"/>
    <SyntheticProperty name="generateCenter" type="boolean" value="true"/>
  </SyntheticProperties>
  <AuxValues>
    <AuxValue name="FormSettings_autoResourcing" type="java.lang.Integer" value="0"/>
    <AuxValue name="FormSettings_autoSetComponentName" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_generateFQN" type="java.lang.Boolean" value="true"/>
    <AuxValue name="FormSettings_generateMnemonicsCode" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_i18nAutoMode" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_listenerGenerationStyle" type="java.lang.Integer" value="0"/>
    <AuxValue name="FormSettings_variablesLocal" type="java.lang.Boolean" value="false"/>
    <AuxValue name="FormSettings_variablesModifier" type="java.lang.Integer" value="2"/>
  </AuxValues>

  <Layout class="org.netbeans.modules.form.compat2.layouts.DesignBorderLayout"/>
  <SubComponents>
    <Container class="javax.swing.JPanel" name="jPanel2">
      <Constraints>
        <Constraint layoutClass="org.netbeans.modules.form.compat2.layouts.DesignBorderLayout" value="org.netbeans.modules.form.compat2.layouts.DesignBorderLayout$BorderConstraintsDescription">
          <BorderConstraints direction="South"/>
        </Constraint>
      </Constraints>

      <Layout class="org.netbeans.modules.form.compat2.layouts.DesignFlowLayout">
        <Property name="alignment" type="int" value="2"/>
      </Layout>
      <SubComponents>
        <Component class="javax.swing.JButton" name="jcmdOK">
          <Properties>
            <Property name="icon" type="javax.swing.Icon" editor="org.netbeans.modules.form.editors2.IconEditor">
              <Image iconType="3" name="/com/openbravo/images/button_ok.png"/>
            </Property>
            <Property name="text" type="java.lang.String" editor="org.netbeans.modules.i18n.form.FormI18nStringEditor">
              <ResourceString bundle="pos_messages.properties" key="Button.OK" replaceFormat="AppLocal.getIntString(&quot;{key}&quot;)"/>
            </Property>
          </Properties>
          <Events>
            <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="jcmdOKActionPerformed"/>
          </Events>
        </Component>
        <Component class="javax.swing.JButton" name="jcmdCancel">
          <Properties>
            <Property name="icon" type="javax.swing.Icon" editor="org.netbeans.modules.form.editors2.IconEditor">
              <Image iconType="3" name="/com/openbravo/images/button_cancel.png"/>
            </Property>
            <Property name="text" type="java.lang.String" editor="org.netbeans.modules.i18n.form.FormI18nStringEditor">
              <ResourceString bundle="pos_messages.properties" key="Button.Cancel" replaceFormat="AppLocal.getIntString(&quot;{key}&quot;)"/>
            </Property>
          </Properties>
          <Events>
            <EventHandler event="actionPerformed" listener="java.awt.event.ActionListener" parameters="java.awt.event.ActionEvent" handler="jcmdCancelActionPerformed"/>
          </Events>
        </Component>
      </SubComponents>
    </Container>
    <Container class="javax.swing.JPanel" name="jPanel1">
      <Constraints>
        <Constraint layoutClass="org.netbeans.modules.form.compat2.layouts.DesignBorderLayout" value="org.netbeans.modules.form.compat2.layouts.DesignBorderLayout$BorderConstraintsDescription">
          <BorderConstraints direction="Center"/>
        </Constraint>
      </Constraints>

      <Layout class="org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout">
        <Property name="useNullLayout" type="boolean" value="true"/>
      </Layout>
      <SubComponents>
        <Component class="javax.swing.JLabel" name="jLabelMercury130">
          <Properties>
            <Property name="text" type="java.lang.String" editor="org.netbeans.modules.i18n.form.FormI18nStringEditor">
              <ResourceString bundle="pos_messages.properties" key="message.preparescanner" replaceFormat="AppLocal.getIntString(&quot;{key}&quot;)"/>
            </Property>
            <Property name="horizontalTextPosition" type="int" value="10"/>
            <Property name="verticalTextPosition" type="int" value="1"/>
          </Properties>
          <AccessibilityProperties>
            <Property name="AccessibleContext.accessibleName" type="java.lang.String" editor="org.netbeans.modules.i18n.form.FormI18nStringEditor">
              <ResourceString bundle="pos_messages.properties" key="message.preparemercury130" replaceFormat="java.util.ResourceBundle.getBundle(&quot;{bundleNameSlashes}&quot;).getString(&quot;{key}&quot;)"/>
            </Property>
          </AccessibilityProperties>
          <Constraints>
            <Constraint layoutClass="org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout" value="org.netbeans.modules.form.compat2.layouts.DesignAbsoluteLayout$AbsoluteConstraintsDescription">
              <AbsoluteConstraints x="10" y="10" width="440" height="70"/>
            </Constraint>
          </Constraints>
        </Component>
      </SubComponents>
    </Container>
  </SubComponents>
</Form>
```

Создаём пакет `com.openbravo.pos.mercury130` в котором располагаем следующие классы:

`DeviceMercury130.class`, перечисляем методы необходимые для работы ККМ в режиме приёма списка товаров.
```
package com.openbravo.pos.mercury130;

public interface DeviceMercury130 {
    public void connectDevice() throws DeviceMercury130Exception;
    public void disconnectDevice();
    public void startUploadProduct() throws DeviceMercury130Exception;
    public void sendProduct(String sName, String sCode, Double dPrice) throws DeviceMercury130Exception;
    public void stopUploadProduct() throws DeviceMercury130Exception;
}
```
`DeviceMercury130Comm.java`, устанавливаем процедуры необходимые для выполнения методов класса `DeviceMercury130.class`.
```
package com.openbravo.pos.mercury130;

import gnu.io.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TooManyListenersException;

public class DeviceMercury130Comm implements DeviceMercury130, SerialPortEventListener {

    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;

    private String m_sPort;
    private OutputStream m_out;
    private InputStream m_in;

    private static final byte[] COMMAND_ACK = new byte[] {(byte) 0xf0, (byte) 0x0f};
    private static final byte[] COMMAND_OFF = new byte[] {0x00, 0x00, (byte) 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x34};
    private static final byte[] COMMAND_BUFF = new byte[] {0x03, 0x00, (byte) 0xc0, 0x01, (byte) 0x42, (byte) 0x6f, (byte) 0x78, 0x20, (byte) 0x80, (byte) 0x2d, (byte) 0xbf, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, (byte) 0xdc, (byte) 0x41, (byte) 0x0f, 0x00, 0x08, 0x0f, (byte) 0x84, 0x39, 0x50, 0x35, 0x00, (byte) 0x90, (byte) 0xf8, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, (byte) 0x79};
    private static final byte[] COMMAND_W_IN = new byte[] {(byte) 0x80, (byte) 0x80};

    private Queue<byte[]> m_aLines;
    private ByteArrayOutputStream m_abuffer;

    private int m_iProductOrder;

    DeviceMercury130Comm(String sPort) {
        m_sPort = sPort;
        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    public void connectDevice() throws DeviceMercury130Exception {
        try {
            m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPort);
            m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000);
            m_out = m_CommPortPrinter.getOutputStream();
            m_in = m_CommPortPrinter.getInputStream();
            m_CommPortPrinter.addEventListener(this);
            m_CommPortPrinter.notifyOnDataAvailable(true);
            m_CommPortPrinter.setSerialPortParams(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
        } catch (Exception e) {
            m_PortIdPrinter = null;
            m_CommPortPrinter = null;
            m_out = null;
            m_in = null;
            throw new DeviceMercury130Exception(e);
        }

        synchronized (this) {
            m_aLines = new LinkedList<byte[]>();
            m_abuffer = new ByteArrayOutputStream();
        }
    }

    public void disconnectDevice() {

        try {
            m_out.close();
            m_in.close();
            m_CommPortPrinter.close();
        } catch (IOException e) {
        }

        synchronized (this) {
            m_aLines = null;
            m_abuffer = null;
        }

        m_PortIdPrinter = null;
        m_CommPortPrinter = null;
        m_out = null;
        m_in = null;
    }

    public void startUploadProduct() throws DeviceMercury130Exception {
        writeLine(COMMAND_BUFF);
        readCommand(COMMAND_ACK);
        m_iProductOrder = 0;
    }

    public void stopUploadProduct() throws DeviceMercury130Exception {
        writeLine(COMMAND_OFF);
    }

    public void sendProduct(String sName, String sCode, Double dPrice) throws DeviceMercury130Exception {

      m_iProductOrder++;

        ByteArrayOutputStream lineout = new ByteArrayOutputStream();
        try{
            //Номер товара в диапозоне. Смещение 0. Длина 2 байта.
            if (m_iProductOrder > 0 || m_iProductOrder <= 9999) {
                lineout.write(convertHEX(m_iProductOrder), 0, 2);
            } else {
                lineout.write(0x00);
                lineout.write(0x00);
            }

            // Признак записи товара. Смещение 2. Длина 2 байта.
            lineout.write(COMMAND_W_IN, 0, 2);

            // Наименование товара. Смещение 4. Длина 24.
            for (int i = sName.length(); i < 24; i++) {
                sName = sName + " ";
            }
            lineout.write(convertASCII(sName), 0, 24);

            // Цена товара. Смещение 28. Длина 4.
            lineout.write(convertHEX((long) (dPrice * 100)), 0, 4);

            // Налоговая группа товара. Смещение 32. Длина 1.
            lineout.write(0x01);

            // Секция товара. Смещение 33. Длина 1.
            lineout.write(0x01);

            // Штрих-кода товара. Смещение 34. Длина 7.
            lineout.write(convertBCD(sCode), 0, 7);

            lineout.write(0x00);

            // Атрибут разрешения продажи товара. Смещение 43. Длина 1.
            lineout.write(0x01);

            // Атрибут быстрой продажи товара. Смещение 44. Длина 1.
            lineout.write(0x01);

            lineout.write(0x00);
            lineout.write(0x00);
            lineout.write(0x00);

            // Контрольная сумма. Смещение 47. Длина 1.
            lineout.write(calcCheckSum(lineout.toByteArray()));
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        writeLine(lineout.toByteArray());
        lineout = null;
        readCommand(COMMAND_ACK);
    }

    private void readCommand(byte[] cmd) throws DeviceMercury130Exception {
        byte[] b = readLine();
        if (!checkCommand(cmd, b)) {
            throw new DeviceMercury130Exception("Command not expected");
        }
    }

    private void writeLine(byte[] aline) throws DeviceMercury130Exception {
        if (m_CommPortPrinter == null) {
            throw new DeviceMercury130Exception("No Serial port opened");
        } else {
            synchronized (this) {
                try {
                    m_out.write(aline);
                    m_out.flush();
                } catch (IOException e) {
                    throw new DeviceMercury130Exception(e);
                }
            }
        }
    }

    private byte[] readLine() throws DeviceMercury130Exception {
        synchronized (this) {

            if (!m_aLines.isEmpty()) {
                return m_aLines.poll();
            }

            try {
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_aLines.isEmpty()) {
                throw new DeviceMercury130Exception("Timeout");
            } else {
                return m_aLines.poll();
            }
        }
    }

    private static byte[] convertHEX(long sdata) {

        byte[] result = new byte[24];

        if (sdata <= 255.00 && sdata >= 0) {
            result[0] = (byte) sdata;
        } else {
            int j = 0;
            String buff = Long.toHexString(Long.reverseBytes(sdata));
            for (int i = 0; i < buff.length() / 2; i++) {
                result[i] = (byte) Integer.parseInt(buff.substring(j, j + 2), 16);
                j = j + 2;
            }
        }
        return result;
    }

    private static byte[] convertASCII(String sdata) {
        byte[] result = new byte[sdata.length()];
        for (int i = 0; i < sdata.length(); i++) {
            char c = sdata.charAt(i);
            if ((c >= 0x0020) && (c < 0x0080)) {
                result[i] = (byte) c;
            } else {
                result[i] = convertCyrillic(c);
            }
        }
        return result;
    }

    private static byte convertCyrillic(char sdata) {
        switch (sdata) {

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
            case '\u0428': return (byte) 0x98;// Ш
            case '\u0429': return (byte) 0x99;// Щ
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
            case '\u0440': return (byte) 0xB0;// р
            case '\u0441': return (byte) 0xB1;// с
            case '\u0442': return (byte) 0xB2;// т
            case '\u0443': return (byte) 0xB3;// у
            case '\u0444': return (byte) 0xB4;// ф
            case '\u0445': return (byte) 0xB5;// х
            case '\u0446': return (byte) 0xB6;// ц
            case '\u0447': return (byte) 0xB7;// ч
            case '\u0448': return (byte) 0xB8;// ш
            case '\u0449': return (byte) 0xB9;// щ
            case '\u044A': return (byte) 0xBA;// ъ
            case '\u044B': return (byte) 0xBB;// ы
            case '\u044C': return (byte) 0xBC;// ь
            case '\u044D': return (byte) 0xBD;// ы
            case '\u044E': return (byte) 0xBE;// ю
            case '\u044F': return (byte) 0xBF;// я

            default: return (byte) 0x3F; // ? Not valid character.
        }
    }

    private static byte[] convertBCD(String sdata) {
        int j = 0;
        int x = sdata.length();
        if (x != 13) {
            sdata = "FFFFFFFFFFFFF";
        } else if (x == 8) {
            sdata = sdata + "FFFFF";
        }
        sdata = sdata + "F";
        byte[] result = new byte[sdata.length() / 2];
        for (int i = 0; i < sdata.length(); i++) {
            result[j] = (byte) Integer.parseInt(sdata.substring(i + 1, i + 2) + sdata.substring(i, i + 1), 16);
            j = j + 1;
            i++;
        }
        return result;
    }

    private byte[] calcCheckSum(byte[] adata) {
        int isum = 0;
        for (int i = 0; i < adata.length; i++) {
            byte b = (byte) (adata[i] ^ i);
            isum = isum + b;
        }
        byte low = (byte) (isum & 0x00FF);
        byte[] result = new byte[2];
        result[0] = low;
        return result;
    }

    private boolean checkCommand(byte[] bcommand, byte[] brecieved) {
        if (bcommand.length == brecieved.length) {
            for (int i = 0; i < bcommand.length; i++) {
                if (bcommand[i] != brecieved[i]) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
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
                        int b = m_in.read();
                        synchronized(this) {
                            if (b == 0x0F) {
                                m_abuffer.write(b);
                                m_aLines.add(m_abuffer.toByteArray());
                                m_abuffer.reset();
                                notifyAll();
                            } else {
                                m_abuffer.write(b);
                            }
                        }
                    }
                } catch (IOException eIO) {}
                break;
        }
    }
}
```
`DeviceMercury130Factory.class`, реализуем метод определения типа подключённого устройства, метод `DeviceMercury130Comm()` может отличатся от версии установленной прошивки в ККМ.
```
package com.openbravo.pos.mercury130;

import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.util.StringParser;

public class DeviceMercury130Factory {
    
    private DeviceMercury130Factory() {
    }
    
    public static DeviceMercury130 createInstance(AppProperties props) {
        
        StringParser sd = new StringParser(props.getProperty("machine.scanner"));
        String sPassiveCRType = sd.nextToken(':');
        String sPassiveCRParam1 = sd.nextToken(',');

        if ("mercury130kz039".equals(sPassiveCRType)) {
            return new DeviceMercury130Comm(sPassiveCRParam1);
        } else {
            return null;
        }
    }
}
```
`DeviceMercury130Exception.class`, реакция на ошибки возникшие во время выполнения методов пакетов.
```
package com.openbravo.pos.mercury130;

public class DeviceMercury130Exception extends java.lang.Exception {
    
    public DeviceMercury130Exception() {
    }
    
    public DeviceMercury130Exception(String msg) {
        super(msg);
    }

    public DeviceMercury130Exception(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public DeviceMercury130Exception(Throwable cause) {
        super(cause);
    }
}
```

Объявляем полученные методы в следующих классах:

`AppView.class` и вносим изменения.
```
...
package com.openbravo.pos.forms;
...
import com.openbravo.pos.mercury130.DeviceMercury130;
...
    public DeviceScanner getDeviceScanner();
    public DeviceMercury130 getDeviceMercury130();
...
```
`JRootApp.class` и вносим изменения.
```
...
package com.openbravo.pos.forms;
...
import com.openbravo.pos.mercury130.DeviceMercury130;
import com.openbravo.pos.mercury130.DeviceMercury130Factory;
...
    private DeviceScanner m_Scanner;
    private DeviceMercury130 m_Mercury130;
...
        // Inicializamos la scanpal
        m_Scanner = DeviceScannerFactory.createInstance(m_props);

        m_Mercury130 = DeviceMercury130Factory.createInstance(m_props);
            
...
```
`JFrmSQL.class` и вносим изменения.
```
...
package com.openbravo.pos.sql;
...
import com.openbravo.pos.mercury130.DeviceMercury130;
...
    public DeviceScanner getDeviceScanner() {
        return null;
    }

    public DeviceMercury130 getDeviceMercury130() {
        return null;
    }
...
```

Также при создании диалогов в `JPanelTable.java` были созданы новые текстовые поля, которые следует локализовать.
Для английского языка добавляем в `pos_messages.properties`.
```
...
message.preparemercury130=Prepare the Mercury130 to upload the products list and press OK.
label.uploadingproducts=Uploading products... Please wait
```
Для русского языка добавляем в `pos_messages_ru.properties`.
```
...
message.preparemercury130=Приготовьте Меркурий130 для загрузки данных по товарам и нажмите кнопку Принять.
label.uploadingproducts=Производится выгрузка товаров. Пожалуйста ждите...
```

## Результат ##

### Характеристика тестовой системы ###
  * компьютер на базе системной платы MSI 975X Platinum Powerup Edition;
  * ОС Ubuntu 8.04 LTS;
  * исходный код Openbravo POS версия 2.20;
  * сканер штрих-кодов Metrologic MS9520 Voyager® с подключением по интерфейсу RS232.

### Проверка работы ###
  * Подключить ККМ к компьютеру.
  * Компилировать исходный код программы.
  * Исправить ошибки, повторить компиляцию.
  * Запустить исходный код.

#### Проверка передачи данных из POS в ККМ ####
  * Номенклатурная позиция по товару предварительно должна быть зарегистрирована в системе под кодом с этикетки.

#### Проверка правильности загруженных данных в ККМ ####
  * Подключить сканер штрих-кодов к порту RS232 ККМ.
  * Войти в режим регистрацим продаж ККМ.
  * Провести сканером штрих-кода по этикетки товара.
  * Если товар зарегистрирован в ККМ, должен прозвучать звуковой сигнал и появится цена товара на дисплее ККМ.
  * Закрыть чек в ККМ и дождаться распечатки чека.
  * Проверить наименование товара на чеке.

### Примеры работы ###
Загрузка базы данных по товарам в ККМ Меркурий 130Ф и регистрация продаж по PLU.

| ![http://farm4.static.flickr.com/3287/3018418551_62302acf43_m.jpg](http://farm4.static.flickr.com/3287/3018418551_62302acf43_m.jpg) | ![http://farm4.static.flickr.com/3148/3018418557_148cf5946b_m.jpg](http://farm4.static.flickr.com/3148/3018418557_148cf5946b_m.jpg) |
|:------------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------------|

### Нерешённые задачи ###

## Исходный код ##
Исходный код включён в [репозитарий данного проекта с ревизии 119](http://code.google.com/p/openbravoposru/source/list).