/**
 * This class creates an Appointment data structure. Appointments have a timeslot 
 * and may have a patient. 
 */

package data;

public class Appointment {
	private Date date;
	private TimeSlot slot;
	private Patient pat;
	private String note;
	
	public Appointment(Date d, TimeSlot ts) {
		date = d;
		slot = ts;
		pat = null;
		note = "";
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date d) {
		date = d;
	}
	
	public Patient getPatient() {
		return pat;
	}
	
	/**
	 * sets the patient for this appointment
	 * @param pat
	 */
	public void setPatient(Patient pat) {
		this.pat = pat;
	}
	
	/**
	 * checks if the appointment is filled or not
	 * @return true if the appointment has a patient
	 */
	public boolean isFilled() {
		if (pat == null) return false;
		else return true;
	}
	
	/**
	 * gets the time slot for this appointment
	 * @return TimeSlot
	 */
	public TimeSlot getTimeSlot() {
		return slot;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getShortNote(int chars) {
		if (note.length() > chars) 
			return note.substring(0,chars) + "...";
		else return note;
	}
}
