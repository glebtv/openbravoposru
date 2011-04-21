/*
 * PrintSaleRefund.java
 *
 * Created on April 2 2008, 20:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.MethodParameter;

/****************************************************************************
 
    Sale Refund
 
    Command:	82H. Length: 60 bytes.
    ·	Operator password (4 bytes)
    ·	Quantity (5 bytes) 0000000000…9999999999
    ·	Unit Price (5 bytes) 0000000000…9999999999
    ·	Department (1 byte) 0…16
    ·	Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Text (40 bytes)
 
    Answer:		82H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
****************************************************************************/

public final class PrintSaleRefund extends PrinterCommand
{
    // in 
    private final int password;
    private final long price;
    private final long quantity;
    private final int department;
    private final int tax1;
    private final int tax2;
    private final int tax3;
    private final int tax4;
    private final String text;
    // out
    private int operator;
         
    /**
     * Creates a new instance of PrintSaleRefund
     */
    public PrintSaleRefund(
        int password, long price, long quantity, int department,
        int tax1, int tax2, int tax3, int tax4, String text) 
    {
        MethodParameter.checkRange(quantity, 0, 0xFFFFFFFFFFL, "quantity");
        MethodParameter.checkRange(price, 0, 0xFFFFFFFFFFL, "price");
        
        this.password = password;
        this.price = price;
        this.quantity = quantity;
        this.department = department;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.text = text;
    }
    
    public final int getCode()
    {
        return 0x82;
    }
    
    public final String getText()
    {
        return "Sale refund";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeLong(quantity, 5);
        out.writeLong(price, 5);
        out.writeByte(department);
        out.writeByte(tax1);
        out.writeByte(tax2);
        out.writeByte(tax3);
        out.writeByte(tax4);
        out.writeString(text, 40);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
    }
}

