package pl.edu.icm.saos.api.courts.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import static pl.edu.icm.saos.api.ApiConstants.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pavtel
 */
@Component
public class CourtAssembler {

    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionMapper;

    @Autowired
    private FieldsMapper<CommonCourt> courtMapper;



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



    //*** setters ***
    public void setDivisionMapper(FieldsMapper<CommonCourtDivision> divisionMapper) {
        this.divisionMapper = divisionMapper;
    }

    public void setCourtMapper(FieldsMapper<CommonCourt> courtMapper) {
        this.courtMapper = courtMapper;
    }
}
