package pl.edu.icm.saos.webapp.analysis.generator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * 
 * Service for generating flot charts
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotChartService")
public class FlotChartService {

    
    private FlotChartGenerator flotChartGenerator;
    
    private FlotChartAggregator flotChartAggregator;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates and returns a map of {@link FlotChart}s according to the settings in the passed analysisForm  
     */
    public Map<ChartCode, FlotChart> generateCharts(AnalysisForm analysisForm) {
        
        Preconditions.checkNotNull(analysisForm);
        
        Map<ChartCode, FlotChart> charts = Maps.newHashMap();
        
        
        generateAllPossibleCharts(analysisForm, charts);
        
        generateAggregatedMainChart(analysisForm, charts);
        
        
        return charts;
        
    }


   

    
    //------------------------ PRIVATE --------------------------
    
    private void generateAllPossibleCharts(AnalysisForm analysisForm, Map<ChartCode, FlotChart> charts) {
            
        for (ChartCode chartCode : ChartCode.values()) {
            if (flotChartGenerator.canGenerateChart(chartCode, analysisForm)) {
                FlotChart chart = flotChartGenerator.generateFlotChart(chartCode, analysisForm);
                charts.put(chartCode, chart);
            }
        }
            
    }
    
    private void generateAggregatedMainChart(AnalysisForm analysisForm, Map<ChartCode, FlotChart> charts) {
        
        FlotChart mainChart = charts.get(ChartCode.MAIN_CHART);
        if (mainChart != null) {
            FlotChart aggregatedMainChart = flotChartAggregator.aggregateChart(charts.get(ChartCode.MAIN_CHART), analysisForm.getYsettings().getValueType());
            charts.put(ChartCode.AGGREGATED_MAIN_CHART, aggregatedMainChart);
        }
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setFlotChartAggregator(FlotChartAggregator flotChartAggregator) {
        this.flotChartAggregator = flotChartAggregator;
    }

    @Autowired
    public void setFlotChartGenerator(FlotChartGenerator flotChartGenerator) {
        this.flotChartGenerator = flotChartGenerator;
    }

    
    
    
}
