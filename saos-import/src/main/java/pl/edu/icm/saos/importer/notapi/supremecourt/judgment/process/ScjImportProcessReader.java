package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

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
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;

/**
 * Simple supreme court judgment import - process - reader
 *  
 * @author Łukasz Dumiszewski
 */
@Service("scjImportProcessReader")
public class ScjImportProcessReader implements ItemStreamReader<RawSourceScJudgment> {

    private static Logger log = LoggerFactory.getLogger(ScjImportProcessReader.class);
    
    private RawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository;
    
    private LinkedList<Integer> rJudgmentIds;
    
   
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceScJudgment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        Integer rJudgmentId = rJudgmentIds.poll();

        if (rJudgmentId == null) {
            return null;
        }
        
        return simpleRawSourceScJudgmentRepository.getOne(rJudgmentId);
        
    }
    
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        rJudgmentIds = new LinkedList<Integer>(simpleRawSourceScJudgmentRepository.findAllNotProcessedIds());
        log.debug("Number of raw source supreme court judgments to process: {}", rJudgmentIds.size());
    }

    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }

   
    
    //------------------------ PRIVATE --------------------------
    
    @Autowired
    public void setSimpleRawSourceScJudgmentRepository(RawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository) {
        this.simpleRawSourceScJudgmentRepository = simpleRawSourceScJudgmentRepository;
    }
    
}
