package pl.edu.icm.saos.search.analysis.solr;

import java.util.function.Function;

import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.RangeFacet.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * Converts {@link RangeFacet} to {@link Series}
 * 
 * @author madryk
 */
@Service
public class FacetToSeriesConverter {

    private FacetXValueConverterManager facetXValueConverterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    public Series<Object, Integer> convert(RangeFacet<?, ?> facet, XField xField) {
        
        Preconditions.checkNotNull(facet);
        Preconditions.checkNotNull(xField);
        
        Function<String, Object> xConverter = facetXValueConverterManager.getXValueConverter(xField);
        
        Series<Object, Integer> series = new Series<Object, Integer>();
        for (Count count : facet.getCounts()) {
            series.addPoint(xConverter.apply(count.getValue()), count.getCount());
        }
        
        return series;
    }
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFacetXValueConverterManager(FacetXValueConverterManager facetXValueConverterManager) {
        this.facetXValueConverterManager = facetXValueConverterManager;
    }
}
