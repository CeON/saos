package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.enrichment.reference.JudgmentReferenceRemover;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

import com.fasterxml.jackson.core.JsonParseException;

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
    
    @Mock private JudgmentReferenceRemover enrichmentTagReferenceRemover;
    
    
    // data
    
    private RawSourceScJudgment rJudgment = new RawSourceScJudgment();
    
    private SourceScJudgment sourceScJudgment = new SourceScJudgment();
    
    private SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private JudgmentWithCorrectionList<SupremeCourtJudgment> jWithCorrectionList = new JudgmentWithCorrectionList<>(scJudgment, correctionList);

    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        scjImportProcessProcessor.setSourceJudgmentConverter(sourceScJudgmentConverter);
        scjImportProcessProcessor.setSourceJudgmentParser(sourceScJudgmentParser);
        scjImportProcessProcessor.setJudgmentRepository(judgmentRepository);
        scjImportProcessProcessor.setJudgmentOverwriter(judgmentOverwriter);
        scjImportProcessProcessor.setRawSourceJudgmentRepository(rawSourceJudgmentRepository);
        scjImportProcessProcessor.setEnrichmentTagRepository(enrichmentTagRepository);
        scjImportProcessProcessor.setEnrichmentTagReferenceRemover(enrichmentTagReferenceRemover);
    }

    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void process_OldJudgmentNotFound() throws JsonParseException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("AAAXXX");
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                Mockito.any(SourceCode.class), Mockito.anyString(), Mockito.any())).thenReturn(null);
        
        
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
        
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, judgmentRepository);
        verifyZeroInteractions(enrichmentTagRepository);
    }
    
    
    
    
    @Test
    public void process_OldJudgmentFound() throws JsonParseException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("ABCXYZ");
        scJudgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        SupremeCourtJudgment oldScJudgment = new SupremeCourtJudgment();
        long oldScJudgmentId = 2L;
        Whitebox.setInternalState(oldScJudgment, "id", oldScJudgmentId);
        
        when(judgmentRepository.findOneBySourceCodeAndSourceJudgmentId(
                SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId(), SupremeCourtJudgment.class)).thenReturn(oldScJudgment);
        
        
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
        verify(enrichmentTagReferenceRemover).removeReference(oldScJudgmentId);
        
        
        verify(rawSourceJudgmentRepository).save(rJudgment);
         
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, judgmentRepository, judgmentOverwriter);
        verifyNoMoreInteractions(enrichmentTagRepository);
    }

    
}
