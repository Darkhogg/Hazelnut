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
package es.darkhogg.crazycastle;

public enum Weapon {
	ARROWS( 0 ), BOMBS( 1 );

	private final byte value;

	private Weapon(int value) {
		this.value = (byte)value;
	}

	public byte getValue() {
		return value;
	}

	public static Weapon valueOf(int value) {
		if ( value == 0 ) {
			return ARROWS;
		}

		return BOMBS;
	}
}
