package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;

import java.util.Map;

/**
 * @author pavtel
 */
@Service
public class JudgmentJpqlSearchImplementor extends AbstractJpqlSearchImplementor<JudgmentSearchFilter, Judgment> {



    @Override
    protected String createQuery(JudgmentSearchFilter searchFilter) {
        return " select journal from " + Judgment.class.getName() + " journal ";
    }

    @Override
    protected Map<String, Object> createParametersMap(JudgmentSearchFilter searchFilter) {
        return null;
    }
}
