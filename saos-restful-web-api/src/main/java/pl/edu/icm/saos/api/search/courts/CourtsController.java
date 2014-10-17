package pl.edu.icm.saos.api.search.courts;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.edu.icm.saos.api.search.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.services.ApiSearchService;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;


/**
 * Provides functionality for constructing view for list of courts.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/courts")
public class CourtsController extends ControllersEntityExceptionHandler{

    // ******** fields *********
    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private ApiSearchService<CommonCourt,RequestParameters> searchService;

    @Autowired
    private CourtsListSuccessRepresentationBuilder successRepresentationBuilder;

    private final String DEFAULT_OFFSET = "0";

    //*********** END fields *************


    //********* business methods **************

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showCourts(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = DEFAULT_OFFSET) int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = DEFAULT_OFFSET) int pageNumber
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);

        ElementsSearchResults<CommonCourt,RequestParameters> results = searchService.performSearch(new RequestParameters(null, pagination));

        Map<String, Object> representation = successRepresentationBuilder.build(results);


        return new ResponseEntity<>(representation, HttpStatus.OK);
    }

    //*********** END business methods ************


    //*** setters ***

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }

    public void setSearchService(ApiSearchService<CommonCourt,RequestParameters> searchService) {
        this.searchService = searchService;
    }

    public void setSuccessRepresentationBuilder(CourtsListSuccessRepresentationBuilder successRepresentationBuilder) {
        this.successRepresentationBuilder = successRepresentationBuilder;
    }
}
