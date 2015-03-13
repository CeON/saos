package pl.edu.icm.saos.search.analysis.solr;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Mapper of {@link XField} to solr field name
 * 
 * @author madryk
 */
@Service
public class XFieldNameMapper {

    private Map<XField, String> fieldNamesMappings = Maps.newHashMap();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns mapped {@link XField} to solr field name
     * 
     * @throws IllegalArgumentException if no mapping was found
     */
    public String mapXField(XField xField) {
        
        Preconditions.checkNotNull(xField);
        
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
