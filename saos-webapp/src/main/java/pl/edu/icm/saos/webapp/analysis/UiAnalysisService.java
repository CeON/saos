package pl.edu.icm.saos.webapp.analysis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.analysis.ApiChart;
import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.search.analysis.AnalysisService;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.converter.JudgmentSeriesFilterConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UixSettingsConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UiySettingsConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;

import com.google.common.base.Preconditions;

/**
 * 
 * UI related analysis service
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uiAnalysisService")
public class UiAnalysisService {

    
    private AnalysisService analysisService;
    
    private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter;
    
    private UixSettingsConverter uixSettingsConverter;
    
    private UiySettingsConverter uiySettingsConverter;
    
    private ChartConverter flotChartConverter;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates and returns a {@link ApiChart} according to the settings in the passed analysisForm  
     */
    public FlotChart generateChart(AnalysisForm analysisForm) {
        
        Preconditions.checkNotNull(analysisForm);
        
        
        // create request
        
        List<JudgmentSeriesCriteria> judgmentSeriesCriteriaList = judgmentSeriesFilterConverter.convertList(analysisForm.getFilters());
  
        XSettings xsettings = uixSettingsConverter.convert(analysisForm.getXsettings());
        
        YSettings ysettings = uiySettingsConverter.convert(analysisForm.getYsettings());
        
        
        // execute
        
        Chart<Object, Number> chart = analysisService.generateChart(judgmentSeriesCriteriaList, xsettings, ysettings);
        
        
        // convert & return result
        
        return flotChartConverter.convert(chart);
        
    }


    //------------------------ SETTERS --------------------------

    @Autowired
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @Autowired
    public void setJudgmentSeriesFilterConverter(JudgmentSeriesFilterConverter judgmentSeriesFilterConverter) {
        this.judgmentSeriesFilterConverter = judgmentSeriesFilterConverter;
    }

    @Autowired
    public void setUixSettingsConverter(UixSettingsConverter uixSettingsConverter) {
        this.uixSettingsConverter = uixSettingsConverter;
    }

    @Autowired
    public void setUiySettingsConverter(UiySettingsConverter uiySettingsConverter) {
        this.uiySettingsConverter = uiySettingsConverter;
    }

    @Autowired
    public void setFlotChartConverter(ChartConverter flotChartConverter) {
        this.flotChartConverter = flotChartConverter;
    }
    
    
    
}
