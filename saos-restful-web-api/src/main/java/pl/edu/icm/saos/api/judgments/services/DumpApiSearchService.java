package pl.edu.icm.saos.api.judgments.services;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Arrays;

/**
 * @author pavtel
 */
@Service
public class DumpApiSearchService implements ApiSearchService<Judgment> {


    @Override
    public ElementsSearchResults<Judgment> performSearch(RequestParameters requestParameters) {

        Judgment judgment = new CommonCourtJudgment();
        judgment.setCaseNumber("11111");

        return new ElementsSearchResults<>(requestParameters, Arrays.asList(judgment));

    }
}
