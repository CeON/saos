package pl.edu.icm.saos.search.analysis.solr.result;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.solr.XFieldNameMapper;

import com.google.common.collect.Maps;

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
    public void extractFacetCounts_RANGE_FACET() {
        // given
        Map<String, Integer> facetResultsCount = Maps.newHashMap();
        facetResultsCount.put("key1", 1);
        facetResultsCount.put("key2", 5);
        QueryResponse response = createQueryResponseWithRangeFacet("judgmentDate", facetResultsCount);
        
        // execute
        List<FacetCount> facetCounts = xFieldFacetExtractor.extractFacetCounts(response, XField.JUDGMENT_DATE);
        
        // assert
        assertEquals(2, facetCounts.size());
        
        assertEquals("key1", facetCounts.get(0).getValue());
        assertEquals(1, facetCounts.get(0).getCount());
        
        assertEquals("key2", facetCounts.get(1).getValue());
        assertEquals(5, facetCounts.get(1).getCount());
    }
    
    @Test
    public void extractFacetCounts_FIELD_FACET() {
        // given
        Map<String, Integer> facetResultsCount = Maps.newHashMap();
        facetResultsCount.put("key1", 1);
        facetResultsCount.put("key2", 5);
        QueryResponse response = createQueryResponseWithFieldFacet("judgmentDate", facetResultsCount);
        
        // execute
        List<FacetCount> facetCounts = xFieldFacetExtractor.extractFacetCounts(response, XField.JUDGMENT_DATE);
        
        // assert
        assertEquals(2, facetCounts.size());
        
        assertEquals("key1", facetCounts.get(0).getValue());
        assertEquals(1, facetCounts.get(0).getCount());
        
        assertEquals("key2", facetCounts.get(1).getValue());
        assertEquals(5, facetCounts.get(1).getCount());
    }
    
    @Test(expected = RuntimeException.class)
    public void extractFacetCounts_NO_FACET() {
        // execute
        xFieldFacetExtractor.extractFacetCounts(new QueryResponse(), XField.JUDGMENT_DATE);
    }
    
    @Test(expected = NullPointerException.class)
    public void extractFacet_NULL_RESPONSE() {
        // execute
        xFieldFacetExtractor.extractFacetCounts(null, XField.JUDGMENT_DATE);
    }
    
    @Test(expected = NullPointerException.class)
    public void extractFacet_NULL_XFIELD() {
        // execute
        xFieldFacetExtractor.extractFacetCounts(new QueryResponse(), null);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private QueryResponse createQueryResponseWithRangeFacet(String fieldName, Map<String, Integer> facetResultsCount) {
        QueryResponse response = new QueryResponse();
        
        NamedList<Object> counts = createCounts(facetResultsCount);
        
        NamedList<Object> fieldNameNamedList = new NamedList<Object>();
        fieldNameNamedList.add(fieldName, counts);
        
        NamedList<Object> facetRanges = new NamedList<Object>();
        facetRanges.add("facet_ranges", fieldNameNamedList);
        
        NamedList<Object> facetCounts = new NamedList<Object>();
        facetCounts.add("facet_counts", facetRanges);
        
        response.setResponse(facetCounts);
        
        
        return response;
    }
    
    private QueryResponse createQueryResponseWithFieldFacet(String fieldName, Map<String, Integer> facetResultsCount) {
        QueryResponse response = new QueryResponse();
        
        NamedList<Object> counts = createCounts(facetResultsCount);
        
        NamedList<Object> fieldNameNamedList = counts;
        counts.setName(0, fieldName);
        
        NamedList<Object> facetFields = new NamedList<Object>();
        facetFields.add("facet_fields", fieldNameNamedList);
        
        NamedList<Object> facetCounts = new NamedList<Object>();
        facetCounts.add("facet_counts", facetFields);
        
        response.setResponse(facetCounts);
        
        
        return response;
    }
    
    private NamedList<Object> createCounts(Map<String, Integer> facetResultsCount) {
        NamedList<Integer> facetResult = new NamedList<Integer>();
        for (Map.Entry<String, Integer> countEntry : facetResultsCount.entrySet()) {
            facetResult.add(countEntry.getKey(), countEntry.getValue());
        }
        
        NamedList<Object> counts = new NamedList<Object>();
        counts.add("counts", facetResult);
        
        return counts;
    }
    
}
