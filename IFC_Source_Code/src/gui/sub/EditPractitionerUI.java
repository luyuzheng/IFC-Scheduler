package gui.sub;

import backend.DataService.DataServiceImpl;
import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import backend.DataTransferObjects.*;

/**
 * Popup that allows a single practitioner's details to be edited
 */
public class EditPractitionerUI extends JDialog implements ActionListener {
	private static EditPractitionerUI editPractitionerUI;

	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JComboBox typeCombo = new JComboBox();
	private JTextArea note = new JTextArea();
	private JTextField apptLengthField = new JTextField();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");

	private static PractitionerDto p;

	private JPanel panel;
	
	private EditPractitionerUI(String name) {
		setModal(true);
		setTitle(name);

		panel = makeMainPanel();

		add(panel, BorderLayout.CENTER);		

		setResizable(false);

	}
	
	private JPanel makeMainPanel() {

		firstNameField.setText(p.getFirst());
		lastNameField.setText(p.getLast());
		note.setText((p.getNotes()).replaceAll("\t\t", "\n"));
		apptLengthField.setText("" + p.getApptLength());

		JPanel fullPanel = new JPanel(new BorderLayout());
		JPanel input = new JPanel(new GridLayout(0,1));

		JPanel namePanel = new JPanel(new GridLayout(0, 2));
		JPanel firstNamePanel = new JPanel(new BorderLayout());
		JPanel lastNamePanel = new JPanel(new BorderLayout());
		JLabel firstNameLabel = new JLabel("First Name: ");
		JLabel lastNameLabel = new JLabel("Last Name: ");
		firstNameLabel.setFont(Constants.PARAGRAPH);
		lastNameLabel.setFont(Constants.PARAGRAPH);
		firstNamePanel.add(firstNameLabel, BorderLayout.NORTH);
		firstNameField.setColumns(8);
		firstNameField.setFont(Constants.PARAGRAPH);
		firstNamePanel.add(firstNameField, BorderLayout.CENTER);
		lastNamePanel.add(lastNameLabel, BorderLayout.NORTH);
		lastNameField.setColumns(8);
		lastNameField.setFont(Constants.PARAGRAPH);
		lastNamePanel.add(lastNameField, BorderLayout.CENTER);

		namePanel.add(firstNamePanel);
		namePanel.add(lastNamePanel);
		input.add(namePanel);

		JPanel apptLengthPanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("Appt Length: ", JLabel.CENTER);
		label.setFont(Constants.PARAGRAPH);
		apptLengthPanel.add(label, BorderLayout.NORTH);
		apptLengthField.setFont(Constants.PARAGRAPH);
		apptLengthPanel.add(apptLengthField, BorderLayout.CENTER);

		input.add(apptLengthPanel);

		JPanel typePanel = new JPanel(new BorderLayout());
		label = new JLabel("Type: ", JLabel.CENTER);
		label.setFont(Constants.PARAGRAPH);
		typePanel.add(label, BorderLayout.NORTH);
		typeCombo = new JComboBox((DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes().toArray()));
		typeCombo.setFont(Constants.DIALOG);
		typeCombo.setSelectedItem(p.getType());
		JPanel typeInnerPanel = new JPanel(new BorderLayout());
		typeInnerPanel.add(typeCombo, BorderLayout.CENTER);
		JButton newTypeButton = new JButton("New Type");
		newTypeButton.setFont(Constants.DIALOG);
		newTypeButton.addActionListener(this);
		newTypeButton.setActionCommand("New Type");
		typeInnerPanel.add(newTypeButton, BorderLayout.EAST);
		typePanel.add(typeInnerPanel, BorderLayout.CENTER);
		input.add(typePanel);

		JPanel notePanel = new JPanel(new BorderLayout());
		label = new JLabel("Practitioner Notes: ", JLabel.CENTER);
		label.setFont(Constants.PARAGRAPH);
		notePanel.add(label, BorderLayout.NORTH);
		note.setFont(Constants.PARAGRAPH);
		note.setLineWrap(true);
		note.setWrapStyleWord(true);
		JScrollPane notePane = new JScrollPane(note);
    	notePane.setPreferredSize(new Dimension(200,100)); //Added by Aakash    			
		notePanel.add(notePane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		okButton.setActionCommand("okNew");
		okButton.addActionListener(this);
		okButton.setFont(Constants.DIALOG);
		cancelButton.setFont(Constants.DIALOG);
		cancelButton.addActionListener(this);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		fullPanel.add(input, BorderLayout.NORTH);
		fullPanel.add(notePanel, BorderLayout.CENTER);
		fullPanel.add(buttonPanel, BorderLayout.SOUTH);
		return fullPanel;
	}


	public static PractitionerDto ShowDialog(Component owner, PractitionerDto pat) {
		p = pat;
		editPractitionerUI = new EditPractitionerUI("Edit Practitioner");
		editPractitionerUI.pack();
		editPractitionerUI.setLocationRelativeTo(owner);
		editPractitionerUI.setVisible(true);
		return p;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Type")) {
			TypeDto t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			
			boolean exists = false;
			for (int i = 0; i < typeCombo.getItemCount(); i++) {
				if (t.equals(typeCombo.getItemAt(i))) {
					typeCombo.setSelectedItem(t);
					exists = true;
					break;
				}
			}
			if (!exists) {
				typeCombo.addItem(t);
				typeCombo.setSelectedItem(t);
			}
			
			repaint();
			validate();
			return;
		} else if (!e.getActionCommand().equals("Cancel")) {
			JLabel msg = new JLabel();
			msg.setFont(Constants.PARAGRAPH);
			String firstName = firstNameField.getText();
			if (firstName.equals("")) {
				msg.setText("Please enter a first name.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String lastName = lastNameField.getText();
			if (lastName.equals("")) {
				msg.setText("Please enter a last name.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
			}
			String apptLength = apptLengthField.getText();
			try {
				int length = Integer.parseInt(apptLength);
				if (length <= 0) {
					msg.setText("Please enter a valid appointment length.");
					JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (Exception ex) {
				msg.setText("Please enter a valid appointment length.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}		
			String noteText = note.getText().replaceAll("[\r\n]+", "\t\t");
			if (typeCombo.getSelectedIndex() < 0) {
				msg.setText("Please select a type.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			TypeDto t = (TypeDto)typeCombo.getSelectedItem();

			int newApptLength = Integer.parseInt(apptLength);
			if (p.getApptLength() == newApptLength) {
				p.setFirst(firstName);
                p.setLast(lastName);
				p.setNotes(noteText);
				p.setType(t);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.updatePractitionerInfo(p);
			} else {
                                DataServiceImpl.GLOBAL_DATA_INSTANCE.removePractitioner(p);
                     
				p = DataServiceImpl.GLOBAL_DATA_INSTANCE.addPractitioner(t.getTypeID(), firstName, lastName, newApptLength, "" ,noteText);
			}
		}
		editPractitionerUI.setVisible(false);
	}

}