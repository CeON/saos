package pl.edu.icm.saos.importer.common.overwriter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.Judgment;


/**
 * @author Łukasz Dumiszewski
 */

public class DelegatingJudgmentOverwriterTest {

    
    
    private DelegatingJudgmentOverwriter<Judgment> delegatingJudgmentOverwriter = new DelegatingJudgmentOverwriter<>();
    
    @Mock
    private JudgmentOverwriter<Judgment> commonJudgmentOverwriter;
    
    @Mock
    private JudgmentOverwriter<Judgment> specificJudgmentOverwriter;

    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    
    @Before
    public void before() {
        initMocks(this);
        
        delegatingJudgmentOverwriter.setCommonJudgmentOverwriter(commonJudgmentOverwriter);
        delegatingJudgmentOverwriter.setSpecificJudgmentOverwriter(specificJudgmentOverwriter);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void overwriteJudgment_NULL() {
        
        Judgment oldJudgment = null;
        Judgment newJudgment = mock(Judgment.class);
        
        delegatingJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        verifyZeroInteractions(commonJudgmentOverwriter, specificJudgmentOverwriter);
        
    }
    
    
    
    @Test
    public void overwriteJudgment() {
        
        Judgment oldJudgment = mock(Judgment.class);
        Judgment newJudgment = mock(Judgment.class);
        
        delegatingJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        verify(commonJudgmentOverwriter).overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        verify(specificJudgmentOverwriter).overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
    }
    
}
