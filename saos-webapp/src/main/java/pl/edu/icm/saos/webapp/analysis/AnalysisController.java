package pl.edu.icm.saos.webapp.analysis;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.DefaultJudgmentGlobalFilterFactory;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
public class AnalysisController {

    
    
    private UiAnalysisService uiAnalysisService;
    
    private DefaultJudgmentGlobalFilterFactory judgmentDateRangeFactory;
    
    private int maxNumberOfSearchPhrases;
    
    
    //------------------------ MODEL --------------------------
    
    @ModelAttribute("analysisForm")
    public AnalysisForm analysisForm() {
        
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.addSeriesFilter(new JudgmentSeriesFilter()); // one empty phrase by default
        analysisForm.setGlobalFilter(judgmentDateRangeFactory.createDefaultJudgmentGlobalFilter());
        
        return analysisForm;
    }
    
    
    @ModelAttribute("maxNumberOfSearchPhrases")
    public int maxNumberOfSearchPhrases() {
        return maxNumberOfSearchPhrases;
    }
    
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setAutoGrowCollectionLimit(maxNumberOfSearchPhrases);
    }
    
    //------------------------ LOGIC --------------------------
    
    @RequestMapping(value="/analysis", method=RequestMethod.GET)
    public String showAnalysis(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        return "analysis";
    }
    
    @RequestMapping(value="/analysis/removePhrase", method=RequestMethod.POST)
    public String removeSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, @RequestParam("filterIndexToRemove") int filterIndexToRemove, ModelMap model, HttpServletRequest request) {
        analysisForm.getSeriesFilters().remove(filterIndexToRemove);
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/addNewPhrase", method=RequestMethod.POST)
    public String addNewSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        analysisForm.addSeriesFilter(new JudgmentSeriesFilter());
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/generate", method= RequestMethod.GET)
    @ResponseBody
    public Map<ChartCode, FlotChart> generate(@ModelAttribute("analysisForm") AnalysisForm analysisForm) {
        return uiAnalysisService.generateCharts(analysisForm);
        
    }

    
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUiAnalysisService(UiAnalysisService uiAnalysisService) {
        this.uiAnalysisService = uiAnalysisService;
    }

    @Autowired
    public void setJudgmentDateRangeFactory(DefaultJudgmentGlobalFilterFactory judgmentDateRangeFactory) {
        this.judgmentDateRangeFactory = judgmentDateRangeFactory;
    }
    
    @Value("${analysis.maxNumberOfSearchPhrases}")
    public void setMaxNumberOfSearchPhrases(int maxNumberOfSearchPhrases) {
        this.maxNumberOfSearchPhrases = maxNumberOfSearchPhrases;
    }

    
        
}
