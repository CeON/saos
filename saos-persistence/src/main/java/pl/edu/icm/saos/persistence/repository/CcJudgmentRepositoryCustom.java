package pl.edu.icm.saos.persistence.repository;

import org.joda.time.DateTime;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentSource;

/**
 * @author Łukasz Dumiszewski
 */

public interface CcJudgmentRepositoryCustom {

    /**
     * Retrieves the max {@link JudgmentSource#getSourcePublicationDate()} from all
     * {@link CommonCourtJudgment}s. <br/> 
     * See: {@link CommonCourtJudgment#getJudgmentSource()}
     */
    public DateTime getMaxSourcePublicationDate();
    
}
