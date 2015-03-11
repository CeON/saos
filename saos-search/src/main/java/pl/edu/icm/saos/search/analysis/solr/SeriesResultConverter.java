package pl.edu.icm.saos.search.analysis.solr;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * @author madryk
 */
@Service
public class SeriesResultConverter {
    
    private XFieldFacetExtractor xFieldFacetExtractor;
    
    private FacetToSeriesConverter facetToSeriesConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    public Series<Object, Integer> convertToSeries(QueryResponse response, XField xField) {
        
        RangeFacet<?, ?> facet = xFieldFacetExtractor.extractFacet(response, xField);
        
        return facetToSeriesConverter.convert(facet, xField);
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
