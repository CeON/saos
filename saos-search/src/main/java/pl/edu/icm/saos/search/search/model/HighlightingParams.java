package pl.edu.icm.saos.search.search.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Collection of highlighting parameters that should be applied to query
 * @author madryk
 */
public class HighlightingParams {

    private Map<String, String> params = Maps.newHashMap();
    
    private List<HighlightingFieldParams> fieldsParams = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    public void addParam(String param, String value) {
        params.put(param, value);
    }
    
    public void addFieldParams(HighlightingFieldParams fieldParams) {
        fieldsParams.add(fieldParams);
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public Map<String, String> getParams() {
        return params;
    }
    
    public List<HighlightingFieldParams> getFieldsParams() {
        return fieldsParams;
    }
    
}
