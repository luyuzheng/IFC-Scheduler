/**
 * This class is responsible for interacting with the FileManager. It can get a list 
 * of existing practitioners from the data file or add a new one. 
 */

package data.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Type;

public class TypeManager {
	
	private ArrayList<Type> types = new ArrayList<Type>();
	private File file;
	
	public TypeManager() {
		file = new File(Constants.TYPE_FILE_LOCATION);
		try {
			File parent = new File(file.getParent());
			if (!parent.mkdirs() && !file.createNewFile()) {
				createTypeList();
			}
		} catch (IOException e) {
			System.err.println("IO Error Caught By Practitioner Manager: " + e.getMessage());
		}
	}
	
	public ArrayList<Type> getTypeList() {
		try {
			createTypeList();
		} catch(Exception e) {
			
		}
		return types;
	}
	
	/**
	 * Adds a new practitioner to the practitioner data file
	 * @param p the practitioner to be added
	 */
	public void addType(Type t) {
		ArrayList<String> contents = new FileManager().loadFile(file);
		if (contents.size() == 0) contents.add("<format>1.0</format>");
		contents.add("<type>");
		contents.add("<id>" + t.getId() + "</id>");
		contents.add("<name>" + t.toString() + "</name>");
		contents.add("</type>");
		new FileManager().writeFile(contents, file);
	}
	
	public void updateList() {
		types = new ArrayList<Type>();
		try {
			createTypeList();
		} catch(Exception e) {}
	}
	
	/**
	 * Generates a list of existing practitioner types from the type data file
	 * @throws IOException
	 */
	private void createTypeList() throws IOException {
		types = new ArrayList<Type>();
		ArrayList<String> contents = new FileManager().loadFile(file);
		int counter = 1;
		while (counter < contents.size() && contents.get(counter).equals("<type>")) {
			counter++;
			String idString = (String)contents.get(counter);
			idString = idString.replaceFirst("<id>", "");
			idString = idString.replaceFirst("</id>", "");
			int id = Integer.parseInt(idString);
			counter++;
			String name = (String)contents.get(counter);
			name = name.replaceFirst("<name>", "");
			name = name.replaceFirst("</name>", "");
			counter++;
			types.add(new Type(id, name));
			counter++;
		}
	}
	
	public Type getType(int id) {
		for (Type t : types) {
			if (t.getId() == id) 
				return t;
		}
		return null;
	}
	
	public Type getType(String name) {
		for (Type t : types) {
			if (t.toString().equals(name)) 
				return t;
		}
		return null;
	}
	
	public int getNewId() {
		int maxId = -1;
		for (Type t : types) {
			if (t.getId() > maxId)
				maxId = t.getId();
		}
		maxId++;
		return maxId;
	}
	//Added by Aakashon 14th feb to fix combolist practitioner-type bug
	public int getMaxId() {
		int maxId = -1;
		for (Type t : types) {
			if (t.getId() > maxId)
				maxId = t.getId();
		}
		return maxId;
	}
}