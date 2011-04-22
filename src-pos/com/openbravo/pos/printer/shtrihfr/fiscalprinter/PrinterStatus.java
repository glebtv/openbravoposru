/*
 * PrinterStatus.java
 *
 * Created on 11 Ноябрь 2009 г., 17:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class PrinterStatus {
    
    public int mode;
    public int flags;
    public int advancedMode;
    public int operatorNumber;
    
    /** Creates a new instance of PrinterStatus */
    public PrinterStatus()
    {
    }
    
    public PrinterFlags getFlags()
    {
        return new PrinterFlags(flags);
    }
        
    public PrinterMode getMode()
    {
        return new PrinterMode(mode);
    }
}
