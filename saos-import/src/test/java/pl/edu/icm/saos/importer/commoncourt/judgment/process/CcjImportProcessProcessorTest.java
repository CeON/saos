package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingStatus;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportProcessProcessorTest {

    private CcjImportProcessProcessor ccjImportProcessProcessor = new CcjImportProcessProcessor();
    
    @Mock
    private RawSourceCcJudgmentConverter rawSourceCcJudgmentConverter;
    
    @Mock
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    @Mock
    private CcjProcessingService ccjProcessingService;
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        ccjImportProcessProcessor.setCcjProcessingService(ccjProcessingService);
        ccjImportProcessProcessor.setRawSourceCcjConverter(rawSourceCcJudgmentConverter);
        ccjImportProcessProcessor.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        
    }

    
    @Test
    public void process_normalJudgment() throws Exception {
        
        //--------------- data preparation --------------------
        
        SourceCcJudgment sourceCcJudgment = mock(SourceCcJudgment.class);
        RawSourceCcJudgment rJudgment = mock(RawSourceCcJudgment.class);
        CommonCourtJudgment ccJudgment = mock(CommonCourtJudgment.class);
        
        when(rJudgment.isJustReasons()).thenReturn(false);
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        when(ccjProcessingService.processNormalJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
                
        
        //---------------- test method invocation -------------
        
        CommonCourtJudgment retCcJudgment = ccjImportProcessProcessor.process(rJudgment);
        
        
        //---------------- assertions -------------------------
        
        assertTrue(retCcJudgment == ccJudgment);
        
        verify(rJudgment).isJustReasons();
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(ccjProcessingService).processNormalJudgment(argSourceJudgment.capture());
        assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
        
        
        verify(rJudgment).updateProcessingStatus(ImportProcessingStatus.OK);
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verify(ccjProcessingService, never()).processReasoningJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
    
    @Test
    public void process_reasoningJudgment_RelatedJudgmentNotFound() throws Exception {
        
        //--------------- data preparation --------------------
        
        SourceCcJudgment sourceCcJudgment = mock(SourceCcJudgment.class);
        RawSourceCcJudgment rJudgment = mock(RawSourceCcJudgment.class);
        CommonCourtJudgment ccJudgment = mock(CommonCourtJudgment.class);
        
        when(rJudgment.isJustReasons()).thenReturn(true);
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        when(ccjProcessingService.processReasoningJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
                
        
        //---------------- test method invocation -------------
        
        CommonCourtJudgment retCcJudgment = ccjImportProcessProcessor.process(rJudgment);
        
        
        //---------------- assertions -------------------------
        
        assertTrue(ccJudgment == retCcJudgment);
        
        verify(rJudgment).isJustReasons();
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(ccjProcessingService).processReasoningJudgment(argSourceJudgment.capture());
        assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
        
        
        verify(rJudgment).updateProcessingStatus(ImportProcessingStatus.OK);
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verify(ccjProcessingService, never()).processNormalJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
    @Test
    public void process_reasoningJudgment_RelatedJudgmentFound() throws Exception {
        
        //--------------- data preparation --------------------
        
        SourceCcJudgment sourceCcJudgment = mock(SourceCcJudgment.class);
        RawSourceCcJudgment rJudgment = mock(RawSourceCcJudgment.class);
        
        when(rJudgment.isJustReasons()).thenReturn(true);
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        when(ccjProcessingService.processReasoningJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(null);
                
        
        //---------------- test method invocation -------------
        
        CommonCourtJudgment retCcJudgment = ccjImportProcessProcessor.process(rJudgment);
        
        
        //---------------- assertions -------------------------
        
        assertNull(retCcJudgment);
        
        verify(rJudgment).isJustReasons();
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(ccjProcessingService).processReasoningJudgment(argSourceJudgment.capture());
        assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
        
        
        verify(rJudgment).updateProcessingStatus(ImportProcessingStatus.RELATED_JUDGMENT_NOT_FOUND);
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verify(ccjProcessingService, never()).processNormalJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
}