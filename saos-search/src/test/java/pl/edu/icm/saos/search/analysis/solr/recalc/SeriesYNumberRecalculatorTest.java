package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.NumberYValue;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;


/**
 * @author Łukasz Dumiszewski
 */

public class SeriesYNumberRecalculatorTest {

    
    private SeriesYNumberRecalculator recalculator = new SeriesYNumberRecalculator();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void handles_AbsoluteNumberYValue() {
        
        // execute & assert
        assertTrue(recalculator.handles(new NumberYValue()));
        
    }
    
    
    @Test
    public void handles_OtherValueType() {
        
        // execute & assert
        assertFalse(recalculator.handles(new RateYValue(1)));
        
    }
    
    
    @Test
    public void recalculateSeries() {
        
        // given
        Series<Object, Integer> series = new Series<>();
        
        // execute
        Series<Object, ? extends Number> recalcSeries = recalculator.recalculateSeries(series, new JudgmentSeriesCriteria(), new XSettings(), new YSettings());
        
        // assert
        assertTrue(series == recalcSeries);
    }
    
    
}
