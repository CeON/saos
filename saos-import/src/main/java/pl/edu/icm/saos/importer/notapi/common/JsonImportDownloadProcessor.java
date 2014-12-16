package pl.edu.icm.saos.importer.notapi.common;

import org.springframework.batch.item.ItemProcessor;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Spring Batch import download processor of judgments
 * from json format
 * 
 * @author Łukasz Dumiszewski
 * @param <T> - type of judgment that can be processed 
 */
public class JsonImportDownloadProcessor<T extends RawSourceJudgment> implements ItemProcessor<String, T> {

    private RawSourceJudgmentFactory<T> rawSourceJudgmentFactory;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T process(String jsonContent) {
        return rawSourceJudgmentFactory.createRawSourceJudgment(jsonContent);
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setRawSourceJudgmentFactory(RawSourceJudgmentFactory<T> rawSourceJudgmentFactory) {
        this.rawSourceJudgmentFactory = rawSourceJudgmentFactory;
    }

}
