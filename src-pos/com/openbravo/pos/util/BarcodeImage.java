/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.impl.postnet.POSTNETBean;
import org.krysalis.barcode4j.impl.qr.QRCodeBean;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.impl.upcean.EAN8Bean;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.impl.upcean.UPCEBean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

/**
 *
 * @author adrian
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class BarcodeImage {

    public static Image getBarcodeCodabar(String value) {
        AbstractBarcodeBean barcode = new CodabarBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeCode39(String value) {
        AbstractBarcodeBean barcode = new Code39Bean();
        value = BarcodeString.getBarcodeStringCode39(value);
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        barcode.setModuleWidth(1.0);
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeInterleaved2Of5(String value) {
        AbstractBarcodeBean barcode = new Interleaved2Of5Bean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodePOSTNET(String value) {
        AbstractBarcodeBean barcode = new POSTNETBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeUPCA(String value) {
        AbstractBarcodeBean barcode = new UPCABean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeUPCE(String value) {
        AbstractBarcodeBean barcode = new UPCEBean();
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeEAN13(String value) {
        AbstractBarcodeBean barcode = new EAN13Bean();
        value = BarcodeString.getBarcodeStringEAN13(value);
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        barcode.setModuleWidth(1.0);
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);
        return getBarcode(value, barcode);
    }

    public static Image getBarcodeEAN8(String value) {
        AbstractBarcodeBean barcode = new EAN8Bean();
        value = BarcodeString.getBarcodeStringEAN8(value);
        barcode.setMsgPosition(HumanReadablePlacement.HRP_BOTTOM);
        barcode.setModuleWidth(1.0);
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);
        return getBarcode(value, barcode);
    }

    public static Image getBarcode128(String value) {
        AbstractBarcodeBean barcode = new Code128Bean();
        value = BarcodeString.getBarcodeStringCode128(value);
        barcode.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        barcode.setModuleWidth(1.0);
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);
        return getBarcode(value, barcode);
    }

    public static Image getDataMatrix(String value) {
        AbstractBarcodeBean barcode = new DataMatrixBean();
        value = BarcodeString.getBarcodeStringDataMatrix(value);
        barcode.setModuleWidth(5.0);
        return getBarcode(value, barcode);
    }

    public static Image getQRCode(String value) {
        QRCodeBean m_qrcode = new QRCodeBean();
        value = BarcodeString.getBarcodeStringQRCode(value);
        m_qrcode.setEncoding("Cp1251");
        m_qrcode.setModuleWidth(5.0);
        return getBarcode(value, m_qrcode);
    }

    private static Image getBarcode(String value, AbstractBarcodeBean barcode) {

        BarcodeDimension dim = barcode.calcDimensions(value);
        int width = (int) dim.getWidth(0) + 20;
        int height = (int) dim.getHeight(0);

        BufferedImage imgtext = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imgtext.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);

        try {
            barcode.generateBarcode(new Java2DCanvasProvider(g2d, 0), value);
        } catch (IllegalArgumentException e) {
            g2d.drawRect(0, 0, width - 1, height - 1);
            g2d.drawString(value, 2, height - 3);
        }

        g2d.dispose();

        return imgtext;
    }
}
