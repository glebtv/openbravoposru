/*
 * BeginFiscalReceipt.java
 *
 * Created on April 2 2008, 21:17
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
 
    Open Receipt
 
    Command:	8DH. Length: 6 bytes.
    ·	Operator password (4 bytes)
    ·	Receipt type (1 byte):		
        0 – Sale;
        1 – Buy;
        2 – Sale Refund;
        3 – Buy Refund.
 
    Answer:		8DH. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
****************************************************************************/

public final class BeginFiscalReceipt extends PrinterCommand
{
    // in 
    private final int password;
    private final int receiptType;
    // out
    private int operator;
    
    /**
     * Creates a new instance of BeginFiscalReceipt
     */
    public BeginFiscalReceipt(int password, int receiptType) 
    {
        this.password = password;
        this.receiptType = receiptType;
    }
    
    public final int getCode()
    {
        return 0x8D;
    }
    
    public final String getText()
    {
        return "Open receipt";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(receiptType);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
    }
    
    public int getOperator()
    {
        return operator;
    }
}
