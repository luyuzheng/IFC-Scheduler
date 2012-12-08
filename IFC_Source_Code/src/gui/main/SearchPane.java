package gui.main;

import gui.Constants;
import gui.sub.SearchForAppointmentUI;
import gui.sub.SearchForPatientUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

/**
 * SearchPane displays the search pane on the right-hand side of the application when the "Search" button is clicked.
 * A person can search by patient, by practitioner, and by appointment. The pane can be closed by clicking the "Hide Search" button.
 */
public class SearchPane extends JPanel {
	
	private enum SearchType {
		SEARCHBYPATIENT,
		SEARCHBYAPPT
	}
	private SearchType clicked = null;
	
	private Component owner;
	private JTable resultsTable;
	private JButton searchForPatientButton = new JButton("Search for a Patient");
	private JButton searchForApptButton = new JButton("Search for an Open Appointment Time Slot");
	
	/**
	 * This method is called by MainWindow (the owner). It creates the UI for the pane, including all
	 * labels and the table of results.
	 * 
	 * @param owner - the component that owns this pane
	 */
	public SearchPane(Component owner) {
		this.owner = owner;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		// Create radio button panel
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		
		// Provide choice for type of search
		JLabel typeOfSearchLabel = new JLabel("Choose a Search Option: ");
		typeOfSearchLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
		typeOfSearchLabel.setFont(Constants.DIALOG);
		
		// Create buttons for each search option
		searchForPatientButton.setAction(searchForPatientAction);
		searchForApptButton.setAction(searchForApptAction);
		
		searchForPatientButton.setFont(Constants.DIALOG);
		searchForApptButton.setFont(Constants.DIALOG);
		
		// Add label and buttons to button panel
		buttonPanel.add(typeOfSearchLabel);
		buttonPanel.add(searchForPatientButton);
		buttonPanel.add(searchForApptButton);
		
		
		// Add to the main GUI
		add(buttonPanel, BorderLayout.NORTH);
		
		JPanel resultsTablePanel = new JPanel(new BorderLayout());
		AbstractTableModel model;
		if (clicked == SearchType.SEARCHBYPATIENT) {
			model = new PatientResultsTableModel(new ArrayList<PatientDto>()); // ACTUALLY NEED TO PASS IN A LIST OF PEOPLE HERE!!!
		} else if (clicked == SearchType.SEARCHBYAPPT) {
			model = new AppointmentResultsTableModel(new ArrayList<AppointmentDto>());
		} else { // Nothing clicked yet
			model = new EmptyResultsTableModel();
		}
		resultsTable = new JTable(model);
		resultsTable.setDragEnabled(true);
		resultsTable.setFont(Constants.DIALOG);
		//resultsTable.addMouseListener(new SearchListener(resultsTable, this));
		//specTable.setTransferHandler(new WaitlistTransferHandler());
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.getTableHeader().setReorderingAllowed(false);
		resultsTable.getTableHeader().setFont(Constants.DIALOG);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTablePanel.add(resultsTable.getTableHeader(), BorderLayout.PAGE_START);
		resultsTablePanel.add(resultsTable, BorderLayout.CENTER);
    	JScrollPane scrollPane = new JScrollPane(resultsTablePanel);
    	add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * This calls the appropriate pop up window when the "Search for a Patient" button is clicked.
	 */
	private final AbstractAction searchForPatientAction = new AbstractAction("Search for a Patient") {
		public void actionPerformed(ActionEvent e) {
			List<PatientDto> results = SearchForPatientUI.ShowDialog(owner);
			if (results != null) {
				if (!(resultsTable.getModel() instanceof PatientResultsTableModel)) {
					resultsTable.setModel(new PatientResultsTableModel(results));
				} else {
					((PatientResultsTableModel) resultsTable.getModel()).setPatients(results);
					((PatientResultsTableModel) resultsTable.getModel()).fireTableDataChanged();
				}
			}
		}
	};
	
	/**
	 * This calls the appropriate pop up window when the "Search for Next Available Appointment" button is clicked.
	 */
	private final AbstractAction searchForApptAction = new AbstractAction("Search for Next Available Appointment") {
		public void actionPerformed(ActionEvent e) {
			ArrayList<AppointmentDto> results =
				(ArrayList<AppointmentDto>) SearchForAppointmentUI.ShowDialog(owner);
			if (results != null) {
				if (!(resultsTable.getModel() instanceof AppointmentResultsTableModel)) {
					resultsTable.setModel(new AppointmentResultsTableModel(results));
				} else {
					((AppointmentResultsTableModel) resultsTable.getModel()).setAppointments(results);
					((AppointmentResultsTableModel) resultsTable.getModel()).fireTableDataChanged();
				}
			}
		}
	};
	
	/**
	 * Resets the table model.
	 */
	public void resetModel() {
		// if such and such a button is clicked, set it to the correct model!!!
		//TODO: table.setModel(new SearchTableModel(acm.getConfirmationList()));
	}
	
	/**
	 * Displays an empty table when first initialized (no search has been conducted yet).
	 */
	public class EmptyResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		

		/**
		 * Constructor to produce an empty table model.
		 */ 
		public EmptyResultsTableModel() {
			columnNames = new String[] {" ", " ", " "};
		}
		
		/**
		 * Returns the number of columns in the table.
		 * 
		 * @return number of columns in the table
		 */
		public int getColumnCount() {
			return columnNames.length;
		}
		
		/**
		 * Returns the name of a specified column in the table.
		 * 
		 * @param col - the column number
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		/**
		 * Returns an empty number of rows in the table, since there are no search results yet.
		 * 
		 * @returns the number of rows in the table = 0
		 */
		public int getRowCount() {
			return 0;
		}
		
		/**
		 * Returns no information because there are no search results.
		 * 
		 * @param row - the row number
		 * @param col - the column number
		 */
		public Object getValueAt(int row, int col) {
			return null;
		}
	}
	
	/**
	 * Displays the table of results after searching for a patient.
	 */
	public class PatientResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		private List<PatientDto> patients;
		
		/**
		 * Constructor to produce a patient results table model.
		 */ 
		public PatientResultsTableModel(List<PatientDto> pat) {
			patients = pat;
			columnNames = new String[] { "Name", "Phone Number", "Comments", /*"Waitlisted",*/ "No Shows"};
		}
		
		/**
		 * Gets the information for a selected patient.
		 * 
		 * @param row - the row of the table where the desired patient is located.
		 * @return the patient information for a selected row
		 */
		public PatientDto getPatient(int row) {
			return patients.get(row);
		}
		
		/**
		 * Returns the number of columns in the table.
		 * 
		 * @return number of columns in the table
		 */
		public int getColumnCount() {
			return columnNames.length;
		}
		
		/**
		 * Returns the name of a specified column in the table.
		 * 
		 * @param col - the column number
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		/**
		 * Returns the number of patients found in the search results.
		 * 
		 * @returns the number of rows in the table
		 */
		public int getRowCount() {
			return patients.size();
		}
		
		public void setPatients(List<PatientDto> patients) {
			this.patients = patients; 
		}
		
		/**
		 * Returns the cell of information specified at a particular row and column.
		 * 
		 * @param row - the row number
		 * @param col - the column number
		 */
		public Object getValueAt(int row, int col) {
			PatientDto p = patients.get(row);
			if (col == 0) {
				return p.getFirst() + " " + p.getLast();
			} else if (col == 1) {
				return p.getPhone();
			} else if (col == 2) {
				return p.getNotes();
			//} else if (col == 3) {
			//	return p.isWaitlisted();
			} else {
				return p.getNoShows();
			}
		}
	}
	
	/**
	 * Displays the table of results after searching for an open appointment time slot.
	 */
	public class AppointmentResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		private ArrayList<AppointmentDto> appointments;
		
		/**
		 * Constructor to produce an appointment results table model.
		 */ 
		public AppointmentResultsTableModel(ArrayList<AppointmentDto> appt) {
			appointments = appt;
			columnNames = new String[] { "Date", "Start Time", "Practitioner", "Appointment Length"};
		}
		
		/**
		 * Gets the information for a selected appointment.
		 * 
		 * @param row - the row of the table where the desired appointment is located.
		 * @return the appointment information for a selected row
		 */
		public AppointmentDto getAppointment(int row) {
			return appointments.get(row);
		}
		
		/**
		 * Returns the number of columns in the table.
		 * 
		 * @return number of columns in the table
		 */
		public int getColumnCount() {
			return columnNames.length;
		}
		
		/**
		 * Returns the name of a specified column in the table.
		 * 
		 * @param col - the column number
		 */
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		/**
		 * Returns the number of available appointments found in the search results.
		 * 
		 * @returns the number of rows in the table
		 */
		public int getRowCount() {
			return appointments.size();
		}
		
		public void setAppointments(ArrayList<AppointmentDto> appointments) {
			this.appointments = appointments;
		}
		
		/**
		 * Returns the cell of information specified at a particular row and column.
		 * 
		 * @param row - the row number
		 * @param col - the column number
		 */
		public Object getValueAt(int row, int col) {
			AppointmentDto a = appointments.get(row);
			if (col == 0) {
				return a.getApptDate();
			} else if (col == 1){
				return a.prettyPrintStart();
			} else if (col == 2) {
				return a.getPractName();
			} else {
				return a.getEnd() - a.getStart();
			}
		}
	}
	
	/** 
	 * Displays the appropriate search pop up window depending on which search option
	 * the user wants to use - search by patient, practitioner, or appointment slot. 
	 */
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "Search for a Patient") {
				clicked = SearchType.SEARCHBYPATIENT;
			} else {
				clicked = SearchType.SEARCHBYAPPT;
			}
			// Reset the table to display the proper search results
			resetModel();
		}
	}
}