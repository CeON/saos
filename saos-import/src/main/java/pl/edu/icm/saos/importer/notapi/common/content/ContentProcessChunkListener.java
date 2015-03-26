package pl.edu.icm.saos.importer.notapi.common.content;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContext;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionManager;

/**
 * Listener handling storing of judgment content files
 * 
 * @author madryk
 */
@Service
public class ContentProcessChunkListener implements ChunkListener {

    @Autowired
    private ContentFileTransactionManager judgmentContentAdder;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeChunk(ChunkContext context) {
        ContentFileTransactionContext transactionContext = judgmentContentAdder.openTransaction();
        context.setAttribute("contentFileTransactionContext", transactionContext);
        
    }

    @Override
    public void afterChunk(ChunkContext context) {
        ContentFileTransactionContext transactionContext = (ContentFileTransactionContext)context.getAttribute("contentFileTransactionContext");
        judgmentContentAdder.commit(transactionContext);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        ContentFileTransactionContext transactionContext = (ContentFileTransactionContext)context.getAttribute("contentFileTransactionContext");
        judgmentContentAdder.rollback(transactionContext);
    }
    
    
}
