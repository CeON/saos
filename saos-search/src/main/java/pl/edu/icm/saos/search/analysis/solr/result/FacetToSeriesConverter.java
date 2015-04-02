package pl.edu.icm.saos.search.analysis.solr.result;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.base.Preconditions;

/**
 * Converts list of {@link FacetCount}s to {@link Series}
 * 
 * @author madryk
 */
@Service
public class FacetToSeriesConverter {

    private FacetValueConverterManager facetValueConverterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given facet counts to Series
     */
    public Series<Object, Integer> convert(List<FacetCount> facetCounts, XSettings xsettings) {
        
        Preconditions.checkNotNull(facetCounts);
        Preconditions.checkNotNull(xsettings);
        
        FacetValueConverter xValueConverter = facetValueConverterManager.getConverter(xsettings);
        
        
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        for (FacetCount count : facetCounts) {
            
            series.addPoint(xValueConverter.convert(count.getValue(), xsettings), count.getCount());
        
        }
        
        return series;
    }


    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFacetValueConverterManager(FacetValueConverterManager facetValueConverterManager) {
        this.facetValueConverterManager = facetValueConverterManager;
    }
    
}
