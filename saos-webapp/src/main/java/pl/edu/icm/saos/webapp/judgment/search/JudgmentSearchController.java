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

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaForm;
import pl.edu.icm.saos.webapp.judgment.PageLinkGenerator;

/**
 * Controller for judgment search view.
 * 
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class JudgmentSearchController {

	
	@Autowired
	private JudgmentWebSearchService judgmentsWebSearchService;
	
	@Autowired
	private CcListService ccListService;
	
	@Autowired
	private ScListService scListService;

	@Autowired
	private LawJournalEntryRepository lawJournalEntryRepository;
	
	@Autowired
	private JudgmentRepository judgmentRepository;
	
	
	//------------------------ LOGIC --------------------------
	
	@RequestMapping(value="/search", method=RequestMethod.GET)
	public String judgmentSearchResults(@ModelAttribute("judgmentCriteriaForm") JudgmentCriteriaForm judgmentCriteriaForm,
	@SortDefault(sort="JUDGMENT_DATE", direction=Sort.Direction.DESC) Pageable pageable,
		ModelMap model, HttpServletRequest request) {
		
		SearchResults<JudgmentSearchResult> searchResults = judgmentsWebSearchService.search(judgmentCriteriaForm, pageable);
		
		model.addAttribute("pageable", pageable);
		model.addAttribute("searchResults", searchResults);
		model.addAttribute("pageLink", PageLinkGenerator.generateSearchPageBaseLink(request));
		
		addCommonCourtsToModel(judgmentCriteriaForm, model);
		addSupremeCourtChambersToModel(judgmentCriteriaForm, model);
		addSupremeCourtJudgmentForm(judgmentCriteriaForm, model);
		addLawJournalEntryToModel(judgmentCriteriaForm, model);
		addReferencedJudgmentToModel(judgmentCriteriaForm, model);
		
		
		return "judgmentSearch";
	}
	
	
	//------------------------ PRIVATE --------------------------
	
	private void addCommonCourtsToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
	    
	    if(CourtType.COMMON.equals(judgmentCriteriaForm.getCourtType())) {
    		model.addAttribute("commonCourts", ccListService.findCommonCourts());
    		
    		if (judgmentCriteriaForm.getCommonCourtId() != null) {
    			model.addAttribute("commonCourtDivisions", ccListService.findCcDivisions(judgmentCriteriaForm.getCommonCourtId()));
    		}
	    }
	}
	
	private void addSupremeCourtChambersToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
	    
	    if(CourtType.SUPREME.equals(judgmentCriteriaForm.getCourtType())) {
    		model.addAttribute("supremeChambers", scListService.findScChambers());
    		
    		if (judgmentCriteriaForm.getSupremeChamberId() != null) {
    			model.addAttribute("supremeChamberDivisions", scListService.findScChamberDivisions(judgmentCriteriaForm.getSupremeChamberId()));
    		}
	    }
	}
	
	private void addSupremeCourtJudgmentForm(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
	    
	    if(CourtType.SUPREME.equals(judgmentCriteriaForm.getCourtType())) {
	        model.addAttribute("scJudgmentForms", scListService.findScJudgmentForms());
	    }
	}
	
	private void addLawJournalEntryToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
	    
	    if (judgmentCriteriaForm.getLawJournalEntryId() != null) {
	        model.addAttribute("lawJournalEntry", lawJournalEntryRepository.findOne(judgmentCriteriaForm.getLawJournalEntryId()));
	    }
	}
	
	private void addReferencedJudgmentToModel(JudgmentCriteriaForm judgmentCriteriaForm, ModelMap model) {
	    
	    if (judgmentCriteriaForm.getReferencedCourtCaseId() != null) {
	        Judgment judgment = judgmentRepository.findOneAndInitialize(judgmentCriteriaForm.getReferencedCourtCaseId());
	        model.addAttribute("referencedJudgment", judgment);
	    }
	}

}
