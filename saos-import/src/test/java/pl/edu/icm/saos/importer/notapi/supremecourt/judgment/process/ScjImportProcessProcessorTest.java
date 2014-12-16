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

import pl.edu.icm.saos.common.json.JsonItemParser;
import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentRepository;

import com.fasterxml.jackson.core.JsonParseException;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportProcessProcessorTest {

    private ScjImportProcessProcessor scjImportProcessProcessor = new ScjImportProcessProcessor();
    
    
    // services
    
    @Mock private JsonItemParser<SourceScJudgment> sourceScJudgmentParser;
    
    @Mock private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    @Mock private ScJudgmentRepository scJudgmentRepository;
    
    @Mock private JudgmentOverwriter<SupremeCourtJudgment> judgmentOverwriter;
    
    @Mock private RawSourceScJudgmentRepository rawSourceScJudgmentRepository;
    
    
    // data
    
    private RawSourceScJudgment rJudgment = new RawSourceScJudgment();
    
    private SourceScJudgment sourceScJudgment = new SourceScJudgment();
    
    private SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private JudgmentWithCorrectionList<SupremeCourtJudgment> jWithCorrectionList = new JudgmentWithCorrectionList<>(scJudgment, correctionList);

    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        scjImportProcessProcessor.setSourceScJudgmentConverter(sourceScJudgmentConverter);
        scjImportProcessProcessor.setSourceScJudgmentParser(sourceScJudgmentParser);
        scjImportProcessProcessor.setScJudgmentRepository(scJudgmentRepository);
        scjImportProcessProcessor.setJudgmentOverwriter(judgmentOverwriter);
        scjImportProcessProcessor.setRawSourceScJudgmentRepository(rawSourceScJudgmentRepository);
        
    }

    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void process_OldJudgmentNotFound() throws JsonParseException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("AAAXXX");
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        when(scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.any(SourceCode.class), Mockito.anyString())).thenReturn(null);
        
        
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
        verify(scJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        verify(rawSourceScJudgmentRepository).save(rJudgment);
        
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, scJudgmentRepository);
    }
    
    
    
    
    @Test
    public void process_OldJudgmentFound() throws JsonParseException {
        
        // given
        
        rJudgment.setJsonContent("12121212esfcsfc");
        
        when(sourceScJudgmentParser.parseAndValidate(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        scJudgment.getSourceInfo().setSourceJudgmentId("ABCXYZ");
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(jWithCorrectionList);
        
        SupremeCourtJudgment oldScJudgment = new SupremeCourtJudgment();
        when(scJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId())).thenReturn(oldScJudgment);
        
        
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
        verify(scJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(SourceCode.SUPREME_COURT, scJudgment.getSourceInfo().getSourceJudgmentId());
        verify(judgmentOverwriter).overwriteJudgment(oldScJudgment, scJudgment, correctionList);
        verify(rawSourceScJudgmentRepository).save(rJudgment);
         
        verifyNoMoreInteractions(sourceScJudgmentParser, sourceScJudgmentConverter, scJudgmentRepository, judgmentOverwriter);
    }

    
}
