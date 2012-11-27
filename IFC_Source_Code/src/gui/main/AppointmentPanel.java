/**
 * An appointment panel is a large scroll pane consisting of all headings, rooms, and time indicators. 
 * It is the primary scheduling area of the application. This class encapsulates the panel in which all
 * of the appointments appear.
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.*;
import gui.Constants;

@SuppressWarnings("serial")
public class AppointmentPanel extends JScrollPane implements Printable, ActionListener {
	/** The date associated with the appointment panel. Each date has its own set of appointments. */
	DayDto day;
	/** The collection of printable pages of the appointment panel. */
	ArrayList<Graphics2D> pages;
	/** The side panel containing the time information for the appointment panel. */
	SidePanel sidePanel;

	/** Constructs the Appointment Panel given the Day Panel object. */
	public AppointmentPanel(DayPanel dp) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel panel = new JPanel(new BorderLayout());
		sidePanel = new SidePanel(dp);
		dp.setSidePanel(sidePanel);
		panel.add(sidePanel, BorderLayout.WEST);
		AppointmentSubpanel as = new AppointmentSubpanel(dp);

		day = dp.getDay();
		for (SchedulePractitionerDto r : DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day)){
			as.addRoom(r);
		}
		panel.add(as, BorderLayout.CENTER);
		setViewportView(panel);
	}

	/** Truncates a string if longer than a specified length, to support readability. */
	private String formatString(String s, int lineLength) {
		if (s.length() > lineLength) return s.substring(0, lineLength-3) + "...";
		else return s;
	}

	/** Returns the full name and type of a practitioner as a String. */
	private String getPracInfo(PractitionerDto p, int lineLength) {
		return formatString(p.getFirst() + " " + p.getLast() + " - " + p.getTypeName().toString(), lineLength);
	}

	/** Builds the page to be printed/shown on the appointment panel. */
	private Graphics2D buildPage(Graphics g, double width, double height, int page) {

		
		//# rooms (i.e., the total number of practitioners scheduled for the day)
		//System.out.print("FirstFirsttest");
		int r = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day).size();

		//set the font for the page
		Font font = new Font("Arial", Font.PLAIN, 12);

		//create the graphics object
		Graphics2D g2d = (Graphics2D) g.create();
		
		//set the font, find the line height
		g2d.setFont(font);
		FontMetrics metrics = g2d.getFontMetrics(font);
		int hgt = metrics.getHeight();

		//top height is the height of the practitioner info box
		double topHeight = 2*hgt + 8;
		//times width is the width of the left times panel
		double timesWidth = 30.0;
		
		//end time is the end # minutes, currTime is the current time
		int endTime = day.getEnd();
		int currTime = day.getStart();
		
		//current drawing point
		int startx = Constants.PRINT_MARGINX;
		int starty = Constants.PRINT_MARGINY;
		
		String[] dateSt = toDateArray(day.getDate());
		g2d.drawString(dateSt[0], startx + 5, starty + hgt);
		g2d.drawString(dateSt[1], startx + 5, starty + 2*hgt);
		
		starty = Constants.PRINT_MARGINY + (int)topHeight;
		
		//leftover is the number of minutes in the first box (if it ends oddly)
		int leftover = 60 - (currTime % 60);
		if (leftover > 0) {
			Rectangle2D.Double topTime = new Rectangle2D.Double ();
			topTime.setRect (startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
			g2d.draw(topTime);
			String timeSt = ((Integer) ((currTime / 60) % 12)).toString() + ":" + ((Integer) (currTime % 60)).toString();
			String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
			g2d.drawString(timeSt, startx + 5, starty + hgt);
			g2d.drawString(amPm, startx + 10, starty + 2*hgt);
			starty += leftover * Constants.PIXELS_PER_MINUTE;
			currTime+=leftover;
		}
		
		while (currTime + 60 < endTime) {
			Rectangle2D.Double timeBlock = new Rectangle2D.Double ();
			timeBlock.setRect (startx, starty, timesWidth, 60 * Constants.PIXELS_PER_MINUTE);
			g2d.draw(timeBlock);
			String timeSt = ((Integer) ((currTime / 60) % 12)).toString() + ":" + ((Integer) (currTime % 60)).toString();
			String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
			g2d.drawString(timeSt, startx + 5, starty + hgt);
			g2d.drawString(amPm, startx + 10, starty + 2*hgt);
			starty += 60 * Constants.PIXELS_PER_MINUTE;
			currTime+=60;
		}
		
		leftover = endTime - currTime;
		Rectangle2D.Double bottomTime = new Rectangle2D.Double ();
		bottomTime.setRect (startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
		g2d.draw(bottomTime);
		String timeSt = ((Integer) ((currTime / 60) % 12)).toString() + ":" + ((Integer) (currTime % 60)).toString();
		String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
		g2d.drawString(timeSt, startx + 5, starty + hgt);
		g2d.drawString(amPm, startx + 10, starty + 2*hgt);
		
		width -= timesWidth;
		double colWidth;
		
		int lineLength = 100;
		
		int roomsLeft = r - page*3;
		
		if (roomsLeft == 1) colWidth = width;
		else if (roomsLeft == 2) {
			colWidth = width / 2.0;
			lineLength = 50;
		}
		else {
			colWidth = width / 3.0;
			roomsLeft = 3;
			lineLength = 30;
		}
		//System.out.println("FirstTest");

		for (int j = 0; j < roomsLeft; j++) {
                    //System.out.println("OtherTest");
			Rectangle2D.Double rect = new Rectangle2D.Double ();
			startx = Constants.PRINT_MARGINX + (int)timesWidth + (int)colWidth*j;
			starty = Constants.PRINT_MARGINY;
			rect.setRect (startx, starty, colWidth, topHeight);
			g2d.draw(rect);

			PractitionerDto p = day.getRooms().get(page*3 + j).getPractitioner();
			g2d.drawString(getPracInfo(p, lineLength), startx+5, starty+hgt);
			g2d.drawString(formatString(p.getNotes().replaceAll("\t\t", "\n"), lineLength), startx+5, starty+2*hgt);
			ArrayList<AppointmentDto> appts = day.getRooms().get(page*3 + j).getAppointments();

			starty += topHeight;

			for (AppointmentDto a : appts) {
				int min = a.getEnd() - a.getStart();
				int blockHeight = min*Constants.PIXELS_PER_MINUTE;
				Rectangle2D.Double apptBlock = new Rectangle2D.Double ();
				apptBlock.setRect (startx, starty, colWidth, blockHeight);
				g2d.draw(apptBlock);

				String line1 = a.getStart().toString() + a.getEnd().toString();
				String line2 = "";
				if (a.getPatientID() != null) {
                                        PatientDto pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(a.getPatientID());
					if (!pat.getNotes().equals("")) line1 += "   " + pat.getNotes().replaceAll("\t\t", "\n");
					line2 = pat.getFullName() + " - " + pat.getPhone();
					if (line2.length() > lineLength) {
						line2 = pat.getFirst().substring(0,1) + ". " + pat.getLast() + " - " + pat.getPhone();
					}
					if (line2.length() > lineLength) { 
						line2 = formatString(pat.getPhone() + " - " + pat.getFullName(), lineLength);
					}
				}
				g2d.drawString(formatString(line1, lineLength), startx+5, starty+hgt);
				g2d.drawString(line2, startx+5, starty+2*hgt);

				starty += blockHeight;
			}
		}


		return g2d;
	}

	/** Returns a string representation of a given date. */
	private String[] toDateArray(Date date) {
		String full = date.toString();
		String[] dateArray = new String[2];
		dateArray[0] = full.substring(0, 3);
		dateArray[1] = full.substring(5,9);
		return dateArray;
	}

	/** Prints the visible contents of the appointment pane of a given page. */
	public int print(Graphics graphics, PageFormat pageFormat, int page) {
		double width = pageFormat.getImageableWidth() - 2*Constants.PRINT_MARGINX;
		double height = pageFormat.getImageableHeight() - 2*Constants.PRINT_MARGINY;
		int pages = (int)Math.ceil((double)day.getRooms().size() / 3.0);
		
		if (page >= pages) {
			return NO_SUCH_PAGE;
		}

		graphics = buildPage(graphics, width, height, page);
		/* Print the entire visible contents of a java.awt.Frame */

		return PAGE_EXISTS;

	}

	/** Sets the view as printable and prints it. */
	public void actionPerformed(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
			}
		}

	}
}
