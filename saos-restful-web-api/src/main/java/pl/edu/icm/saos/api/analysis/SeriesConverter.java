package pl.edu.icm.saos.api.analysis;

import pl.edu.icm.saos.api.analysis.ApiChart.ApiSeries;
import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointFormatter;

import com.google.common.base.Preconditions;

/**
 * A {@link Series} to {ApiSeries} converter.
 * 
 * @author Łukasz Dumiszewski
 */
public class SeriesConverter {

    private PointFormatter pointFormatter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Series} into {@link ApiSeries} 
     */
    public ApiSeries convert(Series<?, ?> series) {
        
        Preconditions.checkNotNull(series);
        
        ApiSeries uiSeries = new ApiSeries();
        
        for (Point<?,?> point : series.getPoints()) {
            
            uiSeries.addPoint(pointFormatter.formatPoint(point));
            
        }
        
        return uiSeries;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setPointFormatter(PointFormatter pointFormatter) {
        this.pointFormatter = pointFormatter;
    }
    
}
