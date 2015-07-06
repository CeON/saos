package pl.edu.icm.saos.api.entry.point;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkAcceptHeader;
import static pl.edu.icm.saos.api.services.exceptions.HttpServletRequestVerifyUtils.checkRequestMethod;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.api.dump.DumpEntryPointController;
import pl.edu.icm.saos.api.search.SearchEntryPointController;
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api")
public class MainEntryPointController extends ControllersEntityExceptionHandler {


    private MessageSource apiMessageService;


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "")
    @ResponseBody
    public ResponseEntity<Object> show(
            @RequestHeader(value = "Accept", required = false) String acceptHeader, Locale locale, HttpServletRequest request) {

        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);


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
    
    @Autowired
    @Qualifier("apiMessageSource")
    public void setApiMessageService(MessageSource apiMessageService) {
        this.apiMessageService = apiMessageService;
    }
}
