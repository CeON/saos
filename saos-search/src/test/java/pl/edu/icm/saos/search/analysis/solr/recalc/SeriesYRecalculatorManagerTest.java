package pl.edu.icm.saos.search.analysis.solr.recalc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SeriesYRecalculatorManagerTest {

    
    private SeriesYRecalculatorManager seriesYRecalculatorManager = new SeriesYRecalculatorManager();
 
    private SeriesYRecalculator seriesYRecalculator1 = mock(SeriesYRecalculator.class);
    private SeriesYRecalculator seriesYRecalculator2 = mock(SeriesYRecalculator.class);
    
    
    
    @Before
    public void before() {
    
        seriesYRecalculatorManager.setSeriesYRecalculators(Lists.newArrayList(seriesYRecalculator1, seriesYRecalculator2));
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void getSeriesYRecalculator() {
        
        // given
        YValueType yValueType1 = mock(YValueType.class);
        YValueType yValueType2 = mock(YValueType.class);
        
        when(seriesYRecalculator1.handles(yValueType1)).thenReturn(true);
        when(seriesYRecalculator1.handles(yValueType2)).thenReturn(false);
        when(seriesYRecalculator2.handles(yValueType1)).thenReturn(false);
        when(seriesYRecalculator2.handles(yValueType2)).thenReturn(true);
        
        // execute && assert
        assertTrue(seriesYRecalculator1 == seriesYRecalculatorManager.getSeriesYRecalculator(yValueType1));
        assertFalse(seriesYRecalculator1 == seriesYRecalculatorManager.getSeriesYRecalculator(yValueType2));
        assertTrue(seriesYRecalculator2 == seriesYRecalculatorManager.getSeriesYRecalculator(yValueType2));
        assertFalse(seriesYRecalculator2 == seriesYRecalculatorManager.getSeriesYRecalculator(yValueType1));
        
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void getSeriesYRecalculator_NoRecalculatorForValueType() {
        
        // given
        YValueType yValueType1 = mock(YValueType.class);
        
        when(seriesYRecalculator1.handles(yValueType1)).thenReturn(false);
        when(seriesYRecalculator2.handles(yValueType1)).thenReturn(false);
        
        // execute && assert
        seriesYRecalculatorManager.getSeriesYRecalculator(yValueType1);
        
    }
    
    
    
}
