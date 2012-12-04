/**
 * Contains all the constants for the application.
 */

package gui;

import java.awt.Font;
//import java.io.File;



public class Constants {
	/** The number of pixels that each minute corresponds to in the calendar area */
	public static final int PIXELS_PER_MINUTE = 4;
	/** The height of the Heading Panel in the Day Panel. */
	public static final int HEADING_PANEL_HEIGHT = 75;
	/** The width of the Time Panel column in the Day Panel. */
	public static final int TIMES_PANEL_WIDTH = 75;
	/** The minimum width of an appointment block. */
	public static final int MIN_APPT_PANEL_WIDTH = 300;
	/** The minimum height of an appointment block. */
	public static final int MIN_APPT_PANEL_HEIGHT = 300;
	/** The width of the Appointment Subpanel. */
	public static final int APPT_SUBPANEL_WIDTH = 200;
	/** The default start time for a day, given in minutes since 12AM. Default is 4PM. */
	public static final int DEFAULT_START_TIME = 960;
	/** The default closing time for a day, given in minutes since 12AM. Default is 8PM. */
	public static final int DEFAULT_END_TIME = 1200;
	/** The width of a day block in Month Panel. */
	public static final int MONTH_BLOCK_WIDTH = 150;
	/** The height of a day block in Month Panel. */
	public static final int MONTH_BLOCK_HEIGHT = 100;
	
	
	//public static final String PRAC_FILE_LOCATION = "data" + File.separator + "prac.txt";
	//public static final String PAT_FILE_LOCATION = "data" + File.separator + "pat.txt";
	//public static final String DATA_FILE_LOCATION = "data" + File.separator + "data_";
	//public static final String TYPE_FILE_LOCATION = "data" + File.separator + "type.txt";
	//public static final String WAITLIST_FILE_LOCATION = "data" + File.separator + "waitlist.txt";
	//public static final String DEFAULT_FILE_LOCATION = "data" + File.separator + "defaults.txt";
	
	/** Default font for header styles */
	public static final Font HEADER = new Font("Arial", Font.BOLD, 16);
	/** Default font for paragraph styles */
	public static final Font PARAGRAPH = new Font("Arial", Font.PLAIN, 14);
	/** Default font for bold paragraph styles */
	public static final Font PARAGRAPH_BOLD = new Font("Arial", Font.BOLD, 14);
	/** Default font for dialog buttons */
	public static final Font DIALOG = new Font("Arial", Font.PLAIN, 16);
	/** Default font for printable text **/
	public static final Font PRINTABLE = new Font("Arial", Font.PLAIN, 12);
	
	/** The horizontal print margin, in pixels */
	public static final int PRINT_MARGINX = 25;
	/** The vertical print margin, in pixels */
	public static final int PRINT_MARGINY = 25;
}
