/**
 * A time slot is two time objects corresponding to the start and end of the 
 * slot. 
 */

package gui;

public class TimeSlot {
	private int startTime;
	private int endTime;
	
	public TimeSlot(int st, int et) {
		startTime = st;
		endTime = et;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getEndTime() {
		return endTime;
	}
	
	public int length() {
		return endTime - startTime;
	}
	
	public int getHours(int time) {
		return time/60;
	}
	
	public int getMinutes(int time) {
		return time%60;
	}
	
	public String toString() {
		String string= "" + getHours(startTime) + ":" + getMinutes(startTime) + " - " + getHours(endTime) + ":" + getMinutes(endTime);
		return string;
	}
	
}
