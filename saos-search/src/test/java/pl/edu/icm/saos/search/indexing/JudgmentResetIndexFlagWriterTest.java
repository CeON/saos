package pl.edu.icm.saos.search.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentResetIndexFlagWriterTest {

    private JudgmentResetIndexFlagWriter judgmentResetIndexFlagReader = new JudgmentResetIndexFlagWriter();

    @Mock
    private JudgmentRepository judgmentRepository;
    
    @Captor
    private ArgumentCaptor<Iterable<Judgment>> saveJudgmentCaptor;
    
    
    @Before
    public void setUp() {
        judgmentResetIndexFlagReader.setJudgmentRepository(judgmentRepository);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        Judgment firstJudgment = new CommonCourtJudgment();
        Judgment secondJudgment = new CommonCourtJudgment();
        
        judgmentResetIndexFlagReader.write(Lists.newArrayList(firstJudgment, secondJudgment));
        
        verify(judgmentRepository, times(1)).save(saveJudgmentCaptor.capture());
        assertThat(saveJudgmentCaptor.getValue(), containsInAnyOrder(firstJudgment, secondJudgment));
    }
}
