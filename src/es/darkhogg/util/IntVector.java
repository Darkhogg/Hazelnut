package es.darkhogg.util;

/**
 * Represents a two-dimensional vector with integer components X and Y.
 * <p>
 * Because this class has a not-general purpose, nothing but the essential
 * is implemented
 * 
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public final class IntVector {
	
	private final int x;
	private final int y;
	
	/**
	 * Constructs a new vector with the given components.
	 * 
	 * @param x The horizontal component of the vector
	 * @param y The vertical component of the vector
	 */
	public IntVector ( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the horizontal component of this vector
	 * @return The horizontal component of this vector
	 */
	public int getX () {
		return x;
	}
	
	/**
	 * Returns the vertical component of this vector
	 * @return The vertical component of this vector
	 */
	public int getY () {
		return y;
	}
	
	@Override
	public boolean equals ( Object obj ) {
		if ( !(obj instanceof IntVector) ) {
			return false;
		}
		
		IntVector iv = (IntVector) obj;
		
		return iv.x == x
			&& iv.y == y;
	}
	
	@Override
	public int hashCode () {
		return 7 * x * y;
	}

	@Override
	public String toString () {
		return IntVector.class.getName() + "{" + x + "," + y + "}";
	}
}
