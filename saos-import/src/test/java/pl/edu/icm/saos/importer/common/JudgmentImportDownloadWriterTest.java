package pl.edu.icm.saos.importer.common;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.common.JudgmentImportDownloadWriter;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentImportDownloadWriterTest {

    
    private JudgmentImportDownloadWriter judgmentImportDownloadWriter = new JudgmentImportDownloadWriter();
    
    private RawSourceJudgmentRepository rawSourceJudgmentRepository = mock(RawSourceJudgmentRepository.class);
    
    
    @Before
    public void before() {
        judgmentImportDownloadWriter.setRawSourceJudgmentRepository(rawSourceJudgmentRepository);
        judgmentImportDownloadWriter.setEntityManager(mock(EntityManager.class));
    }
    
    
    @Test
    public void write() throws Exception {
        List<RawSourceCcJudgment> rJudgments = Lists.newArrayList(new RawSourceCcJudgment());
        judgmentImportDownloadWriter.write(rJudgments);
        verify(rawSourceJudgmentRepository).save(Mockito.eq(rJudgments));
    }
    
    
}
