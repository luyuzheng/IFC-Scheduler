package gui.sub;

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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTabbedPane;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.PatientDto;

/**
 * ADMINISTRATOR PANE
 * Displays a list of all of the patients and allows a patient to be selected for editing or removal
 * Also allows creation of new patients.
 */
@SuppressWarnings("serial")
public class EditPatientsUI extends JDialog implements KeyListener, ActionListener {
	private static EditPatientsUI editPatientsUI;
	private ArrayList<PatientDto> pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
	
	private JButton editButton = new JButton("Edit");
	private JButton okButton = new JButton("OK");
	private JButton newButton = new JButton("New");
	private JButton removeButton = new JButton("Remove");
	private JTable patTable;
	private JTextField searchField = new JTextField();
        
        private Component owner;
	
	
	/** Creates the dialog for viewing the list of patients **/
	public EditPatientsUI(String s, Component owner) {
		setModal(true);
		setTitle(s);
		
        this.owner = owner;
        
        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panel1 = makeExisPatPanel();
		tabbedPane.addTab("Search Existing Patients", null, panel1,
		                  "Select an Existing Patient");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
                
		setLayout(new GridLayout(1,1));
		tabbedPane.setPreferredSize(new Dimension(500,250));
		tabbedPane.setFont(Constants.PARAGRAPH);
		add(tabbedPane);
		setResizable(false);
		
		/**
		add(makeExisPatPanel(), BorderLayout.CENTER);
		pack();
		setResizable(false);
		**/
	}
	
	/** Creates a table for the existing patients **/
	private JComponent makeExisPatPanel() {
    	JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	
    	searchField.addKeyListener(this);
    	
    	patTable = new JTable(new PatTableModel(pat)) {  
    		
    		//Implement table cell tool tips.
    		public String getToolTipText(MouseEvent e) {
    		    String tip = null;
    		    java.awt.Point p = e.getPoint();
    		    
    		    // Convert the row index from the GUI to the row index in the model.
    		    // This is important if the table was sorted in the GUI.
    		    int rowIndex = -1;
    		    if (rowAtPoint(p) > -1) {
    		    	rowIndex = this.getRowSorter().convertRowIndexToModel(rowAtPoint(p));
    		    }
    		    
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
    	patTable.getTableHeader().setFont(Constants.PARAGRAPH);

    	patTable.setFont(Constants.PARAGRAPH);
    	
    	patTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	panel.add(patTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(patTable, BorderLayout.CENTER);
    	
    	// create all the dialog buttons
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("ok");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	
    	editButton.setActionCommand("edit");
    	editButton.addActionListener(this);
    	editButton.setFont(Constants.DIALOG);
    	buttonPanel.add(editButton);
    	
    	removeButton.setActionCommand("remove");
    	removeButton.addActionListener(this);
    	removeButton.setFont(Constants.DIALOG);
    	buttonPanel.add(removeButton);
    	
    	newButton.setActionCommand("new");
    	newButton.addActionListener(this);
    	newButton.setFont(Constants.DIALOG);
    	buttonPanel.add(newButton);
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
        searchField.setFont(Constants.PARAGRAPH);
        
        p.add(searchField, BorderLayout.NORTH);
        p.add(scrollPane, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        p.setPreferredSize(new Dimension(500,250));
        return p;
    }
	
	/** 
	 * Method to split up a string if it is too long to display.
	 * Returns an ArrayList of the lines to be printed.
	 * **/
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
	
	/** Sets the edit patients dialog as visible **/
	public static void ShowDialog(Component owner) {
		editPatientsUI = new EditPatientsUI("Edit Patients", owner);
		editPatientsUI.pack();
		editPatientsUI.setLocationRelativeTo(owner);
		editPatientsUI.setVisible(true);
	}
	
	/** Updates the table when new patients are added **/
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
	
	/** Class to create a table that will list all the existing patients **/
	class PatTableModel extends AbstractTableModel {

		List<PatientDto> patients = new ArrayList<PatientDto>();
		
		public PatTableModel(List<PatientDto> patients) {
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
		
	}

	/** Updates the table when a button is pressed **/
	public void keyReleased(KeyEvent arg0) {
		updateTable();
		
	}

	/** Determines the correct behavior for adding, editing, and removing patients from the table **/
	public void actionPerformed(ActionEvent e) {
		PatTableModel model = (PatTableModel)patTable.getModel();
		if (e.getActionCommand().equals("edit")) {
			if (patTable.getSelectedRow() < 0) return;
			else {
				EditPatientUI.ShowDialog(this, model.getPatient(patTable.getRowSorter().convertRowIndexToModel(patTable.getSelectedRow())));
				pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
                                ((gui.main.MainWindow) owner).refreshAppointments(((gui.main.MainWindow) owner).getCurrentDay().getDate());
				return;
			}
		} else if (e.getActionCommand().equals("new")) {
			NewPatientUI.ShowDialog(this);
			pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
			patTable.setModel(new PatTableModel(pat));
			return;
		} else if (e.getActionCommand().equals("remove")) {
			if (patTable.getSelectedRow() < 0) return;
			
			JLabel msg = new JLabel("Are you sure you want to remove this patient? Removing this patient will not affect historical data, but you will no longer be able to schedule him or her.");
			msg.setFont(Constants.PARAGRAPH);
			if (JOptionPane.showConfirmDialog(this, msg, "Really remove?", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
				DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatient(model.getPatient(patTable.getRowSorter().convertRowIndexToModel(patTable.getSelectedRow())));
				pat = (ArrayList<PatientDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPatients();
				patTable.setModel(new PatTableModel(pat));
			}
			return;	
		}
		editPatientsUI.setVisible(false);
	}
}
