package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessSkipPolicy")
public class CcjImportProcessSkipPolicy implements SkipPolicy {
    public final static Logger log = LoggerFactory.getLogger(CcjImportProcessSkipPolicy.class);
    
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        
        return (t instanceof CcjImportProcessSkippableException || 
                /* 
                 * Handle situation when two or more versions of raw judgment
                 * are being processed in same chunk.
                 * As result of skipping this exception chunk will be divided into
                 * small one element chunks which will be processed correctly.
                 * 
                 * For more info check: https://github.com/CeON/saos/issues/468
                 */
                (t instanceof PersistenceException && t.getCause() instanceof ConstraintViolationException));
    }

}
