package es.darkhogg.bbcc2;

public enum Weapon {
	ARROWS( 0 ), BOMBS( 1 );
	
	private final byte value;
	
	private Weapon ( int value ) {
		this.value = (byte) value;
	}
	
	public byte getValue () {
		return value;
	}
	
	public static Weapon valueOf ( int value ) {
		if ( value == 0 ) {
			return ARROWS;
		}
		
		return BOMBS;
	}
}
