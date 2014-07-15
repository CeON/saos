package pl.edu.icm.saos.importer.commoncourt.download;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportDownloadWriterTest {

    
    private CcjImportDownloadWriter ccjImportDownloadWriter = new CcjImportDownloadWriter();
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository = mock(RawSourceCcJudgmentRepository.class);
    
    
    @Before
    public void before() {
        ccjImportDownloadWriter.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        ccjImportDownloadWriter.setEntityManager(mock(EntityManager.class));
    }
    
    
    @Test
    public void write() throws Exception {
        List<RawSourceCcJudgment> rJudgments = Lists.newArrayList(new RawSourceCcJudgment());
        ccjImportDownloadWriter.write(rJudgments);
        verify(rawSourceCcJudgmentRepository).save(Mockito.eq(rJudgments));
    }
    
    
}
