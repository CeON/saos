package pl.edu.icm.saos.search.analysis.solr.recalc;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentSeriesCriteriaCopier")
public class JudgmentSeriesCriteriaCloner {

    
    /**
     * Makes a shallow copy of the given sourceCriteria
     */
    public JudgmentSeriesCriteria clone(JudgmentSeriesCriteria sourceCriteria) {
        Preconditions.checkNotNull(sourceCriteria);
        JudgmentSeriesCriteria copiedCriteria = new JudgmentSeriesCriteria();
        BeanUtils.copyProperties(sourceCriteria, copiedCriteria);
        return copiedCriteria;
    }
    
}
