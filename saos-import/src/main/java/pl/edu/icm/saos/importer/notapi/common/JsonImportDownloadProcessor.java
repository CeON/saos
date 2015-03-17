package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.content.ImportContentFileUtils;
import pl.edu.icm.saos.persistence.model.importer.notapi.JsonRawSourceJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Spring Batch import download processor of judgments
 * from {@literal String}
 * 
 * @author Łukasz Dumiszewski
 * @param <T> - type of judgment that can be processed 
 */
public class JsonImportDownloadProcessor<T extends JsonRawSourceJudgment> implements ItemProcessor<JsonJudgmentNode, T> {
    
    private JsonStringParser<? extends SourceJudgment> sourceJudgmentParser;
    
    private Class<T> rawSourceJugmentClass;
    
    private ImportContentFileUtils importContentFileUtils;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonImportDownloadProcessor(Class<T> rawSourceJugmentClass) {
        this.rawSourceJugmentClass = rawSourceJugmentClass;
    }
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T process(JsonJudgmentNode item) throws JsonParseException, InstantiationException, IllegalAccessException {
        
        String jsonJudgment = item.getJson();
        
        SourceJudgment sourceJudgment = sourceJudgmentParser.parseAndValidate(jsonJudgment);


        T rawJudgment = rawSourceJugmentClass.newInstance();
        rawJudgment.setJsonContent(jsonJudgment);
        rawJudgment.setSourceId(sourceJudgment.getSource().getSourceJudgmentId());
        
        File contentFile = importContentFileUtils.locateContentFile(item.getFileSource(), rawSourceJugmentClass);
        rawJudgment.setJudgmentContentPath(contentFile.getName());


        return rawJudgment;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setSourceJudgmentParser(JsonStringParser<? extends SourceJudgment> sourceJudgmentParser) {
        this.sourceJudgmentParser = sourceJudgmentParser;
    }

    @Autowired
    public void setImportContentFileUtils(ImportContentFileUtils importContentFileUtils) {
        this.importContentFileUtils = importContentFileUtils;
    }

}
