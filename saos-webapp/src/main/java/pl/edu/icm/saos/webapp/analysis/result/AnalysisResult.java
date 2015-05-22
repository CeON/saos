package pl.edu.icm.saos.webapp.analysis.result;

import java.util.Map;

import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;

/**
 * Analysis result
 * 
 * @author Łukasz Dumiszewski
 */

public class AnalysisResult {


    private AnalysisForm analysisForm;
    
    private Map<ChartCode, FlotChart> charts;

    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public AnalysisResult(AnalysisForm analysisForm, Map<ChartCode, FlotChart> charts) {
        super();
        this.analysisForm = analysisForm;
        this.charts = charts;
    }


    
    //------------------------ GETTERS --------------------------
    
    /**
     * Criteria and settings for which the {@link #getCharts()} have been generated
     */
    public AnalysisForm getAnalysisForm() {
        return analysisForm;
    }

    /**
     * Generated charts 
     */
    public Map<ChartCode, FlotChart> getCharts() {
        return charts;
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setAnalysisForm(AnalysisForm analysisForm) {
        this.analysisForm = analysisForm;
    }

    public void setCharts(Map<ChartCode, FlotChart> charts) {
        this.charts = charts;
    }
    
}
