package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.process;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class CtSpecificJudgmentOverwriterTest {

    private CtSpecificJudgmentOverwriter ctSpecificJudgmentOverwriter = new CtSpecificJudgmentOverwriter();
    
    private ConstitutionalTribunalJudgment oldJudgment = new ConstitutionalTribunalJudgment();
    
    private ConstitutionalTribunalJudgment newJudgment = new ConstitutionalTribunalJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void overrideJudgment_DISSENTING_OPINIONS() {
        
        // given
        
        ConstitutionalTribunalJudgmentDissentingOpinion oldOpinion1 = 
                createDissentingOpinion(Lists.newArrayList("Jan Kowalski", "Adam Nowak"), "abc");
        
        ConstitutionalTribunalJudgmentDissentingOpinion oldOpinion2 = 
                createDissentingOpinion(Lists.newArrayList("Jacek Zielinski"), "def");
        
        ConstitutionalTribunalJudgmentDissentingOpinion oldOpinion3 = 
                createDissentingOpinion(Lists.newArrayList("Katarzyna Kamińska"), "ghi");
        
        
        oldJudgment.addDissentingOpinion(oldOpinion1);
        oldJudgment.addDissentingOpinion(oldOpinion2);
        oldJudgment.addDissentingOpinion(oldOpinion3);
        
        ConstitutionalTribunalJudgmentDissentingOpinion newOpinion1 = 
                createDissentingOpinion(Lists.newArrayList("Jan Kowalski"), "abc");
        
        ConstitutionalTribunalJudgmentDissentingOpinion newOpinion2 = 
                createDissentingOpinion(Lists.newArrayList("Jacek Zieliński"), "def");
        
        ConstitutionalTribunalJudgmentDissentingOpinion newOpinion3 = 
                createDissentingOpinion(Lists.newArrayList("Katarzyna Kamińska"), "ghi");
        
        newJudgment.addDissentingOpinion(newOpinion1);
        newJudgment.addDissentingOpinion(newOpinion2);
        newJudgment.addDissentingOpinion(newOpinion3);
        
        
        // execute
        
        ctSpecificJudgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        // assert
        
        assertThat(newJudgment.getDissentingOpinions(), containsInAnyOrder(newOpinion1, newOpinion2, newOpinion3));
        assertThat(oldJudgment.getDissentingOpinions(), containsInAnyOrder(newOpinion1, newOpinion2, oldOpinion3));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private ConstitutionalTribunalJudgmentDissentingOpinion createDissentingOpinion(List<String> authors, String textContent) {
        ConstitutionalTribunalJudgmentDissentingOpinion opinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        authors.forEach(author -> opinion.addAuthor(author));
        opinion.setTextContent(textContent);
        
        return opinion;
    }
}
