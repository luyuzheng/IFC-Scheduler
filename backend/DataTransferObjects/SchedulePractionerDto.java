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
public class SchedulePractionerDto extends AbstractDto {
    public static final String PRACT_SCHED_ID = "PractSchID";
    public static final String PRACT = "Pract";
    public static final String START = "StartTime";
    public static final String END = "EndTime";
    public static final String DATE = "ScheduleDate";
    
    public SchedulePractionerDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(PRACT, null);
        fieldsMap.put(PRACT_SCHED_ID, null);
        fieldsMap.put(START, null);
        fieldsMap.put(END, null);
        fieldsMap.put(DATE, null);
    }
    
    public PractionerDto getPractioner(){
        return (PractionerDto) fieldsMap.get(PRACT);
    }
    
    public Integer getPractSchedID(){
        return (Integer) fieldsMap.get(PRACT_SCHED_ID);
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
    
    public Date getDate(){
        return (Date) fieldsMap.get(DATE);
    }
}
