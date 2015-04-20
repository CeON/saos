package pl.edu.icm.saos.webapp.analysis.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

/**
 * 
 * A flot chart generator
 * 
 * @author Łukasz Dumiszewski
 */
@Service("flotChartGenerator")
public class FlotChartGenerator {

    private ChartGenerator chartGenerator;
    
    private ChartConverter chartConverter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates {@link FlotChart} according to the given chartCode and analysisForm 
     * @see ChartGenerator#generateChart(ChartCode, AnalysisForm)
     * @see ChartConverter#convert(Chart)
     */
    public FlotChart generateFlotChart(ChartCode chartCode, AnalysisForm analysisForm) {
        
        Chart<Object, Number> chart = chartGenerator.generateChart(chartCode, analysisForm);
        
        FlotChart flotChart = chartConverter.convert(chart);
        
        return flotChart;
    }

    /**
     * Invokes: {@link ChartGenerator#canGenerateChart(ChartCode, AnalysisForm)} 
     */
    public boolean canGenerateChart(ChartCode chartCode, AnalysisForm analysisForm) {
        
        return chartGenerator.canGenerateChart(chartCode, analysisForm);
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setChartGenerator(ChartGenerator chartGenerator) {
        this.chartGenerator = chartGenerator;
    }

    @Autowired
    public void setChartConverter(ChartConverter chartConverter) {
        this.chartConverter = chartConverter;
    }
    
}
