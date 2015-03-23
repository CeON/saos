package pl.edu.icm.saos.webapp.analysis.result;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.formatter.PointValueFormatterManager;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * Generator of x tick labels, see: {@link FlotChart#getXticks()}
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotXticksGenerator")
public class FlotXticksGenerator {

    private PointValueFormatterManager pointValueFormatterManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates x tick list from the given chart. Uses the first series of the chart
     * to generate the map (assumes that every series has the same x values). <br/>
     * Returns empty list if the given chart has no
     * series or the first series is empty. 
     * 
     * @param chart the chart from which the x tick list will be generated
     * @throws NullPointerException if the passed chart is null
     * @see FlotChart#getXticks()
     */
    public List<Object[]> generateXticks(Chart<?, Number> chart) {
        
        Preconditions.checkNotNull(chart);
        
        List<Object[]> xticks = Lists.newArrayList();
        
        
        
        // Assumption: each series has got same x values, so 
        // the first series of the chart will be enough to generate xticks
        
        if (CollectionUtils.isEmpty(chart.getSeriesList())) {
            return xticks;
        }
        
        Series<?, Number> series = chart.getSeriesList().get(0);
        
        if (series == null) {
            return xticks;
        }
        
        
        for (int i=0; i < series.getPoints().size(); i++) {
            
            Point<?, Number> point = series.getPoints().get(i);
            
            xticks.add(new Object[] {i, pointValueFormatterManager.format(point.getX())});
            
        }
        
        
        return xticks;
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setPointValueFormatterManager(PointValueFormatterManager pointValueFormatterManager) {
        this.pointValueFormatterManager = pointValueFormatterManager;
    }
    
}
