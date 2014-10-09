package pl.edu.icm.saos.search.search.model;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Collection of highlighting parameters that should be applied to query
 * for field ${@link #fieldName}
 * @author madryk
 */
public class HighlightingFieldParams {

    private String fieldName;
    
    private Map<String, String> params = Maps.newHashMap();

    
    public HighlightingFieldParams(String fieldName) {
        this.fieldName = fieldName;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addParam(String param, String value) {
        params.put(param, value);
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public String getFieldName() {
        return fieldName;
    }

    public Map<String, String> getParams() {
        return params;
    }
    
}
