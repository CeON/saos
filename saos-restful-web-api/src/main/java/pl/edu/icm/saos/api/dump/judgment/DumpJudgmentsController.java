package pl.edu.icm.saos.api.dump.judgment;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;

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

import pl.edu.icm.saos.api.dump.judgment.parameters.RequestDumpJudgmentsParameters;
import pl.edu.icm.saos.api.dump.judgment.views.DumpJudgmentsView;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsNames;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentDbSearchService;
import pl.edu.icm.saos.persistence.common.FieldsNames;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * Represents page of list of judgments in dump service
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump/judgments")
public class DumpJudgmentsController extends ControllersEntityExceptionHandler{

    
    private ParametersExtractor parametersExtractor;

    private JudgmentEnrichmentDbSearchService judgmentEnrichmentDbSearchService;

    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;


    
    //------------------------ LOGIC --------------------------
    
    
    @RequestMapping(value = "",  method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @RestrictParamsNames
    @ResponseBody
    public ResponseEntity<Object> showJudgments(
            @ModelAttribute RequestDumpJudgmentsParameters requestDumpJudgmentsParameters,
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber
    ) throws WrongRequestParameterException {



        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);


        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .limit(pagination.getPageSize())
                .offset(pagination.getOffset())
                .startDate(requestDumpJudgmentsParameters.getJudgmentStartDate())
                .endDate(requestDumpJudgmentsParameters.getJudgmentEndDate())
                .sinceModificationDateTime(requestDumpJudgmentsParameters.getSinceModificationDate())
                .withGenerated(requestDumpJudgmentsParameters.isWithGenerated())
                .upBy(FieldsNames.JUDGMENT_DATE)
                .filter();

        SearchResult<Judgment> searchResult = judgmentEnrichmentDbSearchService.search(searchFilter);


        DumpJudgmentsView representation = dumpJudgmentsListSuccessRepresentationBuilder
                .build(searchResult, pagination, requestDumpJudgmentsParameters, linkTo(DumpJudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }

    @Autowired
    public void setDumpJudgmentsListSuccessRepresentationBuilder(DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder) {
        this.dumpJudgmentsListSuccessRepresentationBuilder = dumpJudgmentsListSuccessRepresentationBuilder;
    }

    @Autowired
    public void setJudgmentEnrichmentDbSearchService(
            JudgmentEnrichmentDbSearchService judgmentEnrichmentDbSearchService) {
        this.judgmentEnrichmentDbSearchService = judgmentEnrichmentDbSearchService;
    }
}
