package pl.edu.icm.saos.importer.commoncourt;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class CcJudgmentImportWriter implements ItemWriter<String> {

    private Logger log = LoggerFactory.getLogger(CcJudgmentImportWriter.class);
    
    @Override
    public void write(List<? extends String> items) throws Exception {
        log.info("writing: {}", items);
        
    }

}
