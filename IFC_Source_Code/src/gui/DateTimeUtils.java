package gui;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** Contains methods to handle how dates and times are presented in the interface **/

public class DateTimeUtils {
	
	/** Prints a date using the short form of the month, the day, and the year **/
	public static String prettyPrintMonthDay(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		String monthString = "";
		
		switch (month) {
			case 1: monthString = "Jan"; break;
			case 2: monthString = "Feb"; break;
			case 3: monthString = "Mar"; break;
			case 4: monthString = "Apr"; break;
			case 5: monthString = "May"; break;
			case 6: monthString = "June"; break;
			case 7: monthString = "July"; break;
			case 8: monthString = "Aug"; break;
			case 9: monthString = "Sept"; break;
			case 10: monthString = "Oct"; break;
			case 11: monthString = "Nov"; break;
			case 12: monthString = "Dec"; break;
		}
		
		return (monthString + " " + day + ", " + year);
	}
}