package com.openbravo.pos.printer.label;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class PrintLabelItemRectangle implements PrintLabelItem {

    protected int sX;
    protected int sY;
    protected int sW;
    protected int sH;

    public PrintLabelItemRectangle(String sLabelX, String sLabelY, String sWidth, String sHeight) {
        this.sX = Integer.parseInt(sLabelX);
        this.sY = Integer.parseInt(sLabelY);
        this.sW = Integer.parseInt(sWidth);
        this.sH = Integer.parseInt(sHeight);        
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int width) {
//        g.setStroke(new BasicStroke(4));
        g.drawRect(sX, sY, sW, sH);
    }

//    @Override
//    public int getHeight() {
//        return sH;
//    }
}
