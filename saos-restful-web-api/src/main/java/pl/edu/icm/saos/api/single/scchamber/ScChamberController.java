package pl.edu.icm.saos.api.single.scchamber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.ElementDoesNotExistException;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * Provides functionality for creating view of single supreme court chamber
 * @author pavtel
 */
@Controller
@RequestMapping("/api/scChambers/{chamberId}")
public class ScChamberController extends ControllersEntityExceptionHandler {

    @Autowired
    private ScChamberSuccessRepresentationBuilder scChamberRepresentationBuilder;

    @Autowired
    private ScChamberRepository scChamberRepository;

    //------------------------ LOGIC --------------------------
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> showChamber(@PathVariable("chamberId") int chamberId) throws ElementDoesNotExistException {

        SupremeCourtChamber chamber = scChamberRepository.findOneAndInitialize(chamberId);
        if(chamber == null){
            throw new ElementDoesNotExistException("Chamber", chamberId);
        }

        Object representation = scChamberRepresentationBuilder.build(chamber);
        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------

    public void setScChamberRepresentationBuilder(ScChamberSuccessRepresentationBuilder scChamberRepresentationBuilder) {
        this.scChamberRepresentationBuilder = scChamberRepresentationBuilder;
    }

    public void setScChamberRepository(ScChamberRepository scChamberRepository) {
        this.scChamberRepository = scChamberRepository;
    }
}
