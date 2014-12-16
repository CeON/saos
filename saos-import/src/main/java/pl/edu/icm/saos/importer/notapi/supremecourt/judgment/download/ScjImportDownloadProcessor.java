package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Simple supreme court judgment import - download - processor
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class ScjImportDownloadProcessor implements ItemProcessor<String, RawSourceScJudgment> {

    private RawSourceScJudgmentFactory rawSourceScJudgmentFactory;
    
    @Override
    public RawSourceScJudgment process(String jsonContent) throws JsonParseException {
        return rawSourceScJudgmentFactory.createRawSourceScJudgment(jsonContent);
    }
    
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setSimpleRawSourceScJudgmentFactory(RawSourceScJudgmentFactory rawSourceScJudgmentFactory) {
        this.rawSourceScJudgmentFactory = rawSourceScJudgmentFactory;
    }

}
