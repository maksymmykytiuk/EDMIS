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

import com.progdan.pdf2txt.exceptions.COSVisitorException;

/**
 *
 * This class represents an integer number in a PDF document.
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public class COSInteger extends COSNumber
{

    private long value;

    /**
     * constructor.
     *
     * @param val The integer value of this object.
     */
    public COSInteger( long val )
    {
        value = val;
    }

    /**
     * constructor.
     *
     * @param val The integer value of this object.
     */
    public COSInteger( int val )
    {
        this( (long)val );
    }

    /**
     * This will create a new PDF Int object using a string.
     *
     * @param val The string value of the integer.
     *
     * @throws IOException If the val is not an integer type.
     */
    public COSInteger( String val ) throws IOException
    {
        try
        {
            value = Long.parseLong( val );
        }
        catch( NumberFormatException e )
        {
            throw new IOException( "Error: value is not an integer type actual='" + value + "'" );
        }
    }

    /**
     * @see Object#equals( Object )
     */
    public boolean equals(Object o)
    {
        return o instanceof COSInteger && ((COSInteger)o).intValue() == intValue();
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode()
    {
        //taken from java.lang.Long
        return (int)(value ^ (value >> 32));
    }

    /**
     * @see Object#toString()
     */
    public String toString()
    {
        return "COSInt{" + value + "}";
    }



    /**
     * polymorphic access to value as float.
     *
     * @return The float value of this object.
     */
    public float floatValue()
    {
        return (float)value;
    }

    /**
     * polymorphic access to value as float.
     *
     * @return The double value of this object.
     */
    public double doubleValue()
    {
        return (double)value;
    }

    /**
     * Polymorphic access to value as int
     * This will get the integer value of this object.
     *
     * @return The int value of this object,
     */
    public int intValue()
    {
        return (int)value;
    }

    /**
     * Polymorphic access to value as int
     * This will get the integer value of this object.
     *
     * @return The int value of this object,
     */
    public long longValue()
    {
        return value;
    }

    /**
     * visitor pattern double dispatch method.
     *
     * @param visitor The object to notify when visiting this object.
     * @return any object, depending on the visitor implementation, or null
     * @throws COSVisitorException If an error occurs while visiting this object.
     */
    public Object accept(ICOSVisitor visitor) throws COSVisitorException
    {
        return visitor.visitFromInt(this);
    }
}
