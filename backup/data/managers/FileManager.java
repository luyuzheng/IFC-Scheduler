package data.managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import data.Constants;
import data.Date;

public class FileManager {
	//stores each line of a file into an array list of strings and returns it
	public ArrayList<String> loadFile(File file) {
        String line;
        ArrayList<String> fileList = new ArrayList<String>();
        try
        {    
            BufferedReader in = new BufferedReader(new FileReader(file));
            if (!in.ready())
            	return fileList;
            while ((line = in.readLine()) != null)
                fileList.add(line);
            in.close();
        }
        catch (IOException e)
        {
            System.err.println("Error encountered: " + e.getMessage());
            return null;
        }
        return fileList;
    }
	
	//writes a file given an array list of strings and a filename
	public void writeFile(ArrayList<String> str, File file) {
		try {
			file.delete();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			if (str.size() > 0) { 
				writer.write(str.get(0));
				for (int i = 1; i < str.size(); i++) {
					writer.newLine();
					writer.write(str.get(i));
				}
			}
			writer.close();
		} catch (IOException e) { }
	}
	
	public ArrayList<String> loadDay(Date d) {
		String day = d.toString();
		String line;
		boolean found = false;
		ArrayList<String> dayList = new ArrayList<String>();
		try {
			File f = new File(Constants.DATA_FILE_LOCATION + d.toFileString() + ".txt");
			if (!f.exists()) return null;
			BufferedReader in = new BufferedReader(new FileReader(f));
			if (!in.ready())
				return null;
			while ((line = in.readLine()) != null) {
				if (found && line.startsWith("</" + day + ">"))
					return dayList;
				else if (found) 
					dayList.add(line);
				else if (line.startsWith("<" + day + ">")) 
					found = true;
			}
		} catch (IOException e) {
			System.err.println("Error encountered while trying to collect day data: " + e.getMessage());
			return null;
		}
		return null;
	}
	
	public void storeDay(ArrayList<String> day, Date d) {
		if (day == null) return;
		File file = new File(Constants.DATA_FILE_LOCATION + d.toFileString() + ".txt");
		File parent = new File(file.getParent());
		try {
			parent.mkdirs();
			file.createNewFile();
		} catch (Exception e) { }
		String date = day.get(0);
		String date2 = day.get(day.size()-1);
		ArrayList<String> fileData = new ArrayList<String>();
		boolean found = false, done = false;
		String line;
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			if (!in.ready()) {
				day.add(0, "<format>1.0</format>");
				writeFile(day, file);
				return;
			}
			while ((line = in.readLine()) != null) {
				if (line.equals(date)) {
					found = true;
					fileData.addAll(day);
				} else if (line.equals(date2)) 
					done = true;
				else if (found && done) 
					fileData.add(line);
				else if (!found && !done)
					fileData.add(line);
			}
			if (fileData.size() == 0) fileData.add("<format>1.0</format>");
			if (!found) fileData.addAll(day);
			writeFile(fileData, file);
		} catch (IOException e) {
			System.err.println("Error encouncxtered while trying to collect day data: " + e.getMessage());
			return;
		}
	}
}
