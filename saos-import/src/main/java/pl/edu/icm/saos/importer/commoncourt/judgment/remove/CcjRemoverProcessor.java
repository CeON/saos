package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.RemovedJudgment;

/**
 * Spring batch processor for removing judgments
 * 
 * @author madryk
 */
@Service
public class CcjRemoverProcessor implements ItemProcessor<Judgment, RemovedJudgment> {

    //------------------------ LOGIC --------------------------
    
    @Override
    public RemovedJudgment process(Judgment judgment) throws Exception {
        
        RemovedJudgment removedJudgment = new RemovedJudgment();
        
        removedJudgment.setRemovedJudgmentId(judgment.getId());
        removedJudgment.setSourceInfo(judgment.getSourceInfo());
        
        return removedJudgment;
    }

}
