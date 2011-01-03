package es.darkhogg.bbcc2;

import es.darkhogg.util.IntVector;

public final class Enemy extends Entity {

	public Enemy ( EnemyType type, IntVector pos ) {
		super( type, pos );
	}
	
	public Enemy ( EnemyType type, int x, int y ) {
		super( type, x, y );
	}
	
	public EnemyType getType () {
		return (EnemyType)super.getType();
	}

}
