package gui.main;

import gui.sub.SearchForAppointmentUI;
import gui.sub.SearchForPatientUI;
import gui.sub.SearchForPractitionerUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import data.Appointment;
import data.Patient;
import data.Practitioner;

public class SearchPane extends JPanel {
	
	private Component owner;
	private JButton searchForPatientButton = new JButton("Search for a Patient");
	private JButton searchForPracButton = new JButton("Search for a Practitioner");
	private JButton searchForApptButton = new JButton("Search for an Open Appointment Time Slot");
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public SearchPane(Component owner) {
		this.owner = owner;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		// Create radio button panel
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		JPanel searchAreaPanel = new JPanel(new GridLayout(0, 1));
		JPanel searchResultsPanel = new JPanel(new BorderLayout());
		
		// Provide choice for type of search
		JLabel typeOfSearchLabel = new JLabel("Choose a Search Option: ");
		typeOfSearchLabel.setFont(font);
		
		// Create buttons for each search option
		searchForPatientButton.setAction(searchForPatientAction);
		searchForPracButton.setAction(searchForPracAction);
		searchForApptButton.setAction(searchForApptAction);
		
		searchForPatientButton.setFont(font);
		searchForPracButton.setFont(font);
		searchForApptButton.setFont(font);
		
		// Add label and buttons to button panel
		buttonPanel.add(typeOfSearchLabel);
		buttonPanel.add(searchForPatientButton);
		buttonPanel.add(searchForPracButton);
		buttonPanel.add(searchForApptButton);
		
		
		// Add to the main GUI
		add(buttonPanel, BorderLayout.NORTH);
	}
	
	/**
	 * 
	 */
	private final AbstractAction searchForPatientAction = new AbstractAction("Search for a Patient") {
		public void actionPerformed(ActionEvent e) {
			SearchForPatientUI.ShowDialog(owner);
		}
	};
	
	private final AbstractAction searchForPracAction = new AbstractAction("Search for a Practitioner") {
		public void actionPerformed(ActionEvent e) {
			SearchForPractitionerUI.ShowDialog(owner);
		}
	};
	
	private final AbstractAction searchForApptAction = new AbstractAction("Search for Next Available Appointment") {
		public void actionPerformed(ActionEvent e) {
			SearchForAppointmentUI.ShowDialog(owner);
		}
	};
	
	/**
	 * Displays the table of results after searching for a patient.
	 */
	public class PatientResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		private ArrayList<Patient> patients;
		
		public PatientResultsTableModel(ArrayList<Patient> pat) {
			patients = pat;
			columnNames = new String[] { "First Name", "Last Name", "Phone Number", "Comments" /*, "Waitlisted", "No Show" */};
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return patients.size();
		}
		
		public Object getValueAt(int row, int col) {
			Patient p = patients.get(row);
			if (col == 0) {
				return p.getFirstName();
			} else if (col == 1) {
				return p.getLastName();
			} else if (col == 2) {
				return p.getNumberString();
			} else {
				return p.getNote();
			/*} else if (col == 4) {
				return p.isWaitlisted();
			} else {
				return p.noShow();*/
			}
		}
	}
	
	/**
	 * Displays the table of results after searching for a practitioner.
	 */
	public class PractitionerResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		private ArrayList<Practitioner> practitioners;
		
		public PractitionerResultsTableModel(ArrayList<Practitioner> prac) {
			practitioners = prac;
			columnNames = new String[] { "Name", "Type", "Appointment Length" };
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return practitioners.size();
		}
		
		public Object getValueAt(int row, int col) {
			Practitioner p = practitioners.get(row);
			if (col == 0) {
				return p.getName();
			} else if (col == 1) {
				return p.getType();
			} else {
				return p.getApptLength();
			}
		}
	}
	
	/**
	 * Displays the table of results after searching for an open appointment time slot.
	 */
	public class AppointmentResultsTableModel extends AbstractTableModel {
		
		private String columnNames[];
		private ArrayList<Appointment> appointments;
		
		public AppointmentResultsTableModel(ArrayList<Appointment> appt) {
			appointments = appt;
			columnNames = new String[] { "Date", "Start Time", "Appointment Length" /*, "Practitioner" */};
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return appointments.size();
		}
		
		public Object getValueAt(int row, int col) {
			Appointment a = appointments.get(row);
			if (col == 0) {
				return a.getDate();
			} else if (col == 1){
				return a.getTimeSlot().getStartTime();
			} else {
				return a.getTimeSlot().lengthInMinutes();
			}
		}
	}
	
	/** 
	 * Displays the appropriate search box depending on which search option
	 * the user wants to use - search by patient, practitioner, or appointment slot. 
	 */
	public class ButtonListener implements ActionListener {
		public void displayPatientSearchBox() {
			
		}
		
		public void displayPracSearchBox() {
			
		}
		
		public void displayApptSearchBox() {
			
		}
		
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "Search for a Patient") {
				displayPatientSearchBox();
			} else if (e.getActionCommand() == "Search for a Practitioner") {
				displayPracSearchBox();
			} else {
				displayApptSearchBox();
			}
		}
	}
}