package pl.edu.icm.saos.webapp.common;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Provides simple home view.
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class HomeController {


	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/")
	public String home(ModelMap model) {
	    
		return "home";
	}
	
	@RequestMapping("/angular/")
	public String angularView() {
		
		return "angularSearch";
	}

    
}

