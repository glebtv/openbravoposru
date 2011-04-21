/*
 * PrintDiscountVoid.java
 *
 * Created on January 16 2009, 12:03
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
 
    Void Discount
 
    Command:	8AH. Length: 54 bytes.
    ·	Operator password (4 bytes)
    ·	Void Discount value (5 bytes) 0000000000…9999999999
    ·	Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Text (40 bytes)
 
    Answer:		8AH. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30

****************************************************************************/

public class PrintDiscountVoid 
{
    // in 
    private final int password;
    private final AmountItem item;
    // out
    private int operator = 0;
    
    /**
     * Creates a new instance of PrintDiscountVoid
     */
    public PrintDiscountVoid(int password, AmountItem item)
    {
        this.password = password;
        this.item = item;
    }
    
    public final int getCode()
    {
        return 0x8A;
    }
    
    public final String getText()
    {
        return "Void discount";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeLong(item.amount, 5);
        out.writeByte(item.tax1);
        out.writeByte(item.tax2);
        out.writeByte(item.tax3);
        out.writeByte(item.tax4);
        out.writeString(item.text, 40);
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
