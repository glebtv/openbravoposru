/*
 * PrintCharge.java
 *
 * Created on April 2 2008, 20:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/****************************************************************************
 
    Surcharge

    Command:	87H. Length: 54 bytes.
    ·	Operator password (4 bytes)
    ·	Surcharge value (5 bytes) 0000000000…9999999999
    ·	Tax 1 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 2 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 3 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Tax 4 (1 byte) «0» – no tax, «1»…«4» – tax ID
    ·	Text (40 bytes)

    Answer:		87H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
****************************************************************************/

public final class PrintCharge extends PrinterCommand
{
    // in
    private final int password;
    private final long amount;
    private final int tax1;
    private final int tax2;
    private final int tax3;
    private final int tax4;
    private final String text;
    // out
    private int operator;

    
    /**
     * Creates a new instance of PrintCharge
     */
    public PrintCharge(
        int password,
        long amount,
        int tax1, 
        int tax2,
        int tax3,
        int tax4,
        String text) 
    {
        MethodParameter.checkRange(amount, 0, 0xFFFFFFFFFFL, "sum");
        
        this.password = password;
        this.amount = amount;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.text = text;
    }
    
    public final int getCode()
    {
        return 0x87;
    }
    
    public final String getText()
    {
        return "Surcharge";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeInt(password);
        out.writeLong(amount, 5);
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
