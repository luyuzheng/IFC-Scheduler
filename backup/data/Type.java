package data;

public class Type {
	String type;
	int id;
	
	public Type(int id, String type) {
		this.type = type;
		this.id = id;
	}
	
	public String toString() {
		return type;
	}
	
	public int getId() {
		return id;
	}
}
