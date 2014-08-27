package pl.edu.icm.saos.api.utils;

import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Arrays;

import static pl.edu.icm.saos.api.utils.FieldsDefinition.createCommonJudgment;

/**
 * @author pavtel
 */
public class TrivialApiSearchService implements ApiSearchService<Judgment>{

    @Override
    public ElementsSearchResults<Judgment> performSearch(RequestParameters requestParameters) {
        CommonCourtJudgment commonCourtJudgment = createCommonJudgment();
        return new ElementsSearchResults<>(requestParameters, Arrays.asList(commonCourtJudgment));
    }
}
