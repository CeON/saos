package pl.edu.icm.saos.importer.commoncourt.process;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessReader")
public class CcjImportProcessReader implements ItemStreamReader<RawSourceCcJudgment> {

    
    private Logger log = LoggerFactory.getLogger(CcjImportProcessReader.class);
    
    private int pageSize = 100;
    private int pageNo = 0;
    private Queue<RawSourceCcJudgment> rawSourceCcJudgments;
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    

    @Override
    public RawSourceCcJudgment read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (CollectionUtils.isEmpty(rawSourceCcJudgments)) {
            rawSourceCcJudgments = new LinkedList<RawSourceCcJudgment>(rawSourceCcJudgmentRepository.findNotProcessed(new PageRequest(pageNo, pageSize)).getContent());
            if (CollectionUtils.isEmpty(rawSourceCcJudgments)) {
                return null;
            }
            logDebug();
            pageNo++;
        }
        
        RawSourceCcJudgment rJudgment = rawSourceCcJudgments.poll();
        
        return rJudgment;
    }




    
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        pageNo=0;
        rawSourceCcJudgments = Lists.newLinkedList();
        
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close() throws ItemStreamException {       // TODO Auto-generated method stub
        
    }

    
    
    //------------------------ PRIVATE --------------------------
    
    private void logDebug() {
        if (log.isDebugEnabled()) {
            log.debug("{} raw source cc judgments have been read", rawSourceCcJudgments.size());
            log.debug("read judgments: ");
            for (RawSourceCcJudgment rj : rawSourceCcJudgments) {
                log.info("{}", rj.getId());
            };
            log.debug("{} judgments read", rawSourceCcJudgments.size() + pageNo*pageSize);
        }
    }


    
   
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setRawSourceCcJudgmentRepository(RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository) {
        this.rawSourceCcJudgmentRepository = rawSourceCcJudgmentRepository;
    }
    


}
