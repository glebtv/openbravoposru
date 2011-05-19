
package com.openbravo.pos.printer.label;

import com.openbravo.pos.printer.label.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class BasicLabel implements PrintLabelItem {

    protected java.util.List<PrintLabelItem> m_aCommands;
//    protected PrintLabelItemLine pil;
    protected int m_iBodyHeight;

    public BasicLabel() {
        m_aCommands = new ArrayList<PrintLabelItem>();
//        pil = null;
        m_iBodyHeight = 0;
    }

    protected abstract Font getBaseFont();

    protected abstract int getFontHeight();

    protected abstract double getImageScale();

//    public int getHeight() {
//        return m_iBodyHeight;
//    }

    public void draw(Graphics2D g2d, int x, int y, int width) {

        int currenty = y;
        for (PrintLabelItem pi : m_aCommands) {
            pi.draw(g2d, x, currenty, width);
//            currenty += pi.getHeight();
        }
    }
    
    public java.util.List<PrintLabelItem> getCommands() {
        return m_aCommands;
    }

    public void printImage(BufferedImage image) {

        PrintLabelItem pi = new PrintLabelItemImage(image, getImageScale());
        m_aCommands.add(pi);
//        m_iBodyHeight += pi.getHeight();
    }

    public void printBarCode(String type,  String sLabelX, String sLabelY, String sHeight, String position, String code) {

        PrintLabelItem pi = new PrintLabelItemBarcode(type, sLabelX, sLabelY, sHeight, position, code, getImageScale());
        m_aCommands.add(pi);
//        m_iBodyHeight += pi.getHeight();
    }
    
    public void printRectangle(String sX, String sY, String sW, String sH){
        PrintLabelItem pi = new PrintLabelItemRectangle(sX, sY, sW, sH);
        m_aCommands.add(pi);
//        m_iBodyHeight += pi.getHeight();        
    }

    public void printText(String sFontPoint, String sLabelX, String sLabelY, String sText) {
        PrintLabelItem pi = new PrintLabelItemText(sFontPoint, sLabelX, sLabelY, sText);
        m_aCommands.add(pi);
//        m_iBodyHeight += pi.getHeight();        
    }
}
