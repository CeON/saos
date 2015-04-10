package pl.edu.icm.saos.webapp.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.webapp.analysis.generator.ChartAggregator;
import pl.edu.icm.saos.webapp.analysis.generator.ChartGenerator;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

/**
 * @author Łukasz Dumiszewski
 */

public class UiAnalysisServiceTest {

    
    @InjectMocks
    private UiAnalysisService uiAnalysisService = new UiAnalysisService();
    
    @Mock
    private ChartGenerator chartGenerator;
    
    @Mock
    private ChartAggregator chartAggregator;
    

    
    @Before
    public void before() {
        
        initMocks(this);
    
    }
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void generateCharts_NULL() {
        
        // execute
        uiAnalysisService.generateCharts(null);
        
    }
    
    @Test
    public void generateCharts() {
        
        // given
        
        AnalysisForm analysisForm = mock(AnalysisForm.class);
        UiySettings uiySettings = new UiySettings();
        UiyValueType valueType = UiyValueType.NUMBER;
        uiySettings.setValueType(valueType);
        when(analysisForm.getYsettings()).thenReturn(uiySettings);
        
        FlotChart mainChart = mock(FlotChart.class);
        when(chartGenerator.canGenerateChart(ChartCode.MAIN_CHART, analysisForm)).thenReturn(true);
        when(chartGenerator.generateChart(ChartCode.MAIN_CHART, analysisForm)).thenReturn(mainChart);
        when(chartGenerator.canGenerateChart(ChartCode.AGGREGATED_MAIN_CHART, analysisForm)).thenReturn(false);
        
        
        FlotChart aggregatedMainChart = mock(FlotChart.class);
        when(chartAggregator.aggregateChart(mainChart, valueType)).thenReturn(aggregatedMainChart);
        
        
        // execute
        
        Map<ChartCode, FlotChart> flotCharts = uiAnalysisService.generateCharts(analysisForm);
        
        
        // assert
        
        assertEquals(2, flotCharts.size());
        assertTrue(mainChart == flotCharts.get(ChartCode.MAIN_CHART));
        assertTrue(aggregatedMainChart == flotCharts.get(ChartCode.AGGREGATED_MAIN_CHART));
        for (ChartCode chartCode : ChartCode.values()) {
            verify(chartGenerator).canGenerateChart(chartCode, analysisForm);
        }
    }
    
    
    
    
    
}
