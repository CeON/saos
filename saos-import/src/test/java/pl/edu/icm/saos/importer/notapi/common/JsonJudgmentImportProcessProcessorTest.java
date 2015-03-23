package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.springframework.batch.core.scope.context.ChunkContext;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.common.content.ContentTypeExtractor;
import pl.edu.icm.saos.importer.notapi.common.content.InputStreamWithFilename;
import pl.edu.icm.saos.importer.notapi.common.content.JudgmentContentFileExtractor;
import pl.edu.icm.saos.importer.notapi.common.content.JudgmentContentFilePathGenerator;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransaction;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.FilesOperationsTransactionManager;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class JsonJudgmentImportProcessProcessorTest {

    private JsonJudgmentImportProcessProcessor<SourceScJudgment, SupremeCourtJudgment> scjImportProcessProcessor = 
            new JsonJudgmentImportProcessProcessor<>(SupremeCourtJudgment.class);
    
    
    // services
    
    @Mock private JsonStringParser<SourceScJudgment> sourceScJudgmentParser;
    
    @Mock private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    @Mock private JudgmentRepository judgmentRepository;
    
    @Mock private JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter;
    
    @Mock private RawSourceJudgmentRepository rawSourceJudgmentRepository;
    
    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    
    @Mock private JudgmentContentFileExtractor judgmentContentFileExtractor;
    
    @Mock private JudgmentContentFilePathGenerator judgmentContentFilePathGenerator;
    
    @Mock private ContentTypeExtractor contentTypeExtractor;
    
    @Mock private FilesOperationsTransactionManager filesOperationsTransactionManager;
    
    
    private String downloadedContentDir = "/downloaded/content/path/";
    
    
    // data
    
    private RawSourceScJudgment rJudgment = new RawSourceScJudgment();
    
    private SourceScJudgment sourceScJudgment = new SourceScJudgment();
    
    private SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private JudgmentWithCorrectionList<SupremeCourtJudgment> jWithCorrectionList = new JudgmentWithCorrectionList<>(scJudgment, correctionList);
    
    private String transactionId = "transactionId";

    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        scjImportProcessProcessor.setSourceJudgmentConverter(sourceScJudgmentConverter);
        scjImportProcessProcessor.setSourceJudgmentParser(sourceScJudgmentParser);
        scjImportProcessProcessor.setJudgmentRepository(judgmentRepository);
        scjImportProcessProcessor.setJudgmentOverwriter(judgmentOverwriter);
        scjImportProcessProcessor.setRawSourceJudgmentRepository(rawSourceJudgmentRepository);
        scjImportProcessProcessor.setEnrichmentTagRepository(enrichmentTagRepository);
        scjImportProcessProcessor.setJudgmentContentFileExtractor(judgmentContentFileExtractor);
        scjImportProcessProcessor.setJudgmentContentFilePathGenerator(judgmentContentFilePathGenerator);
        scjImportProcessProcessor.setContentTypeExtractor(contentTypeExtractor);
        scjImportProcessProcessor.setFilesOperationsTransactionManager(filesOperationsTransactionManager);
        scjImportProcessProcessor.setDownloadedContentDir(downloadedContentDir);
        
        ChunkContext chunkContext = Mockito.mock(ChunkContext.class);
        when(chunkContext.getAttribute("transactionId")).thenReturn(transactionId);
        scjImportProcessProcessor.beforeChunk(chunkContext);
        
    }

    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void process_OldJudgmentNotFound() throws IOException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        rJudgment.setJudgmentContentFilename("contentFilename.zip");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("AAAXXX");
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                Mockito.any(SourceCode.class), Mockito.anyString(), Mockito.any())).thenReturn(null);
        
        
        InputStreamWithFilename contentStream = Mockito.mock(InputStreamWithFilename.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        when(contentStream.getFilename()).thenReturn("judgmentContent.pdf");
        when(contentStream.getInputStream()).thenReturn(inputStream);
        
        FilesOperationsTransaction filesOperationsTransaction = Mockito.mock(FilesOperationsTransaction.class);
        
        when(filesOperationsTransactionManager.fetchTransaction(transactionId)).thenReturn(filesOperationsTransaction);
        when(judgmentContentFileExtractor.extractJudgmentContent(new File(downloadedContentDir, "contentFilename.zip"), "AAAXXX")).thenReturn(contentStream);
        when(judgmentContentFilePathGenerator.generatePath(Mockito.any())).thenReturn("/judgment/content/path/");
        when(contentTypeExtractor.extractContentType("judgmentContent.pdf")).thenReturn(ContentType.PDF);
        
        
        // execute
        
        JudgmentWithCorrectionList<SupremeCourtJudgment> retJWithCorrectionList = scjImportProcessProcessor.process(rJudgment);
        
        // assert
        
        assertTrue(jWithCorrectionList == retJWithCorrectionList);
        assertTrue(retJWithCorrectionList.getJudgment() == scJudgment);
        assertTrue(retJWithCorrectionList.getCorrectionList() == correctionList);
        
        
        assertTrue(rJudgment.isProcessed());
        assertNotNull(rJudgment.getProcessingDate());
        
        verify(sourceScJudgmentParser).parseAndValidate(rJudgment.getJsonContent());
        verify(sourceScJudgmentConverter).convertJudgment(sourceScJudgment);
        verify(judgmentRepository).findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId(), SupremeCourtJudgment.class);
        verify(rawSourceJudgmentRepository).save(rJudgment);
        
        verify(filesOperationsTransactionManager).fetchTransaction(transactionId);
        verify(judgmentContentFileExtractor).extractJudgmentContent(new File(downloadedContentDir, "contentFilename.zip"), "AAAXXX");
        verify(judgmentContentFilePathGenerator).generatePath(scJudgment);
        verify(contentTypeExtractor).extractContentType("judgmentContent.pdf");
        
        verify(filesOperationsTransaction).addFile(inputStream, "/judgment/content/path/judgmentContent.pdf");
        verify(contentStream).close();
        
        
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, judgmentRepository);
        verifyNoMoreInteractions(filesOperationsTransactionManager, judgmentContentFileExtractor, judgmentContentFilePathGenerator, contentTypeExtractor);
        verifyZeroInteractions(enrichmentTagRepository);
    }
    
    
    
    
    @Test
    public void process_OldJudgmentFound() throws IOException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        rJudgment.setJudgmentContentFilename("contentFilename.zip");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("ABCXYZ");
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        SupremeCourtJudgment oldScJudgment = new SupremeCourtJudgment();
        long oldScJudgmentId = 2L;
        oldScJudgment.getTextContent().setFilePath("/old/judgment/content/path.pdf");
        Whitebox.setInternalState(oldScJudgment, "id", oldScJudgmentId);
        
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId(), SupremeCourtJudgment.class)).thenReturn(oldScJudgment);
        
        
        InputStreamWithFilename contentStream = Mockito.mock(InputStreamWithFilename.class);
        InputStream inputStream = Mockito.mock(InputStream.class);
        when(contentStream.getFilename()).thenReturn("judgmentContent.pdf");
        when(contentStream.getInputStream()).thenReturn(inputStream);
        
        FilesOperationsTransaction filesOperationsTransaction = Mockito.mock(FilesOperationsTransaction.class);
        
        when(filesOperationsTransactionManager.fetchTransaction(transactionId)).thenReturn(filesOperationsTransaction);
        when(judgmentContentFileExtractor.extractJudgmentContent(new File(downloadedContentDir, "contentFilename.zip"), "ABCXYZ")).thenReturn(contentStream);
        when(judgmentContentFilePathGenerator.generatePath(Mockito.any())).thenReturn("/judgment/content/path/");
        when(contentTypeExtractor.extractContentType("judgmentContent.pdf")).thenReturn(ContentType.PDF);
        
        
        // execute
        
        JudgmentWithCorrectionList<SupremeCourtJudgment> retJWithCorrectionList = scjImportProcessProcessor.process(rJudgment);
        
        // assert
        
        assertTrue(jWithCorrectionList == retJWithCorrectionList);
        assertTrue(retJWithCorrectionList.getJudgment() == oldScJudgment);
        assertFalse(retJWithCorrectionList.getJudgment() == scJudgment);
        assertTrue(retJWithCorrectionList.getCorrectionList() == correctionList);
        
        assertTrue(rJudgment.isProcessed());
        assertNotNull(rJudgment.getProcessingDate());
        
        verify(sourceScJudgmentParser).parseAndValidate(rJudgment.getJsonContent());
        verify(sourceScJudgmentConverter).convertJudgment(sourceScJudgment);
        
        verify(judgmentRepository).findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId(), SupremeCourtJudgment.class);
        verify(judgmentOverwriter).overwriteJudgment(oldScJudgment, scJudgment, correctionList);
        
        verify(enrichmentTagRepository).deleteAllByJudgmentId(oldScJudgmentId);
        
        
        verify(rawSourceJudgmentRepository).save(rJudgment);
        
        
        verify(filesOperationsTransactionManager).fetchTransaction(transactionId);
        verify(judgmentContentFileExtractor).extractJudgmentContent(new File(downloadedContentDir, "contentFilename.zip"), "ABCXYZ");
        verify(judgmentContentFilePathGenerator).generatePath(scJudgment);
        verify(contentTypeExtractor).extractContentType("judgmentContent.pdf");
        
        verify(filesOperationsTransaction).overwriteFile(inputStream, "/judgment/content/path/judgmentContent.pdf", "/old/judgment/content/path.pdf");
        verify(contentStream).close();
         
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, judgmentRepository, judgmentOverwriter);
        verifyNoMoreInteractions(filesOperationsTransactionManager, judgmentContentFileExtractor, judgmentContentFilePathGenerator, contentTypeExtractor);
        verifyNoMoreInteractions(enrichmentTagRepository);
    }

    
}
