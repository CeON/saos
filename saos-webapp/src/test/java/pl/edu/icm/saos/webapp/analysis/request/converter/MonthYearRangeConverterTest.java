package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.webapp.analysis.request.MonthYearRange;


/**
 * @author Łukasz Dumiszewski
 */

public class MonthYearRangeConverterTest {

    @InjectMocks
    private MonthYearRangeConverter monthYearRangeConverter;
    
    
    @Mock
    private MonthYearStartDateCalculator monthYearStartDateCalculator;
    
    @Mock
    private MonthYearEndDateCalculator monthYearEndDateCalculator;
    
    @Mock
    private MonthYearRangeGapCalculator monthYearRangeGapCalculator;
    
    
    
    @Before 
    public void before() {
        
        initMocks(this);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL() {
        
        // execute
        
        monthYearRangeConverter.convert(null);
         
    }
    

    
    @Test(expected = IllegalArgumentException.class)
    public void convert_INCORRECT_RANGE() {
        
        // given
        
        MonthYearRange uixRange = new MonthYearRange();
        uixRange.setStartMonth(11);
        uixRange.setStartYear(2010);
        uixRange.setEndMonth(10);
        uixRange.setEndYear(2010);
        
        
        // execute
        
        monthYearRangeConverter.convert(uixRange);
         
    }

    
    @Test
    public void convert() {
        
        // given
        
        MonthYearRange uixRange = new MonthYearRange();
        uixRange.setStartMonth(1);
        uixRange.setStartYear(2010);
        uixRange.setEndMonth(10);
        uixRange.setEndYear(2015);
        
        LocalDate startDate = new LocalDate(uixRange.getStartYear(), uixRange.getStartMonth(), 1);
        when(monthYearStartDateCalculator.calculateStartDate(uixRange.getStartYear(), uixRange.getStartMonth())).thenReturn(startDate);
        
        LocalDate endDate = new LocalDate(uixRange.getEndYear(), uixRange.getEndMonth(), 30);
        when(monthYearEndDateCalculator.calculateEndDate(uixRange.getEndYear(), uixRange.getEndMonth())).thenReturn(endDate);

        Period gap = new Period(1, PeriodUnit.YEAR);
        when(monthYearRangeGapCalculator.calculateGap(uixRange.getStartYear(), uixRange.getStartMonth(), uixRange.getEndYear(), uixRange.getEndMonth())).thenReturn(gap);
        
        
        // execute
        
        XDateRange xDateRange = monthYearRangeConverter.convert(uixRange);

        
        // assert
        
        assertNotNull(xDateRange);
        assertEquals(startDate, xDateRange.getStartDate());
        assertEquals(endDate, xDateRange.getEndDate());
        assertEquals(gap, xDateRange.getGap());
        
    }
   
}
