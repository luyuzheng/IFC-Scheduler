package data;

//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
// implements Transferable
public class WaitingPatient {

	private int id;
	private Patient patient;
	private Type type;
	private String comment;
	private long timestamp;
	
	public WaitingPatient(int id, Patient patient, Type type, String comment, long timestamp) {
		this.timestamp = timestamp;
		this.id = id;
		this.patient = patient;
		this.type = type;
		this.comment = comment;
	}
	
	public int getId() {
		return id;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Type getType() {
		return type;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public String getDateString() {
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(timestamp);
		String text = (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + 
		              cal.get(Calendar.YEAR) + " ";
		int hour = cal.get(Calendar.HOUR);
		if (hour == 0) hour = 12;
		int min = cal.get(Calendar.MINUTE);
		String minute = ((min < 10) ? "0" + min : min + "");
		text += hour + ":" + minute + " " + (cal.get(Calendar.AM_PM) == cal.get(Calendar.PM) ? "PM" : "AM");
		return text;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

//	@Override
//	public Object getTransferData(DataFlavor arg0)
//			throws UnsupportedFlavorException, IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public DataFlavor[] getTransferDataFlavors() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean isDataFlavorSupported(DataFlavor arg0) {
//		// TODO Auto-generated method stub
//		return false;
//	}
	
}
