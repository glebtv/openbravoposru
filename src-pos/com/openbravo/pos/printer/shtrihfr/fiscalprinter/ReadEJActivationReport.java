/*
 * ReadEJActivationReport.java
 *
 * Created on 16 January 2009, 14:37
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
 
    Get Data Of EKLZ Activation Receipt
 
    Command:	BBH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		BBH. Length: 18 bytes.
    ·	Result Code (1 byte)
    ·	ECR model (16 bytes) string of WIN1251 code page characters

****************************************************************************/

public final class ReadEJActivationReport extends PrinterCommand
{
    // in 
    private final int password;
    // out
    private String ecrModel = new String("");
    
    /**
     * Creates a new instance of ReadEJActivationReport
     */
    public ReadEJActivationReport(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xBB;
    }
    
    public final String getText()
    {
        return "Read electronic journal activation report";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        ecrModel = in.readString(in.getSize());
    }
    
    public String getEcrModel()
    {
        return ecrModel;
    }
}
