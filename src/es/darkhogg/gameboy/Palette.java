package es.darkhogg.gameboy;

import java.awt.Color;

public final class Palette {
	
	private final Color color0;
	private final Color color1;
	private final Color color2;
	private final Color color3;
	
	public Palette ( Color c0, Color c1, Color c2, Color c3 ) {
		if ( c1 == null || c2 == null || c3 == null ) {
			throw new NullPointerException();
		}
		
		if ( c0 == null ) {
			c0 = new Color( 0, 0, 0, 0 );
		}

		color0 = c0;
		color1 = c1;
		color2 = c2;
		color3 = c3;
	}
	
	public Color getColor0 () {
		return color0;
	}
	
	public Color getColor1 () {
		return color1;
	}
	
	public Color getColor2 () {
		return color2;
	}
	
	public Color getColor3 () {
		return color3;
	}
	
	public Color getColor ( int i ) {
		switch ( i ) {
			case 0: return color0;
			case 1: return color1;
			case 2: return color2;
			case 3: return color3;
			default: throw new IllegalArgumentException( String.valueOf( i ) );
		}
	}
	
	@Override
	public String toString () {
		return "es.darkhogg.gb.Pallete{" + color0 + "," + color1 + "," +
			   color2 + "," + color3 + "}";
	}
	
	@Override
	public boolean equals ( Object obj ) {
		if ( !( obj instanceof Palette ) ) {
			return false;
		}
		
		Palette pal = (Palette) obj;
		
		return pal.color0.equals( color0 )
			&& pal.color1.equals( color1 )
			&& pal.color2.equals( color2 )
			&& pal.color3.equals( color3 );
	}
	
	@Override
	public int hashCode () {
		return color0.hashCode()
			 * color1.hashCode() 
			 * color2.hashCode()
			 * color3.hashCode()
			 * 7;
	}
}
