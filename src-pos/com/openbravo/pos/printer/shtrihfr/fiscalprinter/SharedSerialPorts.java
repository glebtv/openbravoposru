/*
 * SharedSerialPorts.java
 *
 * Created on May 22 2008, 21:07
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
import java.util.Vector;

public class SharedSerialPorts
{
    private Vector ports = new Vector();
    private static SharedSerialPorts SharedSerialPorts;

    /** A private Constructor prevents any other class from instantiating. */
    private SharedSerialPorts() 
    {
    }

    public static synchronized SharedSerialPorts getSharedSerialPorts() {
        if (SharedSerialPorts == null) {
            SharedSerialPorts = new SharedSerialPorts();
        }
        return SharedSerialPorts;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

   public SharedSerialPort findItem(String portName)
   {
       for (int i=0;i<ports.size();i++)
       {
            SharedSerialPort port = (SharedSerialPort)ports.get(i);
            if (portName.equalsIgnoreCase(port.getPortName()))
            {
                return port;
            }
       }
       return null;
   }

   public SharedSerialPort findItem(SerialPort serialPort)
   {
       for (int i=0;i<ports.size();i++)
       {
            SharedSerialPort port = (SharedSerialPort)ports.get(i);
            if (port.getSerialPort().equals(serialPort))
            {
                return port;
            }
       }
       return null;
   }

   public SharedSerialPort findItem(String portName, Object owner)
   {
       for (int i=0;i<ports.size();i++)
       {
            SharedSerialPort port = (SharedSerialPort)ports.get(i);
            if ((portName.equalsIgnoreCase(port.getPortName()) && 
                (port.getOwner().equals(owner))))
            {
                return port;
            }
       }
       return null;
   }
   
   private SharedSerialPort createPort(SerialPort serialPort, 
       String portName, Object owner)
   {
        SharedSerialPort port = new SharedSerialPort(serialPort, portName, owner);
        ports.add(port);
        return port;
   }
   
   private SharedSerialPort newPort(String portName, int timeout, Object owner)
   throws Exception
   {
        CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
        SerialPort serialPort = (SerialPort) portId.open(owner.getClass().getName(), timeout);   
        return createPort(serialPort, portName, owner);
   }

   public SharedSerialPort openPort(String portName, int timeout, Object owner)
   throws Exception
   {
        SharedSerialPort item = null;
        item = findItem(portName, owner);
        if (item != null) return item;

        item = findItem(portName);
        if (item != null)
        {
            if (item.getOwner().equals(owner))
            {
                return item;
            } else
            {
                if (owner.getClass().equals(item.getOwner()))
                {
                    return newPort(portName, timeout, owner);
                } else
                {
                    return createPort(item.getSerialPort(), portName, owner);
                }
            }

        } else
        {
            return newPort(portName, timeout, owner);
        }
   }

   public void closePort(SharedSerialPort port)
   {
        if (port != null)
        {
            SerialPort serialPort = port.getSerialPort();
            ports.remove(port);
            if (findItem(serialPort) == null)
            {
                serialPort.close();
            }
        }
   }
}

