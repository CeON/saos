package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.solr.client.solrj.response.RangeFacet;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Point;
import pl.edu.icm.saos.search.analysis.result.Series;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class FacetToSeriesConverterTest {

    private FacetToSeriesConverter facetToSeriesConverter = new FacetToSeriesConverter();
    
    private FacetXValueConverterManager facetXValueConverterManager = mock(FacetXValueConverterManager.class);
    
    @Before
    public void setUp() {
        facetToSeriesConverter.setFacetXValueConverterManager(facetXValueConverterManager);
        
        when(facetXValueConverterManager.getXValueConverter(XField.JUDGMENT_DATE)).thenReturn(x -> x + "_converted");
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convert() {
        // given
        RangeFacet<?, ?> rangeFacet = new RangeFacet.Numeric(null, null, null, null, null, null, null);
        rangeFacet.addCount("AAA", 4);
        rangeFacet.addCount("EFGH", 1);
        
        // execute
        Series<Object, Integer> series = facetToSeriesConverter.convert(rangeFacet, XField.JUDGMENT_DATE);
        
        // assert
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<>("AAA_converted", 4));
        expectedPoints.add(new Point<>("EFGH_converted", 1));
        
        assertEquals(expectedPoints, series.getPoints());
    }
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL_RANGE_FACET() {
        // execute
        facetToSeriesConverter.convert(null, XField.JUDGMENT_DATE);
    }
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL_XFIELD() {
        // execute
        facetToSeriesConverter.convert(new RangeFacet.Numeric(null, null, null, null, null, null, null), null);
    }
    
}
