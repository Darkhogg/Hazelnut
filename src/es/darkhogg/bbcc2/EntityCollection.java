package es.darkhogg.bbcc2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class EntityCollection<E extends Entity> implements Collection<E> {

	private final int maxSize;
	private final Set<E> entities;
	
	public EntityCollection ( int maxSize ) {
		this.maxSize = maxSize;
		entities = new HashSet<E>();
	}
	
	public EntityCollection ( EntityCollection<? extends E> items ) {
		maxSize = items.maxSize;
		entities = new HashSet<E>( items.entities );
	}

	@Override
	public boolean add ( E elem ) {
		if ( elem == null ) {
			throw new NullPointerException();
		}
		if ( entities.size() >= maxSize ) {
			System.out.println( "FULL (" + size() + "/" + maxSize() + ") " + this );
			return false;
		}
		
		return entities.add( elem );
	}

	@Override
	public boolean addAll ( Collection<? extends E> elems ) {
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
			ret |= add( elem );
		}
		return ret;
	}

	@Override
	public void clear () {
		entities.clear();
		
	}

	@Override
	public boolean contains ( Object elem ) {
		return entities.contains( elem );
	}

	@Override
	public boolean containsAll ( Collection<?> elems ) {
		return entities.containsAll( elems );
	}

	@Override
	public boolean isEmpty () {
		return entities.isEmpty();
	}

	@Override
	public Iterator<E> iterator () {
		return entities.iterator();
	}

	@Override
	public boolean remove ( Object elem ) {
		return entities.remove( elem );
	}

	@Override
	public boolean removeAll ( Collection<?> elems ) {
		return entities.removeAll( elems );
	}

	@Override
	public boolean retainAll ( Collection<?> elems ) {
		return entities.retainAll( elems );
	}

	@Override
	public int size () {
		return entities.size();
	}

	public int maxSize () {
		return maxSize;
	}
	
	@Override
	public Object[] toArray () {
		return entities.toArray();
	}

	@Override
	public <T> T[] toArray ( T[] arr ) {
		return entities.toArray( arr );
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append( EntityCollection.class.getName() );
		sb.append( '{' );
		boolean first = true;
		for ( E e : this ) {
			if ( first ) {
				first = false;
			} else {
				sb.append( ',' );
			}
			sb.append( e );
		}
		sb.append( '}' );
		
		return sb.toString();
	}
}
