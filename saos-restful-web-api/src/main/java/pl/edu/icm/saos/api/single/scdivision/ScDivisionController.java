package pl.edu.icm.saos.api.single.scdivision;

import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkAcceptHeader;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkRequestMethod;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
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

    private ScDivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    private ScChamberDivisionRepository scChamberDivisionRepository;


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "")
    @ResponseBody
    public ResponseEntity<Object> showDivision(
            @RequestHeader(value = "Accept", required = false) String acceptHeader,
            @PathVariable("divisionId") long divisionId, HttpServletRequest request) throws ElementDoesNotExistException {

        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);


        SupremeCourtChamberDivision division = scChamberDivisionRepository.findOne(divisionId);
        if(division == null){
            throw new ElementDoesNotExistException("Division", divisionId);
        }

        Object representation = divisionSuccessRepresentationBuilder.build(division);

        HttpHeaders httpHeaders = new HttpHeaders();


        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------

    @Autowired
    public void setDivisionSuccessRepresentationBuilder(ScDivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder) {
        this.divisionSuccessRepresentationBuilder = divisionSuccessRepresentationBuilder;
    }

    @Autowired
    public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
        this.scChamberDivisionRepository = scChamberDivisionRepository;
    }
}
