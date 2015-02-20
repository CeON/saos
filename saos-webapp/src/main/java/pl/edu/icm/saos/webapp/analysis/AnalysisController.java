package pl.edu.icm.saos.webapp.analysis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
public class AnalysisController {

    
    
    //------------------------ MODEL --------------------------
    
    @ModelAttribute("analysisForm")
    public AnalysisForm analysisForm() {
        AnalysisForm analysisForm = new AnalysisForm();
        analysisForm.addSeriesSearchCriteria(new SeriesSearchCriteria()); // one empty phrase by default
        return analysisForm;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @RequestMapping(value="/analysis", method=RequestMethod.GET)
    public String showAnalytics(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        return "analysis";
    }
    
    @RequestMapping(value="/analysis", params="searchCriteriaIndexToRemove", method=RequestMethod.POST)
    public String removeSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, @RequestParam("searchCriteriaIndexToRemove") int searchCriteriaIndexToRemove, ModelMap model, HttpServletRequest request) {
        analysisForm.getSeriesSearchCriteriaList().remove(searchCriteriaIndexToRemove);
        return "analysisSearchCriteria";
    }
    
    @RequestMapping(value="/analysis", params="addNewSearchCriteria", method=RequestMethod.POST)
    public String addNewSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        analysisForm.addSeriesSearchCriteria(new SeriesSearchCriteria());
        return "analysisSearchCriteria";
    }
}
