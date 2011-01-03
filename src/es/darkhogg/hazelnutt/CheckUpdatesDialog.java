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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings( "serial" )
public final class CheckUpdatesDialog extends JDialog {
	
	private CheckAndDownloadUpdatesWorker downloadWorker;
	private ApplyUpdatesWorker applyWorker;
	protected EditorFrame frame;
	
	protected JLabel statusLabel;
	protected JProgressBar statusBar;
	protected JButton applyButton;
	private JButton cancelButton;

	public CheckUpdatesDialog ( EditorFrame frame ) {
		super( frame );
		this.frame = frame;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				actionCancel();
			}
		});
		setTitle("Check for Updates");
		downloadWorker = new CheckAndDownloadUpdatesWorker( this );
		downloadWorker.execute();
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		applyButton = new JButton("Apply Update");
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionApply();
			}
		});
		applyButton.setEnabled(false);
		panel.add(applyButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionCancel();
			}
		});
		panel.add(cancelButton);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(10);
		panel_1.add(verticalStrut);
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		panel_1.add(horizontalBox_1);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		horizontalBox_1.add(horizontalStrut_2);
		
		statusLabel = new JLabel("...");
		horizontalBox_1.add(statusLabel);
		
		Component horizontalStrut_3 = Box.createHorizontalStrut(10);
		horizontalBox_1.add(horizontalStrut_3);
		
		Component verticalStrut_2 = Box.createVerticalStrut(10);
		panel_1.add(verticalStrut_2);
		
		Box horizontalBox = Box.createHorizontalBox();
		panel_1.add(horizontalBox);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut);
		
		statusBar = new JProgressBar();
		statusBar.setIndeterminate(true);
		horizontalBox.add(statusBar);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		horizontalBox.add(horizontalStrut_1);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		panel_1.add(verticalStrut_1);
		
		pack();
		setLocationRelativeTo( null );
	}

	private void actionCancel () {
		if ( downloadWorker.isDone() ) {
			
		} else {
			downloadWorker.cancel( false );
		}
		
		dispose();
	}
	
	private void actionApply () {
		int res = JOptionPane.showConfirmDialog(
			this, 
			"Applying the update will backup all the application files " +
				"in a 'bak' directory and then overwrite the files in the " +
				"current directory\nDo you want to apply the updates now?",
			"Confirm Update", JOptionPane.YES_NO_OPTION
		);
		
		if ( res == JOptionPane.YES_OPTION ) {
			try {
				applyWorker = new ApplyUpdatesWorker( this, downloadWorker.get() );
				cancelButton.setEnabled( false );
				applyWorker.execute();
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			} catch ( ExecutionException e ) {
				e.printStackTrace();
			}
		}
	}
}
