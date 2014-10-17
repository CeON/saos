package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download.ScjImportDownloadWriter;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportDownloadWriterTest {

    
    private ScjImportDownloadWriter scjImportDownloadWriter = new ScjImportDownloadWriter();
    
    private RawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository = Mockito.mock(RawSourceScJudgmentRepository.class);
    
    
    
    @Before
    public void before() {
        scjImportDownloadWriter.setSimpleRawSourceScJudgmentRepository(simpleRawSourceScJudgmentRepository);
    }
    
    
    
    @Test
    public void write() {
        
        // data
        
        RawSourceScJudgment rJudgment1 = new RawSourceScJudgment();
        rJudgment1.setJsonContent("12121212");
        RawSourceScJudgment rJudgment2 = new RawSourceScJudgment();
        rJudgment1.setJsonContent("121212ddsddss12");
        
        List<RawSourceScJudgment> rJudgments = Lists.newArrayList(rJudgment1, rJudgment2);
        
        
        // execute
        
        scjImportDownloadWriter.write(rJudgments);
        
        
        // assert
        
        Mockito.verify(simpleRawSourceScJudgmentRepository).save(Mockito.eq(rJudgments));
    }
    
}
