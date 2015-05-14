package pl.edu.icm.saos.api.entry.point;

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
import pl.edu.icm.saos.api.dump.DumpEntryPointController;
import pl.edu.icm.saos.api.search.SearchEntryPointController;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;

import java.util.Locale;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api")
public class MainEntryPointController extends ControllersEntityExceptionHandler {


    @Autowired
    @Qualifier("apiMessageSource")
    private MessageSource apiMessageService;

    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8"})
    @ResponseBody
    public ResponseEntity<Object> show(Locale locale)  {

        LinkWithDescription dumpEntryPoint = new LinkWithDescriptionBuilder()
                .rel("dump")
                .href(DumpEntryPointController.class)
                .description(apiMessageService.getMessage("dump.entry.point", null, locale))
                .build();

        LinkWithDescription searchEntryPoint = new LinkWithDescriptionBuilder()
                .rel("search")
                .href(SearchEntryPointController.class)
                .description(apiMessageService.getMessage("search.entry.point", null, locale))
                .build();

        return new ResponseEntity<>(createLinksRepresentation(dumpEntryPoint, searchEntryPoint), HttpStatus.OK);
    }

    //------------------------ SETTERS --------------------------
    public void setApiMessageService(MessageSource apiMessageService) {
        this.apiMessageService = apiMessageService;
    }
}
