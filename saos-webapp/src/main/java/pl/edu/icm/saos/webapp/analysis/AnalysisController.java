package pl.edu.icm.saos.webapp.analysis;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import pl.edu.icm.saos.webapp.analysis.csv.ChartCsvService;
import pl.edu.icm.saos.webapp.analysis.generator.FlotChartService;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.request.DefaultJudgmentGlobalFilterFactory;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.result.AnalysisResult;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import pl.edu.icm.saos.webapp.common.search.CourtDataModelCreator;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
public class AnalysisController {

    private FlotChartService flotChartService;
    
    private ChartCsvService chartCsvService;
    
    private DefaultJudgmentGlobalFilterFactory judgmentDateRangeFactory;
    
    private CourtDataModelCreator courtDataModelCreator;
    
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

        courtDataModelCreator.addCourtDataToModel(analysisForm.getGlobalFilter().getCourtCriteria(), model);
        
        return "analysis";
    }
    
    @RequestMapping(value="/analysis/removePhrase", method=RequestMethod.POST)
    public String removeSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, @RequestParam("filterIndexToRemove") int filterIndexToRemove, ModelMap model, HttpServletRequest request) {
        analysisForm.getSeriesFilters().remove(filterIndexToRemove);
        courtDataModelCreator.addCourtDataToModel(analysisForm.getGlobalFilter().getCourtCriteria(), model);
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/addNewPhrase", method=RequestMethod.POST)
    public String addNewSeriesSearchCriteria(@ModelAttribute("analysisForm") AnalysisForm analysisForm, ModelMap model, HttpServletRequest request) {
        analysisForm.addSeriesFilter(new JudgmentSeriesFilter());
        courtDataModelCreator.addCourtDataToModel(analysisForm.getGlobalFilter().getCourtCriteria(), model);
        return "analysisForm";
    }
    
    @RequestMapping(value="/analysis/generate", method= RequestMethod.GET)
    @ResponseBody
    public AnalysisResult generate(@ModelAttribute("analysisForm") AnalysisForm analysisForm) {
        return flotChartService.generateCharts(analysisForm);
        
    }

    
    @RequestMapping(value="/analysis/generateCsv", method= RequestMethod.GET)
    public void generateCsv(@ModelAttribute("analysisForm") AnalysisForm analysisForm, @RequestParam("chartCode") ChartCode chartCode, HttpServletResponse response) throws IOException {
        chartCsvService.generateChartCsv(chartCode, analysisForm, response);
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFlotChartService(FlotChartService flotChartService) {
        this.flotChartService = flotChartService;
    }

    @Autowired
    public void setChartCsvService(ChartCsvService chartCsvService) {
        this.chartCsvService = chartCsvService;
    }
    
    @Autowired
    public void setJudgmentDateRangeFactory(DefaultJudgmentGlobalFilterFactory judgmentDateRangeFactory) {
        this.judgmentDateRangeFactory = judgmentDateRangeFactory;
    }


    @Autowired
    public void setCourtDataModelCreator(CourtDataModelCreator courtDataModelCreator) {
        this.courtDataModelCreator = courtDataModelCreator;
    }

    
    @Value("${analysis.maxNumberOfSearchPhrases}")
    public void setMaxNumberOfSearchPhrases(int maxNumberOfSearchPhrases) {
        this.maxNumberOfSearchPhrases = maxNumberOfSearchPhrases;
    }



    
        
}
