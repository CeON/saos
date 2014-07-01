package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportProcessReaderTest {

    private CcjImportProcessReader ccjImportProcessReader = new CcjImportProcessReader();
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository = mock(RawSourceCcJudgmentRepository.class);
    
    
    @Before
    public void before() {
        ccjImportProcessReader.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
    }
    
    
    @Test
    public void read_NO_ITEMS_FOUND() throws UnexpectedInputException, NonTransientResourceException, ParseException, Exception {
        @SuppressWarnings("unchecked")
        Page<RawSourceCcJudgment> page = Mockito.mock(Page.class);
        when(page.getContent()).thenReturn(new ArrayList<RawSourceCcJudgment>());
        when(rawSourceCcJudgmentRepository.findNotProcessed(Mockito.any(Pageable.class))).thenReturn(page);
        
        RawSourceCcJudgment rJudgment = ccjImportProcessReader.read();
        
        assertNull(rJudgment);
    }

    
    @Test
    public void read_ITEMS_FOUND() throws UnexpectedInputException, NonTransientResourceException, ParseException, Exception {
        
        // prepare data & mocks
        @SuppressWarnings("unchecked")
        Page<RawSourceCcJudgment> page = Mockito.mock(Page.class);
        
        RawSourceCcJudgment rJudgment1 = new RawSourceCcJudgment();
        rJudgment1.setSourceId("1212121212");
        rJudgment1.setDataMd5("23232323232323232323");
        RawSourceCcJudgment rJudgment2 = new RawSourceCcJudgment();
        rJudgment2.setSourceId("sdsdsd2121212");
        rJudgment2.setDataMd5("232323sdsdsdsdsdsd23232323");
        
        List<RawSourceCcJudgment> rJudgments = Lists.newArrayList(rJudgment1, rJudgment2);
        
        when(page.getContent()).thenReturn(rJudgments);
        when(rawSourceCcJudgmentRepository.findNotProcessed(Mockito.any(Pageable.class))).thenReturn(page);
        
        
        // 1.read
        
        RawSourceCcJudgment rJudgmentRead = ccjImportProcessReader.read();
        
        assertNotNull(rJudgmentRead);
        assertEquals(rJudgment1, rJudgmentRead);
        ArgumentCaptor<Pageable> arg = ArgumentCaptor.forClass(Pageable.class);
        verify(rawSourceCcJudgmentRepository).findNotProcessed(arg.capture());
        assertEquals(0, arg.getValue().getPageNumber());
        
        // 2.read
        Mockito.reset(rawSourceCcJudgmentRepository);
        
        rJudgmentRead = ccjImportProcessReader.read();
        
        assertNotNull(rJudgmentRead);
        assertEquals(rJudgment2, rJudgmentRead);
        verify(rawSourceCcJudgmentRepository, never()).findNotProcessed(Mockito.any(Pageable.class));
        
        
        // 3.read
        Mockito.reset(rawSourceCcJudgmentRepository);
        when(page.getContent()).thenReturn(new ArrayList<RawSourceCcJudgment>());
        when(rawSourceCcJudgmentRepository.findNotProcessed(Mockito.any(Pageable.class))).thenReturn(page);
        
        
        rJudgmentRead = ccjImportProcessReader.read();
        
        verify(rawSourceCcJudgmentRepository).findNotProcessed(Mockito.any(Pageable.class));
        
    }

}
