/*
 * PrinterError.java
 *
 * Created on August 28 2007, 10:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author V.Kravtsov
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

public class PrinterError 
{
    public static int E_PRINTER_WAITPRINT = 88;
        
    public static String getText(int code)
    {
        switch (code)
        {
            case 0x00: return "No errors";
            case 0x01: return "FM1, FM2 or RTC error";
            case 0x02: return "FM1 missing";
            case 0x03: return "FM2 missing";
            case 0x04: return "Incorrect parameters in FM command";
            case 0x05: return "No data requested available";
            case 0x06: return "FM is in data output mode";
            case 0x07: return "Invalid FM command parameters";
            case 0x08: return "Command is not supported by FM";
            case 0x09: return "Invalid command length";
            case 0x0A: return "Data format is not BCD";
            case 0x0B: return "FM memory failure";
            case 0x11: return "License in not entered";
            case 0x12: return "Serial number is already entered";
            case 0x13: return "Current date less than last FM record date";
            case 0x14: return "Shift sum area overflow";
            case 0x15: return "Shift is already opened";
            case 0x16: return "Shift is not opened";
            case 0x17: return "First shift number more than last shift number";
            case 0x18: return "First shift date more than last shift date";
            case 0x19: return "No FM data available";
            case 0x1A: return "FM fiscal area overflow";
            case 0x1B: return "Serial number not assigned";
            case 0x1C: return "There is error record in the defined range";
            case 0x1D: return "Last shift sum record is corrupted";
            case 0x1F: return "Registers memory missing";
            case 0x20: return "Cash register overflow after add";
            case 0x21: return "Subtract summ is more then cash register value";
            case 0x22: return "Invalid date";
            case 0x23: return "Activation record not found";
            case 0x24: return "Activation area overflow";
            case 0x25: return "Activation not found";
            case 0x2F: return "EKLZ not answered";
            case 0x30: return "EKLZ returned NAK";
            case 0x31: return "EKLZ: format error";
            case 0x32: return "EKLZ: CRC error";
            case 0x33: return "Incorrect command parameters";
            case 0x35: return "Settings: Incorrect command parameters";
            case 0x36: return "Model: Incorrect command parameters";
            case 0x37: return "Command is not supported";
            case 0x38: return "PROM error";
            case 0x39: return "Internal software error";
            case 0x3A: return "Shift charge sum overflow";
            case 0x3C: return "EKLZ: Invalid registration number";
            case 0x3E: return "Shift section sum overflow";
            case 0x3F: return "Shift discount sum overflow";
            case 0x40: return "Discount range error";
            case 0x41: return "Cash sum range overflow";
            case 0x42: return "Pay type 2 sum range overflow";
            case 0x43: return "Pay type 3 sum range overflow";
            case 0x44: return "Pay type 4 sum range overflow";
            case 0x45: return "Payment summ less than receipt summ";
            case 0x46: return "No cash in ECR";
            case 0x47: return "Shift tax sum overflow";
            case 0x48: return "Receipt sum overflow";
            case 0x49: return "Receipt is opened. Command is invalid";
            case 0x4A: return "Receipt is opened. Command is invalid";
            case 0x4B: return "Receipt buffer overflow";
            case 0x4C: return "Shift ttal tax sum overflow";
            case 0x4D: return "Cashless sum more than receipt sum";
            case 0x4E: return "24 hours over";
            case 0x4F: return "Invalid password";
            case 0x50: return "Printing previous command";
            case 0x51: return "Shift cash sum overflow";
            case 0x52: return "Shift pay type 2 sum overflow";
            case 0x53: return "Shift pay type 3 sum overflow";
            case 0x54: return "Shift pay type 4 sum overflow";
            case 0x56: return "No document to repeat";
            case 0x57: return "EKLZ: Closed shift count <> FM shift count";
            case 0x58: return "Waiting for repeat print command";
            case 0x59: return "Document is opened by another operator";
            case 0x5A: return "Discount sum more than receipt sum";
            case 0x5B: return "Charge sum overflow";
            case 0x5C: return "Low supply voltage, 24v";
            case 0x5D: return "Table is undefined";
            case 0x5E: return "Invalid command";
            case 0x5F: return "Negative receipt sum";
            case 0x60: return "Multiplication overflow";
            case 0x61: return "Price range overflow";
            case 0x62: return "Quantity range overflow";
            case 0x63: return "Section range overflow";
            case 0x64: return "FM missing";
            case 0x65: return "No cash in section";
            case 0x66: return "Section sum overflow";
            case 0x67: return "FM connection error";
            case 0x68: return "Insufficient tax sum";
            case 0x69: return "Tax sum overflow";
            case 0x6A: return "Supply error when I2C active";
            case 0x6B: return "No receipt paper";
            case 0x6C: return "No journal paper";
            case 0x6D: return "Insufficient tax sum";
            case 0x6E: return "Tax sum overflow";
            case 0x6F: return "Shift cash out overflow";
            case 0x70: return "FM overflow";
            case 0x71: return "Cutter failure";
            case 0x72: return "Command not supported in this submode";
            case 0x73: return "Command not supported in this mode";
            case 0x74: return "RAM failure";
            case 0x75: return "Supply failure";
            case 0x76: return "Printer failure: no pulse";
            case 0x77: return "Printer failure: no sensor";
            case 0x78: return "Software replaced";
            case 0x79: return "FM replaced";
            case 0x7A: return "Field is not editable";
            case 0x7B: return "Hardware failure";
            case 0x7C: return "Invalid date";
            case 0x7D: return "Invalid date format";
            case 0x7E: return "Invalid frame length";
            case 0x7F: return "Total sum overflow";
            case 0x80: return "FM connection error";
            case 0x81: return "FM connection error";
            case 0x82: return "FM connection error";
            case 0x83: return "FM connection error";
            case 0x84: return "Cash sum overflow";
            case 0x85: return "Shift sale sum overflow";
            case 0x86: return "Shift buy sum overflow";
            case 0x87: return "Shift return sale sum overflow";
            case 0x88: return "Shift return buy sum overflow";
            case 0x89: return "Shift cash in sum overflow";
            case 0x8A: return "Receipt charge sum overflow";
            case 0x8B: return "Receipt discount sum overflow";
            case 0x8C: return "Negative receipt charge sum";
            case 0x8D: return "Negative receipt discount sum";
            case 0x8E: return "Zero receipt sum";
            case 0x8F: return "Not fiscal ECR";
            case 0x90: return "Field range overflow";
            case 0x91: return "Print width error";
            case 0x92: return "Field overflow";
            case 0x93: return "RAM recovery successful";
            case 0xA0: return "EKLZ connection error";
            case 0xA1: return "EKLZ missing";
            case 0xA2: return "EKLZ: Invalid parameter or command format";
            case 0xA3: return "Invalid EKLZ state";
            case 0xA4: return "EKLZ failure";
            case 0xA5: return "EKLZ: Cryptoprocessor failure";
            case 0xA6: return "EKLZ: Time limit exceeded";
            case 0xA7: return "EKLZ overflow";
            case 0xA8: return "EKLZ: invalid date or time";
            case 0xA9: return "EKLZ: no data available";
            case 0xAA: return "EKLZ overflow (negative document sum)";
            case 0xB0: return "EKLZ: Quantity parameter overflow";
            case 0xB1: return "EKLZ: Sum parameter overflow";
            case 0xB2: return "EKLZ: Already activated";
            case 0xC0: return "Confirm date and time";
            case 0xC1: return "EKLZ: report is not interruptable";
            case 0xC2: return "Increased supply voltage";
            case 0xC3: return "EKLZ: mismatch receipt sum";
            case 0xC4: return "Mismatch shift numbers";
            case 0xC5: return "Slip buffer is empty";
            case 0xC6: return "Slip is missing";
            case 0xC7: return "Field is not editable in this mode";
            case 0xC8: return "Printer connection error";
            default: return "Unknown error";
        }
    }
    
    public static String getFullText(int code)
    {
        return String.valueOf(code) + ", " + getText(code);
    }
}
