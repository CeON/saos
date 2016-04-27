package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.importer.commoncourt.judgment.download.SourceCcjExternalRepository;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceCcRemovedJudgmentsFinderTest {

    @InjectMocks
    private SourceCcRemovedJudgmentsFinder removedJudgmentsFinder = new SourceCcRemovedJudgmentsFinder();
    
    @Mock
    private SourceCcjExternalRepository sourceCcjExternalRepository;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    
    @Before
    public void setup() {
        removedJudgmentsFinder.setPageSize(3);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void findRemovedJudgments() {
        
        // given
        
        List<String> sourceIdsPage1 = Lists.newArrayList("sourceId_1", "sourceId_2", "sourceId_3");
        List<String> sourceIdsPage2 = Lists.newArrayList("sourceId_4", "sourceId_5");
        
        List<String> sourceIdsInDb = Lists.newArrayList("sourceId_1", "removed_sourceId_1", "sourceId_3", "sourceId_4", "removed_sourceId_2");
        
        when(sourceCcjExternalRepository.findJudgmentIds(0, 3, null)).thenReturn(sourceIdsPage1);
        when(sourceCcjExternalRepository.findJudgmentIds(1, 3, null)).thenReturn(sourceIdsPage2);
        when(sourceCcjExternalRepository.findJudgmentIds(2, 3, null)).thenReturn(Lists.newArrayList());
        
        when(judgmentRepository.findAllSourceIdsBySourceCode(SourceCode.COMMON_COURT)).thenReturn(sourceIdsInDb);
        
        // execute
        
        List<String> removedSourceIds = removedJudgmentsFinder.findRemovedJudgments();
        
        
        // assert
        
        assertThat(removedSourceIds, containsInAnyOrder("removed_sourceId_1", "removed_sourceId_2"));
    }
    
}
