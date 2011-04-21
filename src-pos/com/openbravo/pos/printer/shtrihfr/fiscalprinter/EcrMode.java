/*
 * EcrMode.java
 *
 * Created on 13 March 2008, 13:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

class EcrMode implements PrinterConst 
{
    private int value = 0;
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int value)
    {
        this.value = value;
    }
    
    public static String getValueText(int value)
    {
        switch (value)
        {
            case MODE_DUMPMODE   : return S_MODE_DUMPMODE;
            case MODE_24NOTOVER  : return S_MODE_24NOTOVER;
            case MODE_24OVER     : return S_MODE_24OVER;
            case MODE_CLOSED     : return S_MODE_CLOSED;
            case MODE_LOCKED     : return S_MODE_LOCKED;
            case MODE_WAITDATE   : return S_MODE_WAITDATE;
            case MODE_POINTPOS   : return S_MODE_POINTPOS;
            case MODE_REC        : return S_MODE_REC;
            case MODE_TECH       : return S_MODE_TECH;
            case MODE_TEST       : return S_MODE_TEST;
            case MODE_FULLREPORT : return S_MODE_FULLREPORT;
            case MODE_EKLZREPORT : return S_MODE_EKLZREPORT;
            case MODE_SLP        : return S_MODE_SLP;
            case MODE_SLPPRINT   : return S_MODE_SLPPRINT;
            case MODE_SLPREADY   : return S_MODE_SLPREADY;
            default              : return S_MODE_UNKNOWN;
        }
    }

    public String getText()
    {
        return getValueText(value);
    }
}

