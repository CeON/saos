package pl.edu.icm.saos.api.search.judgments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.search.judgments.extractors.JudgmentsParametersExtractor;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.services.ApiSearchService;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.search.judgments.extractors.JudgmentsParametersExtractor.inputParameters;


/**
 * Provides functionality for constructing view for list of judgments.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/judgments")
public class JudgmentsController extends ControllersEntityExceptionHandler {

    //******* fields *************
    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ApiSearchService<Judgment, JudgmentsParameters> apiSearchService;

    @Autowired
    private JudgmentsParametersExtractor parametersExtractor;

    //*********** END fields ***************


    //******** business methods ***************
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgments(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = "0") int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = ALL, required = false) String all,
            @RequestParam(value = COURT_NAME, required = false) String courtName,
            @RequestParam(value = LEGAL_BASE, required = false) String legalBase,
            @RequestParam(value = REFERENCED_REGULATION, required = false) String referencedRegulation,
            @RequestParam(value = JUDGE_NAME, required = false) String judgeName,
            @RequestParam(value = KEYWORD, required = false) String keyword,
            @RequestParam(value = JUDGMENT_DATE_FROM, required = false) String judgmentDateFrom,
            @RequestParam(value = JUDGMENT_DATE_TO, required = false) String judgmentDateTo

    ) throws WrongRequestParameterException {

        JudgmentsParameters judgmentsParameters = parametersExtractor.extractFrom(inputParameters()
                        .all(all)
                        .courtName(courtName)
                        .judgeName(judgeName)
                        .keyword(keyword)
                        .legalBase(legalBase)
                        .referencedRegulation(referencedRegulation)
                        .pageSize(pageSize)
                        .pageNumber(pageNumber)
                        .judgmentDateFrom(judgmentDateFrom)
                        .judgmentDateTo(judgmentDateTo)
        );



        ElementsSearchResults<Judgment, JudgmentsParameters>  searchResults = apiSearchService.performSearch(judgmentsParameters);

        Map<String, Object> representation = listSuccessRepresentationBuilder.build(searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //*********** END business methods **************



    //**** setters *******

    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    public void setApiSearchService(ApiSearchService<Judgment, JudgmentsParameters> apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    public void setParametersExtractor(JudgmentsParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
