package pl.edu.icm.saos.webapp.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.common.http.HttpServletRequestUtils;


/**
 * Provides simple home view.
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class HomeController {


	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/")
	public String home(ModelMap model, HttpServletRequest request) {
	    model.addAttribute("homePageUrl", HttpServletRequestUtils.constructRequestUrl(request));
		return "home";
	}
	
	@RequestMapping("/angular/")
	public String angularView() {
		
		return "angularSearch";
	}

    
}

