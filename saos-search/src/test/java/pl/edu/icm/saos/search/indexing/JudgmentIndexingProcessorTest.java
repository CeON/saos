package pl.edu.icm.saos.search.indexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

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
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertSaveJudgmentInRepository(int judgmentId) {
        ArgumentCaptor<Judgment> argCapture = ArgumentCaptor.forClass(Judgment.class);
        verify(judgmentRepository, times(1)).save(argCapture.capture());
        assertEquals(judgmentId, argCapture.getValue().getId());
        assertTrue(argCapture.getValue().isIndexed());
    }
}
