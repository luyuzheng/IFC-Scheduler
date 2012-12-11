package backend.DataTransferObjects;

import java.util.Map;
import java.util.Map.Entry;

public abstract class AbstractDto {

    protected Map<String, Object> fieldsMap;
    
    public Object getField(String key) {
        return fieldsMap.get(key);
    }
    
    public boolean setField(String key, Object value) {
        if (fieldsMap.containsKey(key)) {
            fieldsMap.put(key, value);
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        String str = "";
        for (Entry<String, Object> entry : fieldsMap.entrySet()) {
            str = str + entry.getKey() + " = " + entry.getValue() + "\n";
        }
        return str;
    }

    @Override
    public boolean equals(Object other) {
    	for (Entry<String, Object> entry : fieldsMap.entrySet()) {
    		if (((AbstractDto) other).getField(entry.getKey()) == null) {
    			if (entry.getValue() != null) {
    				return false;
    			}
    		}  else if (entry.getValue() == null) {
    			if (((AbstractDto) other).getField(entry.getKey()) != null) {
    				return false;
    			}
    		} else if (!((AbstractDto) other).getField(entry.getKey()).equals(entry.getValue())) {
            	return false;
            }
        }
    	return true;
    }
}
