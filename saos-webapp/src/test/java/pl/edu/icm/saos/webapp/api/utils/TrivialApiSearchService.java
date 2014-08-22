package pl.edu.icm.saos.webapp.api.utils;

import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.JudgmentsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.webapp.api.services.ApiSearchService;

import java.util.Arrays;

import static pl.edu.icm.saos.webapp.api.utils.FieldsDefinition.*;

/**
 * @author pavtel
 */
public class TrivialApiSearchService implements ApiSearchService{

    @Override
    public JudgmentsSearchResults performSearch(RequestParameters requestParameters) {
        CommonCourtJudgment commonCourtJudgment = createCommonJudgment();
        return new JudgmentsSearchResults(requestParameters, Arrays.asList(commonCourtJudgment));
    }
}
