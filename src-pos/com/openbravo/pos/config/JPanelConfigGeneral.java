//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.
package com.openbravo.pos.config;

import com.openbravo.data.user.DirtyManager;
import java.awt.CardLayout;
import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.util.ReportUtils;
import com.openbravo.pos.util.StringParser;
import java.util.Map;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.SubstanceSkin;
import org.jvnet.substance.skin.SkinInfo;

/**
 *
 * @author adrianromero
 */
public class JPanelConfigGeneral extends javax.swing.JPanel implements PanelConfig {

    private DirtyManager dirty = new DirtyManager();

    private ParametersConfig printer1printerparams;

    private ParametersConfig printer2printerparams;

    private ParametersConfig printer3printerparams;

//    private ParametersConfig fiscalprinterparams;
    
    String[] modelSerialPortSpeed = {"2400","4800","9600","19200","38400","57600","115200"}; //NOI18N
    String[] modelSerialPortDataBits = {"5","6","7","8"};     //NOI18N
    String[] modelSerialPortStopBits = {"1","2"};     //NOI18N
    String[] modelSerialPortParity = {"none","even","odd"}; //NOI18N
    
    /** Creates new form JPanelConfigGeneral */
    public JPanelConfigGeneral() {

        initComponents();

        String[] printernames = ReportUtils.getPrintNames();

        jtxtMachineHostname.getDocument().addDocumentListener(dirty);
        jcboLAF.addActionListener(dirty);
        jcboMachineScreenmode.addActionListener(dirty);
        jcboTicketsBag.addActionListener(dirty);

        jcboMachineDisplay.addActionListener(dirty);
        jcboConnDisplay.addActionListener(dirty);
        jcboSerialDisplay.addActionListener(dirty);
        m_jtxtJPOSName.getDocument().addDocumentListener(dirty);

        jcboMachinePrinter.addActionListener(dirty);
        jcboConnPrinter.addActionListener(dirty);
        jcboSerialPrinter.addActionListener(dirty);
        m_jtxtJPOSPrinter.getDocument().addDocumentListener(dirty);
        m_jtxtJPOSDrawer.getDocument().addDocumentListener(dirty);
        
        printer1printerparams = new ParametersPrinter(printernames);
        printer1printerparams.addDirtyManager(dirty);
        m_jPrinterParams1.add(printer1printerparams.getComponent(), "printer");

        jcboMachinePrinter2.addActionListener(dirty);
        jcboConnPrinter2.addActionListener(dirty);
        jcboSerialPrinter2.addActionListener(dirty);
        m_jtxtJPOSPrinter2.getDocument().addDocumentListener(dirty);
        m_jtxtJPOSDrawer2.getDocument().addDocumentListener(dirty);

        printer2printerparams = new ParametersPrinter(printernames);
        printer2printerparams.addDirtyManager(dirty);
        m_jPrinterParams2.add(printer2printerparams.getComponent(), "printer");

        jcboMachinePrinter3.addActionListener(dirty);
        jcboConnPrinter3.addActionListener(dirty);
        jcboSerialPrinter3.addActionListener(dirty);
        m_jtxtJPOSPrinter3.getDocument().addDocumentListener(dirty);
        m_jtxtJPOSDrawer3.getDocument().addDocumentListener(dirty);

        printer3printerparams = new ParametersPrinter(printernames);
        printer3printerparams.addDirtyManager(dirty);
        m_jPrinterParams3.add(printer3printerparams.getComponent(), "printer");

        jcboMachineFiscalPrinter.addActionListener(dirty);
        jcboConnFiscalPrinter.addActionListener(dirty);
        jcboSerialFiscalPrinter.addActionListener(dirty);
        m_jtxtJPOSFiscalPrinter.getDocument().addDocumentListener(dirty);
        m_jtxtJPOSFiscalPrinter.getDocument().addDocumentListener(dirty);

        jcboMachineScale.addActionListener(dirty);
        jcboSerialScale.addActionListener(dirty);

        jcboMachineScanner.addActionListener(dirty);
        jcboSerialScanner.addActionListener(dirty);

        cboPrinters.addActionListener(dirty);

//        // Openbravo Skin
//        jcboLAF.addItem(new UIManager.LookAndFeelInfo("Openbravo", "com.openbravo.pos.skin.OpenbravoLookAndFeel"));

        // Installed skins
        LookAndFeelInfo[] lafs = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < lafs.length; i++) {
            jcboLAF.addItem(new LAFInfo(lafs[i].getName(), lafs[i].getClassName()));
        }

        // Substance skins
        // new SubstanceLookAndFeel(); // TODO: Remove in Substance 5.0. Workaround for Substance 4.3 to initialize static variables
        Map<String, SkinInfo> skins = SubstanceLookAndFeel.getAllSkins();
        for (SkinInfo skin : skins.values()) {
            jcboLAF.addItem(new LAFInfo(skin.getDisplayName(), skin.getClassName()));
        }

        jcboLAF.addActionListener(new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeLAF();
            }
        });

        jcboMachineScreenmode.addItem("window");
        jcboMachineScreenmode.addItem("fullscreen");

        jcboTicketsBag.addItem("simple");
        jcboTicketsBag.addItem("standard");
        jcboTicketsBag.addItem("restaurant");

        // Printer 1
        jcboMachinePrinter.addItem("screen");
        jcboMachinePrinter.addItem("printer");
        jcboMachinePrinter.addItem("epson");
        jcboMachinePrinter.addItem("tmu220");
        jcboMachinePrinter.addItem("star");
        jcboMachinePrinter.addItem("samsung1251cyr");
        jcboMachinePrinter.addItem("bixolon866cyr");
        jcboMachinePrinter.addItem("senor866cyr");
        jcboMachinePrinter.addItem("citizen866cyr");
        jcboMachinePrinter.addItem("sprtpos58cyr");
        jcboMachinePrinter.addItem("gb2312chinese");
        jcboMachinePrinter.addItem("ithaca");
        jcboMachinePrinter.addItem("surepos");
        jcboMachinePrinter.addItem("plain");
        jcboMachinePrinter.addItem("javapos");
        jcboMachinePrinter.addItem("elveskkm");
        jcboMachinePrinter.addItem("shtrihfr");
        jcboMachinePrinter.addItem("aurafr");
        jcboMachinePrinter.addItem("Not defined");

        jcboConnPrinter.addItem("serial");
        jcboConnPrinter.addItem("file");

        jcboSerialPrinter.addItem("COM1");
        jcboSerialPrinter.addItem("COM2");
        jcboSerialPrinter.addItem("COM3");
        jcboSerialPrinter.addItem("COM4");
        jcboSerialPrinter.addItem("LPT1");
        jcboSerialPrinter.addItem("/dev/ttyS0");
        jcboSerialPrinter.addItem("/dev/ttyS1");
        jcboSerialPrinter.addItem("/dev/ttyS2");
        jcboSerialPrinter.addItem("/dev/ttyS3");
        jcboSerialPrinter.addItem("/dev/usb/lp0");

        // Printer 2        
        jcboMachinePrinter2.addItem("screen");
        jcboMachinePrinter2.addItem("printer");
        jcboMachinePrinter2.addItem("epson");
        jcboMachinePrinter2.addItem("tmu220");
        jcboMachinePrinter2.addItem("star");
        jcboMachinePrinter2.addItem("samsung1251cyr");
        jcboMachinePrinter2.addItem("bixolon866cyr");
        jcboMachinePrinter2.addItem("senor866cyr");
        jcboMachinePrinter2.addItem("citizen866cyr");
        jcboMachinePrinter2.addItem("sprtpos58cyr");
        jcboMachinePrinter2.addItem("gb2312chinese");
        jcboMachinePrinter2.addItem("ithaca");
        jcboMachinePrinter2.addItem("surepos");
        jcboMachinePrinter2.addItem("plain");
        jcboMachinePrinter2.addItem("javapos");
        jcboMachinePrinter2.addItem("elveskkm");
        jcboMachinePrinter2.addItem("shtrihfr");
        jcboMachinePrinter2.addItem("aurafr");
        jcboMachinePrinter2.addItem("Not defined");

        jcboConnPrinter2.addItem("serial");
        jcboConnPrinter2.addItem("file");

        jcboSerialPrinter2.addItem("COM1");
        jcboSerialPrinter2.addItem("COM2");
        jcboSerialPrinter2.addItem("COM3");
        jcboSerialPrinter2.addItem("COM4");
        jcboSerialPrinter2.addItem("LPT1");
        jcboSerialPrinter2.addItem("/dev/ttyS0");
        jcboSerialPrinter2.addItem("/dev/ttyS1");
        jcboSerialPrinter2.addItem("/dev/ttyS2");
        jcboSerialPrinter2.addItem("/dev/ttyS3");
        jcboSerialPrinter2.addItem("/dev/usb/lp0");

        // Printer 3
        jcboMachinePrinter3.addItem("screen");
        jcboMachinePrinter3.addItem("printer");
        jcboMachinePrinter3.addItem("epson");
        jcboMachinePrinter3.addItem("tmu220");
        jcboMachinePrinter3.addItem("star");
        jcboMachinePrinter3.addItem("samsung1251cyr");
        jcboMachinePrinter3.addItem("bixolon866cyr");
        jcboMachinePrinter3.addItem("senor866cyr");
        jcboMachinePrinter3.addItem("citizen866cyr");
        jcboMachinePrinter3.addItem("sprtpos58cyr");
        jcboMachinePrinter3.addItem("gb2312chinese");
        jcboMachinePrinter3.addItem("ithaca");
        jcboMachinePrinter3.addItem("surepos");
        jcboMachinePrinter3.addItem("plain");
        jcboMachinePrinter3.addItem("javapos");
        jcboMachinePrinter3.addItem("elveskkm");
        jcboMachinePrinter3.addItem("shtrihfr");
        jcboMachinePrinter3.addItem("aurafr");
        jcboMachinePrinter3.addItem("Not defined");

        jcboConnPrinter3.addItem("serial");
        jcboConnPrinter3.addItem("file");

        jcboSerialPrinter3.addItem("COM1");
        jcboSerialPrinter3.addItem("COM2");
        jcboSerialPrinter3.addItem("COM3");
        jcboSerialPrinter3.addItem("COM4");
        jcboSerialPrinter3.addItem("LPT1");
        jcboSerialPrinter3.addItem("/dev/ttyS0");
        jcboSerialPrinter3.addItem("/dev/ttyS1");
        jcboSerialPrinter3.addItem("/dev/ttyS2");
        jcboSerialPrinter3.addItem("/dev/ttyS3");
        jcboSerialPrinter3.addItem("/dev/usb/lp0");                

        // Fiscal printer
        jcboMachineFiscalPrinter.addItem("javapos");
        jcboMachineFiscalPrinter.addItem("aurafr");
        jcboMachineFiscalPrinter.addItem("shtrihfr");

        jcboMachineFiscalPrinter.addItem("Not defined");

        jcboConnFiscalPrinter.addItem("serial");

        jcboSerialFiscalPrinter.addItem("COM1");
        jcboSerialFiscalPrinter.addItem("COM2");
        jcboSerialFiscalPrinter.addItem("COM3");
        jcboSerialFiscalPrinter.addItem("COM4");
        jcboSerialFiscalPrinter.addItem("/dev/ttyS0");
        jcboSerialFiscalPrinter.addItem("/dev/ttyS1");
        jcboSerialFiscalPrinter.addItem("/dev/ttyS2");
        jcboSerialFiscalPrinter.addItem("/dev/ttyS3");
        
        // Label printer
        jcboMachineLabelPrinter.addItem("screen");        
        jcboMachineLabelPrinter.addItem("godexezpl");
        jcboMachineLabelPrinter.addItem("Not defined");

        jcboConnLabelPrinter.addItem("serial");
        jcboConnLabelPrinter.addItem("file");        

        jcboSerialLabelPrinter.addItem("COM1");
        jcboSerialLabelPrinter.addItem("COM2");
        jcboSerialLabelPrinter.addItem("COM3");
        jcboSerialLabelPrinter.addItem("COM4");
        jcboSerialLabelPrinter.addItem("/dev/ttyS0");
        jcboSerialLabelPrinter.addItem("/dev/ttyS1");
        jcboSerialLabelPrinter.addItem("/dev/ttyS2");
        jcboSerialLabelPrinter.addItem("/dev/ttyS3");        
        jcboSerialLabelPrinter.addItem("/dev/usb/lp0");
        
        // Display
        jcboMachineDisplay.addItem("screen");
        jcboMachineDisplay.addItem("window");
        jcboMachineDisplay.addItem("javapos");
        jcboMachineDisplay.addItem("epson");
        jcboMachineDisplay.addItem("ld200");
        jcboMachineDisplay.addItem("surepos");
        jcboMachineDisplay.addItem("cd5220rus");
        jcboMachineDisplay.addItem("Not defined");

        jcboConnDisplay.addItem("serial");
        jcboConnDisplay.addItem("file");

        jcboSerialDisplay.addItem("COM1");
        jcboSerialDisplay.addItem("COM2");
        jcboSerialDisplay.addItem("COM3");
        jcboSerialDisplay.addItem("COM4");
        jcboSerialDisplay.addItem("LPT1");
        jcboSerialDisplay.addItem("/dev/ttyS0");
        jcboSerialDisplay.addItem("/dev/ttyS1");
        jcboSerialDisplay.addItem("/dev/ttyS2");
        jcboSerialDisplay.addItem("/dev/ttyS3");

        // Scale
        jcboMachineScale.addItem("screen");
        jcboMachineScale.addItem("dialog1");
        jcboMachineScale.addItem("samsungesp");
        jcboMachineScale.addItem("tves4149");
        jcboMachineScale.addItem("massak");
        jcboMachineScale.addItem("Not defined");

        jcboSerialScale.addItem("COM1");
        jcboSerialScale.addItem("COM2");
        jcboSerialScale.addItem("COM3");
        jcboSerialScale.addItem("COM4");
        jcboSerialScale.addItem("/dev/ttyS0");
        jcboSerialScale.addItem("/dev/ttyS1");
        jcboSerialScale.addItem("/dev/ttyS2");
        jcboSerialScale.addItem("/dev/ttyS3");

        // Scanner and Device with PLUs
        jcboMachineScanner.addItem("scanpal2");
        jcboMachineScanner.addItem("mercury130kz039");
        jcboMachineScanner.addItem("massakvpm");
        jcboMachineScanner.addItem("Not defined");

        jcboSerialScanner.addItem("COM1");
        jcboSerialScanner.addItem("COM2");
        jcboSerialScanner.addItem("COM3");
        jcboSerialScanner.addItem("COM4");
        jcboSerialScanner.addItem("/dev/ttyS0");
        jcboSerialScanner.addItem("/dev/ttyS1");
        jcboSerialScanner.addItem("/dev/ttyS2");
        jcboSerialScanner.addItem("/dev/ttyS3");

        // Printers
        cboPrinters.addItem("(Default)");
        cboPrinters.addItem("(Show dialog)");
        for (String name : printernames) {
            cboPrinters.addItem(name);
        }
    }

    public boolean hasChanged() {
        return dirty.isDirty();
    }

    public Component getConfigComponent() {
        return this;
    }

    public void loadProperties(AppConfig config) {

        jtxtMachineHostname.setText(config.getProperty("machine.hostname"));

        String lafclass = config.getProperty("swing.defaultlaf");
        jcboLAF.setSelectedItem(null);
        for (int i = 0; i < jcboLAF.getItemCount(); i++) {
            LAFInfo lafinfo = (LAFInfo) jcboLAF.getItemAt(i);
            if (lafinfo.getClassName().equals(lafclass)) {
                jcboLAF.setSelectedIndex(i);
                break;
            }
        }
        // jcboLAF.setSelectedItem(new LookAndFeelInfo());

        jcboMachineScreenmode.setSelectedItem(config.getProperty("machine.screenmode"));
        jcboTicketsBag.setSelectedItem(config.getProperty("machine.ticketsbag"));

        StringParser p = new StringParser(config.getProperty("machine.printer"));
        String sparam = unifySerialInterface(p.nextToken(':'));
        if ("elveskkm".equals(sparam) || "shtrihfr".equals(sparam) || "aurafr".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem(sparam);
            jcboConnPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter.setSelectedItem(p.nextToken(','));
            jcboPrinter1SerialPortSpeed.setSelectedItem(p.nextToken(','));            
            jcboPrinter1SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortParity.setSelectedItem(p.nextToken(','));           
        } else if ("serial".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem("epson");
            jcboConnPrinter.setSelectedItem(sparam);
            jcboSerialPrinter.setSelectedItem(p.nextToken(','));
            jcboPrinter1SerialPortSpeed.setSelectedItem(p.nextToken(','));
            jcboPrinter1SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortParity.setSelectedItem(p.nextToken(','));             
        } else if ("file".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem("epson");
            jcboConnPrinter.setSelectedItem(sparam);
            jcboSerialPrinter.setSelectedItem(p.nextToken(','));
        } else if ("javapos".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem(sparam);
            m_jtxtJPOSPrinter.setText(p.nextToken(','));
            m_jtxtJPOSDrawer.setText(p.nextToken(','));
        } else if ("printer".equals(sparam)) {
            jcboMachinePrinter.setSelectedItem(sparam);
            printer1printerparams.setParameters(p);
        } else {
            jcboMachinePrinter.setSelectedItem(sparam);
            jcboConnPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter.setSelectedItem(p.nextToken(','));
            jcboPrinter1SerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter1SerialPortParity.setSelectedItem(p.nextToken(','));               
        }

        p = new StringParser(config.getProperty("machine.printer.2"));
        sparam = unifySerialInterface(p.nextToken(':'));
        if ("elveskkm".equals(sparam) || "shtrihfr".equals(sparam) || "aurafr".equals(sparam)) {
            jcboMachinePrinter2.setSelectedItem(sparam);
            jcboConnPrinter2.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter2.setSelectedItem(p.nextToken(','));
            jcboPrinter2SerialPortSpeed.setSelectedItem(p.nextToken(','));            
            jcboPrinter2SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortParity.setSelectedItem(p.nextToken(','));             
        } else if ("serial".equals(sparam)) {
            jcboMachinePrinter2.setSelectedItem("epson");
            jcboConnPrinter2.setSelectedItem(sparam);
            jcboSerialPrinter2.setSelectedItem(p.nextToken(','));
            jcboPrinter2SerialPortSpeed.setSelectedItem(p.nextToken(','));
            jcboPrinter2SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortParity.setSelectedItem(p.nextToken(','));              
        } else if ("file".equals(sparam)) {
            jcboMachinePrinter2.setSelectedItem("epson");
            jcboConnPrinter2.setSelectedItem(sparam);
            jcboSerialPrinter2.setSelectedItem(p.nextToken(','));
        } else if ("javapos".equals(sparam)) {
            jcboMachinePrinter2.setSelectedItem(sparam);
            m_jtxtJPOSPrinter2.setText(p.nextToken(','));
            m_jtxtJPOSDrawer2.setText(p.nextToken(','));
        } else if ("printer".equals(sparam)) {
            jcboMachinePrinter2.setSelectedItem(sparam);
            printer2printerparams.setParameters(p);
        } else {
            jcboMachinePrinter2.setSelectedItem(sparam);
            jcboConnPrinter2.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter2.setSelectedItem(p.nextToken(','));
            jcboPrinter2SerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter2SerialPortParity.setSelectedItem(p.nextToken(','));               
        }

        p = new StringParser(config.getProperty("machine.printer.3"));
        sparam = unifySerialInterface(p.nextToken(':'));
        if ("elveskkm".equals(sparam) || "shtrihfr".equals(sparam) || "aurafr".equals(sparam)) {
            jcboMachinePrinter3.setSelectedItem(sparam);
            jcboConnPrinter3.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter3.setSelectedItem(p.nextToken(','));
            jcboPrinter3SerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortParity.setSelectedItem(p.nextToken(','));            
        } else if ("serial".equals(sparam)) {
            jcboMachinePrinter3.setSelectedItem("epson");
            jcboConnPrinter3.setSelectedItem(sparam);
            jcboSerialPrinter3.setSelectedItem(p.nextToken(','));
            jcboPrinter3SerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortParity.setSelectedItem(p.nextToken(','));
        } else if ("file".equals(sparam)) {
            jcboMachinePrinter3.setSelectedItem("epson");
            jcboConnPrinter3.setSelectedItem(sparam);
            jcboSerialPrinter3.setSelectedItem(p.nextToken(','));            
        } else if ("javapos".equals(sparam)) {
            jcboMachinePrinter3.setSelectedItem(sparam);
            m_jtxtJPOSPrinter3.setText(p.nextToken(','));
            m_jtxtJPOSDrawer3.setText(p.nextToken(','));
        } else if ("printer".equals(sparam)) {
            jcboMachinePrinter3.setSelectedItem(sparam);
            printer3printerparams.setParameters(p);
        } else {
            jcboMachinePrinter3.setSelectedItem(sparam);
            jcboConnPrinter3.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialPrinter3.setSelectedItem(p.nextToken(','));
            jcboPrinter3SerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboPrinter3SerialPortParity.setSelectedItem(p.nextToken(','));             
        }

        p = new StringParser(config.getProperty("machine.fiscalprinter"));
        sparam = unifySerialInterface(p.nextToken(':'));
        if ("aurafr".equals(sparam) 
            || "shtrihfr".equals(sparam)
            ) {
            jcboMachineFiscalPrinter.setSelectedItem(sparam);
            jcboConnFiscalPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialFiscalPrinter.setSelectedItem(p.nextToken(','));
            jcboFiscalPrinterSerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortParity.setSelectedItem(p.nextToken(','));             
        } else if ("javapos".equals(sparam)) {
            jcboMachineFiscalPrinter.setSelectedItem(sparam);
            m_jtxtJPOSFiscalPrinter.setText(p.nextToken(','));
            m_jtxtJPOSFiscalDrawer.setText(p.nextToken(','));
        } else {
            jcboMachineFiscalPrinter.setSelectedItem(sparam);
            jcboConnFiscalPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialFiscalPrinter.setSelectedItem(p.nextToken(','));
            jcboFiscalPrinterSerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboFiscalPrinterSerialPortParity.setSelectedItem(p.nextToken(','));             
        }
        
        p = new StringParser(config.getProperty("machine.labelprinter"));
        sparam = unifySerialInterface(p.nextToken(':'));
        if ("godexezpl".equals(sparam) 
            ) {
            jcboMachineLabelPrinter.setSelectedItem(sparam);
            jcboConnLabelPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialLabelPrinter.setSelectedItem(p.nextToken(','));
        } else {
            jcboMachineFiscalPrinter.setSelectedItem(sparam);
            jcboConnFiscalPrinter.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialFiscalPrinter.setSelectedItem(p.nextToken(','));
        }

        p = new StringParser(config.getProperty("machine.display"));
        sparam = unifySerialInterface(p.nextToken(':'));
        if ("serial".equals(sparam)) {
            jcboMachineDisplay.setSelectedItem("epson");
            jcboConnDisplay.setSelectedItem(sparam);
            jcboSerialDisplay.setSelectedItem(p.nextToken(','));
            jcboDisplaySerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortParity.setSelectedItem(p.nextToken(','));              
        } else if ("file".equals(sparam)) {
            jcboMachineDisplay.setSelectedItem("epson");
            jcboConnDisplay.setSelectedItem(sparam);
            jcboSerialDisplay.setSelectedItem(p.nextToken(','));
        } else if ("javapos".equals(sparam)) {
            jcboMachineDisplay.setSelectedItem(sparam);
            m_jtxtJPOSName.setText(p.nextToken(','));
        } else {
            jcboMachineDisplay.setSelectedItem(sparam);
            jcboConnDisplay.setSelectedItem(unifySerialInterface(p.nextToken(',')));
            jcboSerialDisplay.setSelectedItem(p.nextToken(','));
            jcboDisplaySerialPortSpeed.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortDataBits.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortStopBits.setSelectedItem(p.nextToken(',')); 
            jcboDisplaySerialPortParity.setSelectedItem(p.nextToken(','));              
        }

        p = new StringParser(config.getProperty("machine.scale"));
        sparam = p.nextToken(':');
        jcboMachineScale.setSelectedItem(sparam);
        if ("dialog1".equals(sparam)
                || "samsungesp".equals(sparam)
                || "tves4149".equals(sparam)
                || "massak".equals(sparam)
                ) {
            jcboSerialScale.setSelectedItem(p.nextToken(','));
        }

        p = new StringParser(config.getProperty("machine.scanner"));
        sparam = p.nextToken(':');
        jcboMachineScanner.setSelectedItem(sparam);
        if ("scanpal2".equals(sparam) 
                || "mercury130kz039".equals(sparam)
                || "massakvpm".equals(sparam)
                ) {
            jcboSerialScanner.setSelectedItem(p.nextToken(','));
        }

        cboPrinters.setSelectedItem(config.getProperty("machine.printername"));

        dirty.setDirty(false);
    }

    public void saveProperties(AppConfig config) {

        config.setProperty("machine.hostname", jtxtMachineHostname.getText());

        LAFInfo laf = (LAFInfo) jcboLAF.getSelectedItem();
        config.setProperty("swing.defaultlaf", laf == null
                ? System.getProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel")
                : laf.getClassName());

        config.setProperty("machine.screenmode", comboValue(jcboMachineScreenmode.getSelectedItem()));
        config.setProperty("machine.ticketsbag", comboValue(jcboTicketsBag.getSelectedItem()));

        String sMachinePrinter = comboValue(jcboMachinePrinter.getSelectedItem());
        String sMachinePrinterMode = comboValue(jcboConnPrinter.getSelectedItem());
        
        if ("epson".equals(sMachinePrinter)
                || "tmu220".equals(sMachinePrinter)
                || "star".equals(sMachinePrinter)
                || "samsung1251cyr".equals(sMachinePrinter)
                || "bixolon866cyr".equals(sMachinePrinter)
                || "senor866cyr".equals(sMachinePrinter)
                || "citizen866cyr".equals(sMachinePrinter)
                || "sprtpos58cyr".equals(sMachinePrinter)
                || "gb2312chinese".equals(sMachinePrinter)
                || "ithaca".equals(sMachinePrinter)
                || "surepos".equals(sMachinePrinter)
                || "elveskkm".equals(sMachinePrinter)
                || "shtrihfr".equals(sMachinePrinter)
                || "aurafr".equals(sMachinePrinter)
                ) {
            if ("serial".equals(sMachinePrinterMode)){
                config.setProperty("machine.printer", sMachinePrinter + ":" + comboValue(jcboConnPrinter.getSelectedItem()) + "," + comboValue(jcboSerialPrinter.getSelectedItem())+ "," + comboValue(jcboPrinter1SerialPortSpeed.getSelectedItem())+ "," + comboValue(jcboPrinter1SerialPortDataBits.getSelectedItem())+ "," + comboValue(jcboPrinter1SerialPortStopBits.getSelectedItem())+ "," + comboValue(jcboPrinter1SerialPortParity.getSelectedItem()));
            } else {
                config.setProperty("machine.printer", sMachinePrinter + ":" + comboValue(jcboConnPrinter.getSelectedItem()) + "," + comboValue(jcboSerialPrinter.getSelectedItem()));
            }
        } else if ("javapos".equals(sMachinePrinter)) {
            config.setProperty("machine.printer", sMachinePrinter + ":" + m_jtxtJPOSPrinter.getText() + "," + m_jtxtJPOSDrawer.getText());
        } else if ("printer".equals(sMachinePrinter)) {
            config.setProperty("machine.printer", sMachinePrinter + ":" + printer1printerparams.getParameters());
        } else {
            config.setProperty("machine.printer", sMachinePrinter);
        }

        String sMachinePrinter2 = comboValue(jcboMachinePrinter2.getSelectedItem());
        String sMachinePrinter2Mode = comboValue(jcboConnPrinter2.getSelectedItem()); 
        
        if ("epson".equals(sMachinePrinter2)
                || "tmu220".equals(sMachinePrinter2)
                || "star".equals(sMachinePrinter2)
                || "samsung1251cyr".equals(sMachinePrinter2)
                || "bixolon866cyr".equals(sMachinePrinter2)
                || "senor866cyr".equals(sMachinePrinter2)
                || "citizen866cyr".equals(sMachinePrinter2)
                || "sprtpos58cyr".equals(sMachinePrinter2)
                || "gb2312chinese".equals(sMachinePrinter2)
                || "ithaca".equals(sMachinePrinter2)
                || "surepos".equals(sMachinePrinter2)
                || "elveskkm".equals(sMachinePrinter2)
                || "shtrihfr".equals(sMachinePrinter2)
                || "aurafr".equals(sMachinePrinter2)
                ) {
            if ("serial".equals(sMachinePrinter2Mode)){
                config.setProperty("machine.printer.2", sMachinePrinter2 + ":" + comboValue(jcboConnPrinter2.getSelectedItem()) + "," + comboValue(jcboSerialPrinter2.getSelectedItem())+ "," + comboValue(jcboPrinter2SerialPortSpeed.getSelectedItem())+ "," + comboValue(jcboPrinter2SerialPortDataBits.getSelectedItem())+ "," + comboValue(jcboPrinter2SerialPortStopBits.getSelectedItem())+ "," + comboValue(jcboPrinter2SerialPortParity.getSelectedItem()));                
            } else {                
                config.setProperty("machine.printer.2", sMachinePrinter2 + ":" + comboValue(jcboConnPrinter2.getSelectedItem()) + "," + comboValue(jcboSerialPrinter2.getSelectedItem()));
            }
        } else if ("javapos".equals(sMachinePrinter2)) {
            config.setProperty("machine.printer.2", sMachinePrinter2 + ":" + m_jtxtJPOSPrinter2.getText() + "," + m_jtxtJPOSDrawer2.getText());
        } else if ("printer".equals(sMachinePrinter2)) {
            config.setProperty("machine.printer.2", sMachinePrinter2 + ":" + printer2printerparams.getParameters());
        } else {
            config.setProperty("machine.printer.2", sMachinePrinter2);
        }

        String sMachinePrinter3 = comboValue(jcboMachinePrinter3.getSelectedItem());
        String sMachinePrinter3Mode = comboValue(jcboConnPrinter3.getSelectedItem());         
        
        if ("epson".equals(sMachinePrinter3)
                || "tmu220".equals(sMachinePrinter3)
                || "star".equals(sMachinePrinter3)
                || "samsung1251cyr".equals(sMachinePrinter3)
                || "bixolon866cyr".equals(sMachinePrinter3)
                || "senor866cyr".equals(sMachinePrinter3)
                || "citizen866cyr".equals(sMachinePrinter3)
                || "sprtpos58cyr".equals(sMachinePrinter3)
                || "gb2312chinese".equals(sMachinePrinter3)
                || "ithaca".equals(sMachinePrinter3)
                || "surepos".equals(sMachinePrinter3)
                || "elveskkm".equals(sMachinePrinter3)
                || "shtrihfr".equals(sMachinePrinter3)
                || "aurafr".equals(sMachinePrinter3)
                ) {
            if ("serial".equals(sMachinePrinter3Mode)){
                config.setProperty("machine.printer.3", sMachinePrinter3 + ":" + comboValue(jcboConnPrinter3.getSelectedItem()) + "," + comboValue(jcboSerialPrinter3.getSelectedItem()) + "," + comboValue(jcboPrinter3SerialPortSpeed.getSelectedItem())+ "," + comboValue(jcboPrinter3SerialPortDataBits.getSelectedItem())+ "," + comboValue(jcboPrinter3SerialPortStopBits.getSelectedItem())+ "," + comboValue(jcboPrinter3SerialPortParity.getSelectedItem()));                
            } else {                
                config.setProperty("machine.printer.3", sMachinePrinter3 + ":" + comboValue(jcboConnPrinter3.getSelectedItem()) + "," + comboValue(jcboSerialPrinter3.getSelectedItem()));
            }            
        } else if ("javapos".equals(sMachinePrinter3)) {
            config.setProperty("machine.printer.3", sMachinePrinter3 + ":" + m_jtxtJPOSPrinter3.getText() + "," + m_jtxtJPOSDrawer3.getText());
        } else if ("printer".equals(sMachinePrinter3)) {
            config.setProperty("machine.printer.3", sMachinePrinter3 + ":" + printer3printerparams.getParameters());
        } else {
            config.setProperty("machine.printer.3", sMachinePrinter3);
        }

        String sMachineFiscalPrinter = comboValue(jcboMachineFiscalPrinter.getSelectedItem());
        if ( "aurafr".equals(sMachineFiscalPrinter)
                || "shtrihfr".equals(sMachineFiscalPrinter)
            ) {
            config.setProperty("machine.fiscalprinter", sMachineFiscalPrinter + ":" + comboValue(jcboConnFiscalPrinter.getSelectedItem()) + "," + comboValue(jcboSerialFiscalPrinter.getSelectedItem())  + "," + comboValue(jcboFiscalPrinterSerialPortSpeed.getSelectedItem())+ "," + comboValue(jcboFiscalPrinterSerialPortDataBits.getSelectedItem())+ "," + comboValue(jcboFiscalPrinterSerialPortStopBits.getSelectedItem())+ "," + comboValue(jcboFiscalPrinterSerialPortParity.getSelectedItem()));
        } else if ("javapos".equals(sMachineFiscalPrinter)) {
            config.setProperty("machine.fiscalprinter", sMachineFiscalPrinter + ":" + m_jtxtJPOSFiscalPrinter.getText() + "," + m_jtxtJPOSFiscalDrawer.getText());
        } else {
            config.setProperty("machine.fiscalprinter", sMachineFiscalPrinter);
        }
        
        String sMachineLabelPrinter = comboValue(jcboMachineLabelPrinter.getSelectedItem());
        if ( "godexezpl".equals(sMachineLabelPrinter)
            ) {
            config.setProperty("machine.labelprinter", sMachineLabelPrinter + ":" + comboValue(jcboConnLabelPrinter.getSelectedItem()) + "," + comboValue(jcboSerialLabelPrinter.getSelectedItem()));
        } else {
            config.setProperty("machine.labelprinter", sMachineLabelPrinter);
        }        

        String sMachineDisplay = comboValue(jcboMachineDisplay.getSelectedItem());
        String sMachineDisplayMode =  comboValue(jcboConnDisplay.getSelectedItem());
        
        if ("epson".equals(sMachineDisplay) || "ld200".equals(sMachineDisplay) || "cd5220rus".equals(sMachineDisplay) || "surepos".equals(sMachineDisplay)) {
            if ("serial".equals(sMachineDisplayMode)){
                config.setProperty("machine.display", sMachineDisplay + ":" + comboValue(jcboConnDisplay.getSelectedItem()) + "," + comboValue(jcboSerialDisplay.getSelectedItem()) + "," + comboValue(jcboDisplaySerialPortSpeed.getSelectedItem())+ "," + comboValue(jcboDisplaySerialPortDataBits.getSelectedItem())+ "," + comboValue(jcboDisplaySerialPortStopBits.getSelectedItem())+ "," + comboValue(jcboDisplaySerialPortParity.getSelectedItem()));                
            } else {
                config.setProperty("machine.display", sMachineDisplay + ":" + comboValue(jcboConnDisplay.getSelectedItem()) + "," + comboValue(jcboSerialDisplay.getSelectedItem()));
            }
        } else if ("javapos".equals(sMachineDisplay)) {
            config.setProperty("machine.display", sMachineDisplay + ":" + m_jtxtJPOSName.getText());
        } else {
            config.setProperty("machine.display", sMachineDisplay);
        }

        // La bascula
        String sMachineScale = comboValue(jcboMachineScale.getSelectedItem());
        if ("dialog1".equals(sMachineScale)
                || "samsungesp".equals(sMachineScale)
                || "tves4149".equals(sMachineScale)
                || "massak".equals(sMachineScale)
                ) {
            config.setProperty("machine.scale", sMachineScale + ":" + comboValue(jcboSerialScale.getSelectedItem()));
        } else {
            config.setProperty("machine.scale", sMachineScale);
        }

        // Scanner and Device with PLUs
        String sMachineScanner = comboValue(jcboMachineScanner.getSelectedItem());
        if ("scanpal2".equals(sMachineScanner) 
                || "mercury130kz039".equals(sMachineScanner)
                || "massakvpm".equals(sMachineScanner)
                ) {
            config.setProperty("machine.scanner", sMachineScanner + ":" + comboValue(jcboSerialScanner.getSelectedItem()));
        } else {
            config.setProperty("machine.scanner", sMachineScanner);
        }

        config.setProperty("machine.printername", comboValue(cboPrinters.getSelectedItem()));

        dirty.setDirty(false);
    }

    private String unifySerialInterface(String sparam) {

        // for backward compatibility
        return ("rxtx".equals(sparam))
                ? "serial"
                : sparam;
    }

    private String comboValue(Object value) {
        return value == null ? "" : value.toString();
    }

    private void changeLAF() {

        final LAFInfo laf = (LAFInfo) jcboLAF.getSelectedItem();
        if (laf != null && !laf.getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
            // The selected look and feel is different from the current look and feel.
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        String lafname = laf.getClassName();
                        Object laf = Class.forName(lafname).newInstance();

                        if (laf instanceof LookAndFeel) {
                            UIManager.setLookAndFeel((LookAndFeel) laf);
                        } else if (laf instanceof SubstanceSkin) {
                            SubstanceLookAndFeel.setSkin((SubstanceSkin) laf);
                        }

                        SwingUtilities.updateComponentTreeUI(JPanelConfigGeneral.this.getTopLevelAncestor());
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    private static class LAFInfo {

        private String name;
        private String classname;

        public LAFInfo(String name, String classname) {
            this.name = name;
            this.classname = classname;
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return classname;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel13 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtxtMachineHostname = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jcboLAF = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jcboMachineScreenmode = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        jcboTicketsBag = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jcboMachineDisplay = new javax.swing.JComboBox();
        m_jDisplayParams = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jlblConnDisplay = new javax.swing.JLabel();
        jcboConnDisplay = new javax.swing.JComboBox();
        jlblDisplayPort = new javax.swing.JLabel();
        jcboSerialDisplay = new javax.swing.JComboBox();
        m_jDisplayPortParams = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jlblPrinterPortParams3 = new javax.swing.JLabel();
        jcboDisplaySerialPortSpeed = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        jcboDisplaySerialPortDataBits = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        jcboDisplaySerialPortStopBits = new javax.swing.JComboBox();
        jLabel34 = new javax.swing.JLabel();
        jcboDisplaySerialPortParity = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        m_jtxtJPOSName = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jcboMachinePrinter = new javax.swing.JComboBox();
        m_jPrinterParams1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlblConnPrinter = new javax.swing.JLabel();
        jcboConnPrinter = new javax.swing.JComboBox();
        jlblPrinterPort = new javax.swing.JLabel();
        jcboSerialPrinter = new javax.swing.JComboBox();
        m_jPrinterPortParams1 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jlblPrinterPortParams = new javax.swing.JLabel();
        jcboPrinter1SerialPortSpeed = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jcboPrinter1SerialPortDataBits = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jcboPrinter1SerialPortStopBits = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jcboPrinter1SerialPortParity = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        m_jtxtJPOSPrinter = new javax.swing.JTextField();
        m_jtxtJPOSDrawer = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jcboMachinePrinter2 = new javax.swing.JComboBox();
        m_jPrinterParams2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlblConnPrinter2 = new javax.swing.JLabel();
        jcboConnPrinter2 = new javax.swing.JComboBox();
        jlblPrinterPort2 = new javax.swing.JLabel();
        jcboSerialPrinter2 = new javax.swing.JComboBox();
        m_jPrinterPortParams2 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jlblPrinterPortParams1 = new javax.swing.JLabel();
        jcboPrinter2SerialPortSpeed = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jcboPrinter2SerialPortDataBits = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jcboPrinter2SerialPortStopBits = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jcboPrinter2SerialPortParity = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        m_jtxtJPOSPrinter2 = new javax.swing.JTextField();
        m_jtxtJPOSDrawer2 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jcboMachinePrinter3 = new javax.swing.JComboBox();
        m_jPrinterParams3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlblConnPrinter3 = new javax.swing.JLabel();
        jcboConnPrinter3 = new javax.swing.JComboBox();
        jlblPrinterPort3 = new javax.swing.JLabel();
        jcboSerialPrinter3 = new javax.swing.JComboBox();
        m_jPrinterPortParams3 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jlblPrinterPortParams2 = new javax.swing.JLabel();
        jcboPrinter3SerialPortSpeed = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jcboPrinter3SerialPortDataBits = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jcboPrinter3SerialPortStopBits = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jcboPrinter3SerialPortParity = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        m_jtxtJPOSPrinter3 = new javax.swing.JTextField();
        m_jtxtJPOSDrawer3 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jcboMachineFiscalPrinter = new javax.swing.JComboBox();
        m_jFiscalPrinterParams = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jlblConnPrinter4 = new javax.swing.JLabel();
        jcboConnFiscalPrinter = new javax.swing.JComboBox();
        jlblPrinterPort6 = new javax.swing.JLabel();
        jcboSerialFiscalPrinter = new javax.swing.JComboBox();
        m_jFiscalPrinterPortParams = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jlblPrinterPortParams4 = new javax.swing.JLabel();
        jcboFiscalPrinterSerialPortSpeed = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();
        jcboFiscalPrinterSerialPortDataBits = new javax.swing.JComboBox();
        jLabel36 = new javax.swing.JLabel();
        jcboFiscalPrinterSerialPortStopBits = new javax.swing.JComboBox();
        jLabel37 = new javax.swing.JLabel();
        jcboFiscalPrinterSerialPortParity = new javax.swing.JComboBox();
        jPanel18 = new javax.swing.JPanel();
        m_jtxtJPOSFiscalPrinter = new javax.swing.JTextField();
        m_jtxtJPOSFiscalDrawer = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jcboMachineScale = new javax.swing.JComboBox();
        m_jScaleParams = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jlblPrinterPort4 = new javax.swing.JLabel();
        jcboSerialScale = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jcboMachineScanner = new javax.swing.JComboBox();
        m_jScannerParams = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jlblPrinterPort5 = new javax.swing.JLabel();
        jcboSerialScanner = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        cboPrinters = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jcboMachineLabelPrinter = new javax.swing.JComboBox();
        m_jLabelPrinterParams = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jlblConnPrinter1 = new javax.swing.JLabel();
        jcboConnLabelPrinter = new javax.swing.JComboBox();
        jlblPrinterPort1 = new javax.swing.JLabel();
        jcboSerialLabelPrinter = new javax.swing.JComboBox();

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("Label.CashMachine"))); // NOI18N

        jLabel5.setText(AppLocal.getIntString("Label.MachineName")); // NOI18N

        jLabel2.setText(AppLocal.getIntString("label.looknfeel")); // NOI18N

        jLabel6.setText(AppLocal.getIntString("Label.MachineScreen")); // NOI18N

        jLabel16.setText(AppLocal.getIntString("Label.Ticketsbag")); // NOI18N

        jLabel15.setText(AppLocal.getIntString("Label.MachineDisplay")); // NOI18N

        jcboMachineDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachineDisplayActionPerformed(evt);
            }
        });

        m_jDisplayParams.setBorder(null);
        m_jDisplayParams.setLayout(new java.awt.CardLayout());
        m_jDisplayParams.add(jPanel2, "empty");

        jlblConnDisplay.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jlblDisplayPort.setText(AppLocal.getIntString("label.machinedisplayport")); // NOI18N

        jcboSerialDisplay.setEditable(true);
        jcboSerialDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboDisplayPortTypeActionPerformed(evt);
            }
        });

        m_jDisplayPortParams.setPreferredSize(new java.awt.Dimension(476, 212));
        m_jDisplayPortParams.setLayout(new java.awt.CardLayout());
        m_jDisplayPortParams.add(jPanel30, "file");

        jPanel31.setPreferredSize(new java.awt.Dimension(476, 100));

        jlblPrinterPortParams3.setText(AppLocal.getIntString("label.serialportspeed")); // NOI18N
        jlblPrinterPortParams3.setMaximumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams3.setMinimumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams3.setPreferredSize(new java.awt.Dimension(120, 18));

        jcboDisplaySerialPortSpeed.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortSpeed));
        jcboDisplaySerialPortSpeed.setSelectedIndex(2);

        jLabel32.setText(AppLocal.getIntString("label.serialportdatabits")); // NOI18N

        jcboDisplaySerialPortDataBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortDataBits));
        jcboDisplaySerialPortDataBits.setSelectedIndex(3);

        jLabel33.setText(AppLocal.getIntString("label.serialportstopbits")); // NOI18N

        jcboDisplaySerialPortStopBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortStopBits));
        jcboDisplaySerialPortStopBits.setSelectedIndex(0);

        jLabel34.setText(AppLocal.getIntString("label.serialportparity")); // NOI18N

        jcboDisplaySerialPortParity.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortParity));
        jcboDisplaySerialPortParity.setSelectedIndex(0);

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPortParams3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboDisplaySerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboDisplaySerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboDisplaySerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboDisplaySerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jcboDisplaySerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboDisplaySerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboDisplaySerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboDisplaySerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPortParams3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34))
                .addGap(0, 0, 0))
        );

        m_jDisplayPortParams.add(jPanel31, "serial");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblDisplayPort, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(572, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(355, Short.MAX_VALUE)
                    .addComponent(m_jDisplayPortParams, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboConnDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblDisplayPort)
                    .addComponent(jcboSerialDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblConnDisplay))
                .addGap(59, 59, 59))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(m_jDisplayPortParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        m_jDisplayParams.add(jPanel1, "comm");

        jLabel20.setText(AppLocal.getIntString("Label.Name")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(696, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jtxtJPOSName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(184, 184, 184))
        );

        m_jDisplayParams.add(jPanel3, "javapos");

        jLabel7.setText(AppLocal.getIntString("Label.MachinePrinter")); // NOI18N

        jcboMachinePrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachinePrinterActionPerformed(evt);
            }
        });

        m_jPrinterParams1.setBorder(null);
        m_jPrinterParams1.setLayout(new java.awt.CardLayout());
        m_jPrinterParams1.add(jPanel5, "empty");

        jlblConnPrinter.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jcboConnPrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPrinterPortTypeActionPerformed(evt);
            }
        });

        jlblPrinterPort.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialPrinter.setEditable(true);

        m_jPrinterPortParams1.setPreferredSize(new java.awt.Dimension(476, 212));
        m_jPrinterPortParams1.setLayout(new java.awt.CardLayout());
        m_jPrinterPortParams1.add(jPanel23, "file");

        jPanel25.setPreferredSize(new java.awt.Dimension(476, 100));

        jlblPrinterPortParams.setText(AppLocal.getIntString("label.serialportspeed")); // NOI18N
        jlblPrinterPortParams.setMaximumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams.setMinimumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams.setPreferredSize(new java.awt.Dimension(120, 18));

        jcboPrinter1SerialPortSpeed.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortSpeed));
        jcboPrinter1SerialPortSpeed.setSelectedIndex(2);

        jLabel3.setText(AppLocal.getIntString("label.serialportdatabits")); // NOI18N

        jcboPrinter1SerialPortDataBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortDataBits));
        jcboPrinter1SerialPortDataBits.setSelectedIndex(3);

        jLabel4.setText(AppLocal.getIntString("label.serialportstopbits")); // NOI18N

        jcboPrinter1SerialPortStopBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortStopBits));
        jcboPrinter1SerialPortStopBits.setSelectedIndex(0);

        jLabel9.setText(AppLocal.getIntString("label.serialportparity")); // NOI18N

        jcboPrinter1SerialPortParity.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortParity));
        jcboPrinter1SerialPortParity.setSelectedIndex(0);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPortParams, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter1SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter1SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter1SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter1SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jcboPrinter1SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter1SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter1SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter1SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPortParams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addGap(0, 0, 0))
        );

        m_jPrinterPortParams1.add(jPanel25, "serial");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblPrinterPort, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jPrinterPortParams1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(m_jPrinterPortParams1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcboConnPrinter)
                            .addComponent(jlblConnPrinter))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jcboSerialPrinter)
                            .addComponent(jlblPrinterPort))))
                .addGap(72, 72, 72))
        );

        m_jPrinterParams1.add(jPanel6, "comm");

        jLabel21.setText(AppLocal.getIntString("label.javapos.drawer")); // NOI18N

        jLabel24.setText(AppLocal.getIntString("label.javapos.printer")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jtxtJPOSPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(m_jtxtJPOSDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(184, 184, 184))
        );

        m_jPrinterParams1.add(jPanel4, "javapos");

        jLabel18.setText(AppLocal.getIntString("Label.MachinePrinter2")); // NOI18N

        jcboMachinePrinter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachinePrinter2ActionPerformed(evt);
            }
        });

        m_jPrinterParams2.setBorder(null);
        m_jPrinterParams2.setLayout(new java.awt.CardLayout());
        m_jPrinterParams2.add(jPanel7, "empty");

        jlblConnPrinter2.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jcboConnPrinter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPrinter2PortTypeActionPerformed(evt);
            }
        });

        jlblPrinterPort2.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialPrinter2.setEditable(true);

        m_jPrinterPortParams2.setPreferredSize(new java.awt.Dimension(476, 212));
        m_jPrinterPortParams2.setLayout(new java.awt.CardLayout());
        m_jPrinterPortParams2.add(jPanel26, "file");

        jPanel27.setPreferredSize(new java.awt.Dimension(476, 100));

        jlblPrinterPortParams1.setText(AppLocal.getIntString("label.serialportspeed")); // NOI18N
        jlblPrinterPortParams1.setMaximumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams1.setMinimumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams1.setPreferredSize(new java.awt.Dimension(120, 18));

        jcboPrinter2SerialPortSpeed.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortSpeed));
        jcboPrinter2SerialPortSpeed.setSelectedIndex(2);

        jLabel10.setText(AppLocal.getIntString("label.serialportdatabits")); // NOI18N

        jcboPrinter2SerialPortDataBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortDataBits));
        jcboPrinter2SerialPortDataBits.setSelectedIndex(3);

        jLabel11.setText(AppLocal.getIntString("label.serialportstopbits")); // NOI18N

        jcboPrinter2SerialPortStopBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortStopBits));
        jcboPrinter2SerialPortStopBits.setSelectedIndex(0);

        jLabel12.setText(AppLocal.getIntString("label.serialportparity")); // NOI18N

        jcboPrinter2SerialPortParity.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortParity));
        jcboPrinter2SerialPortParity.setSelectedIndex(0);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPortParams1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter2SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter2SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter2SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter2SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jcboPrinter2SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter2SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter2SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter2SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPortParams1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0))
        );

        m_jPrinterPortParams2.add(jPanel27, "serial");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblPrinterPort2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(572, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                    .addContainerGap(354, Short.MAX_VALUE)
                    .addComponent(m_jPrinterPortParams2, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboConnPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboSerialPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblConnPrinter2)
                    .addComponent(jlblPrinterPort2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel8Layout.createSequentialGroup()
                    .addComponent(m_jPrinterPortParams2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        m_jPrinterParams2.add(jPanel8, "comm");

        jLabel27.setText(AppLocal.getIntString("label.javapos.printer")); // NOI18N

        jLabel22.setText(AppLocal.getIntString("label.javapos.drawer")); // NOI18N

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSDrawer2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jtxtJPOSPrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(m_jtxtJPOSDrawer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(184, 184, 184))
        );

        m_jPrinterParams2.add(jPanel11, "javapos");

        jLabel19.setText(AppLocal.getIntString("Label.MachinePrinter3")); // NOI18N

        jcboMachinePrinter3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachinePrinter3ActionPerformed(evt);
            }
        });

        m_jPrinterParams3.setBorder(null);
        m_jPrinterParams3.setLayout(new java.awt.CardLayout());
        m_jPrinterParams3.add(jPanel9, "empty");

        jlblConnPrinter3.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jcboConnPrinter3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboPrinter3PortTypeActionPerformed(evt);
            }
        });

        jlblPrinterPort3.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialPrinter3.setEditable(true);

        m_jPrinterPortParams3.setPreferredSize(new java.awt.Dimension(476, 212));
        m_jPrinterPortParams3.setLayout(new java.awt.CardLayout());
        m_jPrinterPortParams3.add(jPanel28, "file");

        jPanel29.setPreferredSize(new java.awt.Dimension(476, 100));

        jlblPrinterPortParams2.setText(AppLocal.getIntString("label.serialportspeed")); // NOI18N
        jlblPrinterPortParams2.setMaximumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams2.setMinimumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams2.setPreferredSize(new java.awt.Dimension(120, 18));

        jcboPrinter3SerialPortSpeed.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortSpeed));
        jcboPrinter3SerialPortSpeed.setSelectedIndex(2);

        jLabel13.setText(AppLocal.getIntString("label.serialportdatabits")); // NOI18N

        jcboPrinter3SerialPortDataBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortDataBits));
        jcboPrinter3SerialPortDataBits.setSelectedIndex(3);

        jLabel14.setText(AppLocal.getIntString("label.serialportstopbits")); // NOI18N

        jcboPrinter3SerialPortStopBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortStopBits));
        jcboPrinter3SerialPortStopBits.setSelectedIndex(0);

        jLabel17.setText(AppLocal.getIntString("label.serialportparity")); // NOI18N

        jcboPrinter3SerialPortParity.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortParity));
        jcboPrinter3SerialPortParity.setSelectedIndex(0);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPortParams2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter3SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter3SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter3SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboPrinter3SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jcboPrinter3SerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter3SerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter3SerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboPrinter3SerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPortParams2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel17))
                .addGap(0, 0, 0))
        );

        m_jPrinterPortParams3.add(jPanel29, "serial");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblPrinterPort3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(572, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel10Layout.createSequentialGroup()
                    .addGap(353, 353, 353)
                    .addComponent(m_jPrinterPortParams3, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboConnPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPort3)
                    .addComponent(jcboSerialPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblConnPrinter3))
                .addGap(125, 125, 125))
            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                    .addComponent(m_jPrinterPortParams3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        m_jPrinterParams3.add(jPanel10, "comm");

        jLabel28.setText(AppLocal.getIntString("label.javapos.printer")); // NOI18N

        jLabel23.setText(AppLocal.getIntString("label.javapos.drawer")); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSDrawer3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jtxtJPOSPrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(m_jtxtJPOSDrawer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(224, 224, 224))
        );

        m_jPrinterParams3.add(jPanel12, "javapos");

        jLabel29.setText(AppLocal.getIntString("Label.MachineFiscalPrinter")); // NOI18N

        jcboMachineFiscalPrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachineFiscalPrinterActionPerformed(evt);
            }
        });

        m_jFiscalPrinterParams.setBorder(null);
        m_jFiscalPrinterParams.setLayout(new java.awt.CardLayout());
        m_jFiscalPrinterParams.add(jPanel14, "empty");

        jlblConnPrinter4.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jcboConnFiscalPrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboFiscalPrinterPortTypeActionPerformed(evt);
            }
        });

        jlblPrinterPort6.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialFiscalPrinter.setEditable(true);

        m_jFiscalPrinterPortParams.setPreferredSize(new java.awt.Dimension(476, 212));
        m_jFiscalPrinterPortParams.setLayout(new java.awt.CardLayout());
        m_jFiscalPrinterPortParams.add(jPanel32, "file");

        jPanel33.setPreferredSize(new java.awt.Dimension(476, 100));

        jlblPrinterPortParams4.setText(AppLocal.getIntString("label.serialportspeed")); // NOI18N
        jlblPrinterPortParams4.setMaximumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams4.setMinimumSize(new java.awt.Dimension(120, 18));
        jlblPrinterPortParams4.setPreferredSize(new java.awt.Dimension(120, 18));

        jcboFiscalPrinterSerialPortSpeed.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortSpeed));
        jcboFiscalPrinterSerialPortSpeed.setSelectedIndex(6);

        jLabel35.setText(AppLocal.getIntString("label.serialportdatabits")); // NOI18N

        jcboFiscalPrinterSerialPortDataBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortDataBits));
        jcboFiscalPrinterSerialPortDataBits.setSelectedIndex(3);

        jLabel36.setText(AppLocal.getIntString("label.serialportstopbits")); // NOI18N

        jcboFiscalPrinterSerialPortStopBits.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortStopBits));
        jcboFiscalPrinterSerialPortStopBits.setSelectedIndex(0);

        jLabel37.setText(AppLocal.getIntString("label.serialportparity")); // NOI18N

        jcboFiscalPrinterSerialPortParity.setModel(new javax.swing.DefaultComboBoxModel(modelSerialPortParity));
        jcboFiscalPrinterSerialPortParity.setSelectedIndex(0);

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPortParams4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboFiscalPrinterSerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboFiscalPrinterSerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboFiscalPrinterSerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboFiscalPrinterSerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jcboFiscalPrinterSerialPortSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboFiscalPrinterSerialPortDataBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboFiscalPrinterSerialPortStopBits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboFiscalPrinterSerialPortParity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPortParams4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37))
                .addGap(12, 12, 12))
        );

        m_jFiscalPrinterPortParams.add(jPanel33, "serial");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnPrinter4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblPrinterPort6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(572, Short.MAX_VALUE))
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addGap(353, 353, 353)
                    .addComponent(m_jFiscalPrinterPortParams, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboConnFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboSerialFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblConnPrinter4)
                    .addComponent(jlblPrinterPort6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel15Layout.createSequentialGroup()
                    .addComponent(m_jFiscalPrinterPortParams, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        m_jFiscalPrinterParams.add(jPanel15, "comm");

        jLabel30.setText(AppLocal.getIntString("label.javapos.printer")); // NOI18N

        jLabel31.setText(AppLocal.getIntString("label.javapos.drawer")); // NOI18N

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_jtxtJPOSFiscalDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(472, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_jtxtJPOSFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(m_jtxtJPOSFiscalDrawer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(224, 224, 224))
        );

        m_jFiscalPrinterParams.add(jPanel18, "javapos");

        jLabel25.setText(AppLocal.getIntString("label.scale")); // NOI18N

        jcboMachineScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachineScaleActionPerformed(evt);
            }
        });

        m_jScaleParams.setBorder(null);
        m_jScaleParams.setLayout(new java.awt.CardLayout());
        m_jScaleParams.add(jPanel16, "empty");

        jlblPrinterPort4.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialScale.setEditable(true);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPort4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialScale, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(756, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboSerialScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPort4))
                .addContainerGap())
        );

        m_jScaleParams.add(jPanel17, "comm");

        jLabel26.setText(AppLocal.getIntString("label.scanner")); // NOI18N

        jcboMachineScanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachineScannerActionPerformed(evt);
            }
        });

        m_jScannerParams.setBorder(null);
        m_jScannerParams.setLayout(new java.awt.CardLayout());
        m_jScannerParams.add(jPanel24, "empty");

        jlblPrinterPort5.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialScanner.setEditable(true);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblPrinterPort5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(756, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboSerialScanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblPrinterPort5))
                .addGap(235, 235, 235))
        );

        m_jScannerParams.add(jPanel19, "comm");

        jLabel1.setText(AppLocal.getIntString("label.reportsprinter")); // NOI18N

        jLabel8.setText(AppLocal.getIntString("Label.MachineLabelPrinter")); // NOI18N

        jcboMachineLabelPrinter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcboMachineLabelPrinterActionPerformed(evt);
            }
        });

        m_jLabelPrinterParams.setBorder(null);
        m_jLabelPrinterParams.setLayout(new java.awt.CardLayout());
        m_jLabelPrinterParams.add(jPanel20, "empty");

        jlblConnPrinter1.setText(AppLocal.getIntString("label.machinedisplayconn")); // NOI18N

        jlblPrinterPort1.setText(AppLocal.getIntString("label.machineprinterport")); // NOI18N

        jcboSerialLabelPrinter.setEditable(true);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jlblConnPrinter1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboConnLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlblPrinterPort1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcboSerialLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(592, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcboConnLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboSerialLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlblConnPrinter1)
                    .addComponent(jlblPrinterPort1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        m_jLabelPrinterParams.add(jPanel21, "comm");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jcboMachinePrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachinePrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboTicketsBag, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineScale, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachinePrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboLAF, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineScreenmode, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtMachineHostname, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jcboMachineLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(m_jDisplayParams, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jPrinterParams1, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jPrinterParams2, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jPrinterParams3, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jFiscalPrinterParams, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jScaleParams, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jScannerParams, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_jLabelPrinterParams, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cboPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtMachineHostname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboLAF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineScreenmode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboTicketsBag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jDisplayParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachinePrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jPrinterParams1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachinePrinter2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jPrinterParams2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachinePrinter3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jPrinterParams3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineFiscalPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jFiscalPrinterParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jScaleParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineScanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jScannerParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPrinters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcboMachineLabelPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_jLabelPrinterParams, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jcboMachineScannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachineScannerActionPerformed
        CardLayout cl = (CardLayout) (m_jScannerParams.getLayout());

        if ("scanpal2".equals(jcboMachineScanner.getSelectedItem()) 
                || "mercury130kz039".equals(jcboMachineScanner.getSelectedItem())
                || "massakvpm".equals(jcboMachineScanner.getSelectedItem())
                ) {
            cl.show(m_jScannerParams, "comm");
        } else {
            cl.show(m_jScannerParams, "empty");
        }
    }//GEN-LAST:event_jcboMachineScannerActionPerformed

    private void jcboMachineScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachineScaleActionPerformed
        CardLayout cl = (CardLayout) (m_jScaleParams.getLayout());

        if ("dialog1".equals(jcboMachineScale.getSelectedItem())
                || "samsungesp".equals(jcboMachineScale.getSelectedItem())
                || "tves4149".equals(jcboMachineScale.getSelectedItem())
                || "massak".equals(jcboMachineScale.getSelectedItem())
                ) {
            cl.show(m_jScaleParams, "comm");
        } else {
            cl.show(m_jScaleParams, "empty");
        }
    }//GEN-LAST:event_jcboMachineScaleActionPerformed

    private void jcboMachinePrinter3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachinePrinter3ActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterParams3.getLayout());

        if ("epson".equals(jcboMachinePrinter3.getSelectedItem())
                || "tmu220".equals(jcboMachinePrinter3.getSelectedItem())
                || "star".equals(jcboMachinePrinter3.getSelectedItem())
                || "samsung1251cyr".equals(jcboMachinePrinter3.getSelectedItem())
                || "bixolon866cyr".equals(jcboMachinePrinter3.getSelectedItem())
                || "senor866cyr".equals(jcboMachinePrinter3.getSelectedItem())
                || "citizen866cyr".equals(jcboMachinePrinter3.getSelectedItem())
                || "sprtpos58cyr".equals(jcboMachinePrinter3.getSelectedItem())
                || "gb2312chinese".equals(jcboMachinePrinter3.getSelectedItem())
                || "ithaca".equals(jcboMachinePrinter3.getSelectedItem())
                || "surepos".equals(jcboMachinePrinter3.getSelectedItem())
                || "elveskkm".equals(jcboMachinePrinter3.getSelectedItem())
                || "shtrihfr".equals(jcboMachinePrinter3.getSelectedItem())
                || "aurafr".equals(jcboMachinePrinter3.getSelectedItem())
                ) {
            cl.show(m_jPrinterParams3, "comm");
        } else if ("javapos".equals(jcboMachinePrinter3.getSelectedItem())) {
            cl.show(m_jPrinterParams3, "javapos");
        } else if ("printer".equals(jcboMachinePrinter3.getSelectedItem())) {
            cl.show(m_jPrinterParams3, "printer");
        } else {
            cl.show(m_jPrinterParams3, "empty");
        }
    }//GEN-LAST:event_jcboMachinePrinter3ActionPerformed

    private void jcboMachinePrinter2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachinePrinter2ActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterParams2.getLayout());

        if ("epson".equals(jcboMachinePrinter2.getSelectedItem())
                || "tmu220".equals(jcboMachinePrinter2.getSelectedItem())
                || "star".equals(jcboMachinePrinter2.getSelectedItem())
                || "samsung1251cyr".equals(jcboMachinePrinter2.getSelectedItem())
                || "bixolon866cyr".equals(jcboMachinePrinter2.getSelectedItem())
                || "senor866cyr".equals(jcboMachinePrinter2.getSelectedItem())
                || "citizen866cyr".equals(jcboMachinePrinter2.getSelectedItem())
                || "sprtpos58cyr".equals(jcboMachinePrinter2.getSelectedItem())
                || "gb2312chinese".equals(jcboMachinePrinter2.getSelectedItem())
                || "ithaca".equals(jcboMachinePrinter2.getSelectedItem())
                || "surepos".equals(jcboMachinePrinter2.getSelectedItem())
                || "elveskkm".equals(jcboMachinePrinter2.getSelectedItem())
                || "shtrihfr".equals(jcboMachinePrinter2.getSelectedItem())
                || "aurafr".equals(jcboMachinePrinter2.getSelectedItem())
                ) {
            cl.show(m_jPrinterParams2, "comm");
        } else if ("javapos".equals(jcboMachinePrinter2.getSelectedItem())) {
            cl.show(m_jPrinterParams2, "javapos");
        } else if ("printer".equals(jcboMachinePrinter2.getSelectedItem())) {
            cl.show(m_jPrinterParams2, "printer");
         } else {
            cl.show(m_jPrinterParams2, "empty");
        }
    }//GEN-LAST:event_jcboMachinePrinter2ActionPerformed

    private void jcboMachineDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachineDisplayActionPerformed
        CardLayout cl = (CardLayout) (m_jDisplayParams.getLayout());

        if ("epson".equals(jcboMachineDisplay.getSelectedItem()) || "ld200".equals(jcboMachineDisplay.getSelectedItem()) || "cd5220rus".equals(jcboMachineDisplay.getSelectedItem()) || "surepos".equals(jcboMachineDisplay.getSelectedItem())) {
            cl.show(m_jDisplayParams, "comm");
        } else if ("javapos".equals(jcboMachineDisplay.getSelectedItem())) {
            cl.show(m_jDisplayParams, "javapos");
        } else {
            cl.show(m_jDisplayParams, "empty");
        }
    }//GEN-LAST:event_jcboMachineDisplayActionPerformed

    private void jcboMachinePrinterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachinePrinterActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterParams1.getLayout());

        if ("epson".equals(jcboMachinePrinter.getSelectedItem())
                || "tmu220".equals(jcboMachinePrinter.getSelectedItem())
                || "samsung1251cyr".equals(jcboMachinePrinter.getSelectedItem())
                || "bixolon866cyr".equals(jcboMachinePrinter.getSelectedItem())
                || "senor866cyr".equals(jcboMachinePrinter.getSelectedItem())
                || "citizen866cyr".equals(jcboMachinePrinter.getSelectedItem())
                || "sprtpos58cyr".equals(jcboMachinePrinter.getSelectedItem())
                || "gb2312chinese".equals(jcboMachinePrinter.getSelectedItem())
                || "star".equals(jcboMachinePrinter.getSelectedItem())
                || "ithaca".equals(jcboMachinePrinter.getSelectedItem())
                || "surepos".equals(jcboMachinePrinter.getSelectedItem())
                || "elveskkm".equals(jcboMachinePrinter.getSelectedItem())
                || "shtrihfr".equals(jcboMachinePrinter.getSelectedItem())
                || "aurafr".equals(jcboMachinePrinter.getSelectedItem())
                ) {
            cl.show(m_jPrinterParams1, "comm");
        } else if ("javapos".equals(jcboMachinePrinter.getSelectedItem())) {
            cl.show(m_jPrinterParams1, "javapos");
        } else if ("printer".equals(jcboMachinePrinter.getSelectedItem())) {
            cl.show(m_jPrinterParams1, "printer");
        } else {
            cl.show(m_jPrinterParams1, "empty");
        }
    }//GEN-LAST:event_jcboMachinePrinterActionPerformed

    private void jcboMachineFiscalPrinterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachineFiscalPrinterActionPerformed
        CardLayout cl = (CardLayout) (m_jFiscalPrinterParams.getLayout());

        if ("aurafr".equals(jcboMachineFiscalPrinter.getSelectedItem()) 
            || "shtrihfr".equals(jcboMachineFiscalPrinter.getSelectedItem())
            ) {
            cl.show(m_jFiscalPrinterParams, "comm");
        } else if ("javapos".equals(jcboMachineFiscalPrinter.getSelectedItem())) {
            cl.show(m_jFiscalPrinterParams, "javapos");
        } else {
            cl.show(m_jFiscalPrinterParams, "empty");
        }
    }//GEN-LAST:event_jcboMachineFiscalPrinterActionPerformed

    private void jcboMachineLabelPrinterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboMachineLabelPrinterActionPerformed
        CardLayout cl = (CardLayout) (m_jLabelPrinterParams.getLayout());

        if ("godexezpl".equals(jcboMachineLabelPrinter.getSelectedItem()) 
            ) {
            cl.show(m_jLabelPrinterParams, "comm");
        } else {
            cl.show(m_jLabelPrinterParams, "empty");
        }
    }//GEN-LAST:event_jcboMachineLabelPrinterActionPerformed

    private void jcboPrinterPortTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPrinterPortTypeActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterPortParams1.getLayout());

        if ("serial".equals(jcboConnPrinter.getSelectedItem())) {
            cl.show(m_jPrinterPortParams1, "serial");
        } else {
            cl.show(m_jPrinterPortParams1, "file");
        }
    }//GEN-LAST:event_jcboPrinterPortTypeActionPerformed

    private void jcboPrinter2PortTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPrinter2PortTypeActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterPortParams2.getLayout());

        if ("serial".equals(jcboConnPrinter2.getSelectedItem())) {
            cl.show(m_jPrinterPortParams2, "serial");
        } else {
            cl.show(m_jPrinterPortParams2, "file");
        }
    }//GEN-LAST:event_jcboPrinter2PortTypeActionPerformed

    private void jcboPrinter3PortTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboPrinter3PortTypeActionPerformed
        CardLayout cl = (CardLayout) (m_jPrinterPortParams3.getLayout());

        if ("serial".equals(jcboConnPrinter3.getSelectedItem())) {
            cl.show(m_jPrinterPortParams3, "serial");
        } else {
            cl.show(m_jPrinterPortParams3, "file");
        }
    }//GEN-LAST:event_jcboPrinter3PortTypeActionPerformed

    private void jcboDisplayPortTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboDisplayPortTypeActionPerformed
        CardLayout cl = (CardLayout) (m_jDisplayPortParams.getLayout());

        if ("serial".equals(jcboConnDisplay.getSelectedItem())) {
            cl.show(m_jDisplayPortParams, "serial");
        } else {
            cl.show(m_jDisplayPortParams, "file");
        }
    }//GEN-LAST:event_jcboDisplayPortTypeActionPerformed

    private void jcboFiscalPrinterPortTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcboFiscalPrinterPortTypeActionPerformed
        CardLayout cl = (CardLayout) (m_jFiscalPrinterPortParams.getLayout());

        if ("serial".equals(jcboConnFiscalPrinter.getSelectedItem())) {
            cl.show(m_jFiscalPrinterPortParams, "serial");
        } else {
            cl.show(m_jFiscalPrinterPortParams, "file");
        }
    }//GEN-LAST:event_jcboFiscalPrinterPortTypeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cboPrinters;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JComboBox jcboConnDisplay;
    private javax.swing.JComboBox jcboConnFiscalPrinter;
    private javax.swing.JComboBox jcboConnLabelPrinter;
    private javax.swing.JComboBox jcboConnPrinter;
    private javax.swing.JComboBox jcboConnPrinter2;
    private javax.swing.JComboBox jcboConnPrinter3;
    private javax.swing.JComboBox jcboDisplaySerialPortDataBits;
    private javax.swing.JComboBox jcboDisplaySerialPortParity;
    private javax.swing.JComboBox jcboDisplaySerialPortSpeed;
    private javax.swing.JComboBox jcboDisplaySerialPortStopBits;
    private javax.swing.JComboBox jcboFiscalPrinterSerialPortDataBits;
    private javax.swing.JComboBox jcboFiscalPrinterSerialPortParity;
    private javax.swing.JComboBox jcboFiscalPrinterSerialPortSpeed;
    private javax.swing.JComboBox jcboFiscalPrinterSerialPortStopBits;
    private javax.swing.JComboBox jcboLAF;
    private javax.swing.JComboBox jcboMachineDisplay;
    private javax.swing.JComboBox jcboMachineFiscalPrinter;
    private javax.swing.JComboBox jcboMachineLabelPrinter;
    private javax.swing.JComboBox jcboMachinePrinter;
    private javax.swing.JComboBox jcboMachinePrinter2;
    private javax.swing.JComboBox jcboMachinePrinter3;
    private javax.swing.JComboBox jcboMachineScale;
    private javax.swing.JComboBox jcboMachineScanner;
    private javax.swing.JComboBox jcboMachineScreenmode;
    private javax.swing.JComboBox jcboPrinter1SerialPortDataBits;
    private javax.swing.JComboBox jcboPrinter1SerialPortParity;
    private javax.swing.JComboBox jcboPrinter1SerialPortSpeed;
    private javax.swing.JComboBox jcboPrinter1SerialPortStopBits;
    private javax.swing.JComboBox jcboPrinter2SerialPortDataBits;
    private javax.swing.JComboBox jcboPrinter2SerialPortParity;
    private javax.swing.JComboBox jcboPrinter2SerialPortSpeed;
    private javax.swing.JComboBox jcboPrinter2SerialPortStopBits;
    private javax.swing.JComboBox jcboPrinter3SerialPortDataBits;
    private javax.swing.JComboBox jcboPrinter3SerialPortParity;
    private javax.swing.JComboBox jcboPrinter3SerialPortSpeed;
    private javax.swing.JComboBox jcboPrinter3SerialPortStopBits;
    private javax.swing.JComboBox jcboSerialDisplay;
    private javax.swing.JComboBox jcboSerialFiscalPrinter;
    private javax.swing.JComboBox jcboSerialLabelPrinter;
    private javax.swing.JComboBox jcboSerialPrinter;
    private javax.swing.JComboBox jcboSerialPrinter2;
    private javax.swing.JComboBox jcboSerialPrinter3;
    private javax.swing.JComboBox jcboSerialScale;
    private javax.swing.JComboBox jcboSerialScanner;
    private javax.swing.JComboBox jcboTicketsBag;
    private javax.swing.JLabel jlblConnDisplay;
    private javax.swing.JLabel jlblConnPrinter;
    private javax.swing.JLabel jlblConnPrinter1;
    private javax.swing.JLabel jlblConnPrinter2;
    private javax.swing.JLabel jlblConnPrinter3;
    private javax.swing.JLabel jlblConnPrinter4;
    private javax.swing.JLabel jlblDisplayPort;
    private javax.swing.JLabel jlblPrinterPort;
    private javax.swing.JLabel jlblPrinterPort1;
    private javax.swing.JLabel jlblPrinterPort2;
    private javax.swing.JLabel jlblPrinterPort3;
    private javax.swing.JLabel jlblPrinterPort4;
    private javax.swing.JLabel jlblPrinterPort5;
    private javax.swing.JLabel jlblPrinterPort6;
    private javax.swing.JLabel jlblPrinterPortParams;
    private javax.swing.JLabel jlblPrinterPortParams1;
    private javax.swing.JLabel jlblPrinterPortParams2;
    private javax.swing.JLabel jlblPrinterPortParams3;
    private javax.swing.JLabel jlblPrinterPortParams4;
    private javax.swing.JTextField jtxtMachineHostname;
    private javax.swing.JPanel m_jDisplayParams;
    private javax.swing.JPanel m_jDisplayPortParams;
    private javax.swing.JPanel m_jFiscalPrinterParams;
    private javax.swing.JPanel m_jFiscalPrinterPortParams;
    private javax.swing.JPanel m_jLabelPrinterParams;
    private javax.swing.JPanel m_jPrinterParams1;
    private javax.swing.JPanel m_jPrinterParams2;
    private javax.swing.JPanel m_jPrinterParams3;
    private javax.swing.JPanel m_jPrinterPortParams1;
    private javax.swing.JPanel m_jPrinterPortParams2;
    private javax.swing.JPanel m_jPrinterPortParams3;
    private javax.swing.JPanel m_jScaleParams;
    private javax.swing.JPanel m_jScannerParams;
    private javax.swing.JTextField m_jtxtJPOSDrawer;
    private javax.swing.JTextField m_jtxtJPOSDrawer2;
    private javax.swing.JTextField m_jtxtJPOSDrawer3;
    private javax.swing.JTextField m_jtxtJPOSFiscalDrawer;
    private javax.swing.JTextField m_jtxtJPOSFiscalPrinter;
    private javax.swing.JTextField m_jtxtJPOSName;
    private javax.swing.JTextField m_jtxtJPOSPrinter;
    private javax.swing.JTextField m_jtxtJPOSPrinter2;
    private javax.swing.JTextField m_jtxtJPOSPrinter3;
    // End of variables declaration//GEN-END:variables
}
