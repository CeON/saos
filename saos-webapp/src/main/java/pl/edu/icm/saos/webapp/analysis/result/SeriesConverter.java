package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.result.Point;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.webapp.analysis.result.UiChart.UiSeries;

import com.google.common.base.Preconditions;

/**
 * A series converter.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("seriesConverter")
public class SeriesConverter {

    private PointFormatter pointFormatter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Series} into {@link UiSeries} 
     */
    public UiSeries convert(Series<?, ?> series) {
        
        Preconditions.checkNotNull(series);
        
        UiSeries uiSeries = new UiSeries();
        
        for (Point<?,?> point : series.getPoints()) {
            
            uiSeries.addPoint(pointFormatter.formatPoint(point));
            
        }
        
        return uiSeries;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setPointFormatter(PointFormatter pointFormatter) {
        this.pointFormatter = pointFormatter;
    }
    
}
