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
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

@SuppressWarnings( "serial" )
public final class ConfigDialog extends JDialog {
	
	public ConfigDialog() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				actionCancel();
			}
		});
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ConfigDialog.class.getResource("/es/darkhogg/hazelnutt/icon_preferences.png")));
		setTitle("Hazelnutt - Preferences");
		setModal(true);
		
		JPanel buttonsPanel = new JPanel();
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		FlowLayout fl_buttonsPanel = new FlowLayout(FlowLayout.CENTER, 5, 5);
		buttonsPanel.setLayout(fl_buttonsPanel);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionAccept();
			}
		});
		buttonsPanel.add(btnAccept);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionCancel();
			}
		});
		buttonsPanel.add(btnCancel);
		
		JPanel optionsPanel = new JPanel();
		getContentPane().add(optionsPanel, BorderLayout.CENTER);
		optionsPanel.setLayout(new BorderLayout(0, 0));
		
		/*LookAndFeelInfo[] lafsinfo = UIManager.getInstalledLookAndFeels();
		String[] lafs = new String[ lafsinfo.length ];
		int currLaf = -1;
		for ( int i = 0; i < lafs.length; i++ ) {
			lafs[ i ] = lafsinfo[ i ].getName();
			if ( lafs[i] == UIManager.getLookAndFeel().getName() ) {
				currLaf = i;
			}
		}*/
		
		pack();
	}
	
	public void actionAccept () {
		//Configuration config = Hazelnutt.getConfiguration();
		Logger logger = Hazelnutt.getLogger();
		
		logger.debug( "Saving preferences..." );
		
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
			config.setProperty( "Hazelnutt.gui.lookAndFeel", lafname );
			SwingUtilities.updateComponentTreeUI( Hazelnutt.getFrame() );
		}*/

		logger.info( "Preferences saved" );
		dispose();
	}
	
	public void actionCancel () {
		dispose();
	}

}
