package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Provides view of supreme court chamber divisions in json format.
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class ScListController {

		
	@Autowired
	private ScListService scListService;
	
	
	//------------------------ LOGIC --------------------------    
	
	@RequestMapping("sc/chambers/{supremeChamberId}/chamberDivisions/list")
	@ResponseBody
	public List<SimpleDivision> listChamberDivisions(@PathVariable("supremeChamberId") int supremeChamberId) {
		return scListService.findScChamberDivisions(supremeChamberId);
	}

}
