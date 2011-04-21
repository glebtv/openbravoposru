/*
 * StopEJPrint.java
 *
 * Created on 16 January 2009, 13:02
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
 
    Interrupt Printing Full EKLZ Report/All Receipts For Daily Totals 
    Number Report/Document Duplicate By KPK
 
    Command:	A7H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		A7H. Length: 2 bytes.
    ·	Result Code (1 byte)

****************************************************************************/

public final class StopEJPrint extends PrinterCommand
{
    
    // in 
    private final int password;
    
    /**
     * Creates a new instance of StopEJPrint
     */
    public StopEJPrint(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xA7;
    }
    
    public final String getText()
    {
        return "Stop electronic journal document print";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
    }
}
