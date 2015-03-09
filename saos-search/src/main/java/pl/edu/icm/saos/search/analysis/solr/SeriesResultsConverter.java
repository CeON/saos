package pl.edu.icm.saos.search.analysis.solr;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.RangeFacet.Count;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * @author madryk
 */
@Service
public class SeriesResultsConverter {

    private Map<XField, Function<String, Object>> xConvertStrategy = Maps.newHashMap();
    
    private Map<XField, String> fieldNamesMappings = Maps.newHashMap();
    
    
    //------------------------ LOGIC --------------------------
    
    public Series<Object, Integer> convertToSeries(QueryResponse response, XField xField) {
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        String fieldName = fieldNamesMappings.get(xField);
        if (fieldName == null) {
            throw new IllegalArgumentException("No field mapping found for " + xField.name());
        }
        
        RangeFacet<?, ?> judgmentDateFacets = response.getFacetRanges()
                .stream()
                .filter(rf -> fieldName.equals(rf.getName()))
                .findFirst().get();
        
        List<Count> counts = judgmentDateFacets.getCounts();
        Function<String, Object> xConverter = xConvertStrategy.get(xField);
        if (xConverter == null) {
            throw new IllegalArgumentException("No conversion strategy found for " + xField.name());
        }
        
        for (Count count : counts) {
            series.addPoint(xConverter.apply(count.getValue()), count.getCount());
        }
        
        return series;
    }
    
    public void addXConvertStrategy(XField xField, Function<String, Object> convertFunction) {
        xConvertStrategy.put(xField, convertFunction);
    }
    
    public void addFieldNameMapping(XField xField, String fieldName) {
        fieldNamesMappings.put(xField, fieldName);
    }
    
    
    //------------------------ SETTERS --------------------------

    @Resource
    public void setxConvertStrategy(Map<XField, Function<String, Object>> xConvertStrategy) {
        this.xConvertStrategy = xConvertStrategy;
    }

    @Resource
    public void setFieldNamesMappings(Map<XField, String> fieldNamesMappings) {
        this.fieldNamesMappings = fieldNamesMappings;
    }
    
    
}
