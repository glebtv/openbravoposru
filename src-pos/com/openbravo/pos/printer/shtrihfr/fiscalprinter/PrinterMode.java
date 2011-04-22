/*
 * PrinterMode.java
 *
 * Created on June 4 2008, 15:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.PrinterMode;

public class PrinterMode implements PrinterConst
{
    
    private final int mode;
    
    /** Creates a new instance of PrinterMode */
    public PrinterMode(int mode) 
    {
        this.mode = mode;
    }
    
    public int getLoMode()
    {
        return mode & 0x0F;
    }
    
    public int getHiMode()
    {
        return mode >> 4;
    }
    
    public int getValue()
    {
        return mode;
    }
    
    public boolean isReceiptOpened()
    {
        return getLoMode() == MODE_REC;
    }
    
    public boolean isSlipReceiptOpened()
    {
        return getLoMode() == MODE_SLP;
    }

    public boolean isSlipPrinting()
    {
        return getLoMode() == MODE_SLPPRINT;
    }

    public boolean isSlipPrintReady()
    {
        return getLoMode() == MODE_SLPREADY;
    }

    public boolean getDayOpened()
    {
        return getLoMode() != ECRMODE_CLOSED;
    }
    
    public boolean isDayClosed()
    {
        return getLoMode() == ECRMODE_CLOSED;
    }
  
    public boolean isTestMode()
    {
        return getLoMode() == ECRMODE_TEST;
    }
    
    public boolean canPrintZReport()
    {
        return (getLoMode() == MODE_24NOTOVER) ||
            (getLoMode() == MODE_24OVER);
    }
    
    public boolean isDayEndRequired()
    {
        return (getLoMode() == MODE_24OVER);
    }
}
