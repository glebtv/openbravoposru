/*
 * CloseRecParams.java
 *
 * Created on 15 January 2009, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

import com.openbravo.pos.printer.shtrihfr.util.MethodParameter;

/**
 *
 * @author V.Kravtsov
 */
public class CloseRecParams {
    
    public final long sum1;
    public final long sum2;
    public final long sum3;
    public final long sum4;
    public final int discount;
    public final String text;
    public final int tax1;
    public final int tax2;
    public final int tax3;
    public final int tax4;

    public CloseRecParams(long sum1, long sum2, long sum3, long sum4, 
        int tax1, int tax2, int tax3, int tax4, int discount, String text)
    {
        // check parameters
        MethodParameter.checkRange(sum1, 0, 0xFFFFFFFFFFL, "sum1");
        MethodParameter.checkRange(sum2, 0, 0xFFFFFFFFFFL, "sum2");
        MethodParameter.checkRange(sum3, 0, 0xFFFFFFFFFFL, "sum3");
        MethodParameter.checkRange(sum4, 0, 0xFFFFFFFFFFL, "sum4");
        MethodParameter.checkRange(tax1, 0, 3, "tax1");
        MethodParameter.checkRange(tax2, 0, 3, "tax2");
        MethodParameter.checkRange(tax3, 0, 3, "tax3");
        MethodParameter.checkRange(tax4, 0, 3, "tax4");
        MethodParameter.checkRange(discount, 0, 9999, "discount");

        this.sum1 = sum1;
        this.sum2 = sum2;
        this.sum3 = sum3;
        this.sum4 = sum4;
        this.tax1 = tax1;
        this.tax2 = tax2;
        this.tax3 = tax3;
        this.tax4 = tax4;
        this.discount = discount;
        this.text = text;
    }
}
