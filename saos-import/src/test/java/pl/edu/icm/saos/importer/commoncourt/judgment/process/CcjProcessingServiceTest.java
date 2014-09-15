package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.common.JudgmentOverwriter;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
public class CcjProcessingServiceTest {

    private CcjProcessingService ccjProcessingService = new CcjProcessingService();
    
    @Mock
    private CcjReasoningMerger ccjReasoningMerger;
    
    @Mock
    private CcJudgmentRepository ccJudgmentRepository;
    
    @Mock
    private JudgmentOverwriter<CommonCourtJudgment> judgmentOverwriter;
    
    @Mock
    private JudgmentConverter<CommonCourtJudgment, SourceCcJudgment> sourceCcJudgmentConverter;
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        ccjProcessingService.setCcjReasoningMerger(ccjReasoningMerger);
        ccjProcessingService.setCcJudgmentRepository(ccJudgmentRepository);
        ccjProcessingService.setJudgmentOverwriter(judgmentOverwriter);
        ccjProcessingService.setSourceCcJudgmentConverter(sourceCcJudgmentConverter);
        
    }
    
    
    @Test
    public void processNormal_New() {
        
        //------------------ data preparation -----------------------

        SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
        when(ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()))).thenReturn(null);
        

        //------------------ method invocation -----------------------

        CommonCourtJudgment retCcJudgment = ccjProcessingService.processNormalJudgment(sourceCcJudgment);
      
        
        //------------------ assertions -----------------------
        
        assertTrue(ccJudgment == retCcJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
        assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
        
        verify(ccJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()));
        
        verifyZeroInteractions(judgmentOverwriter, ccjReasoningMerger);
    }
    
    
    @Test
    public void processNormal_Existing() {
        
        //------------------ data preparation -----------------------

        SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        CommonCourtJudgment existingCcJudgment = new CommonCourtJudgment();
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
        when(ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()))).thenReturn(existingCcJudgment);
        
        
        //------------------ method invocation -----------------------

        CommonCourtJudgment retCcJudgment = ccjProcessingService.processNormalJudgment(sourceCcJudgment);
        
        
        //------------------ assertions -----------------------
        
        assertTrue(existingCcJudgment == retCcJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
        assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
        
        verify(ccJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()));
        
        ArgumentCaptor<CommonCourtJudgment> argOldJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        ArgumentCaptor<CommonCourtJudgment> argNewJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        verify(judgmentOverwriter).overwriteJudgment(argOldJudgment.capture(), argNewJudgment.capture());
        assertTrue(existingCcJudgment == argOldJudgment.getValue());
        assertTrue(ccJudgment == argNewJudgment.getValue());
        
        verifyZeroInteractions(ccjReasoningMerger);
    }
    
    
    
    
    @Test(expected=CcjImportProcessSkippableException.class)
    public void processReasoning_RelatedNotFound() {
        
        //------------------ data preparation -----------------------
        
        SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        ccJudgment.addCourtCase(new CourtCase("12122dsdcsc"));
        ccJudgment.setCourtDivision(new CommonCourtDivision());
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
        when(ccJudgmentRepository.findBySourceCodeAndCaseNumber(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getCaseNumbers().get(0)))).thenReturn(null);
        
        //------------------ method invocation -----------------------
        
        try {
            ccjProcessingService.processReasoningJudgment(sourceCcJudgment);
        }
        
        finally {

        //------------------ assertions -----------------------
        
            ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
            verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
            assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
            
            verify(ccJudgmentRepository).findBySourceCodeAndCaseNumber(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getCaseNumbers().get(0)));
            
            
            verifyZeroInteractions(judgmentOverwriter, ccjReasoningMerger);
        }
    }
    
    
    @Test
    public void processReasoning_RelatedFound() {
        
        //------------------ data preparation -----------------------
        
        SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
        
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        ccJudgment.addCourtCase(new CourtCase("12122dsdcsc"));
        
        
        CommonCourtJudgment relatedJudgment = new CommonCourtJudgment();
        relatedJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        relatedJudgment.getSourceInfo().setSourceJudgmentId("2341232345");
        relatedJudgment.addCourtCase(new CourtCase("12122dsdcsc"));
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(ccJudgment);
        when(ccJudgmentRepository.findBySourceCodeAndCaseNumber(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getCaseNumbers().get(0)))).thenReturn(Lists.newArrayList(relatedJudgment));
        
        //------------------ method invocation -----------------------
        
        CommonCourtJudgment retCcJudgment = ccjProcessingService.processReasoningJudgment(sourceCcJudgment);
        
        //------------------ assertions -----------------------
        
        assertNotNull(retCcJudgment);
        assertTrue(retCcJudgment == relatedJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
        assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
        
        verify(ccJudgmentRepository).findBySourceCodeAndCaseNumber(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getCaseNumbers().get(0)));
        
        
        ArgumentCaptor<CommonCourtJudgment> argRelatedJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        ArgumentCaptor<CommonCourtJudgment> argReasoningJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        verify(ccjReasoningMerger).mergeReasoning(argRelatedJudgment.capture(), argReasoningJudgment.capture());
        assertTrue(relatedJudgment == argRelatedJudgment.getValue());
        assertTrue(ccJudgment == argReasoningJudgment.getValue());
        
        
        
        verifyZeroInteractions(judgmentOverwriter);
    }
    
    
}
