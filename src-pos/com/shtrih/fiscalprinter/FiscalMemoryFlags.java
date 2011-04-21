/*
 * FiscalMemoryFlags.java
 *
 * Created on 2 April 2008, 17:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import com.shtrih.util.BitUtils;
    
public class FiscalMemoryFlags 
{
    private int value;
    
    // Fiscal memory storage 1 (0 - not installed, 1 - installed)
    public boolean isFm1Present;		
    // Fiscal memory storage 2 (0 - not installed, 1 - installed)
    public boolean isFm2Present;		
    // License (0 - not entered, 1 - entered)
    public boolean isLicensePresent;	
    // Fiscal memory overflow (0 - no, 1 - yes)
    public boolean isFmOverflow;		
    // Battery (0 - >80%, 1 - <80%)
    public boolean isBatteryLow;		
    // Last record is (0 - correct, 1 - corrupted)
    public boolean isFmLastRecordCorrupted;
    // Session is (0 - closed, 1 - opened)
    public boolean isSessionOpened;		
    // 24 hours is (0 - not overed, 1 - overed)
    public boolean isFm24HoursOver;		
    
    public FiscalMemoryFlags() {}
    
    public FiscalMemoryFlags(int v)
    {
        setValue(v);
    }
    
    public int getValue()
    {
        return value;
    }
    public void setValue(int v)
    {
        isFm1Present = BitUtils.testBit(v, 0);
        isFm2Present = BitUtils.testBit(v, 1);
        isLicensePresent = BitUtils.testBit(v, 2);
        isFmOverflow = BitUtils.testBit(v, 3);
        isBatteryLow = BitUtils.testBit(v, 4);
        isFmLastRecordCorrupted = BitUtils.testBit(v, 5);
        isSessionOpened = BitUtils.testBit(v, 6);
        isFm24HoursOver = BitUtils.testBit(v, 7);
    }
}
