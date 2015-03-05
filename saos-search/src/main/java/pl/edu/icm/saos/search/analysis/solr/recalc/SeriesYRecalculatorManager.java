package pl.edu.icm.saos.search.analysis.solr.recalc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("seriesYRecalculatorManager")
public class SeriesYRecalculatorManager {

    
    private List<SeriesYRecalculator> seriesYRecalculators = Lists.newArrayList();

    
    public SeriesYRecalculator getSeriesYRecalculator(YValueType yValueType) {
        
        for (SeriesYRecalculator seriesYRecalculator : seriesYRecalculators) {
            
            if (seriesYRecalculator.handles(yValueType)) {
                
                return seriesYRecalculator;
            
            }
            
        }
        
        throw new IllegalStateException("no seriesYRecalculator handling " + yValueType + " can be found");
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesYRecalculators(List<SeriesYRecalculator> seriesYRecalculators) {
        this.seriesYRecalculators = seriesYRecalculators;
    }
    
}
