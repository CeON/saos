package pl.edu.icm.saos.webapp.analysis;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.webapp.analysis.generator.ChartAggregator;
import pl.edu.icm.saos.webapp.analysis.generator.ChartGenerator;
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

    
    private ChartGenerator chartGenerator;
    
    private ChartAggregator chartAggregator;
    
    
    
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
            if (chartGenerator.canGenerateChart(chartCode, analysisForm)) {
                FlotChart chart = chartGenerator.generateChart(chartCode, analysisForm);
                charts.put(chartCode, chart);
            }
        }
            
    }
    
    private void generateAggregatedMainChart(AnalysisForm analysisForm, Map<ChartCode, FlotChart> charts) {
        
        FlotChart mainChart = charts.get(ChartCode.MAIN_CHART);
        if (mainChart != null) {
            FlotChart aggregatedMainChart = chartAggregator.aggregateChart(charts.get(ChartCode.MAIN_CHART), analysisForm.getYsettings().getValueType());
            charts.put(ChartCode.AGGREGATED_MAIN_CHART, aggregatedMainChart);
        }
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setChartGenerator(ChartGenerator chartGenerator) {
        this.chartGenerator = chartGenerator;
    }

    @Autowired
    public void setChartAggregator(ChartAggregator chartAggregator) {
        this.chartAggregator = chartAggregator;
    }

    
    
    
}
