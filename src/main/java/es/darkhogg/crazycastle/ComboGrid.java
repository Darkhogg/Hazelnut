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

import es.darkhogg.util.IntVector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ComboGrid {
	private IntVector size;
	private IntVector offset = new IntVector(Integer.MAX_VALUE / 2, Integer.MAX_VALUE / 2);

	private final static byte DEFAULT_COMBO = (byte)0xD3;

	private final static int CHUNK_SIZE = 6;
	private final Map<IntVector,byte[]> chunks = new HashMap<>();


	public ComboGrid() {
		this(IntVector.ZERO);
	}

	public ComboGrid(IntVector size) {
		this.size = size;
	}

	public ComboGrid(ComboGrid other) {
		this(other.size);

		offset = other.offset;

		for (final var entry : other.chunks.entrySet()) {
			final var val = entry.getValue();
			chunks.put(entry.getKey(), Arrays.copyOf(val, val.length));
		}
	}

	public IntVector getSize() {
		return size;
	}

	public void setSize(IntVector newSize) {
		size = newSize;
	}

	private IntVector chunkKey(int x, int y) {
		return new IntVector((x + offset.getX()) / CHUNK_SIZE, (y + offset.getY()) / CHUNK_SIZE);
	}

	private int chunkIndex(int x, int y) {
		return ((x + offset.getX()) % CHUNK_SIZE) + (((y + offset.getY()) % CHUNK_SIZE) * CHUNK_SIZE);
	}

	public byte get(int x, int y) {
		final var chunkKey = chunkKey(x, y);
		final var chunkIdx = chunkIndex(x, y);

		final var chunk = chunks.get(chunkKey);
		return chunk == null ? DEFAULT_COMBO : chunk[chunkIdx];
	}

	public byte get(IntVector pos) {
		return get(pos.getX(), pos.getY());
	}

	public void set(int x, int y, byte value) {
		final var chunkKey = chunkKey(x, y);
		final var chunkIdx = chunkIndex(x, y);

		byte[] chunk;
		if (chunks.containsKey(chunkKey)) {
			chunk = chunks.get(chunkKey);
		} else {
			chunk = new byte[CHUNK_SIZE * CHUNK_SIZE];
			Arrays.fill(chunk, DEFAULT_COMBO);
			chunks.put(chunkKey, chunk);
		}

		chunk[chunkIdx] = value;
	}

	public void set(IntVector pos, byte value) {
		set(pos.getX(), pos.getY(), value);
	}

	public void clear() {
		chunks.clear();
	}

	public byte[][] getData() {
		final var w = size.getX();
		final var h = size.getY();

		final var ret = new byte[ w ][ h ];

		for (var x = 0; x < w; x++) {
			for (var y = 0; y < h; y++) {
				ret[x][y] = get(x, y);
			}
		}

		return ret;
	}
	public void setData(byte[][] data) {
		final var w = data.length;
		if (size.getX() != w) {
			throw new IllegalArgumentException("data size doesn't match: width (" + w + ")");
		}

		for (var x = 0; x < w; x++) {
			final var h = data[x].length;
			if (size.getY() != h) {
				throw new IllegalArgumentException("data size doesn't match: height at " + x + " (" + h + ")");
			}
			for (var y = 0; y < h; y++) {
				set(x, y, data[x][y]);
			}
		}
	}

	public void move(IntVector move) {
		offset = offset.sub(move);
	}

	public void prune() {
		final var data = getData();
		clear();
		setData(data);
	}
}
