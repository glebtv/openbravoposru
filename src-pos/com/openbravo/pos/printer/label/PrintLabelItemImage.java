
package com.openbravo.pos.printer.label;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class PrintLabelItemImage implements PrintLabelItem {

    protected BufferedImage image;
    protected double scale;

    public PrintLabelItemImage(BufferedImage image, double scale) {
        this.image = image;
        this.scale = scale;
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int width) {
        g.drawImage(image, x + (width - (int)(image.getWidth() * scale)) / 2, y, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale), null);
    }

    @Override
    public int getHeight() {
        return (int) (image.getHeight() * scale);
    }
}
