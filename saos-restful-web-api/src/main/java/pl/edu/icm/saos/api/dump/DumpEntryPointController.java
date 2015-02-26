package pl.edu.icm.saos.api.dump;

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

import pl.edu.icm.saos.api.dump.court.DumpCommonCourtsController;
import pl.edu.icm.saos.api.dump.enrichmenttag.DumpEnrichmentTagController;
import pl.edu.icm.saos.api.dump.judgment.DumpJudgmentsController;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.DumpSupremeCourtChambersController;
import pl.edu.icm.saos.api.entry.point.LinkWithDescription;
import pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder;

import java.util.Locale;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump")
public class DumpEntryPointController {

    @Autowired
    @Qualifier("apiMessageSource")
    private MessageSource apiMessageService;

    //------------------------ LOGIC --------------------------

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> show(Locale locale)  {

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
    public void setApiMessageService(MessageSource apiMessageService) {
        this.apiMessageService = apiMessageService;
    }
}
