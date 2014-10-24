package pl.edu.icm.saos.api.search.judgments;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.search.services.ApiSearchService;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;


/**
 * Provides functionality for constructing view for list of judgments.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/search/judgments")
public class JudgmentsController extends ControllersEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(JudgmentsController.class);


    //******* fields *************
    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ApiSearchService<Judgment, JudgmentsParameters> apiSearchService;

    @Autowired
    private ParametersExtractor parametersExtractor;

    //*********** END fields ***************


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgments(@ModelAttribute JudgmentsParameters judgmentsParameters,
                                                             @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
                                                             @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber
    ) throws WrongRequestParameterException {


        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);
        judgmentsParameters.setPagination(pagination);

        ElementsSearchResults<Judgment, JudgmentsParameters>  searchResults = apiSearchService.performSearch(judgmentsParameters);

        Map<String, Object> representation = listSuccessRepresentationBuilder.build(searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }





    //------------------------ SETTERS --------------------------

    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    public void setApiSearchService(ApiSearchService<Judgment, JudgmentsParameters> apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
