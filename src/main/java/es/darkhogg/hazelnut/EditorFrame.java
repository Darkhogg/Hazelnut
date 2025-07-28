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

import es.darkhogg.crazycastle.ComboType;
import es.darkhogg.crazycastle.CrazyCastleRom;
import es.darkhogg.crazycastle.Enemy;
import es.darkhogg.crazycastle.EnemyType;
import es.darkhogg.crazycastle.Item;
import es.darkhogg.crazycastle.ItemType;
import es.darkhogg.crazycastle.Level;
import es.darkhogg.crazycastle.RomLevel;
import es.darkhogg.crazycastle.SaveOptions;
import es.darkhogg.gameboy.Palette;
import es.darkhogg.util.IntVector;
import org.apache.commons.configuration2.Configuration;
import org.apache.log4j.Logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EditorFrame extends JFrame {

	public static final Palette PALETTE_COMBOS = new Palette(
		Color.DARK_GRAY, Color.WHITE, Color.LIGHT_GRAY, Color.BLACK
	);

	public static final Palette PALETTE_SPAWN = new Palette(
		null, new Color( 0x600060 ), new Color( 0xF860F8 ), new Color( 0xF8D0F8 )
	);

	public static final Palette PALETTE_ITEMS = new Palette(
		null, new Color( 0x603000 ), new Color( 0xF8B060 ), new Color( 0xF8E8D0 )
	);

	public static final Palette PALETTE_DOOR_ITEMS = new Palette(
		null, new Color( 0x006000 ), new Color( 0x60F860 ), new Color( 0xD0F8D0 )
	);

	public static final Palette PALETTE_ENEMIES_LIGHT = new Palette(
		null, new Color( 0x600000 ), new Color( 0xF89090 ), new Color( 0xF8D0D0 )
	);

	public static final Palette PALETTE_ENEMIES_DARK = new Palette(
		null, new Color( 0x600000 ), new Color( 0xD83030 ), new Color( 0xF89090 )
	);

	private final Configuration config;
	private final Logger logger;

	private JPanel contentPane;

	private JCheckBoxMenuItem menuViewSpawn;
	private JCheckBoxMenuItem menuViewItems;
	private JCheckBoxMenuItem menuViewDoorItems;
	private JCheckBoxMenuItem menuViewEnemies;
	private JCheckBoxMenuItem menuViewTypes;
	private JCheckBoxMenuItem menuViewColl;
	private JCheckBoxMenuItem menuViewWarns;

	private JToggleButton barViewSpawn;
	private JToggleButton barViewItems;
	private JToggleButton barViewDoorItems;
	private JToggleButton barViewEnemies;
	private JToggleButton barViewTypes;
	private JToggleButton barViewColl;
	private JToggleButton barViewWarns;

	private JPanel sidePanel;
	private JMenuItem menuLoadRom;
	private JMenuItem menuSaveRom;
	private JMenuItem menuExit;
	private JButton barLoadRom;
	private JButton barSaveRom;
	private PropertiesPanel propertiesPanel;

	private JFileChooser fileChooser, hlfFileChooser;
	private File loadedFile;
	private CrazyCastleRom loadedRom;
	private Level selectedLevel;
	private int selectedLevelNum;
	private LinkedList<String> recentFiles;

	private JMenuItem menuSaveLevel;
	private JMenuItem menuReloadLevel;
	private JMenuItem menuLoadLevel;
	private JButton barLoadLevel;
	private JButton barReloadLevel;
	private JButton barSaveLevel;
	private JMenuItem mntmPreferences;
	private JMenuItem mntmSpriteView;

	private JScrollPane scrollPane;
	private LevelDisplay levelDisplay;

	private JToggleButton scaleButton;
	private JToggleButton gridButton;
	private OmniSelectorPanel selectorPanel;

	private boolean romHasChanged, levelHasChanged;
	private boolean romFeaturesEnabled, levelFeaturesEnabled;

	private static final Map<ComboType,Image> helpSet;
	private JPanel levelWrapper;
	private JCheckBoxMenuItem scaleMenuItem;
	private JCheckBoxMenuItem gridMenuItem;
	private JMenuItem menuClearLevel;
	private JMenuItem menuSaveRomAs;
	private JMenuItem menuImportLevel;
	private JMenuItem menuExportLevel;
	private JButton barClearLevel;
	private JButton barImportLevel;
	private JButton barExportLevel;
	private JMenu menuRecentFiles;

	static {
		final var COMBO_TYPES_TILES = new ComboType[][]{
			{
				ComboType.ROPE_FLR, ComboType.DOOR, ComboType.FINAL,
				ComboType.FLYUP_SIDE, ComboType.ELEV_UPRGT, ComboType.ELEV_UPLFT,
			},
			{
				ComboType.STAIRS_BTM, ComboType.FLYUP, ComboType.WARP,
				ComboType.SLOW, ComboType.ROPE, ComboType.LADDER,
			},
			{
				ComboType.PIPE_ENTDN, ComboType.PIPE_ENTUP, ComboType.PIPE_VER,
				ComboType.PIPE_VERFLR, ComboType.PIPE_HOR, ComboType.PIPE_HORWALL,
			},
			{
				ComboType.PIPE_UPLFT, ComboType.PIPE_UPRGT, ComboType.PIPE_DNLFT,
				ComboType.PIPE_DNRGT, ComboType.PIPE_CROSS, ComboType.LADDER_TOP,
			},
			{
				ComboType.BLK_HAMMER, ComboType.BLK_PICK, ComboType.ELEV_DNRGT,
				ComboType.ELEV_DNLFT, ComboType.STAIRS_LFT, ComboType.STAIRS_RGT,
			},
		};
		try {
			EnumMap<ComboType,Image> hs =
				new EnumMap<ComboType, Image>( ComboType.class );

			BufferedImage iconSet = ImageIO.read(EditorFrame.class.getResource(
				"/es/darkhogg/hazelnut/help_icons.png"));

			for (var i = 0; i < COMBO_TYPES_TILES.length; i++) {
				final var row = COMBO_TYPES_TILES[i];
				for (var j = 0; j < row.length; j++) {
					final var ct = row[j];
					hs.put(ct, iconSet.getSubimage(j * 16, i * 16, 16, 16));
				}
			}

			helpSet = Collections.unmodifiableMap(hs);
		} catch ( Throwable e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Create the frame. The GUI will be initialized on the Event Dispatch
	 * thread. If the initialization is not completed, this constructor throws
	 * an unspecified RuntimeException with its cause set to the exception that
	 * originally caused the initialization error.
	 *
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public EditorFrame() {
		config = Hazelnut.getConfiguration();
		logger = Hazelnut.getLogger();

		try {
			SwingUtilities.invokeAndWait(() -> initializeGui());
		} catch ( InterruptedException e ) {
			throw new RuntimeException( e );
		} catch ( InvocationTargetException e ) {
			throw new RuntimeException( e );
		}
	}

	private void initializeGui() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 640, 480);

		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent arg0) {
				actionExit();
			}
		});

		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("GameBoy ROM (*.gb)", "gb"));

		hlfFileChooser = new JFileChooser();
		hlfFileChooser.setFileFilter(new FileNameExtensionFilter("Hazelnut Level Format (*.hlf)", "hlf"));

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		menuLoadRom = new JMenuItem("Load ROM...");
		menuLoadRom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		menuLoadRom.addActionListener(action -> actionOpenRom(null));
		menuLoadRom.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_open.png")));
		menuLoadRom.setMnemonic('O');
		mnFile.add(menuLoadRom);

		menuSaveRom = new JMenuItem("Save ROM");
		menuSaveRom.addActionListener(action -> actionSaveRom());
		menuSaveRom.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_save.png")));
		menuSaveRom.setMnemonic('S');
		menuSaveRom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		mnFile.add(menuSaveRom);

		menuSaveRomAs = new JMenuItem("Save ROM As...");
		menuSaveRomAs.setMnemonic('a');
		menuSaveRomAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		menuSaveRomAs.addActionListener(action -> actionSaveRomAs());
		menuSaveRomAs.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_save_as.png")));
		mnFile.add(menuSaveRomAs);

		mnFile.addSeparator();

		menuLoadLevel = new JMenuItem("Load Level...");
		menuLoadLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		menuLoadLevel.addActionListener(action -> actionLoadLevel());
		menuLoadLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_load_level.png")));
		menuLoadLevel.setMnemonic('L');
		mnFile.add(menuLoadLevel);

		menuReloadLevel = new JMenuItem("Reload Level");
		menuReloadLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		menuReloadLevel.addActionListener(action -> actionReloadLevel());
		menuReloadLevel.setMnemonic('R');
		menuReloadLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_reload_level.png")));
		mnFile.add(menuReloadLevel);

		menuSaveLevel = new JMenuItem("Save Level");
		menuSaveLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
		menuSaveLevel.addActionListener(action -> actionSaveLevel());
		menuSaveLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_save_level.png")));
		menuSaveLevel.setMnemonic('v');

		mnFile.add(menuSaveLevel);

		//*
		mnFile.addSeparator();

		menuImportLevel = new JMenuItem("Import Level...");
		menuImportLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		menuImportLevel.setMnemonic('i');
		menuImportLevel.addActionListener(action -> actionImportLevel());
		menuImportLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_import.png")));
		mnFile.add(menuImportLevel);

		menuExportLevel = new JMenuItem("Export Level...");
		menuExportLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		menuExportLevel.setMnemonic('e');
		menuExportLevel.addActionListener(action -> actionExportLevel());
		menuExportLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_export.png")));
		mnFile.add(menuExportLevel);
		//*/

		mnFile.addSeparator();

		menuExit = new JMenuItem("Exit");
		menuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		menuExit.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_exit.png")));
		menuExit.addActionListener(action -> actionExit());

		menuRecentFiles = new JMenu("Recent Files...");
		mnFile.add(menuRecentFiles);

		mnFile.addSeparator();
		menuExit.setMnemonic('X');
		mnFile.add(menuExit);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setMnemonic('E');
		menuBar.add(mnEdit);

		menuViewSpawn = new JCheckBoxMenuItem("View Spawn Point");
		menuViewSpawn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
		menuViewSpawn.addActionListener(action -> actionToggle(false));

		menuClearLevel = new JMenuItem("Clear Level");
		menuClearLevel.setMnemonic('c');
		menuClearLevel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		menuClearLevel.addActionListener(action -> actionClearLevel());
		menuClearLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_clear_level.png")));
		mnEdit.add(menuClearLevel);

		mnEdit.addSeparator();

		menuViewSpawn.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_spawn.png")));
		menuViewSpawn.setMnemonic('S');
		menuViewSpawn.setSelected(true);
		mnEdit.add(menuViewSpawn);

		menuViewItems = new JCheckBoxMenuItem("View Items");
		menuViewItems.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
		menuViewItems.addActionListener(action -> actionToggle(false));
		menuViewItems.setSelected(true);
		menuViewItems.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_items.png")));
		menuViewItems.setMnemonic('I');
		mnEdit.add(menuViewItems);

		menuViewDoorItems = new JCheckBoxMenuItem("View Door Items");
		menuViewDoorItems.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
		menuViewDoorItems.addActionListener(action -> actionToggle(false));
		menuViewDoorItems.setSelected(true);
		menuViewDoorItems.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_door_items.png")));
		menuViewDoorItems.setMnemonic('D');
		mnEdit.add(menuViewDoorItems);

		menuViewEnemies = new JCheckBoxMenuItem("View Enemies");
		menuViewEnemies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0));
		menuViewEnemies.addActionListener(action -> actionToggle(false));
		menuViewEnemies.setSelected(true);
		menuViewEnemies.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_enemies.png")));
		menuViewEnemies.setMnemonic('E');
		mnEdit.add(menuViewEnemies);

		menuViewTypes = new JCheckBoxMenuItem("View Combo Types");
		menuViewTypes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0));
		menuViewTypes.addActionListener(action -> actionToggle(false));
		menuViewTypes.setSelected(true);
		menuViewTypes.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_combo_infos.png")));
		menuViewTypes.setMnemonic('C');
		mnEdit.add(menuViewTypes);

		menuViewColl = new JCheckBoxMenuItem("View Combo Collision");
		menuViewColl.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0));
		menuViewColl.addActionListener(action -> actionToggle(false));
		menuViewColl.setSelected(true);
		menuViewColl.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_combo_collision.png")));
		menuViewColl.setMnemonic('K');
		mnEdit.add(menuViewColl);

		menuViewWarns = new JCheckBoxMenuItem("View Warnings");
		menuViewWarns.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0));
		menuViewWarns.addActionListener(action -> actionToggle(false));
		menuViewWarns.setSelected(true);
		menuViewWarns.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_warning.png")));
		menuViewWarns.setMnemonic('W');
		mnEdit.add(menuViewWarns);

		mnEdit.addSeparator();

		scaleMenuItem = new JCheckBoxMenuItem("Scale Level Display");
		scaleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
		scaleMenuItem.setMnemonic('l');
		scaleMenuItem.addActionListener(action -> actionScale(false));
		scaleMenuItem.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_x2.png")));
		mnEdit.add(scaleMenuItem);

		gridMenuItem = new JCheckBoxMenuItem("Display grid");
		gridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0));
		gridMenuItem.setMnemonic('G');
		gridMenuItem.addActionListener(action -> actionGrid(false));
		gridMenuItem.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_grid.png")));
		mnEdit.add(gridMenuItem);

		mnEdit.addSeparator();

		mntmPreferences = new JMenuItem("Preferences...");
		mntmPreferences.addActionListener(action -> actionPreferences());
		mntmPreferences.setMnemonic('P');
		mntmPreferences.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_preferences.png")));
		mnEdit.add(mntmPreferences);

		final var mnTools = new JMenu("Tools");
		mnTools.setMnemonic('T');

		mntmSpriteView = new JMenuItem("Tileset & Sprite Viewer...");
		mntmSpriteView.addActionListener(action -> actionTilesSpriteViewer());
		mntmSpriteView.setMnemonic('T');
		mntmSpriteView.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_tssprview.png")));

		if (config.getBoolean("Hazelnut.devMode", false)) {
			mnTools.add(mntmSpriteView);
		}

		if (mnTools.getItemCount() > 0) {
			menuBar.add(mnTools);
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder( 5, 5, 5, 5 ));
		contentPane.setLayout(new BorderLayout( 0, 0 ));
		setContentPane(contentPane);

		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		contentPane.add(toolBar, BorderLayout.NORTH);

		barLoadRom = new JButton("");
		barLoadRom.addActionListener(action -> actionOpenRom(null));
		barLoadRom.setToolTipText("Open");
		barLoadRom.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_open.png")));
		toolBar.add(barLoadRom);

		barSaveRom = new JButton("");
		barSaveRom.addActionListener(action -> actionSaveRom());
		barSaveRom.setToolTipText("Save");
		barSaveRom.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_save.png")));
		toolBar.add(barSaveRom);

		toolBar.addSeparator();

		barImportLevel = new JButton("");
		barImportLevel.addActionListener(action -> actionImportLevel());
		barImportLevel.setToolTipText("Import Level");
		barImportLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_import.png")));
		toolBar.add(barImportLevel);

		barExportLevel = new JButton("");
		barExportLevel.addActionListener(action -> actionExportLevel());
		barExportLevel.setToolTipText("Export Level");
		barExportLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_export.png")));
		toolBar.add(barExportLevel);

		toolBar.addSeparator();

		barLoadLevel = new JButton("");
		barLoadLevel.setToolTipText("Load Level");
		barLoadLevel.addActionListener(action -> actionLoadLevel());
		barLoadLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_load_level.png")));
		toolBar.add(barLoadLevel);

		barReloadLevel = new JButton("");
		barReloadLevel.setToolTipText("Reload Current Level");
		barReloadLevel.addActionListener(action -> actionReloadLevel());
		barReloadLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_reload_level.png")));
		toolBar.add(barReloadLevel);

		barSaveLevel = new JButton("");
		barSaveLevel.setToolTipText("Save Level");
		barSaveLevel.addActionListener(action -> actionSaveLevel());
		barSaveLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_save_level.png")));
		toolBar.add(barSaveLevel);

		toolBar.addSeparator();

		barViewItems = new JToggleButton("");
		barViewItems.setToolTipText("Toggle View Items");
		barViewItems.addActionListener(action -> actionToggle(true));

		barViewSpawn = new JToggleButton("");
		barViewSpawn.setToolTipText("Toggle View Spawn");
		barViewSpawn.addActionListener(action -> actionToggle(true));

		barClearLevel = new JButton("");
		barClearLevel.addActionListener(action -> actionClearLevel());
		barClearLevel.setToolTipText("Clear Level");
		barClearLevel.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_clear_level.png")));
		toolBar.add(barClearLevel);

		toolBar.addSeparator();

		barViewSpawn.setSelected(true);
		barViewSpawn.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_spawn.png")));
		toolBar.add(barViewSpawn);
		barViewItems.setSelected(true);
		barViewItems.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_items.png")));
		toolBar.add(barViewItems);

		barViewDoorItems = new JToggleButton("");
		barViewDoorItems.setToolTipText("Toggle View Door Items");
		barViewDoorItems.addActionListener(action -> actionToggle(true));
		barViewDoorItems.setSelected(true);
		barViewDoorItems.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_door_items.png")));
		toolBar.add(barViewDoorItems);

		barViewEnemies = new JToggleButton("");
		barViewEnemies.setToolTipText("toggle View Enemies");
		barViewEnemies.addActionListener(action -> actionToggle(true));
		barViewEnemies.setSelected(true);
		barViewEnemies.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_enemies.png")));
		toolBar.add(barViewEnemies);

		barViewTypes = new JToggleButton("");
		barViewTypes.setToolTipText("Toggle view Combo Help");
		barViewTypes.addActionListener(action -> actionToggle(true));
		barViewTypes.setSelected(true);
		barViewTypes.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_combo_infos.png")));
		toolBar.add(barViewTypes);

		barViewColl = new JToggleButton("");
		barViewColl.setToolTipText("Toggle view Combo Collision");
		barViewColl.addActionListener(action -> actionToggle(true));
		barViewColl.setSelected(true);
		barViewColl.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_combo_collision.png")));
		toolBar.add(barViewColl);

		barViewWarns = new JToggleButton("");
		barViewWarns.setToolTipText("Toggle view Warnings");
		barViewWarns.addActionListener(action -> actionToggle(true));
		barViewWarns.setSelected(true);
		barViewWarns.setIcon(new ImageIcon(EditorFrame.class.getResource(
			"/es/darkhogg/hazelnut/icon_warning.png")));
		toolBar.add(barViewWarns);

		barViewSpawn.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_SPAWN, true));
		barViewItems.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_ITEMS, true));
		barViewDoorItems.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_DOOR_ITEMS, true));
		barViewEnemies.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_ENEMIES, true));
		barViewTypes.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_CTYPES, true));
		barViewColl.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_COLL, false));
		barViewWarns.setSelected(config.getBoolean(Hazelnut.CONFIG_VIEW_WARNS, true));

		toolBar.addSeparator();

		scaleButton = new JToggleButton("");
		scaleButton.setToolTipText("Toggle Scale Display");
		scaleButton.addActionListener(action -> actionScale(true));
		scaleButton.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_x2.png")));
		toolBar.add(scaleButton);

		scaleButton.setSelected(config.getBoolean(Hazelnut.CONFIG_IS_SCALED, false));

		gridButton = new JToggleButton("");
		gridButton.setToolTipText("Display Grid");
		gridButton.addActionListener(action -> actionGrid(true));
		gridButton.setIcon(new ImageIcon(EditorFrame.class.getResource("/es/darkhogg/hazelnut/icon_grid.png")));
		toolBar.add(gridButton);
		gridButton.setSelected(config.getBoolean(Hazelnut.CONFIG_SHOW_GRID, false));

		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);

		levelWrapper = new JPanel();
		levelWrapper.setBorder(new TitledBorder(null, "Level", TitledBorder.LEADING, TitledBorder.TOP));
		levelWrapper.setLayout(new BorderLayout(0, 0));

		scrollPane = new JScrollPane();
		scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		levelWrapper.add(scrollPane);

		var scrollCenterer = new JPanel();
		scrollCenterer.setLayout(new GridBagLayout());
		scrollPane.setViewportView(scrollCenterer);

		levelDisplay = new LevelDisplay();
		levelDisplay.addEditListener(new EditListener(){
			@Override public void leftPressed(int x, int y) {
				final var st = selectorPanel.getSelectedType();
				final var so = selectorPanel.getSelectedObject();

				if (st == SelectionType.COMBO) {
					paintComboAt(x, y, ((Byte)so).byteValue());

				} else if (st == SelectionType.SPAWN) {
					selectedLevel.getRomLevel().setSpawn(
						new IntVector( x, y ));
					levelHasChanged = true;

				} else if (st == SelectionType.ITEM) {
					final var ent = new Item( (ItemType)so, x, y );
					final var items = selectedLevel.getRomLevel().getItems();
					for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
						final var item = it.next();
						if (item.getX() == x && item.getY() == y) {
							it.remove();
						}
					}
					if (items.isFull()) {
						selectedLevel.getRomLevel().pruneItems(1);
						levelHasChanged = true;
					}
					if (!items.isFull()) {
						items.add(ent);
						levelHasChanged = true;
					}

				} else if (st == SelectionType.DOOR_ITEM) {
					final var ent = new Item( (ItemType)so, x, y );
					final var doorItems = selectedLevel.getRomLevel().getDoorItems();
					for (Iterator<Item> it = doorItems.iterator(); it.hasNext(); ) {
						final var item = it.next();
						if (item.getX() == x && item.getY() == y) {
							it.remove();
						}
					}
					if (doorItems.isFull()) {
						selectedLevel.getRomLevel().pruneDoorItems(1);
						levelHasChanged = true;
					}
					if (!doorItems.isFull()) {
						doorItems.add(ent);
						levelHasChanged = true;
					}

				} else if (st == SelectionType.ENEMY) {
					final var ent = new Enemy( (EnemyType)so, x, y );
					final var enemies = selectedLevel.getRomLevel().getEnemies();
					for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
						final var enem = it.next();
						if (enem.getX() == x && enem.getY() == y) {
							it.remove();
						}
					}
					if (enemies.isFull()) {
						selectedLevel.getRomLevel().pruneEnemies(1);
						levelHasChanged = true;
					}
					if (!enemies.isFull()) {
						enemies.add(ent);
						levelHasChanged = true;
					}

				}

				updateTitle();
				levelDisplay.repaint();
				levelHasChanged = true;
			}
			@Override public void centerPressed(int x, int y) {
				selectorPanel.setSelection(
					SelectionType.COMBO,
					selectedLevel.getRomLevel().getGrid().get(x, y)
				);
			}
			@Override public void rightPressed(int x, int y) {
				deleteVisibleEntitiesAt(x, y);
			}
			@Override public void leftDragged(int x, int y) {
				if (selectorPanel.getSelectedType() == SelectionType.COMBO) {
					paintComboAt(x, y,
						((Byte)selectorPanel.getSelectedObject()).byteValue());
				}
			}
			@Override public void centerDragged(int x, int y) {
				// Nothing to de here
			}
			@Override public void rightDragged(int x, int y) {
				deleteVisibleEntitiesAt(x, y);
			}


			// Utility methods
			/*private void selectComboAt ( int x, int y ) {

			}/**/
			private void paintComboAt(int x, int y, byte value) {
				selectedLevel.getRomLevel().getGrid().set(x, y, value);
				levelHasChanged = true;

				updateTitle();
				levelDisplay.repaint();
			}
			private void deleteVisibleEntitiesAt(int x, int y) {
				Collection<Item> items = selectedLevel.getRomLevel().getItems();
				Collection<Item> doorItems = selectedLevel.getRomLevel().getDoorItems();
				Collection<Enemy> enemies = selectedLevel.getRomLevel().getEnemies();

				if (barViewItems.isSelected()) {
					for (Iterator<Item> it = items.iterator(); it.hasNext(); ) {
						Item item = it.next();
						if (item.getX() == x && item.getY() == y) {
							it.remove();
							levelHasChanged = true;
						}
					}
				}

				if (barViewDoorItems.isSelected()) {
					for (Iterator<Item> it = doorItems.iterator(); it.hasNext(); ) {
						Item doorItem = it.next();
						if (doorItem.getX() == x && doorItem.getY() == y) {
							it.remove();
							levelHasChanged = true;
						}
					}
				}

				if (barViewEnemies.isSelected()) {
					for (Iterator<Enemy> it = enemies.iterator(); it.hasNext(); ) {
						Enemy enem = it.next();
						if (enem.getX() == x && enem.getY() == y) {
							it.remove();
							levelHasChanged = true;
						}
					}
				}

				updateTitle();
				levelDisplay.repaint();
			}
		});
		scrollCenterer.add(levelDisplay);

		sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

		selectorPanel = new OmniSelectorPanel();
		sidePanel.add(selectorPanel);

		propertiesPanel = new PropertiesPanel( null );
		propertiesPanel.setBorder(new TitledBorder(
			null,
			"Level Properties",
			TitledBorder.LEADING,
			TitledBorder.TOP
		));
		sidePanel.add(propertiesPanel);

		sidePanel.setMinimumSize(new Dimension(350, 0));
		sidePanel.setPreferredSize(new Dimension(350, 0));

		contentPane.add(levelWrapper, BorderLayout.CENTER);
		contentPane.add(sidePanel, BorderLayout.EAST);

		Component verticalGlue = Box.createVerticalGlue();
		sidePanel.add(verticalGlue);

		updateTitle();
		actionToggle(true);
		actionScale(true);
		actionGrid(true);
		setIconImages(Arrays.asList(
			Toolkit.getDefaultToolkit().getImage(
        EditorFrame.class.getResource("/es/darkhogg/hazelnut/logo_16.png")
      ),
      Toolkit.getDefaultToolkit().getImage(
        EditorFrame.class.getResource("/es/darkhogg/hazelnut/logo_24.png")
      )
    ));
		setRomFeaturesEnabled(false);
		setLevelFeaturesEnabled(false);

		if ( config.getBoolean(Hazelnut.CONFIG_FRAME_MAXIMUM, false) ) {
			setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		} else {
			int x = config.getInt(Hazelnut.CONFIG_FRAME_LOCATION_X, Integer.MIN_VALUE);
			int y = config.getInt(Hazelnut.CONFIG_FRAME_LOCATION_Y, Integer.MIN_VALUE);
			int w = config.getInt(Hazelnut.CONFIG_FRAME_SIZE_WIDTH, Integer.MIN_VALUE);
			int h = config.getInt(Hazelnut.CONFIG_FRAME_SIZE_HEIGHT, Integer.MIN_VALUE);

			if (
				x == Integer.MIN_VALUE || x == Integer.MIN_VALUE
			 || w == Integer.MIN_VALUE || h == Integer.MIN_VALUE
			) {
				setLocationRelativeTo(null);
			} else {
				setLocation(x, y);
				setSize(w, h);
			}
		}

		propertiesPanel.addPropertyChangedListener(() -> {
			levelHasChanged = true;
			updateTitle();
			updateDisplay();
		});

		if ( config.containsKey(Hazelnut.CONFIG_LAST_DIR) ) {
			File lastDir = new File( config.getString(Hazelnut.CONFIG_LAST_DIR) );
			if ( lastDir.exists() && lastDir.isDirectory() ) {
				//logger.debug( "Resetting the last directory: '" + lastDir + "'" );
				fileChooser.setCurrentDirectory(lastDir);
			}
		}

		// Configure the PgUp and PgDn shortcuts
		InputMap im = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), "PgUp");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), "PgDn");
		ActionMap am = contentPane.getActionMap();
		am.put("PgUp", new AbstractAction(){
			@Override public void actionPerformed(ActionEvent arg0) {
				actionLevelUp();
			}
		});
		am.put("PgDn", new AbstractAction() {
			@Override public void actionPerformed(ActionEvent arg0) {
				actionLevelDown();
			}
		});

		// Get the recent files
		recentFiles = new LinkedList<String>(
			Arrays.asList(
				config.getStringArray(Hazelnut.CONFIG_RECENT_FILES)
			)
		);
		updateRecentFiles();

	}

	private void selectLevelNum(int num) {
		if ( num >= 0 && num < 29 ) {
			// Level number is correct
			logger.info("Level #" + (num + 1) + " selected");

			selectedLevelNum = num;
			selectLevel(new Level( loadedRom.getLevel(num) ));

		} else {
			// Level number is incorrect
			logger.warn("Level #" + (num + 1) + " is an incorrect level");

			selectLevel(null);
		}

		updateTitle();
	}

	private void selectLevel(Level level) {
		selectedLevel = level;

		if ( level == null ) {
			setLevelFeaturesEnabled(false);
		} else {
			levelHasChanged = false;
			setLevelFeaturesEnabled(true);
		}

		propertiesPanel.setLevel(selectedLevel);
		levelDisplay.setLevel(selectedLevel);
		updateDisplay();
		actionToggle(false);
	}

	private Palette getConfigPalette(String confKey, Palette dflt, boolean forceC0Transparent) {
		return Optional.ofNullable(config.getString(confKey))
			.map(str -> Palette.parse(str))
			.map(pal -> forceC0Transparent ? pal.cloneWith(0, null) : pal)
			.orElse(dflt);
	}

	private void updateDisplay() {
		if ( selectedLevel != null ) {
			final var palCombos = getConfigPalette(Hazelnut.CONFIG_PALETTE_COMBOS, PALETTE_COMBOS, false);
			final var palSpawn = getConfigPalette(Hazelnut.CONFIG_PALETTE_SPAWN, PALETTE_SPAWN, true);
			final var palItems = getConfigPalette(Hazelnut.CONFIG_PALETTE_ITEMS, PALETTE_ITEMS, true);
			final var palDoorItems = getConfigPalette(Hazelnut.CONFIG_PALETTE_DOOR_ITEMS, PALETTE_DOOR_ITEMS, true);
			final var palEnemiesLight = getConfigPalette(Hazelnut.CONFIG_PALETTE_ENEMIES_LIGHT, PALETTE_ENEMIES_LIGHT, true);
			final var palEnemiesDark = getConfigPalette(Hazelnut.CONFIG_PALETTE_ENEMIES_DARK, PALETTE_ENEMIES_DARK, true);

			levelDisplay.setComboInfoMap(loadedRom.getComboInfoMap());
			levelDisplay.setComboSet(
				loadedRom.getThemeSpriteSet(selectedLevel.getRomLevel().getTheme().getValue()),
				palCombos
			);
			levelDisplay.setBugsSprite(loadedRom.getBugsSprite(), palSpawn);
			levelDisplay.setItemSet(loadedRom.getItemSpriteSet(), palItems, palDoorItems);
			levelDisplay.setEnemySet(
				loadedRom.getEnemySpriteSet(selectedLevel.getEnemyGroup().getIndex()),
				palEnemiesLight, palEnemiesDark
			);
			levelDisplay.setHelpSet(helpSet);
			levelDisplay.updateSize();

			scrollPane.validate();

			selectorPanel.setComboInfoMap(loadedRom.getComboInfoMap());

			selectorPanel.comboSelector.setComboInfoMap(loadedRom.getComboInfoMap());
			selectorPanel.comboSelector.setComboSet(levelDisplay.getComboSet());
			selectorPanel.comboSelector.setHelpSet(levelDisplay.getHelpSet());

			selectorPanel.entitySelector.setSpawnImage(levelDisplay.getSpawnImage());
			selectorPanel.entitySelector.setItemSet(levelDisplay.getItemSet());
			selectorPanel.entitySelector.setDoorItemSet(levelDisplay.getDoorItemSet());
			selectorPanel.entitySelector.setEnemySet(levelDisplay.getEnemySet());
			selectorPanel.entitySelector.updateDisplay();
		}
	}

	private void updateTitle() {
		setTitle("Hazelnut "
			+ (loadedRom == null ? "" : " - " + loadedFile)
			+ (romHasChanged ? "*" : "")
			+ (selectedLevel == null ? "" : " - Level " + (selectedLevelNum + 1))
			+ (levelHasChanged ? "*" : "")
		);

		updateSaveFeatures();
	}

	private void updateSaveFeatures() {
		if ( romFeaturesEnabled ) {
			menuSaveRom.setEnabled(romHasChanged);
			barSaveRom.setEnabled(romHasChanged);
		}
		if ( levelFeaturesEnabled ) {
			menuReloadLevel.setEnabled(levelHasChanged);
			menuSaveLevel.setEnabled(levelHasChanged);
			barReloadLevel.setEnabled(levelHasChanged);
			barSaveLevel.setEnabled(levelHasChanged);
		}
	}

	private void updateRecentFiles() {
		// Clean
		menuRecentFiles.removeAll();

		// Remove while recentFiles > N
		while ( recentFiles.size() >= 8 ) {
			recentFiles.removeLast();
		}

		// Add every menu item
		if ( recentFiles != null ) {
			for ( String str : recentFiles ) {
				final JMenuItem mi = new JMenuItem( str );
				menuRecentFiles.add(mi);
				mi.addActionListener(new ActionListener(){
					@Override public void actionPerformed(ActionEvent ae) {
						String fileName = mi.getText();
						actionOpenRom(fileName);
					}
				});
			}
		}
	}

	protected boolean checkRomModified() {
		if ( !checkLevelModified() ) {
			return false;
		}

		if ( romHasChanged ) {
			int res = JOptionPane.showConfirmDialog(this,
				"The currently open ROM has modified levels but is not saved to disk.\n"
					+ "Do you want to save it now?",
				"Confirm ROM Changes",
				JOptionPane.YES_NO_CANCEL_OPTION);
			if ( res == JOptionPane.YES_OPTION ) {
				actionSaveRom();
			} else if ( res == JOptionPane.NO_OPTION ) {
				// no-op
			} else {
				return false;
			}
		}

		updateTitle();
		return true;
	}

	private boolean checkLevelModified() {
		if ( levelHasChanged ) {
			int res = JOptionPane.showConfirmDialog(this,
				"The currently selected level has changed but is not saved to the ROM.\n"
					+ "Do you want to save it now?",
				"Confirm Level Changes",
				JOptionPane.YES_NO_CANCEL_OPTION);

			if ( res == JOptionPane.YES_OPTION ) {
				actionSaveLevel();

			} else if ( res != JOptionPane.NO_OPTION ) {
				return false;

			}
		}

		updateTitle();
		return true;
	}

	private void actionScale(boolean toolBar) {
		if ( toolBar ) {
			scaleMenuItem.setSelected(scaleButton.isSelected());
		} else {
			scaleButton.setSelected(scaleMenuItem.isSelected());
		}

		boolean selected = scaleButton.isSelected();
		levelDisplay.setScale(selected ? 2 : 1);
		scrollPane.validate();
		config.setProperty(Hazelnut.CONFIG_IS_SCALED, selected);
	}

	private void actionGrid(boolean toolBar) {
		if ( toolBar ) {
			gridMenuItem.setSelected(gridButton.isSelected());
		} else {
			gridButton.setSelected(gridMenuItem.isSelected());
		}

		boolean selected = gridButton.isSelected();
		levelDisplay.setShowGrid(selected);
		scrollPane.validate();
		config.setProperty(Hazelnut.CONFIG_SHOW_GRID, selected);
	}

	private void actionPreferences() {
		final var cd = new SettingsDialog();
		cd.setLocationRelativeTo(null);
		cd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

		cd.setVisible(true);

		updateDisplay();
	}

	private void actionTilesSpriteViewer() {
		final var cd = new TilesetSpriteViewerDialog(loadedRom);
		cd.setLocationRelativeTo(null);
		cd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

		cd.setVisible(true);
	}

	private void setRomFeaturesEnabled(boolean enabled) {
		romFeaturesEnabled = enabled;

		menuSaveRom.setEnabled(enabled);
		menuSaveRomAs.setEnabled(enabled);
		menuLoadLevel.setEnabled(enabled);
		barSaveRom.setEnabled(enabled);
		barLoadLevel.setEnabled(enabled);

		mntmSpriteView.setEnabled(enabled);

		if ( !enabled ) {
			setLevelFeaturesEnabled(false);
		}

		updateSaveFeatures();
	}

	private void setLevelFeaturesEnabled(boolean enabled) {
		levelFeaturesEnabled = enabled;

		menuReloadLevel.setEnabled(enabled);
		menuSaveLevel.setEnabled(enabled);

		menuExportLevel.setEnabled(enabled);
		menuImportLevel.setEnabled(enabled);

		menuClearLevel.setEnabled(enabled);

		menuViewSpawn.setEnabled(enabled);
		menuViewItems.setEnabled(enabled);
		menuViewDoorItems.setEnabled(enabled);
		menuViewEnemies.setEnabled(enabled);
		menuViewTypes.setEnabled(enabled);
		menuViewColl.setEnabled(enabled);
    menuViewWarns.setEnabled(enabled);

		barReloadLevel.setEnabled(enabled);
		barSaveLevel.setEnabled(enabled);

		barExportLevel.setEnabled(enabled);
		barImportLevel.setEnabled(enabled);

		barClearLevel.setEnabled(enabled);

		barViewSpawn.setEnabled(enabled);
		barViewItems.setEnabled(enabled);
		barViewDoorItems.setEnabled(enabled);
		barViewEnemies.setEnabled(enabled);
		barViewTypes.setEnabled(enabled);
		barViewColl.setEnabled(enabled);
    barViewWarns.setEnabled(enabled);
		//barSelectLevel.setEnabled( enabled );

		propertiesPanel.setEnabled(enabled);
		selectorPanel.setEnabled(enabled);

		scaleButton.setEnabled(enabled);
		scaleMenuItem.setEnabled(enabled);
		gridButton.setEnabled(enabled);
		gridMenuItem.setEnabled(enabled);

		if ( enabled ) {
			setRomFeaturesEnabled(true);
		}

		updateSaveFeatures();
	}

	private void actionToggle(boolean toolBar) {
		// Sync both menu and toolbar
		if ( toolBar ) {
			menuViewSpawn.setSelected(barViewSpawn.isSelected());
			menuViewItems.setSelected(barViewItems.isSelected());
			menuViewDoorItems.setSelected(barViewDoorItems.isSelected());
			menuViewEnemies.setSelected(barViewEnemies.isSelected());
			menuViewTypes.setSelected(barViewTypes.isSelected());
			menuViewColl.setSelected(barViewColl.isSelected());
			menuViewWarns.setSelected(barViewWarns.isSelected());
		} else {
			barViewSpawn.setSelected(menuViewSpawn.isSelected());
			barViewItems.setSelected(menuViewItems.isSelected());
			barViewDoorItems.setSelected(menuViewDoorItems.isSelected());
			barViewEnemies.setSelected(menuViewEnemies.isSelected());
			barViewTypes.setSelected(menuViewTypes.isSelected());
			barViewColl.setSelected(menuViewColl.isSelected());
			barViewWarns.setSelected(menuViewWarns.isSelected());
		}

		// Update the level display
		levelDisplay.setDisplayFlags(
			barViewSpawn.isSelected(), barViewItems.isSelected(),
			barViewEnemies.isSelected(), barViewDoorItems.isSelected(),
			barViewTypes.isSelected(), barViewColl.isSelected(),
      barViewWarns.isSelected()
		);

		// Update the combo selector
		selectorPanel.comboSelector.setDisplayComboType(barViewTypes.isSelected());
		selectorPanel.comboSelector.setDisplayComboCollision(barViewColl.isSelected());

		// Update the program configuration
		config.setProperty(Hazelnut.CONFIG_VIEW_SPAWN, barViewSpawn.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_ITEMS, barViewItems.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_DOOR_ITEMS, barViewDoorItems.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_ENEMIES, barViewEnemies.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_CTYPES, barViewTypes.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_COLL, barViewColl.isSelected());
		config.setProperty(Hazelnut.CONFIG_VIEW_WARNS, barViewWarns.isSelected());
	}

	private void actionOpenRom(String fileName) {
		if ( checkRomModified() ) {

			int res = fileName == null
				? fileChooser.showOpenDialog(this)
				: JFileChooser.APPROVE_OPTION;

			if ( res == JFileChooser.APPROVE_OPTION ) {
				File romFile = fileName == null
					? fileChooser.getSelectedFile()
					: new File( fileName );

				try {
					logger.trace("loading file: '" + romFile + "'");

					// Load it
					final var stTime = System.nanoTime();
					var rom = CrazyCastleRom.loadFromFile(romFile);
					if (rom == null) {
						logger.trace("could not determine ROM edition...");
						final var edition = EditionSelectDialog.showSelector();
						if (edition == null) {
							return;
						}

						logger.debug("user selected edition: " + edition.getName());
						rom = CrazyCastleRom.loadFromFile(romFile, edition);
						if (rom == null) {
							return;
						}
					}

					// Everything went fine, update the fields
					logger.info(
						"file '" + romFile + "' loaded as '" + rom.getEdition().getName() + "'"
						+ " in " + (((System.nanoTime() - stTime) / 100000) / 10d) + "ms"
					);
					loadedFile = romFile;
					loadedRom = rom;
					romHasChanged = false;
					setRomFeaturesEnabled(true);

					String path = loadedFile.getAbsolutePath();
					recentFiles.remove(path);
					recentFiles.addFirst(path);
					updateRecentFiles();

					selectLevelNum(0);

				} catch ( Exception exc ) {
					logger.info("errror loading '" + romFile + "': " + exc);
					exc.printStackTrace();
					JOptionPane.showMessageDialog(this,
						"An error ocurred while loading the file\n\n"
							+ "  File: " + romFile + "\n"
							+ "  Error: " + exc,
						"Error", JOptionPane.ERROR_MESSAGE);
				} finally {
					updateTitle();
				}
			}
		}
	}

	private boolean actionSaveRom() {
		final var config = Hazelnut.getConfiguration();
		checkLevelModified();

		try {

			final var stTime = System.nanoTime();

			if (config.getBoolean(Hazelnut.CONFIG_SAVE_BACKUP, true)) {
				final var backupFile = new File(loadedFile.getParent(), loadedFile.getName() + "." + stTime + ".backup");
				loadedFile.renameTo(backupFile);
				logger.info("Target file '" + loadedFile + "' backed up to '" + backupFile + "'");
			}

			final var saveOptions = new SaveOptions( true, true );
			loadedRom.saveToFile(loadedFile, saveOptions);
			romHasChanged = false;

			logger.info("ROM saved to '" + loadedFile + "' in " + (((System.nanoTime() - stTime) / 1000) / 1000d) + "ms");

			return true;
		} catch ( Exception e ) {
			logger.info("Errror saving '" + loadedFile + "': " + e);
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
				"An error ocurred while saving the file\n\n"
					+ "File: " + loadedFile + "\n"
					+ "Error: " + e,
				"Error", JOptionPane.ERROR_MESSAGE);
		} finally {
			updateTitle();
		}

		return false;
	}

	private void actionSaveRomAs() {
		final var res = fileChooser.showSaveDialog(this);

    if (res == JFileChooser.APPROVE_OPTION) {
      final var oldFile = loadedFile;
      loadedFile = fileChooser.getSelectedFile();

      if ( !actionSaveRom() ) {
        loadedFile = oldFile;
      }

      updateTitle();
    }
	}

	private void actionLoadLevel() {
		LevelSelectDialog lsd = new LevelSelectDialog( selectedLevelNum + 1 );
		lsd.setVisible(true);

		if ( lsd.isAccepted() ) {
			if ( checkLevelModified() ) {
				selectLevelNum(lsd.getSelectedNumber() - 1);
			}
		}
	}

	private void actionLevelUp() {
		if ( levelFeaturesEnabled && selectedLevelNum < 28 && checkLevelModified() ) {
			selectLevelNum(selectedLevelNum + 1);
		}
	}

	private void actionLevelDown() {
		if ( levelFeaturesEnabled && selectedLevelNum > 0 && checkLevelModified() ) {
			selectLevelNum(selectedLevelNum - 1);
		}
	}

	private void actionReloadLevel() {
		if ( levelHasChanged ) {
			int res = JOptionPane.showConfirmDialog(this,
				"If you reload this level, unsaved changes will be lost.\n"
					+ "Are you sure you want to reload this level?",
				"Reload Level Confirm", JOptionPane.YES_NO_OPTION);
			if ( res == JOptionPane.YES_OPTION ) {
				selectLevelNum(selectedLevelNum);
			}
		}

		updateTitle();
	}

	private void actionSaveLevel() {
		selectedLevel.getRomLevel().prune(loadedRom.getComboInfoMap());

		loadedRom.setLevel(selectedLevelNum, selectedLevel);
		levelHasChanged = false;
		romHasChanged = true;

		updateTitle();
		updateDisplay();
	}

	private void actionClearLevel() {
		int res = JOptionPane.showConfirmDialog(this,
			"This action will delete everything in the level.\n"
				+ "Are you sure you want to clear this level?",
			"Clear Level Confirm", JOptionPane.YES_NO_OPTION);

		if ( res == JOptionPane.YES_OPTION ) {
			RomLevel rl = selectedLevel.getRomLevel();

			rl.getGrid().clear();

			for (Iterator<?> it = rl.getItems().iterator(); it.hasNext(); ) {
				it.next();
				it.remove();
			}

			for (Iterator<?> it = rl.getDoorItems().iterator(); it.hasNext(); ) {
				it.next();
				it.remove();
			}

			for (Iterator<?> it = rl.getEnemies().iterator(); it.hasNext(); ) {
				it.next();
				it.remove();
			}

			levelDisplay.repaint();
			levelHasChanged = true;
		}

		updateTitle();
	}

	private void actionImportLevel() {
		int res = hlfFileChooser.showOpenDialog(this);

		if ( res == JFileChooser.APPROVE_OPTION ) {
			File file = hlfFileChooser.getSelectedFile();
			try {
				Level level = HazelnutLevelFormat.getInstance().loadLevelFromFile(file);

				selectLevel(level);
				levelHasChanged = true;

			} catch ( Exception e ) {
				logger.info("Error loading the file: '" + file + "'");
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
					"An error ocurred while loading the file\n\n"
						+ "File: " + loadedFile + "\n"
						+ "Error: " + e,
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void actionExportLevel() {
		int res = hlfFileChooser.showSaveDialog(this);

		if ( res == JFileChooser.APPROVE_OPTION ) {
			File file = hlfFileChooser.getSelectedFile();
			try {
				HazelnutLevelFormat.getInstance().saveLevelToFile(selectedLevel, file);
			} catch ( Exception e ) {
				logger.info("Error saving the file: '" + file + "'");
				e.printStackTrace();
				JOptionPane.showMessageDialog(this,
					"An error ocurred while saving the file\n\n"
						+ "File: " + loadedFile + "\n"
						+ "Error: " + e,
					"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void actionExit() {
		logger.trace("Exit requested...");

		boolean close = true;
		if ( !checkRomModified() ) {
			close = false;
		}

		if ( close && config.getBoolean(Hazelnut.CONFIG_CONFIRM_EXIT, true) ) {
			int res = JOptionPane.showConfirmDialog(
				this,
				"Do you really want to close Hazelnut?",
				"Confirm",
				JOptionPane.YES_NO_OPTION
			);
			close = res==JOptionPane.OK_OPTION;
		}

		if ( close ) {
			logger.info("Saving configuration values...");

			boolean maximum = (getExtendedState() & JFrame.MAXIMIZED_BOTH)!=0;

			Point loc = getLocation();
			Dimension size = getSize();

			logger.debug(
				"Saving the GUI state: ["
				+ loc.getX() + "," + loc.getY() + " : "
				+ size.getWidth() + "," + size.getHeight() + " : "
				+ (maximum ? "MAX" : "-") + "]"
			);
			config.setProperty(Hazelnut.CONFIG_FRAME_MAXIMUM, maximum);
			config.setProperty(Hazelnut.CONFIG_FRAME_LOCATION_X, (int)loc.getX());
			config.setProperty(Hazelnut.CONFIG_FRAME_LOCATION_Y, (int)loc.getY());
			config.setProperty(Hazelnut.CONFIG_FRAME_SIZE_WIDTH, (int)size.getWidth());
			config.setProperty(Hazelnut.CONFIG_FRAME_SIZE_HEIGHT, (int)size.getHeight());

			logger.debug("Saving the last opened directory: '"
				+ fileChooser.getCurrentDirectory() + "'");
			config.setProperty(Hazelnut.CONFIG_LAST_DIR,
				fileChooser.getCurrentDirectory());

			logger.debug("Saving the recently opened files");
			config.clearProperty(Hazelnut.CONFIG_RECENT_FILES);
			for ( String str : recentFiles ) {
				config.addProperty(Hazelnut.CONFIG_RECENT_FILES, str);
			}

			logger.trace("Closing application...");
			dispose();
		}
	}
}
