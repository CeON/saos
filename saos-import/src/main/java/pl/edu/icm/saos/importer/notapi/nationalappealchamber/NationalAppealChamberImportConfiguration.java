package pl.edu.icm.saos.importer.notapi.nationalappealchamber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverterImpl;
import pl.edu.icm.saos.importer.common.overwriter.DelegatingJudgmentOverwriter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.content.ContentDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.json.SourceNacJudgment;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process.NacSpecificJudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.nationalappealchamber.judgment.process.SourceNacJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
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
    
    @Autowired
    private SourceNacJudgmentExtractor sourceNacJudgmentExtractor;
    
    @Autowired 
    private NacSpecificJudgmentOverwriter nacSpecificJudgmentOverwriter;
    
    
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
    
    @Bean
    public ContentDownloadStepExecutionListener nacjContentDownloadStepExecutionListener(
            @Value("${import.judgments.nationalAppealChamber.dir}") String importDir,
            @Value("${import.judgments.nationalAppealChamber.content.dir}") String importContentDir) {
        
        ContentDownloadStepExecutionListener stepExecutionListener = new ContentDownloadStepExecutionListener();
        stepExecutionListener.setImportDir(importDir);
        stepExecutionListener.setImportContentDir(importContentDir);
        stepExecutionListener.setRawJudgmentClass(RawSourceNacJudgment.class);
        
        return stepExecutionListener;
    }
    
    @Bean
    public JudgmentConverter<NationalAppealChamberJudgment, SourceNacJudgment> sourceNacJudgmentConverter() {
        JudgmentConverterImpl<NationalAppealChamberJudgment, SourceNacJudgment> judgmentConverter = new JudgmentConverterImpl<>();
        judgmentConverter.setJudgmentDataExtractor(sourceNacJudgmentExtractor);
        return judgmentConverter;
    }
    
    
    @Bean
    public JudgmentOverwriter<NationalAppealChamberJudgment> nacJudgmentOverwriter() {
        DelegatingJudgmentOverwriter<NationalAppealChamberJudgment> nacJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
        nacJudgmentOverwriter.setSpecificJudgmentOverwriter(nacSpecificJudgmentOverwriter);
        return nacJudgmentOverwriter;
    }



    @Bean
    public JudgmentImportProcessReader<RawSourceNacJudgment> nacjImportProcessReader() {
        JudgmentImportProcessReader<RawSourceNacJudgment> importProcessReader = new JudgmentImportProcessReader<>(RawSourceNacJudgment.class);
        return importProcessReader;
    }
    
    @Bean
    public JsonJudgmentImportProcessProcessor<SourceNacJudgment, NationalAppealChamberJudgment> nacjImportProcessProcessor() {
        JsonJudgmentImportProcessProcessor<SourceNacJudgment, NationalAppealChamberJudgment> nacjImportProcessProcessor = new JsonJudgmentImportProcessProcessor<>(NationalAppealChamberJudgment.class);
        nacjImportProcessProcessor.setSourceJudgmentParser(sourceNacJudgmentParser());
        nacjImportProcessProcessor.setSourceJudgmentConverter(sourceNacJudgmentConverter());
        nacjImportProcessProcessor.setJudgmentOverwriter(nacJudgmentOverwriter());
        
        return nacjImportProcessProcessor;
    }
}
