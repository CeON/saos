package pl.edu.icm.saos.webapp.lawjournal;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.webapp.WebappTestSupport;


/**
 * @author madryk
 */
@WebAppConfiguration
@Category(SlowTest.class)
public class LawJournalEntryAutocompletionControllerTest extends WebappTestSupport {

    @Autowired
    private LawJournalEntryAutocompletionController lawJournalController;
    
    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    private MockMvc mockMvc;
    
    
    private LawJournalEntry lawJournalEntry;
    
    private static final String PATH = "/lawjournal/search";
    
    
    @Before
    public void setUp() throws Exception {
        
        lawJournalEntry = new LawJournalEntry(1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego");
        lawJournalEntryRepository.save(lawJournalEntry);
        
        mockMvc = standaloneSetup(lawJournalController)
                .build();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void lawJournalSearch_FOUND() throws Exception {
        
        // execute
        
        ResultActions result = mockMvc.perform(get(PATH)
                .param("year", "1964")
                .param("journalNo", "43")
                .param("entry", "296")
                .param("text", "ustawa"));
        
        
        // assert
        
        result
            .andExpect(jsonPath("$.[0].id").value(equalsLong(lawJournalEntry.getId())))
            .andExpect(jsonPath("$.[0].year").value(lawJournalEntry.getYear()))
            .andExpect(jsonPath("$.[0].journalNo").value(lawJournalEntry.getJournalNo()))
            .andExpect(jsonPath("$.[0].entry").value(lawJournalEntry.getEntry()))
            .andExpect(jsonPath("$.[0].title").value(lawJournalEntry.getTitle()));
        
    }
    
    @Test
    public void lawJournalSearch_NOT_FOUND() throws Exception {
        
        // execute
        ResultActions result = mockMvc.perform(get(PATH).param("text", "dostawa"));
        
        // assert
        result.andExpect(jsonPath("$", is(empty())));
        
    }
    
    
    
}
