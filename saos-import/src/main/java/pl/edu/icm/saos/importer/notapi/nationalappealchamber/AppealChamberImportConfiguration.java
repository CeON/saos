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
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceAcJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceAcJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
@Configuration
public class AppealChamberImportConfiguration {

    @Autowired
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    
    //------------------------ BEANS --------------------------
    
    @Bean
    public JsonStringParser<SourceAcJudgment> sourceAcJudgmentParser() {
        JsonStringParser<SourceAcJudgment> sourceNacJudgmentParser = new JsonStringParser<>(SourceAcJudgment.class);
        sourceNacJudgmentParser.setCommonValidator(commonValidator);
        sourceNacJudgmentParser.setJsonFactory(jsonFactory);
        return sourceNacJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader acjImportDownloadReader(@Value("${import.judgments.nationalAppealChamber.dir}") String importDir) {
        JsonImportDownloadReader acjImportDownloadReader = new JsonImportDownloadReader();
        acjImportDownloadReader.setImportDir(importDir);
        
        return acjImportDownloadReader;
    }
    
    @Bean
    public JsonImportDownloadProcessor<RawSourceAcJudgment> acjImportDownloadProcessor() {
        JsonImportDownloadProcessor<RawSourceAcJudgment> acjImportDownloadProcessor = new JsonImportDownloadProcessor<>(RawSourceAcJudgment.class);
        acjImportDownloadProcessor.setSourceJudgmentParser(sourceAcJudgmentParser());
        
        return acjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener acjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceAcJudgment.class);
        
        return stepExecutionListener;
    }
}
