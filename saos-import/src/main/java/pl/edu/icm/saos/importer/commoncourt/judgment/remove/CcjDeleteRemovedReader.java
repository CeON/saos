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
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * Spring batch reader of common court judgments
 * that have been removed at the source side
 * 
 * @author madryk
 */
@Service
public class CcjDeleteRemovedReader implements ItemStreamReader<Judgment> {

    @Autowired
    private CcRemovedJudgmentsFinder ccRemovedJudgmentsFinder;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    
    private Queue<Long> judgmentIdsToDelete;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        
        judgmentIdsToDelete = new LinkedList<>(ccRemovedJudgmentsFinder.findCcRemovedJudgmentSourceIds());
        
    }

    @Override
    public Judgment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        Long judgmentIdToDelete = judgmentIdsToDelete.poll();
        
        if (judgmentIdToDelete == null) {
            return null;
        }
        
        Judgment judgmentToDelete = judgmentRepository.findOne(judgmentIdToDelete);
        
        return judgmentToDelete != null ? judgmentToDelete : read(); // prevents from stopping the job when this judgment have been deleted by some other process
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
