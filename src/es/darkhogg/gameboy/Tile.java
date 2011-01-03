package es.darkhogg.gameboy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Represents a GameBoy tile, that is, a 8x8 image which can display a maximum
 * of 4 colors. Includes methods to convert it to AWT Images and to inspect the
 * color value in each position.
 * <p>
 * This class is immutable.
 * 
 * @author Daniel Escoz (Darkhogg)
 * @version 1.0
 */
public final class Tile {
	
	/**
	 * A void tile to use instead of <tt>null</tt> in certain situations
	 */
	public static final Tile VOID_TILE = new Tile( 0L, 0L );
	
	/**
	 * The first 8 bytes of the tile
	 */
	private final long first;
	
	/**
	 * The last 8 bytes of the tile
	 */
	private final long last;
	
	/**
	 * The Tile data
	 */
	private final int[][] data;
	
	/**
	 * Creates a new tile using the passed array.
	 * No reference to the passed array is kept.
	 * 
	 * @param data An 8x8 two-dimensional array of numbers from 0 to 3, inclusive.
	 * @throws IllegalArgumentException If <i>data</i> is not 8x8 or contains an
	 *         invalid value
	 * @throws NullPointerException If <i>data</i> is <tt>null</tt>
	 */
	public Tile ( int[][] data ) {
		if ( data.length != 8 ) {
			throw new IllegalArgumentException(
				"A tile must be 8 pixels width" );
		}
		
		long high = 0;
		long low = 0;
		
		// Copy the data array
		this.data = new int[ 8 ][ 8 ];
		for ( int i = 0; i < data.length; i++ ) {
			if ( data[i].length != 8 ) {
				throw new IllegalArgumentException(
					"A tile must be 8 pixels height" );
			}
			
			for ( int j = 0; j < data[i].length; j++ ) {
				if ( data[i][j] < 0 || data[i][j] > 3 ) {
					throw new IllegalArgumentException(
						"Colors must be 0, 1, 2 or 3" );
				}
				
				int value = data[ i ][ j ];
				boolean h = (value&2)==2;
				boolean l = (value&1)==1;
				int sh = (63 - (i + j*8));
				high |= (h?1L:0L)<<sh;
				low |= (l?1L:0L)<<sh;
				this.data[ i ][ j ] = value;
			}
		}
		
		this.first = ((low & 0xFF00000000000000L) >>> 0)
                   | ((low & 0x00FF000000000000L) >>> 8)
                   | ((low & 0x0000FF0000000000L) >>> 16)
                   | ((low & 0x000000FF00000000L) >>> 24)
		           | ((high & 0xFF00000000000000L) >>> 8)
		           | ((high & 0x00FF000000000000L) >>> 16)
		           | ((high & 0x0000FF0000000000L) >>> 24)
		           | ((high & 0x000000FF00000000L) >>> 32);
		
		this.last = ((low & 0x00000000FF000000L) << 32)
                  | ((low & 0x0000000000FF0000L) << 24)
                  | ((low & 0x000000000000FF00L) << 16)
                  | ((low & 0x00000000000000FFL) << 8)
                  | ((high & 0x00000000FF000000L) << 24)
                  | ((high & 0x0000000000FF0000L) << 16)
                  | ((high & 0x000000000000FF00L) << 8)
                  | ((high & 0x00000000000000FFL) << 0 );
	}
	
	/**
	 * Constructs a Tile from two long values, 16 bytes total, representing the
	 * tile in the original GB format.
	 * <p>
	 * This method basically converts the internal GameBoy tile representation
	 * to a Tile object.
	 * 
	 * @param first The first 8 bytes of the tile data
	 * @param last The last 8 bytes of the tile data
	 */
	public Tile ( long first, long last ) {
		this.first = first;
		this.last = last;
		data = new int[ 8 ][ 8 ];
		
		long high = ((first & 0x00FF000000000000L) << 8)
                  | ((first & 0x000000FF00000000L) << 16)
                  | ((first & 0x0000000000FF0000L) << 24)
                  | ((first & 0x00000000000000FFL) << 32)
                  | ((last & 0x00FF000000000000L) >>> 24)
                  | ((last & 0x000000FF00000000L) >>> 16)
                  | ((last & 0x0000000000FF0000L) >>> 8)
                  | ((last & 0x00000000000000FFL) >>> 0);
		
		long low = ((first & 0xFF00000000000000L) << 0)
		         | ((first & 0x0000FF0000000000L) << 8)
		         | ((first & 0x00000000FF000000L) << 16)
		         | ((first & 0x000000000000FF00L) << 24)
		         | ((last & 0xFF00000000000000L) >>> 32)
		         | ((last & 0x0000FF0000000000L) >>> 24)
		         | ((last & 0x00000000FF000000L) >>> 16)
		         | ((last & 0x000000000000FF00L) >>> 8);
		
		int sh = 63;
		for ( int j = 0; j < 8; j++ ) {
			for ( int i = 0; i < 8; i++ ) {
				long msk = 1L << sh;
				boolean lb = (low & msk) == msk;
				boolean hb = (high & msk) == msk;
				data[ i ][ j ] = (hb?2:0) + (lb?1:0);
				sh--;
			}
		}
	}
	
	/**
	 * Returns a copy of the data array.
	 * <p>
	 * Use this method carefully, as it allocates a new array and performs in
	 * quadratic time (as any Tile is always 8x8, it could be considered
	 * <i>constant</i>, but it <i>is</i> quadratic on the size of the tile).
	 * <p>
	 * The returned array is <i>safe</i>, as no references are kept by this
	 * object to it or any of its components.
	 * 
	 * @return A new bidimensional array with this tile color values.
	 */
	public int[][] getData () {
		int[][] ret = new int[8][8];
		
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				ret[i][j] = data[i][j];
			}
		}
		
		return ret;
	}
	
	/**
	 * Gets the color value of a position of the tile
	 * 
	 * @param x The horizontal coordinate, between 0 and 7 (inclusive)
	 * @param y The vertical coordinate, between 0 and 7 (inclusive)
	 * @return The color value, between 0 and 3 (inclusive), of that position
	 * @throws IllegalArgumentException If <i>x</i> or <i>y</i> are outside the
	 *         range [0,8)
	 */
	public int getValueAt ( int x, int y ) {
		if ( x < 0 || x > 7 || y < 0 || y > 7 ) {
			throw new IllegalArgumentException();
		}
		
		return data[ x ][ y ];
	}
	
	/**
	 * Returns the first 8 bytes of this tile as a <tt>long</tt> value
	 * 
	 * @return This tile's first 8 bytes
	 */
	public long getFirstBits () {
		return first;
	}
	
	/**
	 * Returns the last 8 bytes of this tile as a <tt>long</tt> value
	 * 
	 * @return This tile's last 8 bytes
	 */
	public long getLastBits () {
		return last;
	}
	
	/**
	 * Returns a newly allocated Image object representing this tile in the
	 * given palette.
	 * 
	 * @param pal The palette to use for this image colors
	 * @return An image representing this tile using the given palette
	 * @throws NullPointerException If <i>pal</i> is <tt>null</tt>
	 */
	public Image asImage ( Palette pal ) {
		if ( pal == null ) {
			throw new NullPointerException();
		}
		
		BufferedImage im = new BufferedImage( 8, 8, BufferedImage.TYPE_INT_ARGB );
		Graphics g = im.getGraphics();
		
		for ( int i = 0; i < 8; i++ ) {
			for ( int j = 0; j < 8; j++ ) {
				g.setColor( pal.getColor( data[ i ][ j ] ) );
				g.drawLine( i, j, i, j );
			}
		}
		
		return im;
	}

	/**
	 * A Tile object is equal only to another Tile object that represents the
	 * same GameBoy tile, that is, which has the same pixel value colors.
	 */
	@Override
	public boolean equals ( Object obj ) {
		if ( !(obj instanceof Tile) ) {
			return false;
		}
		
		Tile tile = (Tile) obj;
		return tile.first == first
			&& tile.last == last;
	}
	
	/**
	 * Returns this tile's hash code
	 */
	@Override
	public int hashCode () {
		return (int)( 3 * first * last );
	}
}
