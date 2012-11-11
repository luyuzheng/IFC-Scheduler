/**
 * An appointment block is the actual square containing an appointment. 
 */

package gui.main;

import gui.main.listeners.NewPatientListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import backend.DataService.DataService;
import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.PatientDto;

import gui.Constants;


public class AppointmentBlock extends JPanel implements FocusListener {

	AppointmentDto appointment;
	JTextArea textArea;
	NewPatientListener npl = new NewPatientListener(this, this.getParent());
	DayPanel dp;

	public AppointmentBlock(AppointmentDto appointment, DayPanel dp) {

		this.dp = dp;
		this.appointment = appointment;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial",Font.PLAIN,14));
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
		
		if (appointment.getPatientID() != null) setBackground(new Color(238,238,255));

		setText();
		
	}
	
	/**
	 * Sets the text of the appointment block. If the appointment is filled, it lists the time 
	 * slot and patient. Otherwise, just the patient. 
	 */
	public void setText() {
		JLabel timeslot= new JLabel(appointment.getStart().toString() + " - " + appointment.getEnd().toString());
		timeslot.setFont(new Font("Arial",Font.BOLD, 14));
		add(timeslot, BorderLayout.NORTH);
		String text = "";
		if (appointment.getPatientID() != null) {
			int patientId= appointment.getPatientID();
			PatientDto patient= DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(patientId); 
			
			text += patient.getFirst() + " " + patient.getLast() + " - ";
			if (patient.getPhone() == null) text += "No Phone # Specified";
			else text += patient.getPhone();
			if (!appointment.getNote().equals("")) text += "\n\nNote: " + appointment.getShortNote(50).replaceAll("\t\t", " ");
		} 
		textArea.setText(text);
	}
	
	/**
	 * Sets the appointment
	 * @param patId the patient that is filling this appointment
	 */
	public void setPatient(int patId) {
		appointment.setPatientID(patId);
		setText();
		// TODO new DaySaver().storeDay(dp.getDay()); WHAT IS THISSSSSSS GARBAGE
	}

	/**
	 * When focus is gained, enable buttons on the "DayPanel" and change the background color
	 * of the appointment block. Also, add a mouse listener to begin looking for double-click events.
	 */
	public void focusGained(FocusEvent arg0) {
		dp.setPatButtonEnabled(true, this);
		if (appointment.getPatientID() != null) 
			setBackground(new Color(255,200,200));
		else
			setBackground(new Color(200,200,255));
		textArea.addMouseListener(npl);
	}

	/**
	 * When focus is lost, disable buttons on the "DayPanel" and change the background color
	 * of the block back to normal. Remove double-click mouse listener. 
	 */
	public void focusLost(FocusEvent arg0) {
		if (appointment.getPatientID() != null)
			setBackground(new Color(238, 238, 255));
		else
			setBackground(Color.WHITE);
		dp.setPatButtonEnabled(false, null);
		textArea.removeMouseListener(npl);
		
	}
	
	public void clearAppt() {
		appointment.setPatientID(null);
	}
	
	public AppointmentDto getAppointment() {
		return appointment;
	}
	
	public void setNote(String note) {
		appointment.setNote(note);
	}
	
}
