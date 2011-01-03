package es.darkhogg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a version with four integer components: <i>Major</i>,
 * <i>Minor</>, <i>Revision</i> and <i>Build</i> numbers.
 * <p>
 * TODO Document this a bit more
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
	 * Revision number
	 */
	private final int revision;
	
	/**
	 * Build number
	 */
	private final int build;
	
	/**
	 * Constructs a version using the given <i>major</i>, <i>minor</i>,
	 * <i>revision</i> and <i>build</i> numbers.
	 * 
	 * @param major The major version number
	 * @param minor The minor version number
	 * @param revision The revision number
	 * @param build The build number
	 */
	public Version ( int major, int minor, int revision, int build ) {
		if ( major < 0 || minor < 0 || revision < 0 || build < 0 ) {
			throw new IllegalArgumentException();
		}
		this.major = major;
		this.minor = minor;
		this.revision = revision;
		this.build = build;
	}
	
	/**
	 * Constructs a version using the given <i>major</i>, <i>minor</i> and
	 * <i>revision</i> numbers and zero as the <i>build</i> number.
	 * 
	 * @param major The major version number
	 * @param minor The minor version number
	 * @param revision The revision number
	 */
	public Version ( int major, int minor, int revision ) {
		this( major, minor, revision, 0 );
	}
	
	/**
	 * Constructs a version using the given <i>major</i> and <i>minor</i>
	 * numbers and zero as the <i>revision</i> and <i>build</i> numbers.
	 * 
	 * @param major The major version number
	 * @param minor The minor version number
	 */
	public Version ( int major, int minor ) {
		this( major, minor, 0, 0 );
	}
	
	/**
	 * Constructs a version using the given <i>major</i> number and zero as the
	 * and <i>minor</i>, <i>revision</i> and <i>build</i> numbers.
	 * 
	 * @param major The major version number
	 */
	public Version ( int major ) {
		this( major, 0, 0, 0 );
	}
	
	/**
	 * @return The major version number of this version
	 */
	public int getMajor () {
		return major;
	}
	
	/**
	 * @return The minor version number of this version
	 */
	public int getMinor () {
		return minor;
	}
	
	/**
	 * @return The revision number of this version
	 */
	public int getRevision() {
		return revision;
	}
	
	/**
	 * @return The build of this version
	 */
	public int getBuild () {
		return build;
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
	 *   <li> <tt>A.major == B.major && A.minor == B.minor && A.revision ==
	 *        B.revision && A.build &lt; B.build</tt>
	 *   </ul>
	 * <li>A version <i>A</i> is <i>greater than</i> another version <i>B</i>
	 * if <i>A</i> is not <i>equals</i> or <i>lower than</i> <i>B</i>.
	 * </ul>
	 * <p>
	 * The natural order imposed by this method <i>is</i> consistent with equals.
	 */
	@Override
	public int compareTo ( Version ver ) {
		int value = major - ver.major;
		if ( value == 0 ) {
			value = minor - ver.minor;
			if ( value == 0 ) {
				value = revision - ver.revision;
				if ( value == 0 ) {
					value = build - ver.build;
				}
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
	public boolean equals ( Object obj ) {
		if ( !( obj instanceof Version) ) {
			return false;
		}
		
		Version ver = (Version) obj;
		return ver.major == major
			&& ver.minor == minor
			&& ver.revision == revision
			&& ver.build == build;
	}
	
	/**
	 * Returns a hash code for this object, calculated using the four numbers
	 * in this version object
	 */
	@Override
	public int hashCode () {
		return ((((((major*31)+minor)*31)+revision)*31)+build)*31;
	}

	/**
	 * Converts this version to a string that represents it and is
	 * human-readable. The <tt>String</tt> returned by this object is:
	 * <ul>
	 * <li>If <i>build</i> is not zero, a string of the form:
	 * <i>M</i>.<i>m</i>.<i>r</i>_<i>b</i>
	 * <li>If <i>build</i> is zero and <i>revision</i> is not, a string of the
	 * form: <i>M</i>.<i>m</i>.<i>r</i>
	 * <li>In <i>build</i> and <i>revision</i> are both zero, a string of the
	 * form: <i>M</i>.<i>m</i>
	 * </ul>
	 * Where <i>M</i>, <i>m</i>, <i>r</i> and <i>b</i> are, respectively, the
	 * <i>major</i> number, the <i>minor</i> number, the <i>revision</i> number
	 * and the <i>build</i>.
	 */
	@Override
	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append( major );
		sb.append( '.' );
		sb.append( minor );
		if ( revision > 0 || build > 0 ) {
			sb.append( '.' );
			sb.append( revision );
			if ( build > 0 ) {
				sb.append( '_' );
				sb.append( build );
			}
		}

		return sb.toString();
	}
	
	/**
	 * Returns a Version object which represents the same version than the
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
	public static Version valueOf ( String str ) {
		Pattern fourPattern = Pattern.compile( "^(\\d+)\\.(\\d+)\\.(\\d+)_(\\d+)$" );
		Pattern threePattern = Pattern.compile( "^(\\d+)\\.(\\d+)\\.(\\d+)$" );
		Pattern twoPattern = Pattern.compile( "^(\\d+)\\.(\\d+)$" );
		Pattern onePattern = Pattern.compile( "^(\\d+)$" );

		Matcher fourMatcher = fourPattern.matcher( str );
		Matcher threeMatcher = threePattern.matcher( str );
		Matcher twoMatcher = twoPattern.matcher( str );
		Matcher oneMatcher = onePattern.matcher( str );
		
		Version ver = null;
		if ( fourMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt( fourMatcher.group( 1 ) ),
				Integer.parseInt( fourMatcher.group( 2 ) ),
				Integer.parseInt( fourMatcher.group( 3 ) ),
				Integer.parseInt( fourMatcher.group( 4 ) )
			);
		} else if ( threeMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt( threeMatcher.group( 1 ) ),
				Integer.parseInt( threeMatcher.group( 2 ) ),
				Integer.parseInt( threeMatcher.group( 3 ) )
			);
		} else if ( twoMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt( twoMatcher.group( 1 ) ),
				Integer.parseInt( twoMatcher.group( 2 ) )
			);
		} else if ( oneMatcher.matches() ) {
			ver = new Version(
				Integer.parseInt( oneMatcher.group( 1 ) )
			);
		}
		
		return ver;
	}

}
