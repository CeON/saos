package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
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
    
    @Mock
    private SourceCcJudgment sourceCcJudgment;
    
    @Mock
    private RawSourceCcJudgment rJudgment;
    
    @Mock
    private CommonCourtJudgment ccJudgment;
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
   

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        ccjImportProcessProcessor.setCcjProcessingService(ccjProcessingService);
        ccjImportProcessProcessor.setRawSourceCcjConverter(rawSourceCcJudgmentConverter);
        ccjImportProcessProcessor.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        
        when(rJudgment.getId()).thenReturn(101);
        when(rawSourceCcJudgmentRepository.findOne(Mockito.eq(101))).thenReturn(rJudgment);
    }

    
    @Test
    public void process_normalJudgment() throws Exception {
        
        //--------------- data preparation --------------------
        
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
        
        
        verify(rJudgment).markProcessingOk();
        verify(rJudgment).getId();
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verify(ccjProcessingService, never()).processReasoningJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
    
    @Test
    public void process_reasoningJudgment_RelatedJudgmentFound() throws Exception {
        
        //--------------- data preparation --------------------
        
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
        
        
        verify(rJudgment).markProcessingOk();
        verify(rJudgment).getId();
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verify(ccjProcessingService, never()).processNormalJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
    @Test(expected=CcjImportProcessSkippableException.class)
    public void process_reasoningJudgment_RelatedJudgmentNotFound() throws Exception {
        
        //--------------- data preparation --------------------
        
        when(rJudgment.isJustReasons()).thenReturn(true);
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        when(ccjProcessingService.processReasoningJudgment(Mockito.isA(SourceCcJudgment.class))).thenThrow(new CcjImportProcessSkippableException("related judgment not found", ImportProcessingSkipReason.RELATED_JUDGMENT_NOT_FOUND));
                
        
        //---------------- test method invocation -------------
        
        try {
            ccjImportProcessProcessor.process(rJudgment);
        
        } finally {
        
        //---------------- assertions -------------------------
        
        
            verify(rJudgment).isJustReasons();
            verify(rJudgment).getId();
            
            ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
            verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
            assertTrue(argRJudgment.getValue() == rJudgment);
            
            ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
            verify(ccjProcessingService).processReasoningJudgment(argSourceJudgment.capture());
            assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
            
            
            verify(ccjProcessingService, never()).processNormalJudgment(Mockito.any(SourceCcJudgment.class));
            
            verifyNoMoreInteractions(rJudgment);
        }
    }
    
    
    @Test
    public void process_courtNotFound() throws Exception {
        
        exception.expect(CcjImportProcessSkippableException.class);
        exception.expect(CcjSkippableExceptionMatcher.hasSkipReason(ImportProcessingSkipReason.COURT_NOT_FOUND));
        
        //--------------- data preparation --------------------
        
        when(rJudgment.isJustReasons()).thenReturn(false);
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        when(ccjProcessingService.processNormalJudgment(Mockito.isA(SourceCcJudgment.class))).thenThrow(new CcjImportProcessSkippableException("", ImportProcessingSkipReason.COURT_NOT_FOUND));
                
        
        //---------------- test method invocation -------------
        
        ccjImportProcessProcessor.process(rJudgment);
        
        
        verify(rJudgment).isJustReasons();
        verify(rJudgment).getId();
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(ccjProcessingService).processNormalJudgment(argSourceJudgment.capture());
        assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
        
        
        verify(ccjProcessingService, never()).processNormalJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);

    }
    
    
    
}