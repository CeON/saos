package pl.edu.icm.saos.webapp.analysis.csv;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;

import com.google.common.base.Preconditions;

/**
 * A service for creating csv headers and rows
 * 
 * @author Łukasz Dumiszewski
 */
@Service("chartCsvGenerator")
public class ChartCsvGenerator {

    private PointValueFormatterManager pointValueFormatterManager;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates csv header from the given chart
     */
    public String[] generateHeader(Chart<Object, Number> chart) {
        
        Preconditions.checkNotNull(chart);
        
        
        // Assumption: each series has got same x values, so 
        // the first series of the chart will be enough to generate header
        
        if (CollectionUtils.isEmpty(chart.getSeriesList())) {
            return new String[]{};
        }
        
        Series<?, Number> series = chart.getSeriesList().get(0);
        
        if (series == null) {
            return new String[]{};
        }
        
        String[] header = new String[series.getPoints().size()];
        
        for (int i=0; i < series.getPoints().size(); i++) {
            
            Point<?, Number> point = series.getPoints().get(i);
            
            header[i] = pointValueFormatterManager.format(point.getX());
            
        }
        
        
        return header;
    }
    
    /**
     * Generates csv row from y values of the given series points 
     */
    public String[] generateRow(Series<Object, Number> series) {
        
        Preconditions.checkNotNull(series);
        
        String[] row = new String[series.getPoints().size()];
        
        for (int i=0; i < series.getPoints().size(); i++) {
            
            Point<?, Number> point = series.getPoints().get(i);
            
            row[i] = pointValueFormatterManager.format(point.getY());
            
        }
        
        return row;
        
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    @Qualifier("csvPointValueFormatterManager")
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }
    
}
