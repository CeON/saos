package pl.edu.icm.saos.webapp.analysis.request;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class AnalysisForm {

    private List<JudgmentSeriesFilter> filters = Lists.newArrayList();
    
    private UiXSettings uiXSettings;
    
    private UiYSettings uiYSettings;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public List<JudgmentSeriesFilter> getFilters() {
        return filters;
    }
    
    public UiXSettings getUiXSettings() {
        return uiXSettings;
    }

    public UiYSettings getUiYSettings() {
        return uiYSettings;
    }

    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeriesSearchCriteria(JudgmentSeriesFilter seriesSearchCriteria) {
        filters.add(seriesSearchCriteria);
    }
    

    
    //------------------------ SETTERS --------------------------
    
    public void setFilters(List<JudgmentSeriesFilter> filters) {
        this.filters = filters;
    }

    public void setUiXSettings(UiXSettings uiXSettings) {
        this.uiXSettings = uiXSettings;
    }

    public void setUiYSettings(UiYSettings uiYSettings) {
        this.uiYSettings = uiYSettings;
    }
    
   
    
}
