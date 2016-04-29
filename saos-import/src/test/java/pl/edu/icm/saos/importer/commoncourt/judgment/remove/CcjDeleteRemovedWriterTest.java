package pl.edu.icm.saos.importer.commoncourt.judgment.remove;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.enrichment.delete.JudgmentWithEnrichmentDeleter;
import pl.edu.icm.saos.persistence.model.DeletedJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.DeletedJudgmentRepository;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class CcjDeleteRemovedWriterTest {

    @InjectMocks
    private CcjDeleteRemovedWriter ccjDeleteRemovedWriter = new CcjDeleteRemovedWriter();
    
    
    @Mock
    private JudgmentWithEnrichmentDeleter judgmentWithEnrichmentDeleter;
    
    @Mock
    private DeletedJudgmentRepository deletedJudgmentRepository;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void write() throws Exception {
        
        // given
        
        DeletedJudgment judgmentToRemove1 = createDeletedJudgment(1L, "AA1");
        DeletedJudgment judgmentToRemove2 = createDeletedJudgment(2L, "AA2");
        
        
        // execute
        
        ccjDeleteRemovedWriter.write(Lists.newArrayList(judgmentToRemove1, judgmentToRemove2));
        
        
        // assert
        
        verify(judgmentWithEnrichmentDeleter).delete(Lists.newArrayList(1L, 2L));
        verify(deletedJudgmentRepository).save(Lists.newArrayList(judgmentToRemove1, judgmentToRemove2));
        
        verifyNoMoreInteractions(judgmentWithEnrichmentDeleter, deletedJudgmentRepository);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private DeletedJudgment createDeletedJudgment(long judgmentId, String sourceJudgmentId) {
        DeletedJudgment deletedJudgment = new DeletedJudgment();
        
        deletedJudgment.setJudgmentId(judgmentId);
        deletedJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        deletedJudgment.getSourceInfo().setSourceJudgmentId(sourceJudgmentId);
        
        return deletedJudgment;
    }
}
