/*
 * ReadFMDayRange.java
 *
 * Created on 15 January 2009, 14:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************

    Get Dates And Shifts Ranges In FM
 
    Command:	64H. Length: 5 bytes.
    ·	Tax Officer password (4 bytes)
 
    Answer:		64H. Length: 12 bytes.
    ·	Result Code (1 byte)
    ·	Date of first daily totals record in FM (3 bytes) DD-MM-YY
    ·	Date of last daily totals record in FM (3 bytes) DD-MM-YY
    ·	Number of first daily totals record in FM (2 bytes) 0000…2100
    ·	Number of last daily totals record in FM (2 bytes) 0000…2100
    
****************************************************************************/

public class ReadFMDayRange 
{
    // in params
    private final int password;
    // out params
    private PrinterDate firstDate;
    private PrinterDate lastDate;
    private int firstShift;
    private int lastShift;
    
    /**
     * Creates a new instance of ReadFMDayRange
     */
    public ReadFMDayRange(int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0x64;
    }
    
    public final String getText()
    {
        return "Get dates and shifts ranges in FM";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        firstDate = in.readDate();
        lastDate = in.readDate();
        firstShift = in.readShort();
        lastShift = in.readShort();
    }
    
    public PrinterDate getFirstDate() {
        return firstDate;
    }
    
    public PrinterDate getLastDate() {
        return lastDate;
    }
    
    public int getFirstShift() {
        return firstShift;
    }
    
    public int getLastShift() {
        return lastShift;
    }
}
