/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.DataTransferObjects;

import java.util.HashMap;

/**
 *
 * @author kenny
 */
public class TypeDto extends AbstractDto {
    
    public static final String TYPE_ID = "TypeID";
    public static final String TYPE_NAME = "TypeName";
    
    public TypeDto() {
        fieldsMap = new HashMap<String, Object>();
        fieldsMap.put(TYPE_ID, null);
        fieldsMap.put(TYPE_NAME, null);
    }
    
    public Integer getTypeID(){
        return (Integer) fieldsMap.get(TYPE_ID);
    }
    
    public String getTypeName(){
        return (String) fieldsMap.get(TYPE_NAME);
    }
}
