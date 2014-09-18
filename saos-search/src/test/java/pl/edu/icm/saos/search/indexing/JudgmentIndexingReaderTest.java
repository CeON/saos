package pl.edu.icm.saos.search.indexing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class JudgmentIndexingReaderTest {

    private JudgmentIndexingReader judgmentIndexingReader = new JudgmentIndexingReader();
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    @Before
    public void setUp() {
         judgmentIndexingReader.setJudgmentRepository(judgmentRepository);
    }
    
    @Test
    public void read_NOT_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        Page<Judgment> page = new PageImpl<Judgment>(Lists.newArrayList());
        when(judgmentRepository.findAllToIndex(any(Pageable.class))).thenReturn(page);
        
        Judgment judgment = judgmentIndexingReader.read();
        
        assertNull(judgment);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void read_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        Judgment firstJudgment = createCcJudgment(1);
        Judgment secondJudgment = createCcJudgment(2);
        Judgment thirdJudgment = createCcJudgment(3);
        
        Page<Judgment> firstPage = new PageImpl<Judgment>(Lists.newArrayList(firstJudgment, secondJudgment), new PageRequest(0, 2), 3);
        Page<Judgment> secondPage = new PageImpl<Judgment>(Lists.newArrayList(thirdJudgment), new PageRequest(1, 1), 3);
        Page<Judgment> thirdPage = new PageImpl<Judgment>(Lists.newArrayList());
        
        when(judgmentRepository.findAllToIndex(any(Pageable.class))).thenReturn(firstPage, secondPage, thirdPage);

        
        Judgment actualFirst = judgmentIndexingReader.read();
        Judgment actualSecond = judgmentIndexingReader.read();        
        Judgment actualThird = judgmentIndexingReader.read();
        Judgment actualFourth = judgmentIndexingReader.read();
        
        
        ArgumentCaptor<Pageable> pageArgCapture = ArgumentCaptor.forClass(Pageable.class);
        verify(judgmentRepository, atLeast(2)).findAllToIndex(pageArgCapture.capture());
        
        Pageable firstPageRequestArg = pageArgCapture.getAllValues().get(0);
        assertEquals(0, firstPageRequestArg.getPageNumber());
        
        Pageable secondPageRequestArg = pageArgCapture.getAllValues().get(1);
        assertEquals(1, secondPageRequestArg.getPageNumber());
        
        assertEquals(firstJudgment.getId(), actualFirst.getId());
        assertEquals(secondJudgment.getId(), actualSecond.getId());
        assertEquals(thirdJudgment.getId(), actualThird.getId());
        assertNull(actualFourth);
    }
    
    
    private CommonCourtJudgment createCcJudgment(int id) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(ccJudgment, "id", id);
        
        return ccJudgment;
    }
}
