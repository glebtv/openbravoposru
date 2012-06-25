/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.printer.aurafr.util;

/**
 *
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class UnicodeConverterCP866KZ {

    public UnicodeConverterCP866KZ() {
    }

    public static final byte[] convertString(String sConvert) {
        byte bAux[] = new byte[sConvert.length()];
        for (int i = 0; i < sConvert.length(); i++) {
            bAux[i] = transChar(sConvert.charAt(i));
        }
        return bAux;
    }

    //Конвертация таблицы символов из UTF8
    private static byte transChar(char sChar) {
        if ((sChar >= 0x0000) && (sChar < 0x0080) && (sChar != 0x0024)) {
            return (byte) sChar;
        } else {
            switch (sChar) {
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
            case '\u044A': return (byte) 0xEA;// ъ
            case '\u044B': return (byte) 0xEB;// ы
            case '\u044C': return (byte) 0xEC;// ь
            case '\u044D': return (byte) 0xED;// э
            case '\u044E': return (byte) 0xEE;// ю
            case '\u044F': return (byte) 0xEF;// я

            // kk - Kazakh - Ә ә Ғ ғ Қ қ Ң ң Ө ө Ұ ұ Ү ү Һ һ І i
            
            case '\u04D8': return (byte) 0xB0;// Ә
            case '\u04D9': return (byte) 0xB1;// ә
            case '\u0492': return (byte) 0xDB;// Ғ
            case '\u0493': return (byte) 0xDC;// ғ
            case '\u049A': return (byte) 0xDE;// Қ
            case '\u049B': return (byte) 0xDF;// қ
            case '\u04A2': return (byte) 0xF0;// Ң
            case '\u04A3': return (byte) 0xF1;// ң
            case '\u04E8': return (byte) 0xF3;// Ө
            case '\u04E9': return (byte) 0xF4;// ө
            case '\u04B0': return (byte) 0xF5;// Ұ
            case '\u04B1': return (byte) 0xF6;// ұ
            case '\u04AE': return (byte) 0xF7;// Ү
            case '\u04AF': return (byte) 0xF8;// ү
            case '\u04BA': return (byte) 0xFD;// Һ
            case '\u04BB': return (byte) 0xFE;// һ
            case '\u0406': return (byte) 0x49;// І
            case '\u0456': return (byte) 0x69;// i

            case '\u00A0': return (byte) 0x7F;// &nbsp
            case '\u2116': return (byte) 0x24;// №
            case '\u20AC': return (byte) 0xF2;// €
            case '\u0024': return (byte) 0xFC;// $
            case '\u2014': return (byte) 0xDD;// —

                  default: return 0x3F; // ? Not valid character.
            }
        }
    }
}
