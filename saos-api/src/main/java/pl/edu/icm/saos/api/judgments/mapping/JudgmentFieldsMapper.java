package pl.edu.icm.saos.api.judgments.mapping;

import org.springframework.stereotype.Component;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/** {@inheritDoc}
 * @author pavtel
 */
@Component("judgmentFieldsMapper")
public class JudgmentFieldsMapper implements FieldsMapper<Judgment> {


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> toMap(Judgment element) {
        Map<String, Object> item = new LinkedHashMap<>();

        item.put(CASE_NUMBER, element.getCaseNumber());
        //TODO add more fields

        return item;
    }
}
