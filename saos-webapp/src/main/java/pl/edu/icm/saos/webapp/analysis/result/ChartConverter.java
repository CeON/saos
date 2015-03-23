package pl.edu.icm.saos.webapp.analysis.result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;

import com.google.common.base.Preconditions;

/**
 * A {@link Chart} to {@link FlotChart}  converter
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotChartConverter")
public class ChartConverter {

    
    private SeriesConverter flotSeriesConverter;
    
    private FlotXticksGenerator flotXticksGenerator;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link Chart} into {@link FlotChart} 
     */
    public FlotChart convert(Chart<?, Number> chart) {
        
        Preconditions.checkNotNull(chart);
        
        FlotChart flotChart = new FlotChart();
        
        for (Series<?, Number> series : chart.getSeriesList()) {
            
            flotChart.addSeries(flotSeriesConverter.convert(series));
            
        }

        flotChart.setXticks(flotXticksGenerator.generateXticks(chart));
        
        return flotChart;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesConverter(SeriesConverter flotSeriesConverter) {
        this.flotSeriesConverter = flotSeriesConverter;
    }

    @Autowired
    public void setFlotXticksGenerator(FlotXticksGenerator flotXticksGenerator) {
        this.flotXticksGenerator = flotXticksGenerator;
    }

    
}
