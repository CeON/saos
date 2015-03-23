package pl.edu.icm.saos.webapp.analysis.request.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import pl.edu.icm.saos.webapp.analysis.request.UixMonthYearRange;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;


/**
 * @author Łukasz Dumiszewski
 */

public class UixMonthYearRangeConverterTest {

    @InjectMocks
    private UixMonthYearRangeConverter uixMonthYearRangeConverter;
    
    
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
    public void handles_NULL() {
        
        // execute
        
        uixMonthYearRangeConverter.handles(null);
        
    }
    
    
    @Test
    public void handles() {
        
        // execute & assert
        
        assertFalse(uixMonthYearRangeConverter.handles(UixRange.class));
        assertTrue(uixMonthYearRangeConverter.handles(UixMonthYearRange.class));
         
    }
    
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL() {
        
        // execute
        
        uixMonthYearRangeConverter.convert(null);
         
    }
    

    
    @Test(expected = IllegalArgumentException.class)
    public void convert_INCORRECT_RANGE() {
        
        // given
        
        UixMonthYearRange uixRange = new UixMonthYearRange();
        uixRange.setStartMonth(11);
        uixRange.setStartYear(2010);
        uixRange.setEndMonth(10);
        uixRange.setEndYear(2010);
        
        
        // execute
        
        uixMonthYearRangeConverter.convert(uixRange);
         
    }

    
    @Test
    public void convert() {
        
        // given
        
        UixMonthYearRange uixRange = new UixMonthYearRange();
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
        
        XDateRange xDateRange = uixMonthYearRangeConverter.convert(uixRange);

        
        // assert
        
        assertNotNull(xDateRange);
        assertEquals(startDate, xDateRange.getStartDate());
        assertEquals(endDate, xDateRange.getEndDate());
        assertEquals(gap, xDateRange.getGap());
        
    }
   
}
