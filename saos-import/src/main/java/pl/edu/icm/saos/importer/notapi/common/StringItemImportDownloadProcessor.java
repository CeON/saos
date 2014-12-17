package pl.edu.icm.saos.importer.notapi.common;

import org.springframework.batch.item.ItemProcessor;

import com.fasterxml.jackson.core.JsonParseException;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Spring Batch import download processor of judgments
 * from {@literal String}
 * 
 * @author Łukasz Dumiszewski
 * @param <T> - type of judgment that can be processed 
 */
public class StringItemImportDownloadProcessor<T extends RawSourceJudgment> implements ItemProcessor<String, T> {

    private RawSourceJudgmentParser<T> rawSourceJudgmentParser;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T process(String item) throws JsonParseException {
        return rawSourceJudgmentParser.parseRawSourceJudgment(item);
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setRawSourceJudgmentParser(RawSourceJudgmentParser<T> rawSourceJudgmentParser) {
        this.rawSourceJudgmentParser = rawSourceJudgmentParser;
    }

}
