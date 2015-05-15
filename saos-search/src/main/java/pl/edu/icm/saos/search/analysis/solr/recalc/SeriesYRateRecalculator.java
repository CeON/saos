package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.RateYValue;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("seriesYRateRecalculator")
public class SeriesYRateRecalculator implements SeriesYRecalculator {

    
    private RateBaseSeriesGenerator rateBaseSeriesGenerator;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public Series<Object, ? extends Number> recalculateSeries(Series<Object, Integer> series, JudgmentSeriesCriteria criteria, XSettings xsettings, YSettings ysettings) {
        
        RateYValue rateYValue = (RateYValue)ysettings.getValueType();
        
        Preconditions.checkNotNull(rateYValue);
        
        Series<Object, Integer> baseSeries = rateBaseSeriesGenerator.generateRateBaseSeries(criteria, xsettings);
        
        return recalculateSeries(series, baseSeries, rateYValue.getRateRatio());
        
    }




    @Override
    public boolean handles(YValueType yValueType) {
        Preconditions.checkNotNull(yValueType);
        return yValueType.getClass().equals(RateYValue.class);
    
    }

   

    
    //------------------------ PRIVATE --------------------------
 
    <X> Series<X, Float> recalculateSeries(Series<X, Integer> series, Series<X, Integer> baseSeries, int rateRatio) {
        
        Preconditions.checkArgument(series.getPoints().size() == baseSeries.getPoints().size());
        
        Series<X, Float> rateSeries = new Series<>();
        for (int i = 0; i < series.getPoints().size(); i++) {
        
            Point<X, Integer> bPoint = baseSeries.getPoints().get(i);
            Point<X, Integer> sPoint = series.getPoints().get(i);
            
            float newY = calcRate(sPoint.getY(), bPoint.getY(), rateRatio);
            Point<X, Float> recPoint = new Point<>(sPoint.getX(), newY);  
            
            rateSeries.addPoint(recPoint);
        }
        return rateSeries;
    } 
    
    private float calcRate(int value, int baseValue, int rateRatio) {
        if (baseValue == 0) {
            return 0;
        }
        return ((float)((long)value * rateRatio)) / baseValue; 
    }


   

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRateBaseSeriesGenerator(RateBaseSeriesGenerator rateBaseSeriesGenerator) {
        this.rateBaseSeriesGenerator = rateBaseSeriesGenerator;
    }






    
}
