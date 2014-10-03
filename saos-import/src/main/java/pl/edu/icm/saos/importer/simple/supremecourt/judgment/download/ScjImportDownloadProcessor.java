package pl.edu.icm.saos.importer.simple.supremecourt.judgment.download;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;

/**
 * Simple supreme court judgment import - download - processor
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class ScjImportDownloadProcessor implements ItemProcessor<String, SimpleRawSourceScJudgment> {

    @Override
    public SimpleRawSourceScJudgment process(String jsonContent) {
        SimpleRawSourceScJudgment judgment = new SimpleRawSourceScJudgment();
        judgment.setJsonContent(jsonContent);
        return judgment;
    }

}
