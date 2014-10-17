package pl.edu.icm.saos.api.single.judgment.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author pavtel
 * Converts {@link pl.edu.icm.saos.persistence.model.Judgment Judgment} elements
 * into objects which can be easily serialized as json
 */
@Component("judgmentAssembler")
public class JudgmentAssembler {

    //********** fields *************

    @Autowired
    private FieldsMapper<Judgment> judgmentFieldsMapper;

    //********** END fields *********


    //********** business methods ***********

    /**
     * Converts judgment (uses all important judgment's fields) into object which can be easily serialized as json.
     * @param judgment to convert.
     * @return Object which can be easily serialized as json.
     */
    public Map<String, Object> fieldsToItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.fieldsToMap(judgment);

        return data;
    }


    /**
     * Converts judgment (uses only most important judgment's fields) into object which can be easily serialized as json.
     * @param judgment to convert.
     * @return Object which can be easily serialized as json.
     */
    public Map<String, Object> basicFieldsToItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.basicFieldsToMap(judgment);

        return data;
    }

    /**
     * Converts list of judgments into list of objects
     * which can be easily serialized as json. Converts only most important judgment's fields.
     * @param judgments list of judgments to convert.
     * @return List of Objects which can be easily serialized as json.
     */
    public List<Object> toItemsList(List<? extends Judgment> judgments){

        List<Object> items = judgments.stream()
                .map(this::basicFieldsToItemRepresentation)
                .collect(Collectors.toList());

        return items;
    }

    //******** END business methods ***********

    //*** setters ***
    public void setJudgmentFieldsMapper(FieldsMapper<Judgment> judgmentFieldsMapper) {
        this.judgmentFieldsMapper = judgmentFieldsMapper;
    }
}
