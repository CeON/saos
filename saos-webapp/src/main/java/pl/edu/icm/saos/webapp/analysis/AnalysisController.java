package pl.edu.icm.saos.webapp.analysis;


import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.webapp.chart.Chart;
import pl.edu.icm.saos.webapp.chart.Chart.Series;

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
    public String showAnalysis(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        return "analysis";
    }
    
    @RequestMapping(value="/analysis/removePhrase", method=RequestMethod.POST)
    public String removeSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, @RequestParam("searchCriteriaIndexToRemove") int searchCriteriaIndexToRemove, ModelMap model, HttpServletRequest request) {
        analysisForm.getSeriesSearchCriteriaList().remove(searchCriteriaIndexToRemove);
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/addNewPhrase", method=RequestMethod.POST)
    public String addNewSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        analysisForm.addSeriesSearchCriteria(new SeriesSearchCriteria());
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/generate", method= RequestMethod.GET)
    @ResponseBody
    public Chart generate(@ModelAttribute("analysisForm") AnalysisForm analysisForm) {
        Chart chart = new Chart();
        IntStream.rangeClosed(1, analysisForm.getSeriesSearchCriteriaList().size()).forEach(i->chart.addSeries(generateMockSeries()));
        return chart;
    }

    
    
    //------------------------ PRIVATE --------------------------

    // TODO: will be changed to a real series data generator in the future
    private Series generateMockSeries() {
        Series series = new Series();
        for (int i = 100; i > 0; i--) {
            LocalDate month = new LocalDate().minusMonths(i);
            int value = RandomUtils.nextInt(100);
            series.addPoint(""+month.toDate().getTime(), ""+value);
        }
        return series;
    }
}
