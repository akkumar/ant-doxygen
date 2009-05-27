/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package test.org.doxygen.tools;

import org.doxygen.tools.DoxygenTask;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * Test case for Ant task for Doxygen.
 *
 * @author <a href="mailto:akkumar@users.sourceforge.net">Karthik Kumar</a>
 *
 * @since Ant 1.6
 *
 */
public class DoxygenTest extends TestCase {

    /**
     * This is the instance of the task used by all the
     * test cases.
     **/
    private DoxygenTask doxygenTask;

    /**
     * Output Directory root of the doxygen utility.
     * All directories for html and latex are created by
     * doxygen under this.
     **/
    private static final String OUTPUT_DIR = ".\\test\\output" ;

    /**
     * Input Directory for the doxygen utility.
     **/
    private static final String INPUT_DIR = ".\\test\\input" ;

    /**
     * The html directory created by doxygen when generating
     * html files.
     **/
    static final String HTML_DIR =
            OUTPUT_DIR + File.separator + "html";

    /**
     * The latex directory created by doxygen when generating
     * latex files.
     **/
    static final String LATEX_DIR =
            OUTPUT_DIR + File.separator + "latex";

    /**
     * File reference to HTML_DIR
     **/
    private final File htmlDir = new File(HTML_DIR);

    /**
     * File reference to LATEX_DIR
     **/
    private final File latexDir = new File(LATEX_DIR);

    /**
     * Sets up a new instance of the task before applying tests
     **/
    public void setUp() {
         doxygenTask = new DoxygenTask();
         System.out.println("Setting up test case");
    }

    /**
     * Destroys the task instance created.
     **/
    public void tearDown() {
        doxygenTask = null;
    }


    /**
     * Returns the suite of test included in this test case
     * @return Returns a Test suite
     **/
    public static Test suite() {
        return new TestSuite(DoxygenTest.class);
    }


    /**
     * Test case for generating html and not
     * generting latex.
     **/
    public void testHtmlOnly() {
        DoxygenTask.Property thisProp = new DoxygenTask.Property();
                
        try {
            doxygenTask.execute();
            assertEquals(true, htmlDir.exists());
            deleteDir(htmlDir);
        } catch (BuildException ex) {
            System.out.println("Error executing doxygen task " + ex);
        }
    }

    /**
     * Recursive deletes the directory tree under File f.
     * @param f Directory tree root that needs to be destroyed
     **/
    private void deleteDir (File f) {
        File [] contents = f.listFiles();
        for (int i = 0 ; i < contents.length; i++) {
            if (!contents[i].delete()) {
                System.out.println(" unable to delete " + contents[i]) ;
            }
        }
        if (!f.delete()) {
            System.out.println("Error deleting directory " + f);
        }
    }

}
