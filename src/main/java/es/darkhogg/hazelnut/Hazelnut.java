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

import com.formdev.flatlaf.FlatLightLaf;
import es.darkhogg.util.Version;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.EnhancedPatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Hazelnut {
	public static final String CONFIG_VIEW_SPAWN = "Hazelnut.gui.viewSpawn";
	public static final String CONFIG_VIEW_ITEMS = "Hazelnut.gui.viewItems";
	public static final String CONFIG_VIEW_DOOR_ITEMS = "Hazelnut.gui.viewDoorItems";
	public static final String CONFIG_VIEW_ENEMIES = "Hazelnut.gui.viewEnemies";
	public static final String CONFIG_VIEW_CTYPES = "Hazelnut.gui.viewHelp";
	public static final String CONFIG_IS_SCALED = "Hazelnut.gui.scaled";
	public static final String CONFIG_SHOW_GRID = "Hazelnut.gui.grid";
	public static final String CONFIG_FRAME_MAXIMUM = "Hazelnut.gui.maximum";
	public static final String CONFIG_FRAME_LOCATION_X = "Hazelnut.gui.location.x";
	public static final String CONFIG_FRAME_LOCATION_Y = "Hazelnut.gui.location.y";
	public static final String CONFIG_FRAME_SIZE_WIDTH = "Hazelnut.gui.size.width";
	public static final String CONFIG_FRAME_SIZE_HEIGHT = "Hazelnut.gui.size.height";
	public static final String CONFIG_LAST_DIR = "Hazelnut.gui.lastDirectory";
	public static final String CONFIG_LOOK_AND_FEEL = "Hazelnut.gui.lookAndFeel";
	public static final String CONFIG_CONFIRM_EXIT = "Hazelnut.gui.confirmExit";
	public static final String CONFIG_SAVE_BACKUP = "Hazelnut.gui.saveBackup";
	public static final String CONFIG_VIEW_COLL = "Hazelnut.gui.viewColl";
	public static final String CONFIG_VIEW_WARNS = "Hazelnut.gui.viewWarns";
	public static final String CONFIG_RECENT_FILES = "Hazelnut.gui.recentFiles";
	public static final String CONFIG_PALETTE_COMBOS = "Hazelnut.gui.paletteCombos";
	public static final String CONFIG_PALETTE_SPAWN = "Hazelnut.gui.paletteSpawn";
	public static final String CONFIG_PALETTE_ITEMS = "Hazelnut.gui.paletteItems";
	public static final String CONFIG_PALETTE_DOOR_ITEMS = "Hazelnut.gui.paletteDoorItems";
	public static final String CONFIG_PALETTE_ENEMIES_LIGHT = "Hazelnut.gui.paletteEnemiesLight";
	public static final String CONFIG_PALETTE_ENEMIES_DARK = "Hazelnut.gui.paletteEnemiesDark";

	/**
	 * Version of the program
	 */
	private static final Version VERSION = new Version( 1, 1, 0 );

	/**
	 * Logger for the whole application
	 */
	private static final Logger LOGGER;
	static {
		Logger.getRootLogger().addAppender(NullAppender.getNullAppender());

		try {
			LOGGER = Logger.getLogger(Hazelnut.class);
			LOGGER.addAppender(new ConsoleAppender(
				new EnhancedPatternLayout( "%d{HH:mm:ss.SSS} %5p : %m%n" ),
				ConsoleAppender.SYSTEM_OUT
			));
			LOGGER.addAppender(new DailyRollingFileAppender(
				new EnhancedPatternLayout( "%d{HH:mm:ss.SSS} %5p : %m%n" ),
				"log" + System.getProperty("file.separator") + "Hazelnut.log",
				"'.'yyyy-MM-dd"
			));
			//LOGGER.addAppender( new SwingPanelAppender( FRAME.getLogPanel(), true ) );
			LOGGER.setLevel(Level.TRACE);
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}

	/**
	 * Configuration for the whole application
	 */
	private static final Configuration CONFIG;
	static {
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
			new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
			.configure(new Parameters().properties()
				.setFileName("Hazelnut.properties")
				.setThrowExceptionOnMissing(false)
				.setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
				.setIncludesAllowed(false));
		builder.setAutoSave(true);

		try {
			CONFIG = builder.getConfiguration();
		} catch (ConfigurationException exc) {
			throw new RuntimeException( exc );
		}
	}

	/**
	 * Main frame of the program
	 */
	private static final EditorFrame FRAME;
	static {
		FRAME = new EditorFrame();
	}

	/**
	 * Returns the current version of the application as an integer, where each
	 * byte is a version component.
	 *
	 * @return This application version
	 */
	public static Version getVersion() {
		return VERSION;
	}

	/**
	 * Returns an already initialized and configured Logger for the whole
	 * application.
	 *
	 * @return This application logger
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Returns an already created and initialized JFrame which is the main
	 * frame for this application.
	 *
	 * @return This application main frame
	 */
	public static EditorFrame getFrame() {
		return FRAME;
	}

	/**
	 * Returns an already created, loaded and ready to be used Configuration
	 * for the whole application.
	 *
	 * @return This application configuration object
	 */
	public static Configuration getConfiguration() {
		return CONFIG;
	}

	/**
	 * Terminates the application in at most <i>time</i> milliseconds for
	 * every alive thread.
	 *
	 * @param time Number of milliseconds to wait for each thread to terminate
	 */
	public static void terminate(long time) {
		Logger logger = getLogger();
		logger.info("Terminating application...");

		try {
			getFrame().dispose();

			// Get the root thread group
			ThreadGroup rootThreadGroup = Thread.currentThread().getThreadGroup();
			while ( rootThreadGroup.getParent() != null ) {
				rootThreadGroup = rootThreadGroup.getParent();
			}

			// Declare some collections
			Queue<ThreadGroup> threadGroups = new LinkedList<>();
			Queue<Thread> threads = new LinkedList<>();

			// Get ALL groups
			threadGroups.add(rootThreadGroup);
			while ( !threadGroups.isEmpty() ) {
				ThreadGroup group = threadGroups.remove();

				Thread[] subThreads = new Thread[ group.activeCount() * 2 ];
				//group.enumerate( subThreads );
				for ( Thread subThread : subThreads ) {
					if ( subThread != null ) {
						threads.add(subThread);
					}
				}

				ThreadGroup[] subThreadGroups = new ThreadGroup[ group.activeGroupCount() * 2 ];
				for ( ThreadGroup subThreadGroup : subThreadGroups ) {
					if ( subThreadGroup != null ) {
						threadGroups.add(subThreadGroup);
					}
				}
			}

			// Join a maximum of time milliseconds for all non-daemon threads
			while ( !threads.isEmpty() ) {
				Thread thread = threads.remove();
				LOGGER.trace(thread);

				if ( !thread.isDaemon() && thread != Thread.currentThread() ) {
					logger.trace("Waiting for thread '" + thread.getName() + "'");
					thread.join(time);
					if ( thread.isAlive() ) {
						logger.trace("Interrupting thread '" + thread.getName() + "'");
						thread.interrupt();
					}
				}
			}

		} catch ( Throwable exc ) {
			LOGGER.warn("Interrupted while terminating application", exc);

		} finally {
			// Exit the program
			System.exit(0);
		}
	}

	/**
	 * Runs the application
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
	throws Exception {
		// Print some version information
		LOGGER.info("Hazelnut " + VERSION);

		LOGGER.trace("Selecting Look&Feel...");

		// Select the L&F from configuration or the default if not present
		String slaf = CONFIG.getString(Hazelnut.CONFIG_LOOK_AND_FEEL);
		if ( slaf == null ) {
			LOGGER.info("Configuration entry for L&F missing, creating default");
			slaf = FlatLightLaf.class.getCanonicalName();
		}

		// Set it or print an error
		try {
			LOGGER.trace("Trying to select L&F: " + slaf);
			UIManager.setLookAndFeel(slaf);
		} catch ( Exception e ) {
			LOGGER.warn("Error while selecting the L&F \"" + slaf
				+ "\", leaving default");
		}

		// Update the configuration with the currently selected L&F
		LookAndFeel laf = UIManager.getLookAndFeel();
		LOGGER.debug("L&F selected: " + laf.getName() + " (" + laf.getClass().getName() + ")");
		CONFIG.setProperty(Hazelnut.CONFIG_LOOK_AND_FEEL, laf.getClass().getName());

		// Load the frame
		LOGGER.trace("Launching main frame...");
		SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				SwingUtilities.updateComponentTreeUI(FRAME);
				FRAME.setVisible(true);
			}
		});
	}
}
