package pl.edu.icm.saos.api.dump.judgment;

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
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
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
public class DumpJudgmentsController {

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;


    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgments(
            @RequestParam(value = LIMIT, required = false, defaultValue = "0") int limit,
            @RequestParam(value = OFFSET, required = false, defaultValue = "0") int offset,
            @RequestParam(value = JUDGMENT_START_DATE, required = false) String judgmentStartDate,
            @RequestParam(value = JUDGMENT_END_DATE, required = false) String judgmentEndDate
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(limit, offset);

        LocalDate startDate = parametersExtractor.extractLocalDate(judgmentStartDate, JUDGMENT_START_DATE);
        LocalDate endDate = parametersExtractor.extractLocalDate(judgmentEndDate, JUDGMENT_END_DATE);

        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .limit(pagination.getLimit())
                .offset(pagination.getOffset())
                .startDate(startDate)
                .endDate(endDate)
                .upBy(FieldsNames.JUDGMENT_DATE)
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);


        Map<String, Object> representation = dumpJudgmentsListSuccessRepresentationBuilder
                .build(searchResult, pagination, judgmentStartDate, judgmentEndDate, linkTo(DumpJudgmentsController.class).toUriComponentsBuilder());

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
