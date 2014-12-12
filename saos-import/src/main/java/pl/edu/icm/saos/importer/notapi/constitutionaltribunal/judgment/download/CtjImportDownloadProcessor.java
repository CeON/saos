package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

/**
 * @author madryk
 */
@Service
public class CtjImportDownloadProcessor implements ItemProcessor<String, RawSourceCtJudgment> {
    
    private RawSourceCtJudgmentFactory rawSourceCtJudgmentFactory;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public RawSourceCtJudgment process(String item) throws Exception {
        return rawSourceCtJudgmentFactory.createRawSourceCtJudgment(item);
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceCtJudgmentFactory(
            RawSourceCtJudgmentFactory rawSourceCtJudgmentFactory) {
        this.rawSourceCtJudgmentFactory = rawSourceCtJudgmentFactory;
    }

}
