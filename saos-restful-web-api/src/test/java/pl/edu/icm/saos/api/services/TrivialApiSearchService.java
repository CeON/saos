package pl.edu.icm.saos.api.services;

import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.services.ApiSearchService;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Arrays;

import static pl.edu.icm.saos.api.services.FieldsDefinition.createCommonJudgment;

/**
 * @author pavtel
 */
public class TrivialApiSearchService implements ApiSearchService<Judgment, JudgmentsParameters>{

    private int totalResults;

    public TrivialApiSearchService(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public ElementsSearchResults<Judgment, JudgmentsParameters> performSearch(JudgmentsParameters parameters) {
        CommonCourtJudgment commonCourtJudgment = createCommonJudgment();
        return new ElementsSearchResults<Judgment, JudgmentsParameters>(Arrays.asList(commonCourtJudgment), parameters)
                .totalResults(totalResults);
    }
}
