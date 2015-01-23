package pl.edu.icm.saos.webapp.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Provides site map view. 
 * 
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class SiteMapController {


	//------------------------ LOGIC --------------------------
	
	@RequestMapping("/siteMap")
	public String siteMap() {
		return "siteMap";
	}
}
