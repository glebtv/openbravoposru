/*
 * PrintBarcode.java
 *
 * Created on March 7 2008, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */


/*****************************************************************************

    Print barcode

    Command:	C2H. Length: 10 bytes.
    ·	Operator password (4 bytes)
    ·	Barcode (5 bytes) 000000000000…999999999999

    Answer:		C2H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
 
*****************************************************************************/

public final class PrintBarcode extends PrinterCommand
{
    // in
    private final int password;         // operator password
    private final String barcode;       // barcode text
    // out
    private int operator;
    
    public PrintBarcode(int password, String barcode)
    {
        this.password = password;
        this.barcode = barcode;
    }
    
    public final int getCode()
    {
        return 0xC2;
    }
    
    public final String getText()
    {
        return "Print barcode";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeLong(Long.parseLong(barcode), 5);
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
