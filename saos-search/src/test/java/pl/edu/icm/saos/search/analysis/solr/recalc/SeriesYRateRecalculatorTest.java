package pl.edu.icm.saos.search.analysis.solr.recalc;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.AbsoluteNumberYValue;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;


/**
 * @author Łukasz Dumiszewski
 */

public class SeriesYRateRecalculatorTest {

    
    private SeriesYRateRecalculator recalculator = new SeriesYRateRecalculator();
    
    private RateBaseSeriesGenerator rateBaseSeriesGenerator = mock(RateBaseSeriesGenerator.class);
    
    
    @Before
    public void before() {
        
        recalculator.setRateBaseSeriesGenerator(rateBaseSeriesGenerator);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void handles_RateYValue() {
        
        // execute & assert
        assertTrue(recalculator.handles(new RateYValue(2)));
        
    }
    
    
    @Test
    public void handles_OtherValueType() {
        
        // execute & assert
        assertFalse(recalculator.handles(new AbsoluteNumberYValue()));
        
    }
    
    
    @Test(expected = NullPointerException.class)
    public void recalculateSeries_NullRateYValue() {
    
        // given
        Series<Object, Integer> series = new Series<>();
        
        // execute
        recalculator.recalculateSeries(series, new XSettings(), new YSettings());
        
        
    }
    
    
    @Test
    public void recalculateSeries() {
        
        // given
        Series<Object, Integer> series = new Series<>();
        series.addPoint("A", 123);
        series.addPoint("B", 30);
        
        Series<Object, Integer> baseSeries = new Series<>();
        baseSeries.addPoint("A", 12343);
        baseSeries.addPoint("B", 267);
        
        XSettings xsettings = mock(XSettings.class);
        
        YSettings ysettings = new YSettings();
        ysettings.setValueType(new RateYValue(1000));
        
        when(rateBaseSeriesGenerator.generateRateBaseSeries(xsettings)).thenReturn(baseSeries);
        
        
        // execute
        Series<Object, ? extends Number> recalcSeries = recalculator.recalculateSeries(series, xsettings, ysettings);
        
        
        // assert
        assertRecalcPoint(recalcSeries.getPoints().get(0), "A", 9.965f);  // 123*1000/12343
        assertRecalcPoint(recalcSeries.getPoints().get(1), "B", 112.36f);
    }


    //------------------------ PRIVATE --------------------------
    
    private void assertRecalcPoint(Point<Object, ? extends Number> recalcPoint, String newXValue, float newYValue) {
        assertEquals(newXValue, recalcPoint.getX());
        assertTrue(Math.abs(recalcPoint.getY().floatValue() - newYValue) < 0.001);
    }
    
    
}
