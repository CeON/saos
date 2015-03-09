package pl.edu.icm.saos.webapp.analysis.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang.RandomStringUtils;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesFilterConverterTest {

    
    private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter = new JudgmentSeriesFilterConverter();
    
    
    private JudgmentSeriesFilter filter1;
    private JudgmentSeriesFilter filter2;
    
    
    @Before
    public void before() {
        
        filter1 = createFilter();
        filter2 = createFilter();
        
    }

    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_Null() {
        
        // execute
        judgmentSeriesFilterConverter.convert(null);
        
    }
    

    @Test
    public void convert() {
        
        // execute
        JudgmentSeriesCriteria criteria = judgmentSeriesFilterConverter.convert(filter1);
        
        // assert
        assertNotNull(filter1);
        assertCriteria(filter1, criteria);
        
    }

    
    @Test
    public void convertList() {
        
        // given
        List<JudgmentSeriesFilter> filters = Lists.newArrayList(filter1, filter2);
        
        // execute
        List<JudgmentSeriesCriteria> criteriaList = judgmentSeriesFilterConverter.convertList(filters);
        
        // assert
        assertNotNull(criteriaList);
        assertEquals(filters.size(), criteriaList.size());
        IntStream.range(0, filters.size()).forEach(i->assertCriteria(filters.get(i), criteriaList.get(i)));
        
    }


    
    
    //------------------------ PRIVATE --------------------------
    

    private void assertCriteria(JudgmentSeriesFilter filter, JudgmentSeriesCriteria criteria) {
        assertEquals(filter.getPhrase(), criteria.getPhrase());
    }

    
    private JudgmentSeriesFilter createFilter() {
        JudgmentSeriesFilter filter = new JudgmentSeriesFilter();
        filter.setPhrase(RandomStringUtils.randomAlphabetic(10));
        return filter;
    }

    
}
