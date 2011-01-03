package es.darkhogg.bbcc2;

import es.darkhogg.util.IntVector;

public final class Item extends Entity {

	public Item ( ItemType type, IntVector pos ) {
		super( type, pos );
	}
	
	public Item ( ItemType type, int x, int y ) {
		super( type, x, y );
	}
	
	public ItemType getType () {
		return (ItemType)super.getType();
	}

}
