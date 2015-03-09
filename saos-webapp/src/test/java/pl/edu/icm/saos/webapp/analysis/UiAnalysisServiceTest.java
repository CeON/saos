package pl.edu.icm.saos.webapp.analysis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.edu.icm.saos.search.analysis.AnalysisService;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Chart;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilterConverter;
import pl.edu.icm.saos.webapp.analysis.request.UiXSettings;
import pl.edu.icm.saos.webapp.analysis.request.UiXSettingsConverter;
import pl.edu.icm.saos.webapp.analysis.request.UiYSettings;
import pl.edu.icm.saos.webapp.analysis.request.UiYSettingsConverter;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.UiChart;
import pl.edu.icm.saos.webapp.analysis.result.UiChart.UiSeries;

/**
 * @author Łukasz Dumiszewski
 */

public class UiAnalysisServiceTest {

    private UiAnalysisService uiAnalysisService = new UiAnalysisService();
    
    @Mock private AnalysisService analysisService;
    
    @Mock private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter;
    
    @Mock private UiXSettingsConverter uiXSettingsConverter;
    
    @Mock private UiYSettingsConverter uiYSettingsConverter;
    
    @Mock private ChartConverter chartConverter;
    
    
    @Before
    public void before() {
    
        initMocks(this);
        
        uiAnalysisService.setAnalysisService(analysisService);
        uiAnalysisService.setJudgmentSeriesFilterConverter(judgmentSeriesFilterConverter);
        uiAnalysisService.setUiXSettingsConverter(uiXSettingsConverter);
        uiAnalysisService.setUiYSettingsConverter(uiYSettingsConverter);
        uiAnalysisService.setChartConverter(chartConverter);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void generateChart_NullAnalysisForm() {
        
        // execute
        uiAnalysisService.generateChart(null);
        
    }
    
    
    
    @Test
    public void generateChart() {
        
        // given
        
        
        JudgmentSeriesFilter filter1 = new JudgmentSeriesFilter();
        filter1.setPhrase("ABC");
        
        JudgmentSeriesFilter filter2 = new JudgmentSeriesFilter();
        filter1.setPhrase("DEF");
        
        
        List<JudgmentSeriesFilter> filters = Lists.newArrayList(filter1, filter2);
        UiXSettings uiXSettings = new UiXSettings();
        UiYSettings uiYSettings = new UiYSettings();
        
        AnalysisForm analysisForm = createAnalysisForm(filters, uiXSettings, uiYSettings);
        
        
        JudgmentSeriesCriteria criteria1 = new JudgmentSeriesCriteria();
        criteria1.setPhrase("ABC");
        
        JudgmentSeriesCriteria criteria2 = new JudgmentSeriesCriteria();
        criteria2.setPhrase("DEF");
        
        List<JudgmentSeriesCriteria> criteriaList = Lists.newArrayList(criteria1, criteria2);
        
        XSettings xsettings = new XSettings();
        YSettings ysettings = new YSettings();
        
        
        
        
        Chart<Object, Number> chart = createChart();
        UiChart uiChart = createUiChart();
        
        
        
        when(judgmentSeriesFilterConverter.convertList(filters)).thenReturn(criteriaList);
        when(uiXSettingsConverter.convert(uiXSettings)).thenReturn(xsettings);
        when(uiYSettingsConverter.convert(uiYSettings)).thenReturn(ysettings);
        when(analysisService.generateChart(criteriaList, xsettings, ysettings)).thenReturn(chart);
        when(chartConverter.convert(chart)).thenReturn(uiChart);
                
        
        // execute
        
        UiChart generatedUiChart = uiAnalysisService.generateChart(analysisForm);
        
        
        // assert
        
        assertEquals(uiChart, generatedUiChart);
        verify(judgmentSeriesFilterConverter).convertList(filters);
        verify(uiXSettingsConverter).convert(uiXSettings);
        verify(uiYSettingsConverter).convert(uiYSettings);
        verify(analysisService).generateChart(criteriaList, xsettings, ysettings);
        verify(chartConverter).convert(chart);
        
    }



    //------------------------ PRIVATE --------------------------

    private Chart<Object, Number> createChart() {
        Chart<Object, Number> chart = new Chart<>();
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("XYZ", 123);
        chart.addSeries(series1);
        return chart;
    }

    
    private UiChart createUiChart() {
        UiChart uiChart = new UiChart();
        UiSeries uiSeries = new UiSeries();
        uiSeries.addPoint("XYZ", "123");
        uiChart.addSeries(uiSeries);
        return uiChart;
    }


    private AnalysisForm createAnalysisForm(List<JudgmentSeriesFilter> filters, UiXSettings uiXSettings,
            UiYSettings uiYSettings) {
        
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.setFilters(filters);
        analysisForm.setUiXSettings(uiXSettings);
        analysisForm.setUiYSettings(uiYSettings);
        return analysisForm;
    }


    

    
}
