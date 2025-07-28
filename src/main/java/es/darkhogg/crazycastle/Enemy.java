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

public final class Enemy extends Entity {

	public Enemy(EnemyType type, IntVector pos) {
		super(type, pos);
	}

	public Enemy(EnemyType type, int x, int y) {
		super(type, x, y);
	}

	public EnemyType getType() {
		return (EnemyType)super.getType();
	}

}
