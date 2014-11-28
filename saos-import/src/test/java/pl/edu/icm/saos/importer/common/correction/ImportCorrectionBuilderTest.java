package pl.edu.icm.saos.importer.common.correction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createCreate;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createDelete;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.correction.model.ChangeOperation;
import pl.edu.icm.saos.persistence.model.Judge;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionBuilderTest {

    
    
    private Judge judge = new Judge("Jan Nowak");
    
    
    {
        Whitebox.setInternalState(judge, "id", 56);
    }
    
 
    
    
    //------------------------ TESTS --------------------------
    
    
    
    //--- update
    
    
    @Test
    public void createUpdate_ok() {
        
        // execute
        
        ImportCorrection importCorrection = createUpdate(judge).ofProperty(NAME).oldValue("Sędzia Anna Maria").newValue("Anna Maria").build();
        
        // assert
        
        assertEquals(ChangeOperation.UPDATE, importCorrection.getChangeOperation());
        assertEquals(judge, importCorrection.getCorrectedObject());
        assertEquals(NAME, importCorrection.getCorrectedProperty());
        assertEquals("Sędzia Anna Maria", importCorrection.getOldValue());
        assertEquals("Anna Maria", importCorrection.getNewValue());
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_update_noCorrectedProperty() {
     
        createUpdate(judge).oldValue("!!!").newValue("dddd").build();
        
    }
   
    
    @Test(expected=IllegalStateException.class)
    public void build_update_noNewValue() {
     
        createUpdate(judge).ofProperty(NAME).oldValue("sdsdsd").newValue(null).build();
        
    }
    

    //--- create
    
    @Test
    public void createCreate_ok() {
        
        // execute
        
        ImportCorrection importCorrection = createCreate(judge).oldValue("Sędzia Anna Maria").newValue("Anna Maria").build();
        
        // assert
        
        assertEquals(ChangeOperation.CREATE, importCorrection.getChangeOperation());
        assertEquals(judge, importCorrection.getCorrectedObject());
        assertEquals("Sędzia Anna Maria", importCorrection.getOldValue());
        assertEquals("Anna Maria", importCorrection.getNewValue());
        
    }
    
    @Test(expected=NullPointerException.class)
    public void createCreate_nullCreatedObject() {
        
        createCreate(null);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_create_noNewValue() {
     
        createCreate(judge).oldValue("sdsdsd").newValue(null).build();
        
    }
    
       
    @Test(expected=IllegalStateException.class)
    public void build_create_correctedPropertyNotNull() {
     
        createCreate(judge).ofProperty(NAME).oldValue("!!!").newValue("ddsds").build();
        
    }
    
    
    
    //--- delete
    
    @Test
    public void createDelete_ok() {
        
        // execute
        
        ImportCorrection importCorrection = createDelete(Judge.class).oldValue("Sędzia Anna Maria").newValue(null).build();
        
        // assert
        
        assertEquals(ChangeOperation.DELETE, importCorrection.getChangeOperation());
        assertNull(importCorrection.getCorrectedObject());
        assertNull(importCorrection.getCorrectedProperty());
        assertEquals(Judge.class, importCorrection.getDeletedObjectClass());
        assertEquals("Sędzia Anna Maria", importCorrection.getOldValue());
        assertNull(importCorrection.getNewValue());
        
    }

    
    @Test(expected=NullPointerException.class)
    public void createDelete_nullDeletedObjectClass() {
        
        createDelete(null);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_delete_correctedPropertyNotNull() {
     
        createDelete(Judge.class).ofProperty(NAME).oldValue("!!!").newValue(null).build();
        
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void build_delete_newValueNotEmpty() {
     
        createDelete(Judge.class).oldValue(" ss ").newValue("dd").build();
        
    }
    

    
}
