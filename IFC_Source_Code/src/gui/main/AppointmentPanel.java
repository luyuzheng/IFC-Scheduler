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
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import data.Appointment;
import data.Constants;
import data.Day;
import data.Practitioner;
import data.Room;
import data.Time;

public class AppointmentPanel extends JScrollPane implements Printable, ActionListener {
	Day day;
	
	ArrayList<Graphics2D> pages;

	public AppointmentPanel(DayPanel dp) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new SidePanel(dp), BorderLayout.WEST);
		AppointmentSubpanel as = new AppointmentSubpanel(dp);

		day = dp.getDay();
		for (Room r : day.getRooms()) 
			as.addRoom(r);
		panel.add(as, BorderLayout.CENTER);
		setViewportView(panel);
	}

	private String formatString(String s, int lineLength) {
		if (s.length() > lineLength) return s.substring(0, lineLength-3) + "...";
		else return s;
	}

	private String getPracInfo(Practitioner p, int lineLength) {
		return formatString(p.getName() + " - " + p.getType().toString(), lineLength);
	}

	private Graphics2D buildPage(Graphics g, double width, double height, int page) {
		
		//# rooms
		int r = day.getRooms().size();

		//set the font for the page
		Font font = new Font("monospaced", Font.PLAIN, 8);

		//create the graphics object
		Graphics2D g2d = (Graphics2D) g.create();
		
		//set the font, find the line height
		g2d.setFont(font);
		FontMetrics metrics = g2d.getFontMetrics(font);
		int hgt = metrics.getHeight();

		//top height is the height of the prac info box
		double topHeight = 2*hgt + 8;
		//times width is the width of the left times panel
		double timesWidth = 30.0;
		
		//end time is the end # minutes, currTime is the current time
		int endTime = day.getTimeSlot().getEndTime().timeInMinutes();
		Time currTime = day.getTimeSlot().getStartTime();
		
		//current drawing point
		int startx = Constants.PRINT_MARGINX;
		int starty = Constants.PRINT_MARGINY;
		
		String[] dateSt = day.getDate().toPrintString();
		g2d.drawString(dateSt[0], startx + 5, starty + hgt);
		g2d.drawString(dateSt[1], startx + 5, starty + 2*hgt);
		
		starty = Constants.PRINT_MARGINY + (int)topHeight;
		
		//leftover is the number of minutes in the first box (if it ends oddly)
		int leftover = 60 - (currTime.timeInMinutes() % 60);
		if (leftover > 0) {
			Rectangle2D.Double topTime = new Rectangle2D.Double ();
			topTime.setRect (startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
			g2d.draw(topTime);
			String[] timeSt = currTime.toPrintString();
			g2d.drawString(timeSt[0], startx + 5, starty + hgt);
			g2d.drawString(timeSt[1], startx + 10, starty + 2*hgt);
			starty += leftover * Constants.PIXELS_PER_MINUTE;
			currTime = currTime.addMinutes(leftover);
		}
		
		while (currTime.timeInMinutes() + 60 < endTime) {
			Rectangle2D.Double timeBlock = new Rectangle2D.Double ();
			timeBlock.setRect (startx, starty, timesWidth, 60 * Constants.PIXELS_PER_MINUTE);
			g2d.draw(timeBlock);
			String[] timeSt = currTime.toPrintString();
			g2d.drawString(timeSt[0], startx + 5, starty + hgt);
			g2d.drawString(timeSt[1], startx + 10, starty + 2*hgt);
			starty += 60 * Constants.PIXELS_PER_MINUTE;
			currTime = currTime.addMinutes(60);
		}
		
		leftover = endTime - currTime.timeInMinutes();
		Rectangle2D.Double bottomTime = new Rectangle2D.Double ();
		bottomTime.setRect (startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
		g2d.draw(bottomTime);
		String[] timeSt = currTime.toPrintString();
		g2d.drawString(timeSt[0], startx + 5, starty + hgt);
		g2d.drawString(timeSt[1], startx + 10, starty + 2*hgt);
		
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

		for (int j = 0; j < roomsLeft; j++) {
			Rectangle2D.Double rect = new Rectangle2D.Double ();
			startx = Constants.PRINT_MARGINX + (int)timesWidth + (int)colWidth*j;
			starty = Constants.PRINT_MARGINY;
			rect.setRect (startx, starty, colWidth, topHeight);
			g2d.draw(rect);

			Practitioner p = day.getRooms().get(page*3 + j).getPractitioner();
			g2d.drawString(getPracInfo(p, lineLength), startx+5, starty+hgt);
			g2d.drawString(formatString(p.getNote().replaceAll("\t\t", "\n"), lineLength), startx+5, starty+2*hgt);
			ArrayList<Appointment> appts = day.getRooms().get(page*3 + j).getAppointments();

			starty += topHeight;

			for (Appointment a : appts) {
				int min = a.getTimeSlot().lengthInMinutes();
				int blockHeight = min*Constants.PIXELS_PER_MINUTE;
				Rectangle2D.Double apptBlock = new Rectangle2D.Double ();
				apptBlock.setRect (startx, starty, colWidth, blockHeight);
				g2d.draw(apptBlock);

				String line1 = a.getTimeSlot().toString();
				String line2 = "";
				if (a.isFilled()) {
					if (!a.getPatient().getNote().equals("")) line1 += "   " + a.getPatient().getNote().replaceAll("\t\t", "\n");
					line2 = a.getPatient().getFullName() + " - " + a.getPatient().getNumberString();
					if (line2.length() > lineLength) {
						line2 = a.getPatient().getFirstName().substring(0,1) + ". " + a.getPatient().getLastName() + " - " + a.getPatient().getNumberString();
					}
					if (line2.length() > lineLength) { 
						line2 = formatString(a.getPatient().getNumberString() + " - " + a.getPatient().getFullName(), lineLength);
					}
				}
				g2d.drawString(formatString(line1, lineLength), startx+5, starty+hgt);
				g2d.drawString(line2, startx+5, starty+2*hgt);

				starty += blockHeight;
			}
		}


		return g2d;
	}

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
