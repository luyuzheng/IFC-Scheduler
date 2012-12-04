package gui.sub;

import gui.Constants;
import gui.main.AppointmentConfirmationPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

/**
 * DisplayAppointmentConfirmationUI shows information about a patient's appointment when a patient in the table
 * is clicked.
 */
public class DisplayAppointmentConfirmationUI extends JDialog implements ActionListener {
	private static DisplayAppointmentConfirmationUI displayAppointmentConfirmationUI;
	
	private static AppointmentDto appointment;
	
	private JPanel infoPanel;
	private JPanel buttonPanel;
	private JPanel notePanel;
	private JButton confirmButton = new JButton("Confirm");
	private JButton okButton = new JButton("OK");
	private JButton cancelButton = new JButton("Cancel");
	private JTextArea textArea;
	private JTextArea noteArea;
	
	/**
	 * Constructor - creates the actual UI to display the patient's appointment information
	 * @param name - the title to be displayed in the top bar of the pop up window
	 * @param appt - the appointment information to be displayed
	 */
	private DisplayAppointmentConfirmationUI(String name, AppointmentDto appt) {
		appointment = appt;
		
		setModal(true);
		setTitle(name);
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(350, 350));
		setResizable(false);
		
		infoPanel = new JPanel(new BorderLayout());
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appt.getPatientID());
		String confirmed = (appt.getConfirmation() == true ? "Yes" : "No");
		Date date = new Date(appt.getApptDate().getTime());
		
		String text = "Date: " + date.toString() + "\n" +
					  "Time Slot: " + appt.prettyPrintStart() + " - " + appt.prettyPrintEnd() + "\n" +
					  "Patient Name: " + patient.getFirst() + " " + patient.getLast() + "\n" +
					  "Phone Number: " + patient.getPhone() + "\n" +
					  "Confirmed: " + confirmed;
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		textArea.setText(text);
		infoPanel.add(textArea);

		notePanel = new JPanel(new BorderLayout());
		JLabel noteLabel = new JLabel("Appointment Confirmation Note:");
		noteLabel.setFont(Constants.PARAGRAPH);
		JScrollPane notePane = new JScrollPane();
		notePane.setPreferredSize(new Dimension(200,200));
		noteArea = new JTextArea();
		noteArea.setLineWrap(true);
		noteArea.setWrapStyleWord(true);
		noteArea.setFont(Constants.PARAGRAPH);

		noteArea.setText((appt.getNote()).replaceAll("\t\t", "\n"));
		notePane.setViewportView(noteArea);
		notePanel.add(noteLabel, BorderLayout.NORTH);
		notePanel.add(notePane, BorderLayout.CENTER);
		
		confirmButton.setFont(Constants.DIALOG);
		okButton.setFont(Constants.DIALOG);
		cancelButton.setFont(Constants.DIALOG);
		
		confirmButton.setActionCommand("confirm");
		okButton.setActionCommand("OK");
		cancelButton.setActionCommand("cancel");
		
		confirmButton.addActionListener(this);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		buttonPanel.add(confirmButton);
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 20, 10));
		
		add(infoPanel, BorderLayout.NORTH);
		add(notePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
		setResizable(false);
	}
	
	private void refreshPatientInfo(AppointmentDto appt) {
		PatientDto patient = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appt.getPatientID());
		String confirmed = (appt.getConfirmation() == true ? "Yes" : "No");
		Date date = new Date(appt.getApptDate().getTime());
		
		String text = "Date: " + date.toString() + "\n" +
					  "Patient Name: " + patient.getFirst() + " " + patient.getLast() + "\n" +
					  "Phone Number: " + patient.getPhone() + "\n" +
					  "Confirmed: " + confirmed;
		
		textArea.setText(text);
		textArea.updateUI();
	}
	
	/**
	 * Makes the pop up window visible when a patient from the table is selected.
	 * 
	 * @param owner - the component that owns this pane (the AppointmentConfirmationListener)
	 * @param appt  - the appointment information
	 */
	public static AppointmentDto ShowDialog(Component owner, AppointmentDto appt) {
		displayAppointmentConfirmationUI = new DisplayAppointmentConfirmationUI("View Appointment", appt);
		displayAppointmentConfirmationUI.pack();
		displayAppointmentConfirmationUI.setLocationRelativeTo(owner);
		displayAppointmentConfirmationUI.setVisible(true);
		return appointment;
	}
	
	/**
	 * Closes the window once the user hits the "OK" button.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "confirm") {
			if (!appointment.getConfirmation()) {
				appointment.setConfirmation(true);
				DataServiceImpl.GLOBAL_DATA_INSTANCE.confirmAppointment(appointment);
			// TODO: Want to unconfirm
			} else {

			}
			refreshPatientInfo(appointment);
		} else if (e.getActionCommand() == "OK") {
			DataServiceImpl.GLOBAL_DATA_INSTANCE.addNotesToAppointment(appointment);
			displayAppointmentConfirmationUI.setVisible(false);
		} else {
			displayAppointmentConfirmationUI.setVisible(false);
		}
	}
	
}