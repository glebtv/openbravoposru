/*
 * ReadFMTotals.java
 *
 * Created on 15 January 2009, 13:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.command;

/**
 *
 * @author V.Kravtsov
 */

import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandInputStream;
import com.openbravo.pos.printer.shtrihfr.fiscalprinter.CommandOutputStream;

/****************************************************************************

    Get fiscal memory Totals
 
    Command:	62H. Length: 6 bytes.
    ·	Administrator or System Administrator password (4 bytes) 29, 30
    ·	Report type (1 byte) 
        «0» – grand totals, 
        «1» – grand totals after the last refiscalization
 
    Answer:		62H. Length: 29 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 29, 30
    ·	Grand totals of sales (8 bytes)
    ·	Grand totals of buys (6 bytes) If there is no FM2, the value is FFh FFh FFh FFh FFh FFh
    ·	Grand totals of sale refunds (6 bytes) If there is no FM2, the value is FFh FFh FFh FFh FFh FFh
    ·	Grand totals of buy refunds (6 bytes) If there is no FM2, the value is FFh FFh FFh FFh FFh FFh
    
****************************************************************************/
public final class ReadFMTotals extends PrinterCommand {

    // in params
    private final int password;
    private final byte mode;
    // out params
    private int operator;
    private long salesTotal;
    private long buyTotal;
    private long retSaleTotal;
    private long retBuyTotal;

    /**
     * Creates a new instance of ReadFMTotals
     */
    public ReadFMTotals(
            int password, 
            byte mode) {
        this.password = password;
        this.mode = mode;
    }

    public final int getCode() {
        return 0x62;
    }

    public final String getText() {
        return "Get FM totals";
    }

    public final void encode(CommandOutputStream out) throws Exception {
        out.writeInt(password);
        out.writeByte(mode);
    }

    public final void decode(CommandInputStream in) throws Exception {
        operator = in.readByte();
        salesTotal = in.readLong(8);
        buyTotal = in.readLong(6);
        retSaleTotal = in.readLong(6);
        retBuyTotal = in.readLong(6);
    }

    public int getOperator() {
        return operator;
    }

    public long getSalesTotal() {
        return salesTotal;
    }

    public long getBuyTotal() {
        return buyTotal;
    }

    public long getRetSaleTotal() {
        return retSaleTotal;
    }

    public long getRetBuyTotal() {
        return retBuyTotal;
    }
}
