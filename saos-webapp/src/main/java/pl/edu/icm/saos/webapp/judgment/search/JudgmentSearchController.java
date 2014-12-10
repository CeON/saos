package pl.edu.icm.saos.webapp.judgment.search;

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
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaForm;
import pl.edu.icm.saos.webapp.judgment.PageLinkGenerator;

/**
 * Controller for judgment search view.
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
	
	
	//------------------------ LOGIC --------------------------
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String judgmentSearchResults(@ModelAttribute("judgmentCriteriaForm") JudgmentCriteriaForm judgmentCriteriaForm,
	@SortDefault(sort="JUDGMENT_DATE", direction=Sort.Direction.DESC) Pageable pageable,
		ModelMap model, HttpServletRequest request) {
		
		SearchResults<JudgmentSearchResult> searchResults = judgmentsWebSearchService.search(judgmentCriteriaForm, pageable);
		
		model.addAttribute("pageable", pageable);
		model.addAttribute("resultSearch", searchResults);
		model.addAttribute("pageLink", PageLinkGenerator.generateSearchPageBaseLink(request));
		
		model.addAttribute("totalPages", JudgmentSearchResult.getTotalPageNumber(searchResults.getTotalResults(), pageable.getPageSize()));
		
		addCommonCourtsToModel(judgmentCriteriaForm, model);
		addSupremeChambersToModel(judgmentCriteriaForm, model);
		
		return "judgmentSearch";
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private void addCommonCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("commonCourts", ccListService.findCommonCourts());
		
		if (judgmentCriteriaForm.getCommonCourtId() != null) {
			model.addAttribute("commonCourtDivisions", ccListService.findCcDivisions(judgmentCriteriaForm.getCommonCourtId()));
		}
	}
	
	private void addSupremeChambersToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
		model.addAttribute("supremeChambers", scListService.findScChambers());
		
		if (judgmentCriteriaForm.getSupremeChamberId() != null) {
			model.addAttribute("supremeChamberDivisions", scListService.findScChamberDivisions(judgmentCriteriaForm.getSupremeChamberId()));
		}
	}	

}
