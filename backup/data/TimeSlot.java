/**
 * A time slot is two time objects corresponding to the start and end of the 
 * slot. 
 */

package data;

public class TimeSlot {
	private Time startTime;
	private Time endTime;
	
	public TimeSlot(Time st, Time et) {
		startTime = st;
		endTime = et;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
	public int lengthInMinutes() {
		return endTime.timeInMinutes() - startTime.timeInMinutes();
	}
	
	public String toString() {
		return startTime.toString() + " - " + endTime.toString();
	}
	
	public String toFileString() {
		return startTime.to24HrString() + "-" + endTime.to24HrString();
	}
}
