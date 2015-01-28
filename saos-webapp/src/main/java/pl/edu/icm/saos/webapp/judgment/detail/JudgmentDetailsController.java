package pl.edu.icm.saos.webapp.judgment.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.webapp.judgment.detail.correction.JudgmentCorrectionService;

/**
 * Provides view of single judgment.
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class JudgmentDetailsController {

	
	@Autowired
	private JudgmentRepository judgmentRepository;
	
	@Autowired
	private JudgmentCorrectionService judgmentCorrectionService;
	
	
	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/judgments/{judgmentId}")
	public String showJudgmentDetails(ModelMap model, @PathVariable("judgmentId") long judgmentId) {
		
		model.addAttribute("judgment", judgmentRepository.findOneAndInitialize(judgmentId));
		model.addAttribute("corrections", judgmentCorrectionService.findAllByJudgmentIdSorted(judgmentId));
		
		return "judgmentDetails";
	}
}
