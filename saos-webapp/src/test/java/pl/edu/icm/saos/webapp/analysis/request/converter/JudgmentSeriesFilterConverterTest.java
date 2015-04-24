package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.MonthYearRange;
import pl.edu.icm.saos.webapp.common.search.CourtCriteria;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesFilterConverterTest {

    
    @InjectMocks
    private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter = new JudgmentSeriesFilterConverter();
    
    @Mock
    private MonthYearStartDateCalculator monthYearStartDateCalculator;
    
    @Mock
    private MonthYearEndDateCalculator monthYearEndDateCalculator;
    
    
    private JudgmentSeriesFilter seriesFilter1;
    private JudgmentSeriesFilter seriesFilter2;
    private JudgmentGlobalFilter globalFilter;
    private LocalDate startJudgmentDate = new LocalDate(2000, 12, 12);
    private LocalDate endJudgmentDate = new LocalDate(2001, 2, 1);
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        seriesFilter1 = createSeriesFilter();
        seriesFilter2 = createSeriesFilter();
        globalFilter = createGlobalFilter();
        
        when(monthYearStartDateCalculator.calculateStartDate(globalFilter.getJudgmentDateRange().getStartYear(), globalFilter.getJudgmentDateRange().getStartMonth())).thenReturn(startJudgmentDate);
        when(monthYearEndDateCalculator.calculateEndDate(globalFilter.getJudgmentDateRange().getEndYear(), globalFilter.getJudgmentDateRange().getEndMonth())).thenReturn(endJudgmentDate);
        
    }

    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NullJudgmentGlobalFilter() {
        
        // execute
        judgmentSeriesFilterConverter.convert(null, mock(JudgmentSeriesFilter.class));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NullJudgmentSeriesFilter() {
        
        // execute
        judgmentSeriesFilterConverter.convert(mock(JudgmentGlobalFilter.class), null);
        
    }
    

    @Test
    public void convert() {
        
        
        // execute
        JudgmentSeriesCriteria criteria = judgmentSeriesFilterConverter.convert(globalFilter, seriesFilter1);
        
        // assert
        assertNotNull(criteria);
        assertCriteria(seriesFilter1, criteria);
    }

    
    @Test
    public void convertList() {
        
        // given
        List<JudgmentSeriesFilter> filters = Lists.newArrayList(seriesFilter1, seriesFilter2);
        
        // execute
        List<JudgmentSeriesCriteria> criteriaList = judgmentSeriesFilterConverter.convertList(globalFilter, filters);
        
        // assert
        assertNotNull(criteriaList);
        assertEquals(filters.size(), criteriaList.size());
        IntStream.range(0, filters.size()).forEach(i->assertCriteria(filters.get(i), criteriaList.get(i)));
        
    }


    
    
    //------------------------ PRIVATE --------------------------
    

    private void assertCriteria(JudgmentSeriesFilter filter, JudgmentSeriesCriteria criteria) {
        assertEquals(filter.getPhrase(), criteria.getPhrase());
        assertEquals(startJudgmentDate, criteria.getStartJudgmentDate());
        assertEquals(endJudgmentDate, criteria.getEndJudgmentDate());
        
        CourtCriteria courtCriteria = globalFilter.getCourtCriteria();
        assertEquals(courtCriteria.getCourtType(), criteria.getCourtType());
        assertEquals(courtCriteria.getCcCourtId(), criteria.getCcCourtId());
        assertEquals(courtCriteria.getCcCourtDivisionId(), criteria.getCcCourtDivisionId());
        assertEquals(courtCriteria.getScCourtChamberId(), criteria.getScCourtChamberId());
        assertEquals(courtCriteria.getScCourtChamberDivisionId(), criteria.getScCourtChamberDivisionId());
    }

    
    private JudgmentSeriesFilter createSeriesFilter() {
        JudgmentSeriesFilter filter = new JudgmentSeriesFilter();
        filter.setPhrase(RandomStringUtils.randomAlphabetic(10));
        return filter;
    }
    
    private JudgmentGlobalFilter createGlobalFilter() {
        JudgmentGlobalFilter filter = new JudgmentGlobalFilter();
        MonthYearRange monthYearRange = new MonthYearRange();
        monthYearRange.setStartYear(RandomUtils.nextInt(1990, 2020));
        monthYearRange.setStartMonth(RandomUtils.nextInt(1, 12));
        monthYearRange.setEndYear(monthYearRange.getEndYear() + RandomUtils.nextInt(1, 20));
        monthYearRange.setEndMonth(RandomUtils.nextInt(1, 12));
        filter.setJudgmentDateRange(monthYearRange);
        
        CourtCriteria courtCriteria = filter.getCourtCriteria();
        courtCriteria.setCourtType(CourtType.COMMON);
        courtCriteria.setCcCourtId(10L);
        courtCriteria.setCcCourtDivisionId(100L);
        courtCriteria.setScCourtChamberId(1090L);
        courtCriteria.setScCourtChamberDivisionId(109L);
        
        return filter;
    }

    
}
