package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;
import pl.edu.icm.saos.search.analysis.result.Point;
import pl.edu.icm.saos.search.analysis.result.Series;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("seriesYRateRecalculator")
public class SeriesYRateRecalculator implements SeriesYRecalculator {

    
    private RateBaseSeriesGenerator rateBaseSeriesGenerator;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public <X> Series<X, ? extends Number> recalculateSeries(Series<X, Integer> series, XSettings xsettings, YSettings ysettings) {
        
        RateYValue rateYValue = (RateYValue)ysettings.getValueType();
        
        Series<X, Integer> baseSeries = rateBaseSeriesGenerator.generateRateBaseSeries(xsettings);
        
        return recalculateSeries(series, baseSeries, rateYValue.getRateRatio());
        
    }




    @Override
    public boolean handles(YValueType yValueType) {
        Preconditions.checkNotNull(yValueType);
        return yValueType.getClass().equals(RateYValue.class);
    
    }

   

    
    //------------------------ PRIVATE --------------------------
 
    <X> Series<X, Float> recalculateSeries(Series<X, Integer> series, Series<X, Integer> baseSeries, int rateRatio) {
        
        Preconditions.checkArgument(series.getPoints().equals(baseSeries.getPoints()));
        
        Series<X, Float> rateSeries = new Series<>();
        for (int i = 0; i < series.getPoints().size(); i++) {
        
            Point<X, Integer> aPoint = baseSeries.getPoints().get(i);
            Point<X, Integer> sPoint = series.getPoints().get(i);
            
            float newY = calcRate(sPoint.getY(), aPoint.getY(), rateRatio);
            Point<X, Float> recPoint = new Point<>(sPoint.getX(), newY);  
            
            rateSeries.addPoint(recPoint);
        }
        return rateSeries;
    } 
    
    private float calcRate(int value, int baseValue, int rateRatio) {
        return ((float)((long)value * rateRatio)) / baseValue; 
    }


   

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRateBaseSeriesGenerator(RateBaseSeriesGenerator rateBaseSeriesGenerator) {
        this.rateBaseSeriesGenerator = rateBaseSeriesGenerator;
    }






    
}
