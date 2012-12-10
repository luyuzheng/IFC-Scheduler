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

import backend.DataTransferObjects.*;
import java.util.List;

public class SelectPatientUI extends JDialog implements ActionListener, KeyListener {
	private static SelectPatientUI selectPatientUI;
	private ArrayList<PatientDto> pat = (ArrayList) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
	
	private JTextField firstNameField = new JTextField();
	private JTextField lastNameField = new JTextField();
	private JTextArea note = new JTextArea();
	private JTextField areaCodeField = new JTextField();
	private JTextField numberPart1Field = new JTextField();
	private JTextField numberPart2Field = new JTextField();
	private JButton editButton = new JButton("Edit Patient");
	private JButton okButton1 = new JButton("OK");
	private JButton cancelButton1 = new JButton("Cancel");
	private JButton okButton2 = new JButton("OK");
	private JButton cancelButton2 = new JButton("Cancel");
	JTable patTable;
	private JTextField searchField = new JTextField();
	
	private static PatientDto patient;
	
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
		tabbedPane.setPreferredSize(new Dimension(500,250));
		tabbedPane.setFont(Constants.PARAGRAPH);
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
    	patTable.setFont(Constants.PARAGRAPH);
    	patTable.getTableHeader().setFont(Constants.PARAGRAPH);
    	panel.add(patTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(patTable, BorderLayout.CENTER);
    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	editButton.setActionCommand("edit");
    	editButton.addActionListener(this);
    	editButton.setFont(Constants.DIALOG);
    	buttonPanel.add(editButton);
    	okButton1.setActionCommand("okOld");
    	okButton1.addActionListener(this);
    	okButton1.setFont(Constants.DIALOG);
    	cancelButton1.addActionListener(this);
    	cancelButton1.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton1);
    	buttonPanel.add(cancelButton1);
    	
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
        searchField.setFont(Constants.PARAGRAPH);
        searchField.addKeyListener(this);
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
    	label = new JLabel("Patient Notes: ", JLabel.CENTER);
    	label.setFont(Constants.PARAGRAPH);
    	notePanel.add(label, BorderLayout.NORTH);
    	note.setFont(Constants.PARAGRAPH);
    	//note.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    	note.setLineWrap(true);
    	note.setWrapStyleWord(true);
    	JScrollPane notePane = new JScrollPane(note);
    	notePanel.add(notePane, BorderLayout.CENTER);
    		
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton2.setActionCommand("okNew");
    	okButton2.addActionListener(this);
    	okButton2.setFont(Constants.DIALOG);
    	cancelButton2.addActionListener(this);
    	cancelButton2.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton2);
    	buttonPanel.add(cancelButton2);
    	
    	panel.add(input, BorderLayout.NORTH);
    	panel.add(notePanel, BorderLayout.CENTER);
    	panel.add(buttonPanel, BorderLayout.SOUTH);
    	
    	return panel;
    }
	
	public static PatientDto ShowDialog(Component owner) {
		selectPatientUI = new SelectPatientUI("Select Patient");
		selectPatientUI.pack();
		selectPatientUI.setLocationRelativeTo(owner);
		selectPatientUI.setVisible(true);
		return patient;
	}
	
	public void updateTable() {
	    // Filters patients
		String filter = searchField.getText().toLowerCase();
		String[] filters = filter.split(" ");
        ArrayList<PatientDto> filteredPat = new ArrayList<PatientDto>();
        for (PatientDto p : pat) {
        	String fullName = p.getFullName().toLowerCase();
        	
        	for (String f : filters) {
	        	if (fullName.indexOf(f) >= 0) {
	        		filteredPat.add(p);
	        		break;
	        	}
        	}
        }
        patTable.setModel(new PatTableModel(filteredPat));
	}
	
	public void actionPerformed(ActionEvent e) {
		JLabel msg = new JLabel();
		msg.setFont(Constants.PARAGRAPH);
		
		if (e.getActionCommand().equals("okNew")) {
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
			String areaCode = areaCodeField.getText();
			String numberPart1 = numberPart1Field.getText();
			String numberPart2 = numberPart2Field.getText();
			boolean blank = false;
			if (areaCode.equals("") && numberPart1.equals("") && numberPart2.equals("")) {
				blank = true;
				msg.setText("The Phone Number field is blank. Would you like to continue?");
				if (JOptionPane.showConfirmDialog(this, msg, "Missing Phone Number", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION)
					return;
			}
			String num;
			try {
				if (blank) num = null;
				else if (areaCode.length() != 3 || numberPart1.length() != 3 || numberPart2.length() != 4) {
					msg.setText("Please enter a valid phone number (###-###-####) or leave the field blank.");
					JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					//int a = Integer.parseInt(areaCode);
					//int p1 = Integer.parseInt(numberPart1);
					//int p2 = Integer.parseInt(numberPart2);
					//num = new PhoneNumber(a, p1, p2);
					num = areaCode + "-" + numberPart1 + "-" + numberPart2;
				}
			} catch (Exception ex) {
				msg.setText("Please enter a valid phone number (###-###-####) or leave the field blank.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String noteText = note.getText().replaceAll("[\r\n]+", "\t\t"); //Added by Aakash on 12th feb to fix multiline note bug
			
			patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatient(num, firstName, lastName, noteText);
			
		} else if (e.getActionCommand().equals("okOld")) {
			if (patTable.getSelectedRow() > -1) {
				PatTableModel model = (PatTableModel)patTable.getModel();
				patient = model.getPatient(patTable.getSelectedRow());
				
				msg.setText("This patient has the following note attached: \"" + patient.getNotes() + "\". Are you sure you want to continue?");
				if (!patient.getNotes().equals("") && JOptionPane.showConfirmDialog(this, msg, "Please Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					patient = null;
					return;
				}

				msg.setText("This patient has " + patient.getNoShows() + " no-shows. Are you sure you want to continue?");
				if (patient.getNoShows() > 1 && JOptionPane.showConfirmDialog(this, msg, "Please Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					patient = null;
					return;
				}
			}
			else {
				msg.setText("Please select one of the patients in the table, or add a new one.");
				JOptionPane.showMessageDialog(this, msg, "Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if (e.getActionCommand().equals("edit")) {
			if (patTable.getSelectedRow() < 0) return;
			else {
				PatTableModel model = (PatTableModel)patTable.getModel();
				EditPatientUI.ShowDialog(this, model.getPatient(patTable.getSelectedRow()));
				pat = (ArrayList) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
				return;
			}
		} 
		selectPatientUI.setVisible(false);
    }
	
	class PatTableModel extends AbstractTableModel {

		ArrayList<PatientDto> patients = new ArrayList<PatientDto>();
		
		public PatTableModel(ArrayList<PatientDto> patients) {
			this.patients = patients;
		}
		
		public PatientDto getPatient(int row) {
			return patients.get(row);
		}
		
		private String[] columnNames = { "First Name", "Last Name", "Phone #", "Note", "No Shows"};
		
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
			else if (col == 3)
				return p.getNotes();
			else {
				Integer numNoShows = p.getNoShows();
				return numNoShows.toString();
			}
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

	public void keyPressed(KeyEvent arg0) {
		
	}

	public void keyReleased(KeyEvent arg0) {
		updateTable();
	}
}
