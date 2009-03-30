//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
package com.openbravo.pos.scale;

import gnu.io.*;
import java.io.*;
import java.util.TooManyListenersException;

public class ScaleTves implements Scale, SerialPortEventListener {

    private String m_sPortScale;
    private CommPortIdentifier m_PortIdPrinter;
    private SerialPort m_CommPortPrinter;
    private OutputStream m_out;
    private InputStream m_in;
    private static final int SCALE_READY = 0;
    private static final int SCALE_READING = 1;
    private double m_dWeightBuffer;
    private int m_iStatusScale;

    public ScaleTves(String sPortPrinter) {
        m_sPortScale = sPortPrinter;
        m_out = null;
        m_in = null;
        m_iStatusScale = SCALE_READY;
        m_dWeightBuffer = 0.0;
    }

    public Double readWeight() {

        synchronized (this) {
            if (m_iStatusScale != SCALE_READY) {
                try {
                    wait(2000);
                } catch (InterruptedException e) {
                }
                if (m_iStatusScale != SCALE_READY) {
                    m_iStatusScale = SCALE_READY;
                }
            }
            
            m_dWeightBuffer = 0.0;
            
            write(new byte[]{0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00});

            flush();

            try {                
                wait(1000);
            } catch (InterruptedException e) {
            }

            if (m_iStatusScale == SCALE_READY) {
                // a value as been readed.
                double dWeight = m_dWeightBuffer / 1000.0;
                m_dWeightBuffer = 0.0;
                return new Double(dWeight);
            } else {
                m_iStatusScale = SCALE_READY;
                m_dWeightBuffer = 0.0;
                return new Double(0.0);
            }
        }
    }

    private void flush() {
        try {
            m_out.flush();
        } catch (IOException e) {
        }
    }

    private void write(byte[] data) {
        try {
            if (m_out == null) {
                m_PortIdPrinter = CommPortIdentifier.getPortIdentifier(m_sPortScale); // Tomamos el puerto
                m_CommPortPrinter = (SerialPort) m_PortIdPrinter.open("PORTID", 2000); // Abrimos el puerto
                m_out = m_CommPortPrinter.getOutputStream(); // Tomamos el chorro de escritura
                m_in = m_CommPortPrinter.getInputStream();
                m_CommPortPrinter.addEventListener(this);
                m_CommPortPrinter.notifyOnDataAvailable(true);
                m_CommPortPrinter.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); // Configuramos el puerto
            }
            m_out.write(data);
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialEvent(SerialPortEvent e) {
        switch (e.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (m_in.available() > 0) {
                        byte[] b = new byte[17];
                        m_in.read(b);
                        if (m_iStatusScale == SCALE_READY) {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READING;
                        }
                        for (int i = 5; i >= 0; i--) {
                            m_dWeightBuffer = m_dWeightBuffer * 10.0 + (int) b[i];
                        }
                        if (m_dWeightBuffer >= 0.0 && m_dWeightBuffer <= 15050.0) {
                            synchronized (this) {
                                m_iStatusScale = SCALE_READY;
                                notifyAll();
                            }

                        } else {
                            m_dWeightBuffer = 0.0;
                            m_iStatusScale = SCALE_READY;
                        }
                    }
                } catch (IOException eIO) {
                }
                break;
        }
    }
}