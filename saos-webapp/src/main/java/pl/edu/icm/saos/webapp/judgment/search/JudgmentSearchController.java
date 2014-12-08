package pl.edu.icm.saos.webapp.judgment.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;
import pl.edu.icm.saos.webapp.court.SimpleDivision;
import pl.edu.icm.saos.webapp.court.SimpleDivisionConverter;
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaForm;
import pl.edu.icm.saos.webapp.judgment.PageLinkGenerator;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class JudgmentSearchController {

	
    @Autowired
	private JudgmentRepository judgmentRepository;
    
    @Autowired
    private CcListService ccListService;
    
    @Autowired
    private ScListService scListService;
    
    @Autowired
    private JudgmentWebSearchService judgmentsWebSearchService;
	
    @Autowired
    private SimpleDivisionConverter simpleDivisionConverter;
	
    //------------------------ LOGIC --------------------------
    
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String JudgmentSearchResults(@ModelAttribute("judgmentCriteriaForm") JudgmentCriteriaForm judgmentCriteriaForm,
			@SortDefault(sort="JUDGMENT_DATE", direction=Sort.Direction.DESC) Pageable pageable,
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
	
	
	//------------------------ PRIVATE --------------------------
	
	private void addTotalNumberOfPagesToModel(long totalResults, int pageSize, ModelMap model) {
		model.addAttribute("totalPages", (int)Math.ceil(((double)totalResults)/((double)pageSize)));
	}
	
	private void addCommonCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("commonCourts", ccListService.findCommonCourts());
		
		if (judgmentCriteriaForm.getCommonCourtId() != null) {
			model.addAttribute("commonCourtDivisions", getCcDivisionList(judgmentCriteriaForm.getCommonCourtId()));
		}
	}
	
	private List<SimpleDivision> getCcDivisionList(int commonCourtId) {
		return simpleDivisionConverter.convertCcDivisions(ccListService.findCcDivisions(commonCourtId));
	}
	
	private void addSupremeCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("supremeChambers", scListService.findScChambers());
		
		if (judgmentCriteriaForm.getSupremeChamberId() != null) {
			model.addAttribute("supremeChamberDivisions", getSupremeChamberDivisionList(judgmentCriteriaForm.getSupremeChamberId()));
		}
	}
	
	private List<SimpleDivision> getSupremeChamberDivisionList(int supremeChamberId) {
		return simpleDivisionConverter.convertScChamberDivisions(scListService.findScChamberDivisions(supremeChamberId));
	}
}
