/*
 * PrinterConst.java
 *
 * Created on August 6 2007, 12:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

public interface PrinterConst 
{
    //###################################################################
    //#### General SHTRIH fiscal printer constants
    //###################################################################

    /////////////////////////////////////////////////////////////////////
    // "ecrMode" Constants
    /////////////////////////////////////////////////////////////////////

    // Dump mode
    public static final int ECRMODE_DUMPMODE        = 0x01; 
    // Fiscal day opened, 24 hours have not run out
    public static final int ECRMODE_24NOTOVER       = 0x02; 
    // Fiscal day opened, 24 hours have run out
    public static final int ECRMODE_24OVER          = 0x03; 
    // Fiscal day closed
    public static final int ECRMODE_CLOSED          = 0x04; 
    // Fiscal printer blocked after wrong password of tax inspector
    public static final int ECRMODE_LOCKED          = 0x05; 
    // Confirm date mode
    public static final int ECRMODE_WAITDATE        = 0x06; 
    // Mode for changing decimal dot position
    public static final int ECRMODE_POINTPOS        = 0x07; 
    // Opened fiscal document: sale
    public static final int ECRMODE_RECSELL         = 0x08; 
    // Opened fiscal document: buy
    public static final int ECRMODE_RECBUY          = 0x18; 
    // Opened fiscal document: sale return 
    public static final int ECRMODE_RECRETSELL      = 0x28; 
    // Opened fiscal document: buy return
    public static final int ECRMODE_RECRETBUY       = 0x38; 
    // Fiscal printer reset mode
    public static final int ECRMODE_TECH            = 0x09; 
    // Print selftest mode
    public static final int ECRMODE_TEST            = 0x0A; 
    // Printing full fiscal report
    public static final int ECRMODE_FULLREPORT      = 0x0B; 
    // Printing electronic journal report
    public static final int ECRMODE_EKLZREPORT      = 0x0C; 
    // Opened fiscal slip document: sale
    public static final int ECRMODE_SLPSELL         = 0x0D; 
    // Opened fiscal slip document: buy
    public static final int ECRMODE_SLPBUY          = 0x1D; 
    // Opened fiscal slip document: return sale
    public static final int ECRMODE_SLPRETSELL      = 0x2D; 
    // Opened fiscal slip document: return buy
    public static final int ECRMODE_SLPRETBUY       = 0x3D; 
    // Wait for slip loaded
    public static final int ECRMODE_SLPWAITLOAD     = 0x0E; 
    // Slip loading and adjusting 
    public static final int ECRMODE_SLPLOAD         = 0x1E; 
    // Slip adjusting
    public static final int ECRMODE_SLPPOSITION     = 0x2E; 
    // Printing slip
    public static final int ECRMODE_SLPPRINTING     = 0x3E; 
    // Slip printing completed
    public static final int ECRMODE_SLPPRINTED      = 0x4E; 
    // Slip ejecting
    public static final int ECRMODE_SLPEJECT        = 0x5E; 
    // Waiting for slip removed
    public static final int ECRMODE_SLPWAITEJECT    = 0x6E; 
    // Fiscal slip ready to print
    public static final int ECRMODE_SLPREADY        = 0x0F; 
    
    //###################################################################
    //#### MODE constants
    //###################################################################

    // Dump mode
    public static final int MODE_DUMPMODE   = 0x01; 
    
    // Fiscal day opened, 24 hours have not run out
    public static final int MODE_24NOTOVER  = 0x02; 
    
    // Fiscal day opened, 24 hours have run out
    public static final int MODE_24OVER     = 0x03; 
    
    // Fiscal day closed
    public static final int MODE_CLOSED     = 0x04; 
    
    // Fiscal printer blocked after wrong password of tax inspector
    public static final int MODE_LOCKED     = 0x05; 
    
    // Confirm date mode
    public static final int MODE_WAITDATE   = 0x06;
    
    // Mode for changing decimal dot position
    public static final int MODE_POINTPOS   = 0x07;
    
    // Opened fiscal document
    public static final int MODE_REC        = 0x08; 
    
    // Fiscal printer reset mode
    public static final int MODE_TECH       = 0x09;
    
    // Print selftest mode
    public static final int MODE_TEST       = 0x0A; 
    
    // Printing full fiscal report
    public static final int MODE_FULLREPORT = 0x0B;
    
    // Printing electronic journal report
    public static final int MODE_EKLZREPORT = 0x0C; 
    
    // Opened fiscal slip document
    public static final int MODE_SLP        = 0x0D;
    
    // Print fiscal slip mode
    public static final int MODE_SLPPRINT   = 0x0E; 
    
    // Fiscal slip ready to print
    public static final int MODE_SLPREADY   = 0x0F; 

    //###################################################################
    //#### MODE constants
    //###################################################################

    public static final String S_MODE_DUMPMODE   = "Data output mode";
    public static final String S_MODE_24NOTOVER  = "Shift is opened, 24 hours are not over";
    public static final String S_MODE_24OVER     = "Shift is opened, 24 hours are over";
    public static final String S_MODE_CLOSED     = "Shift is closed";
    public static final String S_MODE_LOCKED     = "Locked by incorrect tax assessor password";
    public static final String S_MODE_WAITDATE   = "Wait for date confirmation";
    public static final String S_MODE_POINTPOS   = "Decimal point position mode";
    public static final String S_MODE_REC        = "Opened document";
    public static final String S_MODE_TECH       = "Technical zero mode";
    public static final String S_MODE_TEST       = "Test print mode";
    public static final String S_MODE_FULLREPORT = "Printing full fiscal report";
    public static final String S_MODE_EKLZREPORT = "Printing EKLZ report";
    public static final String S_MODE_SLP        = "Slip document opened";
    public static final String S_MODE_SLPPRINT   = "Waiting for slip";
    public static final String S_MODE_SLPREADY   = "Fiscal slip is ready";
    public static final String S_MODE_UNKNOWN    = "Unknown mode";
    
    //###################################################################
    //#### ecrAdvancedMode constants
    //###################################################################

    // Paper is present
    public static final int ECR_ADVANCEDMODE_IDLE      = 0; 
    // Out of paper: passive
    public static final int ECR_ADVANCEDMODE_PASSIVE   = 1; 
    // Out of paper: active
    public static final int ECR_ADVANCEDMODE_ACTIVE    = 2; 
    // After active paper out
    public static final int ECR_ADVANCEDMODE_AFTER     = 3; 
    // Printing reports mode
    public static final int ECR_ADVANCEDMODE_REPORT    = 4; 
    // Printing mode
    public static final int ECR_ADVANCEDMODE_PRINT     = 5; 

    //###################################################################
    //#### ecrAdvancedMode text constants
    //###################################################################

    public static final String S_ECR_ADVANCEDMODE_IDLE      = "Paper is present";
    public static final String S_ECR_ADVANCEDMODE_PASSIVE   = "Out of paper: passive";
    public static final String S_ECR_ADVANCEDMODE_ACTIVE    = "Out of paper: active";
    public static final String S_ECR_ADVANCEDMODE_AFTER     = "After active paper out";
    public static final String S_ECR_ADVANCEDMODE_REPORT    = "Printing reports mode";
    public static final String S_ECR_ADVANCEDMODE_PRINT     = "Printing mode";
    public static final String S_ECR_ADVANCEDMODE_UNKNOWN   = "Unknown advanced mode";
    
    //###################################################################
    //#### Cash totalizers numbers
    //###################################################################
    
    // Receipt sales total amount on department 1
    public static final int REG_REC_SELL1			= 0; 
    
    // Receipt buys total amount on department 1
    public static final int REG_REC_BUY1			= 1; 
    
    // Receipt sales return total amount on department 1
    public static final int REG_REC_RETSELL1		= 2;	
    
    // Receipt buys return total amount on department 1
    public static final int REG_REC_RETBUY1			= 3;	

    // Total receipt discount amount on sales 
    public static final int REG_REC_DISC_SELL		= 64;	
    
    // Total receipt discount amount on buys
    public static final int REG_REC_DISC_BUY		= 65;	
    
    // Total receipt discount amount on sale returns
    public static final int REG_REC_DISC_RETSELL	= 66;
    
    // Total receipt discount amount on buy returns
    public static final int REG_REC_DISC_RETBUY		= 67;
    
    // Total receipt charge amount on sales
    public static final int REG_REC_CHRG_SELL		= 68;
    
    // Total receipt charge amount on buys
    public static final int REG_REC_CHRG_BUY		= 69;
    
    // Total receipt charge amount on sale returns
    public static final int REG_REC_CHRG_RETSELL	= 70;
    
    // Total receipt charge amount on buy returns
    public static final int REG_REC_CHRG_RETBUY		= 71;

    // Total day discount amount on sales
    public static final int REG_DAY_DISC_SELL		= 185;	
    
    // Total day discount amount on buys
    public static final int REG_DAY_DISC_BUY		= 186;
    
    // Total day discount amount on sale returns
    public static final int REG_DAY_DISC_RETSELL	= 187;	
    
    // Total day discount amount on buy returns
    public static final int REG_DAY_DISC_RETBUY		= 188;
    
    // Total day charge amount on sales
    public static final int REG_DAY_CHRG_SELL		= 189;
    
    // Total day charge amount on buys
    public static final int REG_DAY_CHRG_BUY		= 190;
    
    // Total day charge amount on sale returns
    public static final int REG_DAY_CHRG_RETSELL	= 191;
    
    // Total day charge amount on buy returns
    public static final int REG_DAY_CHRG_RETBUY		= 192;

    // Total day sale amount from electronic journal
    public static final int REG_DAY_SELL			= 245;	
    
    // Total day buy amount from electronic journal
    public static final int REG_DAY_BUY				= 246;	
    
    // Total day sale return amount from electronic journal
    public static final int REG_DAY_RETSELL			= 247;	
    
    // Total day buy return amount from electronic journal
    public static final int REG_DAY_RETBUY			= 248;

    //###################################################################
    //#### Receipt types
    //###################################################################
    
    // Sale receipt
    public static final int SMFP_RECTYPE_SALE      = 0;	
    
    // Buy receipt
    public static final int SMFP_RECTYPE_BUY       = 1;	
    
    // Sale return receipt
    public static final int SMFP_RECTYPE_RETSALE   = 2;	
    
    // Buy return receipt
    public static final int SMFP_RECTYPE_RETBUY    = 3;	

    //###################################################################
    //#### Fiscal printer error codes
    //###################################################################
    
    // No error
    public static final int SMFP_EFPTR_SUCCESS          = 0x00;	
    // Error from FM1, FM2 or real time clock (RTC)
    public static final int SMFP_EFPTR_FM_FAILURE       = 0x01;	
    // Fiscal memory 1 missing
    public static final int SMFP_EFPTR_FM1_FAILURE      = 0x02;	
    // Fiscal memory 2 missing
    public static final int SMFP_EFPTR_FM2_FAILURE      = 0x03;	
    // Incorrect command parameters
    public static final int SMFP_EFPTR_FM_PARAMS        = 0x04;	
    // No data available
    public static final int SMFP_EFPTR_FM_NODATA        = 0x05;	
    // Fiscal memory in dump mode
    public static final int SMFP_EFPTR_FM_DATAMODE      = 0x06;	
    // Incorrect command parameters for this model
    public static final int SMFP_EFPTR_FM_PARAMS2       = 0x07;	
    // Command is not supported for this model
    public static final int SMFP_EFPTR_FM_INVALID_CMD   = 0x08;	
    // Incorrect command length
    public static final int SMFP_EFPTR_FM_CMDLENGTH     = 0x09;	
    // Data not in BCD format
    public static final int SMFP_EFPTR_FM_NOTBCD        = 0x0A;	
    // Fiscal memory failure on save total
    public static final int SMFP_EFPTR_FM_MEMFAIL       = 0x0B;	
    // License not entered
    public static final int SMFP_EFPTR_FM_NOLICENSE     = 0x11;	
    // Serial number already entered
    public static final int SMFP_EFPTR_FM_SERIAL_EXISTS = 0x12;	
    // Current date is less then last fiscal memory date
    public static final int SMFP_EFPTR_FM_INVALID_DATE  = 0x13;	
    // Day totals area overflow in fiscal memory 
    public static final int SMFP_EFPTR_FM_OVERFLOW      = 0x14;	
    // Day is already opened
    public static final int SMFP_EFPTR_FM_DAYOPENED     = 0x15;	
    // Day is not opened
    public static final int SMFP_EFPTR_FM_DAYNOTOPENED  = 0x16;	
    // First day number more then last day number
    public static final int SMFP_EFPTR_FM_DAYNUMBER     = 0x17;	
    // First day date more then last day date
    public static final int SMFP_EFPTR_FM_DAYDATE       = 0x18;	
    // Command is not supported by this version of FP
    public static final int SMFP_EFPTR_NOT_SUPPORTED    = 0x37;
    // Receipt paper is empty
    public static final int SMFP_EFPTR_NO_REC_PAPER     = 0x6B;	
    // Journal paper is empty
    public static final int SMFP_EFPTR_NO_JRN_PAPER     = 0x6C;	
    // Slip is empty
    public static final int SMFP_EFPTR_NO_SLP_PAPER     = 0xC6;	
    // Table is undefined
    public static final int SMFP_EFPTR_INVALID_TABLE    = 0x5D;	

    //###################################################################
    //#### cut types
    //###################################################################
    
    public static final int SMFP_CUT_FULL       = 0x00;	// full cut
    public static final int SMFP_CUT_PARTIAL    = 0x01;	// partial cut
    
    //###################################################################
    //#### station types
    //###################################################################
    
    /** journal station **/
    public static final int SMFP_STATION_JRN    = 0x01;	
    
    /** receipt station **/
    public static final int SMFP_STATION_REC    = 0x02;	
    
    /** receipt & journal stations **/ 
    public static final int SMFP_STATION_RECJRN = 0x03;	
    
    //////////////////////////////////////////////////////////////////////////
    // table numbers
    //////////////////////////////////////////////////////////////////////////
    
    // ECR type and mode
    public static final int SMFP_TABLE_SETUP        = 1;    
    // Cashier and admin's passwords
    public static final int SMFP_TABLE_CASHIER      = 2;    
    // Time conversion table
    public static final int SMFP_TABLE_TIME         = 3;    
    // Text in receipt
    public static final int SMFP_TABLE_TEXT         = 4;    
    // Payment type names
    public static final int SMFP_TABLE_PAYTYPE      = 5;    
    // Tax rates
    public static final int SMFP_TABLE_TAX          = 6;    
    // Department names
    public static final int SMFP_TABLE_DEPARTMENT   = 7;    
    // Font settings
    public static final int SMFP_TABLE_FONTS        = 8;    
    // Receipt format table
    public static final int SMFP_TABLE_RECFORMAT    = 9;    
    
    
    //////////////////////////////////////////////////////////////////////////
    // tables values
    //////////////////////////////////////////////////////////////////////////
    
    public static final int SMFP_TABLE_TAX_ROW_MIN      = 1;    
    public static final int SMFP_TABLE_TAX_ROW_MAX      = 4;    
    
    public static final int SMFP_TABLE_TAX_ROW_CASH     = 1;    
    public static final int SMFP_TABLE_TAX_ROW_CREDIT   = 2;    
    public static final int SMFP_TABLE_TAX_ROW_TARE     = 3;    
    public static final int SMFP_TABLE_TAX_ROW_CARD     = 4;    
    
    public static final int SMFP_TABLE_TAX_FIELD_CASH   = 1;    
    public static final int SMFP_TABLE_TAX_FIELD_CREDIT = 1;    
    public static final int SMFP_TABLE_TAX_FIELD_TARE   = 1;    
    public static final int SMFP_TABLE_TAX_FIELD_CARD   = 1;    
    
    public static final String SMFP_TABLE_TAX_VALUE_CASH   = "CASH";    
    public static final String SMFP_TABLE_TAX_VALUE_CREDIT = "CREDIT";    
    public static final String SMFP_TABLE_TAX_VALUE_TARE   = "TARE";    
    public static final String SMFP_TABLE_TAX_VALUE_CARD   = "CARD";    
    
    //////////////////////////////////////////////////////////////////////////
    // graphics limits
    //////////////////////////////////////////////////////////////////////////
    
    public static final int MAX_LINES_GRAPHICS      = 255;
    public static final int MAX_LINES_GRAPHICS_EX   = 500;

    //////////////////////////////////////////////////////////////////////////
    // report type constants
    //////////////////////////////////////////////////////////////////////////
    
    public static final int SMFP_REPORT_TYPE_SHORT  = 0;    
    public static final int SMFP_REPORT_TYPE_FULL   = 1;    
    
    //////////////////////////////////////////////////////////////////////////
    // read FM totals constants
    //////////////////////////////////////////////////////////////////////////
    
    // grand totals
    public static final int SMFP_FM_GRANDTOTALS = 0x00; 
    // grand totals after the last refiscalization
    public static final int SMFP_FM_GRANDTOTALSFISC = 0x01; 
}
