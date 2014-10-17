package pl.edu.icm.saos.api.single.court.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.DIVISIONS;

/**
 * * Converts {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt} elements
 * into objects which can be easily serialized as json
 * @author pavtel
 */
@Component
public class CourtAssembler {

    //******* fields *************
    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionMapper;

    @Autowired
    private FieldsMapper<CommonCourt> courtMapper;

    //*********** END fields *************



    //********** business methods ****************
    /**
     * Converts court (uses all important court's fields) into object which can be easily serialized as json.
     * @param court to convert.
     * @return Object which can be easily serialized as json.
     */
    public Map<String, Object> fieldsToItemRepresentation(CommonCourt court){
        Map<String, Object> item = courtMapper.fieldsToMap(court);

        List<Map<String, Object>> divisionsItems = court.getDivisions()
                .stream()
                .map(divisionMapper::commonFieldsToMap)
                .collect(Collectors.toList())
                ;

        item.put(DIVISIONS, divisionsItems);
        return item;
    }

    /**
     * Converts list of courts into list of objects
     * which can be easily serialized as json. Converts all important judgment's fields.
     * @param courts list of courts to convert.
     * @return List of Objects which can be easily serialized as json.
     */
    public List<Object> toItemsList(List<? extends CommonCourt> courts){

        List<Object> items = courts.stream()
                .map(this::fieldsToItemRepresentation)
                .collect(Collectors.toList());

        return items;
    }

    //************ END business methods ************



    //*** setters ***
    public void setDivisionMapper(FieldsMapper<CommonCourtDivision> divisionMapper) {
        this.divisionMapper = divisionMapper;
    }

    public void setCourtMapper(FieldsMapper<CommonCourt> courtMapper) {
        this.courtMapper = courtMapper;
    }
}
