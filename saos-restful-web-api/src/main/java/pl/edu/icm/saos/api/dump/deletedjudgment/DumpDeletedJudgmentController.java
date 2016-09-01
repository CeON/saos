package pl.edu.icm.saos.api.dump.deletedjudgment;

import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkAcceptHeader;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkRequestMethod;
import static pl.edu.icm.saos.api.services.links.ControllerProxyLinkBuilder.linkTo;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.api.dump.deletedjudgment.views.DumpDeletedJudgmentsView;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsNames;
import pl.edu.icm.saos.persistence.repository.DeletedJudgmentRepository;

/**
 * A controller that returns a view with an array of deleted judgment ids
 * @author Łukasz Dumiszewski
 */
@Controller
@RequestMapping("/api/dump/judgments/deleted")
public class DumpDeletedJudgmentController extends ControllersEntityExceptionHandler {


    private DumpDeletedJudgmentsSuccessRepresentationBuilder representationBuilder;

    private DeletedJudgmentRepository deletedJudgmentRepository;


    //------------------------ LOGIC --------------------------
    
    
    @RequestMapping(value = "")
    @RestrictParamsNames
    @ResponseBody
    public ResponseEntity<Object> showDeletedJudgments(@RequestHeader(value = "Accept", required = false) String acceptHeader, HttpServletRequest request) throws WrongRequestParameterException {
        
        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);
        
        
        List<Long> deletedJudgmentIds = deletedJudgmentRepository.findAllJudgmentIds();


        DumpDeletedJudgmentsView representation = representationBuilder.build(deletedJudgmentIds, linkTo(DumpDeletedJudgmentController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }


    //------------------------ SETTERS --------------------------

    
    @Autowired
    public void setRepresentationBuilder(DumpDeletedJudgmentsSuccessRepresentationBuilder representationBuilder) {
        this.representationBuilder = representationBuilder;
    }
    
    @Autowired
    public void setDeletedJudgmentRepository(DeletedJudgmentRepository deletedJudgmentRepository) {
        this.deletedJudgmentRepository = deletedJudgmentRepository;
    }


}
