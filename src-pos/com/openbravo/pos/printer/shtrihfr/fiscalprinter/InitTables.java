/*
 * InitTables.java
 *
 * Created on 15 January 2009, 12:51
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

    Set Table With Default Values
 
    Command:	24H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		24H. Length: 2 bytes.
    ·	Result Code (1 byte)
    
****************************************************************************/


public class InitTables {
    
    // in params
    private final int password;
    
    /**
     * Creates a new instance of InitTables
     */
    public InitTables(int password)
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0x24;
    }
    
    public final String getText()
    {
        return "Init tables with default values";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
}
