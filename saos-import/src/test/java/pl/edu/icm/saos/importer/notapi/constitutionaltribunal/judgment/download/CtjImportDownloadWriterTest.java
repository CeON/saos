package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCtJudgmentRepository;

/**
 * @author madryk
 */
public class CtjImportDownloadWriterTest {

    private CtjImportDownloadWriter ctjImportDownloadWriter = new CtjImportDownloadWriter();
    
    private RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository = mock(RawSourceCtJudgmentRepository.class);
    
    @Before
    public void before() {
        ctjImportDownloadWriter.setRawSourceCtJudgmentRepository(rawSourceCtJudgmentRepository);
    }
    
    @Test
    public void write() throws Exception {
        
        RawSourceCtJudgment firstRawJudgment = new RawSourceCtJudgment();
        firstRawJudgment.setJsonContent("jsonContent");
        RawSourceCtJudgment secondRawJudgment = new RawSourceCtJudgment();
        secondRawJudgment.setJsonContent("jsonContent2");
        
        List<RawSourceCtJudgment> rJudgments = Lists.newArrayList(firstRawJudgment, secondRawJudgment);
        
        
        ctjImportDownloadWriter.write(rJudgments);
        
        
        verify(rawSourceCtJudgmentRepository).save(eq(rJudgments));
    }
    
}
