package pl.edu.icm.saos.api.single.judgment;

import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkAcceptHeader;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkRequestMethod;

import javax.persistence.EntityNotFoundException;
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
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Provides functionality for constructing view for single judgment.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/judgments/{judgmentId}")
public class JudgmentController extends ControllersEntityExceptionHandler{

    private JudgmentEnrichmentService judgmentEnrichmentService;

    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    
    
    
    //------------------------ LOGIC --------------------------
    
    @RequestMapping(value = "")
    @ResponseBody
    public ResponseEntity<Object> showJudgment(
            @RequestHeader(value = "Accept", required = false) String acceptHeader,
            @PathVariable("judgmentId") long judgmentId, HttpServletRequest request) throws ElementDoesNotExistException {

        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);


        Judgment judgment = null;
        try {
            judgment = judgmentEnrichmentService.findOneAndEnrich(judgmentId);
        } catch (EntityNotFoundException e) {
            throw new ElementDoesNotExistException("Judgment", judgmentId);
        }
        

        Object representation = singleJudgmentSuccessRepresentationBuilder.build(judgment);

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
        this.judgmentEnrichmentService = judgmentEnrichmentService;
    }
    
    @Autowired
    public void setSingleJudgmentSuccessRepresentationBuilder(SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder) {
        this.singleJudgmentSuccessRepresentationBuilder = singleJudgmentSuccessRepresentationBuilder;
    }
}
