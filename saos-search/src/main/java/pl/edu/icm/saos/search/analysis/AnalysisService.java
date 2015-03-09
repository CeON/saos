package pl.edu.icm.saos.search.analysis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.search.analysis.result.Chart;
import pl.edu.icm.saos.search.analysis.result.Series;

import com.google.common.base.Preconditions;

/**
 * Main analysis service.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("analysisService")
public class AnalysisService {

    
    private SeriesService seriesService;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns a {@link Chart} based on the specified criteria and x,y axis settings
     * @param criteriaList list of criteria objects, each criteria for one series of a chart
     * @param xsettings settings of x-axis
     * @param ysettings settings of y-axis
     */
    public Chart<Object, Number> generateChart(List<JudgmentSeriesCriteria> criteriaList, XSettings xsettings, YSettings ysettings) {
        
        Preconditions.checkNotNull(criteriaList);
        Preconditions.checkArgument(!criteriaList.isEmpty());
        
        Chart<Object, Number> chart = new Chart<>();
        
        for (JudgmentSeriesCriteria criteria : criteriaList) {
            
            Series<Object, Number> series = seriesService.generateSeries(criteria, xsettings, ysettings);
            
            chart.addSeries(series);
        
        }
        
        
        return chart;
            
        
        
    }
    
    
    
    //------------------------ SETTERS --------------------------


    @Autowired
    public void setSeriesService(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

   
    
    
}
