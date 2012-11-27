/**
 * This class reports behaviors of what happens in the small calendar. The small calendar is the interface that allows
 * users to navigate between days and months.
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
	private MonthView curr;
	/** The application window. */
	private MainWindow mainWindow;
	/** Today's date. */
	private Date currentDate;
	
	/** Constructs the date picker within the main window. */
	public DatePicker(MainWindow window) {
		this.mainWindow = window;
		setMaximumSize(new Dimension(200, 500));
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		
		currentDate = new Date(cal.getTimeInMillis());
		curr = new MonthView(this, currentDate, MonthView.CURRENT_MONTH);		

		//reportFocusGained(selectedBlock);
		selectedBlock = new TinyDayBlock(this, DataServiceImpl.GLOBAL_DATA_INSTANCE.getOrCreateDay(currentDate).getDate(), false);
		
		
		//Date focused = selectedBlock.getDate();
		//cal.setTime(focused);
		curr.selectDay(cal.get(Calendar.DATE));
		
		setLayout(new GridLayout(0,1));
		add(curr);
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
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(selectedBlock.getDate());
		//System.out.print(d);
		//System.out.println(" setDate " + selectedBlock.getDate().toString());
		curr.deselectDay(cal.get(Calendar.DATE));
		curr = new MonthView(this, d, MonthView.CURRENT_MONTH);
		
		removeAll();
		add(curr);
		repaint();
		validate();
		//revalidate();
		
		curr.selectDay(cal.get(Calendar.DATE));
		mainWindow.setDate(d);
	}
	
	/** Advances the date picker one month. */
	public void nextMonth() {
			
		GregorianCalendar focused= new GregorianCalendar();
		Date newDate = curr.getDate();
		System.out.println("# # # " + newDate.toString());
		focused.setTime(newDate);
		focused.add(Calendar.MONTH, 1);
		System.out.println("# # # " + focused.getTime().toString());
		
//		int day = focused.get(Calendar.DAY_OF_MONTH);
//		int month = focused.get(Calendar.MONTH);
//		int year = focused.get(Calendar.YEAR);
//				
//		if (month == 11) {
//			month = 0;
//			year++;
//		} else
//			month++;
//		
//		focused.set(Calendar.DAY_OF_MONTH, day);	
//		focused.set(Calendar.MONTH, month);
//		focused.set(Calendar.YEAR, year);
		newDate = new Date(focused.getTime().getTime());
		int day = focused.get(Calendar.DAY_OF_MONTH);
		
		setDate(newDate);
		//selectedBlock.setDate(newDate);
		//reportFocusGained(selectedBlock);		
		TinyDayBlock b = new TinyDayBlock(this, newDate, Color.LIGHT_GRAY, false);
		
		focused.setTime(selectedBlock.getDate());	
		curr.deselectDay(focused.get(Calendar.DAY_OF_MONTH));
		curr.selectDay(day);
		selectedBlock = b;
	}
	
	/** Goes back one month on the day picker. */
	public void prevMonth() {
		
		GregorianCalendar focused= new GregorianCalendar();
		Date newDate = curr.getDate();
		focused.setTime(newDate);
		
		
		int day = focused.get(Calendar.DAY_OF_MONTH);
		int month = focused.get(Calendar.MONTH);
		int year = focused.get(Calendar.YEAR);
		if (month == 0) {
			month = 11;
			year--;
		} else
			month--;
		
		
		focused.set(Calendar.DAY_OF_MONTH, day);	
		focused.set(Calendar.MONTH, month);
		focused.set(Calendar.YEAR, year);
		newDate = new Date(focused.getTime().getTime());
		
		setDate(newDate);
		//selectedBlock.setDate(newDate);
		//reportFocusGained(selectedBlock);		
		TinyDayBlock b = new TinyDayBlock(this, newDate, Color.LIGHT_GRAY, false);
		focused.setTime(selectedBlock.getDate());	
		curr.deselectDay(focused.get(Calendar.DAY_OF_MONTH));
		curr.selectDay(day);
		selectedBlock = b;
	}
	
	/**
	 * This method determines the behavior of clicking on a day in the month view.
	 */
	public void reportFocusGained(TinyDayBlock b) {
		
		GregorianCalendar focused= new GregorianCalendar();
		Date newDate1 = b.getDate();
		focused.setTime(newDate1);
		
		GregorianCalendar block= new GregorianCalendar();
		Date newDate2 = selectedBlock.getDate();
		block.setTime(newDate2);		
		
		if (focused.get(Calendar.MONTH) == block.get(Calendar.MONTH)) {
			if (mainWindow.inMonthView()) mainWindow.switchView();
			curr.deselectDay(block.get(Calendar.DAY_OF_MONTH));
			curr.selectDay(focused.get(Calendar.DAY_OF_MONTH));
			selectedBlock = b;
			mainWindow.setDate(b.getDate());
		} else {
			if (mainWindow.inMonthView()) mainWindow.switchView();
			setDate(b.getDate());
			System.out.println(block.get(Calendar.DAY_OF_MONTH));
			curr.deselectDay(block.get(Calendar.DAY_OF_MONTH));
			// TODO: I just added deselectDay here to fix a day highlighting bug
			// however, it seems that it should not be necessary because when the month changes
			// all of the tiny day blocks should be regenerated: THE TINYDAYBLOCKS MAY NOT BE REGENERATED!!
			curr.selectDay(focused.get(Calendar.DAY_OF_MONTH));
			selectedBlock = b;
		}
	}
}