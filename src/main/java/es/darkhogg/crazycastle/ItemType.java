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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ItemType implements EntityType {
	//VOID	( 0x00 ),
	BOW( 0x01 ),
	CLOCK( 0x02 ),
	BOMB( 0x03 ),
	PICKAXE( 0x04 ),
	POTION( 0x05 ),
	LIFE( 0x06 ),
	HAMMER( 0x07 ),
	CHEST( 0x08 ),
	SHIELD( 0x09 ),
	BOLT( 0x0A ),
	KEY( 0x0B ),
	WEIGHT( 0x0C );

	private final static Map<Byte,ItemType> byteMap;
	static {
		Map<Byte,ItemType> bm = new HashMap<Byte, ItemType>();
		for ( ItemType eg : values() ) {
			bm.put(eg.getValue(), eg);
		}
		byteMap = Collections.unmodifiableMap(bm);
	}

	private final byte value;

	private ItemType(int value) {
		this.value = (byte)value;
	}

	@Override
	public byte getValue() {
		return value;
	}

	public int getIndex() {
		return value - 1;
	}

	public static ItemType valueOf(byte bt) {
		return byteMap.get(Byte.valueOf(bt));
	}
}
