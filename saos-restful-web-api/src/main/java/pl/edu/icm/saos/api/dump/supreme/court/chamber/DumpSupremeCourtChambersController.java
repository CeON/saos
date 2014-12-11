package pl.edu.icm.saos.api.dump.supreme.court.chamber;

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
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsNames;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.SupremeCourtChamberSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;

/**
 * Represents page of list of supreme court chambers in dump service
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump/scChambers")
public class DumpSupremeCourtChambersController extends ControllersEntityExceptionHandler {

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpScChambersListsSuccessRepresentationBuilder dumpScChambersListsSuccessRepresentationBuilder;


    //------------------------ LOGIC --------------------------
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @RestrictParamsNames
    @ResponseBody
    public ResponseEntity<Object> showChambers(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);

        SupremeCourtChamberSearchFilter searchFilter = SupremeCourtChamberSearchFilter.builder()
                .limit(pagination.getPageSize())
                .offset(pagination.getOffset())
                .filter();

        SearchResult<SupremeCourtChamber> searchResult = databaseSearchService.search(searchFilter);


        Object representation = dumpScChambersListsSuccessRepresentationBuilder
                .build(searchResult, pagination, linkTo(DumpSupremeCourtChambersController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }

    public void setDatabaseSearchService(DatabaseSearchService databaseSearchService) {
        this.databaseSearchService = databaseSearchService;
    }

    public void setDumpScChambersListsSuccessRepresentationBuilder(DumpScChambersListsSuccessRepresentationBuilder dumpScChambersListsSuccessRepresentationBuilder) {
        this.dumpScChambersListsSuccessRepresentationBuilder = dumpScChambersListsSuccessRepresentationBuilder;
    }
}
