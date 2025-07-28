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

import es.darkhogg.crazycastle.EnemyGroup;
import es.darkhogg.crazycastle.Level;
import es.darkhogg.crazycastle.Theme;
import es.darkhogg.crazycastle.Weapon;
import es.darkhogg.util.IntVector;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

public class PropertiesPanel extends JPanel {
	private JSpinner sizeX;
	private JSpinner sizeY;

	private JButton btnLeft;
	private JButton btnUp;
	private JButton btnDown;
	private JButton btnRight;

	//private EditorFrame mainFrame;
	private Level level;
	private JTextField passField;
	private JComboBox<String> enemyGroupCombo;
	private JComboBox<String> weaponCombo;
	private JComboBox<String> themeCombo;

	private boolean inhibitChanges;
	private boolean inhibitListeners;
	private final Collection<PropertiesChangedListener> listeners =
		new HashSet<PropertiesChangedListener>();

	public static interface PropertiesChangedListener {
		public abstract void onPropertiesChanged();
	}

	public PropertiesPanel() {
		this(null);
	}

	/**
	 * Create the panel.
	 */
	public PropertiesPanel(Level level) {
		JLabel labelEnemyGroup = new JLabel("Enemy Group:");

		JLabel labelSize = new JLabel("Size:");
		JLabel labelMove = new JLabel("Move:");
		JLabel labelPass = new JLabel("Password:");
		JLabel labelTheme = new JLabel("Theme:");
		JLabel labelWeapon = new JLabel("Weapon:");

		sizeX = new JSpinner();
		sizeX.setModel(new SpinnerNumberModel(1, 1, 255, 1));
		sizeX.addChangeListener(evt -> onSizeChanged());

		sizeY = new JSpinner();
		sizeY.setModel(new SpinnerNumberModel(1, 1, 255, 1));
		sizeY.addChangeListener(evt -> onSizeChanged());

		btnLeft = new JButton("←");
		btnLeft.addActionListener(action -> onMove(IntVector.UNIT_LEFT));
		btnUp = new JButton("↑");
		btnUp.addActionListener(action -> onMove(IntVector.UNIT_UP));
		btnDown = new JButton("↓");
		btnDown.addActionListener(action -> onMove(IntVector.UNIT_DOWN));
		btnRight = new JButton("→");
		btnRight.addActionListener(action -> onMove(IntVector.UNIT_RIGHT));

		passField = new JTextField();
		passField.setColumns(4);
		passField.addActionListener(action -> {
			onPasswordChanged();
		});
		passField.addFocusListener(new FocusListener() {
			@Override public void focusGained(FocusEvent evt) {}
			@Override public void focusLost(FocusEvent evt) {
				onPasswordChanged();
			};
		});

		enemyGroupCombo = new JComboBox<>();
		enemyGroupCombo.addActionListener(action -> onEnemyGroupChanged());
		enemyGroupCombo.setModel(new DefaultComboBoxModel<>(new String[]{
			"[$03] Tune Squad",
			"[$04] Hazel's Headhunters",
			"[$05] Monstars",
			"[$06] Witch Hazel",
		}));

		themeCombo = new JComboBox<>();
		themeCombo.addActionListener(action -> onThemeChanged());
		themeCombo.setModel(new DefaultComboBoxModel<>(new String[]{
			"[$00] Hall",
			"[$01] Basement",
			"[$02] Balcony",
			"[$03] Treasury",
			"[$04] Gate",
			"[$05] Garden",
			"[$06] Funhouse",
		}));

		weaponCombo = new JComboBox<>();
		weaponCombo.addActionListener(action -> onWeaponChanged());
		weaponCombo.setModel(new DefaultComboBoxModel<>(new String[]{
			"[$00] Bow & Arrows",
			"[$01] Bombs",
		}));

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup()
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelSize)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(sizeX, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sizeY, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelMove)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnLeft, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRight, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDown, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnUp, 0, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelPass)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(passField)
				)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelTheme)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(themeCombo)
				)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelEnemyGroup)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(enemyGroupCombo)
				)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(labelWeapon)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(weaponCombo)
				)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelSize)
					.addComponent(sizeX, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(sizeY, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelMove)
					.addComponent(btnLeft, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnRight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnDown, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addComponent(btnUp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelPass)
					.addComponent(passField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelTheme)
					.addComponent(themeCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelEnemyGroup)
					.addComponent(enemyGroupCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup()
					.addComponent(labelWeapon)
					.addComponent(weaponCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				)
		);

		groupLayout.linkSize(SwingConstants.HORIZONTAL, labelSize, labelMove, labelEnemyGroup, labelTheme, labelWeapon, labelPass);

		setLayout(groupLayout);

		setLevel(level);
	}

	public void setLevel(Level level) {
		this.level = level;
		inhibitListeners = true;
		inhibitChanges = true;

		setEnabled(level != null);

		// Update the size fields
		sizeX.setValue(level == null ? 1 : Integer.valueOf(level.getRomLevel().getSize().getX()));
		sizeY.setValue(level == null ? 1 : Integer.valueOf(level.getRomLevel().getSize().getY()));

		// Update the password
		passField.setEnabled(level != null && level.getPassword() != null);
		passField.setText(level == null ? "" : level.getPasswordNotNull());

		// Update the enemy group, theme and weapon
		if (level != null) {
			enemyGroupCombo.setSelectedIndex(level.getEnemyGroup().getValue() - 3);
			themeCombo.setSelectedIndex(level.getRomLevel().getTheme().getValue());
			weaponCombo.setSelectedIndex(level.getRomLevel().getWeapon().getValue());
		}

		inhibitListeners = false;
		inhibitChanges = false;
	}

	public Level getLevel() {
		return level;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		sizeX.setEnabled(enabled);
		sizeY.setEnabled(enabled);

		btnLeft.setEnabled(enabled);
		btnUp.setEnabled(enabled);
		btnDown.setEnabled(enabled);
		btnRight.setEnabled(enabled);

		passField.setEnabled(enabled);

		enemyGroupCombo.setEnabled(enabled);
		themeCombo.setEnabled(enabled);
		weaponCombo.setEnabled(enabled);
	}

	private void onSizeChanged() {
		if (inhibitChanges) {
			return;
		}

		final var width = ((Number)sizeX.getValue()).intValue();
		final var height = ((Number)sizeY.getValue()).intValue();

		final var changed = level.getRomLevel().getSize().getX() != width || level.getRomLevel().getSize().getY() != height;

		level.getRomLevel().setSize(new IntVector( width, height ));
		if (changed) {
			notifyPropertyChangedListeners();
		}
	}

	private void onPasswordChanged() {
		if (inhibitChanges) {
			return;
		}

		final var password = passField.getText().toUpperCase();

		final var isValid = !passField.isEnabled() || password.matches("^[a-zA-Z]{4}$");
		inhibitChanges = true;
		if ( isValid ) {
			final var changed = Objects.equals(level.getPassword(), password);

			level.setPassword(password);
			if (changed) {
				notifyPropertyChangedListeners();
			}

		} else {
			JOptionPane.showMessageDialog(
				this,
				"Password must contain exactly 4 letters from A to Z",
				"Error",
				JOptionPane.ERROR_MESSAGE
			);

			passField.setText(level.getPassword());
		}
		inhibitChanges = false;
	}

	private void onEnemyGroupChanged() {
		if (inhibitChanges) {
			return;
		}

		final var enemyGroup = EnemyGroup.valueOf((byte)(enemyGroupCombo.getSelectedIndex() + 3));

		final var changed = level.getEnemyGroup() != enemyGroup;

		level.setEnemyGroup(enemyGroup);
		if (changed) {
			notifyPropertyChangedListeners();
		}
	}

	private void onThemeChanged() {
		if (inhibitChanges) {
			return;
		}

		final var theme = Theme.valueOf((byte)themeCombo.getSelectedIndex());

		final var changed = level.getRomLevel().getTheme() != theme;

		level.getRomLevel().setTheme(theme);
		if (changed) {
			notifyPropertyChangedListeners();
		}
	}

	private void onWeaponChanged() {
		if (inhibitChanges) {
			return;
		}

		final var weapon = Weapon.valueOf(weaponCombo.getSelectedIndex());

		final var changed = level.getRomLevel().getWeapon() != weapon;

		level.getRomLevel().setWeapon(weapon);
		if (changed) {
			notifyPropertyChangedListeners();
		}
	}

	private void onMove(IntVector move) {
		level.getRomLevel().move(move);
		notifyPropertyChangedListeners();
	}

	public void addPropertyChangedListener(PropertiesChangedListener listener) {
		listeners.add(listener);
	}

	public void removePropertyChangedListener(PropertiesChangedListener listener) {
		listeners.remove(listener);
	}

	private void notifyPropertyChangedListeners() {
		if (!inhibitListeners) {
			for ( PropertiesChangedListener listener : listeners ) {
				listener.onPropertiesChanged();
			}
		}
	}
}
