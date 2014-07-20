package pl.edu.icm.saos.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    
    @RequestMapping("/")
    public String home(ModelMap model) {
        model.addAttribute("thymeleafPrefix", "bleble");
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

}