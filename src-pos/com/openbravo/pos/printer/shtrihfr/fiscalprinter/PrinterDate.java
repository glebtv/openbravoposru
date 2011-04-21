/*
 * PrinterDate.java
 *
 * Created on April 2 2008, 17:04
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

public class PrinterDate
{
    private final int day;
    private final int month;
    private final int year; 
    
    public PrinterDate() 
    {
        this.day = 0;
        this.month = 0;
        this.year = 0;
    }
    
    public PrinterDate(int day, int month, int year) 
    {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    public int getDay() { return day;}
    public int getMonth() { return month;}
    public int getYear() { return year;}
    
    public String toString()
    {
        return 
            StringUtils.intToStr(day, 2) + "." + 
            StringUtils.intToStr(month, 2) + "." + 
            StringUtils.intToStr(year + 2000, 4);
    }
    
    // 01.02.09
    public static String toText(PrinterDate date)
    {
        return 
            StringUtils.intToStr(date.getDay(), 2) + "." + 
            StringUtils.intToStr(date.getMonth(), 2) + "." + 
            StringUtils.intToStr(date.getYear(), 2);
    }
    
    // 01.02.2009 or 01.02.09
    public static PrinterDate fromText(String text)
    {
        StringTokenizer tokenizer = new StringTokenizer(text, ".");
        int day = Integer.parseInt(tokenizer.nextToken());
        int month = Integer.parseInt(tokenizer.nextToken());
        int year = Integer.parseInt(tokenizer.nextToken()) % 100;
        return new PrinterDate(day, month, year);
    }
}
