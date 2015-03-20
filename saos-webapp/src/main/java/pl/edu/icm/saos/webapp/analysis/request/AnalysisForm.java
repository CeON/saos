package pl.edu.icm.saos.webapp.analysis.request;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * An analysis form data holder
 * 
 * @author Łukasz Dumiszewski
 */

public class AnalysisForm {

    private List<JudgmentSeriesFilter> filters = Lists.newArrayList();
    
    private UixSettings xsettings = new UixSettings();
    
    private UiySettings ysettings = new UiySettings();
    
    
    
    //------------------------ GETTERS --------------------------
    
    public List<JudgmentSeriesFilter> getFilters() {
        return filters;
    }
    
    public UixSettings getXsettings() {
        return xsettings;
    }

    public UiySettings getYsettings() {
        return ysettings;
    }


    
    
    
    //------------------------ LOGIC --------------------------
    
    public void addSeriesSearchCriteria(JudgmentSeriesFilter seriesSearchCriteria) {
        filters.add(seriesSearchCriteria);
    }
    

    
    //------------------------ SETTERS --------------------------
    
    public void setFilters(List<JudgmentSeriesFilter> filters) {
        this.filters = filters;
    }

    public void setXsettings(UixSettings xsettings) {
        this.xsettings = xsettings;
    }

    public void setYsettings(UiySettings ysettings) {
        this.ysettings = ysettings;
    }

   
   
    
}
