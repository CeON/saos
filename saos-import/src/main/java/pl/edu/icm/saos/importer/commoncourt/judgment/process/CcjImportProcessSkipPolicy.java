package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessSkipPolicy")
public class CcjImportProcessSkipPolicy implements SkipPolicy {

    
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        
        return (t instanceof CcjImportProcessSkippableException);
    }

}
