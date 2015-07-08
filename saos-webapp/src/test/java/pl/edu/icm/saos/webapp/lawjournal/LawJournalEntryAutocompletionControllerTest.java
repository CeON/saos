package pl.edu.icm.saos.webapp.lawjournal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.webapp.WebappTestSupport;

import com.google.common.collect.Lists;


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
    
    
    @Autowired
    private WebApplicationContext webApplicationCtx;
    
    private MockMvc mockMvc;
    
    private List<LawJournalEntry> lawJournalEntryList;
    
    private static final String PATH = "/search/lawJournalEntries";
    
    
    @Before
    public void setUp() throws Exception {
        
		lawJournalEntryList = createLawJournalEntryTestData();
		
		lawJournalEntryRepository.save(lawJournalEntryList);
		
		
		mockMvc = webAppContextSetup(webApplicationCtx)
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
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$.[0].id").value(equalsLong(lawJournalEntryList.get(0).getId())))
			.andExpect(jsonPath("$.[0].year").value(lawJournalEntryList.get(0).getYear()))
			.andExpect(jsonPath("$.[0].journalNo").value(lawJournalEntryList.get(0).getJournalNo()))
			.andExpect(jsonPath("$.[0].entry").value(lawJournalEntryList.get(0).getEntry()))
			.andExpect(jsonPath("$.[0].title").value(lawJournalEntryList.get(0).getTitle()));
        
    }
    
    @Test
    public void lawJournalSearch_FOUND_THREE() throws Exception {
        
        // execute
        
        ResultActions result = mockMvc.perform(get(PATH)
                .param("year", "1964")
                .param("pageSize", "1")
                .param("pageNumber", "1"));
        
        
        // assert
        
        result
			.andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$.[0].id").value(equalsLong(lawJournalEntryList.get(1).getId())))
            .andExpect(jsonPath("$.[0].year").value(lawJournalEntryList.get(1).getYear()))
            .andExpect(jsonPath("$.[0].journalNo").value(lawJournalEntryList.get(1).getJournalNo()))
            .andExpect(jsonPath("$.[0].entry").value(lawJournalEntryList.get(1).getEntry()))
            .andExpect(jsonPath("$.[0].title").value(lawJournalEntryList.get(1).getTitle()));
        
    }
    
    @Test
    public void lawJournalSearch_NOT_FOUND() throws Exception {
        
        // execute
        ResultActions result = mockMvc.perform(get(PATH).param("text", "dostawa"));
        
        // assert
        result.andExpect(jsonPath("$", is(empty())));
        
    }
    
    @Test
    public void lawJournalSearch_TOO_BIG_PAGE_SIZE() throws Exception {
        
        // execute
        ResultActions result = mockMvc.perform(get(PATH)
                .param("pageSize", "101"));
        
        // assert
        result
            .andExpect(view().name("wrongParamValue"))
            .andExpect(status().isBadRequest());
        
        assertWrongParamValueModel(result, "pageSize", "error.wrongParamValue.pageSize.tooBig", arrayContaining(100));
    }
    
    @Test
    public void lawJournalSearch_NEGATIVE_PAGE_SIZE() throws Exception {
        
        // execute
        ResultActions result = mockMvc.perform(get(PATH)
                .param("pageSize", "-1"));
        
        // assert
        result
            .andExpect(view().name("wrongParamValue"))
            .andExpect(status().isBadRequest());
        
        assertWrongParamValueModel(result, "pageSize", "error.wrongParamValue.pageSize.negative", emptyArray());
    }
    
    @Test
    public void lawJournalSearch_NEGATIVE_PAGE_NUMBER() throws Exception {
        
        // execute
        ResultActions result = mockMvc.perform(get(PATH)
                .param("pageNumber", "-1"));
        
        // assert
        result
            .andExpect(view().name("wrongParamValue"))
            .andExpect(status().isBadRequest());
        
        assertWrongParamValueModel(result, "pageNumber", "error.wrongParamValue.pageNumber.negative", emptyArray());
    }

    //------------------------ private --------------------------    
    
    private void assertWrongParamValueModel(ResultActions result, String paramName, String errorDetailsMessageCode, Matcher<Object[]> errorDetailsMessageArgsMatcher) throws Exception {
        
        result.andExpect(model().attribute("exception",
                allOf(
                        hasProperty("paramName", is(paramName)),
                        hasProperty("errorDetailsMessageCode", is(errorDetailsMessageCode)),
                        hasProperty("errorDetailsMessageArgs", errorDetailsMessageArgsMatcher)
                )
        ));
    }
    
    private List<LawJournalEntry> createLawJournalEntryTestData() {
    	LawJournalEntry lawJournalEntryOne = new LawJournalEntry(1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego");
    	LawJournalEntry lawJournalEntryTwo = new LawJournalEntry(1964, 42, 291, "Ustawa z dnia 15 wrzesnia 1964 r. - Kodeks spolek handlowych");
    	LawJournalEntry lawJournalEntryThree = new LawJournalEntry(1964, 41, 292, "Ustawa z dnia 21 lutego 1964 r. - Przepisy wprowadzające ustawę o Krajowym Rejestrze Sądowym");
    	
    	return Lists.newArrayList(lawJournalEntryOne, lawJournalEntryTwo, lawJournalEntryThree);
    }
}
