package pl.edu.icm.saos.api.dump.court.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.DIVISIONS;

/**
 * @author pavtel
 */
@Component
public class DumpCourtAssembler {

    @Autowired
    private FieldsMapper<CommonCourt> courtFieldsMapper;

    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionMapper;



    public Map<String, Object> toItemRepresentation(CommonCourt court){
        Map<String, Object> data = courtFieldsMapper.fieldsToMap(court, true);


        List<Map<String, Object>> divisionsItems = court.getDivisions()
                .stream()
                .map(division -> divisionMapper.fieldsToMap(division, true))
                .collect(Collectors.toList());

        data.put(DIVISIONS, divisionsItems);

        return data;
    }

    public List<Object> toItemsList(List<CommonCourt> courts){
        List<Object> items = courts
                .stream()
                .map(this::toItemRepresentation)
                .collect(Collectors.toList());

        return items;
    }


    //*** setters ***
    public void setCourtFieldsMapper(FieldsMapper<CommonCourt> courtFieldsMapper) {
        this.courtFieldsMapper = courtFieldsMapper;
    }

    public void setDivisionMapper(FieldsMapper<CommonCourtDivision> divisionMapper) {
        this.divisionMapper = divisionMapper;
    }
}
