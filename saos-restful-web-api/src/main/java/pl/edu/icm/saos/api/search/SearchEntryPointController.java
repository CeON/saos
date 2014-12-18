package pl.edu.icm.saos.api.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.entry.point.LinkWithDescription;
import pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder;
import pl.edu.icm.saos.api.search.judgments.JudgmentsController;

import java.util.Locale;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * Creates view for search entry point.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/search")
public class SearchEntryPointController {

    @Autowired
    @Qualifier("apiMessageSource")
    private MessageSource apiMessageService;



    //------------------------ LOGIC --------------------------
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> show(Locale locale)  {


        LinkWithDescription judgments = new LinkWithDescriptionBuilder()
                .rel("judgments")
                .href(JudgmentsController.class)
                .description(apiMessageService.getMessage("search.judgments.description", null, locale))
                .build();

        return new ResponseEntity<>(createLinksRepresentation(judgments), HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------
    public void setApiMessageService(MessageSource apiMessageService) {
        this.apiMessageService = apiMessageService;
    }
}
