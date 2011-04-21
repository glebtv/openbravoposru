/*
 * SharedSerialPort.java
 *
 * Created on May 22 2008, 21:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.openbravo.pos.printer.shtrihfr.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */


import gnu.io.*;
import java.util.logging.*;

public class SharedSerialPort
{
    private final Object owner;
    private final String portName;
    private final SerialPort port;
    static Logger logger = Logger.getLogger("com.shtrih.fiscalprinter");

    public SharedSerialPort(SerialPort port, String portName, Object owner) 
    {
        logger.log(Level.INFO, "SharedSerialPort(" + portName + ")");
        this.port = port;
        this.owner = owner;
        this.portName = portName;
    }

    public SerialPort getSerialPort()
    {
        return port;
    }

    public String getPortName()
    {
        return portName;
    }

    public Object getOwner()
    {
        return owner;
    }
}
