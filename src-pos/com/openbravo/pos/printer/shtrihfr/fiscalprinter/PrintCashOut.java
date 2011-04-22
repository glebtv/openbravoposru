/*
 * PrintCashOut.java
 *
 * Created on 2 April 2008, 20:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 
    Cash-Out
 
    Command:	51H. Length: 10 bytes.
    ·	Operator password (4 bytes)
    ·	Sum to be cashed out (5 bytes)
    Answer:		51H. Length: 5 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
    ·	Current receipt number (2 bytes)
 
****************************************************************************/

public final class PrintCashOut extends PrinterCommand
{
    // in 
    private int password = 0;       // Operator password (4 bytes)
    private long sum = 0;           // Sum to be cashed in (5 bytes)
    // out
    private int operator = 0;       // Operator index number (1 byte) 1…30
    private int receiptNumber = 0;  // Current receipt number (2 bytes)
    
    /**
     * Creates a new instance of PrintCashOut
     */
    public PrintCashOut(int password, long sum) 
    {
        this.password = password;
        this.sum = sum;
    }
    
    public final int getCode()
    {
        return 0x51;
    }
    
    public final String getText()
    {
        return "Cash-out";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        MethodParameter.checkRange(sum, 0, 0xFFFFFFFFFFL, "sum");
        out.writeInt(password);
        out.writeLong(sum, 5);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
        receiptNumber = in.readShort();
    }
    
    public int getOperator()
    {
        return operator;
    }
    
    public int getReceiptNumber()
    {
        return receiptNumber;
    }
}
