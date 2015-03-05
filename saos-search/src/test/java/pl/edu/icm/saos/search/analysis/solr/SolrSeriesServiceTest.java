package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.analysis.solr.recalc.SeriesYRecalculator;
import pl.edu.icm.saos.search.analysis.solr.recalc.SeriesYRecalculatorManager;

/**
 * @author Łukasz Dumiszewski
 */

public class SolrSeriesServiceTest {

    private SolrSeriesService seriesService = new SolrSeriesService();
    
    @Mock private SeriesGenerator seriesGenerator;
    
    @Mock private SeriesYRecalculatorManager seriesYRecalculatorManager;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        seriesService.setSeriesGenerator(seriesGenerator);
        
        seriesService.setSeriesYRecalculatorManager(seriesYRecalculatorManager);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected = NullPointerException.class)
    public void generateSeries_NullCriteria() {
        
        // execute
        seriesService.generateSeries(null, mock(XSettings.class), mock(YSettings.class));
    }
    
    
    @Test(expected = NullPointerException.class)
    public void generateSeries_NullXSettings() {
        
        // execute
        seriesService.generateSeries(mock(JudgmentSeriesCriteria.class), null, mock(YSettings.class));
    }
    
    
    @Test(expected = NullPointerException.class)
    public void generateSeries_NullYSettings() {
        
        // execute
        seriesService.generateSeries(mock(JudgmentSeriesCriteria.class), mock(XSettings.class), null);
    }
    
    
    @Test
    public void generateSeries() {
        
        // given
        JudgmentSeriesCriteria criteria = mock(JudgmentSeriesCriteria.class);
        XSettings xsettings = mock(XSettings.class);
        YSettings ysettings = mock(YSettings.class);
        
        when(ysettings.getValueType()).thenReturn(mock(YValueType.class));
        
        Series<Object, Integer> series = new Series<>();
        series.addPoint("X", 123);
        when(seriesGenerator.generateSeries(criteria, xsettings)).thenReturn(series);
        
        SeriesYRecalculator seriesYRecalculator = mock(SeriesYRecalculator.class);
        when(seriesYRecalculatorManager.getSeriesYRecalculator(ysettings.getValueType())).thenReturn(seriesYRecalculator);
        
        Series<Object, Number> recalcSeries = new Series<>();
        @SuppressWarnings("unchecked")
        Series<Object, Number> recalculateSeries = (Series<Object, Number>)seriesYRecalculator.recalculateSeries(series, xsettings, ysettings);
        when(recalculateSeries).thenReturn(recalcSeries);
        
        // execute
        Series<Object, Number> generatedSeries = seriesService.generateSeries(criteria, xsettings, ysettings);
        
        // assert
        assertTrue(recalcSeries == generatedSeries);
        verify(seriesGenerator).generateSeries(criteria, xsettings);
        verify(seriesYRecalculatorManager).getSeriesYRecalculator(ysettings.getValueType());
        verify(seriesYRecalculator).recalculateSeries(series, xsettings, ysettings);
        
        
        
    }
}
