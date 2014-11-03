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
import pl.edu.icm.saos.api.search.courts.services.CourtsSearchService;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.search.parameters.RequestParameters;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException;

import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;


/**
 * Provides functionality for constructing view for list of courts.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/search/courts")
public class CourtsController extends ControllersEntityExceptionHandler{

    // ******** fields *********
    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private CourtsSearchService courtsSearchService;



    //*********** END fields *************


    //********* business methods **************

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> showCourts(
            @RequestParam(value = PAGE_SIZE, required = false, defaultValue = Pagination.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = PAGE_NUMBER, required = false, defaultValue = "0") int pageNumber
    ) throws WrongRequestParameterException {

        Pagination pagination = parametersExtractor.extractAndValidatePagination(pageSize, pageNumber);

        Object results = courtsSearchService.performSearch(new RequestParameters(null, pagination));

//        Map<String, Object> representation = successRepresentationBuilder.build(results);


        //TODO add implementation when court will be in the solr index
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    //*********** END business methods ************


    //*** setters ***

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }

    public void setCourtsSearchService(CourtsSearchService courtsSearchService) {
        this.courtsSearchService = courtsSearchService;
    }

}
