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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import es.darkhogg.bbcc2.ComboType;
import es.darkhogg.bbcc2.Enemy;
import es.darkhogg.bbcc2.Item;
import es.darkhogg.bbcc2.Level;
import es.darkhogg.bbcc2.RomLevel;
import es.darkhogg.gameboy.Palette;
import es.darkhogg.gameboy.Sprite;

@SuppressWarnings( "serial" )
public final class LevelDisplay extends JComponent {

	private Level level;
	private List<Image> comboSet;
	private Map<ComboType,Image> helpSet;
	private Image bugsImage;
	private List<Image> enemySet;
	private List<Image> itemSet;
	private List<Image> doorItemSet;
	private boolean displaySpawn;
	private boolean displayItems;
	private boolean displayEnemies;
	private boolean displayDoorItems;
	private boolean displayHelp;
	private double scale = 1;
	
	private boolean lftBtn, cntBtn, rgtBtn; 
	
	private Set<EditListener> editListeners = new HashSet<EditListener>();
	
	public LevelDisplay () {
		super();
		setLevel( null );
		setComboSet( null, null );
		
		lftBtn = false;
		cntBtn = false;
		rgtBtn = false;
		
		this.addMouseListener( new MouseAdapter(){
			@Override public void mousePressed ( MouseEvent me ) {
				int x = (int)( me.getX() / (16*scale) );
				int y = (int)( me.getY() / (16*scale) );
				
				int but = me.getButton();
				if ( but == MouseEvent.BUTTON1 ) {
					lftBtn = true;
				}
				if ( but == MouseEvent.BUTTON2 ) {
					cntBtn = true;
				}
				if ( but == MouseEvent.BUTTON3 ) {
					rgtBtn = true;
				}
				
				if ( level != null
				 && x >= 0 && x < level.getRomLevel().getSize().getX()
				 && y >= 0 && y < level.getRomLevel().getSize().getY()
				) {

					for ( EditListener el : editListeners ) {
						if ( lftBtn ) {
							el.leftPressed( x, y );
						}
						if ( cntBtn ) {
							el.centerPressed( x, y );
						}
						if ( rgtBtn ) {
							el.rightPressed( x, y );
						}
					}
				}
			}
			@Override public void mouseReleased ( MouseEvent me ) {
				int but = me.getButton();
				if ( but == MouseEvent.BUTTON1) {
					lftBtn = false;
				}
				if ( but == MouseEvent.BUTTON2) {
					cntBtn = false;
				}
				if ( but == MouseEvent.BUTTON3 ) {
					rgtBtn = false;
				}
			}
		});
		
		this.addMouseMotionListener( new MouseMotionAdapter(){
			private int lastX = -1;
			private int lastY = -1;
			
			@Override public void mouseDragged ( MouseEvent me ) {
				int x = (int)( me.getX() / (16*scale) );
				int y = (int)( me.getY() / (16*scale) );
				
				if ( level != null
				 && ( x != lastX || y != lastY )
				 && x >= 0 && x < level.getRomLevel().getSize().getX()
				 && y >= 0 && y < level.getRomLevel().getSize().getY()
				) {
					lastX = x;
					lastY = y;
					//int but = me.getButton();
					for ( EditListener el : editListeners ) {
						if ( lftBtn ) {
							el.leftDragged( x, y );
						}
						if ( cntBtn ) {
							el.centerDragged( x, y );
						}
						if ( rgtBtn ) {
							el.rightDragged( x, y );
						}
					}
				}
			}
		});
	}
	
	public void setLevel ( Level level ) {
		this.level = level;
		
		updateSize();
		repaint();
	}
	
	public void setComboSet ( List<Sprite> spriteset, Palette pal ) {
		if ( spriteset == null ) {
			comboSet = null;
		} else {
			comboSet = new ArrayList<Image>();
			for ( Sprite spr : spriteset ) {
				comboSet.add( spr.asImage( pal ) );
			}
		}
		
		repaint();
	}
	
	public void setItemSet ( List<Sprite> spriteset, Palette pal1, Palette pal2 ) {
		if ( spriteset == null ) {
			itemSet = null;
			doorItemSet = null;
		} else {
			itemSet = new ArrayList<Image>();
			doorItemSet = new ArrayList<Image>();
			
			for ( Sprite spr : spriteset ) {
				itemSet.add( spr.asImage( pal1 ) );
				doorItemSet.add( spr.asImage( pal2 ) );
			}
		}
		
		repaint();
	}
	
	public void setEnemySet (
		List<Sprite> spriteset, Palette light, Palette dark
	) {
		if ( spriteset == null ) {
			enemySet = null;
		} else {
			enemySet = new ArrayList<Image>();
			enemySet.add( spriteset.get( 0 ).asImage( light ) );

			enemySet.add( spriteset.get( 1 ).asImage( light ) );
			enemySet.add( spriteset.get( 1 ).asImage( dark )  );
			
			enemySet.add( spriteset.get( 2 ).asImage( light )  );
			enemySet.add( spriteset.get( 2 ).asImage( dark )  );
			
			enemySet.add( spriteset.get( 3 ).asImage( light )  );
			enemySet.add( spriteset.get( 4 ).asImage( light )  );
			enemySet.add( spriteset.get( 5 ).asImage( light )  );
			
		}
		
		repaint();
	}
	
	public void setHelpSet ( Map<ComboType,Image> imageset ) {
		this.helpSet = imageset;
		
		repaint();
	}
	
	public void setBugsSprite ( Sprite sprite, Palette pal ) {
		if ( sprite == null ) {
			bugsImage = null;
		} else {
			bugsImage = sprite.asImage( pal );
		}

		repaint();
	}
	
	@Override
	public void paintComponent ( Graphics gr ) {
		super.paintComponent( gr );
		
		if ( level != null && comboSet != null ) {
			RomLevel rl = level.getRomLevel();
			byte[][] data = rl.getData();
			
			Image img = new BufferedImage(
				getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB );
			Graphics g = img.getGraphics();
			
			// Display the background & combo help
			for ( int i = 0; i < rl.getSize().getX(); i++ ) {
				for ( int j = 0; j < rl.getSize().getY(); j++ ) {
					int val = ((int)(data[i][j])) & 0xFF;
					g.drawImage(
						comboSet.get( val ),
						i*16, j*16, null
					);
					
					if ( displayHelp && helpSet != null ) {
						ComboType ct = ComboType.valueOf( (byte)val );
						if ( ct != null ) {
							Image im = helpSet.get( ct );
							if ( im != null ) {
								g.drawImage( im, i*16, j*16, null );
							}
						}
					}
				}
			}
			
			// Display the items
			if ( displayItems && itemSet != null ) {
				for ( Item it : rl.getItems() ) {
					int val = it.getType().getValue() & 0xFF;
					g.drawImage( 
						itemSet.get( val ),
						it.getX()*16, it.getY()*16, null
					);
				}
			}
			
			// Display the enemies
			if ( displayEnemies && enemySet != null ) {
				for ( Enemy en : rl.getEnemies() ) {
					int val = en.getType().getValue() & 0xFF;
					g.drawImage( 
						enemySet.get( val ),
						en.getX()*16, en.getY()*16-8, null
					);
				}
			}
			
			// Display the door items
			if ( displayDoorItems && doorItemSet != null ) {
				for ( Item it : rl.getDoorItems() ) {
					int val = it.getType().getValue() & 0xFF;
					g.drawImage( 
						doorItemSet.get( val ),
						it.getX()*16, it.getY()*16, null
					);
				}
			}
			
			// Display the spawn
			if ( displaySpawn && bugsImage != null ) {
				g.drawImage( bugsImage, rl.getSpawn().getX()*16,
					rl.getSpawn().getY()*16-8, null
				);
			}
			
			gr.drawImage( 
				img, 0, 0,
				(int)( img.getWidth( null )*scale ),
				(int)( img.getHeight( null )*scale ),
				null
			);
		}
	}
	
	public void setDisplayFlags( 
		boolean spawn, boolean items, boolean enemies,
		boolean doorItems, boolean help
	) {
		displaySpawn = spawn;
		displayItems = items;
		displayEnemies = enemies;
		displayDoorItems = doorItems;
		displayHelp = help;
		
		repaint();
	}

	public void setScale ( double scale ) {
		this.scale = scale;
		
		updateSize();
		repaint();
	}
	
	private void updateSize () {
		if ( level == null ) {
			setSize( (int)( 16*scale ), (int)( 16*scale ) );
		} else {
			setSize(
				(int)( level.getRomLevel().getSize().getX()*16*scale ),
				(int)( level.getRomLevel().getSize().getY()*16*scale )
			);
		}
		
		setPreferredSize( getSize() );
	}

	protected List<Image> getComboSet () {
		return comboSet;
	}
	protected List<Image> getItemSet () {
		return itemSet;
	}
	protected List<Image> getDoorItemSet () {
		return doorItemSet;
	}
	protected List<Image> getEnemySet () {
		return enemySet;
	}
	protected Map<ComboType,Image> getHelpSet () {
		return helpSet;
	}
	protected Image getSpawnImage () {
		return bugsImage;
	}
	
	public void addEditListener ( EditListener el ) {
		editListeners.add( el );
	}
}
