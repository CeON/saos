package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;

import java.util.Collections;
import java.util.Map;

/**
 * @author pavtel
 */
@Service
public class CommonCourtJpqlSearchImplementator extends AbstractJpqlSearchImplementor<CommonCourtSearchFilter, CommonCourt> {


    @Override
    protected String createQuery(CommonCourtSearchFilter searchFilter) {
        return " select court from "+CommonCourt.class.getName()+" court ";
    }

    @Override
    protected Map<String, Object> createParametersMap(CommonCourtSearchFilter searchFilter) {
        return Collections.emptyMap();
    }
}
