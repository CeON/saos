package pl.edu.icm.saos.webapp.analysis.csv;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.assertj.core.util.Lists;
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

public class ChartCsvGeneratorTest {

    @InjectMocks
    private ChartCsvGenerator chartCsvGenerator;
    
    @Mock
    private PointValueFormatterManager pointValueFormatterManager;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void generateHeader_NULL_CHART() {
        
        // execute 
        chartCsvGenerator.generateHeader(null);
        
    }
    
    
    @Test
    public void generateHeader_NO_SERIES_IN_CHART() {
        
        // execute & assert
        assertEquals(0, chartCsvGenerator.generateHeader(new Chart<>()).length);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void generateHeader_FIRST_SERIES_NULL() {
        
        // given
        Chart<Object, Number> chart = new Chart<>();
        @SuppressWarnings("unchecked")
        List<Series<Object, Number>> seriesList = Lists.newArrayList((Series<Object, Number>)null);
        chart.setSeriesList(seriesList);
        
        
        // execute
        chartCsvGenerator.generateHeader(chart);
        
    }
    
    
    @Test
    public void generateHeader_NO_POINT_IN_SERIES() {
        
        // given
        Chart<Object, Number> chart = new Chart<>();
        chart.addSeries(new Series<>());
        
        
        // execute & assert
        assertEquals(0, chartCsvGenerator.generateHeader(new Chart<>()).length);
        
    }

    
    @Test
    public void generateHeader() {
        
        // given
        
        Chart<Object, Number> chart = new Chart<>();
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("XYZ", 123);
        series1.addPoint("ABC", 112);
        chart.addSeries(series1);
        Series<Object, Number> series2 = new Series<>();
        series2.addPoint("XYZ", 223);
        series2.addPoint("CXX", 22);
        chart.addSeries(series2);
        
        when(pointValueFormatterManager.format("XYZ")).thenReturn("xyz_f");
        when(pointValueFormatterManager.format("ABC")).thenReturn("abc_f");
        
        
        // execute 
        
        String[] header = chartCsvGenerator.generateHeader(chart);
        
        
        // assert
        
        assertNotNull(header);
        assertEquals(2, header.length);
        
        assertEquals("xyz_f", header[0]);
        assertEquals("abc_f", header[1]);
    }
    
    
    
    @Test(expected = NullPointerException.class)
    public void generateRow_Null() {
        
        // execute
        chartCsvGenerator.generateRow(null);
        
    }
    
    
    
    @Test
    public void generateRow() {
        
        // given
        
        Series<Object, Number> series = new Series<>();
        series.addPoint("XYZ", 123);
        series.addPoint("ABC", 112);
        
        when(pointValueFormatterManager.format(123)).thenReturn("123.00");
        when(pointValueFormatterManager.format(112)).thenReturn("112.00");
        
        
        // execute 
        
        String[] row = chartCsvGenerator.generateRow(series);
        
        
        // assert
        
        assertNotNull(row);
        assertEquals(2, row.length);
        
        assertEquals("123.00", row[0]);
        assertEquals("112.00", row[1]);
    }
    
    
}
