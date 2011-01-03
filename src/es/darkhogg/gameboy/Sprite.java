package es.darkhogg.gameboy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import java.util.Set;

public final class Sprite {

	private final int width;
	private final int height;
	
	private final Tile[][] tiles;
	private final Set<TileTransform>[][] transforms;
	
	@SuppressWarnings( "unchecked" )
	public Sprite ( int width, int height ) {
		this.width = width;
		this.height = height;
		
		tiles = new Tile[ width ][ height ];
		transforms = (Set<TileTransform>[][]) new Set[ width ][ height ];
		
		for ( int i = 0; i < width; i++ ) {
			for ( int j = 0; j < height; j++ ) {
				setTileAndTransformAt( i, j, null, null );
			}
		}
	}
	
	public int getWidth () {
		return width;
	}
	
	public int getHeight () {
		return height;
	}
	
	public Tile getTileAt ( int x, int y ) {
		return tiles[ x ][ y ];
	}
	
	public Set<TileTransform> getTransformAt ( int x, int y ) {
		return transforms[ x ][ y ];
	}
	
	public void setTileAt ( int x, int y, Tile tile ) {
		if ( tile == null ) {
			tile = Tile.VOID_TILE;
		}
		tiles[ x ][ y ] = tile;
	}
	
	public void setTransformAt ( int x, int y, Set<TileTransform> transform ) {
		if ( transform == null ) {
			transform = EnumSet.noneOf( TileTransform.class );
		}
		transforms[ x ][ y ] = transform;
	}
	
	public void setTileAndTransformAt ( int x, int y, Tile tile, Set<TileTransform> transform ) {
		setTileAt( x, y, tile );
		setTransformAt( x, y, transform );
	}
	
	public Image asImage ( Palette pal ) {
		BufferedImage im = new BufferedImage(
			width*8, height*8, BufferedImage.TYPE_INT_ARGB );
		Graphics g = (Graphics2D)im.getGraphics();
		
		for ( int i = 0; i < width; i++ ) {
			for ( int j = 0; j < height; j++ ) {
				Image tim = tiles[ i ][ j ].asImage( pal );
				boolean hflip = transforms[ i ][ j ].contains(
					TileTransform.HORIZONTAL_FLIP );
				boolean vflip = transforms[ i ][ j ].contains(
					TileTransform.VERTICAL_FLIP );
				int x1 = i*8 + (hflip?8:0);
				int x2 = i*8 + (hflip?0:8);
				int y1 = j*8 + (vflip?8:0);
				int y2 = j*8 + (vflip?0:8);
				g.drawImage( tim, x1, y1, x2, y2, 0, 0, 8, 8, null );
			}
		}
		
		return im;
	}
}
