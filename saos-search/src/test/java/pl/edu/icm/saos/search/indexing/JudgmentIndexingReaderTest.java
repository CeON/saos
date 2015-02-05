package pl.edu.icm.saos.search.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
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
    
    private JudgmentEnrichmentService judgmentEnrichmentService = mock(JudgmentEnrichmentService.class);
    
    
    @Before
    public void setUp() {
         judgmentIndexingReader.setJudgmentRepository(judgmentRepository);
         judgmentIndexingReader.setJudgmentEnrichmentService(judgmentEnrichmentService);
    }
    
    @Test
    public void read_NOT_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        when(judgmentRepository.findAllNotIndexedIds()).thenReturn(Lists.newLinkedList());
        judgmentIndexingReader.open(new ExecutionContext());
        
        Judgment judgment = judgmentIndexingReader.read();
        
        assertNull(judgment);
    }
    
    @Test
    public void read_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        Judgment firstJudgment = createCcJudgment(1);
        Judgment secondJudgment = createCcJudgment(2);
        Judgment thirdJudgment = createCcJudgment(3);
        
        when(judgmentRepository.findAllNotIndexedIds()).thenReturn(Lists.newArrayList(1l, 2l, 3l));
        when(judgmentEnrichmentService.findOneAndEnrich(1l)).thenReturn(firstJudgment);
        when(judgmentEnrichmentService.findOneAndEnrich(2l)).thenReturn(secondJudgment);
        when(judgmentEnrichmentService.findOneAndEnrich(3l)).thenReturn(thirdJudgment);
        judgmentIndexingReader.open(new ExecutionContext());

        
        Judgment actualFirst = judgmentIndexingReader.read();
        Judgment actualSecond = judgmentIndexingReader.read();
        Judgment actualThird = judgmentIndexingReader.read();
        Judgment actualFourth = judgmentIndexingReader.read();
        
        
        assertEquals(firstJudgment.getId(), actualFirst.getId());
        assertEquals(secondJudgment.getId(), actualSecond.getId());
        assertEquals(thirdJudgment.getId(), actualThird.getId());
        assertNull(actualFourth);
    }
    
    @Test
    public void read_THREAD_SAFE_CHECK() throws InterruptedException {
        final int threadsCount = 5;
        final int judgmentsCount = 300;
        
        List<Long> ids = IntStream
                .rangeClosed(1, judgmentsCount)
                .mapToObj(x -> Long.valueOf(x))
                .collect(Collectors.toList());
        
        when(judgmentRepository.findAllNotIndexedIds()).thenReturn(ids);
        when(judgmentEnrichmentService.findOneAndEnrich(Mockito.anyLong())).thenAnswer(new Answer<Judgment>( ) {
            @Override
            public Judgment answer(InvocationOnMock invocation) throws Throwable {
                long id = (long)(invocation.getArguments()[0]);
                return (id >=1 && id <=judgmentsCount) ? createCcJudgment(id) : null;
            }
        });
        judgmentIndexingReader.open(new ExecutionContext());
        
        
        final CountDownLatch latch = new CountDownLatch(threadsCount);
        final List<List<Long>> threadsResultsIds = Lists.newLinkedList();
        
        for (int i=0; i<threadsCount; ++i) {
            List<Long> threadResultsIds = runReaderThread(latch, i);
            threadsResultsIds.add(threadResultsIds);
        }
        
        latch.await(); // wait for threads to finish reading
        
        
        List<Long> actualIds = threadsResultsIds.stream().flatMap(List::stream).collect(Collectors.toList());
        assertThat(actualIds, containsInAnyOrder(ids.toArray()));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourtJudgment createCcJudgment(long id) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(ccJudgment, "id", id);
        
        return ccJudgment;
    }
    
    private List<Long> runReaderThread(final CountDownLatch latch, int threadNumber) {
        final List<Long> threadResultsIds = new LinkedList<Long>();
        Runnable runner = new Runnable() {
            public void run() {
                try {
                    Judgment judgment = null;
                    do {
                        judgment = judgmentIndexingReader.read();
                        if (judgment != null) {
                            threadResultsIds.add(judgment.getId());
                        }
                    } while (judgment != null);

                } catch (Exception e) { } finally { latch.countDown(); }
            }
        };
        new Thread(runner, "ReaderThread"+threadNumber).start();
        
        return threadResultsIds;
    }
}
