/*
 * ImageUtils.java
 *
 * Created on 15 April 2008, 17:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.util;

/**
 *
 * @author V.Kravtsov
 */

import java.awt.*;
import java.awt.image.*;

public class ImageUtils {
    
    /**
     * Creates a new instance of ImageUtils
     */
    public ImageUtils() {
    }
    
    public static BufferedImage centerImageX(BufferedImage image, int width)
    {
        BufferedImage result = new BufferedImage(width, 
                image.getHeight(), image.getType());
 
        if (image.getWidth() > width) return image;
        int xOffset = (width - image.getWidth())/2;

        Graphics2D g2 = result.createGraphics();
        g2.setBackground(Color.white);
        g2.drawImage(image, null, xOffset, 0);
        return result;
    }
    
    public static BufferedImage indexToDirectColorModel(BufferedImage image)
    {
        BufferedImage result = new BufferedImage(image.getWidth(),
            image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        
        Graphics2D g2 = result.createGraphics();
        g2.drawImage(image, null, null);
        return result;
    }
}
