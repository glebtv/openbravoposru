/*
 * StringUtils.java
 *
 * Created on 30 August 2007, 20:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

package com.openbravo.pos.printer.shtrihfr.util;

import java.util.Vector;

public class StringUtils {
    
    /** Creates a new instance of StringUtils */
    public StringUtils() {
    }
   
    public static String[] split(String text, char c)
    {
        String data = "";
        Vector result = new Vector();
        for (int i=0;i<text.length();i++)
        {
            if (text.charAt(i) == c)
            {
                result.add(data);
                data = "";
            } else
            {
                data += c;
            }
        }
        if (data != "") {result.add(data);}
        return (String[])result.toArray();
    }
    
    public static String centerLine(String data, int lineLength)
    {
        int len = Math.min(data.length(), lineLength);
        String s = data.substring(0, len);
        len = (lineLength - len)/2;
        for (int i=0;i<len;i++)
            s = " " + s;
        return s;
    }
    
    public static String intToStr(int value, int len)
    {
        String result = String.valueOf(value);
        if (result.length() > len)
        {
            result = String.copyValueOf(result.toCharArray(), 0, len);
        }
        int count = len - result.length();
        for (int i=0;i<count;i++)
        {
            result = "0" + result;
        }
        return result;
    }
    
    public static String boolToStr(boolean value)
    {
        if (value) {
            return "1";
        } else {
            return "0";
        }
    }
}
