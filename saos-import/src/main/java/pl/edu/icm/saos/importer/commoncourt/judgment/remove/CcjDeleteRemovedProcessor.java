package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.DeletedJudgment;

/**
 * Spring batch processor for removing judgments
 * 
 * @author madryk
 */
@Service
public class CcjDeleteRemovedProcessor implements ItemProcessor<Judgment, DeletedJudgment> {

    //------------------------ LOGIC --------------------------
    
    @Override
    public DeletedJudgment process(Judgment judgment) throws Exception {
        
        DeletedJudgment deletedJudgment = new DeletedJudgment();
        
        deletedJudgment.setRemovedJudgmentId(judgment.getId());
        deletedJudgment.setSourceInfo(judgment.getSourceInfo());
        
        return deletedJudgment;
    }

}
