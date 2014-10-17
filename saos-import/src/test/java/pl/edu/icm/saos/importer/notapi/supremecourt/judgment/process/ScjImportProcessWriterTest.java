package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportProcessWriterTest {

    private ScjImportProcessWriter scjImportProcessWriter = new ScjImportProcessWriter();
    
    private JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    
    @Before
    public void before() {
        scjImportProcessWriter.setJudgmentRepository(judgmentRepository);
    }
    
    
    @Test
    public void write() {
        
        List<? extends SupremeCourtJudgment> judgments = Lists.newArrayList(new SupremeCourtJudgment(), new SupremeCourtJudgment());
        
        scjImportProcessWriter.write(judgments);
        
        verify(judgmentRepository).save(judgments);
        
    }
    
}
