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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import es.darkhogg.bbcc2.EnemyGroup;
import es.darkhogg.bbcc2.Level;
import es.darkhogg.bbcc2.RomLevel;
import es.darkhogg.bbcc2.Theme;
import es.darkhogg.bbcc2.Weapon;
import es.darkhogg.util.IntVector;

@SuppressWarnings( "serial" )
public class PropertiesPanel extends JPanel {
	private JTextField oldArea;
	private JSpinner sizeX;
	private JSpinner sizeY;
	private JTextField newArea;
	private JButton applyButton;
	private JButton resetButton;
	
	//private EditorFrame mainFrame;
	private Level level;
	private JTextField passField;
	private JComboBox enemyGroupCombo;
	private JComboBox weaponCombo;
	private JComboBox themeCombo;

	private boolean hasChanged;
	
	private final Collection<ApplyListener> listeners =
		new HashSet<ApplyListener>();
	
	public static interface ApplyListener {
		public abstract void apply ();
	}

	public PropertiesPanel () {
		this( null );
	}
	
	/**
	 * Create the panel.
	 */
	public PropertiesPanel ( Level level ) {
		
		JLabel labelEnemyGroup = new JLabel("Enemy Group:");
		
		JLabel labelPass = new JLabel("Password:");
		
		JLabel labelArea = new JLabel("Level Area:");
		
		JLabel labelSize = new JLabel("Level Size:");
		
		JLabel labelTheme = new JLabel("Theme:");
		
		JLabel labelWeapon = new JLabel("Weapon:");
		
		newArea = new JTextField();
		newArea.setEditable(false);
		newArea.setColumns(3);
		
		sizeX = new JSpinner();
		sizeX.setModel(new SpinnerNumberModel(1, 1, 255, 1));
		sizeX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				changedSize();
			}
		});
		
		oldArea = new JTextField();
		oldArea.setEditable(false);
		oldArea.setColumns(10);
		
		sizeY = new JSpinner();
		sizeY.setModel(new SpinnerNumberModel(1, 1, 255, 1));
		sizeY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				changedSize();
			}
		});
		
		passField = new JTextField();
		passField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate ( DocumentEvent arg0 ) {
				changed();
			}
			@Override public void insertUpdate ( DocumentEvent arg0 ) {
				changed();
			}
			@Override public void removeUpdate ( DocumentEvent arg0 ) {
				changed();
			}
		});
		passField.setColumns(4);
		
		enemyGroupCombo = new JComboBox();
		enemyGroupCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changed();
			}
		});
		enemyGroupCombo.setModel(new DefaultComboBoxModel(new String[] {"Daffy's Fowl Fighters", "Yosemite's Gunners", "Sylvester's Munchers", "Boss"}));
		
		themeCombo = new JComboBox();
		themeCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changed();
			}
		});
		themeCombo.setModel(new DefaultComboBoxModel(new String[] {"Chamber of Chaos", "Dungeon of Doom", "Power Tower", "Luxurious Lounge", "Grand Guard House", "Perilous Prison", "Playhouse Palace"}));
		
		weaponCombo = new JComboBox();
		weaponCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				changed();
			}
		});
		weaponCombo.setModel(new DefaultComboBoxModel(new String[] {"Bow & Arrows", "Bombs"}));
		
		applyButton = new JButton("Apply Changes");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionApply();
			}
		});
		applyButton.setEnabled(false);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionReset();
			}
		});
		resetButton.setEnabled(false);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(applyButton, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(resetButton, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(labelPass, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addGap(28)
							.addComponent(passField, GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(labelEnemyGroup, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(enemyGroupCombo, 0, 211, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(labelTheme, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addGap(42)
							.addComponent(themeCombo, 0, 211, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(labelWeapon, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addGap(34)
							.addComponent(weaponCombo, 0, 211, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(labelSize, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
								.addComponent(labelArea, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
							.addGap(24)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(newArea, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
								.addComponent(sizeX, GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(sizeY, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
								.addComponent(oldArea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(labelSize))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(sizeX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(sizeY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(labelArea))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(newArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(oldArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(9)
							.addComponent(labelPass))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(passField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelEnemyGroup)
						.addComponent(enemyGroupCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(9)
							.addComponent(labelTheme))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(themeCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(9)
							.addComponent(labelWeapon))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addComponent(weaponCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(applyButton)
						.addComponent(resetButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(0))
		);
		setLayout(groupLayout);

		
		// Perform actual initialization
		//mainFrame = main;
		setLevel( level );
	}
	
	public void setLevel ( Level level ) {
		this.level = level;
		
		if ( level == null ) {
			setEnabled( false );
		} else {
			setEnabled( true );
			actionReset();
		}
	}
	
	public Level getLevel () {
		return level;
	}
	
	@Override
	public void setEnabled ( boolean enabled ) {
		super.setEnabled( enabled );
		
		sizeX.setEnabled( enabled );
		sizeY.setEnabled( enabled );
		
		oldArea.setEnabled( enabled );
		newArea.setEnabled( enabled );
		
		passField.setEnabled( enabled );
		
		enemyGroupCombo.setEnabled( enabled );
		themeCombo.setEnabled( enabled );
		weaponCombo.setEnabled( enabled );
		
		applyButton.setEnabled( enabled );
		resetButton.setEnabled( enabled );
	}
	
	protected void actionReset () {
		// Update the size fields (implicit newArea update)
		sizeX.setValue( Integer.valueOf(
			level.getRomLevel().getSize().getX() ) );
		sizeY.setValue( Integer.valueOf(
			level.getRomLevel().getSize().getY() ) );
		
		// Update the old area field
		oldArea.setText( String.valueOf(
			level.getRomLevel().getSize().getX() *
			level.getRomLevel().getSize().getY()
		) );
		
		// Update the password
		if ( level.getPassword() == null ) {
			passField.setText( "" );
			passField.setEnabled( false );
		} else {
			passField.setText( level.getPassword() );
		}
		
		// Update the enemy group
		enemyGroupCombo.setSelectedIndex(
			level.getEnemyGroup().getValue() - 3 );
		
		// Update the theme
		themeCombo.setSelectedIndex(
			level.getRomLevel().getTheme().getValue() );
		
		// Update the weapon
		weaponCombo.setSelectedIndex(
			level.getRomLevel().getWeapon().getValue() );
		
		// Disable the buttons
		applyButton.setEnabled( false );
		resetButton.setEnabled( false );
		
		hasChanged = false;
	}

	protected void actionApply () {
		// Check things
		boolean validArea = oldArea.getText().equals( newArea.getText() );
		boolean validPass = !passField.isEnabled()
			|| passField.getText().matches( "^[a-zA-Z]{4}$" );
		
		if ( !validArea ) {
			// Incorrect area
			JOptionPane.showMessageDialog( 
				this,
				"Total area of the level must remain unchanged",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
		} else if ( !validPass ) {
			// Correct area but incorrect password
			JOptionPane.showMessageDialog( 
				this,
				"Password must contain 4 letters from the english alphabet",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);
		} else {
			// Everything correct
			RomLevel rl = level.getRomLevel();
			int res = 0;

			IntVector oldSize = rl.getSize();
			int newSizeX = ( (Number) sizeX.getValue() ).intValue();
			int newSizeY = ( (Number) sizeY.getValue() ).intValue();
			if ( hasChanged && (oldSize.getX() != newSizeX || oldSize.getY() != newSizeY) ) {
				res = JOptionPane.showConfirmDialog( this,
					"The level size has changed, part of the level will be " +
					"removed.\nAre you sure you want to apply these changes?"
				);
			}
			
			if ( !hasChanged || res == JOptionPane.OK_OPTION ) {
				// Level Size
				rl.setSize( new IntVector( newSizeX, newSizeY ) );
				
				// Password & Enemy Group
				if ( passField.isEnabled() ) {
					level.setPassword( passField.getText() );
				}
				level.setEnemyGroup( EnemyGroup.valueOf(
					(byte)(enemyGroupCombo.getSelectedIndex()+3)
				) );
				
				// Theme and Weapon
				rl.setTheme( Theme.valueOf( (byte)themeCombo.getSelectedIndex() ) );
				rl.setWeapon( Weapon.valueOf( weaponCombo.getSelectedIndex() ) );
				
				// "Reset" everything
				actionReset();
				
				// Notify our listeners!
				for ( ApplyListener listener : listeners ) {
					listener.apply();
				}

			}
		}
		
		
	}

	private void changed () {
		applyButton.setEnabled( true );
		resetButton.setEnabled( true );
		
		hasChanged = true;
	}
	
	private void changedSize () {
		changed();
		
		newArea.setText( String.valueOf(
			((Integer)sizeX.getValue()).intValue() *
			((Integer)sizeY.getValue()).intValue()
		) );
	}
	
	public void addApplyListener( ApplyListener listener ) {
		listeners.add( listener );
	}
	
	public void removeApplyListener ( ApplyListener listener ) {
		listeners.remove( listener );
	}
	
	public boolean hasChanged () {
		return hasChanged;
	}
}
