package pl.edu.icm.saos.webapp.api;

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
import pl.edu.icm.saos.api.judgments.JudgmentsListSuccessRepresentationBuilder;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.JudgmentsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.webapp.api.services.ApiSearchService;

import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/judgments")
public class JudgmentsController {

    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ApiSearchService apiSearchService;

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
        JudgmentsSearchResults searchResults = apiSearchService.performSearch(requestParameters);

        Map<String, Object> representation = listSuccessRepresentationBuilder.build(searchResults,
                 linkTo(JudgmentsController.class).toUriComponentsBuilder());

        HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<Map<String, Object>>(representation, httpHeaders, HttpStatus.OK);
    }



    //**** setters *******

    public void setListSuccessRepresentationBuilder(JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder) {
        this.listSuccessRepresentationBuilder = listSuccessRepresentationBuilder;
    }

    public void setApiSearchService(ApiSearchService apiSearchService) {
        this.apiSearchService = apiSearchService;
    }

    public void setParametersExtractor(ParametersExtractor parametersExtractor) {
        this.parametersExtractor = parametersExtractor;
    }
}
