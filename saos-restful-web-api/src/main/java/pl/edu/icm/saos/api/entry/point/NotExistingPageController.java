package pl.edu.icm.saos.api.entry.point;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.PageDoesNotExistException;

/**
 * Controller handling pages that are not found.
 * 
 * @author madryk
 */
@Controller
@RequestMapping("/api/**")
public class NotExistingPageController extends ControllersEntityExceptionHandler {


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "") 
    @ResponseBody
    public ResponseEntity<Object> showNotFoundError() {
        
        throw new PageDoesNotExistException();
    }
}
