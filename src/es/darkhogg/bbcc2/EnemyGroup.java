package es.darkhogg.bbcc2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum EnemyGroup {
	DAFFY		( 0x03 ),
	YOSEMITE	( 0x04 ),
	SYLVESTER	( 0x05 ),
	BOSS		( 0x06 );
	
	private final static Map<Byte,EnemyGroup> byteMap;
	static {
		Map<Byte,EnemyGroup> bm = new HashMap<Byte,EnemyGroup>();
		for ( EnemyGroup eg : values() ) {
			bm.put( eg.getValue(), eg );
		}
		byteMap = Collections.unmodifiableMap( bm );
	}
	
	private final byte value;
	
	private EnemyGroup ( int value ) {
		this.value = (byte)value;
	}
	
	public byte getValue () {
		return value;
	}
	
	public static EnemyGroup valueOf ( byte bt ) {
		return byteMap.get( Byte.valueOf( bt ) );
	}
}
