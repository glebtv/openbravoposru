/*
 * CsvTablesReader.java
 *
 * Created on 17 November 2009, 13:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class CsvTablesReader {
    
    static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");
    
    /**
     * Creates a new instance of CsvTablesReader
     */
    public CsvTablesReader() {
    }
    
    /**
     
    /// Java POS driver
    /// Current date: 2009/11/18 19:49:22
    // Table 1, ...
    // Table number,row,field,Field size,Min,Max,Name,Value
    1,1,1,1,0,1,99,STORE NUMBER,"1" 
     
     **/
    
    public boolean isComment(String line)
    {
        return line.startsWith("//");
    }
 
    public String getParamStr(String line, int index)
    {
        String result = "";
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        for (int i=0;i<=index;i++)
        {
            if (!tokenizer.hasMoreTokens()) break;
            String token = tokenizer.nextToken();
            if (i == index) {
                result = token;
            }
        }
        
        if (result.startsWith("\""))
        {
            result = result.substring(1);
        }
        if (result.endsWith("\""))
        {
            if (result.length() > 1)
            {
                result = result.substring(0, result.length()-1);
            } else
            {
                result = "";
            }
        }
        return result;
    }
    
    public int getParamInt(String line, int index)
    {
        return Integer.parseInt(getParamStr(line, index));
    }
                    
    public void load(String fileName, PrinterFields fields)
    throws IOException, FileNotFoundException
    {
        logger.log(Level.INFO, "load(" + fileName + ")");
        
        fields.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader
            (new FileInputStream(fileName),"UTF8"));        
        
        while (reader.ready()) 
        {
            String line = reader.readLine();
            if (!isComment(line))
            {
                int table = getParamInt(line, 0);
                int row = getParamInt(line, 1);
                int number = getParamInt(line, 2);
                int size = getParamInt(line, 3);
                int type = getParamInt(line, 4);
                int min = getParamInt(line, 5);
                int max = getParamInt(line, 6);
                String name = getParamStr(line, 7);
                String fieldValue = getParamStr(line, 8);
                
                PrinterField field = new PrinterField(
                    table, row, number, size, type, min, max, name, fieldValue);
                fields.add(field);
            }
        }
        logger.log(Level.INFO, "load: OK");
    }
}
