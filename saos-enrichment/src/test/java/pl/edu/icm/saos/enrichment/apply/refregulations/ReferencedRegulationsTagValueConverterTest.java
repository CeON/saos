package pl.edu.icm.saos.enrichment.apply.refregulations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
public class ReferencedRegulationsTagValueConverterTest {

    private ReferencedRegulationsTagValueConverter referencedRegulationsTagValueConverter = new ReferencedRegulationsTagValueConverter();
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void convert_NULL_VALUE_ITEMS() {
        
        // execute
        referencedRegulationsTagValueConverter.convert(null);
    }
    
    @Test
    public void convert() {
        
        // given
        ReferencedRegulationsTagValueItem refRegulationItem1 = new ReferencedRegulationsTagValueItem();
        refRegulationItem1.setJournalYear(1964);
        refRegulationItem1.setJournalNo(43);
        refRegulationItem1.setJournalEntry(296);
        refRegulationItem1.setJournalTitle("Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego");
        refRegulationItem1.setText("Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 391; art. 98)");
        
        ReferencedRegulationsTagValueItem refRegulationItem2 = new ReferencedRegulationsTagValueItem();
        refRegulationItem2.setJournalYear(1999);
        refRegulationItem2.setJournalNo(83);
        refRegulationItem2.setJournalEntry(930);
        refRegulationItem2.setJournalTitle("Ustawa z dnia 10 września 1999 r. - Kodeks karny skarbowy");
        refRegulationItem2.setText("Ustawa z dnia 10 września 1999 r. - Kodeks karny skarbowy (Dz. U. z 1999 r. Nr 83, poz. 930 - art. 107; art. 107 § 1)");
        
        ReferencedRegulationsTagValueItem[] refRegulationsTagValueItems = new ReferencedRegulationsTagValueItem[] { refRegulationItem1, refRegulationItem2 };
                
        
        // execute
        List<JudgmentReferencedRegulation> referencedRegulations = referencedRegulationsTagValueConverter.convert(refRegulationsTagValueItems);
        
        
        // assert
        assertEquals(2, referencedRegulations.size());
        
        
        JudgmentReferencedRegulation referencedRegulation1 = referencedRegulations.get(0);
        LawJournalEntry lawJournalEntry1 = referencedRegulation1.getLawJournalEntry();
        
        assertEquals(refRegulationItem1.getJournalYear(), lawJournalEntry1.getYear());
        assertEquals(refRegulationItem1.getJournalNo(), lawJournalEntry1.getJournalNo());
        assertEquals(refRegulationItem1.getJournalEntry(), lawJournalEntry1.getEntry());
        assertEquals(refRegulationItem1.getJournalTitle(), lawJournalEntry1.getTitle());
        assertEquals(refRegulationItem1.getText(), referencedRegulation1.getRawText());
        
        assertTrue(referencedRegulation1.isGenerated());
        
        
        JudgmentReferencedRegulation referencedRegulation2 = referencedRegulations.get(1);
        LawJournalEntry lawJournalEntry2 = referencedRegulation2.getLawJournalEntry();
        
        assertEquals(refRegulationItem2.getJournalYear(), lawJournalEntry2.getYear());
        assertEquals(refRegulationItem2.getJournalNo(), lawJournalEntry2.getJournalNo());
        assertEquals(refRegulationItem2.getJournalEntry(), lawJournalEntry2.getEntry());
        assertEquals(refRegulationItem2.getJournalTitle(), lawJournalEntry2.getTitle());
        assertEquals(refRegulationItem2.getText(), referencedRegulation2.getRawText());
        
        assertTrue(referencedRegulation2.isGenerated());
        
        
    }
}
