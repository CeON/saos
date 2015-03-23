package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.base.Preconditions;

/**
 * A {@link Series} to {@link FlotSeries} converter.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotSeriesConverter")
public class SeriesConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Series} into {@link FlotSeries} 
     */
    public FlotSeries convert(Series<?, Number> series) {
        
        Preconditions.checkNotNull(series);
        
        FlotSeries flotSeries = new FlotSeries();
        
        for (int i=0; i<series.getPoints().size(); i++) {
            
            Point<?, Number> point = series.getPoints().get(i);
            
            flotSeries.addPoint(i, point.getY());
            
        }
        
        return flotSeries;
        
    }

    
    //------------------------ SETTERS --------------------------
    
  
    
}
