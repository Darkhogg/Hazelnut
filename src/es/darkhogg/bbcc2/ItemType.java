package es.darkhogg.bbcc2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ItemType implements EntityType {
	//VOID	( 0x00 ),
	BOW		( 0x01 ),
	CLOCK	( 0x02 ),
	BOMB	( 0x03 ),
	PICKAXE	( 0x04 ),
	POTION	( 0x05 ),
	LIFE	( 0x06 ),
	HAMMER	( 0x07 ),
	CHEST	( 0x08 ),
	SHIELD	( 0x09 ),
	BOLT	( 0x0A ),
	KEY		( 0x0B ),
	WEIGHT	( 0x0C );

	private final static Map<Byte,ItemType> byteMap;
	static {
		Map<Byte,ItemType> bm = new HashMap<Byte,ItemType>();
		for ( ItemType eg : values() ) {
			bm.put( eg.getValue(), eg );
		}
		byteMap = Collections.unmodifiableMap( bm );
	}
	
	private final byte value;
	
	private ItemType ( int value ) {
		this.value = (byte)value;
	}
	
	@Override
	public byte getValue () {
		return value;
	}

	public static ItemType valueOf ( byte bt ) {
		return byteMap.get( Byte.valueOf( bt ) );
	}
}
