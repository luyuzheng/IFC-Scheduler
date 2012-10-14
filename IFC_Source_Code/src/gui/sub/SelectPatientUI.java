package gui.sub;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import data.Patient;
import data.PhoneNumber;
import data.managers.PatientManager;

public class SelectPatientUI extends JDialog implements ActionListener, KeyListener {
	private static SelectPatientUI selectPatientUI;
	private PatientManager pm = new PatientManager();
	private ArrayList<Patient> pat = pm.getPatientList();
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JTextArea note = new JTextArea();
	private JTextField areaCodeField = new JTextField();
	private JTextField numberPart1Field = new JTextField();
	private JTextField numberPart2Field = new JTextField();
	private JButton editButton = new JButton("Edit Patient");
	private JButton okButton1 = new JButton("Ok");
	private JButton cancelButton1 = new JButton("Cancel");
	private JButton okButton2 = new JButton("Ok");
	private JButton cancelButton2 = new JButton("Cancel");
	JTable patTable;
	private JTextField searchField = new JTextField();
	
	private static Patient patient;
	
	private SelectPatientUI(String name) {
		patient = null;
		setModal(true);
		setTitle(name);
		JTabbedPane tabbedPane = new JTabbedPane();
		JComponent panel1 = makeExisPatPanel();
		tabbedPane.addTab("Search Existing Patients", null, panel1,
		                  "Select an Existing Patient");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		JComponent panel2 = makeNewPatPanel();
		tabbedPane.addTab("New Patient", null, panel2,
		                  "Select a New Patient");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		
		setLayout(new GridLayout(1,1));
		tabbedPane.setPreferredSize(new Dimension(275,250));
		add(tabbedPane);
		setResizable(false);
		
	}
    
	private ArrayList<String> breakupString(String s, int line) {
		ArrayList<String> strings = new ArrayList<String>();
		int startIndex = 0;
		int endIndex = line - 5; //- 5 to compensate for word "Note: " at beginning
		int len = s.length();
		while (endIndex < len) {
			while (endIndex < len && s.charAt(endIndex) != ' ' && s.charAt(endIndex) != '.') 
				endIndex++;
			strings.add(s.substring(startIndex, endIndex));
			startIndex = endIndex;
			endIndex = endIndex + line;
		}
		endIndex = s.length();
		strings.add(s.substring(startIndex, endIndex));
		return strings;
	}
	
    private JComponent makeExisPatPanel() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	searchField.addKeyListener(this);
    	
    	
    	patTable = new JTable(new PatTableModel(pat)) {  
    		//Implement table cell tool tips.
    		public String getToolTipText(MouseEvent e) {
    		    String tip = null;
    		    java.awt.Point p = e.getPoint();
    		    int rowIndex = rowAtPoint(p);
    		    
    		    if (rowIndex >= 0) {
    		    	TableModel model = getModel();
    		    	String firstName = (String)model.getValueAt(rowIndex,0);
    		    	String lastName = (String)model.getValueAt(rowIndex,1);
    		    	String phoneNumber = (String)model.getValueAt(rowIndex, 2);
    		    	String note = (String)model.getValueAt(rowIndex, 3);
    		    	ArrayList<String> notes = breakupString(note, 50);
    		    	if (notes.size() == 0 || notes.get(0).equals("")) {
    		    		notes.clear();
    		    		notes.add("None");
    		    	}
    		    	
    		    	tip = "<html>Patient: " + firstName + " " + lastName + "<br>" +
    		    		  "Phone Number: " + phoneNumber + "<br>" + 
    		    		  "Notes: ";
    		    	for (String s : notes) 
    		    		tip += s + "<br>";
    		    	tip = tip.substring(0, tip.length() - 4);
    		    } else tip = "";
    		    return tip;
    		 
    		}
    	};
    	patTable.setAutoCreateRowSorter(true);
    	patTable.getTableHeader().setReorderingAllowed(false);

    	patTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	panel.add(patTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(patTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton1.setActionCommand("okOld");
    	okButton1.addActionListener(this);
    	cancelButton1.addActionListener(this);
    	buttonPanel.add(okButton1);
    	buttonPanel.add(cancelButton1);
    	
    	editButton.setActionCommand("edit");
    	editButton.addActionListener(this);
    	buttonPanel.add(editButton);
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
        p.add(searchField, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        return p;
    }
    
    private JComponent makeNewPatPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel input = new JPanel(new GridLayout(0,1));
    	JPanel fName = new JPanel(new BorderLayout());
    	JLabel label = new JLabel("First Name: ");
    	fName.add(label, BorderLayout.NORTH);
    	firstNameField.setColumns(15);
    	fName.add(firstNameField, BorderLayout.CENTER);
    	
    	JPanel lName = new JPanel(new BorderLayout());
    	label = new JLabel("Last Name: ");
    	lName.add(label, BorderLayout.NORTH);
    	lastNameField.setColumns(15);
    	lName.add(lastNameField, BorderLayout.CENTER);
    	
    	JPanel names = new JPanel(new FlowLayout());
    	names.add(fName);
    	names.add(lName);
    	
    	input.add(names);
    	
    	JPanel num = new JPanel(new BorderLayout());
    	label = new JLabel("Phone Number: ", JLabel.CENTER);
    	num.add(label, BorderLayout.NORTH);
    	JPanel phonePanel = new JPanel(new FlowLayout());
    	areaCodeField.setColumns(5);
    	numberPart1Field.setColumns(5);
    	numberPart2Field.setColumns(7);
    	phonePanel.add(areaCodeField);
    	phonePanel.add(numberPart1Field);
    	phonePanel.add(numberPart2Field);
    	num.add(phonePanel, BorderLayout.CENTER);
    	input.add(num);
    	
    	JPanel notePanel = new JPanel(new BorderLayout());
    	label = new JLabel("Patient notes: ", JLabel.CENTER);
    	notePanel.add(label, BorderLayout.NORTH);
    	note.setFont(phonePanel.getFont());
    	//note.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    	note.setLineWrap(true);
    	note.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(note);
    	notePanel.add(notePane, BorderLayout.CENTER);
    		
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton2.setActionCommand("okNew");
    	okButton2.addActionListener(this);
    	cancelButton2.addActionListener(this);
    	buttonPanel.add(okButton2);
    	buttonPanel.add(cancelButton2);
    	
    	panel.add(input, BorderLayout.NORTH);
    	panel.add(notePanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static Patient ShowDialog(Component owner) {
		selectPatientUI = new SelectPatientUI("Select Patient");
		selectPatientUI.pack();
		selectPatientUI.setLocationRelativeTo(owner);
		selectPatientUI.setVisible(true);
		return patient;
	}
	
	public void updateTable() {
		String filter = searchField.getText();
		if (filter.equals("")) patTable.setModel(new PatTableModel(pat));
		else patTable.setModel(new PatTableModel(pm.getFilteredPatientList(filter)));
	}
	
	public void actionPerformed(ActionEvent e) {
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
			PhoneNumber num;
			try {
				if (blank) num = null;
				else if (areaCode.length() != 3 || numberPart1.length() != 3 || numberPart2.length() != 4) {
					JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					//int a = Integer.parseInt(areaCode);
					//int p1 = Integer.parseInt(numberPart1);
					//int p2 = Integer.parseInt(numberPart2);
					//num = new PhoneNumber(a, p1, p2);
					num = new PhoneNumber(areaCode, numberPart1, numberPart2);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Please enter a valid phone number (###-###-####) or leave the field blank.", "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String noteText = note.getText().replaceAll("[\r\n]+", "\t\t"); //Added by Aakash on 12th feb to fix multiline note bug
			int id = pm.getNewId();
			
			patient = new Patient(id, firstName, lastName, num, noteText);
			Patient test = pm.patientExists(patient);
			
			if (test == null) pm.addPatient(patient);
			else {
				String text = "A patient exists by that name: \n" +
					          "Name: " + patient.getFullName() + "\n" +
					          "Phone Number: " + patient.getNumberString() + "\n" + 
					          "Note: " + patient.getNote();
				text += "\n\nContinue anyways?";
				
				if (JOptionPane.showConfirmDialog(this, text, "Patient Confirmation", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
					pm.addPatient(patient);
				else {
					patient = null;
					return;
				}	
			}
		} else if (e.getActionCommand().equals("okOld")) {
			if (patTable.getSelectedRow() > -1) {
				PatTableModel model = (PatTableModel)patTable.getModel();
				patient = model.getPatient(patTable.getSelectedRow());
				if (!patient.getNote().equals("") && JOptionPane.showConfirmDialog(this, "This patient has the following note attached: \"" + patient.getNote() + "\". Are you sure you want to continue?", "Please Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
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
				pat = pm.getPatientList();
				patTable.setModel(new PatTableModel(pat));
				return;
			}
		} 
		selectPatientUI.setVisible(false);
    }
	
	class PatTableModel extends AbstractTableModel {

		ArrayList<Patient> patients = new ArrayList<Patient>();
		
		public PatTableModel(ArrayList<Patient> patients) {
			this.patients = patients;
		}
		
		public Patient getPatient(int row) {
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
			Patient p = patients.get(row);
			if (col == 0) 
				return p.getFirstName();
			else  if (col == 1)
				return p.getLastName();
			else if (col == 2)
				return p.getNumberString();
			else
				return p.getNote();
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
