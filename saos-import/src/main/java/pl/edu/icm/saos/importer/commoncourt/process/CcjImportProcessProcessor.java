package pl.edu.icm.saos.importer.commoncourt.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessProcessor")
public class CcjImportProcessProcessor implements ItemProcessor<RawSourceCcJudgment, CommonCourtJudgment> {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessProcessor.class);
    
    @Override
    public CommonCourtJudgment process(RawSourceCcJudgment item) throws Exception {
        
        log.info("process");
        
        return new CommonCourtJudgment();
    }

}
