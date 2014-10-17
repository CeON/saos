package pl.edu.icm.saos.persistence.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

/**
 * @author Łukasz Dumiszewski
 */

@Category(SlowTest.class)
public class ScJudgmentFormRepositoryTest extends PersistenceTestSupport {

    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    private SupremeCourtJudgmentForm scJudgmentForm;
    
    
    @Before
    public void before() {
        
        scJudgmentForm = createScJudgmentForm("form 2 xxmccmxxm");
        createScJudgmentForm("form 3 xxmmxxm");
    }
    
    
    @Test
    public void findOneByName_FOUND() {
        
        SupremeCourtJudgmentForm dbScJudgmentForm = scJudgmentFormRepository.findOneByName(scJudgmentForm.getName());
        
        assertEquals(scJudgmentForm.getId(), dbScJudgmentForm.getId());
    }


    @Test
    public void findOneByFullName_NOT_FOUND() {
        
        SupremeCourtJudgmentForm dbScJudgmentForm = scJudgmentFormRepository.findOneByName(scJudgmentForm.getName()+"xxx");
        
        assertNull(dbScJudgmentForm);
    }


    //------------------------ PRIVATE --------------------------
    
    private SupremeCourtJudgmentForm createScJudgmentForm(String scJudgmentFormName) {
        SupremeCourtJudgmentForm scJudgmentForm = new SupremeCourtJudgmentForm();
        scJudgmentForm.setName(scJudgmentFormName);
        scJudgmentFormRepository.save(scJudgmentForm);
        return scJudgmentForm;
    }
}
