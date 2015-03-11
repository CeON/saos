package pl.edu.icm.saos.search.analysis.solr;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.util.NamedList;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;

/**
 * @author madryk
 */
public class XFieldFacetExtractorTest {

    private XFieldFacetExtractor xFieldFacetExtractor = new XFieldFacetExtractor();
    
    private XFieldNameMapper xFieldNameMapper = mock(XFieldNameMapper.class);
    
    
    @Before
    public void setUp() {
        xFieldFacetExtractor.setxFieldNameMapper(xFieldNameMapper);
        
        when(xFieldNameMapper.mapXField(XField.JUDGMENT_DATE)).thenReturn("judgmentDate");
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractFacet() {
        // given
        Map<String, Integer> facetResultsCount = Maps.newHashMap();
        facetResultsCount.put("key1", 1);
        facetResultsCount.put("key2", 5);
        QueryResponse response = createQueryResponse("judgmentDate", facetResultsCount);
        
        // execute
        RangeFacet<?, ?> facet = xFieldFacetExtractor.extractFacet(response, XField.JUDGMENT_DATE);
        
        // assert
        assertEquals(2, facet.getCounts());
        
        assertEquals("key1", facet.getCounts().get(0).getValue());
        assertEquals(1, facet.getCounts().get(0).getCount());
        
        assertEquals("key2", facet.getCounts().get(1).getValue());
        assertEquals(5, facet.getCounts().get(1).getCount());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private QueryResponse createQueryResponse(String fieldName, Map<String, Integer> facetResultsCount) {
        QueryResponse response = new QueryResponse();
        
        NamedList<Integer> facetResult = new NamedList<Integer>();
        for (Map.Entry<String, Integer> countEntry : facetResultsCount.entrySet()) {
            facetResult.add(countEntry.getKey(), countEntry.getValue());
        }
        
        NamedList<Object> counts = new NamedList<Object>();
        counts.add("counts", facetResult);
        
        NamedList<Object> fieldNameNamedList = new NamedList<Object>();
        fieldNameNamedList.add(fieldName, counts);
        
        NamedList<Object> facetRanges = new NamedList<Object>();
        facetRanges.add("facet_ranges", fieldNameNamedList);
        
        NamedList<Object> facetCounts = new NamedList<Object>();
        facetCounts.add("facet_counts", facetRanges);
        
        response.setResponse(facetCounts);
        
        
        return response;
    }
    
}
