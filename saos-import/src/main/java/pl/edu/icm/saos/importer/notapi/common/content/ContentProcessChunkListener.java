package pl.edu.icm.saos.importer.notapi.common.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransactionManager;

/**
 * Listener handling storing of judgment content files
 * 
 * @author madryk
 */
@Service
public class ContentProcessChunkListener implements ChunkListener {
    
    private Logger log = LoggerFactory.getLogger(ContentProcessChunkListener.class);

    @Autowired
    private FilesOperationsTransactionManager judgmentContentAdder;

    @Override
    public void beforeChunk(ChunkContext context) {
        String transactionId = judgmentContentAdder.openTransaction();
        context.setAttribute("transactionId", transactionId);
//        context.getStepContext().setAttribute("transactionId", transactionId);
//        context.getStepContext().getJobExecutionContext().put("transactionId", transactionId);
        
        log.warn("before chunk triggered");
        
    }

    @Override
    public void afterChunk(ChunkContext context) {
        String transactionId = (String)context.getAttribute("transactionId");
//        String transactionId = (String)context.getStepContext().getAttribute("transactionId");
//        String transactionId = (String)context.getStepContext().getJobExecutionContext().get("transactionId");
        judgmentContentAdder.commit(transactionId);
        
        log.warn("after chunk triggered");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        String transactionId = (String)context.getAttribute("transactionId");
//        String transactionId = (String)context.getStepContext().getAttribute("transactionId");
//        String transactionId = (String)context.getStepContext().getJobExecutionContext().get("transactionId");
        judgmentContentAdder.rollback(transactionId);
        
        log.warn("after chunk triggered - error");
    }
    
    
}
