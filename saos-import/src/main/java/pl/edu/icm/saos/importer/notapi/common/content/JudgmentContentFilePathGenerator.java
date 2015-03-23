package pl.edu.icm.saos.importer.notapi.common.content;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author madryk
 */
@Service
public class JudgmentContentFilePathGenerator {

    //------------------------ LOGIC --------------------------
    
    /**
     * Generates path where judgment content file should be stored
     */
    public String generatePath(Judgment judgment) {
        
        CourtType courtType = judgment.getCourtType();
        LocalDate judgmentDate = judgment.getJudgmentDate();
        
        return courtType.name().toLowerCase() + "/"
                + judgmentDate.getYear() + "/"
                + judgmentDate.getMonthOfYear() + "/"
                + judgmentDate.getDayOfMonth() + "/";
    }
}
