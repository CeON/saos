package pl.edu.icm.saos.api.divisions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;

import java.util.Map;

/**
 * Provides functionality for constructing view for single division.
 * @author pavtel
 */
@Controller
@RequestMapping("/api/divisions/{divisionId}")
public class DivisionController {

    //******** fields *********
    @Autowired
    private CcDivisionRepository ccDivisionRepository;

    @Autowired
    private DivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    //********* END fields ***********


    //******** business methods ************
    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> showDivision(@PathVariable("divisionId") int divisionId){

        CommonCourtDivision division = ccDivisionRepository.findOne(divisionId);

        Map<String, Object> representation = divisionSuccessRepresentationBuilder.build(division);

        HttpHeaders httpHeaders = new HttpHeaders();


        return new ResponseEntity<>(representation, httpHeaders, HttpStatus.OK);
    }
    //*********** END business methods ********


    //*** setters ***
    public void setCcDivisionRepository(CcDivisionRepository ccDivisionRepository) {
        this.ccDivisionRepository = ccDivisionRepository;
    }

    public void setDivisionSuccessRepresentationBuilder(DivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder) {
        this.divisionSuccessRepresentationBuilder = divisionSuccessRepresentationBuilder;
    }
}
