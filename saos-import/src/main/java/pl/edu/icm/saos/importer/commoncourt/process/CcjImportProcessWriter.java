package pl.edu.icm.saos.importer.commoncourt.process;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjImportProcessWriter")
public class CcjImportProcessWriter implements ItemWriter<CommonCourtJudgment> {

    private static Logger log = LoggerFactory.getLogger(CcjImportProcessWriter.class);
    
    
    @Override
    public void write(List<? extends CommonCourtJudgment> items)
            throws Exception {
        
        log.info("write");
    }

}
