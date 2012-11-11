/**
 * Contains all the constants for the application
 */

package gui;

import java.io.File;


public class Constants {
	/**
	 * The number of pixels that each minute corresponds to in the calendar area
	 */
	public static final int PIXELS_PER_MINUTE = 4;
	public static final int HEADING_PANEL_HEIGHT = 75;
	public static final int TIMES_PANEL_WIDTH = 75;
	public static final int MIN_APPT_PANEL_WIDTH = 300;
	public static final int MIN_APPT_PANEL_HEIGHT = 300;
	public static final int APPT_SUBPANEL_WIDTH = 200;
	public static final Time DEFAULT_START_TIME = new Time(16,0);
	public static final Time DEFAULT_END_TIME = new Time(20,0);
	public static final TimeSlot DEFAULT_TIMESLOT = new TimeSlot(DEFAULT_START_TIME,DEFAULT_END_TIME);
	public static final int MONTH_BLOCK_WIDTH = 150;
	public static final int MONTH_BLOCK_HEIGHT = 100;
	
	public static final String PRAC_FILE_LOCATION = "data" + File.separator + "prac.txt";
	public static final String PAT_FILE_LOCATION = "data" + File.separator + "pat.txt";
	public static final String DATA_FILE_LOCATION = "data" + File.separator + "data_";
	public static final String TYPE_FILE_LOCATION = "data" + File.separator + "type.txt";
	public static final String WAITLIST_FILE_LOCATION = "data" + File.separator + "waitlist.txt";
	public static final String DEFAULT_FILE_LOCATION = "data" + File.separator + "defaults.txt";
	
	public static final int PRINT_MARGINX = 25;
	public static final int PRINT_MARGINY = 25;
}
