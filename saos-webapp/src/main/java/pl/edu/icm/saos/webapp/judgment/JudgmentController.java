package pl.edu.icm.saos.webapp.judgment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

@Controller
public class JudgmentController {

    @Autowired
	private JudgmentRepository judgmentRepository;
	
	@RequestMapping("/results")
	public String JudgmentResults(ModelMap model) {
		
		model.addAttribute("judgment", judgmentRepository.findOneAndInitialize(1));
		
		return "results";
	}
}