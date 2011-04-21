/*
 * PriceItem.java
 *
 * Created on January 15 2009, 15:09
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
    
public class PriceItem {
    
    public final long price;
    public final long quantity;
    public final int department;
    public final int tax1;
    public final int tax2;
    public final int tax3;
    public final int tax4;
    public final String text;
    
    /** Creates a new instance of PriceItem */
    public PriceItem(
        long price,
        long quantity,
        int department,
        int tax1,
        int tax2,
        int tax3,
        int tax4,
        String text
        ) 
    {
        MethodParameter.checkRange(quantity, 0, 0xFFFFFFFFFFL, "quantity");
        MethodParameter.checkRange(price, 0, 0xFFFFFFFFFFL, "price");
        
        this.price = price;
        this.quantity = quantity;
        this.department = department;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.text = text;
    }
}
