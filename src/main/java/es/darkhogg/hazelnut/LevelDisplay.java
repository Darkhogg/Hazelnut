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
package es.darkhogg.hazelnut;

import es.darkhogg.crazycastle.ComboInfoMap;
import es.darkhogg.crazycastle.ComboType;
import es.darkhogg.crazycastle.Enemy;
import es.darkhogg.crazycastle.EnemyType;
import es.darkhogg.crazycastle.Item;
import es.darkhogg.crazycastle.Level;
import es.darkhogg.crazycastle.LevelWarningProcessor;
import es.darkhogg.gameboy.Palette;
import es.darkhogg.gameboy.Sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;

public final class LevelDisplay extends JComponent {

	static private final Color COLOR_SOLID = new Color(1f, .4f, .8f, .5f);

  static private final Image IMG_WARNING = Toolkit.getDefaultToolkit().getImage(
    EditorFrame.class.getResource("/es/darkhogg/hazelnut/warning.png")
  );

	private Level level;
	private List<Image> comboSet;
	private Map<ComboType,Image> helpSet;
	private Image bugsImage;
	private List<Image> enemySet;
	private List<Image> itemSet;
	private List<Image> doorItemSet;
	private ComboInfoMap comboInfo;

	private boolean displaySpawn;
	private boolean displayItems;
	private boolean displayEnemies;
	private boolean displayDoorItems;
	private boolean displayComboTypes;
	private boolean displayComboColl;
	private boolean displayWarnings;

	private int scale = 1;
	private boolean showGrid;

	private boolean lftBtn, cntBtn, rgtBtn;

	private final Set<EditListener> editListeners = new HashSet<EditListener>();

	public LevelDisplay() {
		super();
		setLevel(null);
		setComboSet(null, null);
    setToolTipText("");

		lftBtn = false;
		cntBtn = false;
		rgtBtn = false;

		this.addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent me) {
				final int SEP = showGrid ? 17 : 16;

				int x = (int)( me.getX() / (SEP * scale) );
				int y = (int)( me.getY() / (SEP * scale) );

				int but = me.getButton();
				if (but == MouseEvent.BUTTON1) {
					lftBtn = true;
				}
				if (but == MouseEvent.BUTTON2) {
					cntBtn = true;
				}
				if (but == MouseEvent.BUTTON3) {
					rgtBtn = true;
				}

				if (level != null
				 && x >= 0 && x < level.getRomLevel().getSize().getX()
				 && y >= 0 && y < level.getRomLevel().getSize().getY()
				) {

					for (EditListener el : editListeners) {
						if (lftBtn) {
							el.leftPressed(x, y);
						}
						if (cntBtn) {
							el.centerPressed(x, y);
						}
						if (rgtBtn) {
							el.rightPressed(x, y);
						}
					}
				}
			}
			@Override public void mouseReleased(MouseEvent me) {
				int but = me.getButton();
				if (but == MouseEvent.BUTTON1) {
					lftBtn = false;
				}
				if (but == MouseEvent.BUTTON2) {
					cntBtn = false;
				}
				if (but == MouseEvent.BUTTON3) {
					rgtBtn = false;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter(){
			private int lastX = -1;
			private int lastY = -1;

			@Override public void mouseDragged(MouseEvent me) {
				final int SEP = showGrid ? 17 : 16;

				int x = (int)( me.getX() / (SEP * scale) );
				int y = (int)( me.getY() / (SEP * scale) );

				if (level != null
				 && (x != lastX || y != lastY)
				 && x >= 0 && x < level.getRomLevel().getSize().getX()
				 && y >= 0 && y < level.getRomLevel().getSize().getY()
				) {
					lastX = x;
					lastY = y;
					//int but = me.getButton();
					for (EditListener el : editListeners) {
						if (lftBtn) {
							el.leftDragged(x, y);
						}
						if (cntBtn) {
							el.centerDragged(x, y);
						}
						if (rgtBtn) {
							el.rightDragged(x, y);
						}
					}
				}
			}
		});
	}

	public void setComboInfoMap(ComboInfoMap comboInfo) {
		this.comboInfo = comboInfo;

		repaint();
	}

	public void setLevel(Level level) {
		this.level = level;

		updateSize();
		repaint();
	}

	public void setComboSet(List<Sprite> spriteset, Palette pal) {
		if ( spriteset == null ) {
			comboSet = null;
		} else {
			comboSet = new ArrayList<Image>();
			for ( Sprite spr : spriteset ) {
				comboSet.add(spr.asImage(pal));
			}
		}

		repaint();
	}

	public void setItemSet(List<Sprite> spriteset, Palette palItems, Palette palDoorItems) {
		if ( spriteset == null ) {
			itemSet = null;
			doorItemSet = null;
		} else {
			itemSet = new ArrayList<Image>();
			doorItemSet = new ArrayList<Image>();

			for ( Sprite spr : spriteset ) {
				itemSet.add(spr.asImage(palItems));
				doorItemSet.add(spr.asImage(palDoorItems));
			}
		}

		repaint();
	}

	public void setEnemySet(
		List<Sprite> spriteset, Palette light, Palette dark
	) {
		if ( spriteset == null ) {
			enemySet = null;
		} else {
			final var enemySetArr = new Image[EnemyType.values().length];
			for (final var enemyType : EnemyType.values()) {
				enemySetArr[enemyType.getIndex()] = spriteset
					.get(enemyType.getIndex())
					.asImage(enemyType.isDark() ? dark : light);
			}
			enemySet = Arrays.asList(enemySetArr);
		}

		repaint();
	}

	public void setHelpSet(Map<ComboType, Image> imageset) {
		this.helpSet = imageset;

		repaint();
	}

	public void setBugsSprite(Sprite sprite, Palette pal) {
		if ( sprite == null ) {
			bugsImage = null;
		} else {
			bugsImage = sprite.asImage(pal);
		}

		repaint();
	}

	@Override
	public void paintComponent(Graphics gr) {
		super.paintComponent(gr);

		if ( level != null && comboSet != null ) {
      final var w = (int)this.getSize().getWidth();
      final var h = (int)this.getSize().getHeight();

      if (showGrid) {
        gr.setColor(Color.GRAY);
        gr.fillRect(0, 0, w, h);
      }

			final var comboSize = (int)(16 * scale);
			final var gridSize = showGrid ? 1 : 0;

			final var rl = level.getRomLevel();
			final var grid = rl.getGrid();

			// Display the background, combo help and collision
			for (var i = 0; i < rl.getSize().getX(); i++) {
				for (var j = 0; j < rl.getSize().getY(); j++) {
          final var x = 1 + i * (comboSize + gridSize);
          final var y = 1 + j * (comboSize + gridSize);

					final var val = ((int)(grid.get(i, j))) & 0xFF;

					//g.setComposite(alphaFull);
					gr.drawImage(
						comboSet.get(val),
						x, y, comboSize, comboSize,
            null
					);

					final var ct = comboInfo.getComboType((byte)val);
					final var cc = comboInfo.getComboColl((byte)val);

					if (displayComboColl) {
						if (cc.isSolid()) {
							gr.setColor(COLOR_SOLID);
							gr.fillRect(x, y, comboSize, comboSize);
						}
					}

					if (displayComboTypes && helpSet != null) {
						if (ct != null) {
							final var im = helpSet.get(ct);
							if (im != null) {
								//g.setComposite(alphaFull);
								gr.drawImage(im, x, y, comboSize, comboSize, null);
							}
						}
					}
				}
			}

			// Display the items
			if ( displayItems && itemSet != null ) {
				for ( Item it : rl.getItems() ) {
					final var val = it.getType().getIndex();
          final var img = itemSet.get(val);
          drawGridImage(gr, img, it.getX(), it.getY());
				}
			}

			// Display the enemies
			if ( displayEnemies && enemySet != null ) {
				for ( Enemy en : rl.getEnemies() ) {
					final var val = en.getType().getIndex();
          final var img = enemySet.get(val);
          drawGridImage(gr, img, en.getX(), en.getY());
				}
			}

			// Display the door items
			if ( displayDoorItems && doorItemSet != null ) {
				for ( Item it : rl.getDoorItems() ) {
					final var val = it.getType().getIndex();
          final var img = doorItemSet.get(val);
          drawGridImage(gr, img, it.getX(), it.getY());
				}
			}

			// Display the spawn
			if ( displaySpawn && bugsImage != null ) {
        final var sp = rl.getSpawn();
        drawGridImage(gr, bugsImage, sp.getX(), sp.getY());
			}

      // Display the warnings
      if (IMG_WARNING != null && displayWarnings) {
        final var warnings = LevelWarningProcessor.INSTANCE.getWarnings(level, comboInfo);
        for (final var warning : warnings) {
          drawGridImage(gr, IMG_WARNING, warning.position().getX(), warning.position().getY());
        }
      }
		}
	}

  @Override
  public String getToolTipText(MouseEvent event) {
    final var lwp = LevelWarningProcessor.INSTANCE;
    final var warnings = lwp.getWarnings(level, comboInfo);

    final var x = (showGrid ? ((event.getX() - 1) / 17) : (event.getX() / 16)) / scale;
    final var y = (showGrid ? ((event.getY() - 1) / 17) : (event.getY() / 16)) / scale;

    StringBuilder sb = null;
    for (final var warning : warnings) {
      if (warning.position().getX() == x && warning.position().getY() == y) {
        if (sb == null) {
          sb = new StringBuilder();
        } else {
          sb.append("\n");
        }
        sb.append(lwp.getWarningString(warning));
      }
    }

    return sb == null ? null : sb.toString();
  }

  private void drawGridImage(Graphics gr, Image img, int gridX, int gridY) {
    final var cs = 16 * scale;
    final var gs = showGrid ? 1 : 0;

    final var w = (int)img.getWidth(null);
    final var h = (int)img.getHeight(null);

    final var x = 1 + (gridX * (cs+gs)) - ((Math.max(0, w - 16) / 2) * scale);
    final var y = 1 + (gridY * (cs+gs)) - (Math.max(0, h - 16) * scale);

    gr.drawImage(img, x, y, w * scale, h * scale, null);
  }

	public void setDisplayFlags(
		boolean spawn, boolean items, boolean enemies,
		boolean doorItems, boolean comboTypes, boolean comboCollision,
    boolean warnings
	) {
		displaySpawn = spawn;
		displayItems = items;
		displayEnemies = enemies;
		displayDoorItems = doorItems;
		displayComboTypes = comboTypes;
		displayComboColl = comboCollision;
    displayWarnings = warnings;

		repaint();
	}

	public void setScale(int scale) {
		this.scale = scale;

		updateSize();
		repaint();
	}

	public void setShowGrid(boolean grid) {
		this.showGrid = grid;

		updateSize();
		repaint();
	}

	public void updateSize() {
		if ( level == null ) {
			setSize(1, 1);
		} else {
			final var lvlSize = level.getRomLevel().getSize();
      final var cs = scale * 16;
      final var gs = showGrid ? 1 : 0;

			setSize(
				gs + (int)(lvlSize.getX() * (cs + gs)),
				gs + (int)(lvlSize.getY() * (cs + gs))
			);
		}

		setPreferredSize(getSize());
	}

	protected List<Image> getComboSet() {
		return comboSet;
	}
	protected List<Image> getItemSet() {
		return itemSet;
	}
	protected List<Image> getDoorItemSet() {
		return doorItemSet;
	}
	protected List<Image> getEnemySet() {
		return enemySet;
	}
	protected Map<ComboType, Image> getHelpSet() {
		return helpSet;
	}
	protected Image getSpawnImage() {
		return bugsImage;
	}

	public void addEditListener(EditListener el) {
		editListeners.add(el);
	}
}
