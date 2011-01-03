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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 5486190939478880099L;
	
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public AboutDialog () {
		//Logger logger = Hazelnutt.getLogger();
		
		setTitle("Readme");
		setBounds( 100, 100, 660, 440 );
		getContentPane().setLayout( new BorderLayout() );
		contentPanel.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		getContentPane().add( contentPanel, BorderLayout.CENTER );
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		{
			{
				JScrollPane scrollPane = new JScrollPane();
				contentPanel.add(scrollPane);
				JTextArea textArea = new JTextArea();
				scrollPane.setViewportView(textArea);
				textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
				textArea.setRows(50);
				textArea.setColumns(80);
				try {
					BufferedReader r = new BufferedReader(
						new InputStreamReader(
							AboutDialog.class.getResource( "/Readme.txt" ).openConnection().getInputStream()
						)
					);
					StringBuilder sb = new StringBuilder();
					while ( r.ready() ) {
						sb.append( r.readLine() ).append( '\n' );
					}
					textArea.setText( sb.toString() );
				} catch ( Exception e ) {
					textArea.setText( "ERROR: Cannot locate or read Readme.txt" );
					Hazelnutt.getLogger().error( "Error loading 'Readme.txt'" );
					e.printStackTrace();
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout( new FlowLayout( FlowLayout.TRAILING ) );
			getContentPane().add( buttonPane, BorderLayout.SOUTH );
			{
				JButton okButton = new JButton( "Close" );
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand( "OK" );
				buttonPane.add( okButton );
				getRootPane().setDefaultButton( okButton );
			}
		}
		

	}

}
