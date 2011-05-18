
package com.openbravo.pos.printer.label;

import java.awt.*;
import java.awt.geom.*;

import com.openbravo.pos.printer.DevicePrinter;

public class MyPrinterLabelState {

    private int m_iSize;

    /** Creates a new instance of PrinterState */
    public MyPrinterLabelState(int iSize) {
        m_iSize = iSize;
    }

    public int getLineMult() {
        return getLineMult(m_iSize);
    }

    public static int getLineMult(int iSize) {
        switch (iSize) {
            case 0:
            case 2:
                return 1;
            case 1:
            case 3:
                return 2;
            default:
                return 1;
        }
    }

    public Font getFont(Font baseFont, int iStyle) {

        Font f;
        AffineTransform a;
        switch (m_iSize) {
            case 0:
                f = baseFont;
                break;
            case 2:
                a = AffineTransform.getScaleInstance(2.0, 1.0);
                a.preConcatenate(baseFont.getTransform());
                f = baseFont.deriveFont(a);
                break;
            case 1:
                a = AffineTransform.getScaleInstance(1.0, 2.0);
                a.preConcatenate(baseFont.getTransform());
                f = baseFont.deriveFont(a);
                break;
            case 3:
                a = AffineTransform.getScaleInstance(2.0, 2.0);
                a.preConcatenate(baseFont.getTransform());
                f = baseFont.deriveFont(a);
                break;
            default:
                f = baseFont;
                break;
        }
        f = f.deriveFont((iStyle & DevicePrinter.STYLE_BOLD) != 0 ? Font.BOLD : baseFont.getStyle());

        return f;
    }
}
