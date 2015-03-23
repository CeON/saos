package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.SeriesGenerator;

/**
 * @author Łukasz Dumiszewski
 */

public class RateBaseSeriesGeneratorTest {

    private RateBaseSeriesGenerator rateBaseSeriesGenerator = new RateBaseSeriesGenerator();
    
    private SeriesGenerator seriesGenerator = mock(SeriesGenerator.class);
    
    
    
    @Before
    public void before() {
        
        rateBaseSeriesGenerator.setSeriesGenerator(seriesGenerator);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateRateBaseSeries() {
        
        // given
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.JUDGMENT_DATE);
        
        Series<Object, Integer> series = new Series<>();
        when(seriesGenerator.generateSeries(new JudgmentSeriesCriteria(), xsettings)).thenReturn(series);
        
        
        // execute
        Series<Object, Integer> rateBaseSeries = rateBaseSeriesGenerator.generateRateBaseSeries(xsettings);
        
        
        // assert
        assertTrue(rateBaseSeries == series);
        verify(seriesGenerator).generateSeries(new JudgmentSeriesCriteria(), xsettings);
        
    }
}
