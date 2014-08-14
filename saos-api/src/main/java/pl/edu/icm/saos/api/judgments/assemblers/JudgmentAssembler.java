package pl.edu.icm.saos.api.judgments.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.judgments.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author pavtel
 */
@Component("judgmentAssembler")
public class JudgmentAssembler {

    @Autowired
    private FieldsMapper<Judgment> judgmentFieldsMapper;

    public JudgmentAssembler() {
    }

    public Map<String, Object> toSimpleItemRepresentation(Judgment judgment){
        Map<String, Object> data = judgmentFieldsMapper.toMap(judgment);


        return data;
    }

    public Map<String, Object> toItemRepresentation(Judgment judgment){
        Map<String, Object> data = toSimpleItemRepresentation(judgment);

        return data;
    }

    public List<Object> toItemsList(List<? extends Judgment> judgments){
        List<Object> items = new LinkedList<>();

        for(Judgment judgment: judgments){
            items.add(toItemRepresentation(judgment));
        }

        return items;
    }
}
