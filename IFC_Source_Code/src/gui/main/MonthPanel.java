/**
 * Displays the entire monthly view. 
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

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import backend.DataTransferObjects.DayDto;

import gui.Constants;
import java.util.Date;
import data.Day;
import data.DayLoader;
import data.Practitioner;

public class MonthPanel extends JScrollPane implements Printable, ActionListener {
	DayDto day;
	Date d;
	
	public MonthPanel(DayPanel dp) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel panel = new JPanel(new GridLayout(6,7));

		DayDto day = dp.getDay();
		this.d = day.getDate();
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, d.getMonth() - 1);
		cal.set(Calendar.YEAR, d.getYear());
		
		String month = "December";
		if (d.getMonth() == 1) month = "January";
		else if (d.getMonth() == 2) month = "February";
		else if (d.getMonth() == 3) month = "March";
		else if (d.getMonth() == 4) month = "April";
		else if (d.getMonth() == 5) month = "May";
		else if (d.getMonth() == 6) month = "June";
		else if (d.getMonth() == 7) month = "July";
		else if (d.getMonth() == 8) month = "August";
		else if (d.getMonth() == 9) month = "September";
		else if (d.getMonth() == 10) month = "October";
		else if (d.getMonth() == 11) month = "November";
		
		month = month + " " + d.getYear();
		
		//prevDays is the number of boxes in the upper left, before the first of the month, needed since the 
		//calendar is going to be a 6x7 set of boxes. Calendar.SUNDAY is 1 and so forth, so we use day of week - 1
		int prevDays = cal.get(Calendar.DAY_OF_WEEK) - 1; 
		int endDays = 42 - cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays;
		
		cal.roll(Calendar.MONTH, false);
		
		for (int i = 1; i <= prevDays; i++) {
			Date date = new Date(cal.get(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays + i, cal.get(Calendar.YEAR));
			panel.add(new DayBlock(date, Color.LIGHT_GRAY));
		}
		
		cal.roll(Calendar.MONTH, true);
		
		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			Date date = new Date(d.getMonth(), i, d.getYear());
			panel.add(new DayBlock(date));
		}
		
		cal.roll(Calendar.MONTH, true);
		
		for (int i = 1; i <= endDays; i++) {
			Date date = new Date(cal.get(Calendar.MONTH) + 1, i, cal.get(Calendar.YEAR));
			panel.add(new DayBlock(date, Color.LIGHT_GRAY));
		}
		
		MonthHeadingPanel mhp = new MonthHeadingPanel(month);
		JPanel main = new JPanel(new BorderLayout());
		main.add(mhp, BorderLayout.NORTH);
		
		main.add(panel, BorderLayout.CENTER);
		
		setViewportView(main);
	}
	
	private String formatString(String s) {
		if (s.length() > 13) return s.substring(0,11) + "..."; 
		else return s;
	}
	
	private Graphics buildPage(Graphics g, double width, double height, int page) {
		
		Font bigFont = new Font("Monospaced", Font.BOLD, 20);
		Font smallFont = new Font("Monospaced", Font.PLAIN, 9);
		Font medFont = new Font("Monospaced", Font.PLAIN, 14);
		
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D.Double rect = new Rectangle2D.Double ();
		double startx = Constants.PRINT_MARGINX;
		double starty = Constants.PRINT_MARGINY;
		rect.setRect (startx, starty, width, 50);
		g2d.draw(rect);
		g2d.setFont(bigFont);
		String heading;
		int month = d.getMonth();
		if (month == 1) heading = "January " + d.getYear();
		else if (month == 2) heading = "February " + d.getYear();
		else if (month == 3) heading = "March " + d.getYear();
		else if (month == 4) heading = "April " + d.getYear();
		else if (month == 5) heading = "May " + d.getYear();
		else if (month == 6) heading = "June " + d.getYear();
		else if (month == 7) heading = "July " + d.getYear();
		else if (month == 8) heading = "August " + d.getYear();
		else if (month == 9) heading = "September " + d.getYear();
		else if (month == 10) heading = "October " + d.getYear();
		else if (month == 11) heading = "November " + d.getYear();
		else heading = "December " + d.getYear();
		
		FontMetrics f = g2d.getFontMetrics();
		int offsetx = (int)(width / 2.0 - f.charWidth(' ') * (heading.length() / 2.0));
		g2d.drawString(heading, (int)startx + offsetx, (int)starty + f.getHeight());
		
		starty += 50;
		
		double blockWidth = width / 7;
		double blockHeight = (height-100) / 6;
		g2d.setFont(medFont);
		f = g2d.getFontMetrics();
		
		for (int i = 0; i < 7; i++) {
			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, 50);
			g2d.draw(rect);
			if (i == 0) heading = "Sunday";
			else if (i == 1) heading = "Monday";
			else if (i == 2) heading = "Tuesday";
			else if (i == 3) heading = "Wednesday";
			else if (i == 4) heading = "Thursday";
			else if (i == 5) heading = "Friday";
			else heading = "Saturday";
			g2d.drawString(heading, (int)startx + 5, (int)starty + f.getHeight());
			startx += blockWidth;
		}
		
		startx = Constants.PRINT_MARGINX;
		starty += 50;
		
		g2d.setFont(smallFont);
		f = g2d.getFontMetrics();
		
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, d.getMonth() - 1);
		cal.set(Calendar.YEAR, d.getYear());
		
		
		int prevDays = cal.get(Calendar.DAY_OF_WEEK) - 1; 
		int endDays = 42 - cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays;
		
		cal.roll(Calendar.MONTH, false);
		
		int rowCount = 0;
		
		for (int i = 1; i <= prevDays; i++) {
			//Date date = new Date(cal.get(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH) - prevDays + i, cal.get(Calendar.YEAR));
			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);
			//g2d.drawString(date.toFormalString(), (int)startx + 5, (int)starty + f.getHeight());
			startx += blockWidth;
			rowCount++;
		}
		
		cal.roll(Calendar.MONTH, true);
		
		DayLoader dl = new DayLoader();
		
		for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
			Date date = new Date(d.getMonth(), i, d.getYear());
			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);
			int offset = 1;
			g2d.drawString(date.toFormalString(), (int)startx + 5, (int)starty + offset * f.getHeight());
			offset++;
			Day loadedDay = dl.loadDay(date);
			if (loadedDay != null && loadedDay.getRooms() != null) {
				for (Room r : loadedDay.getRooms()) {
					if (r.hasPrac()) {
						Practitioner p = r.getPractitioner();
						String s = p.getType().toString();
						if (r.isFull()) s = "*" + s;
						g2d.drawString(formatString(s), (int)startx + 5, (int)starty + offset * f.getHeight());
						offset++;
					}
				}
			}
			
			
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
			//Date date = new Date(cal.get(Calendar.MONTH) + 1, i, cal.get(Calendar.YEAR));
			rect = new Rectangle2D.Double ();
			rect.setRect (startx, starty, blockWidth, blockHeight);
			g2d.draw(rect);
			//g2d.drawString(date.toFormalString(), (int)startx + 5, (int)starty + f.getHeight());
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
	
	public int print(Graphics graphics, PageFormat pageFormat, int page) {
		
		double width = pageFormat.getImageableWidth() - 2*Constants.PRINT_MARGINX;
		double height = pageFormat.getImageableHeight() - 2*Constants.PRINT_MARGINY;
		
		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		graphics = buildPage(graphics, width, height, page);
		/* Print the entire visible contents of a java.awt.Frame */

		return PAGE_EXISTS;

	}

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