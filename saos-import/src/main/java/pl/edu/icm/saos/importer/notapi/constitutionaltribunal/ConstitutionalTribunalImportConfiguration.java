package pl.edu.icm.saos.importer.notapi.constitutionaltribunal;

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
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.json.SourceCtJudgment;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process.CtSpecificJudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process.SourceCtJudgmentExtractor;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
@Configuration
public class ConstitutionalTribunalImportConfiguration {
    
    @Autowired
    private CommonValidator commonValidator;
    
    @Autowired
    private MappingJsonFactory jsonFactory;
    
    @Autowired
    private SourceCtJudgmentExtractor sourceCtJudgmentExtractor;
    
    @Autowired 
    private CtSpecificJudgmentOverwriter ctSpecificJudgmentOverwriter;
    
    
    @Value("${import.judgments.constitutionalTribunal.dir}")
    private String importMetadataDir;
    
    @Value("${import.judgments.constitutionalTribunal.content.dir}")
    private String importContentDir;
    
    @Value("${import.judgments.constitutionalTribunal.download.dir}")
    private String downloadedContentDir;
    
    
    //------------------------ BEANS --------------------------
    
    @Bean
    public JsonStringParser<SourceCtJudgment> sourceCtJudgmentParser() {
        JsonStringParser<SourceCtJudgment> sourceCtJudgmentParser = new JsonStringParser<>(SourceCtJudgment.class);
        sourceCtJudgmentParser.setCommonValidator(commonValidator);
        sourceCtJudgmentParser.setJsonFactory(jsonFactory);
        return sourceCtJudgmentParser;
    }
    
    @Bean
    public JsonImportDownloadReader ctjImportDownloadReader() {
        JsonImportDownloadReader ctjImportDownloadReader = new JsonImportDownloadReader();
        ctjImportDownloadReader.setImportDir(importMetadataDir);
        
        return ctjImportDownloadReader;
    }
    
    @Bean
    public JsonImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor() {
        JsonImportDownloadProcessor<RawSourceCtJudgment> ctjImportDownloadProcessor = new JsonImportDownloadProcessor<>(RawSourceCtJudgment.class);
        ctjImportDownloadProcessor.setSourceJudgmentParser(sourceCtJudgmentParser());
        ctjImportDownloadProcessor.setDownloadedContentDir(downloadedContentDir);
        
        return ctjImportDownloadProcessor;
    }
    
    @Bean
    public NotApiImportDownloadStepExecutionListener ctjImportDownloadStepExecutionListener() {
        NotApiImportDownloadStepExecutionListener stepExecutionListener = new NotApiImportDownloadStepExecutionListener();
        stepExecutionListener.setRawJudgmentClass(RawSourceCtJudgment.class);
        
        return stepExecutionListener;
    }
    
    @Bean
    public ContentDownloadStepExecutionListener ctjContentDownloadStepExecutionListener() {
        
        ContentDownloadStepExecutionListener stepExecutionListener = new ContentDownloadStepExecutionListener();
        stepExecutionListener.setImportMetadataDir(importMetadataDir);
        stepExecutionListener.setImportContentDir(importContentDir);
        stepExecutionListener.setDownloadedContentDir(downloadedContentDir);
        
        return stepExecutionListener;
    }
    
    @Bean
    public JudgmentConverter<ConstitutionalTribunalJudgment, SourceCtJudgment> sourceCtJudgmentConverter() {
        JudgmentConverterImpl<ConstitutionalTribunalJudgment, SourceCtJudgment> judgmentConverter = new JudgmentConverterImpl<>();
        judgmentConverter.setJudgmentDataExtractor(sourceCtJudgmentExtractor);
        return judgmentConverter;
    }
    
    
    @Bean
    public JudgmentOverwriter<ConstitutionalTribunalJudgment> ctJudgmentOverwriter() {
        DelegatingJudgmentOverwriter<ConstitutionalTribunalJudgment> ctJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
        ctJudgmentOverwriter.setSpecificJudgmentOverwriter(ctSpecificJudgmentOverwriter);
        return ctJudgmentOverwriter;
    }



    @Bean
    public JudgmentImportProcessReader<RawSourceCtJudgment> ctjImportProcessReader() {
        JudgmentImportProcessReader<RawSourceCtJudgment> importProcessReader = new JudgmentImportProcessReader<>(RawSourceCtJudgment.class);
        return importProcessReader;
    }
    
    @Bean
    public JsonJudgmentImportProcessProcessor<SourceCtJudgment, ConstitutionalTribunalJudgment> ctjImportProcessProcessor() {
        JsonJudgmentImportProcessProcessor<SourceCtJudgment, ConstitutionalTribunalJudgment> ctjImportProcessProcessor = new JsonJudgmentImportProcessProcessor<>(ConstitutionalTribunalJudgment.class);
        ctjImportProcessProcessor.setSourceJudgmentParser(sourceCtJudgmentParser());
        ctjImportProcessProcessor.setSourceJudgmentConverter(sourceCtJudgmentConverter());
        ctjImportProcessProcessor.setJudgmentOverwriter(ctJudgmentOverwriter());
        ctjImportProcessProcessor.setDownloadedContentDir(downloadedContentDir);
        
        return ctjImportProcessProcessor;
    }
}
