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

import es.darkhogg.gameboy.Palette;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


class PaletteEditor extends JPanel {
	private Palette palette;
	private Palette originalPalette;

	private final JButton[] btnColors = new JButton[4];
	private final JButton btnReset;

	public PaletteEditor(Palette palette, Palette originalPalette) {
		this.originalPalette = originalPalette;
		if (palette == null) {
			palette = originalPalette;
		}

		final var layout = new BoxLayout(this, BoxLayout.LINE_AXIS);
		setLayout(layout);

		for (var i = 0; i < btnColors.length; i++) {
			final var idx = i;

			final var btn = btnColors[i] = new JButton();
			btn.setMinimumSize(new Dimension(32, 32));
			btn.setPreferredSize(new Dimension(32, 32));
			btn.addActionListener(action -> {
				final var color = JColorChooser.showDialog(this, "Color " + idx, this.palette.getColor(idx), false);
				if (color != null) {
					setPalette(this.palette.cloneWith(idx, color));
				}
			});

			if (i != 0) {
				add(Box.createRigidArea(new Dimension(4, 4)));
			}
			add(btn);
		}

		btnReset = new JButton();
		btnReset.setToolTipText("Reset to Default");
		btnReset.setIcon(new ImageIcon(SettingsDialog.class.getResource("/es/darkhogg/hazelnut/icon_refresh.png")));
		btnReset.setMinimumSize(new Dimension(32, 32));
		btnReset.setPreferredSize(new Dimension(32, 32));
		btnReset.addActionListener(action -> resetPalette());

		add(Box.createRigidArea(new Dimension(16, 16)));
		add(btnReset);

		setPalette(palette);
	}

	public void setPalette(Palette palette) {
		if (palette == null) {
			throw new NullPointerException("palette cannot be null");
		}

		this.palette = palette;

		for (var i = 0; i < btnColors.length; i++) {
			final var color = palette.getColor(i);
			final var btn = btnColors[i];
			btn.setBackground(color);
			if (color.getAlpha() == 0) {
				btn.setText("");
				btn.setEnabled(false);
				btn.setToolTipText(null);
				btn.setIcon(new ImageIcon(SettingsDialog.class.getResource("/es/darkhogg/hazelnut/icon_exit.png")));
			} else {
				btn.setText(" ");
				btn.setEnabled(true);
				btn.setToolTipText("Edit color " + i);
				btn.setIcon(null);
			}
		}
	}

	public void resetPalette() {
		setPalette(originalPalette);
	}

	public Palette getPalette() {
		return palette;
	}

	public void setOriginalPalette(Palette originalPalette) {
		this.originalPalette = originalPalette;
	}

	public Palette getOriginalPalette() {
		return originalPalette;
	}
}

public final class SettingsDialog extends JDialog {
	private final JCheckBox checkboxConfirmExit;
	private final JCheckBox checkboxSaveBackup;
	//private final JComboBox comboLookAndFeels;

	private final PaletteEditor palComboEditor;
	private final PaletteEditor palSpawnEditor;
	private final PaletteEditor palItemsEditor;
	private final PaletteEditor palDoorItemsEditor;
	private final PaletteEditor palEnemiesLightEditor;
	private final PaletteEditor palEnemiesDarkEditor;

	public SettingsDialog() {
		final var config = Hazelnut.getConfiguration();

		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent ev) {
				actionCancel();
			}
		});

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(SettingsDialog.class.getResource("/es/darkhogg/hazelnut/icon_preferences.png")));
		setTitle("Hazelnut - Preferences");

		final var contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		contentPane.setLayout(new BorderLayout(4, 4));
		setContentPane(contentPane);

		final var buttonsPanel = new JPanel();
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);

		final var flButtonsPanel = new FlowLayout(FlowLayout.CENTER, 4, 4);
		buttonsPanel.setLayout(flButtonsPanel);

		final var btnAccept = new JButton("Accept");
		btnAccept.addActionListener(action -> actionAccept());
		buttonsPanel.add(btnAccept);

		final var btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(action -> actionCancel());
		buttonsPanel.add(btnCancel);

		final var optionsPanel = new JPanel();
		contentPane.add(optionsPanel, BorderLayout.CENTER);
		optionsPanel.setLayout(new BorderLayout(0, 0));

		final var labelPalCombo = new JLabel("Combo Plt.");
		final var palComboStr = config.getString(Hazelnut.CONFIG_PALETTE_COMBOS);
		final var palCombo = palComboStr == null ? null : Palette.parse(palComboStr);
		palComboEditor = new PaletteEditor(palCombo, EditorFrame.PALETTE_COMBOS);

		final var labelPalSpawn = new JLabel("Spawn Plt.");
		final var palSpawnStr = config.getString(Hazelnut.CONFIG_PALETTE_SPAWN);
		final var palSpawn = Optional.ofNullable(Palette.parse(palSpawnStr)).map(pal -> pal.cloneWith(0, null));
		palSpawnEditor = new PaletteEditor(palSpawn.orElse(null), EditorFrame.PALETTE_SPAWN);

		final var labelPalItems = new JLabel("Item Plt.");
		final var palItemsStr = config.getString(Hazelnut.CONFIG_PALETTE_ITEMS);
		final var palItems = Optional.ofNullable(Palette.parse(palItemsStr)).map(pal -> pal.cloneWith(0, null));
		palItemsEditor = new PaletteEditor(palItems.orElse(null), EditorFrame.PALETTE_ITEMS);

		final var labelPalDoorItems = new JLabel("D.Item Plt.");
		final var palDoorItemsStr = config.getString(Hazelnut.CONFIG_PALETTE_DOOR_ITEMS);
		final var palDoorItems = Optional.ofNullable(Palette.parse(palDoorItemsStr)).map(pal -> pal.cloneWith(0, null));
		palDoorItemsEditor = new PaletteEditor(palDoorItems.orElse(null), EditorFrame.PALETTE_DOOR_ITEMS);

		final var labelPalEnemiesLight = new JLabel("Enemy (Light) Plt.");
		final var palEnemiesLightStr = config.getString(Hazelnut.CONFIG_PALETTE_ENEMIES_LIGHT);
		final var palEnemiesLight = Optional.ofNullable(Palette.parse(palEnemiesLightStr)).map(pal -> pal.cloneWith(0, null));
		palEnemiesLightEditor = new PaletteEditor(palEnemiesLight.orElse(null), EditorFrame.PALETTE_ENEMIES_LIGHT);

		final var labelPalEnemiesDark = new JLabel("Enemy (Dark) Plt.");
		final var palEnemiesDarkStr = config.getString(Hazelnut.CONFIG_PALETTE_ENEMIES_DARK);
		final var palEnemiesDark = Optional.ofNullable(Palette.parse(palEnemiesDarkStr)).map(pal -> pal.cloneWith(0, null));
		palEnemiesDarkEditor = new PaletteEditor(palEnemiesDark.orElse(null), EditorFrame.PALETTE_ENEMIES_DARK);


		checkboxConfirmExit = new JCheckBox("Ask for confirmation before exiting");
		checkboxConfirmExit.setSelected(config.getBoolean(Hazelnut.CONFIG_CONFIRM_EXIT, true));

		checkboxSaveBackup = new JCheckBox("Create backups before saving files");
		checkboxSaveBackup.setSelected(config.getBoolean(Hazelnut.CONFIG_SAVE_BACKUP, true));

		// final var lafsinfo = UIManager.getInstalledLookAndFeels();
		// final var lafs = new String[ lafsinfo.length ];
		// var currLaf = -1;
		// for ( var i = 0; i < lafs.length; i++ ) {
		// 	lafs[ i ] = lafsinfo[ i ].getName();
		// 	if ( lafsinfo[ i ].getClass().getCanonicalName() == UIManager.getLookAndFeel().getClass().getCanonicalName() ) {
		// 		currLaf = i;
		// 	}
		// }

		// comboLookAndFeels = new JComboBox<>(lafs);
		// comboLookAndFeels.setSelectedIndex(currLaf);

		var layout = new GroupLayout(optionsPanel);
		optionsPanel.setLayout(layout);

		final var sep1 = new JSeparator(JSeparator.HORIZONTAL);

		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup()
				.addComponent(checkboxConfirmExit)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(checkboxSaveBackup)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addComponent(sep1)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalCombo, Alignment.CENTER)
				.addComponent(palComboEditor)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalSpawn, Alignment.CENTER)
				.addComponent(palSpawnEditor)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalItems, Alignment.CENTER)
				.addComponent(palItemsEditor)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalDoorItems, Alignment.CENTER)
				.addComponent(palDoorItemsEditor)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalEnemiesLight, Alignment.CENTER)
				.addComponent(palEnemiesLightEditor)
			)
			.addPreferredGap(ComponentPlacement.RELATED)
			.addGroup(layout.createParallelGroup()
				.addComponent(labelPalEnemiesDark, Alignment.CENTER)
				.addComponent(palEnemiesDarkEditor)
			)
			// .addGroup(layout.createParallelGroup()
			// 	.addComponent(comboLookAndFeels)
			// )
		);

		layout.setHorizontalGroup(layout.createParallelGroup()
			.addGroup(layout.createSequentialGroup()
				.addComponent(checkboxConfirmExit)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(checkboxSaveBackup)
			)
			.addComponent(sep1)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalCombo)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palComboEditor)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalSpawn)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palSpawnEditor)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalItems)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palItemsEditor)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalDoorItems)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palDoorItemsEditor)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalEnemiesLight)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palEnemiesLightEditor)
			)
			.addGroup(layout.createSequentialGroup()
				.addComponent(labelPalEnemiesDark)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(palEnemiesDarkEditor)
			)
			// .addGroup(layout.createSequentialGroup()
			// 	.addComponent(comboLookAndFeels)
			// )
		);

		layout.linkSize(
			SwingConstants.HORIZONTAL,
			labelPalCombo, labelPalSpawn, labelPalItems, labelPalDoorItems,
			labelPalEnemiesLight, labelPalEnemiesDark
		);

		setResizable(false);
		pack();
	}

	private void actionAccept() {
		final var config = Hazelnut.getConfiguration();
		final var logger = Hazelnut.getLogger();

		logger.debug("Saving preferences...");

		config.setProperty(Hazelnut.CONFIG_CONFIRM_EXIT, checkboxConfirmExit.isSelected());
		config.setProperty(Hazelnut.CONFIG_SAVE_BACKUP, checkboxSaveBackup.isSelected());

		config.setProperty(Hazelnut.CONFIG_PALETTE_COMBOS, palComboEditor.getPalette().stringify());
		config.setProperty(Hazelnut.CONFIG_PALETTE_SPAWN, palSpawnEditor.getPalette().stringify());
		config.setProperty(Hazelnut.CONFIG_PALETTE_ITEMS, palItemsEditor.getPalette().stringify());
		config.setProperty(Hazelnut.CONFIG_PALETTE_DOOR_ITEMS, palDoorItemsEditor.getPalette().stringify());
		config.setProperty(Hazelnut.CONFIG_PALETTE_ENEMIES_LIGHT, palEnemiesLightEditor.getPalette().stringify());
		config.setProperty(Hazelnut.CONFIG_PALETTE_ENEMIES_DARK, palEnemiesDarkEditor.getPalette().stringify());

		// Look & Feel
		/*LookAndFeelInfo[] lafsinfo = UIManager.getInstalledLookAndFeels();
		String lafname = null;
		for ( LookAndFeelInfo lafinfo : lafsinfo ) {
			String selected = (String) lafComboBox.getSelectedItem();
			if ( selected == lafinfo.getName() ) {
				lafname = lafinfo.getClassName();
			}
		}

		if ( lafname != null ) {
			try {
				UIManager.setLookAndFeel( lafname );
			} catch ( Exception e ) {
				logger.error( "Tried to select an uninstalled L&F: " + lafname );
				e.printStackTrace();
			}
			config.setProperty( Hazelnut.CONFIG_LOOK_AND_FEEL, lafname );
			SwingUtilities.updateComponentTreeUI( Hazelnut.getFrame() );
		}*/

		logger.info("Preferences saved");
		dispose();
	}

	private void actionCancel() {
		dispose();
	}


}
