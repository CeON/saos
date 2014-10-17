package pl.edu.icm.saos.api.dump.court;

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
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump/courts")
public class DumpCourtsController extends ControllersEntityExceptionHandler {

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpCourtsListSuccessRepresentationBuilder dumpCourtsListSuccessRepresentationBuilder;


    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showCourts(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = "0") int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);



        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .limit(pagination.getPageSize())
                .offset(pagination.getPageNumber())
                .filter();

        SearchResult<CommonCourt> searchResult = databaseSearchService.search(searchFilter);


        Map<String, Object> representation = dumpCourtsListSuccessRepresentationBuilder
                .build(searchResult, pagination, linkTo(DumpCourtsController.class).toUriComponentsBuilder());

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

    public void setDumpCourtsListSuccessRepresentationBuilder(DumpCourtsListSuccessRepresentationBuilder dumpCourtsListSuccessRepresentationBuilder) {
        this.dumpCourtsListSuccessRepresentationBuilder = dumpCourtsListSuccessRepresentationBuilder;
    }
}
