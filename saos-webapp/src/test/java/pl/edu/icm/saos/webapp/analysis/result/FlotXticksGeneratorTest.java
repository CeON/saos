package pl.edu.icm.saos.webapp.analysis.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;

/**
 * @author Łukasz Dumiszewski
 */

public class FlotXticksGeneratorTest {

    
    private FlotXticksGenerator flotXticksGenerator = new FlotXticksGenerator();
    
   
    
    
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
        
        
        
        // execute 
        
        List<Object[]> xticks = flotXticksGenerator.generateXticks(chart);
        
        
        // assert
        
        assertNotNull(xticks);
        assertEquals(2, xticks.size());
        
        assertEquals(0, xticks.get(0)[0]);
        assertEquals("XYZ", xticks.get(0)[1]);

        assertEquals(1, xticks.get(1)[0]);
        assertEquals("ABC", xticks.get(1)[1]);
    }
    
    
}
