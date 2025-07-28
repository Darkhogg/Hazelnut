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

public abstract class Entity {

	private final EntityType type;
	private final IntVector pos;

	protected Entity(EntityType type, IntVector pos) {
		this.type = type;
		this.pos = pos;
	}

	protected Entity(EntityType type, int x, int y) {
		this(type, new IntVector( x, y ));
	}

	public EntityType getType() {
		return type;
	}

	public final IntVector getPosition() {
		return pos;
	}

	public final int getX() {
		return pos.getX();
	}

	public final int getY() {
		return pos.getY();
	}

	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof EntityType) ) {
			return false;
		}

		Entity ent = (Entity)obj;
		return ent.type.equals(type)
			&& ent.pos.equals(pos);
	}

	@Override
	public int hashCode() {
		return 7 * type.hashCode() * pos.hashCode();
	}

	@Override
	public String toString() {
		return getClass().getName() + "{" + type + ";" + pos.getX() + "," + pos.getY() + "}";
	}
}
