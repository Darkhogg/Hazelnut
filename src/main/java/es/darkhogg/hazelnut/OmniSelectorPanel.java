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

import es.darkhogg.crazycastle.ComboCollision;
import es.darkhogg.crazycastle.ComboInfoMap;
import es.darkhogg.crazycastle.EnemyType;
import es.darkhogg.crazycastle.ItemType;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public final class OmniSelectorPanel extends JPanel implements Selector {

	protected ComboSelector comboSelector;
	protected EntitySelector entitySelector;

	private final JLabel selectionLabel;

	private ComboInfoMap comboInfoMap;

	private final Set<Selector.SelectionChangedListener> changeListeners = new HashSet<>();
	private boolean changeInhibited;

	public OmniSelectorPanel() {
		setOpaque(false);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));


		comboSelector = new ComboSelector();
		comboSelector.addSelectionChangedListener(evt -> onSubSelectionChanged(evt));

    JScrollPane comboScroll = new JScrollPane();
    final var dim = new Dimension(
      (int)(comboSelector.getMinimumSize().getWidth() + comboScroll.getVerticalScrollBar().getPreferredSize().getWidth()),
      (int)(comboSelector.getMinimumSize().getHeight() + comboScroll.getVerticalScrollBar().getPreferredSize().getHeight())
    );
    comboScroll.setPreferredSize(dim);
		comboScroll.setBorder(new TitledBorder(null, "Combos", TitledBorder.LEADING, TitledBorder.TOP));
    comboScroll.getHorizontalScrollBar().setUnitIncrement(8);
    comboScroll.getVerticalScrollBar().setUnitIncrement(8);
		comboScroll.setViewportView(comboSelector);

		final var scrollCenterer = new JPanel();
		scrollCenterer.setLayout(new GridBagLayout());
		comboScroll.setViewportView(scrollCenterer);
    	scrollCenterer.add(comboSelector);

		entitySelector = new EntitySelector();
		entitySelector.setOpaque(false);
		entitySelector.addSelectionChangedListener(evt -> onSubSelectionChanged(evt));

		final var selectionPanel = new JPanel();
		selectionPanel.setOpaque(false);
		selectionPanel.setLayout(new GridBagLayout());
		selectionPanel.setPreferredSize(new Dimension(260, 72));
		selectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));
		selectionPanel.setBorder(new TitledBorder(null, "Selected", TitledBorder.LEADING, TitledBorder.TOP));

		selectionLabel = new JLabel("");
		selectionLabel.setAlignmentX(0f);
		selectionLabel.setAlignmentY(.5f);
		selectionLabel.setIconTextGap(8);
		selectionPanel.add(selectionLabel);

		add(selectionPanel);
		add(comboScroll);
		add(entitySelector);

		updateSelectionLabel();
	}

	@Override
	public SelectionType getSelectedType() {
		final var comboType = comboSelector.getSelectedType();
		if (comboType != null) {
			return comboType;
		}

		final var entityType = entitySelector.getSelectedType();
		if (entityType != null) {
			return entityType;
		}

		return null;
	}

	@Override
	public Object getSelectedObject() {
		final var comboType = comboSelector.getSelectedType();
		if (comboType != null) {
			return comboSelector.getSelectedObject();
		}

		final var entityType = entitySelector.getSelectedType();
		if (entityType != null) {
			return entitySelector.getSelectedObject();
		}

		return null;
	}

	@Override
	public Image getSelectionImage() {
		final var comboType = comboSelector.getSelectedType();
		if (comboType != null) {
			return comboSelector.getSelectionImage();
		}

		final var entityType = entitySelector.getSelectedType();
		if (entityType != null) {
			return entitySelector.getSelectionImage();
		}

		return null;
	}

	public String getSelectionLabel() {
		final var type = getSelectedType();
		final var obj = getSelectedObject();

		if (type == null) {
			return "";
		}

		switch (type) {
			case COMBO: {
				final var cb = (Byte)obj;
				final var ci = cb.intValue() & 0xFF;
				final var ct = comboInfoMap.getComboType(cb.byteValue());
				final var cc = comboInfoMap.getComboColl(cb.byteValue());
				final var ccStr = cc == null ? "?" : cc == ComboCollision.SOLID ? "S" : "-";
				return "COMBO "
					+ "(" + String.format("$%02X", ci) + ") "
					+ "[" + ccStr + ":" + (ct == null ? "?" : ct) + "]";
			}
			case SPAWN:
				return "SPAWN";
			case ITEM: {
				final var it = (ItemType)obj;
				return "ITEM " + it + " (" + String.format("$%02X", it.getValue()) + ")";
			}
			case DOOR_ITEM: {
				final var it = (ItemType)obj;
				return "D.ITEM " + it + " (" + String.format("$%02X", ((ItemType)obj).getValue()) + ")";
			}
			case ENEMY: {
				final var et = (EnemyType)obj;
				return "ENEMY " + et + " (" + String.format("$%02X", ((EnemyType)obj).getValue()) + ")";
			}
		}

		return null;
	}

	public void setSelection(SelectionType type, Object obj) {
		if (type == null) {
			comboSelector.setSelectedObject(null);
			entitySelector.setSelection(null, null);
		} else if (type == SelectionType.COMBO) {
			comboSelector.setSelectedObject(obj);
			entitySelector.setSelection(null, null);
		} else {
			entitySelector.setSelection(type, obj);
			comboSelector.setSelectedObject(null);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		comboSelector.setEnabled(enabled);
		entitySelector.setEnabled(enabled);
	}

	public void setComboInfoMap(ComboInfoMap comboInfoMap) {
			this.comboInfoMap = comboInfoMap;
	}

	private void onSubSelectionChanged(SelectionChangedEvent evt) {
		if (changeInhibited) {
			return;
		}

		changeInhibited = true;
		setSelection(evt.type, evt.object);
		changeInhibited = false;

		updateSelectionLabel();

		final var outEvt = new SelectionChangedEvent(getSelectedType(), getSelectedObject());
		for (final var listener : changeListeners) {
			listener.onSelectionChanged(outEvt);
		}
	}

	@Override
	public void addSelectionChangedListener(SelectionChangedListener listener) {
		changeListeners.add(listener);
	}

	@Override
	public void removeSelectionChangedListener(SelectionChangedListener listener) {
		changeListeners.remove(listener);
	}

	private void updateSelectionLabel() {
		final var image = getSelectionImage();
		selectionLabel.setIcon(image == null ? null : new ImageIcon(image));
		selectionLabel.setText(getSelectionLabel());
	}
}
