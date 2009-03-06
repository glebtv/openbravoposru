//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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

