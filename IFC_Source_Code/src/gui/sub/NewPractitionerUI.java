package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import data.Practitioner;
import data.Type;
import data.managers.PractitionerManager;
import data.managers.TypeManager;

public class NewPractitionerUI extends JDialog implements ActionListener {

	private TypeManager tm = new TypeManager();
	private static NewPractitionerUI newPractitionerUI;
	private ArrayList<Practitioner> prac = new PractitionerManager().getPractitionerList();
	
	private JTextField nameField = new JTextField();
	private JComboBox typeCombo;
	private JTextField apptLengthField = new JTextField();
	private JTextArea noteField = new JTextArea();
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	JTable pracTable;
	
	private static Practitioner practitioner;
	
	private NewPractitionerUI(String name) {
		practitioner = null;
		setModal(true);
		setTitle(name);
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(275,250));
		add(makeNewPracPanel());
		setResizable(false);
	}
    
    private JComponent makeNewPracPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	JPanel topSubpanel = new JPanel(new GridLayout(0, 1));
    	
    	JPanel nameLengthPanel = new JPanel(new BorderLayout());
    	JPanel namePanel = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("Name: ");
    	namePanel.add(label, BorderLayout.NORTH);
    	namePanel.add(nameField, BorderLayout.CENTER);
    	JPanel lengthPanel = new JPanel(new BorderLayout());
    	label = new JLabel("Appt Length (Min): ");
    	lengthPanel.add(label, BorderLayout.NORTH);
    	lengthPanel.add(apptLengthField, BorderLayout.CENTER);
    	nameLengthPanel.add(namePanel, BorderLayout.CENTER);
    	nameLengthPanel.add(lengthPanel, BorderLayout.EAST);
    	topSubpanel.add(nameLengthPanel);
    	
    	JPanel typePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner Type: ");
    	typePanel.add(label, BorderLayout.NORTH);
    	JPanel typeComboPanel = new JPanel(new BorderLayout());
    	typeCombo = new JComboBox(tm.getTypeList().toArray());
    	if (tm.getTypeList().size() > 0)
    		typeCombo.setSelectedIndex(0);
    	typeComboPanel.add(typeCombo, BorderLayout.CENTER);
    	JButton newTypeButton = new JButton("New Type");
    	newTypeButton.addActionListener(this);
    	newTypeButton.setActionCommand("New Type");
    	typeComboPanel.add(newTypeButton, BorderLayout.EAST);
    	typePanel.add(typeComboPanel, BorderLayout.CENTER);
    	topSubpanel.add(typePanel);
    	
    	
    	
    	panel.add(topSubpanel, BorderLayout.NORTH);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Practitioner notes: ", JLabel.CENTER);
    	notePanel.add(label, BorderLayout.NORTH);
    	noteField.setFont(nameField.getFont());
    	noteField.setLineWrap(true);
    	noteField.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(noteField);
    	notePanel.add(notePane, BorderLayout.CENTER);
    	panel.add(notePanel, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("okNew");
    	okButton.addActionListener(this);
    	cancelButton.addActionListener(this);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static Practitioner ShowDialog(Component owner) {
		newPractitionerUI = new NewPractitionerUI("Create New Practitioner");
		newPractitionerUI.pack();
		newPractitionerUI.setLocationRelativeTo(owner);
		newPractitionerUI.setVisible(true);
		return practitioner;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New Type")) {
			Type t = NewTypeUI.ShowDialog(this);
			if (t == null) return;
			tm = new TypeManager();
			removeAll();
			add(makeNewPracPanel());
			return;
		}
		else if (e.getActionCommand().equals("okNew")) {
			String name = nameField.getText();
			if (name.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a name.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String type = ((Type)typeCombo.getSelectedItem()).toString();
			if (type.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a type.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int apptLength;
			try {
				apptLength = Integer.parseInt(apptLengthField.getText());
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Appointment Length must be an integer value.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (apptLength <= 0) {
				JOptionPane.showMessageDialog(this, "Appointment length must be positive.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String note = noteField.getText().replaceAll("[\r\n]+", "\t\t");
			
			int id = new PractitionerManager().getNewId();
			TypeManager tm = new TypeManager();
			Type t = tm.getType(type);
			if (t == null) {
				t = new Type(tm.getNewId(), type);
				tm.addType(t);
			}
			
			practitioner = new Practitioner(id, name, t, apptLength, note);
			new PractitionerManager().addPractitioner(practitioner);
		} else if (e.getActionCommand().equals("okOld")) {
			if (pracTable.getSelectedRow() > -1)
				practitioner = prac.get(pracTable.getSelectedRow());
			else {
				JOptionPane.showMessageDialog(this, "Please select one of the practitioners in the table, or add a new one.", "Error!", JOptionPane.ERROR_MESSAGE);
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
			Practitioner p = prac.get(row);
			if (col == 0) 
				return p.getName();
			else if (col == 1) 
				return p.getType();
			else
				return p.getApptLength();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}