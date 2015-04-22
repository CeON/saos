package pl.edu.icm.saos.search.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class JudgmentIndexingReaderTest {

    private JudgmentIndexingReader judgmentIndexingReader = new JudgmentIndexingReader();
    
    private JudgmentEnrichmentService judgmentEnrichmentService = mock(JudgmentEnrichmentService.class);
    
    private JudgmentIndexingItemFetcher judgmentIndexingItemFetcher = mock(JudgmentIndexingItemFetcher.class);
    
    
    @Before
    public void setUp() {
         judgmentIndexingReader.setJudgmentEnrichmentService(judgmentEnrichmentService);
         judgmentIndexingReader.setJudgmentIndexingItemFetcher(judgmentIndexingItemFetcher);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void read_NOT_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        when(judgmentIndexingItemFetcher.fetchJudgmentIndexingItems()).thenReturn(Lists.newLinkedList());
        judgmentIndexingReader.open(new ExecutionContext());
        
        JudgmentIndexingData judgmentIndexingData = judgmentIndexingReader.read();
        
        assertNull(judgmentIndexingData);
    }
    
    @Test
    public void read_FOUND() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception {
        Judgment firstJudgment = createCcJudgment(1);
        Judgment secondJudgment = createCcJudgment(2);
        Judgment thirdJudgment = createCcJudgment(3);
        
        JudgmentIndexingItem firstJudgmentIndexingItem = new JudgmentIndexingItem(1L, 31L);
        JudgmentIndexingItem secondJudgmentIndexingItem = new JudgmentIndexingItem(2L, 0L);
        JudgmentIndexingItem thirdJudgmentIndexingItem = new JudgmentIndexingItem(3L, 53L);
        
        when(judgmentIndexingItemFetcher.fetchJudgmentIndexingItems())
            .thenReturn(Lists.newArrayList(firstJudgmentIndexingItem, secondJudgmentIndexingItem, thirdJudgmentIndexingItem));
        when(judgmentEnrichmentService.findOneAndEnrich(1l)).thenReturn(firstJudgment);
        when(judgmentEnrichmentService.findOneAndEnrich(2l)).thenReturn(secondJudgment);
        when(judgmentEnrichmentService.findOneAndEnrich(3l)).thenReturn(thirdJudgment);
        judgmentIndexingReader.open(new ExecutionContext());

        
        JudgmentIndexingData actualFirst = judgmentIndexingReader.read();
        JudgmentIndexingData actualSecond = judgmentIndexingReader.read();
        JudgmentIndexingData actualThird = judgmentIndexingReader.read();
        JudgmentIndexingData actualFourth = judgmentIndexingReader.read();
        
        
        assertNotNull(actualFirst.getJudgment());
        assertEquals(firstJudgment.getId(), actualFirst.getJudgment().getId());
        assertEquals(31L, actualFirst.getReferencingCount());
        
        assertNotNull(actualSecond.getJudgment());
        assertEquals(secondJudgment.getId(), actualSecond.getJudgment().getId());
        assertEquals(0L, actualSecond.getReferencingCount());
        
        assertNotNull(actualThird.getJudgment());
        assertEquals(thirdJudgment.getId(), actualThird.getJudgment().getId());
        assertEquals(53L, actualThird.getReferencingCount());
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
        List<JudgmentIndexingItem> judgmentIndexingItems = IntStream
                .rangeClosed(1, judgmentsCount)
                .mapToObj(x -> new JudgmentIndexingItem(Long.valueOf(x), 0L))
                .collect(Collectors.toList());
        
        
        when(judgmentIndexingItemFetcher.fetchJudgmentIndexingItems()).thenReturn(judgmentIndexingItems);
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
                    JudgmentIndexingData judgmentIndexingData = null;
                    do {
                        judgmentIndexingData = judgmentIndexingReader.read();
                        if (judgmentIndexingData != null) {
                            threadResultsIds.add(judgmentIndexingData.getJudgment().getId());
                        }
                    } while (judgmentIndexingData != null);

                } catch (Exception e) { } finally { latch.countDown(); }
            }
        };
        new Thread(runner, "ReaderThread"+threadNumber).start();
        
        return threadResultsIds;
    }
}
