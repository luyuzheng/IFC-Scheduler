/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObjects;

import java.sql.Date;
import java.util.HashMap;

/**
 *
 * @author kenny
 */
public class PractionerDto extends AbstractDto {
    
    public static final String PRACT_ID = "PractID";
    public static final String TYPE_ID = "TypeID";
    public static final String TYPE_NAME = "TypeName";
    public static final String FIRST = "FirstName";
    public static final String LAST = "LastName";
    public static final String APPT_LENGTH = "ApptLenght";
    public static final String PHONE = "PhoneNumber";
    public static final String NOTES = "Notes";
    
    public PractionerDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(PRACT_ID, null);
        fieldsMap.put(TYPE_ID, null);
        fieldsMap.put(FIRST, null);
        fieldsMap.put(LAST, null);
        fieldsMap.put(APPT_LENGTH, null);
        fieldsMap.put(PHONE, null);
        fieldsMap.put(NOTES, null);
        fieldsMap.put(TYPE_NAME, null);
    }
    
    public Integer getPractID(){
        return (Integer) fieldsMap.get(PRACT_ID);
    }
    
    public Integer getTypeID(){
        return (Integer) fieldsMap.get(TYPE_ID);
    }
    
    public void setTypeID(int newID){
        fieldsMap.put(TYPE_ID, newID);
    }
    
    public String getTypeName(){
        return (String) fieldsMap.get(TYPE_NAME);
    }
    
    public String getFirst(){
        return (String) fieldsMap.get(FIRST);
    }
    
    public void setFirst(String s){
        fieldsMap.put(FIRST, s);
    }
    
    public String getLast(){
        return (String) fieldsMap.get(LAST);
    }
    
    public void SetLast(String s){
        fieldsMap.put(LAST, s);
    }
    
    public String getPhone(){
        return (String) fieldsMap.get(PHONE);
    }
    
    public void setPhone(String s){
        fieldsMap.put(PHONE, s);
    }
    
    public int getApptLength(){
        return (Integer) fieldsMap.get(APPT_LENGTH);
    }
    
    public void setApptLength(Integer i){
        fieldsMap.put(APPT_LENGTH, i);
    }
    
    public String getNotes(){
       return (String) fieldsMap.get(NOTES);
    }
    
    public void setNotes(String s){
       fieldsMap.put(NOTES, s);
    }
}
