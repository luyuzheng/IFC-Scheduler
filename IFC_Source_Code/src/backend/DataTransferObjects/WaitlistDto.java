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
public class WaitlistDto extends AbstractDto {
    
    public static final String WAITLIST_ID = "WaitlistID";
    public static final String PATIENT = "PatID";
    public static final String TYPE_ID = "TypeID";
    public static final String TYPE_NAME = "TypeName";
    public static final String DATE = "DatetimeEntered";
    
    public WaitlistDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(WAITLIST_ID, null);
        fieldsMap.put(PATIENT, null);
        fieldsMap.put(TYPE_ID, this);
        fieldsMap.put(TYPE_NAME, null);
        fieldsMap.put(DATE, null);
    }
    
    public Integer getWaitlistID(){
        return (Integer) fieldsMap.get(WAITLIST_ID);
    }
    
    public PatientDto getPatient(){
        return (PatientDto) fieldsMap.get(PATIENT);
    }
    
    public Integer getTypeID(){
        return (Integer) fieldsMap.get(TYPE_ID);
    }
    
    public void setTypeID(Integer i){
        fieldsMap.put(TYPE_ID, i);
    }
    
    public String getTypeName(){
        return (String) fieldsMap.get(TYPE_NAME);
    }
    
    public Date getDate(){
        return (Date) fieldsMap.get(DATE);
    }
}
