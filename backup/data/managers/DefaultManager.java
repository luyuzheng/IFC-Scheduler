package data.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Time;
import data.TimeSlot;

public class DefaultManager {
	public static int SUNDAY = 0;
	public static int MONDAY = 1;
	public static int TUESDAY = 2;
	public static int WEDNESDAY = 3;
	public static int THURSDAY = 4;
	public static int FRIDAY = 5;
	public static int SATURDAY = 6;
	
	private ArrayList<TimeSlot> slots;
	
	private File file = new File(Constants.DEFAULT_FILE_LOCATION);
	private FileManager fm = new FileManager();
	
	public DefaultManager() {
		try {
			File parent = new File(file.getParent());
			if (!parent.mkdirs() && !file.createNewFile()) {
				buildDefaultList();
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Default Manager: " + e.getMessage());
		}
	}
	
	private void buildDefaultList() {
		slots = new ArrayList<TimeSlot>();
		ArrayList<String> contents = fm.loadFile(file);
		for (String s : contents) {
			String[] times = s.split("-");
			String[] time1 = times[0].split(":");
			String[] time2 = times[1].split(":");
			Time t1 = new Time(Integer.parseInt(time1[0]), Integer.parseInt(time1[1]));
			Time t2 = new Time(Integer.parseInt(time2[0]), Integer.parseInt(time2[1]));
			TimeSlot ts = new TimeSlot(t1,t2);
			slots.add(ts);
		}
	}
	
	public TimeSlot getTimeSlot(int day) {
		if (slots == null || day < 0 || day >= slots.size()) return Constants.DEFAULT_TIMESLOT;
		else return slots.get(day);
	}
	
	public void storeTimeSlots(ArrayList<TimeSlot> slots) {
		this.slots = slots;
		ArrayList<String> fileList = new ArrayList<String>();
		for (TimeSlot s : slots) {
			fileList.add(s.toFileString());
		}
		fm.writeFile(fileList, file);
	}
	
}
