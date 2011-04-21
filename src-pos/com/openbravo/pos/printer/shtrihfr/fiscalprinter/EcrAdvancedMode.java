/*
 * EcrAdvancedMode.java
 *
 * Created on 13 March 2008, 13:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

class EcrAdvancedMode implements PrinterConst 
{
    
    public String getValueText(int value)
    {
        switch (value)
        {
                case ECR_ADVANCEDMODE_IDLE      : return S_ECR_ADVANCEDMODE_IDLE;
                case ECR_ADVANCEDMODE_PASSIVE   : return S_ECR_ADVANCEDMODE_PASSIVE;
                case ECR_ADVANCEDMODE_ACTIVE    : return S_ECR_ADVANCEDMODE_ACTIVE;
                case ECR_ADVANCEDMODE_AFTER     : return S_ECR_ADVANCEDMODE_AFTER;
                case ECR_ADVANCEDMODE_REPORT    : return S_ECR_ADVANCEDMODE_REPORT;
                case ECR_ADVANCEDMODE_PRINT     : return S_ECR_ADVANCEDMODE_REPORT;
                default: return S_ECR_ADVANCEDMODE_UNKNOWN;
        }
    }
    
}
