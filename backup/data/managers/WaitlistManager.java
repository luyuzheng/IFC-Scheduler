/**
 * This class is responsible for interacting with the FileManager. It can get a list 
 * of existing practitioners from the data file or add a new one. 
 */

package data.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Patient;
import data.Type;
import data.WaitingPatient;


public class WaitlistManager {
	
	private ArrayList<WaitingPatient> waits = new ArrayList<WaitingPatient>();
	private PatientManager pm = new PatientManager();
	private TypeManager tm = new TypeManager();
	private File file;
	
	public WaitlistManager() {
		file = new File(Constants.WAITLIST_FILE_LOCATION);
		try {
			File parent = new File(file.getParent());
			if (!parent.mkdirs() && !file.createNewFile()) {
				createWaitList();
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Waitlist Manager: " + e.getMessage());
		}
	}
	
	public ArrayList<WaitingPatient> getWaitList() {
		return waits;
	}
	
	public ArrayList<WaitingPatient> getWaitList(Type t) {
		ArrayList<WaitingPatient> sub = new ArrayList<WaitingPatient>();
		for (WaitingPatient p : waits) {
			if (p.getType().getId() == t.getId())
				sub.add(p);
		}
		return sub;
	}
	
	public void updateWaitingPatient(WaitingPatient p) {
		ArrayList<String> contents = new FileManager().loadFile(file);
		int index = 0;
		boolean found = false;
		while (!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.set(index+1,"<pid>" + p.getPatient().getId() + "</pid>");
				contents.set(index+2,"<tid>" + p.getType().getId() + "</tid>");
				contents.set(index+3,"<timestamp>" + p.getTimestamp() + "</timestamp>");
				contents.set(index+4, "<comment>" + p.getComment().replaceAll("[\r\n]+","\t\t") + "</comment>");
			}
			index++;
		}
		
		if (found) new FileManager().writeFile(contents, file);
		else addWaitingPatient(p);
	}
	
	/**
	 * Adds a new practitioner to the practitioner data file
	 * @param p the practitioner to be added
	 */
	public void addWaitingPatient(WaitingPatient p) {
		ArrayList<String> contents = new FileManager().loadFile(file);
		if (contents.size() == 0) contents.add("<format>1.0</format>");
		contents.add("<wl>");
		contents.add("<id>" + p.getId() + "</id>");
		contents.add("<pid>" + p.getPatient().getId() + "</pid>");
		contents.add("<tid>" + p.getType().getId() + "</tid>");
		contents.add("<timestamp>" + p.getTimestamp() + "</timestamp>");
		contents.add("<comment>" + p.getComment().replaceAll("[\r\n]+", "\t\t") + "</comment>");
		contents.add("</wl>");
		new FileManager().writeFile(contents, file);
		
	}
	
	public boolean removeWaitingPatient(WaitingPatient p) {
		FileManager fm = new FileManager();
		ArrayList<String> contents = fm.loadFile(file);
		int index = 0;
		boolean found = false;
		while(!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.remove(index); //remove id field
				contents.remove(index); //remove first name
				contents.remove(index); //remove last name
				contents.remove(index); //remove phone
				contents.remove(index); //remove note
				contents.remove(index); //remove close tag
				contents.remove(index-1); //remove open tag
				fm.writeFile(contents, file);
			}
			index++;
		}
		return found;
	}
	
	public void updateWaitlist() {
		waits = new ArrayList<WaitingPatient>();
		try {
			createWaitList();
		} catch(Exception e) {}
	}
	
	/**
	 * Generates a list of existing practitioner types from the type data file
	 * @throws IOException
	 */
	private void createWaitList() throws IOException {
		pm.updateList();
		tm.updateList();
		ArrayList<String> contents = new FileManager().loadFile(file);
		int counter = 1;
		while (counter < contents.size() && contents.get(counter).equals("<wl>")) {
			counter++;
			String idString = (String)contents.get(counter);
			idString = idString.replaceFirst("<id>", "");
			idString = idString.replaceFirst("</id>", "");
			int id = Integer.parseInt(idString);
			counter++;
			String pidString = (String)contents.get(counter);
			pidString = pidString.replaceFirst("<pid>", "");
			pidString = pidString.replaceFirst("</pid>", "");
			int pid = Integer.parseInt(pidString);
			counter++;
			String tidString = (String)contents.get(counter);
			tidString = tidString.replaceFirst("<tid>", "");
			tidString = tidString.replaceFirst("</tid>", "");
			int tid = Integer.parseInt(tidString);
			counter++;
			String timestampString = (String)contents.get(counter);
			timestampString = timestampString.replaceFirst("<timestamp>", "");
			timestampString = timestampString.replaceFirst("</timestamp>", "");
			long timestamp = Long.parseLong(timestampString);
			counter++;
			String comment = (String)contents.get(counter);
			comment = comment.replaceFirst("<comment>", "");
			comment = comment.replaceFirst("</comment>", "");
			counter++;
			
			Patient p = pm.getPatient(pid);
			Type t = tm.getType(tid);
			
			waits.add(new WaitingPatient(id, p, t, comment, timestamp));
			counter++;
		}
	}
	
	public WaitingPatient getWaitingPatient(int id) {
		for (WaitingPatient w : waits) {
			if (w.getId() == id) 
				return w;
		}
		return null;
	}
	
	public int getNewId() {
		int maxId = -1;
		for (WaitingPatient w : waits) {
			if (w.getId() > maxId)
				maxId = w.getId();
		}
		maxId++;
		return maxId;
	}
}
