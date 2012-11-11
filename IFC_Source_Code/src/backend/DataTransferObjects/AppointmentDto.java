/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.DataTransferObjects;

import java.sql.Date;
import java.util.HashMap;
/**
 *
 * @author kenny
 */
public class AppointmentDto extends AbstractDto {

    public static final String APPT_ID = "ApptID";
    public static final String PRACT_SCHED_ID = "PractSchedID";
    public static final String PAT_ID = "PatID";
    public static final String NO_SHOW_ID = "NoShowID";
    public static final String START = "StartTime";
    public static final String END = "EndTime";
    public static final String APPT_DATE = "ApptDate";
    public static final String NOTE = "Note";
    
     public AppointmentDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(APPT_ID, null);
        fieldsMap.put(PRACT_SCHED_ID, null);
        fieldsMap.put(PAT_ID, null);
        fieldsMap.put(NO_SHOW_ID, null);
        fieldsMap.put(START, null);
        fieldsMap.put(END, null);
        fieldsMap.put(APPT_DATE, null);
        fieldsMap.put(NOTE, null);
    }
    
    public Integer getApptID(){
        return (Integer) fieldsMap.get(APPT_ID);
    }
    
    public Integer getPractSchedID(){
        return (Integer) fieldsMap.get(PRACT_SCHED_ID);
    }
    
    public Integer getPatientID(){
        return (Integer) fieldsMap.get(PAT_ID);
    }
    
    public void setPatientID(Integer i){
        fieldsMap.put(PAT_ID, i);
    }
    
    public boolean isNoShow(){
        return fieldsMap.get(NO_SHOW_ID) != null;
    }
    
    public Integer getNoShowID(){
        return (Integer) fieldsMap.get(NO_SHOW_ID);
    }
    
    public void setNoShowID(int i){
        fieldsMap.put(NO_SHOW_ID, i);
    }
    
    public Integer getStart(){
        return (Integer) fieldsMap.get(START);
    }
    
    public void setStart(Integer i){
        fieldsMap.put(START, i);
    }
    
    public Integer getEnd(){
        return (Integer) fieldsMap.get(END);
    }
    
    public void setEnd(Integer i){
        fieldsMap.put(END, i);
    }
    
    public Date getApptDate(){
        return (Date) fieldsMap.get(APPT_DATE);
    }
    
    public String getNote(){
        return (String) fieldsMap.get(NOTE);
    }
    
    public void setNote(String s){
        fieldsMap.put(NOTE, s);
    }
    
    //TODO -- truncates length of note to number of characters specified
    public String getShortNote(int length){
    	return null;
    }
    
}
