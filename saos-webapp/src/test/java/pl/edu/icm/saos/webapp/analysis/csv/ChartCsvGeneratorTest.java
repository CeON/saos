package pl.edu.icm.saos.webapp.analysis.csv;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class ChartCsvGeneratorTest {

    @InjectMocks
    private ChartCsvGenerator chartCsvGenerator = new ChartCsvGenerator();
    
    @Mock
    private PointValueFormatterManager pointValueFormatterManager;


    private AnalysisForm analysisForm = new AnalysisForm();
    
    private Chart<Object, Number> chart = new Chart<>();
    
    @Before
    public void setup() {
        
        JudgmentSeriesFilter seriesFilter1 = new JudgmentSeriesFilter();
        seriesFilter1.setPhrase("phrase1");
        analysisForm.addSeriesFilter(seriesFilter1);
        
        JudgmentSeriesFilter seriesFilter2 = new JudgmentSeriesFilter();
        seriesFilter2.setPhrase("");
        analysisForm.addSeriesFilter(seriesFilter2);
        
        
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("x1", 1);
        series1.addPoint("x2", 2);
        chart.addSeries(series1);
        
        Series<Object, Number> series2 = new Series<>();
        series2.addPoint("x1", 5);
        series2.addPoint("x2", 6);
        chart.addSeries(series2);
        
        when(pointValueFormatterManager.format("x1")).thenReturn("x1");
        when(pointValueFormatterManager.format("x2")).thenReturn("x2");
        
        when(pointValueFormatterManager.format(1)).thenReturn("1");
        when(pointValueFormatterManager.format(2)).thenReturn("2");
        
        when(pointValueFormatterManager.format(5)).thenReturn("5");
        when(pointValueFormatterManager.format(6)).thenReturn("6");
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void generateHeader_NULL_CHART_CODE() {
        
        // execute
        chartCsvGenerator.generateHeader(null, analysisForm);
    }
    
    @Test(expected = NullPointerException.class)
    public void generateHeader_NULL_ANALYSIS_FORM() {
        
        // execute
        chartCsvGenerator.generateHeader(ChartCode.MAIN_CHART, null);
    }
    
    @Test
    public void generateHeader_MAIN_CHART() {
        
        // execute
        
        String[] header = chartCsvGenerator.generateHeader(ChartCode.MAIN_CHART, analysisForm);
        
        // assert
        
        assertEquals(3, header.length);
        assertEquals("Period", header[0]);
        assertEquals("JudgmentCount (phrase1)", header[1]);
        assertEquals("JudgmentCount", header[2]);
    }
    
    @Test
    public void generateHeader_CC_COURT_CHART() {
        
        // execute
        
        String[] header = chartCsvGenerator.generateHeader(ChartCode.CC_COURT_CHART, analysisForm);
        
        // assert
        
        assertEquals(3, header.length);
        assertEquals("Court", header[0]);
        assertEquals("JudgmentCount (phrase1)", header[1]);
        assertEquals("JudgmentCount", header[2]);
    }
    
    @Test
    public void generateHeader_PERCENT() {
        
        // given
        
        analysisForm.getYsettings().setValueType(UiyValueType.PERCENT);
        
        // execute
        
        String[] header = chartCsvGenerator.generateHeader(ChartCode.MAIN_CHART, analysisForm);
        
        // assert
        
        assertEquals(3, header.length);
        assertEquals("Period", header[0]);
        assertEquals("JudgmentPercent (phrase1)", header[1]);
        assertEquals("JudgmentPercent", header[2]);
    }
    
    @Test
    public void generateHeader_NUMBER_PER_1000() {
        
        // given
        
        analysisForm.getYsettings().setValueType(UiyValueType.NUMBER_PER_1000);
        
        // execute
        
        String[] header = chartCsvGenerator.generateHeader(ChartCode.MAIN_CHART, analysisForm);
        
        // assert
        
        assertEquals(3, header.length);
        assertEquals("Period", header[0]);
        assertEquals("JudgmentPer1000Judgments (phrase1)", header[1]);
        assertEquals("JudgmentPer1000Judgments", header[2]);
    }
    
    @Test(expected = NullPointerException.class)
    public void generateRow_NULL_CHART() {
        
        // execute
        chartCsvGenerator.generateRow(null, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void generateRow_NEGATIVE_ROW_NUMBER() {

        // execute
        chartCsvGenerator.generateRow(chart, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void generateRow_TOO_BIG_ROW_NUMBER() {

        // execute
        chartCsvGenerator.generateRow(chart, 2);
    }
    
    @Test
    public void generateRow() {
        
        // execute
        
        String[] row = chartCsvGenerator.generateRow(chart, 0);
        
        // assert
        
        assertEquals(3, row.length);
        assertEquals("x1", row[0]);
        assertEquals("1", row[1]);
        assertEquals("5", row[2]);
    }
    
}
