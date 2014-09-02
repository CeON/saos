package pl.edu.icm.saos.webapp.judgment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

@Controller("webJudgmentController")
public class JudgmentController {

    @Autowired
	private JudgmentRepository judgmentRepository;
	
	@RequestMapping("/result/{id}")
	public String JudgmentSignleResult(ModelMap model, @PathVariable int id) {
		
		Judgment judgment = judgmentRepository.findOneAndInitialize(id);
		
		if(judgment instanceof CommonCourtJudgment){
			CommonCourtJudgment ddJ = (CommonCourtJudgment) judgment;
			model.addAttribute("judgment", ddJ);
		} else {
			model.addAttribute("judgment", judgment);
		}
		
		return "singleResult";
	}
	
	@RequestMapping("/results")
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