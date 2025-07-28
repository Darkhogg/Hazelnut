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

public enum EnemyGroup {
	NORMAL_A( 0x03 ),
	NORMAL_B( 0x04 ),
	NORMAL_C( 0x05 ),
	BOSS( 0x06 );

	private final static Map<Byte,EnemyGroup> byteMap;
	static {
		Map<Byte,EnemyGroup> bm = new HashMap<Byte, EnemyGroup>();
		for ( EnemyGroup eg : values() ) {
			bm.put(eg.getValue(), eg);
		}
		byteMap = Collections.unmodifiableMap(bm);
	}

	private final byte value;

	private EnemyGroup(int value) {
		this.value = (byte)value;
	}

	public byte getValue() {
		return value;
	}

	public int getIndex() {
		return value - 3;
	}

	public static EnemyGroup valueOf(byte bt) {
		return byteMap.get(Byte.valueOf(bt));
	}
}
