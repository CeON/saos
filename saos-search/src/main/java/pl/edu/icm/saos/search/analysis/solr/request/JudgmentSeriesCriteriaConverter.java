package pl.edu.icm.saos.search.analysis.solr.request;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Service("judgmentSeriesCriteriaConverter")
public class JudgmentSeriesCriteriaConverter {

    
   /**
    * Converts {@link JudgmentSeriesCriteria} into {@link JudgmentCriteria}  
    */
    public JudgmentCriteria convert(JudgmentSeriesCriteria judgmentSeriesCriteria) {
        
        JudgmentCriteria judgmentCriteria = new JudgmentCriteria();
        
        judgmentCriteria.setAll(judgmentSeriesCriteria.getPhrase());
        
        return judgmentCriteria;
        
    }
    
}
