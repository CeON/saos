package pl.edu.icm.saos.api.courts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.icm.saos.api.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.LIMIT;
import static pl.edu.icm.saos.api.ApiConstants.OFFSET;

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
            @RequestParam(value = LIMIT, required = false, defaultValue = "0") int limit,
            @RequestParam(value = OFFSET, required = false, defaultValue = DEFAULT_OFFSET) int offset
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(limit, offset);

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
