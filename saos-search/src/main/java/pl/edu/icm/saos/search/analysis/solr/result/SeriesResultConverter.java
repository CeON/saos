package pl.edu.icm.saos.search.analysis.solr.result;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.base.Preconditions;

/**
 * Converter of solr {@link QueryResponse} into {@link Series}
 * 
 * @author madryk
 */
@Service
public class SeriesResultConverter {
    
    private XFieldFacetExtractor xFieldFacetExtractor;
    
    private FacetToSeriesConverter facetToSeriesConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts solr {@link QueryResponse} to {@link Series}
     */
    public Series<Object, Integer> convert(QueryResponse response, XSettings xsettings) {
        
        Preconditions.checkNotNull(response);
        Preconditions.checkNotNull(xsettings);
        
        RangeFacet<?, ?> facet = xFieldFacetExtractor.extractFacet(response, xsettings.getField());
        
        return facetToSeriesConverter.convert(facet, xsettings);
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setxFieldFacetExtractor(XFieldFacetExtractor xFieldFacetExtractor) {
        this.xFieldFacetExtractor = xFieldFacetExtractor;
    }

    @Autowired
    public void setFacetToSeriesConverter(FacetToSeriesConverter facetToSeriesConverter) {
        this.facetToSeriesConverter = facetToSeriesConverter;
    }
    
    
}
