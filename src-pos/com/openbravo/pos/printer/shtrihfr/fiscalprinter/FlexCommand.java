/*
 * FlexCommand.java
 *
 * Created on 19 Ноябрь 2009 г., 16:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class FlexCommand extends PrinterCommand
{
    private final int code; // command code
    private final String text; // command name
    private final CommandParams inParams; // input parameters
    private final CommandParams outParams; // output parameters
        
    /** Creates a new instance of FlexCommand */
    public FlexCommand(int code, String text, CommandParams inParams, 
        CommandParams outParams) {
        this.code = code;
        this.text = text;
        this.inParams = inParams;
        this.outParams = outParams;
    }
    
    public final int getCode()
    {
        return code;
    }
        
    public final String getText()
    {
        return text;
    }
    
    public CommandParams getInParams() {
        return inParams;
    }
    
    public CommandParams getOutParams(){
        return outParams;
    }
    
    public void encode(CommandOutputStream out)
        throws Exception
    {
        inParams.encode(out);
    }
    
    public void decode(CommandInputStream in)
        throws Exception
    {
        outParams.decode(in);
    }
    
}
