/*
 * FiscalizeLong.java
 *
 * Created on 2 April 2008, 18:05
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

    Fiscalize with long ECRRN

    Command:	0DH. Length: 22 bytes.
    ·	Old Tax Officer password (4 bytes)
    ·	New Tax Officer password (4 bytes)
    ·	Long ECRRN (7 bytes) 00000000000000…99999999999999
    ·	Taxpayer ID (6 bytes) 000000000000…999999999999
    Answer:		0DH. Length: 9 bytes.
    ·	Result Code (1 byte)
    ·	Fiscalization/Refiscalization number(1 byte) 1…16
    ·	Quantity of refiscalizations left in FM (1 byte) 0…15
    ·	Last daily totals record number in FM (2 bytes) 0000…2100
    ·	Fiscalization/Refiscalization date (3 bytes) DD-MM-YY
    
NOTE:   The command is introduced into this protocol to conform to the 
        Byelorussian legislation that requires Electronic Cash Registers 
        to have registration numbers (ECRRN) 14 digits long, where as 
        Russian ECRRN is 10 digits long.
    
****************************************************************************/

public final class FiscalizeLong extends PrinterCommand
{
    // in params
    private final int password;
    private final int newPassword;
    private final long regID;
    private final long taxID;
    // out params
    private int fiscNumber = 0;
    private int fiscNumberLeft = 0;
    private int sessionNumber = 0;
    private PrinterDate date = new PrinterDate();
    
    /**
     * Creates a new instance of FiscalizeLong
     */
    public FiscalizeLong(int password, int newPassword, 
        long regID, long taxID) 
    {
        this.password = password;
        this.newPassword = newPassword;
        this.regID = regID;
        this.taxID = taxID;
    }
    
    public final int getCode()
    {
        return 0x0D;
    }
    
    public final String getText()
    {
        return "Fiscalize with long ECRRN";
    }
    
    public final void encode(CommandOutputStream out)
        throws Exception
    {
        out.writeInt(password);
        out.writeInt(newPassword);
        out.writeLong(regID, 7);
        out.writeLong(taxID, 6);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        fiscNumber = in.readByte();
        fiscNumberLeft = in.readByte();
        sessionNumber = in.readShort();
        date = in.readDate();
    }
}
