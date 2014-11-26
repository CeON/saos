package pl.edu.icm.saos.search.indexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class JudgmentResetIndexFlagReaderTest {

    private JudgmentResetIndexFlagReader judgmentResetIndexFlagReader = new JudgmentResetIndexFlagReader();
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    @Before
    public void setUp() {
        CommonCourtJudgment ccIndexedJudgment = createCcJudgment(1);
        SupremeCourtJudgment scIndexedJudgment = createScJudgment(3);
        
        when(judgmentRepository.findAllIndexedIds()).thenReturn(Lists.newArrayList(ccIndexedJudgment.getId(), scIndexedJudgment.getId()));
        when(judgmentRepository.findIndexedIdsBySourceCode(SourceCode.COMMON_COURT)).thenReturn(Lists.newArrayList(ccIndexedJudgment.getId()));
        when(judgmentRepository.findIndexedIdsBySourceCode(SourceCode.SUPREME_COURT)).thenReturn(Lists.newArrayList(scIndexedJudgment.getId()));
        
        when(judgmentRepository.findOne(ccIndexedJudgment.getId())).thenReturn(ccIndexedJudgment);
        when(judgmentRepository.findOne(scIndexedJudgment.getId())).thenReturn(scIndexedJudgment);
        
        
        judgmentResetIndexFlagReader.setJudgmentRepository(judgmentRepository);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read_ALL_INDEXED() throws Exception {
        judgmentResetIndexFlagReader.open(new ExecutionContext());
        
        Judgment firstJudgment = judgmentResetIndexFlagReader.read();
        Judgment secondJudgment = judgmentResetIndexFlagReader.read();
        Judgment thirdJudgment = judgmentResetIndexFlagReader.read();
        
        
        assertNotNull(firstJudgment);
        assertEquals(CourtType.COMMON, firstJudgment.getCourtType());
        assertEquals(1, firstJudgment.getId());
        
        assertNotNull(secondJudgment);
        assertEquals(CourtType.SUPREME, secondJudgment.getCourtType());
        assertEquals(3, secondJudgment.getId());
        
        assertNull(thirdJudgment);
    }
    
    @Test
    public void read_ALL_INDEXED_COMMON_COURT() throws Exception {
        judgmentResetIndexFlagReader.setSourceCode(SourceCode.COMMON_COURT.name());
        judgmentResetIndexFlagReader.open(new ExecutionContext());
        
        Judgment firstJudgment = judgmentResetIndexFlagReader.read();
        Judgment secondJudgment = judgmentResetIndexFlagReader.read();
        
        
        assertNotNull(firstJudgment);
        assertEquals(CourtType.COMMON, firstJudgment.getCourtType());
        assertEquals(1, firstJudgment.getId());
        
        assertNull(secondJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(int id) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(ccJudgment, "id", id);
        
        return ccJudgment;
    }
    
    private SupremeCourtJudgment createScJudgment(int id) {
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();
        ReflectionTestUtils.setField(scJudgment, "id", id);
        
        return scJudgment;
    }

}
