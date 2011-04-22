/*
 * ReadEJStatus2.java
 *
 * Created on 16 January 2009, 13:36
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
 
    Get EKLZ Status 2
 
    Command:	AEH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		AEH. Length: 28 bytes.
    ·	Result Code (1 byte)
    ·	Number of last daily totals (2 bytes) 0000…2100
    ·	Grand totals of sales (6 bytes) 000000000000…999999999999
    ·	Grand totals of buys (6 bytes) 000000000000…999999999999
    ·	Grand totals of sale refunds (6 bytes) 000000000000…999999999999
    ·	Grand totals of buy refunds (6 bytes) 000000000000…999999999999

****************************************************************************/

public final class ReadEJStatus2 extends PrinterCommand
{
    // in 
    private final int password;
    // out
    // Number of last daily totals (2 bytes) 0000…2100
    private int dayNumber;
    // Grand totals of sales (6 bytes) 000000000000…999999999999
    private long saleTotal;
    // Grand totals of buys (6 bytes) 000000000000…999999999999
    private long buyTotal;
    // Grand totals of sale refunds (6 bytes) 000000000000…999999999999
    private long saleRefundTotal;
    // Grand totals of buy refunds (6 bytes) 000000000000…999999999999
    private long buyRefundTotal;
    
    
    /**
     * Creates a new instance of ReadEJStatus2
     */
    public ReadEJStatus2(
        int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAE;
    }
    
    public final String getText()
    {
        return "Read electronic journal status by code 2";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        dayNumber = in.readShort();
        saleTotal = in.readLong(6);
        buyTotal = in.readLong(6);
        saleRefundTotal = in.readLong(6);
        buyRefundTotal = in.readLong(6);
    }
}
