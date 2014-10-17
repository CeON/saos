package pl.edu.icm.saos.api.entry.point;

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

import static pl.edu.icm.saos.api.entry.point.LinkWithDescriptionBuilder.createLinksRepresentation;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api")
public class MainEntryPointController extends ControllersEntityExceptionHandler {


    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Object> show()  {

        LinkWithDescription dumpEntryPoint = new LinkWithDescriptionBuilder()
                .rel("dump")
                .href(DumpEntryPointController.class)
                .description("Dump entry point")
                .build();

        LinkWithDescription searchEntryPoint = new LinkWithDescriptionBuilder()
                .rel("search")
                .href(SearchEntryPointController.class)
                .description("Search entry point")
                .build();

        return new ResponseEntity<>(createLinksRepresentation(dumpEntryPoint, searchEntryPoint), HttpStatus.OK);
    }


}
