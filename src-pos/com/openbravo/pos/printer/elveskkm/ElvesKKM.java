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
package com.openbravo.pos.printer.elveskkm;

public class ElvesKKM {

    public static final byte ETX    = 0x03; //Символ конца блока данных
    public static final byte INIT   = 0x3f; //Запрос состояния ККМ
    public static final byte PRN    = 0x4C; //Печать строки текста
    public static final byte BEEP   = 0x47; //Гудок

    public static final byte TITLE  = 0x6C; //Печать клише чека
    public static final byte OPEN_DRAWER  = (byte) 0x80; //Открыть денежный ящик
}
