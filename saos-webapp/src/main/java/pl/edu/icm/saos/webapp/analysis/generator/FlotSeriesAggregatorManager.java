package pl.edu.icm.saos.webapp.analysis.generator;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart.FlotSeries;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * A {@link FlotSeriesAggregator} manager.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotSeriesAggregatorManager")
public class FlotSeriesAggregatorManager {

    private List<FlotSeriesAggregator> flotSeriesAggregators = Lists.newArrayList();

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Aggregates the given flotSeries by using a proper {@link FlotSeriesAggregator} from {@link #setFlotSeriesAggregators(List)}
     */
    public FlotSeries aggregateFlotSeries(FlotSeries flotSeries, int flotSeriesIndex, UiyValueType uiyValueType) {
        
        Preconditions.checkNotNull(flotSeries);
        Preconditions.checkNotNull(uiyValueType);
        
        List<FlotSeriesAggregator> handlingAggregators = flotSeriesAggregators.stream().filter(a->a.handles(uiyValueType)).collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(handlingAggregators)) {
            throw new IllegalArgumentException("no " + FlotSeriesAggregator.class.getName() + " handling " + uiyValueType.name());
        }
        
        return handlingAggregators.get(0).aggregateFlotSeries(flotSeries, flotSeriesIndex);
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFlotSeriesAggregators(List<FlotSeriesAggregator> flotSeriesAggregators) {
        this.flotSeriesAggregators = flotSeriesAggregators;
    }
    
}
