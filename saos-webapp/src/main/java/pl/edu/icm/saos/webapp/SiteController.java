package pl.edu.icm.saos.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

@Controller
public class SiteController {

	
    @RequestMapping("/")
    public String home(ModelMap model) {
        
        return "content";
    }
    
    @RequestMapping("/search")
    public String search(ModelMap model) {
        return "search";
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