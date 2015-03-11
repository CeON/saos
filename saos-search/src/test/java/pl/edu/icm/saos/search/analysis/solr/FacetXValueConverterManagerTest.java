package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.collect.ImmutableMap;

/**
 * @author madryk
 */
public class FacetXValueConverterManagerTest {

    private FacetXValueConverterManager facetXValueConverterManager = new FacetXValueConverterManager();
    
    @Test
    public void getXValueConverter_FOUND() {
        // given
        Function<String, Object> converter = x -> x;
        facetXValueConverterManager.setxConvertStrategy(ImmutableMap.of(XField.JUDGMENT_DATE, converter));
        
        // execute
        Function<String, Object> retConverter = facetXValueConverterManager.getXValueConverter(XField.JUDGMENT_DATE);
        
        // assert
        assertTrue(retConverter == converter);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void getXValueConverter_NOT_FOUND() {
        // execute
        facetXValueConverterManager.getXValueConverter(XField.JUDGMENT_DATE);
    }
}
