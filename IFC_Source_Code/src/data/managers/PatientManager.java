/**
 * The PatientManager is responsible for interacting with the FileManager. It can
 * read existing patients from the patients data file and add a new one. 
 */

package data.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Patient;
import data.PhoneNumber;


public class PatientManager {
	private ArrayList<Patient> pat = new ArrayList<Patient>();
	private File file;
	private FileManager fm = new FileManager();
	
	public PatientManager() {
		file = new File(Constants.PAT_FILE_LOCATION);
		try {
			if (!file.createNewFile()) {
				createPatList();
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Patient Manager: " + e.getMessage());
		}
	}
	
	public ArrayList<Patient> getPatientList() {
		updateList();
		return pat;
	}
	
	public ArrayList<Patient> getFilteredPatientList(String filter) {
		ArrayList<Patient> filteredPat = new ArrayList<Patient>();
		filter = filter.toLowerCase();
		String[] filters = filter.split(" ");
		for (Patient p : pat) {
			String name = p.getFullName().toLowerCase();
			boolean match = true;
			for (String f : filters) {
				if (name.indexOf(f) >= 0) 
					name = name.replaceFirst(f, "*");
				else {
					match = false;
					break;
				}
			}
			
			if (match) filteredPat.add(p);
		}
		return filteredPat;
	}
	
	public Patient patientExists(Patient p) {
		for (Patient patient : pat) {
			if (p.getFullName().equals(patient.getFullName()))
				return patient;
		}
		return null;
	}
	
	/**
	 * Writes a new patient to the patients data file
	 * @param p The patient to be added
	 */
	public void addPatient(Patient p) {
		ArrayList<String> contents = fm.loadFile(file);
		if (contents.size() == 0) contents.add("<format>1.0</format>");
		contents.add("<patient>");
		contents.add("<id>" + p.getId() + "</id>");
		contents.add("<first_name>" + p.getFirstName() + "</first_name>");
		contents.add("<last_name>" + p.getLastName() + "</last_name>");
		contents.add("<phone>" + p.getNumberString() + "</phone>");
		contents.add("<note>" + p.getNote().replaceAll("[\r\n]+", "\t\t") + "</note>"); //Modified by Aakash on 12th feb to fix multiline note bug
		contents.add("</patient>");
		fm.writeFile(contents, file);
	}
	
	public void updateList() {
		pat = new ArrayList<Patient>();
		try {
			createPatList();
		} catch(Exception e) {}
	}
	
	public void updatePatient(Patient p) {
		ArrayList<String> contents = fm.loadFile(file);
		int index = 0;
		boolean found = false;
		while (!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.set(index+1,"<first_name>" + p.getFirstName() + "</first_name>");
				contents.set(index+2,"<last_name>" + p.getLastName() + "</last_name>");
				contents.set(index+3,"<phone>" + p.getNumberString() + "</phone>");
				contents.set(index+4, "<note>" + p.getNote().replaceAll("[\r\n]+", "\t\t") + "</note>"); //Modified by Aakash on 12th feb to fix multiline note bug
			}
			index++;
		}
		
		if (found) fm.writeFile(contents, file);
		else addPatient(p);
	}
	
	public void retirePatient(Patient p) {
		ArrayList<String> contents = fm.loadFile(file);
		int index = 0;
		boolean found = false;
		while (!found && index < contents.size()) {
			if (contents.get(index).equals("<id>" + p.getId() + "</id>")) {
				found = true;
				contents.set(index+1,"<first_name>" + p.getFirstName() + "</first_name>");
				contents.set(index+2,"<last_name>" + p.getLastName() + "</last_name>");
				contents.set(index+3,"<phone>" + p.getNumberString() + "</phone>");
				contents.set(index+4, "<note>" + p.getNote().replaceAll("[\r\n]+", "\t\t") + "</note>"); //Modified by Aakash on 12th feb to fix multiline note bug
				contents.add(index+1, "<retired>");
			}
			index++;
		}
		
		fm.writeFile(contents, file);
	}
	
	public boolean removePatient(Patient p) {
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
	
	/**
	 * Generates the list of existing patients from the patients data file
	 * @throws IOException
	 */
	private void createPatList() throws IOException {
		pat = new ArrayList<Patient>();
		ArrayList<String> contents = fm.loadFile(file);
		int counter = 1;
		while (counter < contents.size() && contents.get(counter).equals("<patient>")) {
			boolean retire = false;
			counter++;
			String idString = contents.get(counter);
			idString = idString.replaceFirst("<id>", "");
			idString = idString.replaceFirst("</id>","");
			int id = -1;
			try {
				id = Integer.parseInt(idString);
			} catch (Exception e) {}
			counter++;
			String retired = contents.get(counter);
			if (retired.equals("<retired>")) {
				retire = true;
				counter++;
			}
			String firstName = contents.get(counter);
			firstName = firstName.replaceFirst("<first_name>", "");
			firstName = firstName.replaceFirst("</first_name>", "");
			counter++;
			String lastName = contents.get(counter);
			lastName = lastName.replaceFirst("<last_name>", "");
			lastName = lastName.replaceFirst("</last_name>", "");
			counter++;
			String number = contents.get(counter);
			number = number.replaceFirst("<phone>", "");
			number = number.replaceFirst("</phone>", "");
			String[] parts = number.split("-");
			PhoneNumber phone = null;
			try {
				phone = new PhoneNumber(parts[0], parts[1], parts[2]);
			} catch (Exception e) {}
			counter++;
			String note = contents.get(counter);
			note = note.replaceFirst("<note>","");
			note = note.replaceFirst("</note>", "");
			counter++;
			if (!retire) pat.add(new Patient(id, firstName, lastName, phone, note));
			counter++;
		}
	}
	
	public int getNewId() {
		int maxId = -1;
		for (Patient p : pat) {
			if (p.getId() > maxId)
				maxId = p.getId();
		}
		maxId++;
		return maxId;
	}
	
	public Patient getPatient(int id) {
		for (Patient p : pat) {
			if (p.getId() == id) 
				return p;
		}
		return null;
	}
}
