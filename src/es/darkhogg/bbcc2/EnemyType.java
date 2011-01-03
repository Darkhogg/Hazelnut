package es.darkhogg.bbcc2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum EnemyType implements EntityType {
	//VOID			( 0x00 ),
	FOGHORN			( 0x01 ),
	FOGHORN_DARK	( 0x02 ),
	BEAKY			( 0x03 ),
	BEAKY_DARK		( 0x04 ),
	DAFFY			( 0x05 ),
	FLAME			( 0x06 ),
	HAZEL			( 0x07 );

	private final static Map<Byte,EnemyType> byteMap;
	static {
		Map<Byte,EnemyType> bm = new HashMap<Byte,EnemyType>();
		for ( EnemyType eg : values() ) {
			bm.put( eg.getValue(), eg );
		}
		byteMap = Collections.unmodifiableMap( bm );
	}
	
	private final byte value;
	
	private EnemyType ( int value ) {
		this.value = (byte)value;
	}
	
	@Override
	public byte getValue () {
		return value;
	}

	public static EnemyType valueOf ( byte bt ) {
		return byteMap.get( Byte.valueOf( bt ) );
	}
	
}
