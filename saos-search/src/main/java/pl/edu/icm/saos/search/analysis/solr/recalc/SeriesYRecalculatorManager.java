package pl.edu.icm.saos.search.analysis.solr.recalc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.YValueType;

import com.google.common.collect.Lists;

/**
 * Manager of {@link SeriesYRecalculator}s
 * 
 * @author Łukasz Dumiszewski
 */
@Service("seriesYRecalculatorManager")
public class SeriesYRecalculatorManager {

    
    private List<SeriesYRecalculator> seriesYRecalculators = Lists.newArrayList();

    /**
     * Searches for a {@link SeriesYRateRecalculator} (in {@link #setSeriesYRecalculators(List)}) handling 
     * the specified yValueType (see: {@link SeriesYRateRecalculator#handles(YValueType)}) and returns it. 
     */
    public SeriesYRecalculator getSeriesYRecalculator(YValueType yValueType) {
        
        for (SeriesYRecalculator seriesYRecalculator : seriesYRecalculators) {
            
            if (seriesYRecalculator.handles(yValueType)) {
                
                return seriesYRecalculator;
            
            }
            
        }
        
        throw new IllegalArgumentException("no seriesYRecalculator handling " + yValueType.getClass().getName() + " can be found");
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSeriesYRecalculators(List<SeriesYRecalculator> seriesYRecalculators) {
        this.seriesYRecalculators = seriesYRecalculators;
    }
    
}
