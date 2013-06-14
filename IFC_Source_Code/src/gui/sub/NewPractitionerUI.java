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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import backend.DataTransferObjects.*;

import java.util.List;

/**
 * Popup that allows new practitioners to be created.
 */
public class NewPractitionerUI extends JDialog implements ActionListener {

	private static NewPractitionerUI newPractitionerUI;
	private List<PractitionerDto> prac = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitioners();
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JComboBox typeCombo;
	private JTextField apptLengthField = new JTextField();
	private JTextArea noteField = new JTextArea();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	
	private JComponent panel;
	JTable pracTable;

	
	private static PractitionerDto practitioner;
	
	private NewPractitionerUI(String name) {
		practitioner = null;
		setModal(true);
		setTitle(name);
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(400,250));
		
		panel = makeNewPracPanel();
		add(panel, BorderLayout.CENTER);

		setResizable(false);
	}
    
    private JComponent makeNewPracPanel() {
    	JPanel fullPanel = new JPanel(new BorderLayout());
    	
    	JPanel topSubpanel = new JPanel(new GridLayout(0, 1));
    	
    	JPanel nameLengthPanel = new JPanel(new GridLayout(0, 3));
    	JPanel firstNamePanel = new JPanel(new BorderLayout());
    	JPanel lastNamePanel = new JPanel(new BorderLayout());
    	JLabel firstNameLabel = new JLabel("First Name: ");
    	JLabel lastNameLabel = new JLabel("Last Name: ");
    	firstNameLabel.setFont(Constants.PARAGRAPH);
    	firstNameField.setFont(Constants.PARAGRAPH);
    	firstNamePanel.add(firstNameLabel, BorderLayout.NORTH);
    	firstNamePanel.add(firstNameField, BorderLayout.CENTER);
    	lastNameLabel.setFont(Constants.PARAGRAPH);
    	lastNameField.setFont(Constants.PARAGRAPH);
    	lastNamePanel.add(lastNameLabel, BorderLayout.NORTH);
    	lastNamePanel.add(lastNameField, BorderLayout.CENTER);
    	JPanel lengthPanel = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("Appt Length (Min): ");
    	label.setFont(Constants.PARAGRAPH);
    	apptLengthField.setFont(Constants.PARAGRAPH);
    	lengthPanel.add(label, BorderLayout.NORTH);
    	lengthPanel.add(apptLengthField, BorderLayout.CENTER);
    	
    	nameLengthPanel.add(firstNamePanel);
    	nameLengthPanel.add(lastNamePanel);
    	nameLengthPanel.add(lengthPanel);
    	topSubpanel.add(nameLengthPanel);
    	
    	
    	JPanel typePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner Type: ");
    	label.setFont(Constants.PARAGRAPH);
    	typePanel.add(label, BorderLayout.NORTH);
    	JPanel typeComboPanel = new JPanel(new BorderLayout());
        ArrayList<TypeDto> typeList = (ArrayList<TypeDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionerTypes();
    	typeCombo = new JComboBox(typeList.toArray());
    	typeCombo.setFont(Constants.DIALOG);
    	typeComboPanel.add(typeCombo, BorderLayout.CENTER);
    	
    	JButton newTypeButton = new JButton("New Type");
    	newTypeButton.addActionListener(this);
    	newTypeButton.setActionCommand("New Type");
    	newTypeButton.setFont(Constants.DIALOG);
    	typeComboPanel.add(newTypeButton, BorderLayout.EAST);
    	typePanel.add(typeComboPanel, BorderLayout.CENTER);
    	topSubpanel.add(typePanel);
    	
    	fullPanel.add(topSubpanel, BorderLayout.NORTH);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner notes: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	notePanel.add(label, BorderLayout.NORTH);
    	noteField.setFont(Constants.PARAGRAPH);
    	noteField.setLineWrap(true);
    	noteField.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(noteField);
    	notePanel.add(notePane, BorderLayout.CENTER);
    	fullPanel.add(notePanel, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("okNew");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	cancelButton.addActionListener(this);
    	cancelButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	fullPanel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return fullPanel;
    }
	
	public static PractitionerDto ShowDialog(Component owner) {
		newPractitionerUI = new NewPractitionerUI("Create New Practitioner");
		newPractitionerUI.pack();
		newPractitionerUI.setLocationRelativeTo(owner);
		newPractitionerUI.setVisible(true);
		return practitioner;
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
		}
		else if (e.getActionCommand().equals("okNew")) {
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
				return;
			}
			String type = ((TypeDto)typeCombo.getSelectedItem()).getTypeName();
			if (type.equals("")) {
				msg.setText("Please enter a type.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int apptLength;
			try {
				apptLength = Integer.parseInt(apptLengthField.getText());
			} catch (Exception ex) {
				msg.setText("Appointment Length must be an integer value.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (apptLength <= 0) {
				msg.setText( "Appointment length must be positive.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String note = noteField.getText().replaceAll("[\r\n]+", "\t\t");
			
			TypeDto t = DataServiceImpl.GLOBAL_DATA_INSTANCE.getType(type);
			if (t == null) {
				t = DataServiceImpl.GLOBAL_DATA_INSTANCE.addNewPractitionerType(type);
			}
			practitioner = DataServiceImpl.GLOBAL_DATA_INSTANCE.addPractitioner(t.getTypeID(), firstName, lastName, apptLength, "", note);
		} else if (e.getActionCommand().equals("okOld")) {
			if (pracTable.getSelectedRow() > -1)
				practitioner = prac.get(pracTable.getSelectedRow());
			else {
				JLabel msg = new JLabel("Please select one of the practitioners in the table, or add a new one.");
				msg.setFont(Constants.PARAGRAPH);
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		newPractitionerUI.setVisible(false);
    }
	
	class PracTableModel extends AbstractTableModel {

		private String[] columnNames = { "Name", "Type", "Appointment Length (Minutes)" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return prac.size();
		}

		public Object getValueAt(int row, int col) {
			PractitionerDto p = prac.get(row);
			if (col == 0) 
				return p.getFirst() + p.getLast();
			else if (col == 1) 
				return p.getTypeName();
			else
				return p.getApptLength();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}