package pl.edu.icm.saos.search.indexing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentIndexingProcessorTest {

    private JudgmentIndexingProcessor judgmentIndexingProcessor = new JudgmentIndexingProcessor();
    
    @Mock
    private CcJudgmentIndexFieldsFiller ccJudgmentIndexFieldsFiller;
    
    @Mock
    private ScJudgmentIndexFieldsFiller scJudgmentIndexFieldsFiller;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    @Before
    public void setUp() {
        judgmentIndexingProcessor.setJudgmentRepository(judgmentRepository);
        doCallRealMethod().when(ccJudgmentIndexFieldsFiller).isApplicable(any());
        doCallRealMethod().when(scJudgmentIndexFieldsFiller).isApplicable(any());
        judgmentIndexingProcessor.setJudgmentIndexFieldsFillers(Lists.newArrayList(ccJudgmentIndexFieldsFiller, scJudgmentIndexFieldsFiller));
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void process_SUPREME_COURT_JUDGMENT() throws Exception {
        SupremeCourtJudgment judgment = new SupremeCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 5);
        
        
        judgmentIndexingProcessor.process(judgment);
        
        ArgumentCaptor<Judgment> argCaptureForFill = ArgumentCaptor.forClass(Judgment.class);
        verify(scJudgmentIndexFieldsFiller, times(1)).fillFields(any(SolrInputDocument.class), argCaptureForFill.capture());
        assertEquals(5, argCaptureForFill.getValue().getId());
        
        assertSaveJudgmentInRepository(5);
    }
    
    @Test
    public void process_COMMON_COURT_JUDGMENT() throws Exception {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 6);
        
        
        judgmentIndexingProcessor.process(judgment);
        
        ArgumentCaptor<CommonCourtJudgment> argCaptureForFill = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        verify(ccJudgmentIndexFieldsFiller, times(1)).fillFields(any(SolrInputDocument.class), argCaptureForFill.capture());
        assertEquals(6, argCaptureForFill.getValue().getId());
        
        assertSaveJudgmentInRepository(6);
    }
    
    @Test(expected=RuntimeException.class)
    public void process_UNKNOWN_JUDGMENT() throws Exception {
        UnknownJudgment judgment = new UnknownJudgment();
        
        judgmentIndexingProcessor.process(judgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertSaveJudgmentInRepository(int judgmentId) {
        ArgumentCaptor<Judgment> argCapture = ArgumentCaptor.forClass(Judgment.class);
        verify(judgmentRepository, times(1)).save(argCapture.capture());
        assertEquals(judgmentId, argCapture.getValue().getId());
        assertTrue(argCapture.getValue().isIndexed());
    }
    
    private class UnknownJudgment extends Judgment {
        
    }
}
