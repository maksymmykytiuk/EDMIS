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
package com.progdan.pdf2txt.pdmodel.font;

import java.io.IOException;

import com.progdan.pdf2txt.cos.COSDictionary;
import com.progdan.pdf2txt.cos.COSName;

/**
 * This will create the correct type of font based on information in the dictionary.
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public class PDFontFactory
{
    /**
     * private constructor, should only use static methods in this class.
     */
    private PDFontFactory()
    {
    }

    /**
     * This will create the correct font based on information in the dictionary.
     *
     * @param dic The populated dictionary.
     *
     * @return The corrent implementation for the font.
     *
     * @throws IOException If the dictionary is not valid.
     */
    public static PDFont createFont( COSDictionary dic ) throws IOException
    {
        PDFont retval = null;

        COSName type = (COSName)dic.getDictionaryObject( COSName.TYPE );
        if( !type.equals( COSName.FONT ) )
        {
            throw new IOException( "Cannot create font if /Type is not /Font.  Actual=" +type );
        }

        COSName subType = (COSName)dic.getDictionaryObject( COSName.SUBTYPE );
        if( subType.equals( COSName.getPDFName( "Type1" ) ) )
        {
            retval = new PDType1Font( dic );
        }
        else if( subType.equals( COSName.getPDFName( "MMType1" ) ) )
        {
            retval = new PDMMType1Font( dic );
        }
        else if( subType.equals( COSName.getPDFName( "TrueType" ) ) )
        {
            retval = new PDTrueTypeFont( dic );
        }
        else if( subType.equals( COSName.getPDFName( "Type3" ) ) )
        {
            retval = new PDType3Font( dic );
        }
        else if( subType.equals( COSName.getPDFName( "Type0" ) ) )
        {
            retval = new PDType0Font( dic );
        }
        else if( subType.equals( COSName.getPDFName( "CIDFontType0" ) ) )
        {
            retval = new PDCIDFontType0Font( dic );
        }
        else if( subType.equals( COSName.getPDFName( "CIDFontType2" ) ) )
        {
            retval = new PDCIDFontType2Font( dic );
        }
        else
        {
            throw new IOException( "Unknown font subtype=" + subType );
        }

        return retval;
    }
}
