package pl.edu.icm.saos.webapp.api.utils;

import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.webapp.api.model.JudgmentsSearchResults;
import pl.edu.icm.saos.webapp.api.services.ApiSearchService;

import java.util.Arrays;

import static pl.edu.icm.saos.webapp.api.utils.FieldsDefinition.*;

/**
 * @author pavtel
 */
public class TrivialApiSearchService implements ApiSearchService{

    @Override
    public JudgmentsSearchResults performSearch(Pagination pagination) {
        CommonCourtJudgment commonCourtJudgment = createCommonJudgment();
        return new JudgmentsSearchResults(pagination, Arrays.asList(commonCourtJudgment));
    }
}
