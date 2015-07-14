
package gui.sub;

import gui.Constants;
import gui.DateTimeUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

import gui.main.MainWindow;
/**
 * DisplayAppointmentConfirmationUI shows information about a patient's appointment when a patient in the table
 * is clicked.
 */
@SuppressWarnings("serial")
public class DisplayAppointmentConfirmationUI extends JDialog implements ActionListener {
	private static DisplayAppointmentConfirmationUI displayAppointmentConfirmationUI;
	
	private static AppointmentDto appointment;
	
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private JPanel notePanel;
	private JButton confirmButton = new JButton("Confirm");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JScrollPane scrollPane;
	private JScrollPane notePane;
	private JTextPane textPane;
	private JTextPane noteArea;
	
        private MainWindow main;
        
	/**
	 * Constructor - creates the actual UI to display the patient's appointment information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param appt - the appointment information to be displayed
	 */
	private DisplayAppointmentConfirmationUI(String name, AppointmentDto appt, MainWindow main) {
		appointment = appt;
		
        this.main = main;
                
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(450, 450));
		setResizable(true);
		
		infoPanel = new JPanel(new BorderLayout());
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		String text = getApptConfirmationText(appt);
		
		textPane = new JTextPane();
		textPane.setFont(Constants.PARAGRAPH);
		textPane.setContentType("text/html");
		textPane.setText("<html>" + text + "</html>");
		textPane.setEditable(false);
		textPane.setCaretPosition(0);
		textPane.setOpaque(false);
		textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); // Used so that font will display properly
	
		scrollPane = new JScrollPane(textPane);
		TitledBorder title = BorderFactory.createTitledBorder("Patient/Appointment Information");
		title.setTitleFont(Constants.PARAGRAPH_BOLD);
		scrollPane.setBorder(title);
		infoPanel.add(scrollPane);

		notePanel = new JPanel(new BorderLayout());
		JLabel noteLabel = new JLabel("<html><p><b>Appointment Confirmation Note:</b></p></html>");
		noteLabel.setFont(Constants.PARAGRAPH);

		noteArea = new JTextPane();
		noteArea.setFont(Constants.PARAGRAPH);
		noteArea.setText((appt.getNote()).replaceAll("\t\t", "\n"));
		
		notePane = new JScrollPane(noteArea);
		notePane.setPreferredSize(new Dimension(200,200));
		notePanel.add(noteLabel, BorderLayout.NORTH);
		notePanel.add(notePane, BorderLayout.CENTER);
		
		confirmButton.setFont(Constants.DIALOG);
		okButton.setFont(Constants.DIALOG);
		cancelButton.setFont(Constants.DIALOG);
		
		confirmButton.setActionCommand("confirm");
		okButton.setActionCommand("OK");
		cancelButton.setActionCommand("cancel");
		
		confirmButton.setAction(changeConfirmationAction);
		isConfirmedValidate();
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		buttonPanel.add(confirmButton);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		notePanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		
		add(infoPanel, BorderLayout.NORTH);
		add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private String getApptConfirmationText(AppointmentDto appt) {
		PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appt.getPatientID());
		String confirmed = (appt.getConfirmation() == true ? "Yes" : "No");
		String date = DateTimeUtils.prettyPrintMonthDay(new Date(appt.getApptDate().getTime()));
		
		String[] labels = {"Date", "Time Slot", "Patient Name", "Phone Number", "No Shows", "Confirmed"};
		String[] info = {date, 
						 appt.prettyPrintStart() + " - " + appt.prettyPrintEnd(), 
						 patient.getFirst() + " " + patient.getLast(),
						 patient.getPhone(),
						 patient.getNoShows().toString(),
						 confirmed
						 };
		String text = "<table>";
		for (int i = 0; i < labels.length; i++) {
			text += "<tr><td><b>" + labels[i] + ": </b></td><td align='left'>" + info[i] + "</td></tr><br />";
		}
		text += "</table>";

		return text;
	}
	
	private void refreshPatientInfo(AppointmentDto appt) {
		String text = getApptConfirmationText(appt);
		textPane.setText(text);
		textPane.updateUI();
	}
	
	/**
	 * Makes the pop up window visible when a patient from the table is selected.
	 * 
	 * @param owner - the component that owns this pane (the AppointmentConfirmationListener)
	 * @param appt  - the appointment information
	 */
	public static AppointmentDto ShowDialog(Component owner, AppointmentDto appt, MainWindow main) {
		displayAppointmentConfirmationUI = new DisplayAppointmentConfirmationUI("View Appointment", appt, main);
		displayAppointmentConfirmationUI.pack();
		displayAppointmentConfirmationUI.setLocationRelativeTo(owner);
		displayAppointmentConfirmationUI.setVisible(true);
		return appointment;
	}
	
	public void isConfirmedValidate() {
		if (appointment.getConfirmation()) {
			confirmButton.setText("<html>Unconfirm</html>");
		} else {
			confirmButton.setText("<html>Confirm</html>");
		}
	}
	
	private final AbstractAction changeConfirmationAction = new AbstractAction("<html>Confirm</html>") {
		public void actionPerformed(ActionEvent e) {
			if (!appointment.getConfirmation()) {
				appointment.setConfirmation(true);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.confirmAppointment(appointment);
				confirmButton.setText("<html>Unconfirm</html>");
			} else {
				appointment.setConfirmation(false);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.unConfirmAppointment(appointment);
				confirmButton.setText("<html>Confirm</html>");
			}
			refreshPatientInfo(appointment);
            main.refreshAppointments(appointment.getApptDate());
		}
	};
	
	/**
	 * Closes the window once the user hits the "OK" button.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "OK") {
			appointment.setNote(noteArea.getText());
			DataServiceImpl.GLOBAL_DATA_INSTANCE.addNotesToAppointment(appointment);
            main.refreshAppointments(appointment.getApptDate());
		} 
		displayAppointmentConfirmationUI.setVisible(false);
	}
	
}