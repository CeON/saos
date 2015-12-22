package pl.edu.icm.saos.search.indexing;

import static org.junit.Assert.assertEquals;
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

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
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
        // given
        SupremeCourtJudgment judgment = new SupremeCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 5);
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(judgment);
        
        // execute
        judgmentIndexingProcessor.process(indexingData);
        
        // assert
        ArgumentCaptor<JudgmentIndexingData> argCaptureForFill = ArgumentCaptor.forClass(JudgmentIndexingData.class);
        verify(scJudgmentIndexFieldsFiller, times(1)).fillFields(any(SolrInputDocument.class), argCaptureForFill.capture());
        assertEquals(5, argCaptureForFill.getValue().getJudgment().getId());
        
        verify(judgmentRepository).markAsIndexed(5L);
    }
    
    @Test
    public void process_COMMON_COURT_JUDGMENT() throws Exception {
        // given
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 6);
        JudgmentIndexingData indexingData = new JudgmentIndexingData();
        indexingData.setJudgment(judgment);
        
        // execute
        judgmentIndexingProcessor.process(indexingData);
        
        // assert
        ArgumentCaptor<JudgmentIndexingData> argCaptureForFill = ArgumentCaptor.forClass(JudgmentIndexingData.class);
        verify(ccJudgmentIndexFieldsFiller, times(1)).fillFields(any(SolrInputDocument.class), argCaptureForFill.capture());
        assertEquals(6, argCaptureForFill.getValue().getJudgment().getId());
        
        verify(judgmentRepository).markAsIndexed(6L);
    }
    
}
