package pl.edu.icm.saos.enrichment.apply.refregulations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.common.json.JsonNormalizer;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

/**
 * @author madryk
 */
public class ReferencedRegulationsTagValueParserTest {

    private final String jsonTagValue = JsonNormalizer.normalizeJson("[" + 
            "{" + 
                "journalTitle : 'Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych'," +
                "journalNo : 162," +
                "journalYear : 1998," +
                "journalEntry : 1118," +
                "text : 'Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych (Dz. U. z 1998 r. Nr 162, poz. 1118 - art. 103; art. 2 a)'" +
            "}," +
            "{" + 
                "journalTitle : 'Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego'," +
                "journalNo : 43," +
                "journalYear : 1964," +
                "journalEntry : 296," +
                "text : 'Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 102)'" +
            "}]");
    
    
    private JsonStringParser<ReferencedRegulationsTagValueItem[]> jsonStringParser = new JsonStringParser<>(ReferencedRegulationsTagValueItem[].class);
    
    
    @Before
    public void before() {

        jsonStringParser.setJsonFactory(new MappingJsonFactory());
        
        jsonStringParser.setCommonValidator(mock(CommonValidator.class));
    
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void parseAndValidate() throws JsonParseException {
        
        // execute
        ReferencedRegulationsTagValueItem[] referencedRegulationsTagValueItems = jsonStringParser.parseAndValidate(jsonTagValue);
        
        // assert
        assertNotNull(referencedRegulationsTagValueItems);
        assertEquals(2, referencedRegulationsTagValueItems.length);
        
        ReferencedRegulationsTagValueItem refRegulationsItem1 = referencedRegulationsTagValueItems[0];
        assertEquals(1998, refRegulationsItem1.getJournalYear());
        assertEquals(162, refRegulationsItem1.getJournalNo());
        assertEquals(1118, refRegulationsItem1.getJournalEntry());
        assertEquals("Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych", refRegulationsItem1.getJournalTitle());
        assertEquals("Ustawa z dnia 17 grudnia 1998 r. o emeryturach i rentach z Funduszu Ubezpieczeń Społecznych (Dz. U. z 1998 r. Nr 162, poz. 1118 - art. 103; art. 2 a)", refRegulationsItem1.getText());
        
        ReferencedRegulationsTagValueItem refRegulationsItem2 = referencedRegulationsTagValueItems[1];
        assertEquals(1964, refRegulationsItem2.getJournalYear());
        assertEquals(43, refRegulationsItem2.getJournalNo());
        assertEquals(296, refRegulationsItem2.getJournalEntry());
        assertEquals("Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego", refRegulationsItem2.getJournalTitle());
        assertEquals("Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296 - art. 102)", refRegulationsItem2.getText());
        
    }
}
