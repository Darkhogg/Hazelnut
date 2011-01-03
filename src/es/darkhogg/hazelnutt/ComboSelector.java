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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import es.darkhogg.bbcc2.ComboType;

@SuppressWarnings( "serial" )
public final class ComboSelector extends JComponent {
	
	private List<Image> comboSet;
	private Map<ComboType,Image> helpSet;
	private List<Image> mixedSet;
	private boolean displayHelp;
	private int selectedValue;
	private SelectorPanel selParent;
	
	public ComboSelector () {
		updateSize();
		selectValue( 0 );
		
		addMouseListener( new MouseAdapter(){
			@Override public void mousePressed ( MouseEvent me ) {
				if ( comboSet != null
				 && me.getX() >= 0 && me.getX() < 256
				 && me.getY() >= 0 && me.getY() < 256
				) {
					selParent.setSelectionType( SelectionType.COMBO );
					selectValue( me.getX()/16 + (me.getY()/16)*16 );
					repaint();
				}
			}
		});
	}
	
	public ComboSelector ( SelectorPanel parent ) {
		this();
		selParent = parent;
	}
	
	public int getSelectedValue () {
		return selectedValue;
	}
	
	public void setComboSet ( List<Image> comboset ) {
		this.comboSet = comboset;
		
		updateSize();
		generateMixedSet();
		selectValue( selectedValue );
		repaint();
	}
	
	public void setDisplayHelp ( boolean help ) {
		displayHelp = help;

		updateSize();
		generateMixedSet();
		selectValue( selectedValue );
		repaint();
	}
	
	public void setHelpSet ( Map<ComboType,Image> comboset ) {
		this.helpSet = comboset;

		updateSize();
		generateMixedSet();
		selectValue( selectedValue );
		repaint();
	}
	
	private void generateMixedSet () {
		if ( comboSet != null && helpSet != null ) {
			mixedSet = new ArrayList<Image>();
			
			for ( int i = 0; i < 256; i++ ) {
				Image img = new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB );
				Graphics g = img.getGraphics();

				g.drawImage( comboSet.get( i ), 0, 0, null );
				if ( helpSet != null && displayHelp ) {
					ComboType ct = ComboType.valueOf( (byte)i );
					if ( ct != null ) {
						Image im = helpSet.get( ct );
						if ( im != null ) {
							g.drawImage( im, 0, 0, null );
						}
					}
				}
				
				mixedSet.add( img );
			}
		}
	}
	
	private void selectValue ( int value ) {
		selectedValue = value;
		
		if ( mixedSet != null && selParent != null && value >= 0 && value < 256 ) {
			if ( selParent.getSelectionType() == SelectionType.COMBO ) {
				selParent.setSelectionImage( mixedSet.get( value ) );
				selParent.setSelectionObject( Byte.valueOf( (byte) selectedValue ) );
			}
		}
	}
	
	@Override
	public void paintComponent ( Graphics g ) {
		super.paintComponent( g );
		
		if ( comboSet != null ) {
			for ( int i = 0; i < 256; i++ ) {
				int x = (i%16) * 16 + 2;
				int y = (i/16) * 16 + 2;
				
				g.drawImage( comboSet.get( i ), x, y, null );
				
				if ( displayHelp && helpSet != null ) {
					ComboType ct = ComboType.valueOf( (byte)i );
					if ( ct != null ) {
						Image im = helpSet.get( ct );
						if ( im != null ) {
							g.drawImage( im, x, y, null );
						}
					}
				}
			}
			
			if ( selectedValue >= 0 && selectedValue < 256 ) {
				int x = (selectedValue%16) * 16 + 2;
				int y = (selectedValue/16) * 16 + 2;
				g.setColor( Color.RED );
				g.drawRect( x-1, y-1, 16, 16 );
				g.drawRect( x-2, y-2, 18, 18 );
			}
		}
	}
	
	private void updateSize () {
		if ( comboSet == null ) {
			setSize( 0, 0 );
		} else {
			setSize( 260, 260 );
		}
		
		setPreferredSize( getSize() );
	}

	public void forceComboSelection ( int i ) {
		selectValue( i );
	}
}
