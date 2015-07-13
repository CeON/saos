package pl.edu.icm.saos.api.search.judgments;

import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkAcceptHeader;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkRequestMethod;
import static pl.edu.icm.saos.api.services.links.ControllerProxyLinkBuilder.linkTo;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.api.ApiConstants;
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


    private int maxPageSize = 100;

    private int minPageSize = 1;


    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    private JudgmentsApiSearchService apiSearchService;

    private ParametersExtractor parametersExtractor;



    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "")
    @RestrictParamsNames
    @ResponseBody
    public ResponseEntity<Object> showJudgments(
            @RequestHeader(value = "Accept", required = false) String acceptHeader,
            @ModelAttribute JudgmentsParameters judgmentsParameters,
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber,
            @ModelAttribute Sort sort,
            HttpServletRequest request

    ) throws WrongRequestParameterException {


        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber, minPageSize, maxPageSize);
        validateSort(sort);
        judgmentsParameters.setPagination(pagination);
        judgmentsParameters.setSort(sort);

        SearchResults<JudgmentSearchResult> searchResults = apiSearchService.performSearch(judgmentsParameters);

        SearchJudgmentsView representation = listSuccessRepresentationBuilder.build(judgmentsParameters, searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }


    //------------------------ PRIVATE --------------------------

    private void validateSort(Sort sort) {
        if (!ArrayUtils.contains(ApiConstants.ALLOWED_SORTING_FIELDS, sort.getSortingField())) {
            throw new WrongRequestParameterException("sortingField", "can't have value '" + sort.getSortingField().name() + "'");
        }
    }


    //------------------------ SETTERS --------------------------

    @Value("${restful.api.search.max.page.size}")
    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    @Value("${restful.api.search.min.page.size}")
    public void setMinPageSize(int minPageSize) {
        this.minPageSize = minPageSize;
    }

    @Autowired
    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    @Autowired
    public void setApiSearchService(JudgmentsApiSearchService apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    @Autowired
    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }



}
