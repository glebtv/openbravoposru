/*
 * FlexCommandsReader.java
 *
 * Created on November 19, 2009, 16:49
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

    
public class FlexCommandsReader {
    
    static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");
    
    /** Creates a new instance of FlexCommandsReader */
    public FlexCommandsReader() {
    }
   
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) 
    {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}
		return textVal;
	}

	
	/**
	 * Calls getTextValue and returns a int value
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}   
    
    private CommandParam readParam(Element element)
    {
        String name = getTextValue(element,"Name");
        int size = getIntValue(element,"Size");
        int type = getIntValue(element,"Type");
        int min = getIntValue(element,"MinValue");
        int max = getIntValue(element,"MaxValue");
        //String defaultValue = getTextValue(element, "defaultValue");
        String defaultValue = "";
        return new CommandParam(name, size, type, min, max, defaultValue);
    }

    private CommandParams readParams(Element element, String tagName)
    {
        CommandParams result = new CommandParams();
		NodeList nl = element.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) 
        {
            for (int i=0;i<nl.getLength();i++)
            {
                Element el = (Element)nl.item(i);
                NodeList nlparam = el.getElementsByTagName("Param");
                if (nlparam != null) 
                {
                    for (int j=0;j<nlparam.getLength();j++)
                    {
                        Element elparam = (Element)nlparam.item(j);
                        result.add(readParam(elparam));
                    }
                }
            }
		}
        return result;
    }
        
	private FlexCommand getCommand(Element element) 
    {
		int code = getIntValue(element,"Code");
		String text = getTextValue(element,"Name");
        
        CommandParams inParams = readParams(element, "InParams");
        CommandParams outParams = readParams(element, "OutParams");
        
		return new FlexCommand(code, text, inParams, outParams);
	}
    
    public void loadFromXml(String fileName, FlexCommands commands)
    throws Exception
    {
        commands.clear();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dom = db.parse(fileName);
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("Commands");
		if(nl != null && nl.getLength() > 0) 
        {
			for(int i=0;i<nl.getLength();i++) 
            {
				Element el = (Element)nl.item(i);
                NodeList nlcommand = el.getElementsByTagName("Command");
                if (nlcommand != null) 
                {
                    for (int j=0;j<nlcommand.getLength();j++)
                    {
                        Element elcommand = (Element)nlcommand.item(j);
                        try
                        {
                            FlexCommand command = getCommand(elcommand);
                            commands.add(command);
                        }
                        catch(Exception e)
                        {
                            logger.log(Level.SEVERE, "loadFromXml error", e);
                        }
                    }
                }
			}
		}
    }
}
