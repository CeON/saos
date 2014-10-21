package pl.edu.icm.saos.api.dump.judgment;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.persistence.common.FieldsNames;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;


import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump/judgments")
public class DumpJudgmentsController extends ControllersEntityExceptionHandler{

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;


    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgments(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = "0") int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = JUDGMENT_START_DATE, required = false) String judgmentStartDate,
            @RequestParam(value = JUDGMENT_END_DATE, required = false) String judgmentEndDate,
            @RequestParam(value = SINCE_MODIFICATION_DATE, required = false) String modificationDate
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);

        LocalDate startDate = parametersExtractor.extractLocalDate(judgmentStartDate, JUDGMENT_START_DATE);
        LocalDate endDate = parametersExtractor.extractLocalDate(judgmentEndDate, JUDGMENT_END_DATE);
        DateTime sinceModificationDateTime = parametersExtractor.extractDateTime(modificationDate, SINCE_MODIFICATION_DATE);

        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .limit(pagination.getPageSize())
                .offset(pagination.getPageNumber())
                .startDate(startDate)
                .endDate(endDate)
                .sinceModificationDateTime(sinceModificationDateTime)
                .upBy(FieldsNames.JUDGMENT_DATE)
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);


        Map<String, Object> representation = dumpJudgmentsListSuccessRepresentationBuilder
                .build(searchResult, pagination, judgmentStartDate, judgmentEndDate, modificationDate, linkTo(DumpJudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //*** setters ***

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }

    public void setDatabaseSearchService(DatabaseSearchService databaseSearchService) {
        this.databaseSearchService = databaseSearchService;
    }

    public void setDumpJudgmentsListSuccessRepresentationBuilder(DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder) {
        this.dumpJudgmentsListSuccessRepresentationBuilder = dumpJudgmentsListSuccessRepresentationBuilder;
    }
}
