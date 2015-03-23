package pl.edu.icm.saos.importer.common.correction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createCreate;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionConverterTest {

    
    private ImportCorrectionConverter correctionConverter = new ImportCorrectionConverter();
    
    
    
    private Judgment judgment = new SupremeCourtJudgment();
    
    private Judge judge = new Judge("JanNowak");
    
    
    
    @Before
    public void before() {
        
        Whitebox.setInternalState(judge, "id", 5);
        Whitebox.setInternalState(judgment, "id", 15);
        
        judgment.setJudgmentType(JudgmentType.REGULATION);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected=NullPointerException.class)
    public void convertToJudgmentCorrection_nullJudgment() {
        
        // given
        
        String oldJudgmentType = "XXX";
        
        ImportCorrection correctionJudgmentType = createUpdate(null).ofProperty(JUDGMENT_TYPE).oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name()).build();
        
        
        // execute
        
        correctionConverter.convertToJudgmentCorrection(null, correctionJudgmentType);
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void convertToJudgmentCorrection_nullImportCorrection() {
        
        // execute
        
        correctionConverter.convertToJudgmentCorrection(judgment, null);
        
    }

    
    @Test
    public void convertToJudgmentCorrection_Update_Judge() {
        
        // given
        
        String oldJudgeName = "Sédzia Jan Nowak";
                
        ImportCorrection correctionJudgeName = createUpdate(judge).ofProperty(NAME).oldValue(oldJudgeName).newValue(judge.getName()).build();

        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgeName);
        
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrection = JudgmentCorrectionBuilder.createFor(judgment)
                                                            .update(judge)
                                                            .property(NAME)
                                                            .oldValue(oldJudgeName).newValue(judge.getName())
                                                            .build();
        
        assertEquals(expectedJudgmentCorrection, judgmentCorrection);
    }
    
    
    @Test
    public void convertToJudgmentCorrection_Update_Judgment() {
        
        // given
        
        String oldJudgmentType = "XXX";
        
        ImportCorrection correctionJudgmentType = createUpdate(judgment).ofProperty(JUDGMENT_TYPE).oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name()).build();
        
        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgmentType);
       
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrection = JudgmentCorrectionBuilder.createFor(judgment)
                .update(judgment)
                .property(JUDGMENT_TYPE)
                .oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name())
                .build();

        assertEquals(expectedJudgmentCorrection, judgmentCorrection);
        
    }
    
    
    @Test
    public void convertToJudgmentCorrection_Update_null() {
        
        // given
        
        String oldJudgmentType = "XXX";
        
        ImportCorrection correctionJudgmentType = createUpdate(null).ofProperty(JUDGMENT_TYPE).oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name()).build();
        
        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgmentType);
       
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrection = JudgmentCorrectionBuilder.createFor(judgment)
                .update(judgment)
                .property(JUDGMENT_TYPE)
                .oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name())
                .build();

        assertEquals(expectedJudgmentCorrection, judgmentCorrection);
        
    }
    
    
    @Test
    public void convertToJudgmentCorrection_Create_Judge() {
        
        // given
        
        String oldJudgeName = "Sédzia Jan Nowak";
                
        ImportCorrection correctionJudgeName = createCreate(judge).oldValue(oldJudgeName).newValue(judge.getName()).build();

        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgeName);
        
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrection = JudgmentCorrectionBuilder.createFor(judgment)
                                                            .create(judge)
                                                            .oldValue(oldJudgeName).newValue(judge.getName())
                                                            .build();
        
        assertEquals(expectedJudgmentCorrection, judgmentCorrection);
    }

    
    @Test
    public void convertToJudgmentCorrection_Delete_Judge() {
        
        // given
        
        String oldJudgeName = "Sédzia Jan Nowak";
                
        ImportCorrection correctionJudgeName = ImportCorrectionBuilder.createDelete(Judge.class).oldValue(oldJudgeName).newValue(null).build();

        
        // execute
        
        JudgmentCorrection judgmentCorrection = correctionConverter.convertToJudgmentCorrection(judgment, correctionJudgeName);
        
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrection = JudgmentCorrectionBuilder.createFor(judgment)
                                                            .delete(Judge.class)
                                                            .oldValue(oldJudgeName).newValue(null)
                                                            .build();
        
        assertEquals(expectedJudgmentCorrection, judgmentCorrection);
    }
    
    
    @Test
    public void convertToJudgmentCorrections() {
        
        // given
        
        String oldJudgeName = "Sédzia Jan Nowak";
        
        Judge judge = new Judge("JanNowak");
        Whitebox.setInternalState(judge, "id", 5);

        ImportCorrection correctionJudgeName = createUpdate(judge).ofProperty(NAME).oldValue(oldJudgeName).newValue(judge.getName()).build();

        
        String oldJudgmentType = "XXX";
        
        ImportCorrection correctionJudgmentType = createUpdate(judgment).ofProperty(JUDGMENT_TYPE).oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name()).build();
        
        
        // execute
        
        List<JudgmentCorrection> judgmentCorrections = correctionConverter.convertToJudgmentCorrections(judgment, Lists.newArrayList(correctionJudgeName, correctionJudgmentType));
       
        
        // assert
        
        JudgmentCorrection expectedJudgmentCorrectionJudgeName = createFor(judgment)
                .update(judge)
                .property(NAME)
                .oldValue(oldJudgeName).newValue(judge.getName())
                .build();
        
        
        JudgmentCorrection expectedJudgmentCorrectionJudgmentType = createFor(judgment)
                .update(judgment)
                .property(JUDGMENT_TYPE)
                .oldValue(oldJudgmentType).newValue(judgment.getJudgmentType().name())
                .build();

        assertEquals(2, judgmentCorrections.size());
        
        assertThat(judgmentCorrections, Matchers.containsInAnyOrder(expectedJudgmentCorrectionJudgeName, expectedJudgmentCorrectionJudgmentType));
    }
    
    
    
 
    
}
