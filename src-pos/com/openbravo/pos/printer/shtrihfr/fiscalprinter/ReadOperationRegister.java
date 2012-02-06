/*
 * ReadOperationRegister.java
 *
 * Created on 2 April 2008, 20:39
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

/****************************************************************************
 
    Get Operation Totalizer Value

    Command:	1BH. Length: 6 bytes.
    ·	Operator password (4 bytes)
    ·	Operation totalizer number (1 byte) 0…255
    Answer:		1BH. Length: 5 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 1…30
    ·	Operation totalizer value (2 bytes)
 
****************************************************************************/

public final class ReadOperationRegister extends PrinterCommand
{
    // in
    private final int password;
    private final int totalizerNumber;
    // out 
    private int operator;
    private int totalizerValue;
    
    /**
     * Creates a new instance of ReadOperationRegister
     */
    public ReadOperationRegister(int password, int totalizerNumber) 
    {
        MethodParameter.checkRange(totalizerNumber, 0, 0xFF, "totalizer number");
        
        this.password = password;
        this.totalizerNumber = totalizerNumber;
    }
    
    public final int getCode()
    {
        return 0x1B;
    }
    
    public final String getText()
    {
        return "Get operation totalizer value";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(totalizerNumber);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
        totalizerValue = in.readShort();
    }
    
    public int getValue()
    {
        return totalizerValue;
    }
}
