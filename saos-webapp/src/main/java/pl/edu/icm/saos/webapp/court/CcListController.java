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
public class CcListController {	
    
    @Autowired
    private CcListService ccListService;
    
    @Autowired
    private SimpleDivisionConverter simpleDivisionConverter;
    

	//------------------------ LOGIC --------------------------
    
	@RequestMapping("cc/courts/{commonCourtId}/courtDivisions/list")
	@ResponseBody
	public List<SimpleDivision> listCourtDivisions(@PathVariable("commonCourtId") int commonCourtId) {
		return ccListService.findCcDivisions(commonCourtId);
	}

}
