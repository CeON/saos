package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.result.FacetToSeriesConverter;
import pl.edu.icm.saos.search.analysis.solr.result.SeriesResultConverter;
import pl.edu.icm.saos.search.analysis.solr.result.XFieldFacetExtractor;

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
        
        XSettings xsettings = mock(XSettings.class);
        when(xsettings.getField()).thenReturn(XField.JUDGMENT_DATE);
        
        doReturn(rangeFacet).when(xFieldFacetExtractor).extractFacet(response, XField.JUDGMENT_DATE);
        doReturn(series).when(facetToSeriesConverter).convert(rangeFacet, xsettings);
        
        // execute
        Series<Object, Integer> retSeries = seriesResultsConverter.convert(response, xsettings);
        
        // assert
        assertTrue(retSeries == series);
        verify(xFieldFacetExtractor).extractFacet(response, XField.JUDGMENT_DATE);
        verify(facetToSeriesConverter).convert(rangeFacet, xsettings);
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void convertToSeries_NULL_RESPONSE() {
        
        // expected
        seriesResultsConverter.convert(null, mock(XSettings.class));
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void convertToSeries_NULL_XSETTINGS() {
    
        // expected
        seriesResultsConverter.convert(new QueryResponse(), null);
        
    }
    
}
