/**
 * Stores all the data for a room -- the practitioner, the appointments, and 
 * any leftover time that can not be filled by appointments. It also keeps track
 * of the current day. 
 */

package data;

import java.util.ArrayList;

public class Room {
	private Day day;
	private Practitioner prac;
	private ArrayList<Appointment> appointments;
	private int leftoverMinutes = 0;
	
	public Room(Day day, Practitioner prac) {
		this.day = day;
		setPractitioner(prac);
	}
	
//	public Room(Day day) {
//		this.day = day;
//		prac = null;
//		appointments = null;
//		leftoverMinutes = 0;
//	}
	
	public void setPractitioner(Practitioner prac) {
		this.prac = prac;
		appointments = new ArrayList<Appointment>();
		if (prac != null) buildApptList();
	}
	
	public Practitioner getPractitioner() {
		return prac;
	}
	
	public int getLeftover() {
		return leftoverMinutes;
	}
	
	public boolean hasPrac() {
		if (prac == null) 
			return false;
		else return true;
	}
	
	public boolean isFull() {
		if (!hasPrac()) return false;
		for (Appointment a : appointments) {
			if (!a.isFilled()) return false;
		}
		return true;
	}
	
	/**
	 * Builds a list of appointments based on the given practitioner's appointment
	 * length time, and the day's time slot length.
	 */
	private void buildApptList() {
		int apptLength = prac.getApptLength();
		Date date = day.getDate();
		TimeSlot slot = day.getTimeSlot();
		Time t1 = slot.getStartTime();
		Time t2 = slot.getEndTime();
		int minDiff = t2.timeInMinutes() - t1.timeInMinutes();
		
		int appts = minDiff/apptLength;
		t2 = t1;
		for (int i = 0; i < appts; i++) {
			t1 = t2;
			t2 = t1.addMinutes(apptLength);
			TimeSlot ts = new TimeSlot(t1, t2);
			appointments.add(new Appointment(date, ts));			
		}
		leftoverMinutes = day.getTimeSlot().getEndTime().timeInMinutes() - t2.timeInMinutes();
	}
	
	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	
	public Appointment getAppointment(TimeSlot ts) {
		for (Appointment a : appointments) {
			if (a.getTimeSlot().toString().equals(ts.toString())) 
				return a;
		}
		return null;
	}
}
