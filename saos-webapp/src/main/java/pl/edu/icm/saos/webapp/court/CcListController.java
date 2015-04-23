package pl.edu.icm.saos.webapp.court;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Provides view of common courts divisions in json format.
 * 
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class CcListController {	
    
    @Autowired
    private CcListService ccListService;
    
    
    //------------------------ LOGIC --------------------------
    	
    @RequestMapping("cc/courts/list")
    @ResponseBody
    public List<SimpleEntity> listCcCourts() {
	return ccListService.findCommonCourts();
    }
    	
    @RequestMapping("cc/courts/{commonCourtId}/courtDivisions/list")
    @ResponseBody
    public List<SimpleEntity> listCcCourtDivisions(@PathVariable("commonCourtId") long commonCourtId) {
	return ccListService.findCcDivisions(commonCourtId);
    }

}
