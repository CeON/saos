package pl.edu.icm.saos.webapp.judgment.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;
import pl.edu.icm.saos.webapp.judgment.detail.correction.JudgmentCorrectionService;
import pl.edu.icm.saos.webapp.judgment.detail.refcourtcases.ReferencingJudgmentsService;

/**
 * Provides view of single judgment.
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class JudgmentDetailsController {

	@Autowired
	private JudgmentEnrichmentService judgmentEnrichmentService;
	
	@Autowired
	private JudgmentCorrectionService judgmentCorrectionService;
	
	@Autowired 
	private JudgmentDetailsSortService judgmentDetailsSortService;
	
	@Autowired
	private ReferencingJudgmentsService referencingJudgmentsService;
	
	
	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/judgments/{judgmentId}")
	public String showJudgmentDetails(ModelMap model, @PathVariable("judgmentId") long judgmentId) {		
		
	    Judgment judgment = judgmentDetailsSortService.sortJudges(judgmentEnrichmentService.findOneAndEnrich(judgmentId));
	    
	    String formattedTextContent = judgment.getRawTextContent();
	    if (judgment.getTextContent().getType() != ContentType.HTML) {
	        formattedTextContent = formattedTextContent.replaceAll("\\n", "<br />");
	    }
	    
		model.addAttribute("judgment", judgment);
		model.addAttribute("corrections", judgmentCorrectionService.findAllByJudgmentIdSorted(judgmentId));
		model.addAttribute("formattedTextContent", formattedTextContent);
		model.addAttribute("referencingCount", referencingJudgmentsService.fetchReferencingJudgmentsCount(judgment.getId()));
		
		return "judgmentDetails";
	}
}
