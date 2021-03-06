/**
 * Copyright (c) 2004, ProgDan� Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of pdf2txt; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://progdan.no-ip.org:25000
 *
 */
package com.progdan.pdf2txt.pdmodel.graphics;

import com.progdan.pdf2txt.cos.COSArray;
import com.progdan.pdf2txt.cos.COSBase;
import com.progdan.pdf2txt.cos.COSDictionary;
import com.progdan.pdf2txt.cos.COSFloat;
import com.progdan.pdf2txt.cos.COSNumber;

import com.progdan.pdf2txt.pdmodel.common.COSObjectable;

import com.progdan.pdf2txt.pdmodel.font.PDFont;
import com.progdan.pdf2txt.pdmodel.font.PDFontFactory;

import java.io.IOException;

/**
 * This class represents a font setting used for the graphics state.  A font setting is a font and a
 * font size.  Maybe there is a better name for this?
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public class PDFontSetting implements COSObjectable
{
    private COSArray fontSetting = null;

    /**
     * Creates a blank font setting, font will be null, size will be 1.
     */
    public PDFontSetting()
    {
        fontSetting = new COSArray();
        fontSetting.add( null );
        fontSetting.add( new COSFloat( 1 ) );
    }

    /**
     * Constructs a font setting from an existing array.
     *
     * @param fs The new font setting value.
     */
    public PDFontSetting( COSArray fs )
    {
        fontSetting = fs;
    }

    /**
     * @see COSObjectable#getCOSObject()
     */
    public COSBase getCOSObject()
    {
        return fontSetting;
    }

    /**
     * This will get the font for this font setting.
     *
     * @return The font for this setting of null if one was not found.
     *
     * @throws IOException If there is an error getting the font.
     */
    public PDFont getFont() throws IOException
    {
        PDFont retval = null;
        COSBase font = fontSetting.get( 0 );
        if( font instanceof COSDictionary )
        {
            retval = PDFontFactory.createFont( (COSDictionary)font );
        }
        return retval;
    }

    /**
     * This will set the font for this font setting.
     *
     * @param font The new font.
     */
    public void setFont( PDFont font )
    {
        if( font == null )
        {
            fontSetting.set( 0, null );
        }
        else
        {
            fontSetting.set( 0, font.getCOSObject() );
        }
    }

    /**
     * This will get the size of the font.
     *
     * @return The size of the font.
     */
    public float getFontSize()
    {
        COSNumber size = (COSNumber)fontSetting.get( 1 );
        return size.floatValue();
    }

    /**
     * This will set the size of the font.
     *
     * @param size The new size of the font.
     */
    public void setFontSize( float size )
    {
        fontSetting.set( 1, new COSFloat( size ) );
    }
}
