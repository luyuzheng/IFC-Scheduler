package data;

public class Date {
	private int month;
	private int day;
	private int year;
	
	public Date(int month, int day, int year) {
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}
	
	public int getYear() {
		return year;
	}
	
	public String toString() {
		String m, d;
		if (month < 10) 
			m = "0" + month;
		else m = "" + month;
		if (day < 10)
			d = "0" + day;
		else d = "" + day;
		return m + d + year;
	}
	
	public String toFormalString() {
		return month + "/" + day + "/" + year;
	}
	
	public String toFileString() {
		String m;
		if (month < 10) 
			m = "0" + month;
		else m = "" + month;
		return m + year;
	}
	
	public String[] toPrintString() {
		return new String[] {month + "/" + day, year + ""};
	}
}
