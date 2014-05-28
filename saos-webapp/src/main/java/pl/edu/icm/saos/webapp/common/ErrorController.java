package pl.edu.icm.saos.webapp.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Łukasz Dumiszewski
 */
@Controller
@RequestMapping("/errors")
public class ErrorController {


    @RequestMapping("/404")
    public String error404() {
        return "error404";
    }
    
    @RequestMapping("/500")
    public String error500() {
        return "error500";
    }
    
}
