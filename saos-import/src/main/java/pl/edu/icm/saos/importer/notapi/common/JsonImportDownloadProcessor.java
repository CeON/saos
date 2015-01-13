package pl.edu.icm.saos.importer.notapi.common;

import org.springframework.batch.item.ItemProcessor;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.persistence.model.importer.notapi.JsonRawSourceJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Spring Batch import download processor of judgments
 * from {@literal String}
 * 
 * @author Łukasz Dumiszewski
 * @param <T> - type of judgment that can be processed 
 */
public class JsonImportDownloadProcessor<T extends JsonRawSourceJudgment> implements ItemProcessor<String, T> {
    
    private JsonStringParser<? extends SourceJudgment> sourceJudgmentParser;
    
    private Class<T> rawSourceJugmentClass;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonImportDownloadProcessor(Class<T> rawSourceJugmentClass) {
        this.rawSourceJugmentClass = rawSourceJugmentClass;
    }
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T process(String item) throws JsonParseException, InstantiationException, IllegalAccessException {
        
        SourceJudgment sourceJudgment = sourceJudgmentParser.parseAndValidate(item);


        T rawJudgment = rawSourceJugmentClass.newInstance();
        rawJudgment.setJsonContent(item);
        rawJudgment.setSourceId(sourceJudgment.getSource().getSourceJudgmentId());


        return rawJudgment;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setSourceJudgmentParser(JsonStringParser<? extends SourceJudgment> sourceJudgmentParser) {
        this.sourceJudgmentParser = sourceJudgmentParser;
    }

}
