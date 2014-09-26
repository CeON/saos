package pl.edu.icm.saos.search.indexing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
public class JudgmentIndexingProcessorTest {

    private JudgmentIndexingProcessor judgmentIndexingProcessor = new JudgmentIndexingProcessor();
    
    private CcJudgmentIndexFieldsFiller ccJudgmentIndexFieldsFiller = mock(CcJudgmentIndexFieldsFiller.class);
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    @Before
    public void setUp() {
        judgmentIndexingProcessor.setCcJudgmentRepository(judgmentRepository);
        judgmentIndexingProcessor.setCcJudgmentIndexingProcessor(ccJudgmentIndexFieldsFiller);
        judgmentIndexingProcessor.init();
    }
    
    @Test
    public void process() throws Exception {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 6);
        
        
        judgmentIndexingProcessor.process(judgment);
        
        ArgumentCaptor<CommonCourtJudgment> argCaptureForFill = ArgumentCaptor.forClass(CommonCourtJudgment.class);
        verify(ccJudgmentIndexFieldsFiller, times(1)).fillFields(any(SolrInputDocument.class), argCaptureForFill.capture());
        assertEquals(6, argCaptureForFill.getValue().getId());
        
        ArgumentCaptor<Judgment> argCapture = ArgumentCaptor.forClass(Judgment.class);
        verify(judgmentRepository, times(1)).save(argCapture.capture());
        assertEquals(6, argCapture.getValue().getId());
        assertTrue(argCapture.getValue().isIndexed());
    }
    
}
