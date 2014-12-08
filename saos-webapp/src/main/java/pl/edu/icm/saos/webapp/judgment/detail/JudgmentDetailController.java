package pl.edu.icm.saos.webapp.judgment.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.model.Judgment;
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
	public String JudgmentSignleResult(ModelMap model, @PathVariable int judgmentId) {
		
		Judgment judgment = judgmentRepository.findOneAndInitialize(judgmentId);
		model.addAttribute("judgment", judgment);
		
		return "singleResult";
	}
}
