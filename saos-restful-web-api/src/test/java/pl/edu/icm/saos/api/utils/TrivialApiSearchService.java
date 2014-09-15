package pl.edu.icm.saos.api.utils;

import static pl.edu.icm.saos.api.utils.FieldsDefinition.createCommonJudgment;

import java.util.Arrays;

import pl.edu.icm.saos.api.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
public class TrivialApiSearchService implements ApiSearchService<Judgment, JudgmentsParameters>{

    @Override
    public ElementsSearchResults<Judgment, JudgmentsParameters> performSearch(JudgmentsParameters parameters) {
        CommonCourtJudgment commonCourtJudgment = createCommonJudgment();
        return new ElementsSearchResults<>(Arrays.asList(commonCourtJudgment), parameters);
    }
}
