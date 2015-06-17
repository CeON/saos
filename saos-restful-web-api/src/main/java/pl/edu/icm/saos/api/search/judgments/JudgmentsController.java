package pl.edu.icm.saos.api.search.judgments;

import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.services.links.ControllerProxyLinkBuilder.linkTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.judgments.parameters.Sort;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.api.search.judgments.views.SearchJudgmentsView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsNames;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;


/**
 * Provides functionality for constructing view for list of judgments.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/search/judgments")
public class JudgmentsController extends ControllersEntityExceptionHandler {

    

    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private JudgmentsApiSearchService apiSearchService;

    @Autowired
    private ParametersExtractor parametersExtractor;



    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"})
    @RestrictParamsNames
    @ResponseBody
    public ResponseEntity<Object> showJudgments(@ModelAttribute JudgmentsParameters judgmentsParameters,
                                                             @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
                                                             @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber,
                                                             @ModelAttribute Sort sort

    ) throws WrongRequestParameterException {


        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);
        judgmentsParameters.setPagination(pagination);
        judgmentsParameters.setSort(sort);

        SearchResults<JudgmentSearchResult> searchResults = apiSearchService.performSearch(judgmentsParameters);

        SearchJudgmentsView representation = listSuccessRepresentationBuilder.build(judgmentsParameters, searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }





    //------------------------ SETTERS --------------------------

    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    public void setApiSearchService(JudgmentsApiSearchService apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
