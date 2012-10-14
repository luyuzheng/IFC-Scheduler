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
import javax.swing.JPanel;
import javax.swing.JTextArea;


import data.Appointment;
import data.Constants;
import data.Date;
import data.DaySaver;
import data.Patient;

public class AppointmentBlock extends JPanel implements FocusListener {

	Appointment appointment;
	JTextArea textArea;
	NewPatientListener npl = new NewPatientListener(this, this.getParent());
	DayPanel dp;

	public AppointmentBlock(Appointment appointment, DayPanel dp) {

		this.dp = dp;
		this.appointment = appointment;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(new Font("Tahoma",Font.PLAIN,11));
		textArea.setOpaque(false);
		textArea.setHighlighter(null);
		int time = appointment.getTimeSlot().lengthInMinutes();
		setPreferredSize(new Dimension(0, time*Constants.PIXELS_PER_MINUTE));
		setMaximumSize(new Dimension(Integer.MAX_VALUE, time*Constants.PIXELS_PER_MINUTE));
		setMinimumSize(new Dimension(0, time*Constants.PIXELS_PER_MINUTE));
		setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		add(textArea, BorderLayout.CENTER);
		
		this.setFocusable(true);
		textArea.addFocusListener(this);
		
		if (appointment.isFilled()) setBackground(new Color(238,238,255));

		setText();
		
	}
	
	/**
	 * Sets the text of the appointment block. If the appointment is filled, it lists the time 
	 * slot and patient. Otherwise, just the patient. 
	 */
	public void setText() {
		String text = appointment.getTimeSlot().toString();
		if (appointment.isFilled()) {
			if (!appointment.getNote().equals("")) text += "   Note: " + appointment.getShortNote(50).replaceAll("\t\t", " ");
			text += "\n" + appointment.getPatient().getFullName() + " - ";
			if (appointment.getPatient().getNumber() == null) text += "No Phone # Specified";
			else text += appointment.getPatient().getNumberString();
		} 
		textArea.setText(text);
	}
	
	/**
	 * Sets the appointment
	 * @param pat the patient that is filling this appointment
	 */
	public void setPatient(Patient pat) {
		appointment.setPatient(pat);
		setText();
		new DaySaver().storeDay(dp.getDay());
	}

	/**
	 * When focus is gained, enable buttons on the "DayPanel" and change the background color
	 * of the appointment block. Also, add a mouse listener to begin looking for double-click events.
	 */
	public void focusGained(FocusEvent arg0) {
		dp.setPatButtonEnabled(true, this);
		if (appointment.isFilled()) 
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
		if (appointment.isFilled())
			setBackground(new Color(238, 238, 255));
		else
			setBackground(Color.WHITE);
		dp.setPatButtonEnabled(false, null);
		textArea.removeMouseListener(npl);
		
	}
	
	public void clearAppt() {
		setPatient(null);
	}
	
	public Appointment getAppointment() {
		return appointment;
	}
	
	public void setNote(String note) {
		appointment.setNote(note);
	}
	
}
