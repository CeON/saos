package pl.edu.icm.saos.importer.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.repository.JudgmentResultRepository;

/**
 * @author madryk
 */
@Service
public class JudgmentResultCreator {

    private JudgmentResultRepository judgmentResultRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    public JudgmentResult fetchOrCreateJudgmentResult(CourtType courtType, String text) {
        JudgmentResult judgmentResult = judgmentResultRepository.findOneByCourtTypeAndTextIgnoreCase(courtType, text);
        if (judgmentResult == null) {
            judgmentResult = new JudgmentResult(courtType, text);
            judgmentResultRepository.save(judgmentResult);
            judgmentResultRepository.flush();
        }
        return judgmentResult;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentResultRepository(JudgmentResultRepository judgmentResultRepository) {
        this.judgmentResultRepository = judgmentResultRepository;
    }
    
    
}
