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

import java.util.Iterator;

/**
 * Represents a level from BBCC2 as encountered in the original ROM
 *
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public final class RomLevel {

	private Theme theme;
	private Weapon weapon;

	private IntVector size;
	private IntVector spawn;

	private final EntityCollection<Item> items;
	private final EntityCollection<Enemy> enemies;
	private final EntityCollection<Item> doorItems;

	//private byte[][] data;
	private final ComboGrid grid;

	public RomLevel(
		Theme theme, Weapon weapon, IntVector size, IntVector spawn
	) {
		this.grid = new ComboGrid(size);

		setTheme(theme);
		setWeapon(weapon);
		setSize(size);
		setSpawn(spawn);

		this.items = new EntityCollection<Item>( 16 );
		this.enemies = new EntityCollection<Enemy>( 15 );
		this.doorItems = new EntityCollection<Item>( 16 );

	}

	public RomLevel(RomLevel romLevel) {
		grid = new ComboGrid(romLevel.grid);

		theme = romLevel.theme;
		weapon = romLevel.weapon;
		size = romLevel.size;
		spawn = romLevel.spawn;

		items = new EntityCollection<Item>( romLevel.items );
		enemies = new EntityCollection<Enemy>( romLevel.enemies );
		doorItems = new EntityCollection<Item>( romLevel.doorItems );
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		if ( theme == null ) {
			throw new NullPointerException();
		}
		this.theme = theme;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		if ( weapon == null ) {
			throw new NullPointerException();
		}
		this.weapon = weapon;
	}

	public IntVector getSize() {
		return size;
	}

	public void setSize(IntVector size) {
		if ( size == null ) {
			throw new NullPointerException();
		}

		if ( size.getX() < 0 || size.getX() > 255
		  || size.getY() < 0 || size.getY() > 255
		) {
			throw new IllegalArgumentException();
		}

		grid.setSize(size);

		// Set the new size
		this.size = size;
	}

	public void pruneItems(int maxItems) {
		var n = 0;
		for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
			final var item = it.next();

      final var x = item.getX();
      final var y = item.getY();

			if (x < 0 || y < 0 || x >= size.getX() || y >= size.getY()) {
				n++;
				it.remove();
			}
			if (n >= maxItems) {
				return;
			}
		}
	}

	public void pruneEnemies(int maxEnemies) {
		var n = 0;
		for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
			final var enem = it.next();

      final var x = enem.getX();
      final var y = enem.getY();

			if (x < 0 || y < 0 || x >= size.getX() || y >= size.getY()) {
				n++;
				it.remove();
			}
			if (n >= maxEnemies) {
				return;
			}
		}
	}

	public void pruneDoorItems(int maxDoorItems, ComboInfoMap comboInfoMap) {
		var n = 0;
		for (Iterator<Item> it = doorItems.iterator(); it.hasNext(); ) {
			final var ditem = it.next();

      final var x = ditem.getX();
      final var y = ditem.getY();

			if (x < 0 || y < 0 || x >= size.getX() || y >= size.getY()) {
				n++;
				it.remove();
			} else if (comboInfoMap != null) {
        final var ct = comboInfoMap.getComboType(grid.get(x, y + 2));
        if (ct != ComboType.DOOR) {
          n++;
				  it.remove();
        }
      }
			if (n >= maxDoorItems) {
				return;
			}
		}
	}

  public void pruneDoorItems(int maxDoorItems) {
    pruneDoorItems(maxDoorItems, null);
  }

	public void prune(ComboInfoMap comboInfoMap) {
		pruneItems(items.maxSize());
		pruneEnemies(enemies.maxSize());
		pruneDoorItems(doorItems.maxSize(), comboInfoMap);

		// Clamp the spawn to the grid
		spawn = new IntVector(
			Math.max(0, Math.min(spawn.getX(), size.getX() - 1)),
			Math.max(0, Math.min(spawn.getY(), size.getY() - 1))
		);

		// prune the grid
		grid.prune();
	}

  public void prune() {
    prune(null);
  }

	public IntVector getSpawn() {
		return spawn;
	}

	public void setSpawn(IntVector spawn) {
		if ( spawn == null ) {
			throw new NullPointerException();
		}
		this.spawn = spawn;
	}

	public EntityCollection<Item> getItems() {
		return items;
	}

	public EntityCollection<Enemy> getEnemies() {
		return enemies;
	}

	public EntityCollection<Item> getDoorItems() {
		return doorItems;
	}

	public ComboGrid getGrid() {
		return grid;
	}

	public void move(IntVector move) {
		// move the grid
		grid.move(move);

		// move the spawn
		spawn = spawn.add(move);

		// move the items
		final var newItems = new EntityCollection<Item>(items.maxSize());
		for (final var item : items) {
			newItems.add(new Item(item.getType(), item.getPosition().add(move)));
		}
		items.clear();
		items.addAll(newItems);

		// move the enemies
		final var newEnemies = new EntityCollection<Enemy>(enemies.maxSize());
		for (final var enemy : enemies) {
			newEnemies.add(new Enemy(enemy.getType(), enemy.getPosition().add(move)));
		}
		enemies.clear();
		enemies.addAll(newEnemies);

		// move the door items
		final var newDoorItems = new EntityCollection<Item>(doorItems.maxSize());
		for (final var doorItem : doorItems) {
			newDoorItems.add(new Item(doorItem.getType(), doorItem.getPosition().add(move)));
		}
		doorItems.clear();
		doorItems.addAll(newDoorItems);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(RomLevel.class.getCanonicalName());

		sb.append("{");
		sb.append("Theme:");
		sb.append(theme);

		sb.append(";Size:");
		sb.append(size.getX());
		sb.append(',');
		sb.append(size.getY());

		sb.append(";Weapon:");
		sb.append(weapon);

		sb.append(";Spawn:");
		sb.append(spawn.getX());
		sb.append(',');
		sb.append(spawn.getY());

		sb.append("}");
		return sb.toString();
	}
}
