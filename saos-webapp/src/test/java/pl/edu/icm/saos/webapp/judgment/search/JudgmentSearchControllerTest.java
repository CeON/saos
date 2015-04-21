package pl.edu.icm.saos.webapp.judgment.search;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;
import pl.edu.icm.saos.webapp.WebappTestConfiguration;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;
import pl.edu.icm.saos.webapp.court.SimpleEntity;
import pl.edu.icm.saos.webapp.court.TestCourtsFactory;
import pl.edu.icm.saos.webapp.judgment.JudgmentCriteriaForm;

import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebappTestConfiguration.class)
@Category(SlowTest.class)
public class JudgmentSearchControllerTest {

	
	@Autowired
	private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
	@Autowired
	@InjectMocks
	private JudgmentSearchController judgmentSearchController;
	
	@Mock
	private ScJudgmentFormRepository scJudgmentFormRepository;
	
	@Mock
	private CcListService ccListService;
	
	@Mock
	private ScListService scListService;
	
	@Mock
	private JudgmentWebSearchService judgmentsWebSearchService;
	
	@Mock
	private LawJournalEntryRepository lawJournalEntryRepository;
	
	@Mock
	private JudgmentRepository judgmentRepository;
	
	@Autowired
	private TestCourtsFactory testCourtsFactory;
	
	private List<SimpleEntity> simpleEntities;
	
	private List<SimpleEntity> commonCourts = getTestCommonCourts();
	
	private List<SimpleEntity> scChambers = getTestScChamber();
	
	private List<SupremeCourtJudgmentForm> scJudgmentForms = getTestScJudgmentForm();
	
	private SearchResults<JudgmentSearchResult> results;
	
	private LawJournalEntry lawJournalEntry = getTestLawJournalEntry();
    
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		simpleEntities = testCourtsFactory.getSimpleEntities();
		
		results = new SearchResults<JudgmentSearchResult>();
		results.addResult(new JudgmentSearchResult());
		
		when(judgmentsWebSearchService.search(Mockito.any(JudgmentCriteriaForm.class), Mockito.any(Pageable.class))).thenReturn(results);
		
		when(ccListService.findCommonCourts()).thenReturn(commonCourts);
		when(scListService.findScChambers()).thenReturn(scChambers);
		when(scJudgmentFormRepository.findAll()).thenReturn(scJudgmentForms);
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void judgmentSearchResults_empty_search_form() throws Exception {
	
		
		mockMvc.perform(get("/search"))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentSearch"))
			.andExpect(model().attribute("pageable", instanceOf(Pageable.class)))
			.andExpect(model().attribute("searchResults", results))
			;
		

            verify(ccListService, times(0)).findCommonCourts();
            verify(scListService, times(0)).findScChambers();
            verify(scJudgmentFormRepository, times(0)).findAll();
	}
	
	@Test
	public void judgmentSearchResults_search_form_contains_commonCourtId() throws Exception {

		Integer commonCourtId = 1;
		when(ccListService.findCcDivisions(commonCourtId)).thenReturn(simpleEntities);
		
		mockMvc.perform(get("/search").param("commonCourtId", commonCourtId.toString()).param("courtType", "COMMON"))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentSearch"))
			.andExpect(model().attribute("commonCourtDivisions", simpleEntities))
			.andExpect(model().attribute("commonCourtDivisions", hasSize(2)))
			.andExpect(model().attribute("commonCourtDivisions", hasItem(
                            allOf(
                    		hasProperty("id", is(simpleEntities.get(0).getId())),
                                hasProperty("name", is(simpleEntities.get(0).getName()))
                            )
			)))
			.andExpect(model().attribute("commonCourtDivisions", hasItem(
                            allOf(
                    		hasProperty("id", is(simpleEntities.get(1).getId())),
                                hasProperty("name", is(simpleEntities.get(1).getName()))
                            )
			)))
			;
		
		verify(ccListService, times(1)).findCcDivisions(commonCourtId);
	}
	
	@Test
	public void judgmentSearchResults_search_form_contains_supremeChamberId() throws Exception {
		
		Integer supremeChamberId = 1;
		when(scListService.findScChamberDivisions(supremeChamberId)).thenReturn(simpleEntities);
		
		mockMvc.perform(get("/search").param("supremeChamberId", supremeChamberId.toString()).param("courtType", "SUPREME"))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentSearch"))
			.andExpect(model().attribute("supremeChamberDivisions", simpleEntities))
			.andExpect(model().attribute("supremeChamberDivisions", hasSize(2)))
			.andExpect(model().attribute("supremeChamberDivisions", hasItem(
                            allOf(
                        	hasProperty("id", is(simpleEntities.get(0).getId())),
                                hasProperty("name", is(simpleEntities.get(0).getName()))
                            )
			)))
                	.andExpect(model().attribute("supremeChamberDivisions", hasItem(
                            allOf(
                        	hasProperty("id", is(simpleEntities.get(1).getId())),
                                hasProperty("name", is(simpleEntities.get(1).getName()))
                            )
                	)))
			;
		
		verify(scListService, times(1)).findScChamberDivisions(supremeChamberId);
	}
	
	@Test
	public void judgmentSearchResults_search_form_scJudgmentForm() throws Exception {
		

		
		mockMvc.perform(get("/search").param("courtType", "SUPREME"))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentSearch"))
			.andExpect(model().attribute("scJudgmentForms", scJudgmentForms))
			.andExpect(model().attribute("scJudgmentForms", hasSize(2)))
			.andExpect(model().attribute("scJudgmentForms", hasItem(
                            allOf(
                                hasProperty("name", is(scJudgmentForms.get(0).getName()))
                            )
			)))
                	.andExpect(model().attribute("scJudgmentForms", hasItem(
                            allOf(
                                hasProperty("name", is(scJudgmentForms.get(1).getName()))
                            )
                	)))
			;
		
		verify(scJudgmentFormRepository, times(1)).findAll();
	}
	
	
	@Test
	public void judgmentSearchResults_search_form_contains_lawJournalEntryId() throws Exception {
		
		Long lawJournalEntryId = 18l;
		
		when(lawJournalEntryRepository.findOne(lawJournalEntryId)).thenReturn(lawJournalEntry);
		
		mockMvc.perform(get("/search").param("lawJournalEntryId", lawJournalEntryId.toString()))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentSearch"))
			.andExpect(model().attribute("lawJournalEntry", lawJournalEntry))
			;
		
		verify(lawJournalEntryRepository, times(1)).findOne(lawJournalEntryId);
	}
	
	@Test
	public void judgmentSearchResults_search_form_contains_referenced_judgment() throws Exception {
	    Judgment judgment = TestInMemoryObjectFactory.createSimpleCcJudgment();
	    when(judgmentRepository.findOneAndInitialize(12L)).thenReturn(judgment);
	    
	    ResultActions result = mockMvc.perform(get("/search").param("referencedCourtCaseId", "12"));
	    
	    result
	        .andExpect(status().isOk())
	        .andExpect(view().name("judgmentSearch"))
	        .andExpect(model().attribute("referencedJudgment", is(judgment)
	        ));
	}
	
	//------------------------ PRIVATE --------------------------
	
	private List<SimpleEntity> getTestCommonCourts() {
	    	SimpleEntity commonCourtOne = new SimpleEntity();
	    	SimpleEntity commonCourtTwo = new SimpleEntity();
		
		commonCourtOne.setId(13);
		commonCourtOne.setName("Sąd Rejonowy w Radomiu");
		
		commonCourtOne.setId(14);
		commonCourtTwo.setName("Sąd Okręgowy w Toruniu");
		
		return Lists.newArrayList(commonCourtOne, commonCourtTwo);
	}
	
	private List<SimpleEntity> getTestScChamber() {
	    	SimpleEntity scChamberOne = new SimpleEntity();
	    	SimpleEntity scChamberTwo = new SimpleEntity();
		
	    	scChamberOne.setId(8);
		scChamberOne.setName("Izba Cywilna");
		
		scChamberTwo.setId(43);
		scChamberTwo.setName("Izba Karna");
		
		return Lists.newArrayList(scChamberOne, scChamberTwo);
	}

	private List<SupremeCourtJudgmentForm> getTestScJudgmentForm() {
		
		SupremeCourtJudgmentForm scJudgmentFormOne = new SupremeCourtJudgmentForm();
		SupremeCourtJudgmentForm scJudgmentFormTwo = new SupremeCourtJudgmentForm();
		
		scJudgmentFormOne.setName("wyrok SN");
		scJudgmentFormTwo.setName("uchwała SN");
		
		return Lists.newArrayList(scJudgmentFormOne, scJudgmentFormTwo);
	}
	
	private LawJournalEntry getTestLawJournalEntry() {
		return new LawJournalEntry(2000, 1, 18, "Ustawa karna i cywilna");
	}
}
