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
package es.darkhogg.gameboy;

/**
 * Represents the transformation a sprite can do over a tile. This class is
 * meant to be used within a set, to select zero or more transforms.
 *
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public enum TileTransform {
	/**
	 * Performs an horizontal flip
	 */
	HORIZONTAL_FLIP,

	/**
	 * Performs a vertical flip
	 */
	VERTICAL_FLIP;
}
