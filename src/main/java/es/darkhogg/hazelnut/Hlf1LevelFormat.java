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
package es.darkhogg.hazelnut;

import es.darkhogg.crazycastle.Enemy;
import es.darkhogg.crazycastle.EnemyGroup;
import es.darkhogg.crazycastle.EnemyType;
import es.darkhogg.crazycastle.Item;
import es.darkhogg.crazycastle.ItemType;
import es.darkhogg.crazycastle.Level;
import es.darkhogg.crazycastle.LevelFormat;
import es.darkhogg.crazycastle.RomLevel;
import es.darkhogg.crazycastle.Theme;
import es.darkhogg.crazycastle.Weapon;
import es.darkhogg.util.IntVector;

import java.nio.ByteBuffer;
import java.util.Collection;

public final class Hlf1LevelFormat extends LevelFormat {

	private static Hlf1LevelFormat instance;

	private Hlf1LevelFormat() {}

	public static Hlf1LevelFormat getInstance() {
		if ( instance == null ) {
			instance = new Hlf1LevelFormat();
		}
		return instance;
	}

	@Override
	public Level loadLevelFromBuffer(ByteBuffer buffer) {
		// Ignore first 4 bytes (version)
		buffer.getInt();

		// Get the theme
		Theme theme = Theme.valueOf(buffer.get());

		// Get the size
		IntVector size = new IntVector( buffer.getInt(), buffer.getInt() );

		// Get the spawn
		IntVector spawn = new IntVector( buffer.getInt(), buffer.getInt() );

		// Get the weapon
		Weapon weapon = Weapon.valueOf(buffer.get());

		// Get the enemy group
		EnemyGroup enemyGroup = EnemyGroup.valueOf(buffer.get());

		// Get the password
		String password = null;
		if ( buffer.get() != 0 ) {
			byte[] bytes = new byte[ 4 ];
			buffer.get(bytes);
			password = new String( bytes );
		}

		// Create the rom level
		RomLevel romLevel = new RomLevel( theme, weapon, size, spawn );
		byte[][] data = romLevel.getGrid().getData();

		// Get the data!
		int width = romLevel.getSize().getX();
		int height = romLevel.getSize().getY();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				data[i][j] = buffer.get();
			}
		}

		// Get items
		Collection<Item> items = romLevel.getItems();
		int numItems = buffer.getInt();
		for (int i = 0; i < numItems; i++) {
			items.add(new Item(
				ItemType.valueOf(buffer.get()),
				new IntVector( buffer.getInt(), buffer.getInt() )
			));
		}

		// Get door items
		Collection<Item> doorItems = romLevel.getDoorItems();
		int numDoorItems = buffer.getInt();
		for (int i = 0; i < numDoorItems; i++) {
			doorItems.add(new Item(
				ItemType.valueOf(buffer.get()),
				new IntVector( buffer.getInt(), buffer.getInt() )
			));
		}

		// Get enemies
		Collection<Enemy> enemies = romLevel.getEnemies();
		int numEnemies = buffer.getInt();
		for (int i = 0; i < numEnemies; i++) {
			enemies.add(new Enemy(
				EnemyType.valueOf(buffer.get()),
				new IntVector( buffer.getInt(), buffer.getInt() )
			));
		}

		// Return the level
		return new Level( romLevel, enemyGroup, password );
	}

	@Override
	public int saveLevelToBuffer(Level level, ByteBuffer buffer) {
		int bytes = 0;
		RomLevel romLevel = level.getRomLevel();

		// Version
		buffer.putInt(1);
		bytes += 4;

		// Theme
		buffer.put(romLevel.getTheme().getValue());
		bytes += 1;

		// Size
		buffer.putInt(romLevel.getSize().getX());
		buffer.putInt(romLevel.getSize().getY());
		bytes += 8;

		// Spawn
		buffer.putInt(romLevel.getSpawn().getX());
		buffer.putInt(romLevel.getSpawn().getY());
		bytes += 8;

		// Weapon
		buffer.put(romLevel.getWeapon().getValue());
		bytes += 1;

		// Enemy Group
		buffer.put(level.getEnemyGroup().getValue());
		bytes += 1;

		// Password
		buffer.put((byte)( level.getPassword() != null ? 1 : 0 ));
		buffer.put(level.getPasswordNotNull().getBytes());
		bytes += 5;

		// Data
		int width = romLevel.getSize().getX();
		int height = romLevel.getSize().getY();
		byte[][] data = romLevel.getGrid().getData();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				buffer.put(data[i][j]);
				bytes += 1;
			}
		}

		// Items
		Collection<Item> items = romLevel.getItems();
		buffer.putInt(items.size());
		bytes += 4;
		for ( Item item : items ) {
			buffer.put(item.getType().getValue());
			buffer.putInt(item.getX());
			buffer.putInt(item.getY());
			bytes += 9;
		}

		// Door Items
		Collection<Item> doorItems = romLevel.getDoorItems();
		buffer.putInt(doorItems.size());
		bytes += 4;
		for ( Item doorItem : doorItems ) {
			buffer.put(doorItem.getType().getValue());
			buffer.putInt(doorItem.getX());
			buffer.putInt(doorItem.getY());
			bytes += 9;
		}

		// Enemies
		Collection<Enemy> enemies = romLevel.getEnemies();
		buffer.putInt(enemies.size());
		bytes += 4;
		for ( Enemy enemy : enemies ) {
			buffer.put(enemy.getType().getValue());
			buffer.putInt(enemy.getX());
			buffer.putInt(enemy.getY());
			bytes += 9;
		}

		// Return
		return bytes;
	}

}
