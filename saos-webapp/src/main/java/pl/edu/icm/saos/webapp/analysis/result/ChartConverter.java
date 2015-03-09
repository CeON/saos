package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.result.Chart;
import pl.edu.icm.saos.search.analysis.result.Series;

import com.google.common.base.Preconditions;

/**
 * A chart converter
 * 
 * @author Łukasz Dumiszewski
 */
@Service("chartConverter")
public class ChartConverter {

    
    private SeriesConverter seriesConverter;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Chart} into {@link UiChart} 
     */
    public UiChart convert(Chart<?, ?> chart) {
        
        Preconditions.checkNotNull(chart);
        
        UiChart uiChart = new UiChart();
        
        for (Series<?, ?> series : chart.getSeriesList()) {
            
            uiChart.addSeries(seriesConverter.convert(series));
            
        }

        return uiChart;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesConverter(SeriesConverter seriesConverter) {
        this.seriesConverter = seriesConverter;
    }
    
}
