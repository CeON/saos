package pl.edu.icm.saos.api.single.scchamber;

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
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

/**
 * Provides functionality for creating view of single supreme court chamber
 * @author pavtel
 */
@Controller
@RequestMapping("/api/scChambers/{chamberId}")
public class ScChamberController extends ControllersEntityExceptionHandler {

    private ScChamberSuccessRepresentationBuilder scChamberRepresentationBuilder;

    private ScChamberRepository scChamberRepository;


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "")
    @ResponseBody
    public ResponseEntity<Object> showChamber(
            @RequestHeader(value = "Accept", required = false) String acceptHeader,
            @PathVariable("chamberId") long chamberId, HttpServletRequest request) throws ElementDoesNotExistException {

        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);


        SupremeCourtChamber chamber = scChamberRepository.findOneAndInitialize(chamberId);
        if(chamber == null){
            throw new ElementDoesNotExistException("Chamber", chamberId);
        }

        Object representation = scChamberRepresentationBuilder.build(chamber);
        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }


    //------------------------ SETTERS --------------------------

    @Autowired
    public void setScChamberRepresentationBuilder(ScChamberSuccessRepresentationBuilder scChamberRepresentationBuilder) {
        this.scChamberRepresentationBuilder = scChamberRepresentationBuilder;
    }

    @Autowired
    public void setScChamberRepository(ScChamberRepository scChamberRepository) {
        this.scChamberRepository = scChamberRepository;
    }
}
