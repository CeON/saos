package pl.edu.icm.saos.webapp.analysis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.generator.ChartAggregator;
import pl.edu.icm.saos.webapp.analysis.generator.MainChartGenerator;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

/**
 * 
 * UI related analysis service
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uiAnalysisService")
public class UiAnalysisService {

    
    private MainChartGenerator mainChartGenerator;
    
    private ChartAggregator chartAggregator;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates and returns a map of {@link FlotChart}s according to the settings in the passed analysisForm  
     */
    public Map<ChartCode, FlotChart> generateCharts(AnalysisForm analysisForm) {
        
        Preconditions.checkNotNull(analysisForm);
        
        Map<ChartCode, FlotChart> charts = Maps.newHashMap();
        
        
        // main chart
        
        FlotChart mainChart = mainChartGenerator.generateChart(analysisForm);
        
        // aggregated chart
        
        FlotChart aggregatedMainChart = chartAggregator.aggregateChart(mainChart, analysisForm.getYsettings().getValueType()); 
        
        // return
        
        charts.put(ChartCode.MAIN_CHART, mainChart);
        charts.put(ChartCode.AGGREGATED_MAIN_CHART, aggregatedMainChart);
        
        
        return charts;
        
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setMainChartGenerator(MainChartGenerator mainChartGenerator) {
        this.mainChartGenerator = mainChartGenerator;
    }

    @Autowired
    public void setChartAggregator(ChartAggregator chartAggregator) {
        this.chartAggregator = chartAggregator;
    }

    
    
    
}
