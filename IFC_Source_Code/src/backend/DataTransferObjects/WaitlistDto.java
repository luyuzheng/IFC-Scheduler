/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.DataTransferObjects;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author kenny
 */
public class WaitlistDto extends AbstractDto {
    
    public static final String WAITLIST_ID = "WaitlistID";
    public static final String TYPE = "Type";
    public static final String TIMESTAMP = "DatetimeEntered";
    public static final String COMMENTS = "Comments";
    
    private PatientDto patient;
    
    public WaitlistDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(WAITLIST_ID, null);
        fieldsMap.put(TYPE, null);
        fieldsMap.put(TIMESTAMP, null);
        fieldsMap.put(COMMENTS, null);
    }
    
    public Integer getWaitlistID(){
        return (Integer) fieldsMap.get(WAITLIST_ID);
    }
    
    public Integer getPatientID(){
        return patient.getPatID();
    }

    public PatientDto getPatient() {
    	return patient;
    }

    public void setPatient(PatientDto patient) {
    	this.patient = patient;
    }

    public Integer getTypeID(){
        return ((TypeDto) fieldsMap.get(TYPE)).getTypeID();
    }
    
    public void setType(TypeDto t){
        fieldsMap.put(TYPE, t);
    }
    
    public String getTypeName(){
        return ((TypeDto) fieldsMap.get(TYPE)).getTypeName();
    }
    
    
    public Timestamp getTimestamp() {
    	return (Timestamp) fieldsMap.get(TIMESTAMP);
    }
    
   public String getDate(){
	   DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
	   return dateFormat.format(getTimestamp().getTime());
    }

   public String getTimeAdded(){
	   DateFormat timeFormat = new SimpleDateFormat("h:mm a");
    	return timeFormat.format(getTimestamp().getTime());
   }
    
    public String getComments() {
    	return (String) fieldsMap.get(COMMENTS);
    }
    
    public void setComments(String comment) {
    	fieldsMap.put(COMMENTS, comment);
    }
}
