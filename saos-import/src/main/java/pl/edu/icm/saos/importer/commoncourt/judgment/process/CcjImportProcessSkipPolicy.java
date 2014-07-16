package pl.edu.icm.saos.importer.commoncourt.judgment.process;

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

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessSkipPolicy.class);
    
    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        log.error("Error during ccj import process, error count: {}", skipCount);
        log.error("", t);
        
        return (t instanceof Throwable);
    }

}
