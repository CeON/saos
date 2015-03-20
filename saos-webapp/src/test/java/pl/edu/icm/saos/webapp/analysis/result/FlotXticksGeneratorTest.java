package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;

/**
 * @author Łukasz Dumiszewski
 */

public class FlotXticksGeneratorTest {

    @InjectMocks
    private FlotXticksGenerator flotXticksGenerator;
    
    @Mock
    private PointValueFormatterManager pointValueFormatterManager;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void generateXticks_NULL_CHART() {
        
        // execute 
        flotXticksGenerator.generateXticks(null);
        
    }
    
    
    @Test
    public void generateXticks_NO_SERIES_IN_CHART() {
        
        // execute & assert
        assertEquals(0, flotXticksGenerator.generateXticks(new Chart<>()).size());
        
    }
    
    
    @Test
    public void generateXticks_NO_POINT_IN_SERIES() {
        
        // given
        Chart<Object, Number> chart = new Chart<>();
        chart.addSeries(new Series<>());
        
        
        // execute & assert
        assertEquals(0, flotXticksGenerator.generateXticks(new Chart<>()).size());
        
    }

    
    @Test
    public void generateXticks() {
        
        // given
        
        Chart<Object, Number> chart = new Chart<>();
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("XYZ", 123);
        series1.addPoint("ABC", 112);
        chart.addSeries(series1);
        Series<Object, Number> series2 = new Series<>();
        series2.addPoint("XYZ", 223);
        chart.addSeries(series2);
        
        when(pointValueFormatterManager.format("XYZ")).thenReturn("xyz_f");
        when(pointValueFormatterManager.format("ABC")).thenReturn("abc_f");
        
        
        // execute 
        
        List<Object[]> xticks = flotXticksGenerator.generateXticks(chart);
        
        
        // assert
        
        assertNotNull(xticks);
        assertEquals(2, xticks.size());
        
        assertEquals(0, xticks.get(0)[0]);
        assertEquals("xyz_f", xticks.get(0)[1]);

        assertEquals(1, xticks.get(1)[0]);
        assertEquals("abc_f", xticks.get(1)[1]);
    }
    
    
}
