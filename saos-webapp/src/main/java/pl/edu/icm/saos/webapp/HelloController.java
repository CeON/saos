package pl.edu.icm.saos.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    
    @RequestMapping("/")
    public String index(ModelMap model) {
        model.addAttribute("thymeleafPrefix", "bleble");
        return "index";
    }

}