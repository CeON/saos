package pl.edu.icm.saos.webapp.judgment;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.webapp.services.JudgmentsWebSearchService;


@Controller("webJudgmentController")
public class JudgmentController {

    @Autowired
	private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JudgmentsWebSearchService judgmentsWebSearchService;

    @RequestMapping(value="/search", method=RequestMethod.GET)
    public String searchJudgment(Model model) {
    	
    	model.addAttribute("judgmentCriteriaForm", new JudgmentCriteriaForm());
    	
        return "search";
    }
	
	@RequestMapping(value="/results", method=RequestMethod.GET)
	public String JudgmentSearchResults(@ModelAttribute("judgmentCriteriaForm") JudgmentCriteriaForm judgmentCriteriaForm,
			@SortDefault(sort="JUDGMENT_DATE") Pageable pageable,
			ModelMap model, HttpServletRequest request) {

		SearchResults<JudgmentSearchResult> resultSearchResults = judgmentsWebSearchService.search(judgmentCriteriaForm, pageable);

		model.addAttribute("pageable", pageable);
		model.addAttribute("resultSearch", resultSearchResults);
		model.addAttribute("pageLink", PageLinkGenerator.generateSearchPageBaseLink(request));
		model.addAttribute("totalPages", (int)Math.ceil(((double)resultSearchResults.getTotalResults())/((double)pageable.getPageSize())));
		
		return "searchResults";
	}
	
	@RequestMapping("/result/{id}")
	public String JudgmentSignleResult(ModelMap model, @PathVariable int id) {
		
		Judgment judgment = judgmentRepository.findOneAndInitialize(id);
		model.addAttribute("judgment", judgment);
		
		return "singleResult";
	}
	

	/* REMOVE 
	 * Old search results - judgment list with mock items
	 * */
	@RequestMapping("/results_MOCK")
	public String JudgmentResults(ModelMap model) {
		
		List<Judgment> judgments = new ArrayList<Judgment>();
		judgments.add(judgmentRepository.findOneAndInitialize(1));
		judgments.add(judgmentRepository.findOneAndInitialize(2));
		judgments.add(judgmentRepository.findOneAndInitialize(3));
		judgments.add(judgmentRepository.findOneAndInitialize(4));
		
		model.addAttribute("judgments", judgments);
		
		return "results";
	}
	
}