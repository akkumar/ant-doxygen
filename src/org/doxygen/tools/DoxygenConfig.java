// -*- Mode: Java; indent-tabs-mode: nil; c-basic-offset: 4; -*-
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
//----------------------------------------------------------------------
// $Header: /cvsroot/ant-doxygen/ant_task/src/org/doxygen/tools/Attic/DoxygenConfig.java,v 1.1.2.4 2004/02/05 00:52:32 jfrotz Exp $
//
package org.doxygen.tools;

import org.apache.tools.ant.BuildException;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Properties;
import java.util.Map;

import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/** This class holds the Configuration properties of Doxygen.
 *
 *  @version $Revision: 1.1.2.4 $
 *  @since Ant-Doxygen 1.3.1
 */
public class DoxygenConfig {

    /** List of nested properties. */
    private Map<String, DoxygenTask.Property> taskProperties = new TreeMap<String, DoxygenTask.Property>();





    //----------------------------------------------------------------------
    /** This constructor sets the initial Doxygen properties that are
     *  different from the default Doxygen configuration.  Namely (in
     *  Doxygen configuration file terms):
     *
     *  <ul>
     *   <li> DETAILS_AT_TOP = YES
     *   <li> EXPAND_TOC = YES
     *   <li> FILE_PATTERNS = *.java
     *   <li> GENERATE_LATEX = NO
     *   <li> HAVE_DOT = YES
     *   <li> HIDE_UNDOC_MEMBERS = YES
     *   <li> INLINE_SOURCES = YES
     *   <li> INPUT = src
     *   <li> OPTIMIZE_OUTPUT_JAVA = YES
     *   <li> OUTPUT_DIRECTORY = doc
     *   <li> QUIET = YES
     *   <li> RECURSIVE = YES
     *   <li> SOURCE_BROWSER = YES
     *  </ul>
     *
     */
    public DoxygenConfig() {
    	//DETAILS_AT_TOP is deprecated since 1.5.8 
        //setProperty("DETAILS_AT_TOP", true);
    	
    	//since 1.5.8 of doxygen - EXPAND_TOC is changed to TOC_EXPAND . 
    	//Until we figure out a better way - this is being commented out.
        //setProperty("EXPAND_TOC", true);
        //setProperty("FILE_PATTERNS", "*.java");
        setProperty("GENERATE_LATEX", false);
        setProperty("GENERATE_TREEVIEW", true);
        setProperty("HIDE_UNDOC_MEMBERS", true);
        setProperty("INLINE_SOURCES", true);
        setProperty("INPUT", "src");
        //setProperty("OPTIMIZE_OUTPUT_JAVA", true);
        setProperty("OUTPUT_DIRECTORY", "doc");
        setProperty("QUIET", true);
        setProperty("RECURSIVE", true);
        setProperty("SOURCE_BROWSER", true);
    }




    //----------------------------------------------------------------------
    /** This method translates an Ant &lt;doxygen&gt; task element
     *  into a Doxygen configuration file property name/value
     *  pair.
     *
     *  @test String properties containing spaces are double
     *  quoted.
     *
     *  @param keyName for this property.
     *  @param value for this property.
     *
     */
    public final void setProperty(final String keyName,
                                  final String value) {
        DoxygenTask.Property nestedElement = getProperty(keyName);
        taskProperties.put(keyName, nestedElement);

        String val = value;
        //TODO: Some properties like FILE_PATTERNS do not like spaces though. 
        // There must be some optional way to set the same.
        if (val.indexOf(' ') > -1) { val =  "\"" + val + "\""; }
        nestedElement.setValue(val);

    }




    //----------------------------------------------------------------------
    /** This method translates an Ant &lt;doxygen&gt; task element into a
     *  Doxygen configuration file property name/value pair.
     *
     *  @param keyName for this property.
     *  @param value for this property.
     *
     */
    public final void setProperty(final String keyName,
                                  final int value) {
        DoxygenTask.Property nestedElement = getProperty(keyName);
        taskProperties.put(keyName, nestedElement);
        nestedElement.setValue("" + value);
    }




    //----------------------------------------------------------------------
    /** This method translates an Ant &lt;doxygen&gt; task element into a
     *  Doxygen configuration file property name/value pair.
     *
     *  @param keyName for this property.
     *  @param value for this property.
     *
     */
    public final void setProperty(final String keyName,
                                  final boolean value) {
        DoxygenTask.Property nestedElement = getProperty(keyName);
        taskProperties.put(keyName, nestedElement);

        String val = "YES";
        if  (!value) { val = "NO"; }
        nestedElement.setValue(val);
    }





    //----------------------------------------------------------------------
    /** This method returns the attributes for jUnit test analysis.
     *
     *  @param keyName to be retreived.
     *
     *  @return a <code>Property</code> containing as much of the
     *  specified attribute as is currently set.
     *
     */
    public final DoxygenTask.Property getProperty(final String keyName) {
        DoxygenTask.Property retval = null;
        if  (taskProperties.containsKey(keyName)) {
            retval = taskProperties.get(keyName);
        }
        if  (retval == null) {
            retval = new DoxygenTask.Property();
            retval.setName(keyName);
        }
        return retval;
    }




    //----------------------------------------------------------------------
    /** This method writes and synchronizes the properties.
     *
     *  @test If specified, the base configuration file is not
     *  overwritten.
     *
     *  @param theConfigFilename used by Doxygen.
     *
     */
    public final void writeDoxygenConfig(final String theConfigFilename) {

        PrintStream ps = null;
        SortedMap<String, String> map = readDoxygenConfig(theConfigFilename);
        cascadeDoxygenConfig(map);
        try {
            ps = new PrintStream(
                     new FileOutputStream(DoxygenTask.CONFIG_FILE));
            Set<String> keys = map.keySet();
            for (String key : keys) {
                String param = key;
                String value = map.get(key);
                String line  = param + "\t=";
                if  (value != null) { line += " " + value; }
                ps.println(line);
            }
            DoxygenTask.activityLog(false, "Updated Doxygen config file: "
                                    + "[" + DoxygenTask.CONFIG_FILE + "]");
        } catch (IOException ioe) {
            throw new BuildException("Unable to update Doxygen config file: "
                                     + "[" + theConfigFilename + "]", ioe);
        } finally {
            if  (ps != null) {
                ps.close();
                ps = null;
            }
        }
    }





    //----------------------------------------------------------------------
    /** This method reads the Doxygen generated configuration file.
     *
     *  @note Due to the use of java.util.Properties, all comments in
     *  the base configuration file are dropped in the almagamated
     *  configuration file.  This is acceptable, since the base
     *  configuration file is not overwritten and the almagamated file
     *  is alphabetized.
     *
     *  @bug ID=xxxxxx: In controlling Doxygen config file generation
     *  verbosity, that output now appears in the config file input
     *  stream.
     *
     *  @param theConfigFilename generated by Doxygen -g.
     *
     *  @return a <code>SortedMap</code> containing all of the Doxygen
     *  parameters to be used in the amalgamated configuration file.
     *
     */
    public final SortedMap<String, String> readDoxygenConfig(final String theConfigFilename) {
        SortedMap<String, String> map = new TreeMap<String, String>();
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(theConfigFilename));
            Set<Object> names = p.keySet();
            for (Object obj : names) {
                map.put( (String) obj, p.getProperty( (String) obj));
            }
        } catch (IOException ioe) {
            throw new BuildException("Unable to read Doxygen config file: "
                                     + "[" + theConfigFilename + "]", ioe);
        }
        return map;
    }




    //----------------------------------------------------------------------
    /** This method cascades all Ant task attribute and nest attribute
     *  values into the passed SortedMap instance.  This SortedMap
     *  instance is supposed to have been populated from the generated
     *  or user-specified Doxygen configuraton file.
     *
     *  @test the minimalist case has open-source / Java flavor
     *  Doxygen parameters set.
     *
     *  @test &lt;doxygen&gt; elements can influence Doxygen.
     *
     *  @test &lt;doxygen&gt; nested elements can influence Doxygen.
     *
     *  @test &lt;doxygen&gt; nested elements take precedence over
     *  &lt;doxygen&gt; non-nested elements.
     *
     *  @todo Add the Doxygen parameter version checking.
     *
     *  @param map contains all of the Doxygen configuration file basis.
     *
     */
    public final void cascadeDoxygenConfig(final SortedMap<String, String> map) {
    	Collection<DoxygenTask.Property> coll = taskProperties.values();
    	for (DoxygenTask.Property nestedElement : coll) {
                map.put(nestedElement.getName(), nestedElement.getValue());
        }
    }
}
