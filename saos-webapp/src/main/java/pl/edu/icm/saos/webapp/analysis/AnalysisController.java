package pl.edu.icm.saos.webapp.analysis;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.result.UiChart;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
public class AnalysisController {

    
    
    @Autowired
    private UiAnalysisService uiAnalysisService;
    
    
    
    //------------------------ MODEL --------------------------
    
    @ModelAttribute("analysisForm")
    public AnalysisForm analysisForm() {
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.addSeriesSearchCriteria(new JudgmentSeriesFilter()); // one empty phrase by default
        return analysisForm;
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
    public UiChart generate(@ModelAttribute("analysisForm") AnalysisForm analysisForm) {
        
        return uiAnalysisService.generateChart(analysisForm);
        
    }

    
        
}
