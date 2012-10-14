package data;

import java.util.ArrayList;

import data.managers.FileManager;


public class DaySaver {
	
	public void storeDay(Day day) {
		ArrayList<String> dayData = new ArrayList<String>();
		dayData.add("<" + day.getDate().toString() + ">");
		String time = day.getTimeSlot().getStartTime().to24HrString();
		time += " - " + day.getTimeSlot().getEndTime().to24HrString();
		dayData.add("<timeslot>" + time + "</timeslot>");
		for (Room r : day.getRooms()) {
			if (!r.hasPrac()) 
				dayData.add("<practitioner></practitioner>");
			else {
				dayData.add("<practitioner>" + r.getPractitioner().getId() + "</practitioner>");
				for (Appointment a : r.getAppointments()) {
					if (a.isFilled()) {
						String appt = "<" + a.getTimeSlot().getStartTime().to24HrString() + ">";
						appt += a.getPatient().getId();
						appt += ":";
						appt += a.getNote();
						appt += "<" + a.getTimeSlot().getEndTime().to24HrString() + ">";
						dayData.add(appt);
					}
				}
			}
		}
		dayData.add("</" + day.getDate().toString() + ">");
		new FileManager().storeDay(dayData, day.getDate());
	}
}
