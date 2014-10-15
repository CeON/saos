package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process.ScjImportProcessReader;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;


/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportProcessReaderTest {

    private ScjImportProcessReader scjImportProcessReader = new ScjImportProcessReader();
    
    private RawSourceScJudgmentRepository rawJudgmentRepository = Mockito.mock(RawSourceScJudgmentRepository.class);
    
    
    @Before
    public void before() {
        scjImportProcessReader.setSimpleRawSourceScJudgmentRepository(rawJudgmentRepository);
    }
    
    
    
    @Test
    public void open() {
        
        // given
        
        List<Integer> rJudgmentIds = Lists.newArrayList(12, 123, 45);
        when(rawJudgmentRepository.findAllNotProcessedIds()).thenReturn(rJudgmentIds);
        
        
        // execute
        
        scjImportProcessReader.open(Mockito.mock(ExecutionContext.class));
        
        
        
        // assert
        
        List<Integer> internalRJudgmentIds = Whitebox.getInternalState(scjImportProcessReader, "rJudgmentIds");
        
        verify(rawJudgmentRepository).findAllNotProcessedIds();
        verifyNoMoreInteractions(rawJudgmentRepository);
        
        assertEquals(rJudgmentIds, internalRJudgmentIds);
        
    }
    
    
    
    @Test
    public void read() throws Exception {
        
        // given
        
        List<Integer> rJudgmentIds = Lists.newArrayList(12, 123, 45);
        Whitebox.setInternalState(scjImportProcessReader, "rJudgmentIds", new LinkedList<Integer>(rJudgmentIds));
        
        RawSourceScJudgment rJudgment0 = createSimpleRawSourceScJudgment(rJudgmentIds.get(0));
        RawSourceScJudgment rJudgment1 = createSimpleRawSourceScJudgment(rJudgmentIds.get(1));
        RawSourceScJudgment rJudgment2 = createSimpleRawSourceScJudgment(rJudgmentIds.get(2));;

        when(rawJudgmentRepository.getOne(Mockito.eq(rJudgmentIds.get(0)))).thenReturn(rJudgment0);
        when(rawJudgmentRepository.getOne(Mockito.eq(rJudgmentIds.get(1)))).thenReturn(rJudgment1);
        when(rawJudgmentRepository.getOne(Mockito.eq(rJudgmentIds.get(2)))).thenReturn(rJudgment2);
        
     
        
        // execute & assert
        
        readAndAssert(rJudgment0);
        readAndAssert(rJudgment1);
        readAndAssert(rJudgment2);
        readAndAssert(null);
        
        
    }


     
    
    
    
    //------------------------ PRIVATE --------------------------

    
    private void readAndAssert(RawSourceScJudgment expectedRawJudgment) throws Exception {
        
        // execute (1)
        RawSourceScJudgment readRJudgment = scjImportProcessReader.read();
        
        // assert
        if (expectedRawJudgment == null) {
            assertNull(readRJudgment);
        } else {
            assertEquals(expectedRawJudgment.getId(), readRJudgment.getId());
        }
    }
    
    private RawSourceScJudgment createSimpleRawSourceScJudgment(int rJudgmentId) {
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent(""+rJudgmentId);
        Whitebox.setInternalState(rJudgment, "id", rJudgmentId);
        return rJudgment;
        
    }
    
}
