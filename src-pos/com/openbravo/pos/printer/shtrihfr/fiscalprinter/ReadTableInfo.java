/*
 * ReadTableInfo.java
 *
 * Created on 2 April 2008, 19:59
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
 
    Get Table Structure
 
    Command:	2DH. Length: 6 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Table number (1 byte)
 
    Answer:		2DH. Length: 45 bytes.
    ·	Result Code (1 byte)
    ·	Table name (40 bytes)
    ·	Number of rows (2 bytes)
    ·	Number of fields (1 byte)
 
****************************************************************************/

public class ReadTableInfo extends PrinterCommand
{
    // in params
    private final int password;
    private final int tableNumber;
    // out
    private String tableName;
    private int rowCount;
    private int fieldCount;

    /**
     * Creates a new instance of ReadTableInfo
     */
    public ReadTableInfo(int password, int tableNumber) 
    {
        this.password = password;
        this.tableNumber = tableNumber;
    }
    
    public final int getCode()
    {
        return 0x2D;
    }
    
    public final String getText()
    {
        return "Get table structure";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeByte(tableNumber);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        tableName = in.readString(40);
        tableName = tableName.trim();
        rowCount = in.readShort();
        fieldCount = in.readByte();
    }
    
    public int getTableNumber()
    {
        return tableNumber;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public int getRowCount()
    {
        return rowCount;
    }
    
    public int getFieldCount()
    {
        return fieldCount;
    }
}
