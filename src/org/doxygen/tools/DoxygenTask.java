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
// $Header: /cvsroot/ant-doxygen/ant_task/src/org/doxygen/tools/DoxygenTask.java,v 1.8.4.12 2004/02/05 01:10:25 jfrotz Exp $
//
package org.doxygen.tools;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import java.io.File;

/**
 * Ant task which invokes Doxygen to generate a documentation set. This task
 * provides a mapping between all current Doxygen configuration file options (as
 * of 1.2.18) and Ant build files.
 * 
 * @author <a href="mailto:akkumar@users.sourceforge.net">Karthik Kumar</a>
 * @version $Revision: 1.8.4.12 $
 * @since Ant 1.5
 * 
 */
public class DoxygenTask extends Task {

	/**
	 * Name of the temporary configuration file that will be created in
	 * ${user.home}.
	 * 
	 */
	public static String CONFIG_FILE = "";
	static {
		try {
			CONFIG_FILE = File.createTempFile("doxygen", ".cfg")
					.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Object containing the configuration options of DoxygenConf.
	 * 
	 */
	private DoxygenConfig conf;

	/**
	 * Object responsible for creating Doxygen processes and executing the same.
	 * 
	 */
	private DoxygenProcess proc;

	/**
	 * Flag to control the Ant task output verbosity.
	 * 
	 */
	private static boolean verbose = false;

	/**
	 * String that contains the version against which compatibility check is to
	 * be performed.
	 * 
	 */
	private String versionCompatible = null;

	/**
	 * Name of the configuration file used as a basis for applying Doxygen
	 * parameters. This value may be updated by the user via the
	 * configFilename="file" element of the Ant task.
	 * 
	 */
	private String configFilename = null;



	// ----------------------------------------------------------------------
	/**
	 * Initializes the Doxygen Configuration options.
	 **/
	public DoxygenTask() {
		Project antProject = getProject();
		proc = new DoxygenProcess(antProject);
		conf = new DoxygenConfig();
		if (antProject != null) {
			String projectName = antProject.getName();
			if (projectName != null) {
				conf.setProperty("PROJECT_NAME", projectName);
			}
		}
	}

	// ----------------------------------------------------------------------
	/**
	 * This is the main execution entry-point of the task.
	 * 
	 * @throws BuildException
	 *             thrown in case any problem executing the task.
	 * 
	 */
	public final void execute() throws BuildException {
		proc.checkVersion(versionCompatible);
		if (configFilename == null) {
			configFilename = CONFIG_FILE;
			proc.createConfig(configFilename);
			activityLog(false, "Created config file [" + configFilename + "].");
		} else {
			configFilename = makePathAbsolute(configFilename);
			activityLog(false, "Using config file [" + configFilename + "].");
		}

		conf.writeDoxygenConfig(configFilename); // Then we update it.
		activityLog(true, "Executing doxygen ...");
		proc.executeDoxygenConfig(configFilename);
		activityLog(true, "done");
		// No need to delete the config. file at all.
		// Let it be there for the user to know what was used.
	}

	// ======================================================================
	// Ant Task-level Elements.
	// ======================================================================

	// ----------------------------------------------------------------------
	/**
	 * Core Ant task element that controls the output verbosity of this Ant
	 * task. By default, verbosity is minimized to allow for cleaner build logs.
	 * Turn this on, if you need more detailed diagnostic information.
	 * 
	 * @test there is a marked difference in output depending on the value of
	 *       verbose.
	 * 
	 * @param attr
	 *            from build.xml.
	 * 
	 */
	public final void setVerbose(final boolean attr) {
		verbose = attr;
	}

	// ----------------------------------------------------------------------
	/**
	 * Core Ant task element which specifies a path to the Doxygen executable.
	 * By default the application name "doxygen" is used which presumes Doxygen
	 * is on the path.
	 * 
	 * @test Specification of a bogus application name will fail.
	 * 
	 * @param attr
	 *            from build.xml.
	 * 
	 */
	public final void setDoxygenPath(final String attr) {
		proc.setDoxygenPath(attr);
	}

	// ---------------------------------------------------------
	/**
	 * This method supports the task-level element
	 * <code>versionCompatibile</code>. If specified, this value is the minimum
	 * supported version of Doxygen that will be mandated later in this
	 * execution session. If not specified, this Ant task will assume that any
	 * version of Doxygen is sufficient for the needs of the user.
	 * 
	 * @param newVersion
	 *            Sets the version against which compatibility check is to be
	 *            run.
	 * 
	 */
	public final void setVersionCompatible(final String newVersion) {
		versionCompatible = newVersion;
	}

	// ----------------------------------------------------------------------
	/**
	 * Core Ant task element which specifies an non-generated Doxygen base
	 * configuration file. All &lt;doxygen&gt; elements and nested elements that
	 * appear in the build.xml file will take precedence over this base
	 * configuration.
	 * 
	 * The base configuration file will not be updated. Doxygen will be invoked
	 * with an amalgamated configuration file which is derived from either this
	 * configuration file or if not specified, a Doxygen default and then Ant
	 * task flavored almalgamated configuration file.
	 * 
	 * @test If the configuration file is set, then the amalgamated
	 *       configuration file is retained.
	 * 
	 * @param attr
	 *            from build.xml.
	 * 
	 */
	public final void setConfigFilename(final String attr) {
		configFilename = attr;
	}

	// ----------------------------------------------------------------------
	/**
	 * Method completes the JavaBean interface for attribute "verbose".
	 * 
	 * @return a <code>boolean</code> value indicating that the task verbosity
	 *         is high(true) or low(false).
	 * 
	 */
	public final boolean getVerbose() {
		return verbose;
	}

	// ----------------------------------------------------------------------
	/**
	 * This method completes the JavaBean interface for attribute
	 * 'versionCompatible'.
	 * 
	 * @return Returns the <code>String</code> version against which the
	 *         compatibility check is to be run.
	 * 
	 */
	public final String getVersionCompatible() {
		return versionCompatible;
	}

	// ----------------------------------------------------------------------
	/**
	 * This method completes the JavaBean interface for attribute
	 * 'configFilename'.
	 * 
	 * @return a <code>String</code> value containing the name of the
	 *         configuration file to be passed to Doxygen.
	 * 
	 */
	public final String getConfigFilename() {
		return configFilename;
	}

	// ======================================================================
	// Ant Task Nested Elements.
	// ======================================================================

	// ----------------------------------------------------------------------
	/**
	 * This method enables nested property tags. This is the preferred attribute
	 * specification method as it reduces the acute knowledge that the task has
	 * over a given Doxygen implementation. However, non-nested attributes may
	 * still be specified.
	 * 
	 * @note this method is only called by Ant.
	 * 
	 * @return an empty <code>Property</code>.
	 * 
	 */
	public final Property createProperty() {
		return new Property();
	}

	// ----------------------------------------------------------------------
	/**
	 * This method enables nested property tags. This is the preferred attribute
	 * specification method as it reduces the acute knowledge that the task has
	 * over a given Doxygen implementation. However, non-nested attributes may
	 * still be specified.
	 * 
	 * @note this method is only called by Ant.
	 * 
	 * @param p
	 *            is the Ant-configured DoxygenTask.Property to be added as a
	 *            nested element.
	 * 
	 */
	public final void addConfiguredProperty(final DoxygenTask.Property p) {
		conf.setProperty(p.getName(), p.getValue());
	}

	// ======================================================================
	// Utility Methods
	// ======================================================================

	// ----------------------------------------------------------------------
	/**
	 * This method facilitates simplified unit test code. Unit test access is
	 * package-default to avoid confusing the Ant reflection code.
	 * 
	 * @param propertyName
	 *            to be set.
	 * 
	 * @param propertyValue
	 *            to be set.
	 * 
	 */
	final void setProperty(final String propertyName, final String propertyValue) {
		conf.setProperty(propertyName, propertyValue);
	}

	// ----------------------------------------------------------------------
	/**
	 * This method facilitates simplified unit test code. Unit test access is
	 * package-default to avoid confusing the Ant reflection code.
	 * 
	 * @param propertyName
	 *            to be retrieved.
	 * 
	 * @return a <code>DoxygenTask.Property</code> instance that refers to the
	 *         single instance of <code>propertyName</code>. If propertyName
	 *         does not exist, an empty Property will be returned rather than a
	 *         null Property.
	 * 
	 */
	final Property getProperty(final String propertyName) {
		return conf.getProperty(propertyName);
	}

	// ----------------------------------------------------------------------
	/**
	 * This method allows a single point of control over the task output
	 * verbosity. This method was replicated to restore lost diagnostic
	 * functionality until it can be refactored correctly.
	 * 
	 * @test message output is conditional. Some messages are always output,
	 *       while others are only sometimes output.
	 * 
	 * @param terseMessage
	 *            is a flag that indicates the calling code wants this message
	 *            to appear regardless of the current verbosity level.
	 * 
	 * @param theMessage
	 *            to be output, guard condition permitting.
	 * 
	 */
	public static final void activityLog(final boolean terseMessage,
			final String theMessage) {
		if (theMessage != null) {
			if (terseMessage || verbose) {
				System.out.println(theMessage);
			}
		}
	}

	/**
	 * This helper method makes the path of the file absolute if it isn't
	 * absolute already.
	 * 
	 * @param file
	 *            the file where the absolute path (the current ant working
	 *            directory) shall be prepended
	 * @return the file with the absolute path
	 */
	private String makePathAbsolute(final String file) {
		String absolutePath;

		File fileObj = new File(file);
		if (!fileObj.isAbsolute()) {
			absolutePath = getProject().getBaseDir().getAbsolutePath()
					+ File.separatorChar + configFilename;
		} else {
			absolutePath = file;
		}

		return absolutePath;
	}

	// ======================================================================
	// Inner Classes (Nested Element Support)
	// ======================================================================

	// ----------------------------------------------------------------
	/**
	 * This inner class implements a nested Ant XML property container. This
	 * class is used for both nested and non-nested properties.
	 */
	public static class Property {

		/** This is the Doxygen configuration file parameter name. */
		private String key = null;

		/** This is the Doxygen configuration file parameter value. */
		private String val = null;

		// ------------------------------------------------------------
		/**
		 * This accessor method retrieves the current Doxygen parameter name.
		 * 
		 * @return a <code>String</code> value containing the property key name.
		 */
		public final String getName() {
			return key;
		}

		// ------------------------------------------------------------
		/**
		 * This access method retrieves the current Doxygen parameter value.
		 * 
		 * @return a <code>String</code> value containing the property value.
		 * 
		 */
		public final String getValue() {
			return val;
		}

		// ------------------------------------------------------------
		/**
		 * This method sets the Doxygen parameter name. This is the actual
		 * parameter name as seen in the Doxygen configuration file.
		 * 
		 * @param theNewName
		 *            of the property.
		 * 
		 */
		public final void setName(final String theNewName) {
			key = theNewName;
		}

		// ------------------------------------------------------------
		/**
		 * This method sets the value of the Doxygen parameter.
		 * 
		 * @param theNewValue
		 *            of the property.
		 * 
		 */
		public final void setValue(final String theNewValue) {
			if (theNewValue.toLowerCase().equals("true")) {
				val = "YES";
			} else if (theNewValue.toLowerCase().equals("false")) {
				val = "NO";
			} else {
				val = theNewValue;
			}
		}

		// ------------------------------------------------------------
		/**
		 * This method returns a String representation of this instance.
		 * 
		 * @return a <code>String</code> containing identifying information
		 *         about this instance.
		 * 
		 */
		@Override
		public final String toString() {
			return "Property=[" + "key={" + key + "}," + "value={" + val + "}"
					+ "]";
		}
	}
}
