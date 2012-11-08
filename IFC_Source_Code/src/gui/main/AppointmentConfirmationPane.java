package gui.main;

import gui.main.WaitListPane.WaitlistTableModel;
import gui.main.listeners.AppointmentConfirmationListener;
import gui.main.listeners.WaitlistPatientListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import data.Date;
import data.Appointment;
import data.managers.WaitlistManager;

public class AppointmentConfirmationPane extends JPanel {

	private Component owner;
	private JTable table;
	//private AppointmentConfirmationManager acm = new AppointmentConfirmationManager();
	
	private Font font = new Font("Arial", Font.PLAIN, 16);
	
	public AppointmentConfirmationPane(Component owner, DayPanel dp) {
		this.owner = owner;
		setMinimumSize(new Dimension(0,0));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		
		// Create heading for the appointment confirmation panel
		JPanel apptConfirmationPanel = new JPanel(new GridLayout(0, 1));
		apptConfirmationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		String date = dp.getDay().getDate().toFormalString();
		JLabel apptConfirmationLabel = new JLabel("Appointments to Confirm for " + date + ":");
		apptConfirmationLabel.setFont(font);
		
		// Add header label to the panel
		apptConfirmationPanel.add(apptConfirmationLabel);
		add(apptConfirmationPanel, BorderLayout.NORTH);
		
		// Create panel to display patients for a particular day
		JPanel tablePanel = new JPanel(new BorderLayout());
		AppointmentConfirmationTableModel model = new AppointmentConfirmationTableModel(new ArrayList<Appointment>()); // CHANGE THIS LATER!!!
		table = new JTable(model);
		table.setDragEnabled(true);
		table.setFont(font);
		table.addMouseListener(new AppointmentConfirmationListener(table, this));
		table.setAutoCreateRowSorter(true);
    	table.getTableHeader().setReorderingAllowed(false);
    	table.getTableHeader().setFont(font);
    	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tablePanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
    	tablePanel.add(table, BorderLayout.CENTER);
    	JScrollPane scrollPane = new JScrollPane(tablePanel);
    	add(scrollPane, BorderLayout.CENTER);
	}
	
	public void resetModel() {
		//table.setModel(new AppointmentConfirmationTableModel(acm.getConfirmationList()));
	}
	
	public class AppointmentConfirmationTableModel extends AbstractTableModel {
		private String[] columnNames;
		private ArrayList<Appointment> confirm;

		public AppointmentConfirmationTableModel(ArrayList<Appointment> confirm) {
			this.confirm = confirm;
			columnNames = new String[] {"Confirmed", "First Name", "Last Name", "Phone Number"};
		}
		
		public Appointment getAppointment(int row) {
			return confirm.get(row);
		}
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return confirm.size();
		}

		public Object getValueAt(int row, int col) {
			Appointment appt = confirm.get(row);
			
			if (col == 0) {
				return appt.getConfirmed();
			} else if (col == 1) {
				return appt.getPatient().getFirstName();
			} else if (col ==2) {
				return appt.getPatient().getLastName();
			} else {
				return appt.getPatient().getNumberString();
			}
		}
	}
}