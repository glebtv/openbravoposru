
package com.openbravo.pos.printer.label;

import java.awt.Font;
import java.awt.geom.AffineTransform;

public class BasicLabelForScreen extends BasicLabel {

    private static Font BASEFONT = new Font("Monospaced", Font.PLAIN, 12).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));
    private static int FONTHEIGHT = 20;
    private static double IMAGE_SCALE = 1.0;

    @Override
    protected Font getBaseFont() {
        return BASEFONT;
    }

    @Override
    protected int getFontHeight() {
        return FONTHEIGHT;
    }

    @Override
    protected double getImageScale() {
        return IMAGE_SCALE;
    }
}
