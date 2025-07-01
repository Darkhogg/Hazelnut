package es.darkhogg.bbcc2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Theme {
	HALL		( 0x00 ),
	BASEMENT	( 0x01 ),
	BALCONY		( 0x02 ),
	TREASURY	( 0x03 ),
	GATE		( 0x04 ),
	GARDEN		( 0x05 ),
	FUNHOUSE	( 0x06 );
	
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
