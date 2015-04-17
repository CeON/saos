package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

/**
 * @author Łukasz Dumiszewski
 */

public class FlotChartGeneratorTest {

    @InjectMocks
    private FlotChartGenerator flotChartGenerator = new FlotChartGenerator();
    
    @Mock
    private ChartGenerator chartGenerator;
    
    @Mock
    private ChartConverter chartConverter;
    
    
    private ChartCode chartCode = ChartCode.MAIN_CHART;
    
    private AnalysisForm analysisForm = Mockito.mock(AnalysisForm.class);
    
    
    @Before
    public void before() {
        
        initMocks(this);
    
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void canGenerateChart_FALSE() {
        
        // given
        when(chartGenerator.canGenerateChart(chartCode, analysisForm)).thenReturn(false);
        
        // execute & assert
        assertFalse(flotChartGenerator.canGenerateChart(chartCode, analysisForm));
        
    }
    

    @Test
    public void canGenerateChart_TRUE() {
        
        // given
        when(chartGenerator.canGenerateChart(chartCode, analysisForm)).thenReturn(true);
        
        // execute & assert
        assertTrue(flotChartGenerator.canGenerateChart(chartCode, analysisForm));
        
    }
    
    
    @Test
    public void generateFlotChart_TRUE() {
        
        // given
        
        @SuppressWarnings("unchecked")
        Chart<Object, Number> chart = mock(Chart.class);
        when(chartGenerator.generateChart(chartCode, analysisForm)).thenReturn(chart);
        
        FlotChart flotChart = mock(FlotChart.class);
        when(chartConverter.convert(chart)).thenReturn(flotChart);
        
        
        // execute
        
        FlotChart generatedFlotChart = flotChartGenerator.generateFlotChart(chartCode, analysisForm);
        
        
        // assert
        
        assertTrue(flotChart == generatedFlotChart);
        verify(chartGenerator).generateChart(chartCode, analysisForm);
        verify(chartConverter).convert(chart);
        verifyNoMoreInteractions(chartGenerator, chartConverter);
        
    }
   

}
