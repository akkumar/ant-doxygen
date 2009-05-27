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
// $Header: /cvsroot/ant-doxygen/ant_task/src/org/doxygen/tools/DoxygenVersion.java,v 1.2.4.6 2004/02/01 19:00:41 jfrotz Exp $
package org.doxygen.tools;

/**
 * This class provides Doxygen version comparison support.
 * 
 * @author <a href="mailto:akkumar@users.sourceforge.net">Karthik Kumar</a>
 * @version $Revision: 1.2.4.6 $
 * 
 * @since Ant-Doxygen 1.0
 * 
 */
public class DoxygenVersion implements Comparable<DoxygenVersion> {

	// ----------------------------------------------------------------------
	/**
	 * This constructor parses each of the version components for subsequent
	 * version-version comparison. This class solves the normal
	 * <code>String</code> v. <code>int</code> collation / comparison issues at
	 * the version string level.
	 * 
	 * @param releaseMajorMinor
	 *            string obtained from "Doxygen --version".
	 * 
	 * @param orderOfMagnitude
	 *            or number of components in the version string.
	 * 
	 */
	public DoxygenVersion(final String releaseMajorMinor,
			final int orderOfMagnitude) {
		String version = releaseMajorMinor;
		if (version.endsWith("+")) {
			upwards = true;
			int len = version.length();
			version = version.substring(0, len - 1);
		}
		String[] st = version.split("\\.");
		if (st.length != orderOfMagnitude) { 
			throw new IllegalArgumentException(String.format("doxygen --version (%s) . Number of tokens retrieved %d does not match expected %d tokens", version, st.length, orderOfMagnitude));
		}
		versions = new int[orderOfMagnitude];
		for (int i = 0; i < orderOfMagnitude; i++) {
			versions[i] = Integer.parseInt(st[i]);
		}
	}

	// ----------------------------------------------------------------------
	/**
	 * This constructor assumes a canonical Doxygen version string form. That
	 * being Release.Major.Minor.
	 * 
	 * @param releaseMajorMinor
	 *            version string obtained from "Doxygen --version". The order of
	 *            magnitude is assumed to be DEFAULT_MAGNITUDE.
	 * 
	 */
	public DoxygenVersion(final String releaseMajorMinor) {
		this(releaseMajorMinor, DEFAULT_MAGNITUDE);
	}

	// ----------------------------------------------------------------------
	/**
	 * This method implements the compareTo() method from the Comparable
	 * interface.
	 * 
	 * @param dv
	 *            instance to be compared against this instance.
	 * 
	 * @return an <code>int</code> in the following range:
	 *         <ul>
	 *         <li>(1) - if this version is greater than obj version
	 *         <li>(0) - if both versions are equal are equal.
	 *         <li>(-1) - if this version is less than obj version, or if obj is
	 *         not an instance of org.doxygen.tools.DoxygenVersion.
	 *         </ul>
	 * 
	 */
	public final int compareTo(final DoxygenVersion dv) {
		if (!this.upwards && this.versions.length != dv.versions.length) {
			return -1;
		}
		for (int i = 0; (i < versions.length); i++) {
			if (i <= this.versions.length && i <= dv.versions.length) {
				if (this.versions[i] > dv.versions[i]) {
					return 1;
				} else if (this.versions[i] == dv.versions[i]) {
					continue;
				} else {
					return -1;
				}
			}
		}
		return 0; // Equal
	}

	// ----------------------------------------------------------------------
	/**
	 * To check if the given version is compatible with the current version.
	 * 
	 * @param version
	 *            Version against which compatibility is to be checked.
	 * 
	 * @return a <code>boolean</code> value if the passed version compares
	 *         favorably with current version (<code>true</code>). Otherwise a
	 *         problem is indicated by a <code>false</code> value.
	 * 
	 */
	public final boolean isCompatible(final String version) {
		DoxygenVersion compatVersion = new DoxygenVersion(version);
		System.out.println("DoxygenVersion.isCompatible([" + version
				+ "]) => [" + compatVersion.versions.length + "]");
		if (compatVersion.upwards) {
			return (this.compareTo(compatVersion) >= 0);
		}
		return (this.compareTo(compatVersion) == 0);
	}

	// ----------------------------------------------------------------------
	/**
	 * This method provides a standard diagnostic method toString().
	 * 
	 * @return Stringified form of the values contained by this instance.
	 * 
	 */
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i : versions) {
			sb.append(i + ".");
		}
		return sb.toString();
	}

	/** This contains the components of the Doxygen version string. */
	private int[] versions;

	/** This is the default number of version components. */
	private static final int DEFAULT_MAGNITUDE = 3;

	/**
	 * This flag determines if this version represents the class of versions
	 * upwards from here.
	 */
	private boolean upwards = false;

}
