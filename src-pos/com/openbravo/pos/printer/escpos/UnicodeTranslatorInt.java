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

public class UnicodeTranslatorInt extends UnicodeTranslator {
    
    /** Creates a new instance of UnicodeTranslatorInt */
    public UnicodeTranslatorInt() {
    }
    
    public byte[] getCodeTable() {
        return ESCPOS.CODE_TABLE_13;            
    }
    
    public byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
                case '\u00c7': return -0x80;
                case '\u00fc': return -0x7F; // u dieresis
                case '\u00e2': return -0x7D;
                case '\u00e4': return -0x7C;
                case '\u00e0': return -0x7B;
                case '\u00E5': return -0x7A; // ao
                case '\u00e7': return -0x79;
                case '\u00ea': return -0x78;
                case '\u00eb': return -0x77;
                case '\u00e8': return -0x76;
                case '\u00ef': return -0x75;
                case '\u00ee': return -0x74;
                case '\u00ec': return -0x73;
                case '\u00c4': return -0x72;
                case '\u00C5': return -0x71; // Ao
               
                case '\u00c9': return -0x70; // E acute
                case '\u00E6': return -0x6F; // ae
                case '\u00C6': return -0x6E; // AE
                case '\u00f4': return -0x6D;
                case '\u00f6': return -0x6C;
                case '\u00f2': return -0x6B;
                case '\u00fb': return -0x6A;
                case '\u00f9': return -0x69;
                case '\u00ff': return -0x68;
                case '\u00d6': return -0x67;
                case '\u00dc': return -0x66; // U dieresis
                case '\u00F8': return -0x65; // o/
                case '\u00A3': return -0x64; // L- Libra esterlina
                case '\u00D8': return -0x63; // O/
                case '\u00D7': return -0x62; // X
//                case '': return -0x61; // f
               
                case '\u00e1': return -0x60; // a acute
                case '\u00e9': return -0x7E; // e acute
                case '\u00ed': return -0x5F; // i acute
                case '\u00f3': return -0x5E; // o acute
                case '\u00fa': return -0x5D; // u acute
                case '\u00f1': return -0x5C; // n tilde
                case '\u00d1': return -0x5B; // N tilde
                case '\u00aa': return -0x5A;
                case '\u00ba': return -0x59;
                case '\u00bf': return -0x58; // abrir interrogacion
                case '\u00AE': return -0x57; // (R) Registrado
                case '\u00ac': return -0x56; // Not sign
                case '\u00BD': return -0x55; // 1/2
                case '\u00BC': return -0x54; // 1/4
                case '\u00a1': return -0x53; // abrir admiracion
                case '\u00AB': return -0x52; // <<
                case '\u00BB': return -0x51; // >>
               
                case '\u2591': return -0x50; // Light shade
                case '\u2592': return -0x4F; // Medium shade
                case '\u2593': return -0x4E; // Dark shade
                case '\u2502': return -0x4D; // BOX DRAWINGS LIGHT VERTICAL
                case '\u2524': return -0x4C; // BOX DRAWINGS LIGHT VERTICAL AND LEFT
                case '\u00c1': return -0x4B; // A acute
                case '\u00c2': return -0x4A;
                case '\u00c0': return -0x49;
                case '\u00A9': return -0x48; // (c) Copyright
                case '\u2563': return -0x47; // BOX DRAWINGS DOUBLE VERTICAL AND LEFT
                case '\u2551': return -0x46; // BOX DRAWINGS DOUBLE VERTICAL
                case '\u2557': return -0x45; // BOX DRAWINGS DOUBLE DOWN AND LEFT
                case '\u255D': return -0x44; // BOX DRAWINGS DOUBLE UP AND LEFT
                case '\u00A2': return -0x43; // Cent Sign
                case '\u00A5': return -0x42; // Yen Sign
                case '\u2510': return -0x41; // BOX DRAWINGS LIGHT DOWN AND LEFT
               
                case '\u2514': return -0x40; // BOX DRAWINGS LIGHT UP ANDRIGHT
//                case '': return -0x3F; //
//                case '': return -0x3E; //
//                case '': return -0x3D; //
//                case '': return -0x3C; //
//                case '': return -0x3B; //
//                case '': return -0x3A; //
//                case '': return -0x39; //
//                case '': return -0x38; //
//                case '': return -0x37; //
//                case '': return -0x36; //
//                case '': return -0x35; //
//                case '': return -0x34; //
//                case '': return -0x33; //
//                case '': return -0x32; //
//                case '': return -0x31; //
               
//                case '': return -0x30; //
//                case '': return -0x2F; //
                case '\u00ca': return -0x2E; //
                case '\u00cb': return -0x2D; //
                case '\u00c8': return -0x2C; //
                case '\u20ac': return -0x2B; // Euro Sign
                case '\u00cd': return -0x2A; // I acute
                case '\u00ce': return -0x29; //
                case '\u00cf': return -0x28; //
//                case '': return -0x27; //
//                case '': return -0x26; //
//                case '': return -0x25; //
//                case '': return -0x24; //
                case '|': return -0x23; //
                case '\u00cc': return -0x22; //
//                case '': return -0x21; //
               
                case '\u00d3': return -0x20; // O acute
//                case '': return -0x1F; //
                case '\u00d4': return -0x1E; //
                case '\u00d2': return -0x1D; //
//                case '': return -0x1C; //
//                case '': return -0x1B; //
//                case '': return -0x1A; //
//                case '': return -0x19; //
//                case '': return -0x18; //
                case '\u00da': return -0x17; // U acute
                case '\u00db': return -0x16; //
                case '\u00d9': return -0x15; //
                case '\u00fd': return -0x14; //
                case '\u00dd': return -0x13; //
//                case '': return -0x12; //
                case '\u00b4': return -0x11; //
               
//                case '': return -0x10; //
//                case '': return -0x0F; //
//                case '': return -0x0E; //
//                case '': return -0x0D; //
//                case '': return -0x0C; //
//                case '': return -0x0B; //
//                case '': return -0x0A; //
//                case '': return -0x09; //
//                case '': return -0x08; //
                case '\u00a8': return -0x07; //
//                case '': return -0x06; //
//                case '': return -0x05; //
//                case '': return -0x04; //
//                case '': return -0x03; //
//                case '': return -0x02; //
                // case ' ': return -0x01; // SP
        
                default: return 0x3F; // ? Not valid character.
            
// Old translation                
//            case '\u00aa' : return (byte) 0xA6;
//            case '\u00ba' : return (byte) 0xA7;
//            case '\u00a1' : return (byte) 0xAD;
//            case '\u00bf' : return (byte) 0xA8;
//            case '\u00b7' : return (byte) 0xF9;
//            case '\u00f1' : return (byte) 0xA4;
//            case '\u00d1' : return (byte) 0xA5;
//            case '\u00e1' : return (byte) 0xA0;
//            case '\u00c1' : return (byte) 0x86;
//            case '\u00e9' : return (byte) 0x82;
//            case '\u00c9' : return (byte) 0x90;
//            case '\u00ed' : return (byte) 0xA1;
//            case '\u00cd' : return (byte) 0x8B;
//            case '\u00f3' : return (byte) 0xA2;
//            case '\u00d3' : return (byte) 0x9F;
//            case '\u00fa' : return (byte) 0xA3;
//            case '\u00da' : return (byte) 0x96;
//            case '\u00fc' : return (byte) 0x81;
//            case '\u00dc' : return (byte) 0x9A;
//            default: return (byte) sChar;

            // ru - Russian
            case '\u0410': return (byte) 0x80;// A
            case '\u0411': return (byte) 0x81;// Б
            case '\u0412': return (byte) 0x82;// В
            case '\u0413': return (byte) 0x83;// Г
            case '\u0414': return (byte) 0x84;// Д
            case '\u0415': return (byte) 0x85;// Е
            case '\u0401': return (byte) 0x85;// Ё
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
            case '\u0451': return (byte) 0xA5;// ё
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

            }          
        }
    }       
}
