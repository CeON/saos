package pl.edu.icm.saos.api.analysis;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;

import com.google.common.base.Preconditions;

/**
 * A {@link Chart} to {@link ApiChart} converter
 * 
 * @author Łukasz Dumiszewski
 */
public class ChartConverter {

    
    private SeriesConverter seriesConverter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Chart} into {@link ApiChart} 
     */
    public ApiChart convert(Chart<?, Number> chart) {
        
        Preconditions.checkNotNull(chart);
        
        ApiChart uiChart = new ApiChart();
        
        for (Series<?, ?> series : chart.getSeriesList()) {
            
            uiChart.addSeries(seriesConverter.convert(series));
            
        }

        
        
        return uiChart;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setSeriesConverter(SeriesConverter seriesConverter) {
        this.seriesConverter = seriesConverter;
    }

    
}
