/**
 * Stores a time in hours and minutes. It stores the time in military form, 
 * such that 2:00 PM is really 14:00, etc.
 */

package data;

public class Time {
	private int hour;
	private int minute;
	
	public Time(int h, int m) {
		hour = h;
		minute = m;
	}
	
	public int getHour() {
		return hour;
	}
	
	public int getMinute() {
		return minute;
	}
	
	public int timeInMinutes() {
		return (hour*60) + minute;
	}
	
	/**
	 * Generates a time a certain number of minutes after this time
	 * @param min the number of minutes after this time
	 * @return Time
	 */
	public Time addMinutes(int min) {
		int newHour, newMinute;
		newMinute = (min + minute) % 60;
		if ((min + minute) >= 60) 
			newHour = hour + ((min+minute)/60);
		else 
			newHour = hour;
		return new Time(newHour, newMinute);
	}
	
	public String toString() {
		String m;
		if (minute < 10) 
			m = "0" + minute;
		else
			m = minute + "";
		
		if (hour > 12) 
			return (hour - 12) + ":" + m + " PM";
		else if (hour==12)
			return hour + ":" + m + " PM";
		else
			return hour + ":" + m + " AM";
	}
	
	public String to24HrString() {
		String m;
		if (minute < 10) 
			m = "0" + minute;
		else
			m = minute + "";
		String h;
		if (hour < 10) 
			h = "0" + hour;
		else
			h = hour + "";
		return h + ":" + m;
	}
	
	public String[] toPrintString() {
		String m;
		if (minute < 10) 
			m = "0" + minute;
		else
			m = minute + "";
		
		if (hour > 12) 
			return new String[] {(hour - 12) + ":" + m, "PM"};
		else if (hour==12)
			return new String[] {hour + ":" + m, "PM"};
		else
			return new String[] {hour + ":" + m,"AM"}; 
	}
}
