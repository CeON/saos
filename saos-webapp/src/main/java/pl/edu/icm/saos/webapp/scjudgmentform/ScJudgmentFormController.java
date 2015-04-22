package pl.edu.icm.saos.webapp.scjudgmentform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.webapp.court.SimpleEntity;
import pl.edu.icm.saos.webapp.court.SimpleEntityConverter;


@Controller
public class ScJudgmentFormController {
    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    @Autowired
    private SimpleEntityConverter simpleEntityConverter;
    
    //------------------------ LOGIC --------------------------    
    
    @RequestMapping("/sc/judgmentForm/list")
    @ResponseBody
    public List<SimpleEntity> scJudgmentFormList() {
	return simpleEntityConverter.convertScJudgmentForm(scJudgmentFormRepository.findAll());
    }
    
}

