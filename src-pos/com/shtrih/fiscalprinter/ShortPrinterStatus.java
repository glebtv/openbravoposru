/*
 * ShortPrinterStatus.java
 *
 * Created on April 2 2008, 18:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.fiscalprinter.PrinterMode;
    
public class ShortPrinterStatus implements PrinterConst 
{
    
    private PrinterMode mode = new PrinterMode(0);
    
    public int flags = 0;
    public int advancedMode;
    public int fmResultCode;
    public int eklzResultCode;
    public int quantityOfOperations;
    public double batteryVoltage = 0;
    public double powerSourceVoltage = 0;
    public int operatorNumber;
    
    /**
     * Creates a new instance of ShortPrinterStatus
     */
    public ShortPrinterStatus() {
    }

    
    public PrinterMode getMode()
    {
        return mode;
    }
    
    public void setMode(int value)
    {
        mode = new PrinterMode(value);
    }
}
