package es.darkhogg.bbcc2;

import es.darkhogg.util.IntVector;

public abstract class Entity {
	
	private final EntityType type;
	private final IntVector pos;
	
	protected Entity ( EntityType type, IntVector pos ) {
		this.type = type;
		this.pos = pos;
	}
	
	protected Entity ( EntityType type, int x, int y ) {
		this( type, new IntVector( x, y ) );
	}
	
	public EntityType getType () {
		return type;
	}
	
	public final IntVector getPosition () {
		return pos;
	}
	
	public final int getX () {
		return pos.getX();
	}
	
	public final int getY () {
		return pos.getY();
	}
	
	@Override
	public boolean equals ( Object obj ) {
		if ( !(obj instanceof EntityType) ) {
			return false;
		}
		
		Entity ent = (Entity) obj;
		return ent.type.equals( type )
			&& ent.pos.equals( pos );
	}
	
	@Override
	public int hashCode () {
		return 7 * type.hashCode() * pos.hashCode();
	}
	
	@Override
	public String toString () {
		return getClass().getName() + "{" + type + ";" + pos.getX() + "," + pos.getY() + "}";
	}
}
