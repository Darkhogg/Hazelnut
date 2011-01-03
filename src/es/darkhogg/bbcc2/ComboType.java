package es.darkhogg.bbcc2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ComboType {
	/**
	 * A normal door marker
	 */
	DOOR( 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52, 0x53, 0x54,
		0x55, 0x56, 0x57, 0x58, 0x59, 0x5A
	),
	
	/**
	 * A final door marker
	 */
	FINAL( 0x5B, 0x5C, 0x5D, 0x5E, 0x5F ),
	
	/**
	 * An UP tile
	 */
	//UP(),
	
	/**
	 * An up side-movement marker
	 */
	UP_LEAP( 0x87, 0xC1, 0xC2, 0xDA, 0xED ),
	
	/**
	 * Stairs-floor marker
	 */
	STAIRS( 0x71, 0x72, 0xAC ),
	
	/**
	 * Ladder tile
	 */
	//LADDER(),
	
	/**
	 * Warp tile
	 */
	//WARP(),
	
	/**
	 * Stairs up-left marker
	 */
	STAIRS_DOOR_LEFT( 0x1E ),
	
	/**
	 * Stairs up-right marker
	 */
	STAIRS_DOOR_RIGHT( 0x64 ),
	
	/**
	 * Rope "floor" marker
	 */
	ROPE_FLOOR( 0x6C, 0x6F );
	
	/**
	 * Hammer-breakable tile
	 */
	//HAMMER_BLOCK(),
	
	/**
	 * Pick-climbable tile
	 */
	//PICK_BLOCK(),
	
	/**
	 * Slowing-down floor
	 */
	//SLOW(),
	
	/**
	 * Rope tile
	 */
	//ROPE();
	
	private final static Map<Byte,ComboType> typeValues;
	static {
		typeValues = new HashMap<Byte,ComboType>();
		
		for ( ComboType ct : ComboType.values() ) {
			for ( int value : ct.values ) {
				typeValues.put( Byte.valueOf( (byte)value ), ct );
			}
		}
	}
	
	private final List<Byte> values;
	
	/**
	 * Construncts a given combo type that is assigned to the given values
	 * @param values All combo values for this type
	 */
	private ComboType ( int... values ) {
		Byte[] bValues = new Byte[ values.length ];
		for ( int i = 0; i < values.length; i++ ) {
			bValues[ i ] = Byte.valueOf( (byte)values[i] );
		}
		
		this.values = Arrays.asList( bValues );
	}
	
	public static ComboType valueOf ( byte value ) {
		return typeValues.get( value );
	}
	
}
