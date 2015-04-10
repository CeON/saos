package pl.edu.icm.saos.webapp.analysis.generator;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.search.analysis.AnalysisService;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.request.YSettings;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.converter.JudgmentSeriesFilterConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.UiySettingsConverter;
import pl.edu.icm.saos.webapp.analysis.request.converter.XSettingsGenerator;
import pl.edu.icm.saos.webapp.analysis.request.converter.XSettingsGeneratorManager;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.ChartConverter;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

import com.google.common.base.Preconditions;

/**
 * 
 * Main chart generator
 * 
 * @see {@link ChartCode#MAIN_CHART}
 * 
 * @author Łukasz Dumiszewski
 */
@Service("chartGenerator")
public class ChartGenerator {

    
    private AnalysisService analysisService;
    
    private JudgmentSeriesFilterConverter judgmentSeriesFilterConverter;
    
    private XSettingsGeneratorManager xsettingsGeneratorManager;
    
    private UiySettingsConverter uiySettingsConverter;
    
    private ChartConverter flotChartConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    
    /**
     * Generates and returns a {@link FlotChart} according to the passed chart code and analysisForm <br/>
     * @throws IllegalArgumentException if the chart of the given chart code can not be generated for the passed analysisForm,
     * see: {@link #canGenerateChart(ChartCode, AnalysisForm)}
     */
    public FlotChart generateChart(ChartCode chartCode, AnalysisForm analysisForm) {
        
        Preconditions.checkNotNull(analysisForm);
        Preconditions.checkArgument(canGenerateChart(chartCode, analysisForm));
        
        // create request
        
        XSettingsGenerator xsettingsGenerator = xsettingsGeneratorManager.getXSettingsGenerator(chartCode);
        
        XSettings xsettings = xsettingsGenerator.generateXSettings(analysisForm.getGlobalFilter());

        YSettings ysettings = uiySettingsConverter.convert(analysisForm.getYsettings());
        
        
        List<JudgmentSeriesCriteria> judgmentSeriesCriteriaList = judgmentSeriesFilterConverter.convertList(analysisForm.getGlobalFilter(), analysisForm.getSeriesFilters());
  
        
        
        
        
        
        // execute
        
        Chart<Object, Number> chart = analysisService.generateChart(judgmentSeriesCriteriaList, xsettings, ysettings);
        
        
        // convert & return
        
        return flotChartConverter.convert(chart);
        
        
    }
    
    
    /**
     * Checks whether it is possible to generate a chart for the given code and analysisForm.
     * @see {@link ChartCode#isChartGenerated()}
     * @see {@link XSettingsGenerator#canGenerateXSettings(pl.edu.icm.saos.webapp.analysis.request.JudgmentGlobalFilter)}
     */
    public boolean canGenerateChart(ChartCode chartCode, AnalysisForm analysisForm) {
        
        if (!chartCode.isChartGenerated()) {
            return false;
        }
        
        XSettingsGenerator xsettingsGenerator = xsettingsGeneratorManager.getXSettingsGenerator(chartCode);
        
        return xsettingsGenerator.canGenerateXSettings(analysisForm.getGlobalFilter());
        
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
    public void setUiySettingsConverter(UiySettingsConverter uiySettingsConverter) {
        this.uiySettingsConverter = uiySettingsConverter;
    }

    @Autowired
    public void setFlotChartConverter(ChartConverter flotChartConverter) {
        this.flotChartConverter = flotChartConverter;
    }

    @Autowired
    public void setXsettingGeneratorManager(XSettingsGeneratorManager xsettingsGeneratorManager) {
        this.xsettingsGeneratorManager = xsettingsGeneratorManager;
    }

  
    
    
}
