package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Series;

/**
 * @author madryk
 */
public class SeriesResultConverterTest {

    private SeriesResultConverter seriesResultsConverter = new SeriesResultConverter();
    
    
    private XFieldFacetExtractor xFieldFacetExtractor = mock(XFieldFacetExtractor.class);
    
    private FacetToSeriesConverter facetToSeriesConverter = mock(FacetToSeriesConverter.class);
    
    
    @Before
    public void setUp() {
        seriesResultsConverter.setxFieldFacetExtractor(xFieldFacetExtractor);
        seriesResultsConverter.setFacetToSeriesConverter(facetToSeriesConverter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convertToSeries() {
        // given
        QueryResponse response = new QueryResponse();
        RangeFacet<?, ?> rangeFacet = new RangeFacet.Numeric(null, null, null, null, null, null, null);
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        doReturn(rangeFacet).when(xFieldFacetExtractor).extractFacet(response, XField.JUDGMENT_DATE);
        doReturn(series).when(facetToSeriesConverter).convert(rangeFacet, XField.JUDGMENT_DATE);
        
        // execute
        Series<Object, Integer> retSeries = seriesResultsConverter.convertToSeries(response, XField.JUDGMENT_DATE);
        
        // assert
        assertTrue(retSeries == series);
        verify(xFieldFacetExtractor).extractFacet(response, XField.JUDGMENT_DATE);
        verify(facetToSeriesConverter).convert(rangeFacet, XField.JUDGMENT_DATE);
    }
    
    @Test(expected = NullPointerException.class)
    public void convertToSeries_NULL_RESPONSE() {
        // expected
        seriesResultsConverter.convertToSeries(null, XField.JUDGMENT_DATE);
    }
    
    @Test(expected = NullPointerException.class)
    public void convertToSeries_NULL_XFIELD() {
        // expected
        seriesResultsConverter.convertToSeries(new QueryResponse(), null);
    }
    
}
