package pl.edu.icm.saos.batch.core;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.DbCleaner;

/**
 * @author Łukasz Dumiszewski
 */
@Service("batchDbCleaner")
public class BatchDbCleaner {
    
    @Autowired
    private DbCleaner dbCleaner;
    
    /**
     * Invokes {@link DbCleaner#clean()} and then deletes all spring batch_ tables
     */
    @Transactional
    public void clean() {
        dbCleaner.clean();
        deleteBatchTables();
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void deleteBatchTables() {
        dbCleaner.deleteAllSql("BATCH_STEP_EXECUTION_CONTEXT");
        dbCleaner.deleteAllSql("BATCH_STEP_EXECUTION");
        dbCleaner.deleteAllSql("BATCH_JOB_EXECUTION_CONTEXT");
        dbCleaner.deleteAllSql("BATCH_JOB_EXECUTION_PARAMS");
        dbCleaner.deleteAllSql("BATCH_JOB_EXECUTION");
        dbCleaner.deleteAllSql("BATCH_JOB_INSTANCE");
    }
}
