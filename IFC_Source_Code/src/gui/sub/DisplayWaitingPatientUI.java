package gui.sub;

import gui.Constants;
import gui.DateTimeUtils;
import gui.main.listeners.ScheduleWaitlistPatientListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;

import gui.main.WaitListPane;

/**
 * Displays information on an entry in the waitlist.
 * Also allows you to edit comments on that waitlist and schedule a patient directly from that waitlist.
 */
@SuppressWarnings("serial")
public class DisplayWaitingPatientUI extends JDialog implements ActionListener {
	private static DisplayWaitingPatientUI displayWaitingPatientUI;
	
	private ArrayList<AppointmentDto> apt;
	private JTable aptTable;
	
	private WaitlistDto waitingPatient;
	
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JTextArea textArea;
	private JTextArea noteArea;
        
        private WaitListPane pane;
	
	private static String comment = "";
	
	/** Creates the dialog for a patient on the waitlist **/
	private DisplayWaitingPatientUI(String name, WaitlistDto wp, WaitListPane pane) {
		setModal(true);
		setTitle(name);
		this.pane = pane;
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(500, 550));
		
		waitingPatient = wp;
		
		// Display patient information
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel(new BorderLayout());
		JPanel notePanel = new JPanel(new BorderLayout());
		
		PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(wp.getPatientID());
		
		String text = "Date Added: " + wp.getDate() + "\n" + 
					  "Time Added: " + wp.getTimeAdded() + "\n" +
					  "Patient Name: " + patient.getFirst() + " " + patient.getLast() + "\n" +
					  "Phone Number: " + patient.getPhone() + "\n" +
					  "Type: " + wp.getTypeName() + "\n\n";
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		textArea.setBorder(new EmptyBorder(10, 10, 0, 10));
		infoPanel.add(textArea, BorderLayout.NORTH);
		
		JLabel commentsLabel = new JLabel("Comments: ");
		commentsLabel.setFont(Constants.PARAGRAPH);
		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		noteArea.setWrapStyleWord(true);
		noteArea.setFont(Constants.PARAGRAPH);
		noteArea.setText(wp.getComments());
		notePanel.add(commentsLabel, BorderLayout.NORTH);
		notePanel.add(noteArea, BorderLayout.CENTER);
		notePanel.setBorder(new EmptyBorder(0, 10, 40, 10));
		
		// Display available appointments to schedule person off of waitlist
		apt= DataServiceImpl.GLOBAL_DATA_INSTANCE.searchForAppointments(wp.getTypeID());
		JComponent panel= displayAvailAppts();
		panel.setPreferredSize(new Dimension(500,250));

		topPanel.setPreferredSize(new Dimension(500, 250));
		topPanel.add(infoPanel, BorderLayout.NORTH);
		topPanel.add(notePanel, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		setResizable(false);
		
	}
	
	/** Displays the available appointments for someone on the waitlist **/
	private JComponent displayAvailAppts() {
		
		JPanel p = new JPanel(new BorderLayout());
    	JPanel panel = new JPanel(new BorderLayout());
    	JPanel topPanel = new JPanel(new BorderLayout());
    	
    	
    	//searchField.addKeyListener(this);
    	
    	
    	aptTable = new JTable(new ApptTableModel(apt)) {  
    		//Implement table cell tool tips.
    		/*
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

    		    	
    		    	tip = "<html>Patient: " + firstName + " " + lastName + "<br>" +
    		    		  "Phone Number: " + phoneNumber + "<br>" + 
    		    		  "Notes: ";

    		    	tip = tip.substring(0, tip.length() - 4);
    		    } else tip = "";
    		    return tip;
    		 
    		}
    		*/
    	};
    	
    	aptTable.setAutoCreateRowSorter(true);
    	aptTable.getTableHeader().setReorderingAllowed(false);

    	aptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	aptTable.setFont(Constants.PARAGRAPH);
    	aptTable.getTableHeader().setFont(Constants.PARAGRAPH);
    	aptTable.addMouseListener(new ScheduleWaitlistPatientListener(aptTable, this));
    	
    	JLabel scheduleApptLabel = new JLabel("Schedule an Appointment for the Waitlisted Patient: ");
    	scheduleApptLabel.setFont(Constants.PARAGRAPH);
    	
    	panel.add(aptTable.getTableHeader(), BorderLayout.PAGE_START);
    	panel.add(aptTable, BorderLayout.CENTER);
    	    	
    	JPanel buttonPanel = new JPanel(new FlowLayout());
    	okButton.setActionCommand("OK");
    	okButton.addActionListener(this);
    	okButton.setFont(Constants.DIALOG);
    	cancelButton.addActionListener(this);
    	cancelButton.setFont(Constants.DIALOG);
    	buttonPanel.add(okButton);
    	buttonPanel.add(cancelButton);
    	
    	
    	//Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(panel);
        
    	topPanel.add(scheduleApptLabel, BorderLayout.NORTH);
    	topPanel.add(scrollPane, BorderLayout.CENTER);
    	p.add(topPanel, BorderLayout.CENTER);
        p.add(buttonPanel, BorderLayout.SOUTH);
        return p;
	}
    
	
	public static String ShowDialog(Component owner, WaitlistDto waitp, WaitListPane pane) {
		
		String name= "Schedule Appointment for " + waitp.getPatient().getFullName();
		displayWaitingPatientUI = new DisplayWaitingPatientUI(name, waitp, pane);
		displayWaitingPatientUI.pack();
		displayWaitingPatientUI.setLocationRelativeTo(owner);
		displayWaitingPatientUI.setVisible(true);
		return comment;
	}
	
	/** Updates the database when someone is added to the waitlist or scheduled for an appointment **/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			if (aptTable.getSelectedRow() >= 0) {
				AppointmentDto appt = ((ApptTableModel)aptTable.getModel()).getAppointment(aptTable.getSelectedRow());
				WaitlistDto waitlistPatient = getWaitlistPatient();
				PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(waitlistPatient.getPatientID());
				TypeDto type = DataServiceImpl.GLOBAL_DATA_INSTANCE.getType(waitlistPatient.getTypeName());
				
				DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(patient.getPatID(), appt);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromWaitlist(patient, type);
                                pane.refreshAppointments(appt.getApptDate());
			}
			
			
			comment = noteArea.getText();
			DataServiceImpl.GLOBAL_DATA_INSTANCE.commentWaitlist(getWaitlistPatient(), comment);
		} 
		displayWaitingPatientUI.setVisible(false);
    }
	
	/** Retrieves a patient on the waitlist **/
	public WaitlistDto getWaitlistPatient() {
		return waitingPatient;
	}
	
	/** Creates a table for displaying all the appointments available for waitlisted people **/
	public class ApptTableModel extends AbstractTableModel {	

		ArrayList<AppointmentDto> appointments = new ArrayList<AppointmentDto>();
		
		public ApptTableModel(ArrayList<AppointmentDto> appointments) {
			this.appointments = appointments;
		}
		
		public AppointmentDto getAppointment(int row) {
			return appointments.get(row);
		}
		
		private String[] columnNames = { "Date", "Start Time", "End Time", "Practitioner" };
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getRowCount() {
			return appointments.size();
		}

		public Object getValueAt(int row, int col) {
			AppointmentDto a = appointments.get(row);
			if (col == 0) 
				return DateTimeUtils.prettyPrintMonthDay(a.getApptDate());
			else  if (col == 1)
				return a.prettyPrintStart();
			else if (col == 2)
				return a.prettyPrintEnd();
			else
				return a.getPractName();
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
	}
	
}