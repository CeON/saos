package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.content.ContentTypeExtractor;
import pl.edu.icm.saos.importer.notapi.common.content.InputStreamWithFilename;
import pl.edu.icm.saos.importer.notapi.common.content.JudgmentContentFileExtractor;
import pl.edu.icm.saos.importer.notapi.common.content.JudgmentContentFilePathGenerator;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransaction;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransactionManager;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.JsonRawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * Spring batch import - process - processor for json based raw judgments
 * 
 * @author Łukasz Dumiszewski
 * @param <S> - model class for json objects
 * @param <J> - type of target judgment 
 */
public class JsonJudgmentImportProcessProcessor<S, J extends Judgment> implements ItemProcessor<JsonRawSourceJudgment, JudgmentWithCorrectionList<J>> {

    private Logger log = LoggerFactory.getLogger(JsonJudgmentImportProcessProcessor.class);
    
    
    private JsonStringParser<S> sourceJudgmentParser;
    
    private JudgmentConverter<J, S> sourceJudgmentConverter;
    
    private JudgmentRepository judgmentRepository;
    
    private JudgmentOverwriter<J> judgmentOverwriter;
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    private JudgmentContentFileExtractor judgmentContentFileExtractor;
    
    private JudgmentContentFilePathGenerator judgmentContentFilePathGenerator;
    
    private ContentTypeExtractor contentTypeExtractor;
    
    private FilesOperationsTransactionManager filesOperationsTransactionManager;
    
    
    private String downloadedContentDir;
    
    private ChunkContext chunkContext;
    
    
    private Class<J> judgmentClass;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonJudgmentImportProcessProcessor(Class<J> judgmentClass) {
        this.judgmentClass = judgmentClass;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @BeforeChunk
    public void beforeChunk(ChunkContext context) {
        this.chunkContext = context;
    }
    
    @Override
    public JudgmentWithCorrectionList<J> process(JsonRawSourceJudgment rJudgment) throws IOException {
        
        log.trace("Processing: {} id={}", rJudgment.getClass().getName(), rJudgment.getId());

        
        S sourceJudgment = sourceJudgmentParser.parseAndValidate(rJudgment.getJsonContent());
        
        JudgmentWithCorrectionList<J> judgmentWithCorrectionList = sourceJudgmentConverter.convertJudgment(sourceJudgment);
        
        J judgment = judgmentWithCorrectionList.getJudgment();
        

        J oldJudgment = judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                judgment.getSourceInfo().getSourceCode(),
                judgment.getSourceInfo().getSourceJudgmentId(),
                judgmentClass);
        
        
        handleJudgmentContent(rJudgment.getJudgmentContentFilename(), judgment, oldJudgment);
        
        if (oldJudgment != null) {
            
            log.trace("same found (rJudgmentId:{}, judgmentId: {}), updating...", rJudgment.getId(), oldJudgment.getId());
            
            judgmentOverwriter.overwriteJudgment(oldJudgment, judgment, judgmentWithCorrectionList.getCorrectionList());
            
            enrichmentTagRepository.deleteAllByJudgmentId(oldJudgment.getId());
            
            judgmentWithCorrectionList.setJudgment(oldJudgment);
            
        }
        
        
        markProcessed(rJudgment);
        
        return judgmentWithCorrectionList;
    }


    
    //------------------------ PRIVATE --------------------------
    
    
    private void markProcessed(JsonRawSourceJudgment rJudgment) {
        rJudgment.markProcessed();
        rawSourceJudgmentRepository.save(rJudgment);
    }
    
    private void handleJudgmentContent(String downloadedContentFilename, Judgment judgment, Judgment oldJudgment) throws IOException {
        String transactionId = (String)chunkContext.getAttribute("transactionId");
        FilesOperationsTransaction contentFilesTransaction = filesOperationsTransactionManager.fetchTransaction(transactionId);
        
        Preconditions.checkNotNull(contentFilesTransaction);
        
        InputStreamWithFilename judgmentContent = null; 
        try {
            judgmentContent = judgmentContentFileExtractor.extractJudgmentContent(new File(downloadedContentDir, downloadedContentFilename), judgment.getSourceInfo().getSourceJudgmentId());

            updateContentFileInfo(judgment, judgmentContent.getFilename());

            if (oldJudgment != null && oldJudgment.getTextContent().haveExternalContent()) {
                contentFilesTransaction.overwriteFile(judgmentContent.getInputStream(), judgment.getTextContent().getFilePath(), oldJudgment.getTextContent().getFilePath());
            } else {
                contentFilesTransaction.addFile(judgmentContent.getInputStream(), judgment.getTextContent().getFilePath());
            }
        } finally {
            IOUtils.closeQuietly(judgmentContent);
        }
    }
    
    private void updateContentFileInfo(Judgment judgment, String contentFileName) {
        
        String path = judgmentContentFilePathGenerator.generatePath(judgment) + contentFileName;
        judgment.getTextContent().setFilePath(path);
        judgment.getTextContent().setType(contentTypeExtractor.extractContentType(contentFileName));
    }


    //------------------------ SETTERS --------------------------
    
    public void setSourceJudgmentParser(JsonStringParser<S> sourceJudgmentParser) {
        this.sourceJudgmentParser = sourceJudgmentParser;
    }

    public void setSourceJudgmentConverter(JudgmentConverter<J, S> sourceJudgmentConverter) {
        this.sourceJudgmentConverter = sourceJudgmentConverter;
    }

    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    public void setJudgmentOverwriter(JudgmentOverwriter<J> judgmentOverwriter) {
        this.judgmentOverwriter = judgmentOverwriter;
    }

    @Autowired
    public void setRawSourceJudgmentRepository(RawSourceJudgmentRepository rawSourceJudgmentRepository) {
        this.rawSourceJudgmentRepository = rawSourceJudgmentRepository;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }


    @Autowired
    public void setJudgmentContentFileExtractor(JudgmentContentFileExtractor judgmentContentFileExtractor) {
        this.judgmentContentFileExtractor = judgmentContentFileExtractor;
    }

    @Autowired
    public void setJudgmentContentFilePathGenerator(JudgmentContentFilePathGenerator judgmentContentFilePathGenerator) {
        this.judgmentContentFilePathGenerator = judgmentContentFilePathGenerator;
    }

    @Autowired
    public void setContentTypeExtractor(ContentTypeExtractor contentTypeExtractor) {
        this.contentTypeExtractor = contentTypeExtractor;
    }

    @Autowired
    public void setFilesOperationsTransactionManager(FilesOperationsTransactionManager filesOperationsTransactionManager) {
        this.filesOperationsTransactionManager = filesOperationsTransactionManager;
    }


    public void setDownloadedContentDir(String downloadedContentDir) {
        this.downloadedContentDir = downloadedContentDir;
    }

}
