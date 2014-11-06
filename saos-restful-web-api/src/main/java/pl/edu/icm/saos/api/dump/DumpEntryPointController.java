package pl.edu.icm.saos.api.dump;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.dump.court.DumpCourtsController;
import pl.edu.icm.saos.api.dump.judgment.DumpJudgmentsController;
import pl.edu.icm.saos.api.dump.supreme.court.chamber.DumpSupremeCourtChambersController;
import pl.edu.icm.saos.api.entry.point.LinkWithDescription;
import pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/dump")
public class DumpEntryPointController {

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> show()  {

        LinkWithDescription dumpCourts = new LinkWithDescriptionBuilder()
                .rel("courts")
                .href(DumpCourtsController.class)
                .description("Allows dump courts")
                .build();

        LinkWithDescription dumpJudgments = new LinkWithDescriptionBuilder()
                .rel("judgments")
                .href(DumpJudgmentsController.class)
                .description("Allows dump judgments")
                .build();

        LinkWithDescription dumpScChambers = new LinkWithDescriptionBuilder()
                .rel("scChambers")
                .href(DumpSupremeCourtChambersController.class)
                .description("Allows dump supreme court chambers")
                .build();

        return new ResponseEntity<>(createLinksRepresentation(dumpCourts, dumpJudgments, dumpScChambers), HttpStatus.OK);
    }
}
