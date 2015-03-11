package pl.edu.icm.saos.search.analysis.solr;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.collect.Maps;

/**
 * @author madryk
 */
@Service
public class XFieldNameMapper {

    private Map<XField, String> fieldNamesMappings = Maps.newHashMap();
    
    
    //------------------------ LOGIC --------------------------
    
    public String mapXField(XField xField) {
        String fieldName = fieldNamesMappings.get(xField);
        
        if (fieldName == null) {
            throw new IllegalArgumentException("No field mapping found for " + xField.name());
        }
        
        return fieldName;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Resource
    public void setFieldNamesMappings(Map<XField, String> fieldNamesMappings) {
        this.fieldNamesMappings = fieldNamesMappings;
    }
}
