package es.darkhogg.bbcc2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Theme {
	HALL		( 0x00 ),
	DUNGEON		( 0x01 ),
	ROOFTOP		( 0x02 ),
	LUXURY		( 0x03 ),
	ENTRANCE	( 0x04 ),
	VINEYARD	( 0x05 ),
	BALLROOM	( 0x06 );
	
	private final static Map<Byte,Theme> byteMap;
	static {
		Map<Byte,Theme> bm = new HashMap<Byte,Theme>();
		for ( Theme eg : values() ) {
			bm.put( eg.getValue(), eg );
		}
		byteMap = Collections.unmodifiableMap( bm );
	}
	
	private final byte value;
	
	private Theme ( int value ) {
		this.value = (byte)value;
	}
	
	public byte getValue () {
		return value;
	}

	public static Theme valueOf ( byte bt ) {
		return byteMap.get( Byte.valueOf( bt ) );
	}
}
