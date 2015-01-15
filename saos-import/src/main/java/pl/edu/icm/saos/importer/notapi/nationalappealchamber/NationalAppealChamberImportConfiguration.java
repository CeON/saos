package pl.edu.icm.saos.importer.notapi.nationalappealchamber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceNacJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
@Configuration
public class NationalAppealChamberImportConfiguration {

    @Autowired
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    
    //------------------------ BEANS --------------------------
    
    @Bean
    public JsonStringParser<SourceNacJudgment> sourceNacJudgmentParser() {
        JsonStringParser<SourceNacJudgment> sourceNacJudgmentParser = new JsonStringParser<>(SourceNacJudgment.class);
        sourceNacJudgmentParser.setCommonValidator(commonValidator);
        sourceNacJudgmentParser.setJsonFactory(jsonFactory);
        return sourceNacJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader nacjImportDownloadReader(@Value("${import.judgments.nationalAppealChamber.dir}") String importDir) {
        JsonImportDownloadReader nacjImportDownloadReader = new JsonImportDownloadReader();
        nacjImportDownloadReader.setImportDir(importDir);
        
        return nacjImportDownloadReader;
    }
    
    @Bean
    public JsonImportDownloadProcessor<RawSourceNacJudgment> nacjImportDownloadProcessor() {
        JsonImportDownloadProcessor<RawSourceNacJudgment> nacjImportDownloadProcessor = new JsonImportDownloadProcessor<>(RawSourceNacJudgment.class);
        nacjImportDownloadProcessor.setSourceJudgmentParser(sourceNacJudgmentParser());
        
        return nacjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener nacjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceNacJudgment.class);
        
        return stepExecutionListener;
    }
}
