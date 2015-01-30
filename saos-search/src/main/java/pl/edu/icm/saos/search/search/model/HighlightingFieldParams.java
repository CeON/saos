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

    /**
     * List of fields that will be taken into account when highlighting
     * field {@link #getFieldName()}
     * 
     * <br/>
     * For example:
     * For field 'field1' {@link #getHighlightFromFields()} returns 'field2' and 'field3'.
     * <br/>
     * When we perform search on field 'field2' then 'field1' will be highlighted<br/>
     * When we perform search on field 'field4' then 'field1' will not be highlighted
     * 
     * @return highlightFromFields
     */
    public List<String> getHighlightFromFields() {
        return highlightFromFields;
    }

    /**
     * Returns map containing values of highlighting parameters that will be
     * put on field {@link #getFieldName()}
     * 
     * <br/>
     * For example:
     * { "hl.snippets" : "4" } - defines maximum number of highlighted snippets
     * to generate for field {@link #getFieldName()}
     * 
     * <br/>
     * For complete list of available params check: http://wiki.apache.org/solr/HighlightingParameters
     * 
     * @return params
     */
    public Map<String, String> getParams() {
        return params;
    }
    
}
