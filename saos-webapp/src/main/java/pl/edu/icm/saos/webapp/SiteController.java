package pl.edu.icm.saos.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SiteController {

	
    @RequestMapping("/")
    public String home(ModelMap model) {
        
        return "content";
    }
    
    @RequestMapping("/old/")
    public String oldHome() {
    	
    	return "home"; 
    }
    
    @RequestMapping("/old/search")
    public String oldSearch() {
    	
    	return "basicSearch";
    }
    
    @RequestMapping("/angular/")
    public String angularView() {
    	
    	return "angularSearch";
    }

    
}