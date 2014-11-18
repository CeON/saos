package pl.edu.icm.saos.webapp.judgment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.webapp.division.SimpleDivision;
import pl.edu.icm.saos.webapp.division.SimpleDivisionConverter;
import pl.edu.icm.saos.webapp.services.CourtsWebService;
import pl.edu.icm.saos.webapp.services.JudgmentsWebSearchService;


@Controller("webJudgmentController")
public class JudgmentController {

    @Autowired
	private JudgmentRepository judgmentRepository;

    @Autowired
    private CourtsWebService courtsWebService;
    
    @Autowired
    private JudgmentsWebSearchService judgmentsWebSearchService;

    @Autowired
    private SimpleDivisionConverter simpleDivisionConverter;

	
    //------------------------ LOGIC --------------------------
    
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String JudgmentSearchResults(@ModelAttribute("judgmentCriteriaForm") JudgmentCriteriaForm judgmentCriteriaForm,
			@SortDefault(sort="JUDGMENT_DATE") Pageable pageable,
			ModelMap model, HttpServletRequest request) {

		SearchResults<JudgmentSearchResult> searchResults = judgmentsWebSearchService.search(judgmentCriteriaForm, pageable);

		model.addAttribute("pageable", pageable);
		model.addAttribute("resultSearch", searchResults);
		model.addAttribute("pageLink", PageLinkGenerator.generateSearchPageBaseLink(request));
		
		addTotalNumberOfPagesToModel(searchResults.getTotalResults(), pageable.getPageSize(), model);
		
		addCommonCourtsToModel(judgmentCriteriaForm, model);
		addSupremeCourtsToModel(judgmentCriteriaForm, model);
		
		return "search";
	}
	
	@RequestMapping("/judgments/{id}")
	public String JudgmentSignleResult(ModelMap model, @PathVariable int id) {
		
		Judgment judgment = judgmentRepository.findOneAndInitialize(id);
		model.addAttribute("judgment", judgment);
		
		return "singleResult";
	}

	@RequestMapping("/search/courtDivision/{commonCourtId}")
	@ResponseBody
	public List<SimpleDivision> division(@PathVariable("commonCourtId") String commonCourtId) {
		return getCcDivisionList(commonCourtId);
	}
	
	@RequestMapping("/search/chamberDivision/{supremeChamberId}")
	@ResponseBody
	public List<SimpleDivision> chamberDivision(@PathVariable("supremeChamberId") String supremeChamberId) {
		return getSupremeChamberDivisionList(supremeChamberId);
	}

	
	//------------------------ PRIVATE --------------------------
	
	private void addTotalNumberOfPagesToModel(long totalResults, int pageSize, ModelMap model) {
		model.addAttribute("totalPages", (int)Math.ceil(((double)totalResults)/((double)pageSize)));
	}
	
	private void addCommonCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("commonCourts", courtsWebService.getCommonCourts());
		
		if (judgmentCriteriaForm.getCommonCourtId() != null) {
			model.addAttribute("commonCourtDivisions", getCcDivisionList(judgmentCriteriaForm.getCommonCourtId()));
		}
	}
	
	private List<SimpleDivision> getCcDivisionList(String commonCourtId) {
		try {
			int intCourtId = Integer.parseInt(commonCourtId);
			return simpleDivisionConverter.convertCcDivisions(courtsWebService.getCcDivisions(intCourtId));
		} catch (NumberFormatException e) {
			return Lists.newArrayList();
		} 
	}
	
	private void addSupremeCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("supremeChambers", courtsWebService.getScChambers());
		
		if (judgmentCriteriaForm.getSupremeChamberId() != null) {
			model.addAttribute("supremeChamberDivisions", getSupremeChamberDivisionList(judgmentCriteriaForm.getSupremeChamberId()));
		}
	}
	
	private List<SimpleDivision> getSupremeChamberDivisionList(String supremeChamberId) {
		try {
			int intChamberId = Integer.parseInt(supremeChamberId);
			return simpleDivisionConverter.convertScChamberDivisions(courtsWebService.getScChamberDivisions(intChamberId));
		} catch (NumberFormatException e) {
			return Lists.newArrayList();
		} 
	}
}