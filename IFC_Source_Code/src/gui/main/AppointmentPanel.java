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
    
    private ArrayList<Page> pageData;
   

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
			//System.out.println("line: " + line);
			
			int j= line.lastIndexOf(" ");
			if (j != -1) {
				print[i]= line.substring(0, j).trim();
				//System.out.println("prints p[" + i + "]: " + print[i]);
				
				leftover= leftover.substring(j+1);
				//System.out.println("leftover: " + leftover + "\n");
			} else {
				print[i]= line;
				leftover= leftover.substring(lineLength);
				//System.out.println("prints p[" + i + "]: " + print[i]);
				//System.out.println("leftover: " + leftover + "\n");
			}	
			
			i++;
			
		}
		
		// truncate text if longer than (numLines) lines
		if (i < numLines) {
			if (leftover.length() > lineLength) {
				print[i]= "...";
				//System.out.println("truncates p[" + i + "]: " + print[i] + "\n");
			} else {
				print[i]= leftover;
				//System.out.println("prints leftover p[" + i + "]: " + print[i] + "\n");
			}
		} else {
			if (numLines > 0) { // A check to make sure the block can actually fit a note
				int len= print[i-1].length();
				print[i-1]= print[i-1].substring(0, len-3) + "...";
			}
		}
		
		return print;
	}

	
	/** Builds the page to be printed from information on the appointment panel. */
	private Graphics2D buildPage(Graphics g, double width, double height, int page) {
		//System.out.println("start build");		
		
		// Number of rooms (i.e., the total number of practitioners scheduled for the day)
		int r = DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(day).size();
		
		// Create the graphics object
		Graphics2D g2d = (Graphics2D) g.create();
		
		// Set the font, find the line height
		g2d.setFont(Constants.PRINTABLE);
		FontMetrics metrics = g2d.getFontMetrics(Constants.PRINTABLE);
		int hgt = metrics.getHeight();

		// Top height is the height of the practitioner info box
		int pLines= 3; 
		double topHeight = pLines*hgt + 8;
		
		// Date width is the width of the margin that contains the date
		double dateWidth = 35.0; 
		
		// Current drawing point
		int startx = Constants.PRINT_MARGINX;
		int starty = Constants.PRINT_MARGINY;
		
		// Prints the date in the top left corner
		String[] dateSt = toDateArray(day.getDate());
		g2d.drawString(dateSt[1], startx + 5, starty + hgt);
		
		// Redefine the width to be the space left to print the appointments
		width = width - dateWidth + Constants.PRINT_MARGINX;
		
		// Print the page number in the bottom left corner
		g2d.drawString("" + (page + 1), (int)(width / 2.0) + (int)dateWidth + Constants.PRINT_MARGINX, 
						(int)height + Constants.PRINT_MARGINY + hgt);
		
		// Determine width of each column, the length of each line of notes, 
		// and the number of rooms to print on the page
		double colWidth;
		int lineLength = 125; 
		int roomsLeft = r - pageData.get(page).getPageCol() * 2;
		if (roomsLeft == 1) {
			colWidth = width;
		} else {
			colWidth = width / 2.0;
			lineLength = 60;
			roomsLeft = 2;
		}	
		
		// Print the appointments
		for (int j = 0; j < roomsLeft; j++) {
			startx = Constants.PRINT_MARGINX + (int)dateWidth + (int)colWidth*j;
			// Prevent the printer from starting a new column if it doesn't fit on that page
			// (Need to add the margins to the width because startx begins its calculations
			// from the edge of the page, but width does not)
			if (width + Constants.PRINT_MARGINX + (int)dateWidth >= startx + colWidth) {
			
				// Draw the rectangle where the practitioner information will be
				starty = Constants.PRINT_MARGINY;
				Rectangle2D.Double rect = new Rectangle2D.Double();
				rect.setRect (startx, starty, colWidth, topHeight);
				g2d.draw(rect);
	
				// Print the header with practitioner information
				PractitionerDto p = day.getRooms().get(pageData.get(page).getPageCol() * 2 + j).getPractitioner();
				
				String name= p.getFirst().toString() + " " + p.getLast().toString();
				g2d.drawString(formatString(name, lineLength, 1)[0], startx+5, starty+hgt);
				
				String type= p.getType().toString();
				g2d.drawString(formatString(type, lineLength, 1)[0], startx+5, starty+2*hgt);
				
				if (!p.getNotes().isEmpty()) {
					String[] notes= formatString(p.getNotes().replaceAll("\t\t", "\n"), lineLength, pLines-2);
					
					for (int k=0; k<notes.length; k++) {
						if (notes[k] != null) {
							g2d.drawString(notes[k], startx+5, starty+(3+k)*hgt);
						}
					}
				}

				// Get the appointments to be printed on the page
				ArrayList<AppointmentDto> appts = day.getRooms().get(pageData.get(page).getPageCol() * 2 + j).getAppointments();
				int i = pageData.get(page).getApptIndex(j);
				boolean donePrintingRoom = false;
				
				starty += topHeight;
				
				// Print the the text for an appointment block
				while (i < appts.size() && !donePrintingRoom) {
					// Draw the rectangle that contains the appointment information
					int min = appts.get(i).getEnd() - appts.get(i).getStart();
					double blockHeight = min*Constants.PRINT_PIXELS_PER_MINUTE;
					Rectangle2D.Double apptBlock = new Rectangle2D.Double();
					apptBlock.setRect (startx, starty, colWidth, blockHeight);
					g2d.draw(apptBlock);
	
					// Precompute which lines show the patient name, appointment confirmation, phone number, and note
					int aLines= (int) Math.ceil(blockHeight/hgt);
					String line1 = formatString(appts.get(i).prettyPrintStart() + " - " + appts.get(i).prettyPrintEnd(), lineLength, 1)[0];
					String[] line2= new String[5];
					String[] line3= new String[5];
					if (appts.get(i).getPatientID() != null) {
	                                        
						PatientDto pat = DataServiceImpl.GLOBAL_DATA_INSTANCE.getPatient(appts.get(i).getPatientID());
						
						if (appts.get(i).getConfirmation()) {
							line1 += "  CONFIRMED";
						}
						
						if (pat.getPhone() == null) {
							line2= formatString(pat.getFullName() + " - No phone #", lineLength, 2);
						} else {
							line2 = formatString(pat.getFullName() + " - " + pat.getPhone(), lineLength, 2);
						}			
						
						if (!appts.get(i).getNote().isEmpty()) {
							line3 = formatString(appts.get(i).getNote().replaceAll("\t\t", "\n"), lineLength, aLines-4);
						}
					}
					
					// Print the patient name, phone number, and note
					g2d.drawString(line1, startx+5, starty+hgt);
					
					for (int k = 0; k < line2.length; k++) {
						if (line2[k] != null) {
							g2d.drawString(line2[k], startx+5, starty+(2+k)*hgt);
						}
					}
					
					for (int k = 0; k < line3.length; k++) {
						if (line3[k] != null) {
							g2d.drawString(line3[k], startx+5, starty+(3+k)*hgt);
						}
					}
	
					// Move pointer to next appointment location
					starty += blockHeight;
					
					// Stop printing on this page if no more appointments fit
					if (height + Constants.PRINT_MARGINY <= starty + blockHeight) {
						donePrintingRoom = true;
					}
					
					// Move to next appointment
					i++;
				}
			}
		}
		
		//System.out.println("end build");

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
	
	/**
	 * Calculates the number of pages needed to print a room's schedule and 
	 * determines the number of appointments that can be printed on a page.
	 * @param roomNumber - an index used to access the appointments of a room on a given day
	 * @param apptHeight - the amount of space available to print only appointments 
	 * 					   (excludes the practitioner headers)
	 * @return a two-element array, where
	 * 			data[0] = the number of pages, and 
	 * 			data[1] = the number of appointments per page
	 */
	private int[] numberOfPagesPerRoom(int roomNumber, double apptHeight) {
		ArrayList<AppointmentDto> appts = day.getRooms().get(roomNumber).getAppointments();
		int numAppts = appts.size();
		int[] data = {0, 0};
		
		if (numAppts > 0) {
			int numMins = appts.get(0).getEnd() - appts.get(0).getStart();
			double blockHeight = numMins*Constants.PRINT_PIXELS_PER_MINUTE;
			
			int apptsPerPage = (int)Math.floor(apptHeight / blockHeight);
			int numPages = (int)Math.ceil((double)numAppts / apptsPerPage);
			
			data[0] = numPages;
			data[1] = apptsPerPage;
			
			//System.out.println("room number: " + roomNumber + " num pages: " + data[0] + " appts per page: " + data[1]);
			
			return data;
		}
		
		return data; // there were no appointments for that room
	}
	
	/** 
	 * Calculates the information for all pages to be printed, including the row number, column number, and 
	 * appointment indexes for a page. See the Page class for more details.
	 * @param pageHeight - the total height for printing on a page (excluding the top and bottom margins)
	 * @param pracHeaderHeight - the height of the blocks containing practitioner information
	 * @return an array of Pages that tell which appointments should be printed on a given page
	 */
	private ArrayList<Page> calculatePageData(double pageHeight, double pracHeaderHeight) {
		int numRooms = day.getRooms().size();
		double apptHeight = pageHeight - pracHeaderHeight; // do not include height of practitioner header in page calculations
		ArrayList<Page> pageData = new ArrayList<Page>();
		int[] pagesForRoom1 = new int[2];
		int[] pagesForRoom2 = new int[2];
		int numPagesPerTwoRooms = 0;
		int col = 0;
		
		// Calculate the page data for all rooms
		for (int i = 0; i < numRooms; i = i + 2) {
			// Determine the number of pages per two rooms
			pagesForRoom1 = numberOfPagesPerRoom(i, apptHeight);
			if (numRooms > i + 1) {
				pagesForRoom2 = numberOfPagesPerRoom(i + 1, apptHeight);
			} else {
				pagesForRoom2[0] = 0;
				pagesForRoom2[1] = 0;
			}
			numPagesPerTwoRooms = Math.max(pagesForRoom1[0], pagesForRoom2[0]);
			
			// Set the page data
			for (int row = 0; row < numPagesPerTwoRooms; row++) {
				int[] apptIndexes = {row * pagesForRoom1[1], row * pagesForRoom2[1]};
				pageData.add(new Page(row, col, apptIndexes));
				
				//System.out.println("row: " + row + " col: " + col + " apptIndex0: " + apptIndexes[0] + " apptIndex1: " + apptIndexes[1]);
			}
			col++;
		}
		
		return pageData;
	}
	
	/** Prints the visible contents of the appointment pane of a given page. */
	public int print(Graphics graphics, PageFormat pageFormat, int page) throws PrinterException {
		//System.out.println("print the pages");
		
		double width = pageFormat.getImageableWidth() - 2*Constants.PRINT_MARGINX;
		double height = pageFormat.getImageableHeight() - 2*Constants.PRINT_MARGINY;
		
		// Create the graphics object
		Graphics2D g2d = (Graphics2D) graphics.create();
		
		// Set the font, find the line height
		g2d.setFont(Constants.PRINTABLE);
		FontMetrics metrics = g2d.getFontMetrics(Constants.PRINTABLE);
		int hgt = metrics.getHeight();
		
		// Calculate the page height and practitioner header height
		int pLines= 3; 
		double pracHeaderHeight = pLines*hgt + 8;
		double pageHeight = pageFormat.getImageableHeight() - 2*Constants.PRINT_MARGINY;
		
		pageData = calculatePageData(pageHeight, pracHeaderHeight);
		int pages = pageData.size();
		
		if (pageData.size() <= 0) {
			JLabel errorMsg = new JLabel("There are no appointments to print for this day!");
			errorMsg.setFont(Constants.DIALOG);
			JOptionPane.showMessageDialog(this, errorMsg, "Error!", JOptionPane.ERROR_MESSAGE);
			throw new PrinterException();
		}

		//System.out.println("Total number of pages: " + pages);
		
		if (page >= pages) {
			return NO_SUCH_PAGE;
		}

		// Print the entire visible contents of a java.awt.Frame
		graphics = buildPage(graphics, width, height, page);

		return PAGE_EXISTS;

	}
	
	
	/** Sets the view as printable and prints it. */
	public void actionPerformed(ActionEvent e) {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		boolean ok = job.printDialog();
		//System.out.println("job received");
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
	
	/* Below is an example structure of how the printing works:
	 *	  	  col 0       col 1
	 * 		 _______	 ___ ___
	 * 		|	|	|	|	|	|
	 * row 0| 0 | 0 |	| 0	| 0	|
	 * 		|___|___|	|___|___|
	 * 		|	|	|	|	|	|
	 * row 1| 3 | 16|	| 12| 0	|
	 * 		|___|___|	|___|___|
	 * 		|	|	|	  p2 
	 * row 2| 6 | 32|
	 * 		|___|___|
	 * 	 	 p0  p1
	 * 
	 * Assume the following is true about the three practitioners, p0 to p2:
	 * p0 has 7 45-minute appointments -  3 appointments fit per page.
	 * p1 has 20 15-minute appointments - 16 appointments fit per page.
	 * p2 has 24 20-minute appointments - 12 appointments fit per page.
	 * 
	 * In the example above, this is how the pages are indexed:
	 * Page 0 = row 0, col 0
	 * Page 1 = row 1, col 0
	 * Page 2 = row 2, col 0
	 * Page 3 = row 0, col 1
	 * Page 4 is row 1, col 1
	 * 
	 * Column 0 will print practitioners p0 and p1.
	 * Column 1 will print practitioner p2.
	 * 
	 * Inside each cell is an index that says which appointment to start printing 
	 * at the top of the page. For example, for p0, page 0 will contain appts 0-2, 
	 * page 1 contains appts 3-5, and page 2 contains appt 6. 
	 * 
	 * For p1, since there is nothing else to print after the 20th appt, the index
	 * will still increment (for ease of calculation), but buildPage knows not to print 
	 * nonexistent appts (i.e. not to print appts 21-47).
	 * 
	 * Printing p2 works in a similar way, as p0 where appts 0-11 print on page 3 
	 * and appts 12-23 print on page 4.
	 * 
	 * The last column (where practitioner p3 might have been) is empty, so the indexes 
	 * are set to 0 as placeholders. 
	 * 
	 * Note that the appointments for p2 will visibly stretch across the entire page 
	 * since there is no fourth practitioner.
	 */
	private class Page {
		private int pageRow;
		private int pageCol;
		private int[] apptIndexes = new int[2]; // the indexes of the first appointments that can fit on this page for both practitioners
		
		public Page(int row, int col, int[] appts) {
			this.pageRow = row;
			this.pageCol = col;
			this.apptIndexes = appts;
		}
		
		public int getPageRow() {
			return pageRow;
		}
		
		public int getPageCol() {
			return pageCol;
		}
		
		public int[] getApptIndexes() {
			return apptIndexes;
		}
		
		public int getApptIndex(int index) {
			return apptIndexes[index];
		}
		
		public void setPageRow(int row) {
			pageRow = row;
		}
		
		public void setPageCol(int col) {
			pageCol = col;
		}
		
		public void setApptIndexes(int arrayIndex, int apptIndex) {
			apptIndexes[arrayIndex] = apptIndex;
		}
	}
}
