package DataTransferObjects;

import java.sql.Date;
import java.util.HashMap;

public class NoShowDto extends AbstractDto {

    public static final String NOSHOW_ID = "NoShowID";
    public static final String PATIENT_ID = "PatID";
    public static final String DATE = "Date";

    public NoShowDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(NOSHOW_ID, null);
        fieldsMap.put(PATIENT_ID, null);
        fieldsMap.put(DATE, null);
    }

    public Integer getNoShowID() {
        return (Integer) getField(NOSHOW_ID);
    }

    public Integer getPatID() {
        return (Integer) getField(PATIENT_ID);
    }

    public Date getDate() {
        return (Date) getField(DATE);
    }

    public NoShowDto setNoShowID(int noShowID) {
        fieldsMap.put(NOSHOW_ID, noShowID);
        return this;
    }

    public NoShowDto setPatID(int patID) {
        fieldsMap.put(PATIENT_ID, patID);
        return this;
    }

    public NoShowDto setDate(Date date) {
        fieldsMap.put(DATE, date);
        return this;
    }
}
