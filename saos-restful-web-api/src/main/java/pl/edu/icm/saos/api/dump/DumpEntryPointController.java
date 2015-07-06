package pl.edu.icm.saos.api.dump;

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

import pl.edu.icm.saos.api.dump.court.DumpCommonCourtsController;
import pl.edu.icm.saos.api.dump.enrichmenttag.DumpEnrichmentTagController;
import pl.edu.icm.saos.api.dump.judgment.DumpJudgmentsController;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.DumpSupremeCourtChambersController;
import pl.edu.icm.saos.api.entry.point.LinkWithDescription;
import pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump")
public class DumpEntryPointController {

    private MessageSource apiMessageService;


    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "")
    @ResponseBody
    public ResponseEntity<Object> show(
            @RequestHeader(value = "Accept", required = false) String acceptHeader, Locale locale, HttpServletRequest request)  {

        checkRequestMethod(request, HttpMethod.GET);
        checkAcceptHeader(acceptHeader, MediaType.APPLICATION_JSON);


        LinkWithDescription dumpCourts = new LinkWithDescriptionBuilder()
                .rel("commonCourts")
                .href(DumpCommonCourtsController.class)
                .description(apiMessageService.getMessage("dump.courts.description", null, locale))
                .build();

        LinkWithDescription dumpJudgments = new LinkWithDescriptionBuilder()
                .rel("judgments")
                .href(DumpJudgmentsController.class)
                .description(apiMessageService.getMessage("dump.judgments.description", null, locale))
                .build();

        LinkWithDescription dumpScChambers = new LinkWithDescriptionBuilder()
                .rel("scChambers")
                .href(DumpSupremeCourtChambersController.class)
                .description(apiMessageService.getMessage("dump.supreme.court.chambers.description", null, locale))
                .build();
        
        LinkWithDescription dumpEnrichmentTags = new LinkWithDescriptionBuilder()
                .rel("enrichments")
                .href(DumpEnrichmentTagController.class)
                .description(apiMessageService.getMessage("dump.judgments.enrichments.description", null, locale))
                .build();

        return new ResponseEntity<>(createLinksRepresentation(dumpCourts, dumpJudgments, dumpScChambers, dumpEnrichmentTags), HttpStatus.OK);
    }


    //------------------------ SETTERS --------------------------

    @Autowired
    @Qualifier("apiMessageSource")
    public void setApiMessageService(MessageSource apiMessageService) {
        this.apiMessageService = apiMessageService;
    }
}
