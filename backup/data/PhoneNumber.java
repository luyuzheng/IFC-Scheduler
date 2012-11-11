package data;

public class PhoneNumber {
	String areaCode;
	String first;
	String second;
	
	public PhoneNumber(String areaCode, String first, String second) {
		this.areaCode = areaCode;
		this.first = first;
		this.second = second;
	}
	
	public String toString() {
		return areaCode + "-" + first + "-" + second;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof PhoneNumber)) return false;
		return ((PhoneNumber)o).first == first && 
		       ((PhoneNumber)o).second == second &&
		       ((PhoneNumber)o).areaCode == areaCode;
	}
}
