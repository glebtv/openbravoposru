/*
 * CommandParam.java
 *
 * Created on 19 Ноябрь 2009 г., 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.util.logging.*;

public class CommandParam {
    
    static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");
    
    public static final int PARAM_TYPE_INT = 0; // Integer
    public static final int PARAM_TYPE_STR = 1; // String
    public static final int PARAM_TYPE_HEX = 2; // Binary byte array in hex format
    public static final int PARAM_TYPE_DATE = 3; // Date
    public static final int PARAM_TYPE_TIME = 4; // Time
    public static final int PARAM_TYPE_FINT = 5; // Number, Taxpayer ID, if all $FF - not accepted
    public static final int PARAM_TYPE_SYS = 6; // System administrator password
    public static final int PARAM_TYPE_USR = 7; // Operator password
    public static final int PARAM_TYPE_TAX = 8; // Tax officer password
    public static final int PARAM_TYPE_MIN = 9; // Min field value
    public static final int PARAM_TYPE_MAX = 10; // Max field value
    public static final int PARAM_TYPE_FSIZE = 11; // Field size
    public static final int PARAM_TYPE_FTYPE = 12; // Field type
    public static final int PARAM_TYPE_FVALUE = 13; // Vield value
    public static final int PARAM_TYPE_TABLE = 14; // Table number
    public static final int PARAM_TYPE_ROW = 15; // Row number
    public static final int PARAM_TYPE_FIELD = 16; // Field number
    public static final int PARAM_TYPE_VBAT = 17; // Battery voltage
    public static final int PARAM_TYPE_VSRC = 18; // Power supply voltage
    public static final int PARAM_TYPE_TIMEOUT = 19; // Timeout
    
    private final String name;
    private final int size;
    private final int type;
    private final int min;
    private final int max;
    private final String defaultValue;
    private String value = "";
    
    /** Creates a new instance of CommandParam */
    public CommandParam(
        String name,
        int size,
        int type,
        int min,
        int max,
        String defaultValue
        ) {
        
        this.name = name;
        this.size = size;
        this.type = type;
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
    }
    
    public String getName() { 
        return name; 
    }
    
    public int getSize() { 
        return size; 
    }
    
    public int getType() { 
        return type; 
    }
    
    public int getMin() { 
        return min; 
    }
    
    public int getMax() { 
        return max; 
    }
    
    public String getValue() { 
        return value; 
    }
    
    public String getDefaultValue(){
        return defaultValue;
    }
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getTypeName() {
        switch(type){
            case PARAM_TYPE_INT: return "Int";
            case PARAM_TYPE_STR: return "Str";
            case PARAM_TYPE_HEX: return "Hex";
            case PARAM_TYPE_DATE: return "Date";
            case PARAM_TYPE_TIME: return "Time";
            case PARAM_TYPE_FINT: return "FInt";
            case PARAM_TYPE_SYS: return "Sys";
            case PARAM_TYPE_USR: return "Usr";
            case PARAM_TYPE_TAX: return "Tax";
            case PARAM_TYPE_MIN: return "Min";
            case PARAM_TYPE_MAX: return "Max";
            case PARAM_TYPE_FSIZE: return "FSize";
            case PARAM_TYPE_FTYPE: return "FType";
            case PARAM_TYPE_FVALUE: return "FValue";
            case PARAM_TYPE_TABLE: return "Table";
            case PARAM_TYPE_ROW: return "Row";
            case PARAM_TYPE_FIELD: return "Field";
            case PARAM_TYPE_VBAT: return "VBat";
            case PARAM_TYPE_VSRC: return "VSrc";
            case PARAM_TYPE_TIMEOUT: return "Timeout";
            default: return "";
        }
    }
    
    public void encode(CommandOutputStream out)
    throws Exception {
        logger.log(Level.INFO, "Param write");
        logger.log(Level.INFO, "Param type: " + String.valueOf(type));
        logger.log(Level.INFO, "Param size: " + String.valueOf(size));
        logger.log(Level.INFO, "Param value: " + value);
        
        switch(type){
            case PARAM_TYPE_INT:
            case PARAM_TYPE_FINT:
            case PARAM_TYPE_SYS:
            case PARAM_TYPE_USR:
            case PARAM_TYPE_TAX:
            case PARAM_TYPE_MIN:
            case PARAM_TYPE_MAX:
            case PARAM_TYPE_FSIZE:
            case PARAM_TYPE_FTYPE:
            case PARAM_TYPE_FVALUE:
            case PARAM_TYPE_TABLE:
            case PARAM_TYPE_ROW:
            case PARAM_TYPE_FIELD:
            case PARAM_TYPE_VBAT:
            case PARAM_TYPE_VSRC:
            case PARAM_TYPE_TIMEOUT:
                out.writeLong(Long.parseLong(value), size);
                break;
                
            case PARAM_TYPE_STR:
                out.writeString(value, value.length()); // !!!
                break;
                
            case PARAM_TYPE_HEX:
                //out.writeString(hexToStr(value), 40); // !!!
                break;
                
            case PARAM_TYPE_DATE:
                out.writeDate(PrinterDate.fromText(value));
                break;
                
            case PARAM_TYPE_TIME:
                out.writeTime(PrinterTime.fromText(value));
                break;
        }
    }
    
    public void decode(CommandInputStream in)
    throws Exception {
        switch(type) {
            case PARAM_TYPE_INT:
            case PARAM_TYPE_FINT:
            case PARAM_TYPE_SYS:
            case PARAM_TYPE_USR:
            case PARAM_TYPE_TAX:
            case PARAM_TYPE_MIN:
            case PARAM_TYPE_MAX:
            case PARAM_TYPE_FSIZE:
            case PARAM_TYPE_FTYPE:
            case PARAM_TYPE_FVALUE:
            case PARAM_TYPE_TABLE:
            case PARAM_TYPE_ROW:
            case PARAM_TYPE_FIELD:
            case PARAM_TYPE_VBAT:
            case PARAM_TYPE_VSRC:
            case PARAM_TYPE_TIMEOUT:
                value = String.valueOf(in.readLong(size));
                break;
                
            case PARAM_TYPE_STR:
                if (size == 0){
                    value = in.readString();
                } else {
                    value = in.readString(size);
                }
                break;
                
            case PARAM_TYPE_HEX:
                //value = in.readString(size); // !!!
                break;
                
            case PARAM_TYPE_DATE:
                value = PrinterDate.toText(in.readDate());
                break;
                
            case PARAM_TYPE_TIME:
                value = PrinterTime.toText(in.readTime());
                break;
        }
    }
    
    
/*
 
     case PARAM_TYPE_INT:
    case PARAM_TYPE_STR:
    case PARAM_TYPE_HEX:
    case PARAM_TYPE_DATE:
    case PARAM_TYPE_TIME:
    case PARAM_TYPE_FINT:
    case PARAM_TYPE_SYS:
    case PARAM_TYPE_USR:
    case PARAM_TYPE_TAX:
    case PARAM_TYPE_MIN:
    case PARAM_TYPE_MAX:
    case PARAM_TYPE_FSIZE:
    case PARAM_TYPE_FTYPE:
    case PARAM_TYPE_FVALUE:
    case PARAM_TYPE_TABLE:
    case PARAM_TYPE_ROW:
    case PARAM_TYPE_FIELD:
    case PARAM_TYPE_VBAT:
    case PARAM_TYPE_VSRC:
    case PARAM_TYPE_TIMEOUT:
 
 */
}
