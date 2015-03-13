package pl.edu.icm.saos.search.analysis.solr;

import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * Manager of converting solr facets to X values.
 * 
 * @author madryk
 */
@Service
public class FacetXValueConverterManager {

    private Map<XField, Function<String, Object>> xConvertStrategy = Maps.newHashMap();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Picks appropriate function for {@link XField} converting solr facet value
     * 
     * @throws IllegalArgumentException if no appropriate converting function was found
     */
    public Function<String, Object> getXValueConverter(XField xField) {
        
        Preconditions.checkNotNull(xField);
        
        Function<String, Object> xConverter = xConvertStrategy.get(xField);
        if (xConverter == null) {
            throw new IllegalArgumentException("No conversion strategy found for " + xField.name());
        }
        
        return xConverter;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Resource
    public void setxConvertStrategy(Map<XField, Function<String, Object>> xConvertStrategy) {
        this.xConvertStrategy = xConvertStrategy;
    }
}
