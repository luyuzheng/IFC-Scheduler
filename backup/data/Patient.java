/**
 * Contains all the information for a patient -- i.e., first and last name.
 */

package data;

public class Patient {
	private int id;
	private String firstName;
	private String lastName;
	private PhoneNumber number;
	private String note;
	
	public Patient(int id, String firstName, String lastName, PhoneNumber number, String note) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.number = number;
		this.note = note;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public PhoneNumber getNumber() {
		return number;
	}
	
	public void setNumber(PhoneNumber number) {
		this.number = number;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getNumberString() {
		if (number == null) return "";
		else return number.toString();
	}
}
