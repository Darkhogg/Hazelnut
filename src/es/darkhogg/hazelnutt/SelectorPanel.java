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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

@SuppressWarnings( "serial" )
public final class SelectorPanel extends JPanel {
	
	protected ComboSelector comboSelector;
	protected EntitySelector entitySelector;

	private SelectionType selType = SelectionType.COMBO;
	private Object selObj;
	//private Image selImg;
	
	private JLabel selectionLabel;
	
	public SelectorPanel () {
		setOpaque(false);
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		
		JPanel comboSelectPanel = new JPanel();
		comboSelectPanel.setOpaque(false);
		tabbedPane.addTab("Combos", new ImageIcon(SelectorPanel.class.getResource("/es/darkhogg/hazelnutt/icon_edit_combo.png")), comboSelectPanel, null);
		comboSelectPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(UIManager.getColor("TabbedPane.light"));
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPane.setViewportBorder(null);
		scrollPane.setOpaque(false);
		comboSelectPanel.add(scrollPane, BorderLayout.CENTER);
		
		comboSelector = new ComboSelector(this);
		scrollPane.setViewportView(comboSelector);
		
		entitySelector = new EntitySelector(this);
		entitySelector.setPreferredSize(new Dimension(280, 10));
		entitySelector.setOpaque(false);
		tabbedPane.addTab("Entities", new ImageIcon(SelectorPanel.class.getResource("/es/darkhogg/hazelnutt/icon_edit_entity.png")), entitySelector, null);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 32));
		panel.setOpaque(false);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblSelected = new JLabel("Selected:");
		lblSelected.setPreferredSize(new Dimension(80, 14));
		lblSelected.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(lblSelected);
		
		selectionLabel = new JLabel("");
		panel.add(selectionLabel);

	}

	public SelectionType getSelectionType () {
		return selType;
	}
	
	public Object getSelectedObject () {
		return selObj;
	}
	
	protected void setSelectionType ( SelectionType selType ) {
		this.selType = selType;
		
		if ( selType == SelectionType.COMBO ) {
			entitySelector.actionSelect( null );
		} else {
			comboSelector.forceComboSelection( -1 );
		}
	}
	
	protected void setSelectionObject ( Object selObj ) {
		this.selObj = selObj;
	}
	
	protected void setSelectionImage ( Image selImg ) {
		//this.selImg = selImg;
		
		selectionLabel.setIcon( new ImageIcon( selImg ) );
	}

	
	public void forceComboSelection ( byte b ) {
		comboSelector.forceComboSelection( ((int)b) & 0xFF );
		repaint();
	}
	
	@Override
	public void setEnabled ( boolean enabled ) {
		super.setEnabled( enabled );
		
		comboSelector.setEnabled( enabled );
		entitySelector.setEnabled( enabled );
	}
}
