package pl.edu.icm.saos.api.dump.judgment.assemblers;

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
@Component
public class DumpJudgmentAssembler {


    @Autowired
    private FieldsMapper<Judgment> judgmentFieldsMapper;

    public Map<String, Object> toItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.fieldsToMap(judgment, true);

        return data;
    }

    public List<Object> toItemsList(List<Judgment> judgments){
        List<Object> items = judgments.stream()
                .map(this::toItemRepresentation)
                .collect(Collectors.toList());

        return items;
    }

    //*** setters ***
    public void setJudgmentFieldsMapper(FieldsMapper<Judgment> judgmentFieldsMapper) {
        this.judgmentFieldsMapper = judgmentFieldsMapper;
    }
}
