/**
 * An appointment block is the square containing an appointment. 
 */

package gui.main;

import gui.main.listeners.NewPatientListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

import gui.Constants;

import gui.main.MainWindow;

@SuppressWarnings("serial")
public class AppointmentBlock extends JPanel implements FocusListener {

	/** The appointment associated with an appointment block. */
	AppointmentDto appointment;
	/** The text of the appointment block. */
	JTextArea textArea;
        MainWindow main;
	/** Patient Listener to schedule and cancel appointments. */
	NewPatientListener npl;
	/** The Day Panel that contains the appointment block. */
	DayPanel dp;

	/** Constructs an appointment block object given an appointment and pointer to the Day Panel. */
	public AppointmentBlock(AppointmentDto appointment, DayPanel dp, MainWindow main) {
        this.main = main;
        npl = new NewPatientListener(this, this.getParent(), main);
		this.dp = dp;
		this.appointment = appointment;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(Constants.PARAGRAPH);
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		int time = appointment.getEnd() - appointment.getStart();
		setPreferredSize(new Dimension(0, time*Constants.PIXELS_PER_MINUTE));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, time*Constants.PIXELS_PER_MINUTE));
		setMinimumSize(new Dimension(0, time*Constants.PIXELS_PER_MINUTE));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK), new EmptyBorder(5,5,5,5)));
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(textArea, BorderLayout.CENTER);
		
		this.setFocusable(true);
		textArea.addFocusListener(this);
		
		if (appointment.getPatientID() != null && appointment.getPatientID() != 0) setBackground(new Color(238,238,255)); // light blue

		setText();
		
	}
	
	/**
	 * Sets the text of the appointment block. If the appointment is filled, it lists the time 
	 * slot and patient. Otherwise, just the patient. 
	 */
	public void setText() {
		JLabel timeslot= new JLabel(appointment.prettyPrintStart() + " - " + appointment.prettyPrintEnd());
		timeslot.setFont(Constants.PARAGRAPH_BOLD);
		add(timeslot, BorderLayout.NORTH);
		String text = "";
		
		if (appointment.getPatientID() != null && appointment.getPatientID() != 0){
			int patientId= appointment.getPatientID();
			PatientDto patient= DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(patientId); 
			
			text += patient.getFirst() + " " + patient.getLast() + " - ";
			if (patient.getPhone() == null) text += "No Phone # Specified";
			else text += patient.getPhone();

			if (!appointment.getNote().equals("")) text += "\nNote: " + appointment.getShortNote(50).replaceAll("\t\t", " ");
			if (appointment.getConfirmation()) {
				text += "\n--CONFIRMED--";
			}
			if (appointment.getNoShowID() != null && appointment.getNoShowID() > 0) {
				text += "\n--NO SHOW--";
			}
		} 

		if (!(text.equals(""))) {
			textArea.setToolTipText("<html>" + text.replaceAll("\n", "<br>") + "</html>");
		}
		textArea.setText(text);
	}
	
	/**
	 * Sets the appointment
	 * @param patId the patient that is filling this appointment
	 */
	public void setPatient(int patId) {
		appointment.setPatientID(patId);
        DataServiceImpl.GLOBAL_DATA_INSTANCE.addPatientToAppointment(patId, appointment);
		setText();
		setBackground(new Color(238, 238, 255));
        main.refreshConfirmationPane();
	}

	/**
	 * When focus is gained, enables buttons on the "DayPanel" and change the background color
	 * of the appointment block. Also, adds a mouse listener to begin looking for double-click events.
	 */
	public void focusGained(FocusEvent arg0) {
		dp.setPatButtonEnabled(true, this);
		if (appointment.getPatientID() != null && appointment.getPatientID() != 0) 
			setBackground(new Color(255,200,200)); // red
		else
			setBackground(new Color(200,200,255)); // darker blue
		textArea.addMouseListener(npl);		
	}

	/**
	 * When focus is lost, disables buttons on the "DayPanel" and change the background color
	 * of the block back to normal. Removes double-click mouse listener. 
	 */
	public void focusLost(FocusEvent arg0) {
		if (appointment.getPatientID() != null && appointment.getPatientID() != 0)
			setBackground(new Color(238, 238, 255));
		else
			setBackground(Color.WHITE);
		dp.setPatButtonEnabled(false, null);
		textArea.removeMouseListener(npl);
		
	}
	
	/** Clears the appointment block so that it shows up empty. */
	public void clearAppt() {
		appointment.setPatientID(null);
        DataServiceImpl.GLOBAL_DATA_INSTANCE.removePatientFromAppointment(appointment);
        setText();
	}
	
	/** Returns the appointment associated with an appointment block. */
	public AppointmentDto getAppointment() {
		return appointment;
	}
	
	/** Sets the note associated with an appointment. */
	public void setNote(String note) {
		appointment.setNote(note);
        DataServiceImpl.GLOBAL_DATA_INSTANCE.addNotesToAppointment(appointment);
        setText();
	}
	
}
