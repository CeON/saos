package pl.edu.icm.saos.search.analysis;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartServiceTest {

    
    private ChartService chartService = new ChartService();
    
    private SeriesService seriesService = mock(SeriesService.class); 
    
    
    private XSettings xsettings = mock(XSettings.class);
    private YSettings ysettings = mock(YSettings.class);
    
    
    @Before
    public void before() {
        
        chartService.setSeriesService(seriesService);
        
    }
    
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected = NullPointerException.class)
    public void generateChart_NullCriteriaList() {
        
        // execute
        chartService.generateChart(null, xsettings, ysettings);
        
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void generateChart_EmptyCriteriaList() {
        
        // execute
        chartService.generateChart(new ArrayList<>(), xsettings, ysettings);
        
    }

    
    
    @Test
    public void generateChart() {
        
        // given
        
        JudgmentSeriesCriteria criteria1 = mock(JudgmentSeriesCriteria.class);
        JudgmentSeriesCriteria criteria2 = mock(JudgmentSeriesCriteria.class);
        List<JudgmentSeriesCriteria> criteriaList = Lists.newArrayList(criteria1, criteria2);
        
        Series<Object, Number> series1 = new Series<>();
        Series<Object, Number> series2 = new Series<>();
        
        when(seriesService.generateSeries(criteria1, xsettings, ysettings)).thenReturn(series1);
        when(seriesService.generateSeries(criteria2, xsettings, ysettings)).thenReturn(series2);
        
        // execute
        Chart<Object, Number> chart = chartService.generateChart(criteriaList, xsettings, ysettings);
     
        // assert
        verify(seriesService).generateSeries(criteria1, xsettings, ysettings);
        verify(seriesService).generateSeries(criteria2, xsettings, ysettings);
        assertTrue(chart.getSeriesList().get(0) == series1);
        assertTrue(chart.getSeriesList().get(1) == series2);
    }

    
}
