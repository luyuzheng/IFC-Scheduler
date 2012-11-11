/**
 * This class creates the day data structure. A day is comprised of a time slot 
 * and a list of rooms. Each room can schedule appointment within the day's time
 * slot. 
 */

package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import data.managers.DefaultManager;


public class Day {
	private TimeSlot slot;
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private Date date;
	private DefaultManager dm;
	
	public Day(TimeSlot slot, Date date) {
		this.slot = slot;
		this.date = date;
	}
	
	/**
	 * Creates a day object with the default time slot, as defined in Constants.DEFAULT_TIMESLOT
	 */
	public Day(Date date) {
		dm = new DefaultManager();
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth() - 1);
		cal.set(Calendar.DATE, date.getDay());
		this.slot = dm.getTimeSlot(cal.get(Calendar.DAY_OF_WEEK) - 1);
		this.date = date;
	}
	
	/**
	 * gets all the rooms for the day
	 * @return rooms an ArrayList of the Room objects
	 */
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	
	public void removeRoom(Room room) {
		rooms.remove(room);
	}
	
	/**
	 * adds a room with a designated practitioner to the list
	 * @param prac the practitioner for the room
	 */
	public Room addRoom(Practitioner prac) {
		Room r = new Room(this, prac);
		rooms.add(r);
		return r;
	}
	
	/**
	 * gets the time slot for the entire day
	 * @return TimeSlot
	 */
	public TimeSlot getTimeSlot() {
		return slot;
	}
	
	public Date getDate() {
		return date;
	}
	
}
