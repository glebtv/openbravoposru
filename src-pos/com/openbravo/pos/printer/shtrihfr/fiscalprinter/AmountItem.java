/*
 * AmountItem.java
 *
 * Created on 15 January 2009, 16:36
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

public class AmountItem 
{
    public final long amount;
    public final int tax1;
    public final int tax2;
    public final int tax3;
    public final int tax4;
    public final String text;
        
    /** Creates a new instance of AmountItem */
    public AmountItem(
        long amount,
        int tax1,
        int tax2,
        int tax3,
        int tax4,
        String text
        ) 
    {
        MethodParameter.checkRange(amount, 0, 0xFFFFFFFFFFL, "amount");
        
        this.amount = amount;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.text = text;
    }
}
