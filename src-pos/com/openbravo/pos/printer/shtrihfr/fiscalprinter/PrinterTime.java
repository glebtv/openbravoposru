/*
 * PrinterTime.java
 *
 * Created on April 2 2008, 17:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.StringTokenizer;
import com.openbravo.pos.printer.shtrihfr.util.StringUtils;

public class PrinterTime
{
    private final int hour;
    private final int min;
    private final int sec;
    
    public PrinterTime() 
    {
        hour = 0;
        min = 0;
        sec = 0;
    }
    
    public PrinterTime(int hour, int min, int sec) 
    {
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }
    
    public int getHour() { return hour; }
    public int getMin() { return min; }
    public int getSec() { return sec; }
    
    // 01:02:09
    public static String toText(PrinterTime time)
    {
        return 
            StringUtils.intToStr(time.getHour(), 2) + ":" + 
            StringUtils.intToStr(time.getMin(), 2) + ":" + 
            StringUtils.intToStr(time.getSec(), 2);
    }
    
    // 01:02:09
    public static PrinterTime fromText(String text)
    {
        StringTokenizer tokenizer = new StringTokenizer(text, ":");
        int hour = Integer.parseInt(tokenizer.nextToken());
        int min = Integer.parseInt(tokenizer.nextToken());
        int sec = Integer.parseInt(tokenizer.nextToken()) % 100;
        return new PrinterTime(hour, min, sec);
    }
    
}
