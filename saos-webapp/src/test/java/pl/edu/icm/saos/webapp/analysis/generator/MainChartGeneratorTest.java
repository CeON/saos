package pl.edu.icm.saos.webapp.analysis.generator;

import static org.junit.Assert.assertTrue;
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
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.UixSettings;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings;
import pl.edu.icm.saos.webapp.analysis.request.converter.JudgmentSeriesFilterConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UixSettingsConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UiySettingsConverter;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class MainChartGeneratorTest {

    @InjectMocks
    private MainChartGenerator mainChartGenerator = new MainChartGenerator();
    
    @Mock private AnalysisService analysisService;
    
    @Mock private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter;
    
    @Mock private UixSettingsConverter uixSettingsConverter;
    
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
        mainChartGenerator.generateChart(null);
        
    }
    
    
    
    @Test
    public void generateChart() {
        
        // given
        
        
        JudgmentSeriesFilter filter1 = new JudgmentSeriesFilter();
        filter1.setPhrase("ABC");
        
        JudgmentSeriesFilter filter2 = new JudgmentSeriesFilter();
        filter1.setPhrase("DEF");
        
        
        List<JudgmentSeriesFilter> filters = Lists.newArrayList(filter1, filter2);
        UixSettings uiXSettings = new UixSettings();
        UiySettings uiYSettings = new UiySettings();
        
        AnalysisForm analysisForm = createAnalysisForm(filters, uiXSettings, uiYSettings);
        
        
        JudgmentSeriesCriteria criteria1 = new JudgmentSeriesCriteria();
        criteria1.setPhrase("ABC");
        
        JudgmentSeriesCriteria criteria2 = new JudgmentSeriesCriteria();
        criteria2.setPhrase("DEF");
        
        List<JudgmentSeriesCriteria> criteriaList = Lists.newArrayList(criteria1, criteria2);
        
        XSettings xsettings = new XSettings();
        YSettings ysettings = new YSettings();
        
        
        
        
        Chart<Object, Number> chart = createChart();
        FlotChart flotChart = createFlotChart();
        
        
        
        when(judgmentSeriesFilterConverter.convertList(filters)).thenReturn(criteriaList);
        when(uixSettingsConverter.convert(uiXSettings)).thenReturn(xsettings);
        when(uiySettingsConverter.convert(uiYSettings)).thenReturn(ysettings);
        when(analysisService.generateChart(criteriaList, xsettings, ysettings)).thenReturn(chart);
        when(flotChartConverter.convert(chart)).thenReturn(flotChart);
                
        
        // execute
        
        FlotChart generatedFlotChart = mainChartGenerator.generateChart(analysisForm);
        
        
        // assert
        
        assertTrue(flotChart == generatedFlotChart);
        verify(judgmentSeriesFilterConverter).convertList(filters);
        verify(uixSettingsConverter).convert(uiXSettings);
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


    private AnalysisForm createAnalysisForm(List<JudgmentSeriesFilter> filters, UixSettings uiXSettings,
            UiySettings uiYSettings) {
        
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.setFilters(filters);
        analysisForm.setXsettings(uiXSettings);
        analysisForm.setYsettings(uiYSettings);
        return analysisForm;
    }


    

    
}
