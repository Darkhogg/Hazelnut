/**
 * This file is part of Hazelnut.
 *
 * Hazelnut is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hazelnut is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hazelnut.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.darkhogg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a version with three integer components: <i>Major</i>,
 * <i>Minor</> and <i>Patch</i> numbers.
 * <p>
 * This class is <i>immutable</i>
 *
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public final class Version implements Comparable<Version> {

	/**
	 * Major version number
	 */
	private final int major;

	/**
	 * Minor version number
	 */
	private final int minor;

	/**
	 * Patch number
	 */
	private final int patch;

	/**
	 * Constructs a version using the given <i>major</i>, <i>minor</i> and
	 * <i>patch</i> numbers.
	 *
	 * @param major The major version number
	 * @param minor The minor version number
	 * @param patch The patch number
	 */
	public Version(int major, int minor, int patch) {
		if ( major < 0 || minor < 0 || patch < 0) {
			throw new IllegalArgumentException();
		}
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	/**
	 * Constructs a version using the given <i>major</i> and <i>minor</i>
	 * numbers and zero as the <i>revision</i> and <i>build</i> numbers.
	 *
	 * @param major The major version number
	 * @param minor The minor version number
	 */
	public Version(int major, int minor) {
		this(major, minor, 0);
	}

	/**
	 * Constructs a version using the given <i>major</i> number and zero as the
	 * and <i>minor</i>, <i>revision</i> and <i>build</i> numbers.
	 *
	 * @param major The major version number
	 */
	public Version(int major) {
		this(major, 0, 0);
	}

	/**
	 * @return The major version number of this version
	 */
	public int getMajor() {
		return major;
	}

	/**
	 * @return The minor version number of this version
	 */
	public int getMinor() {
		return minor;
	}

	/**
	 * @return The revision number of this version
	 */
	public int getPatch() {
		return patch;
	}

	/**
	 * Compares this version to another using the following criteria:
	 * <p><ul>
	 * <li>Two versions are <i>equals</i> if all the numbers are the same.
	 * <li>A version <i>A</i> is <i>lower than</i> another version <i>B</i> if
	 * one of the following conditions is <tt>true</tt>:
	 *   <ul>
	 *   <li> <tt>A.major &lt; B.major</tt>
	 *   <li> <tt>A.major == B.major && A.minor &lt; B.minor</tt>
	 *   <li> <tt>A.major == B.major && A.minor == B.minor && A.revision &lt;
	 *        B.revision</tt>
	 *   </ul>
	 * <li>A version <i>A</i> is <i>greater than</i> another version <i>B</i>
	 * if <i>A</i> is not <i>equals</i> or <i>lower than</i> <i>B</i>.
	 * </ul>
	 * <p>
	 * The natural order imposed by this method <i>is</i> consistent with equals.
	 */
	@Override
	public int compareTo(Version ver) {
		int value = major - ver.major;
		if ( value == 0 ) {
			value = minor - ver.minor;
			if ( value == 0 ) {
				value = patch - ver.patch;
			}
		}
		return value;
	}

	/**
	 * Compares this version against another one for equality.
	 * <p>
	 * A <tt>Version</tt> is equals only to another <tt>Version</tt> if all
	 * their numbers are the same.
	 */
	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof Version) ) {
			return false;
		}

		Version ver = (Version)obj;
		return ver.major == major
			&& ver.minor == minor
			&& ver.patch == patch;
	}

	/**
	 * Returns a hash code for this object, calculated using the four numbers
	 * in this version object
	 */
	@Override
	public int hashCode() {
		return ((((major*31)+minor)*31)+patch)*31;
	}

	/**
	 * Converts this version to a string that represents it and is
	 * human-readable. The <tt>String</tt> returned by this object is always of
	 * the form <tt>"M.m.p"</tt>.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(major);
		sb.append('.');
		sb.append(minor);
		sb.append('.');
		sb.append(patch);

		return sb.toString();
	}

	/**
	 * Returns a Version object which represents the same version as the
	 * String object given.
	 * <p>
	 * For any <tt>Version</tt> <i>V</i>, the expression
	 * <tt>V.equals(Version.valueOf(V.toString()))</tt> is always
	 * <tt>true</tt>.
	 *
	 * @param str The <tt>String</tt> to be converted into a <tt>Version</tt>
	 *            object
	 * @return A <tt>Version</tt> object representing the version written in
	 *         <i>str</i>, or <tt>null</tt> if the argument doesn't represent
	 *         any <tt>Version</tt>.
	 */
	public static Version valueOf(String str) {
		Pattern threePattern = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)$");
		Pattern twoPattern = Pattern.compile("^(\\d+)\\.(\\d+)$");
		Pattern onePattern = Pattern.compile("^(\\d+)$");

		Matcher threeMatcher = threePattern.matcher(str);
		Matcher twoMatcher = twoPattern.matcher(str);
		Matcher oneMatcher = onePattern.matcher(str);

		Version ver = null;
		if ( threeMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt(threeMatcher.group(1)),
				Integer.parseInt(threeMatcher.group(2)),
				Integer.parseInt(threeMatcher.group(3))
			);
		} else if ( twoMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt(twoMatcher.group(1)),
				Integer.parseInt(twoMatcher.group(2))
			);
		} else if ( oneMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt(oneMatcher.group(1))
			);
		}

		return ver;
	}

}
