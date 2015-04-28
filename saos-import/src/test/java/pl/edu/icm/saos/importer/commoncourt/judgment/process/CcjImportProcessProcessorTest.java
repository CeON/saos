package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
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

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.googlecode.catchexception.CatchException;
import com.googlecode.catchexception.apis.CatchExceptionAssertJ;



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
    
    @Mock
    private ImportCorrectionList correctionList;
    
   

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        ccjImportProcessProcessor.setCcjProcessingService(ccjProcessingService);
        ccjImportProcessProcessor.setRawSourceCcjConverter(rawSourceCcJudgmentConverter);
        ccjImportProcessProcessor.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        
        Mockito.when(rJudgment.getId()).thenReturn(101l);
        when(rawSourceCcJudgmentRepository.findOne(Mockito.eq(101l))).thenReturn(rJudgment);
    }

    
    @Test
    public void process_ok() throws Exception {
        
        //--------------- data preparation --------------------
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenReturn(sourceCcJudgment);
        
        JudgmentWithCorrectionList<CommonCourtJudgment> jWithCorrectionList = new JudgmentWithCorrectionList<>(ccJudgment, correctionList);
        when(ccjProcessingService.processJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(jWithCorrectionList);
                
        
        //---------------- test method invocation -------------
        
        JudgmentWithCorrectionList<CommonCourtJudgment> retJWithCorrectionList = ccjImportProcessProcessor.process(rJudgment);
        
        
        //---------------- assertions -------------------------
        
        assertTrue(retJWithCorrectionList == jWithCorrectionList);
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(ccjProcessingService).processJudgment(argSourceJudgment.capture());
        assertTrue(argSourceJudgment.getValue() == sourceCcJudgment); 
        
        
        verify(rJudgment).markProcessed();
        verify(rJudgment).getId();
        
        argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentRepository).save(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        verifyNoMoreInteractions(rJudgment);
    }
    
    
    
    @Test
    public void process_courtNotFound() throws Exception {
        
        
        //--------------- data preparation --------------------
        
        
        when(rawSourceCcJudgmentConverter.convertSourceCcJudgment(Mockito.isA(RawSourceCcJudgment.class))).thenThrow(new CcjImportProcessSkippableException("", ImportProcessingSkipReason.COURT_NOT_FOUND));
               
        
        //---------------- test method invocation -------------
        
        CatchExceptionAssertJ.when(ccjImportProcessProcessor).process(rJudgment);
        
        CatchExceptionAssertJ.then(CatchException.caughtException())
                             .isExactlyInstanceOf(CcjImportProcessSkippableException.class);
        assertThat((CcjImportProcessSkippableException) CatchException.caughtException(), CcjSkippableExceptionMatcher.hasSkipReason(ImportProcessingSkipReason.COURT_NOT_FOUND));
            
            
        verify(rJudgment).getId();
        
        ArgumentCaptor<RawSourceCcJudgment> argRJudgment = ArgumentCaptor.forClass(RawSourceCcJudgment.class);
        verify(rawSourceCcJudgmentConverter).convertSourceCcJudgment(argRJudgment.capture());
        assertTrue(argRJudgment.getValue() == rJudgment);
        
        
        verify(ccjProcessingService, never()).processJudgment(Mockito.any(SourceCcJudgment.class));
        
        verifyNoMoreInteractions(rJudgment);

    }
    
    
    
}