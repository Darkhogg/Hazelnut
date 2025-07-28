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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.swing.JComponent;

public final class ComboSelector extends JComponent implements Selector {
	static private final Color COLOR_GRID = Color.GRAY;
	static private final int SIZE = 17*16 + 2;

	static private final Color COLOR_SOLID = new Color(1f, .4f, .8f, .5f);

	private ComboInfoMap comboInfo;

	private List<Image> comboSet;
	private Map<ComboType,Image> helpSet;
	private List<Image> mixedSet;

	private boolean displayComboType;
	private boolean displayComboColl;

	private Byte selObj;

	private final Set<Selector.SelectionChangedListener> changeListeners = new HashSet<>();

	public ComboSelector() {
		updateSize();

		addMouseListener(new MouseAdapter(){
			@Override public void mousePressed(MouseEvent me) {
				if (comboSet != null
				 && me.getX() >= 0 && me.getX() < 256
				 && me.getY() >= 0 && me.getY() < 256
				) {
					final var selectedIndex = me.getX() / 17 + (me.getY() / 17) * 16;
					setSelectedObject(Byte.valueOf((byte)selectedIndex));
				}
			}
		});
	}

	public void setComboInfoMap(ComboInfoMap comboInfo) {
		this.comboInfo = comboInfo;
	}

	public void setComboSet(List<Image> comboset) {
		this.comboSet = comboset;

		updateSize();
		generateMixedSet();
		repaint();
	}

	public void setDisplayComboType(boolean display) {
		displayComboType = display;

		updateSize();
		generateMixedSet();
		repaint();
	}

	public void setDisplayComboCollision(boolean display) {
		displayComboColl = display;

		updateSize();
		generateMixedSet();
		repaint();
	}

	public void setHelpSet(Map<ComboType, Image> comboset) {
		this.helpSet = comboset;

		updateSize();
		generateMixedSet();
		repaint();
	}

	private void generateMixedSet() {
		if ( comboSet != null && helpSet != null ) {
			mixedSet = new ArrayList<Image>();

			for (int i = 0; i < 256; i++) {
				Image img = new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB );
				Graphics g = img.getGraphics();

				g.drawImage(comboSet.get(i), 0, 0, null);
				if (helpSet != null && displayComboType) {
					ComboType ct = comboInfo.getComboType((byte)i);
					if (ct != null) {
						Image im = helpSet.get(ct);
						if (im != null) {
							g.drawImage(im, 0, 0, null);
						}
					}
				}

				mixedSet.add(img);
			}
		}
	}

	public void setSelectedObject(Object value) {
		if (value != null && !(value instanceof Byte)) {
			throw new IllegalArgumentException("invalid value: " + value);
		}

		final var changed = !Objects.equals(selObj, value);
		selObj = (Byte)value;

		if (changed) {
			for (final var listener : changeListeners) {
				listener.onSelectionChanged(new SelectionChangedEvent(value == null ? null : SelectionType.COMBO, value));
			}
		}

		repaint();
	}

	@Override
	public SelectionType getSelectedType() {
		return selObj == null ? null : SelectionType.COMBO;
	}

	@Override
	public Byte getSelectedObject() {
		return selObj;
	}

	@Override
	public Image getSelectionImage() {
		return mixedSet.get(selObj.intValue() & 0xFF);
	}

	@Override
	public void addSelectionChangedListener(SelectionChangedListener listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(SelectionChangedListener listener) {
		changeListeners.remove(listener);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (comboSet == null) {
			return;
		}

		g.setColor(COLOR_GRID);
		g.fillRect(1, 1, SIZE - 1, SIZE - 1);

		for (int i = 0; i < 256; i++) {
			final var x = (i % 16) * 17 + 2;
			final var y = (i / 16) * 17 + 2;

			g.drawImage(comboSet.get(i), x, y, null);

			final var ct = comboInfo.getComboType((byte)i);
			final var cc = comboInfo.getComboColl((byte)i);

			if (displayComboColl) {
				if (cc.isSolid()) {
					g.setColor(COLOR_SOLID);
					g.fillRect(x, y, 16, 16);
				}
			}

			if (displayComboType && helpSet != null) {
				if (ct != null) {
					Image im = helpSet.get(ct);
					if (im != null) {
						g.drawImage(im, x, y, null);
					}
				}
			}
		}

		if ( selObj != null ) {
			final var selVal = selObj.intValue() & 0xFF;
			int x = (selVal%16) * 17 + 2;
			int y = (selVal/16) * 17 + 2;
			g.setColor(Color.RED);
			g.drawRect(x - 1, y - 1, 16, 16);
			g.drawRect(x - 2, y - 2, 18, 18);
		}

	}

	private void updateSize() {
		setSize(SIZE, SIZE);
		setPreferredSize(getSize());
	}
}
