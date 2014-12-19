package pl.edu.icm.saos.importer.notapi.common;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * Judgment import - process - reader
 *  
 * @author Łukasz Dumiszewski
 */
public class JudgmentImportProcessReader<T extends RawSourceJudgment> implements ItemStreamReader<T> {

    private static Logger log = LoggerFactory.getLogger(JudgmentImportProcessReader.class);
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    private LinkedList<Integer> rJudgmentIds;
    
    private Class<T> judgmentClass;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JudgmentImportProcessReader(Class<T> judgmentClass) {
        this.judgmentClass = judgmentClass;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        Integer rJudgmentId = rJudgmentIds.poll();

        if (rJudgmentId == null) {
            return null;
        }
        
        return rawSourceJudgmentRepository.getOne(rJudgmentId, judgmentClass);
        
    }
    
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        rJudgmentIds = new LinkedList<Integer>(rawSourceJudgmentRepository.findAllNotProcessedIds(judgmentClass));
        log.debug("Number of {} to process: {}", judgmentClass.getName(), rJudgmentIds.size());
    }

    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }

   
    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setRawSourceJudgmentRepository(RawSourceJudgmentRepository rawSourceJudgmentRepository) {
        this.rawSourceJudgmentRepository = rawSourceJudgmentRepository;
    }
    
}
