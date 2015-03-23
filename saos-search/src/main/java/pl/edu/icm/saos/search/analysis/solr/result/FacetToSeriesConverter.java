package pl.edu.icm.saos.search.analysis.solr.result;

import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.client.solrj.response.RangeFacet.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.base.Preconditions;

/**
 * Converts {@link RangeFacet} to {@link Series}
 * 
 * @author madryk
 */
@Service
public class FacetToSeriesConverter {

    private FacetValueConverterManager facetValueConverterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    public Series<Object, Integer> convert(RangeFacet<?, ?> facet, XSettings xsettings) {
        
        Preconditions.checkNotNull(facet);
        Preconditions.checkNotNull(xsettings);
        
        FacetValueConverter xValueConverter = facetValueConverterManager.getConverter(xsettings);
        
        
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        for (Count count : facet.getCounts()) {
            
            series.addPoint(xValueConverter.convert(count.getValue()), count.getCount());
        
        }
        
        return series;
    }


    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFacetValueConverterManager(FacetValueConverterManager facetValueConverterManager) {
        this.facetValueConverterManager = facetValueConverterManager;
    }
    
}
