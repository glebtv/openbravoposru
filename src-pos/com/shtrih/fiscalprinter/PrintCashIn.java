/*
 * PrintCashIn.java
 *
 * Created on 2 April 2008, 20:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

import com.shtrih.util.MethodParameter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************
 
    Cash In
 
    Command:	50H. Length: 10 bytes.
    ·	Operator password (4 bytes)
    ·	Sum to be cashed in (5 bytes)
    Answer:		50H. Length: 5 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
    ·	Current receipt number (2 bytes)
 
****************************************************************************/

public final class PrintCashIn extends PrinterCommand
{
    // in 
    private int password = 0;       // Operator password (4 bytes)
    private long sum = 0;           // Sum to be cashed in (5 bytes)
    // out
    private int operator = 0;       // Operator index number (1 byte) 1…30
    private int receiptNumber = 0;  // Current receipt number (2 bytes)
    
    /**
     * Creates a new instance of PrintCashIn
     */
    public PrintCashIn(int password, long sum) 
    {
        this.password = password;
        this.sum = sum;
    }
    
    public final int getCode()
    {
        return 0x50;
    }
    
    public final String getText()
    {
        return "Cash-in";
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
