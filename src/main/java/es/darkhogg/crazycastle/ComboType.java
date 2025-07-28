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

import java.util.HashMap;
import java.util.Map;

public enum ComboType {
	STANDARD(0x00),

	DOOR(0x10),
	FINAL(0x11),

	FLYUP(0x19),
	FLYUP_SIDE(0x2a),

	WARP(0x1A),

	STAIRS_BTM(0x27),
	STAIRS_LFT(0x01),
	STAIRS_RGT(0x02),

	LADDER_TOP(0x69),
	LADDER(0x43),

	ELEV_UPLFT(0x56),
	ELEV_UPRGT(0x57),
	ELEV_DNLFT(0x23),
	ELEV_DNRGT(0x22),

	ROPE(0x0C),
	ROPE_FLR(0x26),

	BLK_HAMMER(0x0D),
	BLK_PICK(0x04),

	SLOW(0x1B),

	PIPE_ENTUP(0x54),
	PIPE_ENTDN(0x55),

	PIPE_HOR(0x05),
	PIPE_HORWALL(0x25),
	PIPE_VER(0x46),
	PIPE_VERFLR(0x58),
	PIPE_CROSS(0x4B),
	PIPE_UPLFT(0x4A),
	PIPE_UPRGT(0x49),
	PIPE_DNLFT(0x48),
	PIPE_DNRGT(0x47);

	private final static Map<Byte,ComboType> typeValues;
	static {
		typeValues = new HashMap<Byte, ComboType>();

		for ( ComboType ct : ComboType.values() ) {
			typeValues.put(Byte.valueOf((byte)ct.value), ct);
		}
	}

	private final byte value;

	/**
	 * Constructs a given combo type that is assigned to the given values
	 * @param value Type
	 */
	private ComboType(int value) {
		this.value = (byte)(value & 0x7F);
	}

	public byte getValue() {
		return value;
	}

	public static ComboType valueOf(byte value) {
		return typeValues.get((byte)(value & 0x7F));
	}

}
