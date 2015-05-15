package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

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
    
    private JudgmentSeriesCriteriaCloner judgmentSeriesCriteriaCloner = mock(JudgmentSeriesCriteriaCloner.class);
    
    
    @Before
    public void before() {
        
        rateBaseSeriesGenerator.setSeriesGenerator(seriesGenerator);
        
        rateBaseSeriesGenerator.setJudgmentSeriesCriteriaCloner(judgmentSeriesCriteriaCloner);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateRateBaseSeries() {
        
        // given
        JudgmentSeriesCriteria seriesCriteria = mock(JudgmentSeriesCriteria.class);
        
        XSettings xsettings = new XSettings();
        xsettings.setField(XField.JUDGMENT_DATE);
        
        Series<Object, Integer> series = new Series<>();
        when(seriesGenerator.generateSeries(seriesCriteria, xsettings)).thenReturn(series);
        
        when(judgmentSeriesCriteriaCloner.clone(seriesCriteria)).thenReturn(seriesCriteria);
        
        // execute
        Series<Object, Integer> rateBaseSeries = rateBaseSeriesGenerator.generateRateBaseSeries(seriesCriteria, xsettings);
        
        
        // assert
        assertTrue(rateBaseSeries == series);
        
        InOrder inOrder = Mockito.inOrder(seriesCriteria, seriesGenerator);
        inOrder.verify(seriesCriteria).setPhrase(null);
        inOrder.verify(seriesGenerator).generateSeries(seriesCriteria, xsettings);
        Mockito.verifyNoMoreInteractions(seriesCriteria);
        Mockito.verifyNoMoreInteractions(seriesGenerator);
    }
}
