package pl.edu.icm.saos.webapp.api.services;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.parameters.Pagination;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.webapp.api.model.JudgmentsSearchResults;

import java.util.Arrays;

/**
 * @author pavtel
 */
@Service
public class DumpApiSearchService implements ApiSearchService{


    @Override
    public JudgmentsSearchResults performSearch(Pagination pagination) {

        Judgment judgment = new CommonCourtJudgment();
        judgment.setCaseNumber("11111");

        return new JudgmentsSearchResults(pagination, Arrays.asList(judgment));

    }
}
