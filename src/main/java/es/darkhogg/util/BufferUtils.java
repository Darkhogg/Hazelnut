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

import org.apache.commons.lang3.NotImplementedException;

import java.nio.ByteBuffer;

public final class BufferUtils {
	private BufferUtils() {
		throw new NotImplementedException();
	}

	public static int getShortPointer(ByteBuffer buffer, int address) {
		return getShortPointer(buffer, address, 0);
	}

	public static int getShortPointer(ByteBuffer buffer, int address, int offset) {
		final var sh = buffer.getShort(address + offset * 2);
		return ((int)sh) & 0xFFFF;
	}
}
