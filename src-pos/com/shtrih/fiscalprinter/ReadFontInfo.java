/*
 * ReadFontInfo.java
 *
 * Created on 15 January 2009, 12:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.shtrih.fiscalprinter;

/**
 *
 * @author V.Kravtsov
 */

/****************************************************************************

    Get Font Parameters
 
    Command:	26H. Length: 6 bytes.
    ·	System Administrator password (4 bytes) 30
    ·	Font type (1 byte)
 
    Answer:		26H. Length: 7 bytes.
    ·	Result Code (1 byte)
    ·	Print width in dots (2 bytes)
    ·	Character width in dots (1 byte) the width is given together with inter-character spacing
    ·	Character height in dots (1 byte) the height is given together with inter-line spacing
    ·	Number of fonts in FP (1 byte)
    
****************************************************************************/

public final class ReadFontInfo extends PrinterCommand
{
    
    class FontInfo
    {
        private final int font;
        private final int paperWidth;
        private final int charWidth;
        private final int charHeight;
        private final int fontCount;
        
        public FontInfo(int font, int paperWidth, int charWidth, 
            int charHeight, int fontCount)
        {
            this.font = font;
            this.paperWidth = paperWidth;
            this.charWidth = charWidth;
            this.charHeight = charHeight;
            this.fontCount = fontCount;
        }
        
        public int getFont(){ return font;}
    }
    
    // in params
    private final int password;
    private final int font;
    // out params
    private FontInfo fontInfo;
    
    /**
     * Creates a new instance of ReadFontInfo
     */
    public ReadFontInfo(int password, int font) 
    {
        this.password = password;
        this.font = font;
    }
    
    public final int getCode()
    {
        return 0x26;
    }
    
    public final String getText()
    {
        return "Get font info";
    }
    
    public final void encode(CommandOutputStream out) 
        throws Exception
    {
        out.writeInt(password);
        out.writeInt(font);
    }
    
    public final void decode(CommandInputStream in)
        throws Exception
    {
        int paperWidth = in.readShort();
        int charWidth = in.readByte();
        int charHeight = in.readByte();
        int fontCount = in.readByte();
        fontInfo = new FontInfo(font, paperWidth, charWidth, charHeight, fontCount);
    }
    
    public FontInfo getFontInfo()
    {
        return fontInfo;
    }
    
}
