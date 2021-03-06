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
package com.progdan.pdf2txt.pdmodel.encryption;

import com.progdan.pdf2txt.cos.COSDictionary;
import com.progdan.pdf2txt.cos.COSInteger;
import com.progdan.pdf2txt.cos.COSName;
import com.progdan.pdf2txt.cos.COSNumber;
import com.progdan.pdf2txt.cos.COSString;

import java.io.IOException;

/**
 * This class holds information that is related to the standard PDF encryption.
 *
 * See PDF Reference 1.4 section "3.5 Encryption"
 *
 * @author  Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public class PDStandardEncryption extends PDEncryptionDictionary
{
    /**
     * The 'Filter' name for this security handler.
     */
    public static final String FILTER_NAME = "Standard";

    /**
     * The default revision of one is not specified.
     */
    public static final int DEFAULT_REVISION = 3;

    /**
     * Encryption revision 2.
     */
    public static final int REVISION2 = 2;
    /**
     * Encryption revision 3.
     */
    public static final int REVISION3 = 3;
    /**
     * Encryption revision 4.
     */
    public static final int REVISION4 = 4;

    /**
     * The default set of permissions which is to allow all.
     */
    public static final int DEFAULT_PERMISSIONS = 0xFFFFFFFF ^ 3;//bits 0 & 1 need to be zero

    private static final int PRINT_BIT = 3;
    private static final int MODIFICATION_BIT = 4;
    private static final int EXTRACT_BIT = 5;
    private static final int MODIFY_ANNOTATIONS_BIT = 6;
    private static final int FILL_IN_FORM_BIT = 9;
    private static final int EXTRACT_FOR_ACCESSIBILITY_BIT = 10;
    private static final int ASSEMBLE_DOCUMENT_BIT = 11;
    private static final int DEGRADED_PRINT_BIT = 12;

    /**
     * Default constructor that uses Version 2, Revision 3, 40 bit encryption,
     * all permissions allowed.
     */
    public PDStandardEncryption()
    {
        super();
        encryptionDictionary.setItem( COSName.FILTER, COSName.getPDFName( FILTER_NAME ) );
        setVersion( PDEncryptionDictionary.VERSION1_40_BIT_ALGORITHM  );
        setRevision( PDStandardEncryption.REVISION2 );
        setPermissions( DEFAULT_PERMISSIONS );
    }

    /**
     * Constructor from existing dictionary.
     *
     * @param dict The existing encryption dictionary.
     */
    public PDStandardEncryption( COSDictionary dict )
    {
        super( dict );
    }

    /**
     * This will return the R entry of the encryption dictionary.<br /><br />
     * See PDF Reference 1.4 Table 3.14.
     *
     * @return The encryption revision to use.
     */
    public int getRevision()
    {
        int revision = DEFAULT_VERSION;
        COSNumber cosRevision = (COSNumber)encryptionDictionary.getDictionaryObject( COSName.getPDFName( "R" ) );
        if( cosRevision != null )
        {
            revision = cosRevision.intValue();
        }
        return revision;
    }

    /**
     * This will set the R entry of the encryption dictionary.<br /><br />
     * See PDF Reference 1.4 Table 3.14.  <br /><br/>
     *
     * <b>Note: This value is used to decrypt the pdf document.  If you change this when
     * the document is encrypted then decryption will fail!.</b>
     *
     * @param revision The new encryption version.
     */
    public void setRevision( int revision )
    {
        encryptionDictionary.setItem( COSName.getPDFName( "R" ), new COSInteger( revision ) );
    }

    /**
     * This will get the O entry in the standard encryption dictionary.
     *
     * @return A 32 byte array or null if there is no owner key.
     */
    public byte[] getOwnerKey()
    {
       byte[] o = null;
       COSString owner = (COSString)encryptionDictionary.getDictionaryObject( COSName.getPDFName( "O" ) );
       if( owner != null )
       {
           o = owner.getBytes();
       }
       return o;
    }

    /**
     * This will set the O entry in the standard encryption dictionary.
     *
     * @param o A 32 byte array or null if there is no owner key.
     *
     * @throws IOException If there is an error setting the data.
     */
    public void setOwnerKey( byte[] o ) throws IOException
    {
       COSString owner = new COSString();
       owner.append( o );
       encryptionDictionary.setItem( COSName.getPDFName( "O" ), owner );
    }

    /**
     * This will get the U entry in the standard encryption dictionary.
     *
     * @return A 32 byte array or null if there is no user key.
     */
    public byte[] getUserKey()
    {
       byte[] u = null;
       COSString user = (COSString)encryptionDictionary.getDictionaryObject( COSName.getPDFName( "U" ) );
       if( user != null )
       {
           u = user.getBytes();
       }
       return u;
    }

    /**
     * This will set the U entry in the standard encryption dictionary.
     *
     * @param u A 32 byte array.
     *
     * @throws IOException If there is an error setting the data.
     */
    public void setUserKey( byte[] u ) throws IOException
    {
       COSString user = new COSString();
       user.append( u );
       encryptionDictionary.setItem( COSName.getPDFName( "U" ), user );
    }

    /**
     * This will get the permissions bit mask.
     *
     * @return The permissions bit mask.
     */
    public int getPermissions()
    {
        int permissions = 0;
        COSInteger p = (COSInteger)encryptionDictionary.getDictionaryObject( COSName.getPDFName( "P" ) );
        if( p != null )
        {
            permissions = p.intValue();
        }
        return permissions;
    }

    /**
     * This will set the permissions bit mask.
     *
     * @param p The new permissions bit mask
     */
    public void setPermissions( int p )
    {
        encryptionDictionary.setItem( COSName.getPDFName( "P" ), new COSInteger( p ) );
    }

    private boolean isPermissionBitOn( int bit )
    {
        return (getPermissions() & (1 << (bit-1))) != 0;
    }

    private boolean setPermissionBit( int bit, boolean value )
    {
        int permissions = getPermissions();
        if( value )
        {
            permissions = permissions | (1 << (bit-1));
        }
        else
        {
            permissions = permissions & (0xFFFFFFFF ^ (1 << (bit-1)));
        }
        setPermissions( permissions );

        return (getPermissions() & (1 << (bit-1))) != 0;
    }

    /**
     * This will tell if the user can print.
     *
     * @return true If supplied with the user password they are allowed to print.
     */
    public boolean canPrint()
    {
        return isPermissionBitOn( PRINT_BIT );
    }

    /**
     * Set if the user can print.
     *
     * @param allowPrinting A boolean determining if the user can print.
     */
    public void setCanPrint( boolean allowPrinting )
    {
        setPermissionBit( PRINT_BIT, allowPrinting );
    }

    /**
     * This will tell if the user can modify contents of the document.
     *
     * @return true If supplied with the user password they are allowed to modify the document
     */
    public boolean canModify()
    {
        return isPermissionBitOn( MODIFICATION_BIT );
    }

    /**
     * Set if the user can modify the document.
     *
     * @param allowModifications A boolean determining if the user can modify the document.
     */
    public void setCanModify( boolean allowModifications )
    {
        setPermissionBit( MODIFICATION_BIT, allowModifications );
    }

    /**
     * This will tell if the user can extract text and images from the PDF document.
     *
     * @return true If supplied with the user password they are allowed to extract content
     *              from the PDF document
     */
    public boolean canExtractContent()
    {
        return isPermissionBitOn( EXTRACT_BIT );
    }

    /**
     * Set if the user can extract content from the document.
     *
     * @param allowExtraction A boolean determining if the user can extract content
     *                        from the document.
     */
    public void setCanExtractContent( boolean allowExtraction )
    {
        setPermissionBit( EXTRACT_BIT, allowExtraction );
    }

    /**
     * This will tell if the user can add/modify text annotations, fill in interactive forms fields.
     *
     * @return true If supplied with the user password they are allowed to modify annotations.
     */
    public boolean canModifyAnnotations()
    {
        return isPermissionBitOn( MODIFY_ANNOTATIONS_BIT );
    }

    /**
     * Set if the user can modify annotations.
     *
     * @param allowAnnotationModification A boolean determining if the user can modify annotations.
     */
    public void setCanModifyAnnotations( boolean allowAnnotationModification )
    {
        setPermissionBit( MODIFY_ANNOTATIONS_BIT, allowAnnotationModification );
    }

    /**
     * This will tell if the user can fill in interactive forms.
     *
     * @return true If supplied with the user password they are allowed to fill in form fields.
     */
    public boolean canFillInForm()
    {
        return isPermissionBitOn( FILL_IN_FORM_BIT );
    }

    /**
     * Set if the user can fill in interactive forms.
     *
     * @param allowFillingInForm A boolean determining if the user can fill in interactive forms.
     */
    public void setCanFillInForm( boolean allowFillingInForm )
    {
        setPermissionBit( FILL_IN_FORM_BIT, allowFillingInForm );
    }

    /**
     * This will tell if the user can extract text and images from the PDF document
     * for accessibility purposes.
     *
     * @return true If supplied with the user password they are allowed to extract content
     *              from the PDF document
     */
    public boolean canExtractForAccessibility()
    {
        return isPermissionBitOn( EXTRACT_FOR_ACCESSIBILITY_BIT );
    }

    /**
     * Set if the user can extract content from the document for accessibility purposes.
     *
     * @param allowExtraction A boolean determining if the user can extract content
     *                        from the document.
     */
    public void setCanExtractForAccessibility( boolean allowExtraction )
    {
        setPermissionBit( EXTRACT_FOR_ACCESSIBILITY_BIT, allowExtraction );
    }

    /**
     * This will tell if the user can insert/rotate/delete pages.
     *
     * @return true If supplied with the user password they are allowed to extract content
     *              from the PDF document
     */
    public boolean canAssembleDocument()
    {
        return isPermissionBitOn( ASSEMBLE_DOCUMENT_BIT );
    }

    /**
     * Set if the user can insert/rotate/delete pages.
     *
     * @param allowAssembly A boolean determining if the user can assemble the document.
     */
    public void setCanAssembleDocument( boolean allowAssembly )
    {
        setPermissionBit( ASSEMBLE_DOCUMENT_BIT, allowAssembly );
    }

    /**
     * This will tell if the user can print the document in a degraded format.
     *
     * @return true If supplied with the user password they are allowed to print the
     *              document in a degraded format.
     */
    public boolean canPrintDegraded()
    {
        return isPermissionBitOn( DEGRADED_PRINT_BIT );
    }

    /**
     * Set if the user can print the document in a degraded format.
     *
     * @param allowAssembly A boolean determining if the user can print the
     *        document in a degraded format.
     */
    public void setCanPrintDegraded( boolean allowAssembly )
    {
        setPermissionBit( DEGRADED_PRINT_BIT, allowAssembly );
    }
}
