/**
 * This file is part of Hazelnutt.
 * 
 * Hazelnutt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Hazelnutt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Hazelnutt.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.darkhogg.hazelnutt;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import es.darkhogg.bbcc2.Level;
import es.darkhogg.bbcc2.LevelFormat;

public class HazelnuttLevelFormat extends LevelFormat {
	
	public static final int VERSION = 1;
	
	private static HazelnuttLevelFormat instance;
	
	private HazelnuttLevelFormat () {}
	
	public static HazelnuttLevelFormat getInstance () {
		if ( instance == null ) {
			instance = new HazelnuttLevelFormat();
		}
		return instance;
	}
	
	@Override
	public Level loadLevelFromBuffer ( ByteBuffer buffer ) {
		int pos = buffer.position();
		LevelFormat format = getFormatForVersion( buffer.getInt() );
		buffer.position( pos );
		
		return format.loadLevelFromBuffer( buffer );
	}
	
	@Override
	public Level loadLevelFromFile ( File file )
	throws IOException {
		LevelFormat format = null;
		DataInputStream fin = null;
		try {
			fin = new DataInputStream( new FileInputStream( file ) );
			format = getFormatForVersion( fin.readInt() );
		} finally {
			if ( fin != null ) {
				fin.close();
			}
		}
		
		return format.loadLevelFromFile( file );
	}

	@Override
	public int saveLevelToBuffer ( Level level, ByteBuffer buffer ) {
		LevelFormat format = getFormatForVersion( VERSION );
		return format.saveLevelToBuffer( level, buffer );
	}
	
	@Override
	public int saveLevelToFile ( Level level, File file )
	throws IOException {
		LevelFormat format = getFormatForVersion( VERSION );
		return format.saveLevelToFile( level, file );
	}

	protected static LevelFormat getFormatForVersion ( int ver ) {
		switch ( ver ) {
			case 1: return Hlf1LevelFormat.getInstance();
		}
		
		throw new IllegalArgumentException( "There is no HLF v" + ver );
	}
}
