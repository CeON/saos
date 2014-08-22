package pl.edu.icm.saos.api.judgments.assemblers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.judgments.mapping.FieldsMapper;
import pl.edu.icm.saos.api.parameters.JoinedParameter;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component("judgmentAssembler")
public class JudgmentAssembler {

    @Autowired
    private FieldsMapper<Judgment> judgmentFieldsMapper;

    public JudgmentAssembler() {
    }

    public Map<String, Object> toItemRepresentation(Judgment judgment, boolean expandAll){
        Map<String, Object> data = judgmentFieldsMapper.toMap(judgment, expandAll);

        return data;
    }

    public List<Object> toItemsList(List<? extends Judgment> judgments, JoinedParameter expandParameter){
        final boolean expandAll = expandParameter.containsValue(ALL);

        List<Object> items = judgments.stream()
                .map((judgment) -> toItemRepresentation(judgment, expandAll))
                .collect(Collectors.toList());

        return items;
    }
}
