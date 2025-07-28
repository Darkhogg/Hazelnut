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

public enum EnemyType implements EntityType {
	//VOID			( 0x00 ),
	FOGHORN( 0x01, false ),
	FOGHORN_DARK( 0x02, true ),
	BEAKY( 0x03, false ),
	BEAKY_DARK( 0x04, true ),
	DAFFY( 0x05, false ),
	FLAME( 0x06, false ),
	HAZEL( 0x07, false );

	private final static Map<Byte,EnemyType> byteMap;
	static {
		Map<Byte,EnemyType> bm = new HashMap<Byte, EnemyType>();
		for ( EnemyType eg : values() ) {
			bm.put(eg.getValue(), eg);
		}
		byteMap = Collections.unmodifiableMap(bm);
	}

	private final byte value;
	private final boolean dark;

	private EnemyType(int value, boolean dark) {
		this.value = (byte)value;
		this.dark = dark;
	}

	@Override
	public byte getValue() {
		return value;
	}

	public int getIndex() {
		return value - 1;
	}

	public boolean isDark() {
		return dark;
	}

	public static EnemyType valueOf(byte bt) {
		return byteMap.get(Byte.valueOf(bt));
	}

}
