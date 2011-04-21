/*
 * TextCommand.java
 *
 * Created on January 16 2009, 18:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

public class TextCommand extends PrinterCommand
{
    private int code = 0;
    private byte[] txData = new byte[0];
    private byte[] rxData = new byte[0];
    
    /** Creates a new instance of TextCommand */
    public TextCommand() {
    }
    
    public final int getCode()
    {
        return code;
    }
        
    public final String getText()
    {
        return "";
        //return getCommandName(code);
    }
    
    public void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeBytes(txData);
    }
    
    public void decode(CommandInputStream in)
        throws Exception
    {
        rxData = in.readBytes(in.getSize());
    }
}
