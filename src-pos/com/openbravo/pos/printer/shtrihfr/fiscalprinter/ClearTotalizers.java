/*
 * ClearTotalizers.java
 *
 * Created on 15 January 2009, 13:06
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

    Clear All Totalizers
 
    Command:	27H. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		27H. Length: 2 bytes.
    ·	Result Code (1 byte)
    
****************************************************************************/

public final class ClearTotalizers extends PrinterCommand
{
    
    // in params
    private final int password;
    
    /** Creates a new instance of ClearTotalizers */
    public ClearTotalizers(int password) 
    {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0x27;
    }
    
    public final String getText()
    {
        return "Clear totalizers";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
    }
}
