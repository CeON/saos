package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;

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
        List<FacetCount> facetCounts = Lists.newArrayList(new FacetCount("facetValue1", 3), new FacetCount("facetValue2", 4));
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        XSettings xsettings = mock(XSettings.class);
        when(xsettings.getField()).thenReturn(XField.JUDGMENT_DATE);
        
        doReturn(facetCounts).when(xFieldFacetExtractor).extractFacetCounts(response, XField.JUDGMENT_DATE);
        doReturn(series).when(facetToSeriesConverter).convert(facetCounts, xsettings);
        
        // execute
        Series<Object, Integer> retSeries = seriesResultsConverter.convert(response, xsettings);
        
        // assert
        assertTrue(retSeries == series);
        verify(xFieldFacetExtractor).extractFacetCounts(response, XField.JUDGMENT_DATE);
        verify(facetToSeriesConverter).convert(facetCounts, xsettings);
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
