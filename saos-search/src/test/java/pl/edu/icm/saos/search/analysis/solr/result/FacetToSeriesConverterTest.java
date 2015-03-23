package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.solr.client.solrj.response.RangeFacet;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.result.FacetToSeriesConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetValueConverter;
import pl.edu.icm.saos.search.analysis.solr.result.FacetValueConverterManager;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class FacetToSeriesConverterTest {

    private FacetToSeriesConverter facetToSeriesConverter = new FacetToSeriesConverter();
    
    private FacetValueConverterManager facetValueConverterManager = mock(FacetValueConverterManager.class);
    
    private XSettings xsettings = mock(XSettings.class);
    
    
    
    @Before
    public void setUp() {

        facetToSeriesConverter.setFacetValueConverterManager(facetValueConverterManager);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convert() {
        // given
        RangeFacet<?, ?> rangeFacet = new RangeFacet.Numeric(null, null, null, null, null, null, null);
        rangeFacet.addCount("AAA", 4);
        rangeFacet.addCount("EFGH", 1);
        
        FacetValueConverter facetValueConverter = mock(FacetValueConverter.class);
        when(facetValueConverterManager.getConverter(xsettings)).thenReturn(facetValueConverter);
        when(facetValueConverter.convert("AAA")).thenReturn("AAA_converted");
        when(facetValueConverter.convert("EFGH")).thenReturn("EFGH_converted");
        
        // execute
        Series<Object, Integer> series = facetToSeriesConverter.convert(rangeFacet, xsettings);
        
        // assert
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<>("AAA_converted", 4));
        expectedPoints.add(new Point<>("EFGH_converted", 1));
        
        assertEquals(expectedPoints, series.getPoints());
        
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL_FACET() {
    
        // execute
        facetToSeriesConverter.convert(null, xsettings);
    
    }
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL_XSETTINGS() {
    
        // execute
        facetToSeriesConverter.convert(new RangeFacet.Numeric(null, null, null, null, null, null, null), null);
    
    }
    
}
