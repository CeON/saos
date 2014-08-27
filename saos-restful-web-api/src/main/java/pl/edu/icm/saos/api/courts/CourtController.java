package pl.edu.icm.saos.api.courts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import java.util.Map;

/**
 * @author pavtel
 */
@Controller
@RequestMapping("/api/courts/{courtId}")
public class CourtController {

    @Autowired
    private CommonCourtRepository courtRepository;

    @Autowired
    private SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showCourt(@PathVariable("courtId") int courtId){

        CommonCourt court = courtRepository.findOne(courtId);

        Map<String, Object> representation = singleCourtSuccessRepresentationBuilder.build(court);

        return new ResponseEntity<Map<String, Object>>(representation, HttpStatus.OK);
    }


    //*** setters ****
    public void setCourtRepository(CommonCourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    public void setSingleCourtSuccessRepresentationBuilder(SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder) {
        this.singleCourtSuccessRepresentationBuilder = singleCourtSuccessRepresentationBuilder;
    }
}
