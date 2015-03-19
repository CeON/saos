package pl.edu.icm.saos.importer.notapi.supremecourt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverterImpl;
import pl.edu.icm.saos.importer.common.overwriter.DelegatingJudgmentOverwriter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadReader;
import pl.edu.icm.saos.importer.notapi.common.JsonJudgmentImportProcessProcessor;
import pl.edu.icm.saos.importer.notapi.common.JudgmentImportProcessReader;
import pl.edu.icm.saos.importer.notapi.common.NotApiImportDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.common.JsonImportDownloadProcessor;
import pl.edu.icm.saos.importer.notapi.common.content.ContentDownloadStepExecutionListener;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.SourceScJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
public class SupremeCourtImportConfiguration {

    
    
    @Autowired
    private SourceScJudgmentExtractor sourceScJudgmentExtractor;
    
    @Autowired 
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    
    @Autowired 
    @Qualifier("scSpecificJudgmentOverwriter")
    private JudgmentOverwriter<SupremeCourtJudgment> scSpecificJudgmentOverwriter;
    
    
    @Value("${import.judgments.supremeCourt.dir}")
    private String importMetadataDir;
    
    @Value("${import.judgments.supremeCourt.content.dir}")
    private String importContentDir;
    
    @Value("${import.judgments.supremeCourt.download.dir}")
    private String downloadedContentDir;
    
    
    
    //------------------------ BEANS --------------------------
    

    
    

    
    @Bean
    public JsonStringParser<SourceScJudgment> sourceScJudgmentParser() {
        JsonStringParser<SourceScJudgment> sourceScJudgmentParser = new JsonStringParser<>(SourceScJudgment.class);
        sourceScJudgmentParser.setCommonValidator(commonValidator);
        sourceScJudgmentParser.setJsonFactory(jsonFactory);
        return sourceScJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader scjImportDownloadReader() {
        JsonImportDownloadReader scjImportDownloadReader = new JsonImportDownloadReader();
        scjImportDownloadReader.setImportDir(importMetadataDir);
        
        return scjImportDownloadReader;
    }
    
    @Bean
    public JsonImportDownloadProcessor<RawSourceScJudgment> scjImportDownloadProcessor() {
        JsonImportDownloadProcessor<RawSourceScJudgment> scjImportDownloadProcessor = new JsonImportDownloadProcessor<>(RawSourceScJudgment.class);
        scjImportDownloadProcessor.setSourceJudgmentParser(sourceScJudgmentParser());
        scjImportDownloadProcessor.setDownloadedContentDir(downloadedContentDir);
        
        return scjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener scjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceScJudgment.class);
        
        return stepExecutionListener;
    }
    
    @Bean
    public ContentDownloadStepExecutionListener scjContentDownloadStepExecutionListener() {
        
        ContentDownloadStepExecutionListener stepExecutionListener = new ContentDownloadStepExecutionListener();
        stepExecutionListener.setImportMetadataDir(importMetadataDir);
        stepExecutionListener.setImportContentDir(importContentDir);
        stepExecutionListener.setDownloadedContentDir(downloadedContentDir);
        
        return stepExecutionListener;
    }
    
    
    @Bean
    public JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter() {
        JudgmentConverterImpl<SupremeCourtJudgment, SourceScJudgment> judgmentConverter = new JudgmentConverterImpl<>();
        judgmentConverter.setJudgmentDataExtractor(sourceScJudgmentExtractor);
        return judgmentConverter;
    }
    
    
    @Bean
    public JudgmentOverwriter<SupremeCourtJudgment> scJudgmentOverwriter() {
        DelegatingJudgmentOverwriter<SupremeCourtJudgment> scJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
        scJudgmentOverwriter.setSpecificJudgmentOverwriter(scSpecificJudgmentOverwriter);
        return scJudgmentOverwriter;
    }

  

    @Bean
    public JudgmentImportProcessReader<RawSourceScJudgment> scjImportProcessReader() {
        JudgmentImportProcessReader<RawSourceScJudgment> importProcessReader = new JudgmentImportProcessReader<RawSourceScJudgment>(RawSourceScJudgment.class);
        return importProcessReader;
    }
    
    @Bean
    public JsonJudgmentImportProcessProcessor<SourceScJudgment, SupremeCourtJudgment> scjImportProcessProcessor() {
        JsonJudgmentImportProcessProcessor<SourceScJudgment, SupremeCourtJudgment> scjImportProcessProcessor = new JsonJudgmentImportProcessProcessor<>(SupremeCourtJudgment.class);
        scjImportProcessProcessor.setSourceJudgmentParser(sourceScJudgmentParser());
        scjImportProcessProcessor.setSourceJudgmentConverter(sourceScJudgmentConverter());
        scjImportProcessProcessor.setJudgmentOverwriter(scJudgmentOverwriter());
        
        return scjImportProcessProcessor;
    }
    
}
