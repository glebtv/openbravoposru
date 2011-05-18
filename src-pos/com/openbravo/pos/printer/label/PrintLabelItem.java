
package com.openbravo.pos.printer.label;

import java.awt.Graphics2D;

public interface PrintLabelItem {
    
    public int getHeight();
    public void draw(Graphics2D g, int x, int y, int width);
}
