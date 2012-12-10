package gui.sub;

import gui.Constants;
import gui.main.AppointmentConfirmationPane.AppointmentConfirmationTableModel;
import gui.main.listeners.AppointmentConfirmationListener;
import gui.main.listeners.DisplayPatientSearchListener;
import gui.main.MainWindow;
import gui.sub.SelectPatientUI.PatTableModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

/**
 * DisplayPatientSearchUI shows information about a patient when a patient in the search table
 * is clicked.
 */
public class DisplayPatientSearchUI extends JDialog implements ActionListener {
	private static DisplayPatientSearchUI displayPatientSearchUI;
	
	private MainWindow mw;
	private PatientDto patient;
	private JButton okButton = new JButton("OK");
	private JButton editPatientButton = new JButton("Edit Patient");
	private JTextArea textArea;
	private JTextArea apptArea;
	private JTable table;
	
	/**
	 * Constructor - creates the actual UI to display the patient information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param pat - the patient information to be displayed
	 */
	private DisplayPatientSearchUI(String name, PatientDto pat, MainWindow mw) {
		this.mw = mw;
		patient = pat;
		
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400, 400));
		
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel apptPanel = new JPanel(new BorderLayout());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = "Patient Name: " + patient.getFirst() + " " + patient.getLast() +
				  	  "\nPhone Number: " + patient.getPhone() +
				  	  "\nComments: " + patient.getNotes() +
				  	  "\nNumber of No Shows: " + patient.getNoShows() +
				  	  "\n\nFuture Appointments: ";
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		infoPanel.add(textArea);

		FutureAppointmentsTableModel model = new FutureAppointmentsTableModel(
				(List<AppointmentDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getFutureAppointmentsByPatId(patient.getPatID()));
		table = new JTable(model);
		table.setDragEnabled(true);
		table.setFont(Constants.DIALOG);
		table.addMouseListener(new DisplayPatientSearchListener(table, this));
		table.setAutoCreateRowSorter(true);
    	table.getTableHeader().setReorderingAllowed(false);
    	table.getTableHeader().setFont(Constants.DIALOG);
    	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	apptPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
    	apptPanel.add(table, BorderLayout.CENTER);
    	JScrollPane scrollPane = new JScrollPane(apptPanel);
		
    	editPatientButton.setFont(Constants.DIALOG);
    	editPatientButton.setActionCommand("edit");
    	editPatientButton.addActionListener(this);
		okButton.setFont(Constants.DIALOG);
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPanel.add(editPatientButton);
		buttonPanel.add(okButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		
		add(infoPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
	}

	/**
	 * Makes the pop up window visible when a patient from the table is selected.
	 * 
	 * @param owner - the component that owns this pane (the MainWindow)
	 * @param appt  - the appointment information
	 */
	public static void ShowDialog(Component owner, PatientDto pat, MainWindow mw) {
		displayPatientSearchUI = new DisplayPatientSearchUI("View Future Appointments for Patient", pat, mw);
		displayPatientSearchUI.pack();
		displayPatientSearchUI.setLocationRelativeTo(owner);
		displayPatientSearchUI.setVisible(true);
	}
	
	/**
	 * Resets the table model.
	 */
	public void resetModel() {
		table.setModel(new FutureAppointmentsTableModel(
				(List<AppointmentDto>)DataServiceImpl.GLOBAL_DATA_INSTANCE.getFutureAppointmentsByPatId(patient.getPatID())));
	}
	
	/**
	 * AppointmentConfirmationTableModel creates the table itself, defining the rows and columns of the table.
	 */
	public class FutureAppointmentsTableModel extends AbstractTableModel {
		private String[] columnNames;
		private List<AppointmentDto> confirm;

		/**
		 * Constructor to produce an appointment confirmation table model.
		 * 
		 * @param confirm - a list of appointments that need to be confirmed for a chosen day
		 */
		public FutureAppointmentsTableModel(List<AppointmentDto> confirm) {
			this.confirm = confirm;
			columnNames = new String[] {"Date", "Start Time", "End Time",  "Confirmed"};
		}
		
		/**
		 * Gets the information for a selected appointment.
		 * 
		 * @param row - the row of the table where the desired appointment is located.
		 * @return the appointment information for a selected row
		 */
		public AppointmentDto getAppointment(int row) {
			return confirm.get(row);
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
		 * Returns the number of appointments to be confirmed for a chosen day.
		 * 
		 * @returns the number of rows in the table
		 */
		public int getRowCount() {
			return confirm.size();
		}

		 public boolean isCellEditable(int row, int col) { 
			 return false; 
		 }
		 
		/**
		 * Returns the cell of information specified at a particular row and column.
		 * 
		 * @param row - the row number
		 * @param col - the column number
		 */
		public Object getValueAt(int row, int col) {
			AppointmentDto appt = confirm.get(row);
			
			if (col == 0) {
				return appt.getApptDate();
			} else if (col == 1) {
				return appt.prettyPrintStart();
			} else if (col == 2) {
				return appt.prettyPrintEnd();
			} else {
				if (appt.getConfirmation()) {
					return "Yes";
				} else {
					return "No";
				}
			}
		}
	}
	
	/**
	 * Updates the patient info displayed at the top of the pop up. Used after patient info
	 * has been edited.
	 */
	public void updatePatientInfo() {
		String text = "Patient Name: " + patient.getFirst() + " " + patient.getLast() +
			  	  	  "\nPhone Number: " + patient.getPhone() +
			  	  	  "\nComments: " + patient.getNotes() +
			  	  	  "\nNumber of No Shows: " + patient.getNoShows() +
			  	  	  "\n\nFuture Appointments: ";
		
		textArea.setText(text);
		textArea.updateUI();
	}
	
	/**
	 * Closes the window once the user hits the "OK" button.
	 */
	public void actionPerformed(ActionEvent e) {
		// User hit the Edit Patient button
		if (e.getActionCommand() == "edit") {
			EditPatientUI.ShowDialog(this, patient);
			updatePatientInfo();
		// User hit the OK button
		} else {
			displayPatientSearchUI.setVisible(false);
		}
	}
}