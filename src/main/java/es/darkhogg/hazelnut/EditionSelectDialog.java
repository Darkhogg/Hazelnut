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

import es.darkhogg.crazycastle.CrazyCastleEdition;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class EditionSelectDialog extends JDialog {

	private CrazyCastleEdition edition;

	private final JButton btnAccept;
	private final JList<String> listEditions;

	EditionSelectDialog() {
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setTitle("Hazelnut - ROM Edition Select");
		setIconImages(Arrays.asList(
			Toolkit.getDefaultToolkit().getImage(
        EditorFrame.class.getResource("/es/darkhogg/hazelnut/logo_16.png")
      ),
      Toolkit.getDefaultToolkit().getImage(
        EditorFrame.class.getResource("/es/darkhogg/hazelnut/logo_24.png")
      )
    ));
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent ev) {
				actionCancel();
			}
		});

		final var contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(4, 4));
		setContentPane(contentPane);

		btnAccept = new JButton("Select");
		btnAccept.addActionListener(action -> actionAccept());

		final var btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(action -> actionCancel());

		final var buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER, 4, 4));
		buttonsPane.add(btnAccept);
		buttonsPane.add(btnCancel);
		add(buttonsPane, BorderLayout.SOUTH);

		final var lblInfo = new JLabel();
		lblInfo.setText("Cannot determine ROM layout, please pick one:");
		add(lblInfo, BorderLayout.NORTH);

		final var editions = CrazyCastleEdition.values();
		final var listItems = new String[editions.length];
		for (var i = 0; i < listItems.length; i++) {
			listItems[i] = editions[i].getTitle() + " | " + editions[i].getName();
		}
		listEditions = new JList<>(listItems);
		listEditions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listEditions.addListSelectionListener(evt -> updateGui());
		add(listEditions, BorderLayout.CENTER);

		pack();
		setResizable(false);

		updateGui();
	}

	private void updateGui() {
		final var editions = CrazyCastleEdition.values();

		final var selIdx = listEditions.getSelectedIndex();
		edition = selIdx >= 0 ? editions[selIdx] : null;

		btnAccept.setEnabled(edition != null);
	}

	public static CrazyCastleEdition showSelector() {
		final var dialog = new EditionSelectDialog();
		dialog.setLocationRelativeTo(null);
		dialog.setModalityType(ModalityType.DOCUMENT_MODAL);

		dialog.setVisible(true);

		return dialog.getEdition();
	}

	public CrazyCastleEdition getEdition() {
		return edition;
	}

	private void actionAccept() {
		dispose();
	}

	private void actionCancel() {
		edition = null;
		dispose();
	}
}
