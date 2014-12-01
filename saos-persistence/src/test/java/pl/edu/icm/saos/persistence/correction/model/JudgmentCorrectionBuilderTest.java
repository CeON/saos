package pl.edu.icm.saos.persistence.correction.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentCorrectionBuilderTest {

    
    private Judgment judgment = new CommonCourtJudgment();
    
    Judge judge = new Judge("Anna Maria");
    
    {
        Whitebox.setInternalState(judge, "id", 5);
    }
    
    
    
    
    //------------------------ TESTS --------------------------
    
   
    
    @Test(expected=NullPointerException.class)
    public void createFor_NoJudgment() {
        
        createFor(null);
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_NoChangeOperationNoChangedObject() {
     
        createFor(judgment).property(NAME).oldValue("Sędzia Anna Maria").newValue("Anna Maria").build();
        
    }
    
    
    //--- update
    
    @Test
    public void createUpdate() {
        
        // execute
        
        JudgmentCorrection judgmentCorrection = createFor(judgment).update(judge).property(NAME).oldValue("Sędzia Anna Maria").newValue("Anna Maria").build();
        
        // assert
        
        assertTrue(judgment == judgmentCorrection.getJudgment());
        assertEquals(ChangeOperation.UPDATE, judgmentCorrection.getChangeOperation());
        assertEquals(Judge.class, judgmentCorrection.getCorrectedObjectClass());
        assertEquals(new Integer(judge.getId()), judgmentCorrection.getCorrectedObjectId());
        assertEquals(NAME, judgmentCorrection.getCorrectedProperty());
        assertEquals("Sędzia Anna Maria", judgmentCorrection.getOldValue());
        assertEquals("Anna Maria", judgmentCorrection.getNewValue());
        
    }

    
    @Test(expected=IllegalStateException.class)
    public void build_update_noCorrectedProperty() {
     
        createFor(judgment).update(judge).oldValue("!!!").newValue("gffg").build();
        
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_update_noNewValue() {
     
        createFor(judgment).update(judge).property(NAME).oldValue("sdsdsd").newValue(null).build();
        
    }
    

    //--- create
    
    @Test
    public void createCreate() {
        
        // execute
        
        JudgmentCorrection judgmentCorrection = createFor(judgment).create(judge).oldValue("Sędzia Anna Maria").newValue("Anna Maria").build();
        
        // assert
        
        assertTrue(judgment == judgmentCorrection.getJudgment());
        assertEquals(ChangeOperation.CREATE, judgmentCorrection.getChangeOperation());
        assertEquals(Judge.class, judgmentCorrection.getCorrectedObjectClass());
        assertEquals(new Integer(judge.getId()), judgmentCorrection.getCorrectedObjectId());
        assertNull(judgmentCorrection.getCorrectedProperty());
        assertEquals("Sędzia Anna Maria", judgmentCorrection.getOldValue());
        assertEquals("Anna Maria", judgmentCorrection.getNewValue());
        
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void build_create_createdObjectIsJudgment() {
        
        createFor(judgment).create(judgment).oldValue("!!!").newValue(null).build();
        
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_create_noNewValue() {
     
        createFor(judgment).create(judge).oldValue("sdsdsd").newValue(null).build();
        
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_create_correctedObjectIdNotNull() {
     
        createFor(judgment).create(new Judge("sds")).oldValue("sdsdsd").newValue("fdfd").build();
        
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_create_correctedPropertyNotNull() {
     
        createFor(judgment).create(judge).property(NAME).oldValue("!!!").newValue("ddsds").build();
        
    }
    
    
    
    //--- delete
    
    
    @Test
    public void createDelete() {
        
        // execute
        
        JudgmentCorrection judgmentCorrection = createFor(judgment).delete(Judge.class).oldValue("!!!").newValue(null).build();
        
        // assert
        
        assertTrue(judgment == judgmentCorrection.getJudgment());
        assertEquals(ChangeOperation.DELETE, judgmentCorrection.getChangeOperation());
        assertEquals(Judge.class, judgmentCorrection.getCorrectedObjectClass());
        assertNull(judgmentCorrection.getCorrectedObjectId());
        assertNull(judgmentCorrection.getCorrectedProperty());
        assertEquals("!!!", judgmentCorrection.getOldValue());
        assertNull(judgmentCorrection.getNewValue());
        
    }
  
    
    @Test(expected=IllegalStateException.class)
    public void build_delete_correctedPropertyNotNull() {
     
        createFor(judgment).delete(Judge.class).property(NAME).oldValue("!!!").newValue(null).build();
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_delete_newValueNotEmpty() {
     
        createFor(judgment).delete(Judge.class).oldValue(" ss ").newValue("dd").build();
        
    }
    
}
