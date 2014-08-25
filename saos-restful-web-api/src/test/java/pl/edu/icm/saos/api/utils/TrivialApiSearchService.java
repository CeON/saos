package pl.edu.icm.saos.api.utils;

import pl.edu.icm.saos.api.judgments.services.ApiSearchService;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.JudgmentsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

import java.util.Arrays;

import static pl.edu.icm.saos.api.utils.FieldsDefinition.createCommonJudgment;

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
