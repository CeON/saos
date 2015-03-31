package pl.edu.icm.saos.webapp.analysis;


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

import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.UixRangeFactory;
import pl.edu.icm.saos.webapp.analysis.request.UixSettings;
import pl.edu.icm.saos.webapp.analysis.result.FlotChart;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
public class AnalysisController {

    
    
    private UiAnalysisService uiAnalysisService;
    
    private UixRangeFactory uixRangeFactory;
    
    private int maxNumberOfSearchPhrases;
    
    
    //------------------------ MODEL --------------------------
    
    @ModelAttribute("analysisForm")
    public AnalysisForm analysisForm() {
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.addSeriesSearchCriteria(new JudgmentSeriesFilter()); // one empty phrase by default
        UixSettings uixSettings = analysisForm.getXsettings();
        uixSettings.setField(XField.JUDGMENT_DATE);
        uixSettings.setRange(uixRangeFactory.createUixRange(uixSettings.getField()));
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
        analysisForm.getFilters().remove(filterIndexToRemove);
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/addNewPhrase", method=RequestMethod.POST)
    public String addNewSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        analysisForm.addSeriesSearchCriteria(new JudgmentSeriesFilter());
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/generate", method= RequestMethod.GET)
    @ResponseBody
    public FlotChart generate(@ModelAttribute("analysisForm") AnalysisForm analysisForm) {
        return uiAnalysisService.generateChart(analysisForm);
        
    }

    
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUiAnalysisService(UiAnalysisService uiAnalysisService) {
        this.uiAnalysisService = uiAnalysisService;
    }

    @Autowired
    public void setUixRangeFactory(UixRangeFactory uixRangeFactory) {
        this.uixRangeFactory = uixRangeFactory;
    }

    @Value("${analysis.maxNumberOfSearchPhrases}")
    public void setMaxNumberOfSearchPhrases(int maxNumberOfSearchPhrases) {
        this.maxNumberOfSearchPhrases = maxNumberOfSearchPhrases;
    }

    
        
}
