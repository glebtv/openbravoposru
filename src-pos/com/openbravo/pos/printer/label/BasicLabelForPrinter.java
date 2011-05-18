
package com.openbravo.pos.printer.label;

import com.openbravo.pos.printer.label.*;
import java.awt.Font;
import java.awt.geom.AffineTransform;

public class BasicLabelForPrinter extends BasicLabel {

        private static Font BASEFONT = new Font("Monospaced", Font.PLAIN, 7).deriveFont(AffineTransform.getScaleInstance(1.0, 1.40));
        private static int FONTHEIGHT = 12;
        private static double IMAGE_SCALE = 0.65;

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
