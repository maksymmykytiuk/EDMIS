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
package com.progdan.pdf2txt.cos;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an abstract number in a PDF document.
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public abstract class COSNumber extends COSBase
{
    /**
     * ZERO.
    */
    public static final COSInteger ZERO = new COSInteger( 0 );
    /**
     * ONE.
    */
    public static final COSInteger ONE = new COSInteger( 1 );
    private static final Map COMMON_NUMBERS = new HashMap();

    static
    {
        COMMON_NUMBERS.put( "0", ZERO );
        COMMON_NUMBERS.put( "1", ONE );
    }

    /**
     * This will get the float value of this number.
     *
     * @return The float value of this object.
     */
    public abstract float floatValue();

    /**
     * This will get the double value of this number.
     *
     * @return The double value of this number.
     */
    public abstract double doubleValue();

    /**
     * This will get the integer value of this number.
     *
     * @return The integer value of this number.
     */
    public abstract int intValue();

    /**
     * This will get the long value of this number.
     *
     * @return The long value of this number.
     */
    public abstract long longValue();

    /**
     * This factory method will get the appropriate number object.
     *
     * @param number The string representation of the number.
     *
     * @return A number object, either float or int.
     *
     * @throws IOException If the string is not a number.
     */
    public static COSNumber get( String number ) throws IOException
    {
        COSNumber result = (COSNumber)COMMON_NUMBERS.get( number );
        if( result == null )
        {
            if (number.indexOf('.') >= 0)
            {
                result = new COSFloat( number );
            }
            else
            {
                result = new COSInteger( number );
            }
        }
        return result;
    }
}
