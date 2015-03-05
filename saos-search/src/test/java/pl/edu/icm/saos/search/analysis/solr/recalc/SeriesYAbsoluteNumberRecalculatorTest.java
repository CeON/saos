package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Series;


/**
 * @author Łukasz Dumiszewski
 */

public class SeriesYAbsoluteNumberRecalculatorTest {

    
    private SeriesYAbsoluteNumberRecalculator recalculator = new SeriesYAbsoluteNumberRecalculator();
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void handles_AbsoluteNumberYValue() {
        
        // execute & assert
        assertTrue(recalculator.handles(new AbsoluteNumberYValue()));
        
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
        Series<Object, ? extends Number> recalcSeries = recalculator.recalculateSeries(series, new XSettings(), new YSettings());
        
        // assert
        assertTrue(series == recalcSeries);
    }
    
    
}
