package pl.edu.icm.saos.api.single.scdivision;

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
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;

/**
 * Provides functionality for creating view of single supreme court division
 * @author pavtel
 */
@Controller
@RequestMapping("/api/scDivisions/{divisionId}")
public class ScDivisionController extends ControllersEntityExceptionHandler {

    @Autowired
    private ScDivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;


    //------------------------ LOGIC --------------------------
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> showDivision(@PathVariable("divisionId") int divisionId) throws ElementDoesNotExistException {

        SupremeCourtChamberDivision division = scChamberDivisionRepository.findOne(divisionId);
        if(division == null){
            throw new ElementDoesNotExistException("Division", divisionId);
        }

        Object representation = divisionSuccessRepresentationBuilder.build(division);

        HttpHeaders httpHeaders = new HttpHeaders();


        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------

    public void setDivisionSuccessRepresentationBuilder(ScDivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder) {
        this.divisionSuccessRepresentationBuilder = divisionSuccessRepresentationBuilder;
    }

    public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
        this.scChamberDivisionRepository = scChamberDivisionRepository;
    }
}
