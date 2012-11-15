/**
 * This class reports behaviors of what happens in the small calendar.
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
	
	private TinyDayBlock selectedBlock;
	private MonthView curr;
	private MainWindow mainWindow;
	private Date currentDate;
	
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
	
	public Date getDate() {
		return currentDate;
	}
	
	public void registerCurrentDay(TinyDayBlock b) {
		selectedBlock = b;
	}
	
	public void setDate(Date d) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(selectedBlock.getDate());
		curr = new MonthView(this, d, MonthView.CURRENT_MONTH);
		
		removeAll();
		add(curr);
		repaint();
		validate();
		
		curr.selectDay(cal.get(Calendar.DATE));
		mainWindow.setDate(d);		
	}
	
	public void nextMonth() {
			
		GregorianCalendar focused= new GregorianCalendar();
		Date newDate = curr.getDate();
		focused.setTime(newDate);
		
		
		//int day = curr.getDate().getDay();
		//int month = curr.getDate().getMonth();
		//int year = curr.getDate().getYear();
		
		int day = focused.get(Calendar.DAY_OF_MONTH);
		int month = focused.get(Calendar.MONTH);
		int year = focused.get(Calendar.YEAR);
				
		if (month == 12) {
			month = 1;
			year++;
		} else
			month++;
		
		focused.set(Calendar.DAY_OF_MONTH, day);	
		focused.set(Calendar.MONTH, month);
		focused.set(Calendar.YEAR, year);
		newDate = new Date(focused.getTime().getTime());
		
		//Date newDate = new Date(month, day, year);
		setDate(newDate);
		//selectedBlock.setDate(newDate);
		//reportFocusGained(selectedBlock);		
		TinyDayBlock b = new TinyDayBlock(this, newDate, Color.LIGHT_GRAY, false);
		
		focused.setTime(selectedBlock.getDate());	
		curr.deselectDay(focused.get(Calendar.DAY_OF_MONTH));
		curr.selectDay(day);
		selectedBlock = b;
		//curr.selectDay(day);
		//curr.selectDay(selectedBlock.getDate().getDay());
	}
	
	public void prevMonth() {
		
		GregorianCalendar focused= new GregorianCalendar();
		Date newDate = curr.getDate();
		focused.setTime(newDate);
		
		
		int day = focused.get(Calendar.DAY_OF_MONTH);
		int month = focused.get(Calendar.MONTH);
		int year = focused.get(Calendar.YEAR);
		if (month == 1) {
			month = 12;
			year--;
		} else
			month--;
		
		
		focused.set(Calendar.DAY_OF_MONTH, day);	
		focused.set(Calendar.MONTH, month);
		focused.set(Calendar.YEAR, year);
		newDate = new Date(focused.getTime().getTime());
		
		
		//newDate = new Date(month, day, year);
		setDate(newDate);
		//selectedBlock.setDate(newDate);
		//reportFocusGained(selectedBlock);		
		TinyDayBlock b = new TinyDayBlock(this, newDate, Color.LIGHT_GRAY, false);
		focused.setTime(selectedBlock.getDate());	
		curr.deselectDay(focused.get(Calendar.DAY_OF_MONTH));
		curr.selectDay(day);
		selectedBlock = b;

		//curr.selectDay(day);
		//curr.selectDay(selectedBlock.getDate().getDay());
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
			curr.selectDay(focused.get(Calendar.DAY_OF_MONTH));
			selectedBlock = b;
		}
	}
}