//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2009 Openbravo, S.L.
//    http://www.openbravo.com/product/pos
//
//    This file is part of Openbravo POS.
//
//    Openbravo POS is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Openbravo POS is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Openbravo POS.  If not, see <http://www.gnu.org/licenses/>.

package com.openbravo.pos.printer.escpos;

public class UnicodeTranslator866Cyr extends UnicodeTranslator {

    /** Creates a new instance of UnicodeTranslatorInt */
    public UnicodeTranslator866Cyr() {
    }

    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_RUS;
    }

    public byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {

                default: return 0x3F; // ? Not valid character.

            // ru - Russian for 866 (Cyrillic) code page
            case '\u0410': return (byte) 0x80;// A
            case '\u0411': return (byte) 0x81;// Б
            case '\u0412': return (byte) 0x82;// В
            case '\u0413': return (byte) 0x83;// Г
            case '\u0414': return (byte) 0x84;// Д
            case '\u0415': return (byte) 0x85;// Е
            case '\u0401': return (byte) 0xF0;// Ё
            case '\u0416': return (byte) 0x86;// Ж
            case '\u0417': return (byte) 0x87;// З
            case '\u0418': return (byte) 0x88;// И
            case '\u0419': return (byte) 0x89;// Й
            case '\u041A': return (byte) 0x8A;// К
            case '\u041B': return (byte) 0x8B;// Л
            case '\u041C': return (byte) 0x8C;// М
            case '\u041D': return (byte) 0x8D;// Н
            case '\u041E': return (byte) 0x8E;// О
            case '\u041F': return (byte) 0x8F;// П
            case '\u0420': return (byte) 0x90;// Р
            case '\u0421': return (byte) 0x91;// С
            case '\u0422': return (byte) 0x92;// Т
            case '\u0423': return (byte) 0x93;// У
            case '\u0424': return (byte) 0x94;// Ф
            case '\u0425': return (byte) 0x95;// Х
            case '\u0426': return (byte) 0x96;// Ц
            case '\u0427': return (byte) 0x97;// Ч
            case '\u0428': return (byte) 0xE8;// Ш
            case '\u0429': return (byte) 0xE9;// Щ
            case '\u042A': return (byte) 0x9A;// Ъ
            case '\u042B': return (byte) 0x9B;// Ы
            case '\u042C': return (byte) 0x9C;// Ь
            case '\u042D': return (byte) 0x9D;// Э
            case '\u042E': return (byte) 0x9E;// Ю
            case '\u042F': return (byte) 0x9F;// Я
            case '\u0430': return (byte) 0xA0;// a
            case '\u0431': return (byte) 0xA1;// б
            case '\u0432': return (byte) 0xA2;// в
            case '\u0433': return (byte) 0xA3;// г
            case '\u0434': return (byte) 0xA4;// д
            case '\u0435': return (byte) 0xA5;// е
            case '\u0451': return (byte) 0xF1;// ё
            case '\u0436': return (byte) 0xA6;// ж
            case '\u0437': return (byte) 0xA7;// з
            case '\u0438': return (byte) 0xA8;// и
            case '\u0439': return (byte) 0xA9;// й
            case '\u043A': return (byte) 0xAA;// к
            case '\u043B': return (byte) 0xAB;// л
            case '\u043C': return (byte) 0xAC;// м
            case '\u043D': return (byte) 0xAD;// н
            case '\u043E': return (byte) 0xAE;// о
            case '\u043F': return (byte) 0xAF;// п
            case '\u0440': return (byte) 0xE0;// р
            case '\u0441': return (byte) 0xE1;// с
            case '\u0442': return (byte) 0xE2;// т
            case '\u0443': return (byte) 0xE3;// у
            case '\u0444': return (byte) 0xE4;// ф
            case '\u0445': return (byte) 0xE5;// х
            case '\u0446': return (byte) 0xE6;// ц
            case '\u0447': return (byte) 0xE7;// ч
            case '\u0448': return (byte) 0xE8;// ш
            case '\u0449': return (byte) 0xE9;// щ
            case '\u044A': return (byte) 0xEC;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEA;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

            case '\u00A0': return (byte) 0x7F;// &nbsp
            case '\u2116': return (byte) 0xFC;// №
            }
        }
    }
}