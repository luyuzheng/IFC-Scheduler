package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.managers.FileManager;
import data.managers.PatientManager;
import data.managers.PractitionerManager;


public class DayLoader {
	private File file;
	private static PractitionerManager pm = new PractitionerManager();
	private static PatientManager patm = new PatientManager();
	
	public Day loadDay(Date date) {
		Day day = null;
		file = new File(Constants.DATA_FILE_LOCATION + date.toFileString() + ".txt");
		File parent = new File("data");
		try {
			if (!parent.mkdirs() && !file.createNewFile()) {
				day = generateDay(date);
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Practitioner Manager: " + e.getMessage());
		}
		
		return day;
	}
	
	private Day generateDay(Date dd) {
		String date = dd.toString();
		ArrayList<String> data = new FileManager().loadDay(dd);
		//Moving PractitionerManager and PatientManager to outside of function and making them static
		/*
		PractitionerManager pm = new PractitionerManager();
		PatientManager patm = new PatientManager();
		*/
		if (data == null || data.size() == 0) return null;
		String slot = data.get(0);
		slot = slot.replaceAll("<timeslot>", "");
		slot = slot.replaceAll("</timeslot>", "");
		String h1 = slot.replaceAll(":[0-9][0-9] - [0-9][0-9]:[0-9][0-9]", "");
		String m1 = slot.replaceAll(" - [0-9][0-9]:[0-9][0-9]", "");
		m1 = m1.replaceAll("[0-9][0-9]:", "");
		String h2 = slot.replaceAll("[0-9][0-9]:[0-9][0-9] - ", "");
		h2 = h2.replaceAll(":[0-9][0-9]", "");
		String m2 = slot.replaceAll("[0-9][0-9]:[0-9][0-9] - [0-9][0-9]:", "");
		
		Time t1 = new Time(Integer.parseInt(h1), Integer.parseInt(m1));
		Time t2 = new Time(Integer.parseInt(h2), Integer.parseInt(m2));
		TimeSlot ts = new TimeSlot(t1, t2);
		
		int m = Integer.parseInt(date.substring(0, 2));
		int d = Integer.parseInt(date.substring(2,4));
		int y = Integer.parseInt(date.substring(4));
		
		Day day = new Day(ts, new Date(m,d,y));
		data.remove(0);
		
		
		while(!data.isEmpty() && data.get(0).startsWith("<practitioner>")) {
			String prac = data.get(0);
			prac = prac.replaceAll("<practitioner>", "");
			prac = prac.replaceAll("</practitioner>", "");
			if (prac.equals("")) {
				data.remove(0);
			} else {
				Practitioner p = pm.getPractitioner(Integer.parseInt(prac));
				Room r = day.addRoom(p);
				data.remove(0);
				while (!data.isEmpty() && !data.get(0).startsWith("<practitioner>")) {
					String appt = data.get(0);
					String apptSt = appt.substring(0, appt.indexOf(">"));
					String apptEnd = appt.substring(appt.lastIndexOf("<"));
					String apptPat = appt.substring(appt.indexOf(">") + 1, appt.lastIndexOf("<"));
					String apptNote = "";
					apptEnd = apptEnd.replaceAll("<", "");
					apptEnd = apptEnd.replaceAll(">", "");
					apptSt = apptSt.replaceAll("<", "");
					apptSt = apptSt.replaceAll(">", "");
					apptNote = apptPat.substring(apptPat.indexOf(":") + 1);
					apptPat = apptPat.substring(0, apptPat.indexOf(":"));
					int id = -1;
					try {
						id = Integer.parseInt(apptPat);
					} catch (Exception e) { }
					String[] start = apptSt.split(":");
					String[] end = apptEnd.split(":");
					Time startTime = new Time(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
					Time endTime = new Time(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
					TimeSlot apptSlot = new TimeSlot(startTime, endTime);
					Patient patient = patm.getPatient(id);
					Appointment a = r.getAppointment(apptSlot);
					if (a==null) System.out.println("NULL APPT");
					else {
						a.setPatient(patient);
						a.setNote(apptNote);
					}
					data.remove(0); 
				}
			}
				
		}
		return day;
	}
}
