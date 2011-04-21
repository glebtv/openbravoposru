/*
 * FullPrinterStatus.java
 *
 * Created on April 2 2008, 19:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class FullPrinterStatus implements PrinterConst
{
    private PrinterMode mode = new PrinterMode(0);
        
    public int operatorNumber;
    public int flags;
    public int advancedMode;
    public int fmResultCode;
    public String firmwareVersion = "";
    public int firmwareBuild;
    public PrinterDate firmwareDate = new PrinterDate();
    public int logicalNumber;
    public int documentNumber;
    public int portNumber;
    public String fmFirmwareVersion;
    public int fmFirmwareBuild;
    public PrinterDate fmFirmwareDate = new PrinterDate();
    public PrinterDate date = new PrinterDate();
    public PrinterTime time = new PrinterTime();
    public FiscalMemoryFlags fmFlags = new FiscalMemoryFlags();
    public int serial;
    public int sessionNumber;
    public int freeRecordInFM;
    public int registrationNumber;
    public int freeRegistration;
    public long inn;
    public String innText = "";
    public String serialText = "";
    
    /**
     * Creates a new instance of FullPrinterStatus
     */
    public FullPrinterStatus() 
    {
    }
    
    public PrinterMode getMode()
    {
        return mode;
    }
    
    public PrinterFlags getFlags()
    {
        return new PrinterFlags(flags);
    }
    
    public void setMode(int value)
    {
        mode = new PrinterMode(value);
    }
    
    // "1.0 build 1234 from 12.02.2006"
    public String getFirmwareRevision()
    {
        return firmwareVersion + " build " + String.valueOf(firmwareBuild) +
            " from " + firmwareDate.toString();
    }
}
