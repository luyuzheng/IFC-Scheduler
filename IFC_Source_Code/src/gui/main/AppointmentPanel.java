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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

    MainWindow main;

	/** Constructs the Appointment Panel given the Day Panel object. */
	public AppointmentPanel(DayPanel dp, MainWindow main) {
		super(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel panel = new JPanel(new BorderLayout());
                this.main = main;
		sidePanel = new SidePanel(dp);
		dp.setSidePanel(sidePanel);
		panel.add(sidePanel, BorderLayout.WEST);
		AppointmentSubpanel as = new AppointmentSubpanel(dp, main);

		day = dp.getDay();
		for (SchedulePractitionerDto r : DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day)){
			as.addRoom(r);
		}
		panel.add(as, BorderLayout.CENTER);
		setViewportView(panel);
	}
        
    public void refresh(DayPanel dp) {
		JPanel panel = new JPanel(new BorderLayout());
		sidePanel = new SidePanel(dp);
		dp.setSidePanel(sidePanel);
		panel.add(sidePanel, BorderLayout.WEST);
		AppointmentSubpanel as = new AppointmentSubpanel(dp, main);

		day = dp.getDay();
		for (SchedulePractitionerDto r : DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day)){
			as.addRoom(r);
		}
		panel.add(as, BorderLayout.CENTER);
		setViewportView(panel);
	}
	
	/** Truncates a string if longer than a specified length, to support readability. */
	private String[] formatString(String s, int lineLength, int numLines) {
		
		// string to return
		String[] print= new String[numLines+1];
		// what remains to be processed
		String leftover= s;
		
		int i= 0;
		
		while (leftover.length() > lineLength && i < numLines) {
			String line= leftover.substring(0, lineLength);
			System.out.println("line: " + line);
			
			int j= line.lastIndexOf(" ");
			if (j != -1) {
				print[i]= line.substring(0, j).trim();
				System.out.println("prints p[" + i + "]: " + print[i]);
				
				leftover= leftover.substring(j+1);
				System.out.println("leftover: " + leftover + "\n");
			} else {
				print[i]= line;
				leftover= leftover.substring(lineLength);
				System.out.println("prints p[" + i + "]: " + print[i]);
				System.out.println("leftover: " + leftover + "\n");
			}	
			
			i++;
			
		}
		
		// truncate text if longer than (numLines) lines
		if (i < numLines) {
			
			if (leftover.length() > lineLength) {
				print[i]= "...";
				System.out.println("truncates p[" + i + "]: " + print[i] + "\n");
			} else {
				print[i]= leftover;
				System.out.println("prints leftover p[" + i + "]: " + print[i] + "\n");
			}
		} else {
			int len= print[i-1].length();
			print[i-1]= print[i-1].substring(0, len-3) + "...";
		}
		
		/*
		if (leftover.length() > lineLength && i < numLines) {
			print[i]= "...";
			System.out.println("truncates p[" + i + "]: " + print[i] + "\n");
		} else {
			print[i]= leftover;
			System.out.println("prints leftover p[" + i + "]: " + print[i] + "\n");
		}
		*/
		
		return print;
	}

	/** Builds the page to be printed from information on the appointment panel. */
	private Graphics2D buildPage(Graphics g, double width, double height, int page) {
		System.out.println("start build");
		
		//# rooms (i.e., the total number of practitioners scheduled for the day)
		int r = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day).size();

		//create the graphics object
		Graphics2D g2d = (Graphics2D) g.create();
		
		//set the font, find the line height
		g2d.setFont(Constants.PRINTABLE);
		FontMetrics metrics = g2d.getFontMetrics(Constants.PRINTABLE);
		int hgt = metrics.getHeight();

		//top height is the height of the practitioner info box
		int pLines= 4;
		double topHeight = pLines*hgt + 8;
		//times width is the width of the left times panel
		double timesWidth = 45.0;
		
		//end time is the end # minutes, currTime is the current time
		int endTime = day.getEnd();
		int currTime = day.getStart();
		
		//current drawing point
		int startx = Constants.PRINT_MARGINX;
		int starty = Constants.PRINT_MARGINY;
		
		// Prints the date in the top left corner
		String[] dateSt = toDateArray(day.getDate());
		//g2d.drawString(dateSt[0], startx + 5, starty + hgt);
		g2d.drawString(dateSt[1], startx + 5, starty + hgt);
		
		starty = Constants.PRINT_MARGINY + (int)topHeight;
		
		//leftover is the number of minutes in the first box (if it ends oddly)
		int leftover = 60 - (currTime % 60);
		if (leftover > 0) {
			Rectangle2D.Double topTime = new Rectangle2D.Double ();
			topTime.setRect(startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
			g2d.draw(topTime);
			String timeSt = ((Integer) ((currTime / 60) % 12)).toString();
			String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
			g2d.drawString(timeSt + amPm, startx + 5, starty + hgt);
			//g2d.drawString(amPm, startx + 10, starty + 2*hgt);
			starty += leftover * Constants.PIXELS_PER_MINUTE;
			currTime+=leftover;
		}
		
		// Print hours down the side (e.g. "4pm", "5pm", etc.) 
		while (currTime + 60 < endTime) {
			Rectangle2D.Double timeBlock = new Rectangle2D.Double ();
			timeBlock.setRect(startx, starty, timesWidth, 60 * Constants.PIXELS_PER_MINUTE);
			g2d.draw(timeBlock);
			String timeSt = ((Integer) ((currTime / 60) % 12)).toString();
			String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
			g2d.drawString(timeSt + amPm, startx + 5, starty + hgt);
			//g2d.drawString(amPm, startx + 10, starty + 2*hgt);
			starty += 60 * Constants.PIXELS_PER_MINUTE;
			currTime+=60;
		}
		
		leftover = endTime - currTime;
		Rectangle2D.Double bottomTime = new Rectangle2D.Double ();
		bottomTime.setRect (startx, starty, timesWidth, leftover * Constants.PIXELS_PER_MINUTE);
		g2d.draw(bottomTime);
		String timeSt = ((Integer) ((currTime / 60) % 12)).toString();
		String amPm = (((currTime / 60) % 12) == 0) ? "am" : "pm"; 
		g2d.drawString(timeSt, startx + 5, starty + hgt);
		g2d.drawString(amPm, startx + 10, starty + 2*hgt);
		
		width= width - timesWidth + Constants.PRINT_MARGINX;
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

			// Prints header with practitioner information
			PractitionerDto p = day.getRooms().get(page*3 + j).getPractitioner();
			
			String name= p.getFirst().toString() + " " + p.getLast().toString();
			g2d.drawString(formatString(name, lineLength, 1)[0], startx+5, starty+hgt);
			
			String type= p.getType().toString();
			g2d.drawString(formatString(type, lineLength, 1)[0], startx+5, starty+2*hgt);
			
			if (!p.getNotes().isEmpty()) {
				String[] notes= formatString("Notes: " + p.getNotes().replaceAll("\t\t", "\n"), lineLength, pLines-2);
				
				for (int k=0; k<notes.length; k++) {
					if (notes[k] != null) {
						g2d.drawString(notes[k], startx+5, starty+(3+k)*hgt);
					}
				}
			}
			
			
			ArrayList<AppointmentDto> appts = day.getRooms().get(page*3 + j).getAppointments();
			starty += topHeight;

			// Prints the text for an appointment block
			for (AppointmentDto a : appts) {
				int min = a.getEnd() - a.getStart();
				int blockHeight = min*Constants.PIXELS_PER_MINUTE;
				int aLines= blockHeight/hgt;
				Rectangle2D.Double apptBlock = new Rectangle2D.Double ();
				apptBlock.setRect (startx, starty, colWidth, blockHeight);
				g2d.draw(apptBlock);

				String line1 = formatString(a.prettyPrintStart() + " - " + a.prettyPrintEnd(), lineLength, 1)[0];
				String line2 = "";
				String[] line3= new String[5];
				String[] line4= new String[5];
				if (a.getPatientID() != null) {
                                        
					PatientDto pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(a.getPatientID());
					
					if (a.getConfirmation()) {
						line2="-- CONFIRMED --";
					}
					
					if (pat.getPhone() == null) {
						line3= formatString(pat.getFullName() + " - No phone #", lineLength, 2);
					} else {
						line3 = formatString(pat.getFullName() + " - " + pat.getPhone(), lineLength, 2);
					}			

					
					if (!a.getNote().isEmpty()) {
						line4 = formatString("Notes: " + a.getNote().replaceAll("\t\t", "\n"), lineLength, aLines-4);
					}
					
				}
				
				g2d.drawString(line1, startx+5, starty+hgt);
				g2d.drawString(line2, startx+5, starty+2*hgt);
				
				int k=0;
				int lin=0;
				while (k<line3.length) {

					if (line3[k] !=null) {
						
						g2d.drawString(line3[k], startx+5, starty+(3+k)*hgt);
						lin++;
					}
					
					k++;
				}
				
				k= lin;
				while(k<line4.length) {
					if (line4[k] != null) {
						
						g2d.drawString(line4[k], startx+5, starty+(3+k)*hgt);
						
						System.out.println("processing patient notes...");
					}
					
					k++;
				}


				starty += blockHeight;
			}
		}
		
		System.out.println("end build");

		return g2d;
	}

	/** Returns a string representation of a given date. */
	private String[] toDateArray(Date date) {
		String full = date.toString();
		String[] dateArray = new String[2];
		dateArray[0] = full.substring(0, 4);
		dateArray[1] = full.substring(5,full.length());
		return dateArray;
	}

	/** Prints the visible contents of the appointment pane of a given page. */
	public int print(Graphics graphics, PageFormat pageFormat, int page) {
		System.out.println("print the pages");
		
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
		System.out.println("job received");
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
