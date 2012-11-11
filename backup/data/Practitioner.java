/**
 * This class holds all the data for a practitioner -- the name, practitioner
 * type, and appointment length.
 */

package data;

public class Practitioner {
	private int apptLength;
	private String name;
	private Type type;
	private int id;
	private String note;
	
	public Practitioner(int id, String name, Type type, int apptLength, String note) {
		this.apptLength = apptLength;
		this.name = name;
		this.type = type;
		this.id = id;
		this.note = note;
	}
	
	public int getApptLength() {
		return apptLength;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
}
