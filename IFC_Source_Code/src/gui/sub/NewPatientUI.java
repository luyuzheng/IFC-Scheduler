package gui.sub;

import backend.DataService.DataServiceImpl;
import gui.Constants;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
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
 * Popup that allows new patients to be created
 */
public class NewPatientUI extends JDialog implements ActionListener, KeyListener {
	private static NewPatientUI newPatientUI;
	private ArrayList<PatientDto> pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JTextArea note = new JTextArea();
	private JTextField areaCodeField = new JTextField();
	private JTextField numberPart1Field = new JTextField();
	private JTextField numberPart2Field = new JTextField();
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	JTable patTable;
	private JTextField searchField = new JTextField();
	
	private static PatientDto patient;
	
	private NewPatientUI(String name) {
		patient = null;
		setModal(true);
		setTitle(name);
		setLayout(new GridLayout(1,1));
		setPreferredSize(new Dimension(275,250));
		add(makeNewPatPanel());
		setResizable(false);
		
	}
    
    
    private JComponent makeNewPatPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel input = new JPanel(new GridLayout(0,1));
    	JPanel fName = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("First Name: ");
    	label.setFont(Constants.PARAGRAPH);
    	fName.add(label, BorderLayout.NORTH);
    	firstNameField.setColumns(15);
    	firstNameField.setFont(Constants.PARAGRAPH);
    	fName.add(firstNameField, BorderLayout.CENTER);
    	
    	JPanel lName = new JPanel(new BorderLayout());
    	label = new JLabel("Last Name: ");
    	label.setFont(Constants.PARAGRAPH);
    	lName.add(label, BorderLayout.NORTH);
    	lastNameField.setColumns(15);
    	lastNameField.setFont(Constants.PARAGRAPH);
    	lName.add(lastNameField, BorderLayout.CENTER);
    	
    	JPanel names = new JPanel(new FlowLayout());
    	names.add(fName);
    	names.add(lName);
    	
    	input.add(names);
    	
    	JPanel num = new JPanel(new BorderLayout());
    	label = new JLabel("Phone Number: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	num.add(label, BorderLayout.NORTH);
    	JPanel phonePanel = new JPanel(new FlowLayout());
    	areaCodeField.setColumns(5);
    	areaCodeField.setFont(Constants.PARAGRAPH);
    	numberPart1Field.setColumns(5);
    	numberPart1Field.setFont(Constants.PARAGRAPH);
    	numberPart2Field.setColumns(7);
    	numberPart2Field.setFont(Constants.PARAGRAPH);
    	phonePanel.add(areaCodeField);
    	phonePanel.add(numberPart1Field);
    	phonePanel.add(numberPart2Field);
    	num.add(phonePanel, BorderLayout.CENTER);
    	input.add(num);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Patient notes: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	notePanel.add(label, BorderLayout.NORTH);
    	note.setFont(Constants.PARAGRAPH);
    	//note.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    	note.setLineWrap(true);
    	note.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(note);
    	notePanel.add(notePane, BorderLayout.CENTER);
    		
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("okNew");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	cancelButton.addActionListener(this);
    	cancelButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	panel.add(input, BorderLayout.NORTH);
    	panel.add(notePanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static PatientDto ShowDialog(Component owner) {
		newPatientUI = new NewPatientUI("New Patient");
		newPatientUI.pack();
		newPatientUI.setLocationRelativeTo(owner);
		newPatientUI.setVisible(true);
		return patient;
	}
	
	public void updateTable() {
		String filter = searchField.getText();
                patTable.setModel(new PatTableModel(pat));
		//TODO: FILTER if (filter.equals("")) patTable.setModel(new PatTableModel(pat));
		//else patTable.setModel(new PatTableModel(pm.getFilteredPatientList(filter)));
	}
	
	public void actionPerformed(ActionEvent e) {
                System.out.println("Here");
		if (e.getActionCommand().equals("okNew")) {
			String firstName = firstNameField.getText();
			if (firstName.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a first name.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String lastName = lastNameField.getText();
			if (lastName.equals("")) {
				JOptionPane.showMessageDialog(this, "Please enter a last name.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String areaCode = areaCodeField.getText();
			String numberPart1 = numberPart1Field.getText();
			String numberPart2 = numberPart2Field.getText();
			boolean blank = false;
			if (areaCode.equals("") && numberPart1.equals("") && numberPart2.equals("")) {
				blank = true;
				if (JOptionPane.showConfirmDialog(this, "The Phone Number field is blank. Would you like to continue?", "Missing Phone Number", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
					return;
			}
			String num;
			try {
				if (blank) num = null;
				else if (areaCode.length() != 3 || numberPart1.length() != 3 || numberPart2.length() != 4) {
					JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					int a = Integer.parseInt(areaCode);
					int p1 = Integer.parseInt(numberPart1);
					int p2 = Integer.parseInt(numberPart2);
					num = areaCode + "-" + numberPart1 + "-" + numberPart2;//a, p1, p2);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			//String noteText = note.getText();
			String noteText = note.getText().replaceAll("[\r\n]+", "\t\t"); //added by aakash on feb 12 to fix multiline note bug
                        
			patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatient(firstName, lastName, num, noteText);			
			
		} else if (e.getActionCommand().equals("okOld")) {
			if (patTable.getSelectedRow() > -1) {
				PatTableModel model = (PatTableModel)patTable.getModel();
				patient = model.getPatient(patTable.getSelectedRow());
				if (!patient.getNotes().equals("") && JOptionPane.showConfirmDialog(this, "This patient has the following note attached: \"" + patient.getNotes() + "\". Are you sure you want to continue?", "Please Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					patient = null;
					return;
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "Please select one of the patients in the table, or add a new one.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (e.getActionCommand().equals("edit")) {
			if (patTable.getSelectedRow() < 0) return;
			else {
				PatTableModel model = (PatTableModel)patTable.getModel();
				EditPatientUI.ShowDialog(this, model.getPatient(patTable.getSelectedRow()));
				pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
				return;
			}
		} 
		newPatientUI.setVisible(false);
    }
	
	class PatTableModel extends AbstractTableModel {

		ArrayList<PatientDto> patients = new ArrayList<PatientDto>();
		
		public PatTableModel(ArrayList<PatientDto> patients) {
			this.patients = patients;
		}
		
		public PatientDto getPatient(int row) {
			return patients.get(row);
		}
		
		private String[] columnNames = { "First Name", "Last Name", "Phone #", "Note" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return patients.size();
		}

		public Object getValueAt(int row, int col) {
			PatientDto p = patients.get(row);
			if (col == 0) 
				return p.getFirst();
			else  if (col == 1)
				return p.getLast();
			else if (col == 2)
				return p.getPhone();
			else
				return p.getNotes();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		
		
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyReleased(KeyEvent arg0) {
		updateTable();
		
	}
}
