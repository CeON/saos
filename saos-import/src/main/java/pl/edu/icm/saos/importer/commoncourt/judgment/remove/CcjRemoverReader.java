package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import java.util.LinkedList;
import java.util.Queue;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Spring batch reader of common court judgments
 * that should be removed
 * 
 * @author madryk
 */
@Service
public class CcjRemoverReader implements ItemStreamReader<Judgment> {

    @Autowired
    private SourceCcRemovedJudgmentsFinder sourceRemovedJudgmentsFinder;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    private Queue<String> judgmentSourceIdsToRemove;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentSourceIdsToRemove = new LinkedList<>(sourceRemovedJudgmentsFinder.findRemovedJudgments());
        
    }

    @Override
    public Judgment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        String sourceId = judgmentSourceIdsToRemove.poll();
        
        if (sourceId == null) {
            return null;
        }
        
        Judgment judgmentToRemove = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.COMMON_COURT, sourceId);
        
        return judgmentToRemove != null ? judgmentToRemove : read();
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close() throws ItemStreamException {
        // TODO Auto-generated method stub
        
    }
    
}
