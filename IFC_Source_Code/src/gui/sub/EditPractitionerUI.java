package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import data.Practitioner;
import data.Type;
import data.managers.PractitionerManager;
import data.managers.TypeManager;

public class EditPractitionerUI extends JDialog implements ActionListener {
	private static EditPractitionerUI editPractitionerUI;

	private JTextField nameField = new JTextField();
	private JComboBox typeCombo = new JComboBox();
	private JTextArea note = new JTextArea();
	private JTextField apptLengthField = new JTextField();
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");

	private static Practitioner p;
	private TypeManager tm;

	private JPanel panel;
	
	private EditPractitionerUI(String name) {
		setModal(true);
		setTitle(name);

		panel = makeMainPanel();

		add(panel, BorderLayout.CENTER);		

		setResizable(false);

	}
	
	private JPanel makeMainPanel() {
		tm = new TypeManager();

		nameField.setText(p.getName());
		note.setText((p.getNote()).replaceAll("\t\t", "\n"));
		apptLengthField.setText("" + p.getApptLength());

		JPanel panel = new JPanel(new BorderLayout());
		JPanel input = new JPanel(new GridLayout(0,1));

		JPanel namePanel = new JPanel(new BorderLayout());
		JLabel label = new JLabel("First Name: ");
		namePanel.add(label, BorderLayout.NORTH);
		nameField.setColumns(15);
		namePanel.add(nameField, BorderLayout.CENTER);

		input.add(namePanel);

		JPanel apptLengthPanel = new JPanel(new BorderLayout());
		label = new JLabel("Appt Length: ", JLabel.CENTER);
		apptLengthPanel.add(label, BorderLayout.NORTH);
		apptLengthPanel.add(apptLengthField, BorderLayout.CENTER);

		input.add(apptLengthPanel);

		JPanel typePanel = new JPanel(new BorderLayout());
		label = new JLabel("Type: ", JLabel.CENTER);
		typePanel.add(label, BorderLayout.NORTH);
		typeCombo = new JComboBox(tm.getTypeList().toArray());
		typeCombo.setSelectedIndex(0);
		//Added by Aakash on 14th feb to fix combolist practitioner-type bug
		typeCombo.setSelectedIndex(p.getType().getId());

		JPanel typeInnerPanel = new JPanel(new BorderLayout());
		typeInnerPanel.add(typeCombo, BorderLayout.CENTER);
		JButton newTypeButton = new JButton("New Type");
		newTypeButton.addActionListener(this);
		newTypeButton.setActionCommand("New Type");
		typeInnerPanel.add(newTypeButton, BorderLayout.EAST);
		typePanel.add(typeInnerPanel, BorderLayout.CENTER);
		input.add(typePanel);

		JPanel notePanel = new JPanel(new BorderLayout());
		label = new JLabel("Practitioner notes: ", JLabel.CENTER);
		notePanel.add(label, BorderLayout.NORTH);
		note.setFont(namePanel.getFont());
		note.setLineWrap(true);
		note.setWrapStyleWord(true);
		JScrollPane notePane = new JScrollPane(note);
    	notePane.setPreferredSize(new Dimension(200,100)); //Added by Aakash    			
		notePanel.add(notePane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		okButton.setActionCommand("okNew");
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);

		panel.add(input, BorderLayout.NORTH);
		panel.add(notePanel, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		return panel;
	}


	public static Practitioner ShowDialog(Component owner, Practitioner pat) {
		p = pat;
		editPractitionerUI = new EditPractitionerUI("Edit Practitioner");
		editPractitionerUI.pack();
		editPractitionerUI.setLocationRelativeTo(owner);
		editPractitionerUI.setVisible(true);
		return p;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Type")) {
			data.Type t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			remove(panel);
			panel = makeMainPanel();
			//Added by Aakash on 14th feb to fix combolist practitioner-type bug
			typeCombo.setSelectedIndex(tm.getMaxId());
			add(panel);
			repaint();
			validate();
			return;
		} else {
			String name = nameField.getText();
			if (name.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a name.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String apptLength = apptLengthField.getText();
			try {
				int length = Integer.parseInt(apptLength);
				if (length <= 0) {
					JOptionPane.showMessageDialog(this, "Please enter a valid appointment length.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid appointment length.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}		
			String noteText = note.getText().replaceAll("[\r\n]+", "\t\t");
			if (typeCombo.getSelectedIndex() < 0) {
				JOptionPane.showMessageDialog(this, "Please select a type.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			data.Type t = (data.Type)typeCombo.getSelectedItem();

			PractitionerManager pm = new PractitionerManager();
			int newApptLength = Integer.parseInt(apptLength);
			if (p.getApptLength() == newApptLength) {
				p.setName(name);
				p.setNote(noteText);
				p.setType(t);
				pm.updatePractitioner(p);
			} else {
				Practitioner newPrac = new Practitioner(pm.getNewId(), name, t, newApptLength, noteText);
				pm.retirePractitioner(p);
				pm.addPractitioner(newPrac);
				p = newPrac;
			}
		}
		editPractitionerUI.setVisible(false);
	}

}