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
package com.progdan.pdf2txt.ant;

import java.io.File;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;

import org.apache.tools.ant.types.FileSet;

/**
 * This is an ant task that will allow pdf documents to be converted using an
 * and task.
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.2 $
 */
public class PDFToTextTask extends Task
{
    private List fileSets = new ArrayList();

    /**
     * Adds a set of files (nested fileset attribute).
     *
     * @param set Another fileset to add.
     */
    public void addFileset( FileSet set )
    {
        fileSets.add( set );
    }

    /**
     * This will perform the execution.
     */
    public void execute()
    {
        log( "PDFToTextTask executing" );
        Iterator fileSetIter = fileSets.iterator();
        while( fileSetIter.hasNext() )
        {
            FileSet next = (FileSet)fileSetIter.next();
            DirectoryScanner dirScanner = next.getDirectoryScanner( getProject() );
            dirScanner.scan();
            String[] files = dirScanner.getIncludedFiles();
            for( int i=0; i<files.length; i++ )
            {
                File f = new File( dirScanner.getBasedir(), files[i] );
                log( "processing: " + f.getAbsolutePath() );
                String pdfFile = f.getAbsolutePath();
                if( pdfFile.toUpperCase().endsWith( ".PDF" ) )
                {
                    String textFile = pdfFile.substring( 0, pdfFile.length() -3 );
                    textFile = textFile + "txt";
                    try
                    {
                        com.progdan.pdf2txt.ExtractText.main( new String[] { pdfFile, textFile } );
                    }
                    catch( Exception e )
                    {
                        log( "Error processing " + pdfFile + e.getMessage() );
                    }
                }
            }

        }
    }
}
