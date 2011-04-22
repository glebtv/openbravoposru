/*
 * ReadEJStatus.java
 *
 * Created on 16 January 2009, 13:18
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
 
    Get EKLZ Status 1
 
    Command:	ADH. Length: 5 bytes.
    ·	System Administrator password (4 bytes) 30
 
    Answer:		ADH. Length: 22 bytes.
    ·	Result Code (1 byte)
    ·	KPK value of last fiscal receipt (5 bytes) 0000000000…9999999999
    ·	Date of last KPK (3 bytes) DD-MM-YY
    ·	Time of last KPK (2 bytes) HH-MM
    ·	Number of last KPK (4 bytes) 00000000…99999999
    ·	EKLZ serial number (5 bytes) 0000000000…9999999999
    ·	EKLZ flags (1 byte)
 
    EKLZ flags	
    Bits 0&1	Flag t – Receipt type marker:·	
                «00» – Sale·	
                «01» – Buy·	
                «10» – Sale Refund·	
                «11» – Buy RefundFlag t value is set simultaneously with Flag d value
 
	Bit 2	Flag i – EKLZ is activated (0 – no, 1 – yes).Flag i value 
            turns «1» after successful execution of command A9h «Activate EKLZ». 
            Flag i value turns «0» after successful execution of command 
            AAh «Close EKLZ Archive» or in case of EKLZ archive overflow.
 
	Bit 3	Flag f – Paper in slip station lower sensor (0 – no, 1 – yes).
            Flag f value turns «1» after successful execution of Command A9h 
            «Activate EKLZ». After EKLZ activation Flag f value is always «1».
 
	Bit 4	Flag w – EKLZ in report mode (0 – no, 1 – yes).Flag f value turns 
            «1», if commands B3H – BBH. Flag f value turns «0», if EKLZ returns 
            «no data» upon successful execution of command B3H Get Data Of 
            EKLZ Report, after successful execution of command ACH Cancel 
            Active EKLZ Operation, and if flag a turns «1».
 
	Bit 5	Flag d – Fiscal receipt open (0 – no, 1 – yes).
            Flag d value turns «1», if commands 80H Sale, 81H Buy, 
            82H Sale Refund, and 82H Buy Refund have been successfully executed. 
            Flag d value turns «0», if commands 84H Void Transaction, 
            85H Close Receipt have been successfully executed, and if flag a 
            turns «1»
 
	Bit 6	Flag s – Fiscal day open (0 – no, 1 – yes).
            Flag s value turns «1», if any record that has time is saved 
            in EKLZ archive. Flag s value turns «0», after successful execution 
            of commands A9H Activate EKLZ and 41H Print Z-Report.
 
	Bit 7	Flag a – EKLZ fatal error code (0 – no fatal error, 1 – fatal error).
            Flag a value turns «1» when a checksum or writing to archive error 
            occurs, or when EKLZ archive structure is corrupted.
 
****************************************************************************/

public final class ReadEJStatus extends PrinterCommand
{
    class EJStatus
    {
        // KPK value of last fiscal receipt (5 bytes) 0000000000…9999999999
        public long docCRC;
        // Date of last KPK (3 bytes) DD-MM-YY
        public PrinterDate docDate;
        // Time of last KPK (2 bytes) HH-MM
        public int docHour;
        public int docMin;
        // Number of last KPK (4 bytes) 00000000…99999999
        public long docID;
        // EKLZ serial number (5 bytes) 0000000000…9999999999
        public long serialNumber;
        // EKLZ flags (1 byte)
        public int flags;
    }
    
    // in 
    private final int password;
    // out
    private EJStatus status;
    
    
    /**
     * Creates a new instance of ReadEJStatus
     */
    public ReadEJStatus(int password) {
        this.password = password;
    }
    
    public final int getCode()
    {
        return 0xAD;
    }
    
    public final String getText()
    {
        return "Read electronic journal status by code 1";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        status.docCRC = in.readLong(5);
        status.docDate = in.readDate();
        status.docHour = in.readByte();
        status.docMin = in.readByte();
        status.docID = in.readInt();
        status.serialNumber = in.readLong(5);
        status.flags = in.readByte();
    }
    
    public EJStatus getStatus()
    {
        return status;
    }
}
