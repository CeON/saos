package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.importer.common.JudgmentWithCorrectionList;
import pl.edu.icm.saos.importer.common.converter.JudgmentConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.common.overwriter.JudgmentOverwriter;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.CcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
public class CcjProcessingServiceTest {

    private CcjProcessingService ccjProcessingService = new CcjProcessingService();
    
    @Mock
    private CcJudgmentRepository ccJudgmentRepository;
    
    @Mock
    private JudgmentOverwriter<CommonCourtJudgment> judgmentOverwriter;
    
    @Mock
    private JudgmentConverter<CommonCourtJudgment, SourceCcJudgment> sourceCcJudgmentConverter;
    
    @Mock
    private EnrichmentTagRepository enrichmentTagRepository;
    
    
    
    private SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
    
    private CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private JudgmentWithCorrectionList<CommonCourtJudgment> jWithCorrectionList = new JudgmentWithCorrectionList<>(ccJudgment, correctionList);
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        ccjProcessingService.setCcJudgmentRepository(ccJudgmentRepository);
        ccjProcessingService.setJudgmentOverwriter(judgmentOverwriter);
        ccjProcessingService.setSourceCcJudgmentConverter(sourceCcJudgmentConverter);
        ccjProcessingService.setEnrichmentTagRepository(enrichmentTagRepository);
        
    }
    
    
    @Test
    public void processJudgment_New() {
        
        //------------------ data preparation -----------------------

        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(jWithCorrectionList);
        when(ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()))).thenReturn(null);
        

        //------------------ method invocation -----------------------

        JudgmentWithCorrectionList<CommonCourtJudgment> retJWithCorrectionList = ccjProcessingService.processJudgment(sourceCcJudgment);
      
        
        //------------------ assertions -----------------------
        
        assertTrue(jWithCorrectionList == retJWithCorrectionList);
        
        ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
        assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
        
        verify(ccJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()));
        
    }
    
    
    @Test
    public void processJudgment_Existing() {
        
        //------------------ data preparation -----------------------

        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        CommonCourtJudgment existingCcJudgment = new CommonCourtJudgment();
        Whitebox.setInternalState(existingCcJudgment, "id", 5L);
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId("1232345");
        
        
        when(sourceCcJudgmentConverter.convertJudgment(Mockito.isA(SourceCcJudgment.class))).thenReturn(jWithCorrectionList);
        when(ccJudgmentRepository.findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()))).thenReturn(existingCcJudgment);
        
        
        //------------------ method invocation -----------------------

        JudgmentWithCorrectionList<CommonCourtJudgment> retJWithCorrectionList = ccjProcessingService.processJudgment(sourceCcJudgment);
        
        
        //------------------ assertions -----------------------
        
        assertTrue(retJWithCorrectionList == jWithCorrectionList);
        assertTrue(retJWithCorrectionList.getJudgment() == existingCcJudgment);
        
        ArgumentCaptor<SourceCcJudgment> argSourceCcJudgment = ArgumentCaptor.forClass(SourceCcJudgment.class);
        verify(sourceCcJudgmentConverter).convertJudgment(argSourceCcJudgment.capture());
        assertTrue(sourceCcJudgment == argSourceCcJudgment.getValue());
        
        verify(ccJudgmentRepository).findOneBySourceCodeAndSourceJudgmentId(Mockito.eq(ccJudgment.getSourceInfo().getSourceCode()), Mockito.eq(ccJudgment.getSourceInfo().getSourceJudgmentId()));
        
        ArgumentCaptor<CommonCourtJudgment> argOldJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        ArgumentCaptor<CommonCourtJudgment> argNewJudgment = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        ArgumentCaptor<ImportCorrectionList> argCorrectionList = ArgumentCaptor.forClass(ImportCorrectionList.class);
        
        verify(judgmentOverwriter).overwriteJudgment(argOldJudgment.capture(), argNewJudgment.capture(), argCorrectionList.capture());
        assertTrue(existingCcJudgment == argOldJudgment.getValue());
        assertTrue(ccJudgment == argNewJudgment.getValue());
        assertTrue(correctionList == argCorrectionList.getValue());
        
        verify(enrichmentTagRepository).deleteAllByJudgmentId(5L);
    }
    
    
    
    
   
    
}
