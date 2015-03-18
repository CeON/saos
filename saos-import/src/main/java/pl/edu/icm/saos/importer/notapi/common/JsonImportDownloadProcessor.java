package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.notapi.common.content.ContentSourceFileFinder;
import pl.edu.icm.saos.persistence.model.importer.notapi.JsonRawSourceJudgment;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * Spring Batch import download processor of judgments
 * from {@literal String}
 * 
 * @author Łukasz Dumiszewski
 * @param <T> - type of judgment that can be processed 
 */
public class JsonImportDownloadProcessor<T extends JsonRawSourceJudgment> implements ItemProcessor<JsonJudgmentItem, T> {
    
    private JsonStringParser<? extends SourceJudgment> sourceJudgmentParser;
    
    private Class<T> rawSourceJugmentClass;
    
    private ContentSourceFileFinder contentSourceFileFinder;
    
    private String downloadedContentDir;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonImportDownloadProcessor(Class<T> rawSourceJugmentClass) {
        this.rawSourceJugmentClass = rawSourceJugmentClass;
    }
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public T process(JsonJudgmentItem item) throws JsonParseException, InstantiationException, IllegalAccessException {
        
        String jsonJudgment = item.getJson();
        
        SourceJudgment sourceJudgment = sourceJudgmentParser.parseAndValidate(jsonJudgment);


        T rawJudgment = rawSourceJugmentClass.newInstance();
        rawJudgment.setJsonContent(jsonJudgment);
        rawJudgment.setSourceId(sourceJudgment.getSource().getSourceJudgmentId());
        
        
        File downloadedContentFile = contentSourceFileFinder.findContentFile(new File(downloadedContentDir), item.getSourceMetadataFile());
        rawJudgment.setJudgmentContentFilename(downloadedContentFile.getName());


        return rawJudgment;
    }
    
    
    //------------------------ SETTERS --------------------------

    public void setSourceJudgmentParser(JsonStringParser<? extends SourceJudgment> sourceJudgmentParser) {
        this.sourceJudgmentParser = sourceJudgmentParser;
    }

    @Autowired
    public void setContentSourceFileFinder(ContentSourceFileFinder contentSourceFileFinder) {
        this.contentSourceFileFinder = contentSourceFileFinder;
    }

    public void setDownloadedContentDir(String downloadedContentDir) {
        this.downloadedContentDir = downloadedContentDir;
    }

}
