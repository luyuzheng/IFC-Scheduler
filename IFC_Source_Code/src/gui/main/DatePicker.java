/**
 * This class determines behaviors of the small calendar. The small calendar 
 * is the interface that allows users to navigate between days and months.
 */

package gui.main;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JPanel;

import backend.DataService.DataServiceImpl;

import java.sql.Date;

@SuppressWarnings("serial")
public class DatePicker extends JPanel {
	
	/** The date that is currently selected on the date picker. */
	private TinyDayBlock selectedBlock;
	
	/** The current month shown in the date picker. */
	private MonthView currentMonth;
	
	/** The application window. */
	private MainWindow mainWindow;
	
	/** Today's date. */
	private Date currentDate;
	
	/** Constructs the date picker within the main window. */
	public DatePicker(MainWindow window) {
		this.mainWindow = window;
		setMaximumSize(new Dimension(200, 500));
		setLayout(new GridLayout(0,1));
		
		// Get today's date
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		currentDate = new Date(cal.getTimeInMillis());
		
		// Initialize the selectedBlock to the current date
		selectedBlock = new TinyDayBlock(this, DataServiceImpl.GLOBAL_DATA_INSTANCE.getOrCreateDay(currentDate).getDate(), false);
		
		// Initialize the currentMonth and add it to the GUI
		currentMonth = new MonthView(this, currentDate, MonthView.CURRENT_MONTH);	
		currentMonth.selectDay(cal.get(Calendar.DATE));
		add(currentMonth);
	}
	
	/** Returns today's date as a Date object. */
	public Date getDate() {
		return currentDate;
	}
	
	/** Sets the current day as selected in the date picker. */
	public void registerCurrentDay(TinyDayBlock b) {
		selectedBlock = b;
	}
	
	/** Changes the currently selected day on the date picker.*/
	public void setDate(Date d) {
		
		// Removes the selected date from the tiny month calendar
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(selectedBlock.getDate());
		currentMonth.deselectDay(cal.get(Calendar.DATE));
		
		// Changes the month view to the month of the selected day
		currentMonth = new MonthView(this, d, MonthView.CURRENT_MONTH);
		
		// Reset calendar to the selected date
		cal.setTime(d);
		currentMonth.selectDay(cal.get(Calendar.DATE));
		selectedBlock = new TinyDayBlock(this, d, Color.LIGHT_GRAY, false);
		
		// Changes main window GUI to reflect the info for the new date
		removeAll();
		add(currentMonth);
		repaint();
		validate();
		mainWindow.setDate(d);
	}
	
	/** Advances the date picker by one month. */
	public void nextMonth() {
			
		// Adds one month to the current date
		GregorianCalendar focused= new GregorianCalendar();
		focused.setTime(currentMonth.getDate());
		focused.add(Calendar.MONTH, 1);
		
		// Sets the calendar to the new date
		Date newDate = new Date(focused.getTime().getTime());
		setDate(newDate);
	}
	
	/** Goes back one month on the date picker. */
	public void prevMonth() {
		
		// Subtracts one month from the current date
		GregorianCalendar focused= new GregorianCalendar();
		focused.setTime(currentMonth.getDate());
		focused.add(Calendar.MONTH, -1);

		// Sets the calendar to the new date
		Date newDate = new Date(focused.getTime().getTime());
		setDate(newDate);
	}
	
	/**
	 * Determines the behavior of clicking on a day in the month view.
	 */
	public void reportFocusGained(TinyDayBlock b) {		
		if (mainWindow.inMonthView()) {
			mainWindow.switchView();
		}
		setDate(b.getDate());
	}
}