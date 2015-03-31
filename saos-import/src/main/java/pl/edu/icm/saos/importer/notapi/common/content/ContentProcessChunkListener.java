package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.IOException;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileCommitRollbackService;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContext;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContextFactory;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionException;

/**
 * Listener handling storing of judgment content files
 * 
 * @author madryk
 */
@Service
public class ContentProcessChunkListener implements ChunkListener {

    @Autowired
    private ContentFileTransactionContextFactory contentFileTransactionContextFactory;
    
    @Autowired
    private ContentFileCommitRollbackService contentFileCommitRollbackService;

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeChunk(ChunkContext context) {
        ContentFileTransactionContext transactionContext = contentFileTransactionContextFactory.createTransactionContext();
        context.setAttribute("contentFileTransactionContext", transactionContext);
        
    }

    @Override
    public void afterChunk(ChunkContext context) {
        ContentFileTransactionContext transactionContext = (ContentFileTransactionContext)context.getAttribute("contentFileTransactionContext");
        
        try {
            contentFileCommitRollbackService.commit(transactionContext);
        } catch (IOException e) {
            throw new ContentFileTransactionException(e);
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        ContentFileTransactionContext transactionContext = (ContentFileTransactionContext)context.getAttribute("contentFileTransactionContext");
        
        try {
            contentFileCommitRollbackService.rollback(transactionContext);
        } catch (IOException e) {
            throw new ContentFileTransactionException(e);
        }
    }
    
    
}
