/*
 * PrintDepartmentReport.java
 *
 * Created on January 15 2009, 13:20
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

    Print Department Report
 
    Command:	42H. Length: 5 bytes.
    ·	Administrator or System Administrator password (4 bytes) 29, 30
 
    Answer:		42H. Length: 3 bytes.
    ·	Result Code (1 byte)
    ·	Operator index number (1 byte) 29, 30
    
****************************************************************************/

public final class PrintDepartmentReport extends PrinterCommand
{
    // in params
    private final int password;
    // out params
    private int operator;
    
    /** Creates a new instance of PrintDepartmentReport */
    public PrintDepartmentReport(int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0x42;
    }
    
    public final String getText()
    {
        return "Print department report";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        operator = in.readByte();
    }
    
    public int getOperator()
    {
        return operator;
    }
}
