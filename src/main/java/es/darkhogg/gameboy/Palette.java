/**
 * This file is part of Hazelnut.
 *
 * Hazelnut is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hazelnut is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hazelnut.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.darkhogg.gameboy;

import java.awt.Color;

public final class Palette {

	private final Color color0;
	private final Color color1;
	private final Color color2;
	private final Color color3;

	public Palette(Color c0, Color c1, Color c2, Color c3) {
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

	private Palette(Color[] colors) {
		this(colors[0], colors[1], colors[2], colors[3]);
	}

	public Color getColor0() {
		return color0;
	}

	public Color getColor1() {
		return color1;
	}

	public Color getColor2() {
		return color2;
	}

	public Color getColor3() {
		return color3;
	}

	public Color getColor(int i) {
		switch ( i ) {
			case 0: return color0;
			case 1: return color1;
			case 2: return color2;
			case 3: return color3;
			default: throw new IllegalArgumentException( String.valueOf(i) );
		}
	}

	@Override
	public String toString() {
		return "es.darkhogg.gb.Pallete{" + color0 + "," + color1 + ","
			   + color2 + "," + color3 + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if ( !(obj instanceof Palette) ) {
			return false;
		}

		Palette pal = (Palette)obj;

		return pal.color0.equals(color0)
			&& pal.color1.equals(color1)
			&& pal.color2.equals(color2)
			&& pal.color3.equals(color3);
	}

	@Override
	public int hashCode() {
		return color0.hashCode()
			 * color1.hashCode()
			 * color2.hashCode()
			 * color3.hashCode()
			 * 7;
	}

	public Palette cloneWith(int i, Color col) {
		if (i < 0 || i >= 4) {
			throw new IllegalArgumentException( String.valueOf(i) );
		}

		return new Palette(
			i == 0 ? col : color0,
			i == 1 ? col : color1,
			i == 2 ? col : color2,
			i == 3 ? col : color3
		);
	}

	public String stringify() {
		final var parts = new String[4];

		for (var i = 0; i < parts.length; i++) {
			final var c = getColor(i);
			parts[i] = Integer.toHexString(c.getRGB());
		}

		return String.join(",", parts);
	}

	public static Palette parse(String str) {
		if (str == null) {
			return null;
		}

		final var parts = str.split(",");
		if (parts.length != 4) {
			return null;
		}

		final var colors = new Color[4];

		for (var i = 0; i < colors.length; i++) {
			try {
				final var rgba = (int)Long.parseLong(parts[i], 16);
				colors[i] = new Color(rgba, true);
			} catch (NumberFormatException exc) {
				exc.printStackTrace();
				return null;
			}
		}

		return new Palette(colors);
	}
}
