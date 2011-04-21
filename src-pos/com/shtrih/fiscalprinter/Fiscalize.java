/*
 * Fiscalize.java
 *
 * Created on 15 January 2009, 14:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************

    Fiscalization
 
    Command:	65H. Length: 20 bytes.
    ·	Old tax officer opassword (4 bytes)
    ·	New tax officer password (4 bytes)
    ·	Registration ID (5 bytes) 0000000000…9999999999
    ·	Taxpayer ID (6 bytes) 000000000000…999999999999
 
    Answer:		65H. Length: 9 bytes.
    ·	Result Code (1 byte)
    ·	Fiscalization number (1 byte) 1…16
    ·	Fiscalizations left number (1 byte) 0…15
    ·	Fiscal memory day number (2 bytes) 0000…2100
    ·	Fiscalization date (3 bytes) DD-MM-YY
    
****************************************************************************/

public final class Fiscalize extends PrinterCommand
{
    // in params
    private final int oldPassword;
    private final int newPassword;
    private final long regID;
    private final long taxID;
    // out params
    private int fiscNumber = 0;
    private int freeNumber = 0;
    private int dayNumber = 0;
    private PrinterDate date = new PrinterDate();
    
    /** Creates a new instance of Fiscalize */
    public Fiscalize(int oldPassword, int newPassword, 
        long regID, long taxID) 
    {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.regID = regID;
        this.taxID = taxID;
    }
    
    public int getOldPassword() { return oldPassword; }
    public int getNewPassword() { return newPassword; }
    public long GetRegID() { return regID; }
    public long getTaxID() { return taxID; }
    
    public int getFiscNumber() { return fiscNumber; }
    public int getFreeNumber() { return freeNumber; }
    public int getDayNumber() { return dayNumber; }
    public PrinterDate getDate() { return date; } 
    
    public final int getCode()
    {
        return 0x65;
    }
    
    public final String getText()
    {
        return "Fiscalize printer";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeInt(oldPassword);
        out.writeInt(newPassword);
        out.writeLong(regID, 5);
        out.writeLong(taxID, 6);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        fiscNumber = in.readByte();
        freeNumber = in.readByte();
        dayNumber = in.readShort();
        date = in.readDate();
    }
}
