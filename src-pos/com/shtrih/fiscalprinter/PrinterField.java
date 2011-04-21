/*
 * PrinterField.java
 *
 * Created on April 22 2008, 13:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */
public class PrinterField {
    
    private final int table;
    private final int row;
    private final int number;
    private final int size;
    private final int type;
    private final long min;
    private final long max;
    private final String name;
    private final String value;
    
    // Номер таблицы,Ряд,Поле,Размер поля,Тип поля,Мин. значение, Макс.значение, Название,Значение
    
    /** Creates a new instance of PrinterField */
    public PrinterField(
        int table, 
        int row, 
        int number,
        int size,
        int type,
        long min,
        long max,
        String name, 
        String value
        )
    {
        this.table = table;
        this.row = row;
        this.number = number;
        this.size = size;
        this.type = type;
        this.min = min;
        this.max = max;
        this.name = name;
        this.value = value;
    }
    
    public int getTable() {
        return table;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getNumber() {
        return number;
    }
    
    public int getSize(){
        return size;
    }
    
    public int getType(){
        return type;
    }
    
    public long getMin(){
        return min;
    }
    
    public long getMax(){
        return max;
    }
    
    public String getName(){
        return name;
    }
    
    public String getValue(){
        return value;
    }
}
