package pl.edu.icm.saos.api.judgments;

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
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.*;


/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/judgments")
public class JudgmentsController {

    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ApiSearchService<Judgment> apiSearchService;

    @Autowired
    private ParametersExtractor parametersExtractor;

    private final String DEFAULT_OFFSET = "0";

    public JudgmentsController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showJudgments(
            @RequestParam(value = LIMIT, required = false, defaultValue = "0") int limit,
            @RequestParam(value = OFFSET, required = false, defaultValue = DEFAULT_OFFSET) int offset,
            @RequestParam(value = EXPAND, required = false) String expand
    ) throws WrongRequestParameterException {

        RequestParameters requestParameters = parametersExtractor.extractRequestParameter(expand, limit, offset);
        ElementsSearchResults searchResults = apiSearchService.performSearch(requestParameters);

        Map<String, Object> representation = listSuccessRepresentationBuilder.build(searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<Map<String, Object>>(representation, httpHeaders, HttpStatus.OK);
    }



    //**** setters *******

    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    public void setApiSearchService(ApiSearchService<Judgment> apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
