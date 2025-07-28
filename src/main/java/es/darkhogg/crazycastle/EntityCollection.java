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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class EntityCollection<E extends Entity> implements Collection<E> {

	private final int maxSize;
	private final Set<E> entities;

	public EntityCollection(int maxSize) {
		this.maxSize = maxSize;
		entities = new HashSet<E>();
	}

	public EntityCollection(EntityCollection<? extends E> items) {
		maxSize = items.maxSize;
		entities = new HashSet<E>( items.entities );
	}

	@Override
	public boolean add(E elem) {
		if ( elem == null ) {
			throw new NullPointerException();
		}
		if ( entities.size() >= maxSize ) {
			return false;
		}

		return entities.add(elem);
	}

	@Override
	public boolean addAll(Collection<? extends E> elems) {
		if ( elems.size() + entities.size() > maxSize ) {
			return false;
		}

		for ( E elem : elems ) {
			if ( elem == null ) {
				throw new NullPointerException();
			}
		}

		boolean ret = false;
		for ( E elem : elems ) {
			ret |= add(elem);
		}
		return ret;
	}

	@Override
	public void clear() {
		entities.clear();

	}

	@Override
	public boolean contains(Object elem) {
		return entities.contains(elem);
	}

	@Override
	public boolean containsAll(Collection<?> elems) {
		return entities.containsAll(elems);
	}

	@Override
	public boolean isEmpty() {
		return entities.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return entities.iterator();
	}

	@Override
	public boolean remove(Object elem) {
		return entities.remove(elem);
	}

	@Override
	public boolean removeAll(Collection<?> elems) {
		return entities.removeAll(elems);
	}

	@Override
	public boolean retainAll(Collection<?> elems) {
		return entities.retainAll(elems);
	}

	@Override
	public int size() {
		return entities.size();
	}

	public int maxSize() {
		return maxSize;
	}

	public boolean isFull() {
		return entities.size() >= maxSize;
	}

	@Override
	public Object[] toArray() {
		return entities.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arr) {
		return entities.toArray(arr);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(EntityCollection.class.getName());
		sb.append('{');
		boolean first = true;
		for ( E e : this ) {
			if ( first ) {
				first = false;
			} else {
				sb.append(',');
			}
			sb.append(e);
		}
		sb.append('}');

		return sb.toString();
	}
}
