package pl.edu.icm.saos.search.indexing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

import com.google.common.collect.Lists;

/**
 * Spring batch reader of judgments for indexing
 * @author madryk
 */
@Service
public class JudgmentIndexingReader implements ItemStreamReader<Judgment> {

    private JudgmentRepository judgmentRepository;
    
    
    private volatile Queue<Integer> judgmentIds = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {
        judgmentIds = new ConcurrentLinkedQueue<Integer>(judgmentRepository.findAllNotIndexedIds());
    }

    @Override
    public Judgment read() throws Exception,
            UnexpectedInputException, ParseException,
            NonTransientResourceException {
        
        Integer id = judgmentIds.poll();
        
        if (id == null) {
            return null;
        }
        
        return judgmentRepository.findOne(id);
    }
    
    @Override
    public void update(ExecutionContext executionContext)
            throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
