/*
 * PrinterFlags.java
 *
 * Created on April 2 2008, 17:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.util.BitUtils;
    
public class PrinterFlags 
{
    private final int value;
    
    public PrinterFlags(int value)
    {
        this.value = value;
    }
     
    public int getValue()
    {
        return value;
    }

    // journal paper is near end
    
    public boolean getJrnNearEnd(){ 
        return !BitUtils.testBit(value, 0);
    }

    // receipt paper is near end
    
    public boolean getRecNearEnd() {
        return !BitUtils.testBit(value, 1);
    }

    public boolean getSlpEmpty()
    {
        return !BitUtils.testBit(value, 2);
    }
    
    public boolean getSlpNearEnd()
    {
        return BitUtils.testBit(value, 3);
    }
    
    public boolean getAmountPointPosition()
    {
        return BitUtils.testBit(value, 4);
    }
    
    // electronic journal present
    public boolean getIsEklzPresent()
    {
        return BitUtils.testBit(value, 5);
    }
    
    // journal paper is empty
    public boolean getJrnEmpty()
    {
        return !BitUtils.testBit(value, 6);
    }
    
    // receipt paper is empty
    public boolean getRecEmpty()
    {
        return !BitUtils.testBit(value, 7);
    }
    
    // journal station lever is up
    public boolean getIsJournalLever()
    {
        return BitUtils.testBit(value, 8);
    }
    
    // receipt station lever is up
    public boolean getIsReceiptLever()
    {
        return BitUtils.testBit(value, 9);
    }
    
    // printer cover is opened
    public boolean getIsCoverOpened()
    {
        return BitUtils.testBit(value, 10);
    }
    
    // cash drawer is opened
    public boolean getIsDrawerOpened()
    {
        return BitUtils.testBit(value, 11);
    }
    
    // right printer sensor failure
    public boolean getIsRightSensorFailure()
    {
        return BitUtils.testBit(value, 12);
    }
    
    // left printer sensor failure    
    public boolean getIsLeftSensorFailure()
    {
        return BitUtils.testBit(value, 13);
    }
    
    // electronic journal near end
    public boolean getIsEklzOverflow()
    {
        return BitUtils.testBit(value, 14);
    }
    
    // extended quantity enabled    
    public boolean getQuantityPointPosition()
    {
        return BitUtils.testBit(value, 15);
    }
}
