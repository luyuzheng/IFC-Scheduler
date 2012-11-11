/**
 * This class is responsible for interacting with the FileManager. It can get a list 
 * of existing practitioners from the data file or add a new one. 
 */

package data.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Practitioner;
import data.Type;


public class PractitionerManager {
	
	private ArrayList<Practitioner> prac = new ArrayList<Practitioner>();
	private File file;
	private FileManager fm = new FileManager();
	
	public PractitionerManager() {
		file = new File(Constants.PRAC_FILE_LOCATION);
		try {
			File parent = new File(file.getParent());
			if (!parent.mkdirs() && !file.createNewFile()) {
				createPracList();
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Practitioner Manager: " + e.getMessage());
		}
	}
	
	public ArrayList<Practitioner> getFilteredPractitionerList(String filter) {
		ArrayList<Practitioner> filteredPrac = new ArrayList<Practitioner>();
		filter = filter.toLowerCase();
		String[] filters = filter.split(" ");
		for (Practitioner p : prac) {
			String name = p.getName().toLowerCase();
			boolean match = true;
			for (String f : filters) {
				if (name.indexOf(f) >= 0) 
					name = name.replaceFirst(f, "*");
				else {
					match = false;
					break;
				}
			}
			
			if (match) filteredPrac.add(p);
		}
		return filteredPrac;
	}
	
	public ArrayList<Practitioner> getPractitionerList() {
		try {
			createPracList();
		} catch (Exception e) {}
		return prac;
	}
	
	/**
	 * Adds a new practitioner to the practitioner data file
	 * @param p the practitioner to be added
	 */
	public void addPractitioner(Practitioner p) {
		ArrayList<String> contents = fm.loadFile(file);
		if (contents.size() == 0) contents.add("<format>1.0</format>");
		contents.add("<practitioner>");
		contents.add("<id>" + p.getId() + "</id>");
		contents.add("<name>" + p.getName() + "</name>");
		contents.add("<type>" + p.getType().getId() + "</type>");
		contents.add("<appointment_length>" + p.getApptLength() + "</appointment_length>");
		contents.add("<note>" + p.getNote().replaceAll("[\r\n]+", "\t\t") + "</note>");
		contents.add("</practitioner>");
		fm.writeFile(contents, file);
	}
	
	public void updatePractitioner(Practitioner p) {
		ArrayList<String> contents = fm.loadFile(file);
		int index = 0;
		boolean found = false;
		while (!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.set(index+1,"<name>" + p.getName() + "</name>");
				contents.set(index+2,"<type>" + p.getType().getId() + "</type>");
				contents.set(index+3,"<appointment_length>" + p.getApptLength() + "</appointment_length>");
				contents.set(index+4, "<note>" + p.getNote() + "</note>");
			}
			index++;
		}
		
		if (found) fm.writeFile(contents, file);
		else addPractitioner(p);
	}
	
	public void retirePractitioner(Practitioner p) {
		ArrayList<String> contents = fm.loadFile(file);
		int index = 0;
		boolean found = false;
		while (!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.set(index+1,"<name>" + p.getName() + "</name>");
				contents.set(index+2,"<type>" + p.getType().getId() + "</type>");
				contents.set(index+3,"<appointment_length>" + p.getApptLength() + "</appointment_length>");
				contents.set(index+4, "<note>" + p.getNote() + "</note>");
				contents.add(index+1, "<retired>");
			}
			index++;
		}
		
		fm.writeFile(contents, file);
	}
	
	/**
	 * Generates a list of existing practitioners from the practitioner data file
	 * @throws IOException
	 */
	private void createPracList() throws IOException {
		prac = new ArrayList<Practitioner>();
		ArrayList<String> contents = fm.loadFile(file);
		int counter = 1;
		while (counter < contents.size() && contents.get(counter).equals("<practitioner>")) {
			boolean retired = false;
			counter++;
			String idString = (String)contents.get(counter);
			idString = idString.replaceFirst("<id>", "");
			idString = idString.replaceFirst("</id>", "");
			int id = Integer.parseInt(idString);
			counter++;
			String retire = (String)contents.get(counter);
			if (retire.equals("<retired>")) {
				retired = true;
				counter++;
			}
			String name = (String)contents.get(counter);
			name = name.replaceFirst("<name>", "");
			name = name.replaceFirst("</name>", "");
			counter++;
			String type = (String)contents.get(counter);
			type = type.replaceFirst("<type>", "");
			type = type.replaceFirst("</type>", "");
			Type t = new TypeManager().getType(Integer.parseInt(type));
			counter++;
			String apptLength = (String)contents.get(counter);
			apptLength = apptLength.replaceFirst("<appointment_length>", "");
			apptLength = apptLength.replaceFirst("</appointment_length>", "");
			counter++;
			String note = (String)contents.get(counter);
			note = note.replaceFirst("<note>", "");
			note = note.replaceFirst("</note>", "");
			if (!retired) prac.add(new Practitioner(id, name, t, Integer.parseInt(apptLength), note));
			counter++;
			counter++;
		}
	}
	
	public Practitioner getPractitioner(int id) {
		for (Practitioner p : prac) {
			if (p.getId() == id) 
				return p;
		}
		return null;
	}
	
	public int getNewId() {
		int maxId = -1;
		for (Practitioner p : prac) {
			if (p.getId() > maxId)
				maxId = p.getId();
		}
		maxId++;
		return maxId;
	}
}
