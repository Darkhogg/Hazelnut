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
	public static final IntVector ZERO = new IntVector(0, 0);

	public static final IntVector UNIT_LEFT = new IntVector(-1, 0);
	public static final IntVector UNIT_RIGHT = new IntVector(1, 0);
	public static final IntVector UNIT_DOWN = new IntVector(0, 1);
	public static final IntVector UNIT_UP = new IntVector(0, -1);

	private final int x;
	private final int y;

	/**
	 * Constructs a new vector with the given components.
	 *
	 * @param x The horizontal component of the vector
	 * @param y The vertical component of the vector
	 */
	public IntVector(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the horizontal component of this vector
	 * @return The horizontal component of this vector
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the vertical component of this vector
	 * @return The vertical component of this vector
	 */
	public int getY() {
		return y;
	}

	public IntVector add(IntVector other) {
		return new IntVector(x + other.x, y + other.y);
	}

	public IntVector sub(IntVector other) {
		return new IntVector(x - other.x, y - other.y);
	}

	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof IntVector) ) {
			return false;
		}

		IntVector iv = (IntVector)obj;

		return iv.x == x
			&& iv.y == y;
	}

	@Override
	public int hashCode() {
		return 7 * x * y;
	}

	@Override
	public String toString() {
		return IntVector.class.getSimpleName() + "[" + x + "," + y + "]";
	}
}
