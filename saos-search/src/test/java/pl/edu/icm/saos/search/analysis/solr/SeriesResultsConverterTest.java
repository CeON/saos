package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;

import com.google.common.collect.Maps;

/**
 * @author madryk
 */
public class SeriesResultsConverterTest {

    private SeriesResultsConverter seriesResultsConverter = new SeriesResultsConverter();
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = IllegalArgumentException.class)
    public void convertToSeries_NO_FIELD_MAPPING() {
        // given
        seriesResultsConverter.addXConvertStrategy(XField.JUDGMENT_DATE, (x -> SearchDateTimeUtils.convertISOStringToDate(x)));
        
        QueryResponse response = createQueryResponse("judgmentDate", Maps.newHashMap());
        
        // execute
        seriesResultsConverter.convertToSeries(response, XField.JUDGMENT_DATE);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void convertToSeries_NO_CONVERTING_STRATEGY() {
        // given
        seriesResultsConverter.addFieldNameMapping(XField.JUDGMENT_DATE, "judgmentDate");
        
        QueryResponse response = createQueryResponse("judgmentDate", Maps.newHashMap());
        
        // execute
        seriesResultsConverter.convertToSeries(response, XField.JUDGMENT_DATE);
    }
    
    @Test
    public void convertToSeries_DATE() {
        // given
        seriesResultsConverter.addFieldNameMapping(XField.JUDGMENT_DATE, "judgmentDate");
        seriesResultsConverter.addXConvertStrategy(XField.JUDGMENT_DATE, (x -> SearchDateTimeUtils.convertISOStringToDate(x)));
        
        Map<String, Integer> facetResults = Maps.newHashMap();
        facetResults.put("2005-03-01T00:00:00Z", 4);
        facetResults.put("2005-04-01T00:00:00Z", 6);
        QueryResponse response = createQueryResponse("judgmentDate", facetResults);
        
        // execute
        Series<Object, Integer> series = seriesResultsConverter.convertToSeries(response, XField.JUDGMENT_DATE);
        
        // assert
        assertEquals(2, series.getPoints().size());
        
        assertEquals(new LocalDate(2005, 3, 1), series.getPoints().get(0).getX());
        assertEquals(Integer.valueOf(4), series.getPoints().get(0).getY());
        
        assertEquals(new LocalDate(2005, 4, 1), series.getPoints().get(1).getX());
        assertEquals(Integer.valueOf(6), series.getPoints().get(1).getY());
    }
    
    @Test
    public void convertToSeries_NUMERIC() {
        // given
        seriesResultsConverter.addFieldNameMapping(XField.JUDGMENT_DATE, "judgmentDate");
        seriesResultsConverter.addXConvertStrategy(XField.JUDGMENT_DATE, (x -> Integer.valueOf(x)));
        
        Map<String, Integer> facetResults = Maps.newHashMap();
        facetResults.put("15", 4);
        facetResults.put("16", 6);
        QueryResponse response = createQueryResponse("judgmentDate", facetResults);
        
        // execute
        Series<Object, Integer> series = seriesResultsConverter.convertToSeries(response, XField.JUDGMENT_DATE);
        
        // assert
        assertEquals(2, series.getPoints().size());
        
        assertEquals(15, series.getPoints().get(0).getX());
        assertEquals(Integer.valueOf(4), series.getPoints().get(0).getY());
        
        assertEquals(16, series.getPoints().get(1).getX());
        assertEquals(Integer.valueOf(6), series.getPoints().get(1).getY());
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
