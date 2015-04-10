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
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.request.MonthYearRange;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * @author Łukasz Dumiszewski
 */

public class MainChartXSettingsGeneratorTest {

    @InjectMocks
    private MainChartXSettingsGenerator xsettingsGenerator = new MainChartXSettingsGenerator();
    
    @Mock
    private MonthYearRangeConverter monthYearRangeConverter;
    
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void handles_NULL() {
        
        // execute
        xsettingsGenerator.handles(null);
        
    }

    
    @Test
    public void handles_FALSE() {
        
        // execute
        xsettingsGenerator.handles(ChartCode.AGGREGATED_MAIN_CHART);
        
    }

    
    @Test
    public void handles_TRUE() {
        
        // execute
        xsettingsGenerator.handles(ChartCode.MAIN_CHART);
        
    }

    
    @Test(expected = NullPointerException.class)
    public void canGenerateXSettings_NULL() {
        
        // execute
        xsettingsGenerator.canGenerateXSettings(null);
        
    }
    
    
    @Test
    public void canGenerateXSettings_FALSE() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        
        // execute
        xsettingsGenerator.canGenerateXSettings(globalFilter);
        
    }
    
    
    @Test
    public void canGenerateXSettings_TRUE() {
        
        // given
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        globalFilter.setJudgmentDateRange(new MonthYearRange());
        
        // execute
        xsettingsGenerator.canGenerateXSettings(globalFilter);
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void generateXSettings_NULL() {
        
        // execute
        xsettingsGenerator.generateXSettings(null);
        
    }
    
    
    @Test
    public void generateXSettings() {
     
        // given
        
        JudgmentGlobalFilter globalFilter = new JudgmentGlobalFilter();
        MonthYearRange monthYearRange = new MonthYearRange();
        globalFilter.setJudgmentDateRange(monthYearRange);
        
        XDateRange xDateRange = new XDateRange(new LocalDate(2000, 1, 1), new LocalDate(2011, 12, 31), new Period(2, PeriodUnit.MONTH));
        when(monthYearRangeConverter.convert(monthYearRange)).thenReturn(xDateRange);
        
        
        // execute
        
        XSettings xsettings = xsettingsGenerator.generateXSettings(globalFilter);
        
        
        // assert
        
        assertNotNull(xsettings);
        assertEquals(xDateRange, xsettings.getRange());
        assertEquals(XField.JUDGMENT_DATE, xsettings.getField());
        
    }
}
