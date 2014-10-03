package pl.edu.icm.saos.api.judgments;

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
import pl.edu.icm.saos.api.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import java.util.Map;

/**
 * Provides functionality for constructing view for single judgment.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/judgments/{judgmentId}")
public class JudgmentController extends ControllersEntityExceptionHandler{

    //******** fields ****************

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    //********** END fields ****************


    //*************** business methods *******************

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgment(@PathVariable("judgmentId") int judgmentId){

        Judgment judgment = judgmentRepository.findOneAndInitialize(judgmentId);

        Map<String, Object> representation = singleJudgmentSuccessRepresentationBuilder.build(judgment);

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //*************** END business methods **********************



    //*** setters ****
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    public void setSingleJudgmentSuccessRepresentationBuilder(SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder) {
        this.singleJudgmentSuccessRepresentationBuilder = singleJudgmentSuccessRepresentationBuilder;
    }
}
