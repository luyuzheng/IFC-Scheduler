/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.DataTransferObjects;

import backend.DataService.DataServiceImpl;
import java.sql.Date;
import java.util.HashMap;
import java.util.ArrayList;
import backend.DataTransferObjects.*;

/**
 *
 * @author kenny
 */
public class DayDto extends AbstractDto {

    public static final String DATE = "DayDate";
    public static final String START = "StartTime";
    public static final String END = "EndTime";
    
    
    public DayDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(DATE, null);
        fieldsMap.put(START, null);
        fieldsMap.put(END, null);
    }
    
    public Date getDate(){
        return (Date) fieldsMap.get(DATE);
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
    
    public ArrayList<SchedulePractitionerDto> getRooms(){
        return (ArrayList<SchedulePractitionerDto>) DataServiceImpl.GLOBAL_DATA_INSTANCE.getAllPractitionersForDay(this);
    }

}
