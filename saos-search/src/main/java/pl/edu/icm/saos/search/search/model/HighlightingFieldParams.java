package pl.edu.icm.saos.search.search.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Collection of highlighting parameters that should be applied to query
 * for field {@link #fieldName}
 * @author madryk
 */
public class HighlightingFieldParams {

    private String fieldName;
    
    private List<String> highlightFromFields = Lists.newLinkedList();
    
    private Map<String, String> params = Maps.newHashMap();

    
    public HighlightingFieldParams(String fieldName) {
        this.fieldName = fieldName;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addHighlightFromField(String fieldName) {
        highlightFromFields.add(fieldName);
    }
    
    public void addParam(String param, String value) {
        params.put(param, value);
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public String getFieldName() {
        return fieldName;
    }

    public List<String> getHighlightFromFields() {
        return highlightFromFields;
    }

    public Map<String, String> getParams() {
        return params;
    }
    
}
