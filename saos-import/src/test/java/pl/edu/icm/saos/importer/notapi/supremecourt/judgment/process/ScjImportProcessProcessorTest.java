package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgmentParser;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportProcessProcessorTest {

    private ScjImportProcessProcessor scjImportProcessProcessor = new ScjImportProcessProcessor();
    
    @Mock private SourceScJudgmentParser sourceScJudgmentParser;
    
    @Mock private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    @Mock private ScJudgmentRepository scJudgmentRepository;
    
    @Mock private JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter;
    
    @Mock private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        scjImportProcessProcessor.setSourceScJudgmentConverter(sourceScJudgmentConverter);
        scjImportProcessProcessor.setSourceScJudgmentParser(sourceScJudgmentParser);
        scjImportProcessProcessor.setScJudgmentRepository(scJudgmentRepository);
        scjImportProcessProcessor.setJudgmentOverwriter(judgmentOverwriter);
        scjImportProcessProcessor.setRawSourceScJudgmentRepository(rawSourceScJudgmentRepository);
        
    }

    
    @Test
    public void process_OldJudgmentNotFound() {
        
        // given
        
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent("12121212esfcsfc");
        
        SourceScJudgment sourceScJudgment = new SourceScJudgment();
        when(sourceScJudgmentParser.parse(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        scJudgment.getSourceInfo().setSourceJudgmentId("AAAXXX");
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(scJudgment);
        
        when(scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.any(SourceCode.class), Mockito.anyString())).thenReturn(null);
        
        
        // execute
        
        SupremeCourtJudgment retScJudgment = scjImportProcessProcessor.process(rJudgment);
        
        // assert
        
        assertTrue(scJudgment == retScJudgment);
        assertTrue(rJudgment.isProcessed());
        assertNotNull(rJudgment.getProcessingDate());
        
        verify(sourceScJudgmentParser).parse(rJudgment.getJsonContent());
        verify(sourceScJudgmentConverter).convertJudgment(sourceScJudgment);
        verify(scJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        verify(rawSourceScJudgmentRepository).saveAndFlush(rJudgment);
        
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, scJudgmentRepository);
    }
    
    
    
    
    @Test
    public void process_OldJudgmentFound() {
        
        // given
        
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent("12121212esfcsfc");
        
        SourceScJudgment sourceScJudgment = new SourceScJudgment();
        when(sourceScJudgmentParser.parse(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        scJudgment.getSourceInfo().setSourceJudgmentId("ABCXYZ");
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(scJudgment);
        
        SupremeCourtJudgment oldScJudgment = new SupremeCourtJudgment();
        when(scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId())).thenReturn(oldScJudgment);
        
        
        // execute
        
        SupremeCourtJudgment retScJudgment = scjImportProcessProcessor.process(rJudgment);
        
        // assert
        
        assertTrue(oldScJudgment == retScJudgment);
        assertFalse(scJudgment == retScJudgment);
        assertTrue(rJudgment.isProcessed());
        assertNotNull(rJudgment.getProcessingDate());
        
        
        verify(sourceScJudgmentParser).parse(rJudgment.getJsonContent());
        verify(sourceScJudgmentConverter).convertJudgment(sourceScJudgment);
        verify(scJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        verify(judgmentOverwriter).overwriteJudgment(oldScJudgment, scJudgment);
        verify(rawSourceScJudgmentRepository).saveAndFlush(rJudgment);
         
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, scJudgmentRepository, judgmentOverwriter);
    }

    
}
