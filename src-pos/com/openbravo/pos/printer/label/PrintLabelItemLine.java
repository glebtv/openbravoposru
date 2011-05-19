
package com.openbravo.pos.printer.label;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class PrintLabelItemLine implements PrintLabelItem {

    protected Font font;
    protected int fontheight;
    protected int textsize;
    protected List<StyledText> m_atext;

    /** Creates a new instance of PrinterItemLine */
    public PrintLabelItemLine(int textsize, Font font, int fontheight) {
        this.textsize = textsize;
        this.font = font;
        this.fontheight = fontheight;

        m_atext = new ArrayList<StyledText>();
    }

    public void addText(int style, String text) {
        m_atext.add(new StyledText(style, text));
    }

    public void draw(Graphics2D g, int x, int y, int width) {

        MyPrinterLabelState ps = new MyPrinterLabelState(textsize);
        float left = x;
        for (int i = 0; i < m_atext.size(); i++) {
            StyledText t = m_atext.get(i);
            g.setFont(ps.getFont(font, t.style));
            g.drawString(t.text, left, (float) y);
            left += g.getFontMetrics().getStringBounds(t.text, g).getWidth();
        }
    }

    public int getHeight() {
        return fontheight * MyPrinterLabelState.getLineMult(textsize);
    }

    protected static class StyledText {

        public StyledText(int style, String text) {
            this.style = style;
            this.text = text;            
        }
        public int style;
        public String text;
    }
}
