/**
 * Displays the entire month view, which shows all the days in the month and the practitioners scheduled on those days. 
 */

package gui.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.DataService.DataServiceImpl;
import backend.DataTransferObjects.AppointmentDto;
import backend.DataTransferObjects.DayDto;
import backend.DataTransferObjects.SchedulePractitionerDto;

import gui.Constants;
import java.sql.Date;
import backend.DataTransferObjects.PractitionerDto;

@SuppressWarnings("serial")
public class MonthPanel extends JScrollPane implements Printable, ActionListener {
	/** Current date that is being viewed in the month panel. */
	Date d;
	
	/** Constructs a month panel given a day panel object. */
	public MonthPanel(DayPanel dp) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel panel = new JPanel(new GridLayout(6,7));

		DayDto day = dp.getDay();
		this.d = day.getDate();
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(this.d);
		
		int m= cal.get(Calendar.MONTH) + 1;
		
		String month = "December";
		if (m == 1) month = "January";
		else if (m == 2) month = "February";
		else if (m == 3) month = "March";
		else if (m == 4) month = "April";
		else if (m == 5) month = "May";
		else if (m == 6) month = "June";
		else if (m == 7) month = "July";
		else if (m == 8) month = "August";
		else if (m == 9) month = "September";
		else if (m == 10) month = "October";
		else if (m == 11) month = "November";
		
		month = month + " " + cal.get(Calendar.YEAR);
		
		//prevDays is the number of boxes in the upper left, before the first of the month, needed since the 
		//calendar is going to be a 6x7 set of boxes. Calendar.SUNDAY is 1 and so forth, so we use day of week - 1
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int prevDays = cal.get(Calendar.DAY_OF_WEEK) - 1; 
		int endDays = 42 - cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays;
		
		Calendar c = cal;
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.add(Calendar.DAY_OF_MONTH, (-1) * prevDays + 1);
		for (int i = 1; i <= prevDays; i++) {
			java.sql.Date date = new Date(c.getTime().getTime());
			panel.add(new DayBlock(date, Color.LIGHT_GRAY));
			c.add(Calendar.DAY_OF_MONTH, 1);
		}

		c.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			java.sql.Date date = new Date(c.getTime().getTime());
			panel.add(new DayBlock(date));
		}
		
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		for (int i = 1; i <= endDays; i++) {
			c.set(Calendar.DAY_OF_MONTH, i);
			java.sql.Date date = new Date(c.getTime().getTime());
			panel.add(new DayBlock(date, Color.LIGHT_GRAY));
		}
		
		MonthHeadingPanel mhp = new MonthHeadingPanel(month);
		JPanel main = new JPanel(new BorderLayout());
		main.add(mhp, BorderLayout.NORTH);
		
		main.add(panel, BorderLayout.CENTER);
		
		setViewportView(main);
	}
	
	/** Truncates text if it is too long to display in the month view. */
	private String formatString(String s, int lineLength) {
		if (s.length() > lineLength) return s.substring(0,lineLength-3) + "..."; 
		else return s;
	}
	
	/** Builds the printable/viewable page containing all the days in the month. */
	private Graphics buildPage(Graphics g, double width, double height, int page) {
		
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(d);
		
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D.Double rect = new Rectangle2D.Double ();
		double startx = Constants.PRINT_MARGINX;
		double starty = Constants.PRINT_MARGINY;
		width+= Constants.PRINT_MARGINX;
		
		g2d.setFont(Constants.HEADER);
		FontMetrics metrics = g2d.getFontMetrics();
		int hgt = metrics.getHeight();
		
		rect.setRect(startx, starty, width, hgt+8);
		g2d.draw(rect);
		
		/*
		FontMetrics metrics = g2d.getFontMetrics(Constants.PRINTABLE);
		int hgt = metrics.getHeight();
		
		rect.setRect(startx, starty, width, 50);
		g2d.draw(rect);
		g2d.setFont(Constants.HEADER);
		*/
		
		// print month heading
		String heading;
		int month = cal.get(Calendar.MONTH);
		if (month == 0) heading = "January " + cal.get(Calendar.YEAR);
		else if (month == 1) heading = "February " + cal.get(Calendar.YEAR);
		else if (month == 2) heading = "March " + cal.get(Calendar.YEAR);
		else if (month == 3) heading = "April " + cal.get(Calendar.YEAR);
		else if (month == 4) heading = "May " + cal.get(Calendar.YEAR);
		else if (month == 5) heading = "June " + cal.get(Calendar.YEAR);
		else if (month == 6) heading = "July " + cal.get(Calendar.YEAR);
		else if (month == 7) heading = "August " + cal.get(Calendar.YEAR);
		else if (month == 8) heading = "September " + cal.get(Calendar.YEAR);
		else if (month == 9) heading = "October " + cal.get(Calendar.YEAR);
		else if (month == 10) heading = "November " + cal.get(Calendar.YEAR);
		else heading = "December " + cal.get(Calendar.YEAR);
		
		//FontMetrics f = g2d.getFontMetrics();
		int offsetx = (int)(width / 2.0 - metrics.charWidth(' ') * (heading.length() / 2.0));
		g2d.drawString(heading, (int)startx + offsetx, (int)starty + hgt);
		
		starty += 50;

		// print days of week
		double blockWidth = width / 7;
		double blockHeight = (height-50) / 6;
		g2d.setFont(Constants.PRINTABLE);
		metrics = g2d.getFontMetrics();
		hgt = metrics.getHeight();
		
		for (int i = 0; i < 7; i++) {
			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, hgt + 10);
			g2d.draw(rect);
			if (i == 0) heading = "Sunday";
			else if (i == 1) heading = "Monday";
			else if (i == 2) heading = "Tuesday";
			else if (i == 3) heading = "Wednesday";
			else if (i == 4) heading = "Thursday";
			else if (i == 5) heading = "Friday";
			else heading = "Saturday";
			g2d.drawString(heading, (int)startx + 5, (int)starty + hgt);
			startx += blockWidth;
		}
		
		startx = Constants.PRINT_MARGINX;
		starty += hgt + 10;
		
		g2d.setFont(Constants.PRINTABLE_SMALL);
		metrics = g2d.getFontMetrics();
		hgt = metrics.getHeight();
		
		int prevDays = cal.get(Calendar.DAY_OF_WEEK) - 1; 
		int endDays = 42 - cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays;
		
		cal.roll(Calendar.MONTH, false);
		
		int rowCount = 0;
		
		for (int i = 1; i <= prevDays; i++) {

			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);

			startx += blockWidth;
			rowCount++;
		}
		
		cal.roll(Calendar.MONTH, true);
		
		// print the days in the month and the scheduled practitioners
		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			Calendar c= new GregorianCalendar();
			c.setTimeInMillis(cal.getTimeInMillis());
			c.set(Calendar.DATE, i);
			
			java.sql.Date date = new Date(c.getTimeInMillis());		

			// draws the square and the date
			rect = new Rectangle2D.Double ();
			rect.setRect(startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);
			int offset = 1;
			g2d.drawString("" + i, (int)startx + 5, (int)starty + offset * hgt);
			offset++;
			
			DayDto loadedDay = DataServiceImpl.GLOBAL_DATA_INSTANCE.getOrCreateDay(date);
			
			List<SchedulePractitionerDto> rooms= loadedDay.getRooms();
			
			for (SchedulePractitionerDto p : rooms) {
				
				PractitionerDto prac = p.getPractitioner();
				String last= prac.getLast();
				
				if (p.isFull()) {
					last+= " - FULL";
				}
								
				g2d.drawString(formatString(last, (int) blockWidth), (int)startx+5, (int)starty + offset * hgt);		
					
				offset++;
			}
			
			
			
			//SchedulePractitionerDto scheduled = new SchedulePractitionerDto();
			//scheduled.setField(SchedulePractitionerDto.DATE, loadedDay);
			
			
			
			/*
			if (loadedDay != null && scheduled.getAppointments() != null) {
				for (AppointmentDto appt : scheduled.getAppointments()) {
					
						
					PractitionerDto p = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPractitioner(appt.getPractSchedID());
					String s = p.getTypeName();
					//if (room.isFull()) s = "*" + s;
					g2d.drawString(formatString(s), (int)startx + 5, (int)starty + offset * hgt);
					offset++;
					
				}
			}
			*/
			
			
			startx += blockWidth;
			rowCount++;
			if (rowCount >= 7) {
				rowCount = 0;
				startx = Constants.PRINT_MARGINX;
				starty += blockHeight;
			}
		}
		
		cal.roll(Calendar.MONTH, true);
		
		for (int i = 1; i <= endDays; i++) {

			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);

			startx += blockWidth;
			rowCount++;
			if (rowCount >= 7) {
				rowCount = 0;
				startx = Constants.PRINT_MARGINX;
				starty += blockHeight;
			}
		}
		
		
		
		return g;
	}
	
	/** Prints the visible contents of the month panel of a given page.*/
	public int print(Graphics graphics, PageFormat pageFormat, int page) {
		pageFormat.setOrientation(PageFormat.LANDSCAPE);
		double width = pageFormat.getImageableWidth() - 2*Constants.PRINT_MARGINX;
		double height = pageFormat.getImageableHeight() - 2*Constants.PRINT_MARGINY;
		
		if (page > 0) {
			return NO_SUCH_PAGE;
		}
		
		graphics = buildPage(graphics, width, height, page);
		/* Print the entire visible contents of a java.awt.Frame */

		return PAGE_EXISTS;

	}

	/** Prints the month page. */
	public void actionPerformed(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();
		if (ok) {
			try {
				job.print();
			} catch (PrinterException ex) {
				/* The job did not successfully complete */
				JLabel errorMsg = new JLabel("The page did not print successfully.");
				errorMsg.setFont(Constants.DIALOG);
				JOptionPane.showMessageDialog(this, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

}