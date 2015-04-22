package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;

/**
 * Provides view of supreme court chamber divisions in json format.
 * 
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class ScListController {

    		
    @Autowired
    private ScListService scListService;
    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    @Autowired
    private SimpleEntityConverter simpleEntityConverter;
    	
    	
    //------------------------ LOGIC --------------------------    
    	
    @RequestMapping("sc/chambers/list")
    @ResponseBody
    public List<SimpleEntity> listScChambers() {
	return scListService.findScChambers();
    }
    	
    @RequestMapping("sc/chambers/{supremeChamberId}/chamberDivisions/list")
    @ResponseBody
    public List<SimpleEntity> listScChamberDivisions(@PathVariable("supremeChamberId") long supremeChamberId) {
	return scListService.findScChamberDivisions(supremeChamberId);
    }
    
    @RequestMapping("/sc/judgmentForms/list")
    @ResponseBody
    public List<SimpleEntity> listScJudgmentForms() {
	return simpleEntityConverter.convertScJudgmentForms(scJudgmentFormRepository.findAll());
    }

}
