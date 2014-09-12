package pl.edu.icm.saos.api.judgments.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author pavtel
 */
@Component("judgmentAssembler")
public class JudgmentAssembler {

    @Autowired
    private FieldsMapper<Judgment> judgmentFieldsMapper;

    public JudgmentAssembler() {
    }

    public Map<String, Object> fieldsToItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.fieldsToMap(judgment);

        return data;
    }


    public Map<String, Object> basicFieldsToItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.basicFieldsToMap(judgment);

        return data;
    }

    public List<Object> toItemsList(List<? extends Judgment> judgments){

        List<Object> items = judgments.stream()
                .map((judgment) -> basicFieldsToItemRepresentation(judgment))
                .collect(Collectors.toList());

        return items;
    }

    //*** setters ***
    public void setJudgmentFieldsMapper(FieldsMapper<Judgment> judgmentFieldsMapper) {
        this.judgmentFieldsMapper = judgmentFieldsMapper;
    }
}
