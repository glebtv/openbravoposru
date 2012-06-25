/*
 * PrinterStatus.java
 */
package com.openbravo.pos.printer.aurafr.fiscalprinter;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class PrinterStatus {

    public int iCashierNumber;    
    public int iFPNumber;
//    public PrinterDate date = new PrinterDate();
//    public PrinterTime time = new PrinterTime();
    public int iFlags;
    public int iFPSerialNumber;
    public int iFPModel;
    public String sFirmwareVersion = "";
//    private PrinterMode mode = new PrinterMode(0);
    public int iCurrentReceiptNumber;
    public int iLastZReport;
    public int iCurrentReceiptStatus;
    public long lCurrentReceiptAmount;
    public int iDecimalPoint;
    public int portNumber;

    /**
     * Creates a new instance of FullPrinterStatus
     */
    public PrinterStatus() {
    }

//    public PrinterMode getMode() {
//        return mode;
//    }
//
//    public PrinterFlags getFlags() {
//        return new PrinterFlags(flags);
//    }
//    public void setMode(int value) {
//        mode = new PrinterMode(value);
//    }

    public String getFirmwareVersion() {
        return sFirmwareVersion;
    }
}
