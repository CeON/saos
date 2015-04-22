package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Provides json format view of supreme court chambers, supreme division chambers,
 * supreme courts judgment forms.
 * 
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class ScListController {

    		
    @Autowired
    private ScListService scListService;
    	
    	
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
	return scListService.findScJudgmentForms();
    }

}
