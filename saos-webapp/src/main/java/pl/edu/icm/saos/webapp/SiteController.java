package pl.edu.icm.saos.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SiteController {

    
    @RequestMapping("/")
    public String home(ModelMap model) {
        
        return "home";
    }
    
    @RequestMapping("/search")
    public String search(ModelMap model) {
        return "search";
    }
    
    @RequestMapping("/results")
    public String results(ModelMap model) {
        return "results";
    }
    
    @RequestMapping("/modern/")
    public String modernHome() {
    	
    	return "content"; 
    }
    
    @RequestMapping("/modern/search")
    public String modernSearch() {
    	
    	return "basicSearch";
    }
    
    @RequestMapping("/angular/")
    public String angularView() {
    	
    	return "angularSearch";
    }

    
}