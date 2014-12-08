package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class ScListController {

    @Autowired
    private ScListService scListService;

    @Autowired
    private SimpleDivisionConverter simpleDivisionConverter;
	
    
	//------------------------ LOGIC --------------------------    
    
	@RequestMapping("sc/courts/{supremeChamberId}/chamberDivisions/list")
	@ResponseBody
	public List<SimpleDivision> findChamberDivisionList(@PathVariable("supremeChamberId") int supremeChamberId) {
		return simpleDivisionConverter.convertScChamberDivisions(scListService.findScChamberDivisions(supremeChamberId));
	}
    
}
