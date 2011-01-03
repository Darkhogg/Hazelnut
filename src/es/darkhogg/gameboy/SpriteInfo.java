package es.darkhogg.gameboy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class SpriteInfo {

	private final int width;
	private final int height;
	
	private final int[][] tileNums;
	private final Set<TileTransform>[][] transforms;
	
	@SuppressWarnings( "unchecked" )
	public SpriteInfo ( int width, int height ) {
		this.width = width;
		this.height = height;
		
		tileNums = new int[ width ][ height ];
		transforms = (Set<TileTransform>[][]) new Set[ width ][ height ];
		
		for ( int i = 0; i < width; i++ ) {
			for ( int j = 0; j < height; j++ ) {
				setTransformAt( i, j, null );
			}
		}
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	public int getTileAt ( int x, int y ) {
		return tileNums[ x ][ y ];
	}
	
	public Set<TileTransform> getTransformAt ( int x, int y ) {
		return transforms[ x ][ y ];
	}
	
	public void setTileAt ( int x, int y, int tile ) {
		if ( tile < 0 ) {
			throw new IllegalArgumentException();
		}
		tileNums[ x ][ y ] = tile;
	}
	
	public void setTransformAt ( int x, int y, Set<TileTransform> transform ) {
		if ( transform == null ) {
			transform = EnumSet.noneOf( TileTransform.class );
		}
		transforms[ x ][ y ] = transform;
	}
	
	public void setTileAndTransformAt ( int x, int y, int tile, Set<TileTransform> transform ) {
		setTileAt( x, y, tile );
		setTransformAt( x, y, transform );
	}
	
	public Sprite generateSpriteFromTileSet ( List<Tile> tileset ) {
		final Sprite spr = new Sprite( width, height );
		
		for ( int i = 0; i < width; i++ ) {
			for ( int j = 0; j < height; j++ ) {
				int num = tileNums[ i ][ j ];
				spr.setTileAndTransformAt( i, j,
					tileset.size() > num ? tileset.get( num ) : Tile.VOID_TILE,
					transforms[ i ][ j ]
				);
			}
		}
		
		return spr;
	}
	
	public static SpriteInfo fromString ( String string ) {
		final String str = string.trim();
		
		String[] pieces = str.split( "\\s+" );
		
		// Parse the width and height
		int w, h, i;
		String[] dims = pieces[ 0 ].split( "x" );
		if ( dims.length == 2 ) {
			w = Integer.parseInt( dims[ 0 ] );
			h = Integer.parseInt( dims[ 1 ] );
			i = 1;
		} else {
			w = 2;
			h = 2;
			i = 0;
		}
		
		// Check for correctness
		if ( pieces.length != w*h + i ) {
			throw new IllegalArgumentException();
		}
		
		// Create the sprite
		SpriteInfo spri = new SpriteInfo( w, h );
		
		// Create the transforms (less memory-less time)
		final Set<TileTransform> trN, trV, trH, trR;
		trN = EnumSet.noneOf( TileTransform.class );
		trV = EnumSet.of( TileTransform.VERTICAL_FLIP );
		trH = EnumSet.of( TileTransform.HORIZONTAL_FLIP );
		trR = EnumSet.of( TileTransform.VERTICAL_FLIP, TileTransform.HORIZONTAL_FLIP );
		
		// Parse every piece
		for ( int j = 0; j < w*h; j++, i++ ) {
			String[] piece = pieces[i].split( ":" );
			if ( piece.length == 1 ) {
				spri.setTileAt( j%w, j/w, Integer.parseInt( piece[0] ) );
			} else if ( piece.length == 2 ) {
				int num = Integer.parseInt( piece[0] );
				String trstr = piece[1];
				if ( trstr.length() != 1 ) {
					throw new IllegalArgumentException();
				}
				char trchr = trstr.charAt( 0 );
				
				Set<TileTransform> tr;
				switch ( trchr ) {
					case 'N': tr = trN; break;
					case 'V': tr = trV; break;
					case 'H': tr = trH; break;
					case 'R': tr = trR; break;
					default: throw new IllegalArgumentException();
				}
				
				spri.setTileAndTransformAt( j%w, j/w, num, tr );
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		// Return the result
		return spri;
	}
	
	public String toString () {
		StringBuffer sb = new StringBuffer();
		sb.append( width );
		sb.append( 'x' );
		sb.append( height );
		sb.append( ' ' );
		
		for ( int j = 0; j < height; j++ ) {
			for ( int i = 0; i < width; i++ ) {
				sb.append( ' ' );
				sb.append( tileNums[ i ][ j ] );
				final Set<TileTransform> tt = transforms[ i ][ j ];
				final boolean hf = tt.contains( TileTransform.HORIZONTAL_FLIP );
				final boolean vf = tt.contains( TileTransform.VERTICAL_FLIP );
				if ( vf && hf ) {
					sb.append( ":R" );
				} else if ( vf ) {
					sb.append( ":V" );
				} else if ( hf ) {
					sb.append( ":H" );
				}
			}
		}
		
		return sb.toString();
	}
	
	public static List<SpriteInfo> loadInfoList ( URL url )
	throws IOException {
		List<SpriteInfo> info = new ArrayList<SpriteInfo>();
		BufferedReader in = new BufferedReader(
			new InputStreamReader( url.openStream() )
		);
		while ( in.ready() ) {
			info.add( SpriteInfo.fromString( in.readLine() ) );
		}
		return info;
	}

}
