package pl.edu.icm.saos.api.search;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.api.entry.point.LinkWithDescription;
import pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder;
import pl.edu.icm.saos.api.search.courts.CourtsController;
import pl.edu.icm.saos.api.search.judgments.JudgmentsController;

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/search")
public class SearchEntryPointController {

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> show()  {

        LinkWithDescription courts = new LinkWithDescriptionBuilder()
                .rel("courts")
                .href(CourtsController.class)
                .description("Allows search courts")
                .build();

        LinkWithDescription judgments = new LinkWithDescriptionBuilder()
                .rel("judgments")
                .href(JudgmentsController.class)
                .description("Allows serach judgments")
                .build();

        return new ResponseEntity<>(createLinksRepresentation(courts, judgments), HttpStatus.OK);
    }
}
