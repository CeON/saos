package pl.edu.icm.saos.importer.simple.supremecourt.judgment.download;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.SimpleRawSourceScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportDownloadWriterTest {

    
    private ScjImportDownloadWriter scjImportDownloadWriter = new ScjImportDownloadWriter();
    
    private SimpleRawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository = Mockito.mock(SimpleRawSourceScJudgmentRepository.class);
    
    
    
    @Before
    public void before() {
        scjImportDownloadWriter.setSimpleRawSourceScJudgmentRepository(simpleRawSourceScJudgmentRepository);
    }
    
    
    
    @Test
    public void write() {
        
        // data
        
        SimpleRawSourceScJudgment rJudgment1 = new SimpleRawSourceScJudgment();
        rJudgment1.setJsonContent("12121212");
        SimpleRawSourceScJudgment rJudgment2 = new SimpleRawSourceScJudgment();
        rJudgment1.setJsonContent("121212ddsddss12");
        
        List<SimpleRawSourceScJudgment> rJudgments = Lists.newArrayList(rJudgment1, rJudgment2);
        
        
        // execute
        
        scjImportDownloadWriter.write(rJudgments);
        
        
        // assert
        
        Mockito.verify(simpleRawSourceScJudgmentRepository).save(Mockito.eq(rJudgments));
    }
    
}
