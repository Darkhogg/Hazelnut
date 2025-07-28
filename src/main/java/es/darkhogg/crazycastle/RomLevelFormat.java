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

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Represents the format used by the original BBCC2 game internaly.
 *
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0.1
 */
public final class RomLevelFormat extends LevelFormat {

	/**
	 * The fixed size of a level regardless of dimensions
	 */
	private static final int SIZE_OVERHEAD = (16 * 3 + 1) * 3;

	/**
	 * The singleton for this class
	 */
	private static final RomLevelFormat INSTANCE = new RomLevelFormat();

	/**
	 * Private constructor to ensure singleton
	 */
	private RomLevelFormat() {}

	/**
	 * Returns the singleton of this class
	 *
	 * @return This class singleton
	 */
	public static RomLevelFormat getInstance() {
		return INSTANCE;
	}

	/**
	 * Loads a RomLevel object from the given ByteBuffer in the original ROM
	 * format.
	 *
	 * @param buffer The buffer from which the RomLevel will be read
	 * @return The RomLevel that it represented in the buffer
	 */
	public RomLevel loadRomLevelFromBuffer(ByteBuffer buffer) {
		// Load Theme & Size
		final var theme = Theme.valueOf(buffer.get());
		final var width = ((int)buffer.get() ) & 0xFF;
		final var height = ((int)buffer.get() ) & 0xFF;

		final var lvlDataPos = buffer.position();
		final var lvlEntPos = lvlDataPos + width*height;

		// Load Weapon & Spawn
		buffer.position(lvlEntPos);
		final var weapon = Weapon.valueOf(buffer.get());
		final var spawnX = ((int)buffer.get() ) & 0xFF;
		final var spawnY = ((int)buffer.get() ) & 0xFF;

		final var rl = new RomLevel(
			theme, weapon,
			new IntVector( width, height ),
			new IntVector( spawnX, spawnY )
		);

		// Load Items
		buffer.position(lvlEntPos + 3);
		for (int i = 0; i < 16; i++) {
			int itX = ((int)buffer.get()) & 0xFF;
			int itY = ((int)buffer.get()) & 0xFF;
			byte itVal = buffer.get();

			ItemType itType = ItemType.valueOf(itVal);
			if (itType != null) {
			  rl.getItems().add(new Item( itType, itX, itY ));
      }
		}

		// Load Enemies
		buffer.position(lvlEntPos + 17 * 3);
		for (int i = 0; i < 15; i++) {
			int enX = ((int)buffer.get()) & 0xFF;
			int enY = ((int)buffer.get()) & 0xFF;
			byte enVal = buffer.get();

			EnemyType enType = EnemyType.valueOf(enVal);
			if (enType != null) {
			  rl.getEnemies().add(new Enemy( enType, enX, enY ));
      }
		}

		// Load Door Items
		buffer.position(lvlEntPos + 32 * 3);
		for (int i = 0; i < 16; i++) {
			int itX = ((int)buffer.get()) & 0xFF;
			int itY = ((int)buffer.get()) & 0xFF;
			byte itVal = buffer.get();

			ItemType itType = ItemType.valueOf(itVal);
			if (itType != null) {
			  rl.getDoorItems().add(new Item( itType, itX, itY ));
      }
		}

		// Load Data
		buffer.position(lvlDataPos);
		final var grid = rl.getGrid();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				grid.set(x, y, buffer.get());
			}
		}

		return rl;
	}

	/**
	 * Loads a level to the given buffer using the original ROM format. The
	 * returned level will use some default values for passwords and enemy
	 * group.
	 */
	@Override
	public Level loadLevelFromBuffer(ByteBuffer buffer) {
		return new Level(
			loadRomLevelFromBuffer(buffer),
			EnemyGroup.NORMAL_A,
			null
		);
	}

	/**
	 * Saves a RomLevel to the given buffer using the original ROM format.
	 *
	 * @param level The level to be saved
	 * @param buffer The buffer in which the level will be saved
	 * @param sp Whether this is the special level with only 15 DI's or not
	 * @return The actual number of bytes written into the level
	 */
	public int saveRomLevelToBuffer(RomLevel level, ByteBuffer buffer, boolean sp) {
		int pos = buffer.position();
		int entPos = 6 + level.getSize().getX()*level.getSize().getY() + pos;
		int n;
		byte[] threeZeros = {(byte)0, (byte)0, (byte)0};

		// Theme & Size
		int bytes = 3;
		buffer.put(level.getTheme().getValue());
		buffer.put((byte)level.getSize().getX());
		buffer.put((byte)level.getSize().getY());

		// Data
		bytes += level.getSize().getX() * level.getSize().getY();
		byte[][] data = level.getGrid().getData();
		for (int j = 0; j < level.getSize().getY(); j++) {
			for (int i = 0; i < level.getSize().getX(); i++) {
				buffer.put(data[i][j]);
			}
		}

		// Weapon & Spawn
		bytes += 3;
		buffer.put(level.getWeapon().getValue());
		buffer.put((byte)level.getSpawn().getX());
		buffer.put((byte)level.getSpawn().getY());

		// Items
		buffer.position(entPos);
		n = 0;
		for ( Item item : level.getItems() ) {
			bytes += 3;
			buffer.put((byte)item.getX());
			buffer.put((byte)item.getY());
			buffer.put(item.getType().getValue());
			n++;
		}
		while ( n < level.getItems().maxSize() ) {
			bytes += 3;
			buffer.put(threeZeros);
			n++;
		}

		// Enemies
		buffer.position(entPos + 16 * 3);
		n = 0;
		for ( Enemy enemy : level.getEnemies() ) {
			bytes += 3;
			buffer.put((byte)enemy.getX());
			buffer.put((byte)enemy.getY());
			buffer.put(enemy.getType().getValue());
		}
		while ( n < level.getEnemies().maxSize() ) {
			bytes += 3;
			buffer.put(threeZeros);
			n++;
		}

		// Door Items
		buffer.position(entPos + 31 * 3);
		n = 0;
		int i = 0;
		for ( Item item : level.getDoorItems() ) {
			if ( i < 15 || !sp ) {
				bytes += 3;
				buffer.put((byte)item.getX());
				buffer.put((byte)item.getY());
				buffer.put(item.getType().getValue());
			}
			i++;
			n++;
		}
		while ( n < level.getDoorItems().maxSize() && ( n < 15 || !sp ) ) {
			bytes += 3;
			buffer.put(threeZeros);
			n++;
		}

		return bytes;
	}

	/**
	 * Saves a level to the given buffer using the original ROM format. The
	 * password and enemy group of this level are completely ignored.
	 */
	@Override
	public int saveLevelToBuffer(Level level, ByteBuffer buffer) {
		return saveRomLevelToBuffer(level.getRomLevel(), buffer, false);
	}

	/**
	 * Saves a level to a file using the original ROM format. The password and
	 * enemy group of this level are completely ignored.
	 */
	@Override
	public int saveLevelToFile(Level level, File file)
	throws IOException {
		return saveLevelToFile(level, file, calcLevelSize(level));
	}

	/**
	 * Calculates the byte size of the given level.
	 *
	 * @param level the level for which to calculate the size
	 * @return the size of the given level
	 */
	public int calcLevelSize(Level level) {
		final var size = level.getRomLevel().getSize();
		return (size.getX() * size.getY()) + SIZE_OVERHEAD;
	}

	/**
	 * Get the allowed overlap between the given levels, with <tt>before</tt> representing the level that will have its
	 * contents overlapped by the firts bytes of <tt>after</tt>.
	 */
	public int getOverlapWith(Level before, Level after, ComboInfoMap comboInfo) {
		final var buffer = ByteBuffer.allocate(calcLevelSize(after));
		saveLevelToBuffer(after, buffer);
		buffer.position(0);

		final var maxOverlap = Math.max(0, 16 - before.getRomLevel().getDoorItems().size() - 1);

		final var beforeSize = before.getRomLevel().getSize();
		final var beforeData = before.getRomLevel().getGrid().getData();

		int overlap;
		for (overlap = 0; overlap < maxOverlap; overlap++) {
			final var diX = (int)buffer.get() & 0xFF;
			final var diY = (int)buffer.get() & 0xFF;
			final var diType = buffer.get();
			if (diType == 0 || diX >= beforeSize.getX() || diY + 2 >= beforeSize.getY()) {
				continue;
			}

			final var combo = beforeData[diX][diY + 2];
			final var comboType = comboInfo.getComboType(combo);
			if (comboType != ComboType.DOOR) {
				continue;
			}

			break;
		}

		return overlap * 3;
	}
}
