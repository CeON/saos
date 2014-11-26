package pl.edu.icm.saos.search.indexing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * Spring Batch reader for reseting judgments indexed flag.
 * It can use job parameter <code>sourceCode</code> to reset indexed
 * flag for judgments with specified {@link SourceCode} 
 * 
 * @author madryk
 */
@Service
@StepScope
public class JudgmentResetIndexFlagReader implements ItemStreamReader<Judgment> {
    
    @Value("#{jobParameters[sourceCode]}")
    private String sourceCode;
    
    private JudgmentRepository judgmentRepository;
    
    
    private volatile Queue<Integer> judgmentIds = Lists.newLinkedList();
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {
        if (StringUtils.isNotBlank(sourceCode)) {
            SourceCode sc = SourceCode.valueOf(StringUtils.upperCase(sourceCode));
            judgmentIds = new ConcurrentLinkedQueue<Integer>(judgmentRepository.findIndexedIdsBySourceCode(sc));
        } else {
            judgmentIds = new ConcurrentLinkedQueue<Integer>(judgmentRepository.findAllIndexedIds());
        }
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
    
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
