/*
 * SlipCloseParams.java
 *
 * Created on January 16 2009, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class SlipCloseParams {
    
    // Number of lines in transaction block (1 byte) 1…17
    public byte lineNumber = 0;
    // Line number of Receipt Total element in transaction block (1 byte) 1…17
    public byte totalLine = 0;
    // Line number of Text element in transaction block (1 byte) 0…17, «0» – do not print
    public byte textLine = 0;
    // Line number of Cash Payment element in transaction block (1 byte) 0…17, «0» – do not print
    public byte cashLine = 0;
    // Line number of Payment Type 2 element in transaction block (1 byte) 0…17, «0» – do not print
    public byte amount2Line = 0;
    // Line number of Payment Type 3 element in transaction block (1 byte) 0…17, «0» – do not print
    public byte amount3Line = 0;
    // Line number of Payment Type 4 element in transaction block (1 byte) 0…17, «0» – do not print
    public byte amount4Line = 0;
    // Line number of Change element in transaction block (1 byte) 0…17, «0» – do not print
    public byte changeLine = 0;
    // Line number of Tax 1 Turnover element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax1Line = 0;
    // Line number of Tax 2 Turnover element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax2Line = 0;
    // Line number of Tax 3 Turnover element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax3Line = 0;
    // Line number of Tax 4 Turnover element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax4Line = 0;
    // Line number of Tax 1 Sum element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax1TotalLine = 0;
    // Line number of Tax 2 Sum element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax2TotalLine = 0;
    // Line number of Tax 3 Sum element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax3TotalLine = 0;
    // Line number of Tax 4 Sum element in transaction block (1 byte) 0…17, «0» – do not print
    public byte tax4TotalLine = 0;
    // Line number of Receipt Subtotal Before Discount/Surcharge element in transaction block (1 byte) 0…17, «0» – do not print
    public byte subtotalLine = 0;
    // Line number of Discount/Surcharge Value element in transaction block (1 byte) 0…17, «0» – do not print
    public byte discountLine = 0;
    // Font type of Text element (1 byte)
    public byte textFont = 0;
    // Font type of «TOTAL» element (1 byte)
    public byte totalTextFont = 0;
    // Font type of Receipt Total Value element (1 byte)
    public byte totalAmountFont = 0;
    // Font type of «CASH» element (1 byte)
    public byte cashTextFont = 0;
    // Font type of Cash Payment Value element (1 byte)
    public byte cashAmountFont = 0;
    // Font type of Payment Type 2 Name element (1 byte)
    public byte amount2TextFont = 0;
    // Font type of Payment Type 2 Value element (1 byte)
    public byte amount2ValueFont = 0;
    // Font type of Payment Type 3 Name element (1 byte)
    public byte amount3TextFont = 0;
    // Font type of Payment Type 3 Value element (1 byte)
    public byte amount3ValueFont = 0;
    // Font type of Payment Type 4Name element (1 byte)
    public byte amount4TextFont = 0;
    // Font type of Payment Type 4Value element (1 byte)
    public byte amount4ValueFont = 0;
    // Font type of «CHANGE» element (1 byte)
    public byte chnageTextFont = 0;
    // Font type of Change Value element (1 byte)
    public byte chnageValueFont = 0;
    // Font type of Tax 1 Name element (1 byte)
    public byte tax1TextFont = 0;
    // Font type of Tax 1 Turnover Value element (1 byte)
    public byte tax1AmountFont = 0;
    // Font type of Tax 1 Rate element (1 byte)
    public byte tax1RateFont = 0;
    // Font type of Tax 1 Value element (1 byte)
    public byte tax1ValueFont = 0;
    // Font type of Tax 2 Name element (1 byte)
    public byte tax2TextFont = 0;
    // Font type of Tax 2 Turnover Value element (1 byte)
    public byte tax2AmountFont = 0;
    // Font type of Tax 2 Rate element (1 byte)
    public byte tax2RateFont = 0;
    // Font type of Tax 2 Value element (1 byte)
    public byte tax2ValueFont = 0;
    // Font type of Tax 3 Name element (1 byte)
    public byte tax3TextFont = 0;
    // Font type of Tax 3 Turnover Value element (1 byte)
    public byte tax3AmountFont = 0;
    // Font type of Tax 3 Rate element (1 byte)
    public byte tax3RateFont = 0;
    // Font type of Tax 3 Value element (1 byte)
    public byte tax3ValueFont = 0;
    // Font type of Tax 4 Name element (1 byte)
    public byte tax4TextFont = 0;
    // Font type of Tax 4 Turnover Value element (1 byte)
    public byte tax4AmountFont = 0;
    // Font type of Tax 4 Rate element (1 byte)
    public byte tax4RateFont = 0;
    // Font type of Tax 4 Value element (1 byte)
    public byte tax4ValueFont = 0;
    // Font type of «SUBTOTAL» element (1 byte)
    public byte subtotalTextFont = 0;
    // Font type of Receipt Subtotal Before Discount/Surcharge Value element (1 byte)
    public byte subtotalValueFont = 0;
    // Font type of «DISCOUNT XX.XX%» element (1 byte)
    public byte discountTextFont = 0;
    // Font type of Receipt Discount Value element (1 byte)
    public byte discountValueFont = 0;
    // Length of Text element in characters (1 byte)
    public byte textLength = 0;
    // Length of Receipt Total Value element in characters (1 byte)
    public byte totalLength = 0;
    // Length of Cash Payment Value element in characters (1 byte)
    public byte cashLength = 0;
    // Length of Payment Type 2 Value element in characters (1 byte)
    public byte payment2Length = 0;
    // Length of Payment Type 3 Value element in characters (1 byte)
    public byte payment3Length = 0;
    // Length of Payment Type 4Value element in characters (1 byte)
    public byte payment4Length = 0;
    // Length of Change Value element in characters (1 byte)
    public byte changeLength = 0;
    // Length of Tax 1 Name element in characters (1 byte)
    public byte tax1TextLength = 0;
    // Length of Tax 1 Turnover element in characters (1 byte)
    public byte tax1AmountLength = 0;
    // Length of Tax 1 Rate element in characters (1 byte)
    public byte tax1RateLength = 0;
    // Length of Tax 1 Value element in characters (1 byte)
    public byte tax1ValueLength = 0;
    // Length of Tax 2 Name element in characters (1 byte)
    public byte tax2TextLength = 0;
    // Length of Tax 2 Turnover element in characters (1 byte)
    public byte tax2AmountLength = 0;
    // Length of Tax 2 Rate element in characters (1 byte)
    public byte tax2RateLength = 0;
    // Length of Tax 2 Value element in characters (1 byte)
    public byte tax2ValueLength = 0;
    // Length of Tax 3 Name element in characters (1 byte)
    public byte tax3TextLength = 0;
    // Length of Tax 3 Turnover element in characters (1 byte)
    public byte tax3AmountLength = 0;
    // Length of Tax 3 Rate element in characters (1 byte)
    public byte tax3RateLength = 0;
    // Length of Tax 3 Value element in characters (1 byte)
    public byte tax3ValueLength = 0;
    // Length of Tax 4 Name element in characters (1 byte)
    public byte tax4TextLength = 0;
    // Length of Tax 4 Turnover element in characters (1 byte)
    public byte tax4AmountLength = 0;
    // Length of Tax 4 Rate element in characters (1 byte)
    public byte tax4RateLength = 0;
    // Length of Tax 4 Value element in characters (1 byte)
    public byte tax4ValueLength = 0;
    // Length of Receipt Subtotal Before Discount/Surcharge Value element in characters (1 byte)
    public byte subtotalLength = 0;
    // Length of «DISCOUNT XX.XX%» element in characters (1 byte)
    public byte discountTextLength = 0;
    // Length of Receipt Discount Value element in characters (1 byte)
    public byte discountValueLength = 0;
    // Position in line of Text element (1 byte)
    public byte textOffset = 0;
    // Position in line of «TOTAL» element (1 byte)
    public byte totalTextOffset = 0;
    // Position in line of Receipt Total Value element (1 byte)
    public byte totalValueOffset = 0;
    // Position in line of «CASH» element (1 byte)
    public byte payment1TextOffset = 0;
    // Position in line of Cash Payment Value element (1 byte)
    public byte payment1ValueOffset = 0;
    // Position in line of Payment Type 2 Name element (1 byte)
    public byte payment2TextOffset = 0;
    // Position in line of Payment Type 2 Value element (1 byte)
    public byte payment2ValueOffset = 0;
    // Position in line of Payment Type 3 Name element (1 byte)
    public byte payment3TextOffset = 0;
    // Position in line of Payment Type 3 Value element (1 byte)
    public byte payment3ValueOffset = 0;
    // Position in line of Payment Type 4 Name element (1 byte)
    public byte payment4TextOffset = 0;
    // Position in line of Payment Type 4 Value element (1 byte)
    public byte payment4ValueOffset = 0;
    // Position in line of «CHANGE» element (1 byte)
    public byte changeTextOffset = 0;
    // Position in line of Change Value element (1 byte)
    public byte changeValueOffset = 0;
    // Position in line of Tax 1 Name element (1 byte)
    public byte tax1TextOffset = 0;
    // Position in line of Tax 1 Turnover Value element (1 byte)
    public byte tax1AmountOffset = 0;
    // Position in line of Tax 1 Rate element (1 byte)
    public byte tax1RateOffset = 0;
    // Position in line of Tax 1 Value element (1 byte)
    public byte tax1ValueOffset = 0;
    // Position in line of Tax 2 Name element (1 byte)
    public byte tax2TextOffset = 0;
    // Position in line of Tax 2 Turnover Value element (1 byte)
    public byte tax2AmountOffset = 0;
    // Position in line of Tax 2 Rate element (1 byte)
    public byte tax2RateOffset = 0;
    // Position in line of Tax 2 Value element (1 byte)
    public byte tax2ValueOffset = 0;
    // Position in line of Tax 3 Name element (1 byte)
    public byte tax3TextOffset = 0;
    // Position in line of Tax 3 Turnover Value element (1 byte)
    public byte tax3AmountOffset = 0;
    // Position in line of Tax 3 Rate element (1 byte)
    public byte tax3RateOffset = 0;
    // Position in line of Tax 3 Value element (1 byte)
    public byte tax3ValueOffset = 0;
    // Position in line of Tax 4 Name element (1 byte)
    public byte tax4TextOffset = 0;
    // Position in line of Tax 4 Turnover Value element (1 byte)
    public byte tax4AmountOffset = 0;
    // Position in line of Tax 4 Rate element (1 byte)
    public byte tax4RateOffset = 0;
    // Position in line of Tax 4 Value element (1 byte)
    public byte tax4ValueOffset = 0;
    // Position in line of «SUBTOTAL» element (1 byte)
    public byte subtotalTextOffset = 0;
    // Position in line of Receipt Subtotal Before Discount/Surcharge Value element (1 byte)
    public byte subtotalValueOffset = 0;
    // Position in line of «DISCOUNT XX.XX%» element (1 byte)
    public byte discountTextOffset = 0;
    // Position in line of Receipt Discount Value element (1 byte)
    public byte discountValueOffset = 0;
        
    /** Creates a new instance of SlipCloseParams */
    public SlipCloseParams() {
    }
    
}
