package pl.edu.icm.saos.webapp.judgment.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class JudgmentDetailController {

	
	@Autowired
	private JudgmentRepository judgmentRepository;
	
	
	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/judgments/{judgmentId}")
	public String JudgmentSignleResult(ModelMap model, @PathVariable("judgmentId") Integer judgmentId) {
	
		model.addAttribute("judgment", judgmentRepository.findOneAndInitialize(judgmentId));
	
	return "judgmentDetails";
	}
}
