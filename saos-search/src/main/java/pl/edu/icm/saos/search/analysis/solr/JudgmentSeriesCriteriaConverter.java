package pl.edu.icm.saos.search.analysis.solr;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.service.JudgmentCriteriaBuilder;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentSeriesCriteriaConverter {

    
    
    public JudgmentCriteria convert(JudgmentSeriesCriteria judgmentSeriesCriteria) {
        
        return JudgmentCriteriaBuilder.create()
                               .withAll(judgmentSeriesCriteria.getPhrase())
                               .build();
    }
    
}
