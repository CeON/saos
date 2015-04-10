package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.AnalysisService;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings;
import pl.edu.icm.saos.webapp.analysis.request.converter.JudgmentSeriesFilterConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UiySettingsConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.XSettingsGenerator;
import pl.edu.icm.saos.webapp.analysis.request.converter.XSettingsGeneratorManager;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartGeneratorTest {

    @InjectMocks
    private ChartGenerator mainChartGenerator = new ChartGenerator();
    
    @Mock private AnalysisService analysisService;
    
    @Mock private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter;
    
    @Mock private XSettingsGeneratorManager xsettingsGeneratorManager;
    
    @Mock private UiySettingsConverter uiySettingsConverter;
    
    @Mock private ChartConverter flotChartConverter;
    
    
    @Before
    public void before() {
    
        initMocks(this);
       
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void generateChart_NullAnalysisForm() {
        
        // execute
        mainChartGenerator.generateChart(ChartCode.MAIN_CHART, null);
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void generateChart_NullChartCode() {
        
        // execute
        mainChartGenerator.generateChart(null, mock(AnalysisForm.class));
        
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void generateChart_ChartCodeNotGeneratedChart() {
        
        // execute
        mainChartGenerator.generateChart(ChartCode.AGGREGATED_MAIN_CHART, mock(AnalysisForm.class));
        
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void generateChart_cannotGenerate() {
        
        ChartCode chartCode = ChartCode.MAIN_CHART;
        AnalysisForm analysisForm = createAnalysisForm(mock(JudgmentGlobalFilter.class), Lists.newArrayList(), mock(UiySettings.class));
        
        XSettingsGenerator xsettingsGenerator = mock(XSettingsGenerator.class);

        when(xsettingsGeneratorManager.getXSettingsGenerator(chartCode)).thenReturn(xsettingsGenerator);
        when(xsettingsGenerator.canGenerateXSettings(analysisForm.getGlobalFilter())).thenReturn(false);
                
        
        // execute
        
        mainChartGenerator.generateChart(chartCode, analysisForm);
        
        
     
    }
    
    
    @Test
    public void generateChart() {
        
        // given
        
        
        JudgmentSeriesFilter filter1 = new JudgmentSeriesFilter();
        filter1.setPhrase("ABC");
        
        JudgmentSeriesFilter filter2 = new JudgmentSeriesFilter();
        filter1.setPhrase("DEF");
        
        List<JudgmentSeriesFilter> filters = Lists.newArrayList(filter1, filter2);
        
        JudgmentGlobalFilter globalFilter = mock(JudgmentGlobalFilter.class);
        
        UiySettings uiYSettings = new UiySettings();
        
        AnalysisForm analysisForm = createAnalysisForm(globalFilter, filters, uiYSettings);
        
        
        JudgmentSeriesCriteria criteria1 = new JudgmentSeriesCriteria();
        criteria1.setPhrase("ABC");
        
        JudgmentSeriesCriteria criteria2 = new JudgmentSeriesCriteria();
        criteria2.setPhrase("DEF");
        
        List<JudgmentSeriesCriteria> criteriaList = Lists.newArrayList(criteria1, criteria2);
        
        XSettings xsettings = new XSettings();
        YSettings ysettings = new YSettings();
        
        
        
        
        Chart<Object, Number> chart = createChart();
        FlotChart flotChart = createFlotChart();
        
        ChartCode chartCode = ChartCode.MAIN_CHART;
        
        XSettingsGenerator xsettingsGenerator = mock(XSettingsGenerator.class);

        when(judgmentSeriesFilterConverter.convertList(globalFilter, filters)).thenReturn(criteriaList);
        when(xsettingsGeneratorManager.getXSettingsGenerator(chartCode)).thenReturn(xsettingsGenerator);
        when(xsettingsGenerator.generateXSettings(analysisForm.getGlobalFilter())).thenReturn(xsettings);
        when(xsettingsGenerator.canGenerateXSettings(analysisForm.getGlobalFilter())).thenReturn(true);
        when(uiySettingsConverter.convert(uiYSettings)).thenReturn(ysettings);
        when(analysisService.generateChart(criteriaList, xsettings, ysettings)).thenReturn(chart);
        when(flotChartConverter.convert(chart)).thenReturn(flotChart);
                
        
        // execute
        
        FlotChart generatedFlotChart = mainChartGenerator.generateChart(chartCode, analysisForm);
        
        
        // assert
        
        assertTrue(flotChart == generatedFlotChart);
        verify(judgmentSeriesFilterConverter).convertList(globalFilter, filters);
        verify(xsettingsGeneratorManager, times(2)).getXSettingsGenerator(chartCode);
        verify(xsettingsGenerator).generateXSettings(globalFilter);
        verify(xsettingsGenerator).canGenerateXSettings(globalFilter);
        verify(uiySettingsConverter).convert(uiYSettings);
        verify(analysisService).generateChart(criteriaList, xsettings, ysettings);
        verify(flotChartConverter).convert(chart);
        
    }



    //------------------------ PRIVATE --------------------------

    private Chart<Object, Number> createChart() {
        Chart<Object, Number> chart = new Chart<>();
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("XYZ", 123);
        chart.addSeries(series1);
        return chart;
    }

    
    private FlotChart createFlotChart() {
        FlotChart flotChart = new FlotChart();
        FlotSeries flotSeries = new FlotSeries();
        flotSeries.addPoint(123, 34);
        flotChart.addSeries(flotSeries);
        return flotChart;
    }


    private AnalysisForm createAnalysisForm(JudgmentGlobalFilter globalFilter, List<JudgmentSeriesFilter> filters, UiySettings uiYSettings) {
        
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.setGlobalFilter(globalFilter);
        analysisForm.setSeriesFilters(filters);
        analysisForm.setYsettings(uiYSettings);
        return analysisForm;
    }


    

    
}
